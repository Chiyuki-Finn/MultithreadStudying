package swing;

public class Model {
	private static int localPort;
	private static String localAddress;
	private static int remotePort;
	private static String remoteAddress;
	private static Thread sender;
	private static Thread listener;
	private static ViewWithSwing windowEntity;
	
	private static int status;
	/*
	 * 0:started
	 * 100:listener on awaiting connection
	 * 201:sender on and request sent
	 * 202:sender built up a connection with remote host and awaiting feedback to listener
	 * 211:Received a request and listener built up a connection, trying to send a feedback to remote
	 * 300:FullDuplex communication
	*/

	private static ListMonitor listMonitor;
	
	public static void initialize(){
		localPort=1520;
		remotePort=1520;
		localAddress="127.0.0.1";
		remoteAddress="127.0.0.2";
		
		//initialize windowEntity
		windowEntity=new ViewWithSwing();
		windowEntity.setSize(512, 502);
		windowEntity.setVisible(true);
		windowEntity.setLocation(1920/2-windowEntity.getHeight()/2, 1080/2-windowEntity.getWidth()/2);//1920*1080should be change into screen size
		
		status=0;
		listMonitor=new ListMonitor();
		listMonitor.first=null;
		listMonitor.last=null;
		
		System.out.println("Initialization completed.");
	}
	
	public static boolean startListener(){
		if(status!=0){
			String errorMsg="Current status:"+ status +", a listener has already been instantiated or unknown exception.";
			Controller.warningPane(errorMsg, "Status error");//must not instantiate a listener at later steps
			return false;
		}
		
		//if succeeded
		status=100;
		return true;
	}
	
	public static boolean startSender(){
		if(status!=100||status!=211){
			String errorMsg="Current status:"+ status +", a listener must be instantiated before instantiating a sender or unknown exception.";
			Controller.warningPane(errorMsg, "Status error");//a listener must be instantiated before instantiating a sender 
			return false;
		}
		else if(status==100){
			/*
			 * some codes about start a sender
			 */
			
			//if succeeded
			status=201;
			return true;
		}
		else if(status==211){
			/*
			 * some codes about start a sender
			 */
			
			//if succeeded
			status=;
			return true;
		}
		else return false;
	}

	public static void addCacheObj(String newMsg){
		synchronized(listMonitor){
			if(listMonitor.last!=null){
				listMonitor.last.next=new CacheObj(newMsg);//update the last cache in list
				listMonitor.last=listMonitor.last.next;//update the listMonitor.last
			}
			else{//if there has no data, then new() and link with the first and the last
				listMonitor.first=new CacheObj(newMsg);
				listMonitor.last=listMonitor.first;
			}
		}
	}
	
	public static String readCache(){
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
	
	public static void setPort(boolean isLocal,int port){//method for setting port numbers
		if(isLocal)localPort=port;
		else remotePort=port;
	}
	
	public static boolean checkPortInput(String str){//Input check
		int portNumber;
		//Check whether input is an integer or not
		try{
			portNumber=Integer.parseInt(str);
		}
		catch(Exception e){
			/*str="Invalid input:"+str;
			JOptionPane.showMessageDialog(null, str, "waring", JOptionPane.PLAIN_MESSAGE);*/
			Controller.warningPane("Invalid input", "Input error");
			return false;
		}
		
		//Check whether valid port number
		if(portNumber>=1024 && portNumber<=65535)return true;
		else{
			//JOptionPane.showMessageDialog(null, , "waring", JOptionPane.PLAIN_MESSAGE);
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
