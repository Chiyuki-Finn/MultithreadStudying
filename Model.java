package swing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Model {
	private static int localPort;
	//private static String localAddress;
	private static int remotePort;
	private static String remoteAddress;
	private static ServerSocket serverSocket;
	private static Socket socket;
	private static Thread sender;
	private static Thread listener;
	
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
	
	public static void initialize(){
		localPort=1520;
		remotePort=1520;
		//localAddress="127.0.0.1";
		remoteAddress="127.0.0.2";
		Controller.initialize();				
		setStatus(0);
		listMonitor=new ListMonitor();
		listMonitor.first=null;
		listMonitor.last=null;
		
		System.out.println("Initialization completed.");
	}
	
	//connection module
	private static void ConnectionBuilder(){
		int retry=0;
		try {
			serverSocket=new ServerSocket(localPort);
		} catch (IOException e1) {//test
			e1.printStackTrace();
		}
		while(true){//build up connection
	        try{//check status before next step
	            if(status==1)socket=Model.serverSocket.accept();
	            else if(status==2)socket=new Socket(remoteAddress, remotePort);
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
        			Controller.statusChange(status);
        		}
        		else Controller.warningPane(STATUS_ERR, "Status error");
			}
	    }
		if(socket!=null&&socket.isConnected()){
			setStatus(3);
			/*
			 * !!!!!!!!!!!!!!!!!!!!!!!!!!  MsgSender and MsgListener initialize at here  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			 */
		}
		else Controller.warningPane(CONNECTION_ERR, "Connection error");
	}
	
	private class MsgSender implements Runnable{
		public void run() {
			
		}
		
	}
	
	private class MsgListener implements Runnable{
		public void run() {
	        
			
			while(true){
	            System.out.println("Connected:"+socket.getInetAddress()+":"+socket.getPort());
	            try {
					BufferedReader incomingMessage=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();//test
				}
	            String Msg="";
			}
		}
		
	}
	
	
	//internal tools
	private static void setStatus(int status){
		Model.status=status;
		Controller.statusChange(Model.status);
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
	
	public static void setPort(boolean isLocal,int port){//method for setting port numbers
		if(isLocal)localPort=port;
		else remotePort=port;
	}
	
	
	//external tools
	public static boolean checkPortInput(String str){//Input check
		int portNumber;
		//Check whether input is an integer or not
		try{
			portNumber=Integer.parseInt(str);
		}
		catch(Exception e){
			Controller.warningPane("Invalid input", "Input error");
			return false;
		}
		
		//Check whether valid port number
		if(portNumber>=1024 && portNumber<=65535)return true;
		else{
			Controller.warningPane("Invalid port number(must between 1024-65535)", "Input error");
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
