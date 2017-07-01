package swing;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCP_Connection {
	class Sender implements Runnable{
		private int port;
		private String ipAddress;
		private Socket socket;
		public Sender(String address,int port){
		       this.port=port;
		       this.ipAddress=address;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class Listener implements Runnable{
		private int port;
		private ServerSocket serverSocket;
		public Listener(String address,int port){
			this.port=port;
		}
		
		public void run() {
	        Socket socket=null;
			while(true){//phase 1, build up connection
		        try{
		            socket=serverSocket.accept();
		            Thread.sleep(500);//check every 0.5s
	        	} catch (IOException e){
	        		//e.printStackTrace();
	        	} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		    }
			
			while(true){
	            System.out.println("Connected:"+socket.getInetAddress()+":"+socket.getPort());
	            BufferedReader incomingMessage=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String Msg="";
			}
			
			try{
            	if(socket!=null)socket.close();
            	continue;
            }catch(IOException e){
            	e.printStackTrace();
            	break;
            }
		}
		
	}
}
