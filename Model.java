package swing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import swing.Controller.RefreshView;

public class Model {
	private static int localPort;
	//private static String localAddress;
	private static int remotePort;
	private static String remoteIP;
	private static InetSocketAddress remote;
	private static Charset charset;
	
	//reinitializable variables
	//private static ServerSocket serverSocket;
	private static ServerSocketChannel serverSocketChannel;
	//private static Socket socket;
	private static SocketChannel socketChannel;
	
	private static ConnectionController connectionController;
	private static Thread connectionController_Tr;
	private static Thread msgSender_Tr;
	private static Thread msgListener_Tr;
	private static boolean connectionControl;
	private static boolean msgSenderControl;
	private static boolean msgListenerControl;
	//private static boolean globalControl;
	private static int bufferSize;
	
	private static int status;
	/*
	 * 0:started
	 * 1:listening
	 * 2:send request
	 * 3:connection ready
	*/

	private static LinkedList linkedList;
	
	private static final String STATUS_ERR="Unknown status error occured.";
	private static final String CONNECTION_ERR="Unknown connection error occured. Reset status.";
	private static final String INVALID_IP_ERR="Invalid IP address.";
	private static final String INVALID_PORT_ERR="Invalid port, must be between 1024-65535.";
	private static final int BYTEBUFFER_CAPACITY=500;
	
	public static void initialize(){
		localPort=1520;
		remotePort=1520;
		//localAddress="127.0.0.1";
		remoteIP="127.0.0.2";
		charset=Charset.forName("UTF8");
		Controller.initialize();		
		
		setStatus(0);
		linkedList=new LinkedList();
		linkedList.first=null;
		linkedList.last=null;
		remote=new InetSocketAddress(remoteIP,remotePort);
		connectionController=new ConnectionController();
			
		System.out.println("Initialization completed.");
	}
	
	//connection module
	private static class ConnectionController implements Runnable{//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! bugs!
		public void run() {
			//phase 1: initialize socket
			int retry=0;
			Socket socket = null;
			ServerSocket serverSocket=null;
			try {
				serverSocketChannel=ServerSocketChannel.open();
				serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", localPort));
				serverSocket=serverSocketChannel.socket();
				serverSocket.setSoTimeout(1000);//set time out 1 sec
				//serverSocketChannel.configureBlocking(false);//non-blocking
				socketChannel=SocketChannel.open();
				//socketChannel.configureBlocking(false);
				socket=socketChannel.socket();
			} catch (IOException e1) {//test
				e1.printStackTrace();
			}
			connectionControl=true;
			while(true){//build up connection
				if(!connectionControl){//Interrupt
					reinitialize();
					return;
				}
		        try{//check status before next step
		            if(status==1)socketChannel=serverSocket.accept().getChannel();		            	
		            else if(status==2)socket.connect(remote);
		            else Controller.warningPane(STATUS_ERR, "Status error");
		            
		            if(socketChannel==null)throw new IOException();//catch if no connection
		            break;
	        	} catch (IOException e){
	        		if(status==1)/*try {*/
	        			System.out.println("trying...");//test
	        			//since serverSocket will time out, no need to set Thread.sleep
	        		else if(status==2&&retry<15){//listener can retry unlimited times, but sender can only retry 20 times for each send order
	        			try {
	    					Thread.sleep(1000);//sender retry every 1 sec
	    				} catch (InterruptedException e1) {
	    					e1.printStackTrace();
	    				} finally{
	    					retry++;
	    					//System.out.println(retry);//test
	    				}
	        		}
	        		else if(status==2&&retry==15){
	        			String errorMsg="Remote host \""+ remoteIP +":"+ remotePort +"\"unreachable";
	        			Controller.warningPane(errorMsg, "Connection error");
	        			setStatus(1);//reset status
	        			retry=0;
	        		}
	        		else Controller.warningPane(STATUS_ERR, "Status error");
				}
		    }
			
			//phase 2:initialize two threads
			if(socketChannel.isConnected()){
				setStatus(3);//initialize msgSender and msgListener
				try {
					remote=(InetSocketAddress)socketChannel.getRemoteAddress();
				} catch (IOException e) {
					e.printStackTrace();//test
				}
				remoteIP=remote.getHostName();
				remotePort=remote.getPort();
				try {
					socketChannel.configureBlocking(false);
				} catch (IOException e) {
					e.printStackTrace();//test
				}
				MsgSender msgSender=new MsgSender();
				MsgListener msgListener=new MsgListener();
				msgSender_Tr=new Thread(msgSender);
				msgListener_Tr=new Thread(msgListener);
				msgSender_Tr.start();
				msgListener_Tr.start();
				RefreshView.remoteInetSocketAddressRefresh(remoteIP, ""+remotePort, true);
			}
			else{
				Controller.warningPane(CONNECTION_ERR, "Connection error");
				setStatus(0);
				return;
			}
			
			//phase 3: maintaining network
			System.out.println("Maintaining");//test
			while(true){
				synchronized(socketChannel){
					System.out.println("Controller got socket channel. Connected:"+socketChannel.isConnected()+" ; blocked"+socketChannel.isBlocking());
					if(!connectionControl||bufferSize==-1){//if connectionControl is false, this thread will stop sender and listener
						System.out.println("closed1");
						reinitialize();
						System.out.println("closed2");
						break;
					}
					//socketChannel.notifyAll();
					try {
						socketChannel.wait(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();//test
					}
				}
				
			}//end of phase 3			
		}
	}
	
	private static class MsgSender implements Runnable{
		public void run() {
			ByteBuffer byteBuffer=ByteBuffer.allocate(BYTEBUFFER_CAPACITY);
			msgSenderControl=true;//
			while(msgSenderControl){
				System.out.println("sender is waiting for channel.S/L/G:"+msgSenderControl+","+msgListenerControl+","+connectionControl);//test
				if(!socketChannel.isConnected())Controller.warningPane(CONNECTION_ERR, "Connection error");
				else if(status!=3)Controller.warningPane(STATUS_ERR, "Status error");
				else synchronized(socketChannel){
					try {
						System.out.println("sender get socket channel");
						if(linkedList.first!=null){//send all cached strings, then flush
							String temp=readCache();
							if(temp.length()<BYTEBUFFER_CAPACITY){
								byteBuffer=charset.encode(temp);
								socketChannel.write(byteBuffer);
							}
							else {//capacity of bytebuffer is not capable to store all message at one time
								int splitLength=temp.length()/BYTEBUFFER_CAPACITY+1;
								for(int i=0,offset=0;i<splitLength;i++){
									offset=i*BYTEBUFFER_CAPACITY;
									if(i!=splitLength-1)byteBuffer=charset.encode(temp.substring(offset, offset+BYTEBUFFER_CAPACITY));
									else byteBuffer=charset.encode(temp.substring(offset, temp.length()));
									socketChannel.write(byteBuffer);
								}
							}
						}
						//if linkedList.first==null, nothing needs to be done
						//socketChannel.notifyAll();
						socketChannel.wait(2000);
					} catch (IOException e) {
						connectionControl=false;//stop looping when disconnected, this may happened when remote disconnected accidentally or manually
						System.out.println("sender found disconnection");
						break;
						//e.printStackTrace();//test
					} catch (InterruptedException e) {
						e.printStackTrace();//test
					}
				}
			}
			System.out.println("sender return");//test
		}
		
	}
	
	private static class MsgListener implements Runnable{
		public void run() {
			ByteBuffer byteBuffer=ByteBuffer.allocate(BYTEBUFFER_CAPACITY);
			CharBuffer charBuffer=null;
			String temp="";
			msgListenerControl=true;
			while(msgListenerControl){
				System.out.println("listener waiting for socket channel.S/L/G:"+msgSenderControl+","+msgListenerControl+","+connectionControl);
				if(!socketChannel.isConnected()){
					Controller.warningPane(CONNECTION_ERR, "Connection error");
					return;
				}
				else if(status!=3){
					Controller.warningPane(STATUS_ERR, "Status error");
					return;
				}
					
				else synchronized(socketChannel){
					System.out.println("listener get socket channel");
					try {
						bufferSize=socketChannel.read(byteBuffer);
						System.out.println("listener size"+ bufferSize);
						while(bufferSize>0)//reading
						{
							byteBuffer.flip();
							while(byteBuffer.hasRemaining())
							{
								charBuffer=charset.decode(byteBuffer);
								temp+=charBuffer.toString();
								//System.out.println(charBuffer);
							}
							byteBuffer.clear();
							bufferSize=socketChannel.read(byteBuffer);
						}
						//socketChannel.notifyAll();
						socketChannel.wait(2000);
						//try reading, if size==0 ,wait		
					} catch (IOException e) {
						connectionControl=false;//stop looping when disconnected, this may happened when remote disconnected accidentally or manually
						System.out.println("listener found disconnection");
						break;
						//e.printStackTrace();//test
					} catch (InterruptedException e) {
						e.printStackTrace();//test
					}
				}
				

				System.out.println(temp);//test
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!      callback method is at here      !!!!!!!!!!!!!!!!!!!!!!!!!!!!
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("listener return");//test
		}
		
	}
	
	
	// tools
	private static void setStatus(int status){
		Model.status=status;
		Controller.RefreshView.statusRefresh(Model.status);
	}
	
	private static void reinitialize(){//reset linkedList and socketChannels. Should be used when disconnected
		//And only be used by connection controller, do not use this method without synchronized(socketChannel) !!!!!
		try {
			serverSocketChannel.close();
			socketChannel.close();
		} catch (IOException e1) {
			System.out.println("Channel throws exception when closing");
			e1.printStackTrace();
		}
		serverSocketChannel=null;
		socketChannel=null;
		
		connectionController=null;
		connectionController_Tr=null;
		msgSender_Tr=null;
		msgListener_Tr=null;
		
		connectionControl=false;
		msgSenderControl=false;
		msgListenerControl=false;
		//globalControl=false;
		
		linkedList=new LinkedList();
		linkedList.first=null;
		linkedList.last=null;
		remote=new InetSocketAddress(remoteIP,remotePort);
		connectionController=new ConnectionController();
		setStatus(0);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();//test
		}//in case of some threads haven't finished
	}

	private static String readCache(){
		String result="";
		synchronized(linkedList){
			while(linkedList.first!=null){
				result+=linkedList.first.message+"\n";
				linkedList.first=linkedList.first.next;
				//dispose current cache, and link the linkedList.first pointer to the next cache
				//and if linkedList.first.next is null, the loop will be stopped at the beginning of next round
			}
			linkedList.last=null;//all caches are read and disposed, reset linkedList.last
		}
		return result;
	}
	
	//external interfaces
	public static void portListening(){//set status 1, and create a server socket to wait for connection requests
		if(status==0){
			setStatus(1);
			//
			
			connectionController_Tr=new Thread(connectionController);
			connectionController_Tr.start();
		}
        else if(status==1);//Do nothing for same status
        else if(status==2)setStatus(1);
        else Controller.warningPane(STATUS_ERR, "Status error");
	}
	
	public static void sendRequest(){//set status 2, and create a socket to try sending connection requests to remote host
		if(status==0){
			setStatus(2);
			//

			connectionController_Tr=new Thread(connectionController);
			connectionController_Tr.start();
		}
		else if(status==1)setStatus(2);
		else if(status==2);//Do nothing for same status
		else Controller.warningPane(STATUS_ERR, "Status error");
	}
	
	public static void disconnect(){
		connectionControl=false;
		System.out.println("click"+connectionControl);
	}
	
	

	public static void addCacheObj(String newMsg){
		synchronized(linkedList){
			if(linkedList.last!=null){
				linkedList.last.next=new CacheObj(newMsg);//update last cache to the latest one
				linkedList.last=linkedList.last.next;//update the linkedList.last
			}
			else{//if there has no data, then new() and link with the first and the last
				linkedList.first=new CacheObj(newMsg);
				linkedList.last=linkedList.first;
			}
		}
	}
	
	public static void setLocalPort(String port){//method for setting port numbers
		if(!checkPortInput(port))return;
		localPort=Integer.parseInt(port);
		RefreshView.localPortRefresh(port);
	}
	
	public static void setRemoteSocket(String IPAddress,String port){//set new remote InetSocketAddress
		boolean checkInetAddress=checkIPInput(IPAddress)&&checkPortInput(port);
		if(!checkInetAddress)return;//check
		remoteIP=IPAddress;//change
		remotePort=Integer.parseInt(port);
		remote=new InetSocketAddress(remoteIP,remotePort);
		//refresh view
		RefreshView.remoteInetSocketAddressRefresh(IPAddress, port, false);
	}
	
	
	//tools
	public static boolean checkPortInput(String str){//Input check
		int portNumber;
		//Check whether input is an integer or not
		try{
			portNumber=Integer.parseInt(str);
		}
		catch(Exception e){
			Controller.warningPane(INVALID_PORT_ERR, "Input error");
			return false;
		}
		//Check whether valid port number
		if(portNumber>=1024 && portNumber<=65535)return true;
		else{
			Controller.warningPane(INVALID_PORT_ERR, "Input error");
			return false;
		}
	}
	
	public static boolean checkIPInput(String str){
		String regexIP=new String("(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)");
		if(str.matches(regexIP)){
			return true;
		}
		else{
			Controller.warningPane(INVALID_IP_ERR, "Input error");
			return false;
		}
	}
}


class LinkedList{
	CacheObj first;
	CacheObj last;
}

class CacheObj{
	String message;
	CacheObj next;
	CacheObj(String str){
		message=str;
	}
}
