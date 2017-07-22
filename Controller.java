package mainPackage;

import javax.swing.*;

public class Controller {
	public static ViewWithSwing windowEntity;
	
	public static void initialize(){
	//initialize windowEntity
		windowEntity=new ViewWithSwing();
		windowEntity.setSize(512, 502);
		windowEntity.setVisible(true);
		windowEntity.setLocation(1920/2-windowEntity.getHeight()/2, 1080/2-windowEntity.getWidth()/2);//1920*1080should be change into screen size
		windowEntity.remoteIPAddress_Change_Input.setText("127.0.0.1");
		windowEntity.remotePort_Change_Input.setText("1520");
	}
	
	
	//public tools
	public static void warningPane(String message, String title){
		Thread warning=new Thread(
				new Runnable(){
					public void run(){
						JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
						}
					}
				);
		warning.start();
		/*
		 * print module should be added later
		 */
	}
	
	
	public static class RefreshView{
		public static void localPortRefresh(String port){
			windowEntity.localPort_Get.setText(port);
			String temp="<-info: New local port set:"+ port +" ->";
			appendToText(temp);
		}
		
		public static void remoteInetSocketAddressRefresh(String IP, String port, boolean isConnected){
			windowEntity.remotePort_Get.setText(port);
			windowEntity.remoteIPAddress_Get.setText(IP);
			String temp;
			if(!isConnected)temp="<-info: New remote address set: "+ IP +":"+ port +" ->";
			else temp="<-info: Connected with remote address: "+ IP +":"+ port +" ->";
			appendToText(temp);
		}
		
		public static void statusRefresh(int newStatus){//apply status changes to view	
			if(newStatus==0){
				windowEntity.remoteSettings_Change_btn.setEnabled(true);
				windowEntity.localPort_Change_btn.setEnabled(true);
				windowEntity.listener_btn.setEnabled(true);
				windowEntity.sendRequest_btn.setEnabled(true);
				windowEntity.remoteDisconnect_btn.setEnabled(false);
				windowEntity.TextInput_Send_btn.setEnabled(false);
				windowEntity.connectionStatus_Get.setText("Disconnected");
				String temp="<-info: Ready to connect. ->";
				appendToText(temp);
			}
			else if(newStatus==1){
				windowEntity.remoteSettings_Change_btn.setEnabled(true);
				windowEntity.localPort_Change_btn.setEnabled(false);
				windowEntity.listener_btn.setEnabled(false);
				windowEntity.sendRequest_btn.setEnabled(true);
				windowEntity.remoteDisconnect_btn.setEnabled(true);
				windowEntity.TextInput_Send_btn.setEnabled(false);
				windowEntity.connectionStatus_Get.setText("Listening");
				String temp="<-info: Switch to listening mode. ->";
				appendToText(temp);
			}
			else if(newStatus==2){
				windowEntity.remoteSettings_Change_btn.setEnabled(false);
				windowEntity.localPort_Change_btn.setEnabled(false);
				windowEntity.listener_btn.setEnabled(false);
				windowEntity.sendRequest_btn.setEnabled(false);
				windowEntity.remoteDisconnect_btn.setEnabled(true);
				windowEntity.TextInput_Send_btn.setEnabled(false);
				windowEntity.connectionStatus_Get.setText("Sending");
				String temp="<-info: Switch to sending mode. ->";
				appendToText(temp);
			}
			else{
				windowEntity.remoteSettings_Change_btn.setEnabled(false);
				windowEntity.localPort_Change_btn.setEnabled(false);
				windowEntity.listener_btn.setEnabled(false);
				windowEntity.sendRequest_btn.setEnabled(false);
				windowEntity.remoteDisconnect_btn.setEnabled(true);
				windowEntity.TextInput_Send_btn.setEnabled(true);
				windowEntity.connectionStatus_Get.setText("Connected");
			}
		}
	}
	
	public static void newMessage(String newMsg,boolean isRemote){
		String temp="";
		if(isRemote)temp="Remote:"+ newMsg ;
		else temp="Loacal:"+ newMsg ;
		appendToText(temp);
	}
	
	
	//controller tools	
	private static void appendToText(String str){
		str=windowEntity.textPane.getText()+str+"\n";
		windowEntity.textPane.setText(str);
		return;
	}
	
	
	//buttons
	public static void remoteSettings_Change_btn(){
		String remoteIP=windowEntity.remoteIPAddress_Change_Input.getText();
		String remotePort=windowEntity.remotePort_Change_Input.getText();
		Model.setRemoteSocket(remoteIP, remotePort);
	}
	
	public static void localPort_Change_btn(){
		String Current = windowEntity.localPort_Change_Input.getText();
		Model.setLocalPort(Current);
	}
	
	public static void listener_btn(){
		//RefreshView.statusRefresh(1);//test
		Model.portListening();
	}
	
	public static void sendRequest_btn(){
		//RefreshView.statusRefresh(2);//test
		Model.sendRequest();
	}
	
	public static void remoteDisconnect_btn(){
		//RefreshView.statusRefresh(0);//test
		Model.disconnect();
	}
	
	public static void TextInput_Send_btn(){
		String newMsg=windowEntity.MsgArea.getText();
		Model.addCacheObj(newMsg);
		windowEntity.MsgArea.setText("");
	}
}
