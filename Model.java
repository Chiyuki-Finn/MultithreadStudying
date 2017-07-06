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

public class Model {
	private static int localPort;
	//private static String localAddress;
	private static int remotePort;
	private static String remoteAddress;
	private static InetSocketAddress remote;
	//private static ServerSocket serverSocket;
	private static ServerSocketChannel serverSocketChannel;
	//private static Socket socket;
	private static SocketChannel socketChannel;
	private static Charset charset;
	
	private static Thread msgSender_Tr;
	private static Thread msgListener_Tr;
	
	private static int status;
	/*
	 * 0:started
	 * 1:listening
	 * 2:send request
	 * 3:connection ready
	*/

	private static ListMonitor listMonitor;
	
	private static final String STATUS_ERR="Unknown status error occured.";
	private static final String CONNECTION_ERR="Unknown connection error occured. Retry connection procedure later.";
	private static final String INVALID_IP_ERR="Invalid IP address.";
	private static final String INVALID_PORT_ERR="Invalid port, must be between 1024-65535.";
	private static final int BYTEBUFFER_CAPACITY=500;
	
	public static void initialize(){
		localPort=1520;
		remotePort=1520;
		//localAddress="127.0.0.1";
		remoteAddress="127.0.0.2";
		charset=Charset.forName("UTF8");
		
		Controller.initialize();				
		setStatus(0);
		listMonitor=new ListMonitor();
		listMonitor.first=null;
		listMonitor.last=null;
		remote=new InetSocketAddress(remoteAddress,remotePort);
		
		System.out.println("Initialization completed.");
	}
	
	//connection module
	private static void ConnectionBuilder(){
		int retry=0;
		try {
			serverSocketChannel=ServerSocketChannel.open();
			socketChannel=SocketChannel.open();
		} catch (IOException e1) {//test
			e1.printStackTrace();
		}
		while(true){//build up connection
	        try{//check status before next step
	            if(status==1)socketChannel=Model.serverSocketChannel.accept();
	            else if(status==2)socketChannel.connect(remote);
	            else Controller.warningPane(STATUS_ERR, "Status error");
	            break;
        	} catch (IOException e){
        		if(status==1)try {
					Thread.sleep(1000);//listener retry every 1 sec
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
        		else if(status==2&&retry<15){//listener can retry unlimited times, but sender can only retry 20 times for each send order
        			try {
    					Thread.sleep(1000);//sender retry every 1 sec
    				} catch (InterruptedException e1) {
    					e1.printStackTrace();
    				} finally{
    					retry++;
    				}
        		}
        		else if(status==2&&retry>15){
        			String errorMsg="Remote host \""+ remoteAddress +":"+ remotePort +"\"unreachable";
        			Controller.warningPane(errorMsg, "Connection error");
        			status=1;//reset status
        			Controller.RefreshView.statusRefresh(status);
        		}
        		else Controller.warningPane(STATUS_ERR, "Status error");
			}
	    }
		if(socketChannel.isConnected()){
			setStatus(3);//initialize msgSender and msgListener
			MsgSender msgSender=new MsgSender();
			MsgListener msgListener=new MsgListener();
			msgSender_Tr=new Thread(msgSender);
			msgListener_Tr=new Thread(msgListener);
			msgSender_Tr.start();
			msgListener_Tr.start();
		}
		else Controller.warningPane(CONNECTION_ERR, "Connection error");
	}
	
	private static class MsgSender implements Runnable{
		boolean loopControl=true;
		public void run() {
			ByteBuffer byteBuffer=ByteBuffer.allocate(BYTEBUFFER_CAPACITY);
			while(loopControl){
				if(!socketChannel.isConnected())Controller.warningPane(CONNECTION_ERR, "Connection error");
				else if(status!=3)Controller.warningPane(STATUS_ERR, "Status error");
				else synchronized(socketChannel){
					try {
						if(listMonitor.first==null)Thread.sleep(500);//if empty, check 500ms later
						else{//send all cached strings, then flush
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
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();//test, may add an IOException control, stop looping when disconnected
					}
				}
			}
		}
		
	}
	
	private static class MsgListener implements Runnable{
		boolean loopControl=true;
		public void run() {
			ByteBuffer byteBuffer=ByteBuffer.allocate(BYTEBUFFER_CAPACITY);
			CharBuffer charBuffer=null;
			String temp="";
			int size=0;
			while(loopControl){
				if(!socketChannel.isConnected()){
					Controller.warningPane(CONNECTION_ERR, "Connection error");
					return;
				}
				else if(status!=3){
					Controller.warningPane(STATUS_ERR, "Status error");
					return;
				}
				else synchronized(socketChannel){
					try {
						size=socketChannel.read(byteBuffer);
						while(size!=-1)//reading
						{
							byteBuffer.flip();
							while(byteBuffer.hasRemaining())
							{
								charBuffer=charset.decode(byteBuffer);
								temp+=charBuffer.toString();
								//System.out.println(charBuffer);
							}
							byteBuffer.clear();
							size=socketChannel.read(byteBuffer);
						}
					} catch (IOException e) {
						e.printStackTrace();//test, may add an IOException control, stop looping when disconnected
					}
				}

				System.out.println(temp);//test
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!      callback method is at here      !!!!!!!!!!!!!!!!!!!!!!!!!!!!
			}
		}
		
	}
	
	
	// tools
	private static void setStatus(int status){
		Model.status=status;
		Controller.RefreshView.statusRefresh(Model.status);
	}
	
	private static String readCache(){
		String result="";
		synchronized(listMonitor){
			while(listMonitor.first!=null){
				result+=listMonitor.first.message+"\n";
				listMonitor.first=listMonitor.first.next;
				//dispose current cache, and link the listMonitor.first pointer to the next cache
				//and if listMonitor.first.next is null, the loop will be stopped at the beginning of next round
			}
			listMonitor.last=null;//all caches are read and disposed, reset listMonitor.last
		}
		return result;
	}
	
	
	
	//external interfaces
	public static void PortListening(){//set status 1, and create a server socket to wait for connection requests
		if(status==0||status==2)setStatus(1);
        else if(status==1);//Do nothing for same status
        else Controller.warningPane(STATUS_ERR, "Status error");
	}
	
	public static void SendRequest(){//set status 2, and create a socket to try sending connection requests to remote host
		if(status==0||status==1)setStatus(2);
		else if(status==2);//Do nothing for same status
		else Controller.warningPane(STATUS_ERR, "Status error");
	}

	public static void addCacheObj(String newMsg){
		synchronized(listMonitor){
			if(listMonitor.last!=null){
				listMonitor.last.next=new CacheObj(newMsg);//update last cache to the latest one
				listMonitor.last=listMonitor.last.next;//update the listMonitor.last
			}
			else{//if there has no data, then new() and link with the first and the last
				listMonitor.first=new CacheObj(newMsg);
				listMonitor.last=listMonitor.first;
			}
		}
	}
	
	public static void setLocalPort(String port){//method for setting port numbers
		if(!checkPortInput(port))return;
		localPort=Integer.parseInt(port);
		err
	}
	
	public static void setRemoteSocket(String IPAddress,String port){//set new remote InetSocketAddress
		boolean checkInetAddress=checkIPInput(IPAddress)&&checkPortInput(port);
		if(!checkInetAddress)return;//check
		remoteAddress=IPAddress;//change
		remotePort=Integer.parseInt(port);
		remote=new InetSocketAddress(remoteAddress,remotePort);
		//refresh view
		err
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


class ListMonitor{
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
