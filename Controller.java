package swing;

import javax.swing.*;

public class Controller {
	public static ViewWithSwing windowEntity;
	
	public static void initialize(){
	//initialize windowEntity
		windowEntity=new ViewWithSwing();
		windowEntity.setSize(512, 502);
		windowEntity.setVisible(true);
		windowEntity.setLocation(1920/2-windowEntity.getHeight()/2, 1080/2-windowEntity.getWidth()/2);//1920*1080should be change into screen size
		
	}
	
	
	//public tools
	public static void warningPane(String message, String title){
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
		/*
		 * print module should be added later
		 */
	}
	
	
	public static class RefreshView{
		public static void localPortRefresh(String port){
			windowEntity.localPort_Get.setText(port);
			String temp="<-info: New local port set:"+ port +" ->\n";
			appendToText(temp);
		}
		
		public static void remoteInetSocketAddressRefresh(String IP, String port){
			windowEntity.remotePort_Get.setText(port);
			windowEntity.remoteIPAddress_Get.setText(IP);
			String temp="<-info: New remote address set:"+ IP +":"+ port +" ->\n";
			appendToText(temp);
		}
		
		public static void statusRefresh(int newStatus){//apply status changes to view	
			if(newStatus==0){
				windowEntity.remoteSettings_Change_btn.setEnabled(true);
				windowEntity.localPort_Change_btn.setEnabled(true);
				windowEntity.listener_btn.setEnabled(true);
				windowEntity.sendRequest_btn.setEnabled(true);
				windowEntity.remoteDisconnect_btn.setEnabled(false);
				windowEntity.connectionStatus_Get.setText("Disconnected");
			}
			else if(newStatus==1){
				windowEntity.remoteSettings_Change_btn.setEnabled(true);
				windowEntity.localPort_Change_btn.setEnabled(false);
				windowEntity.listener_btn.setEnabled(false);
				windowEntity.sendRequest_btn.setEnabled(true);
				windowEntity.remoteDisconnect_btn.setEnabled(true);
				windowEntity.connectionStatus_Get.setText("Listening");
				String temp="<-info: Switch to listening mode. ->\n";
				appendToText(temp);
			}
			else if(newStatus==2){
				windowEntity.remoteSettings_Change_btn.setEnabled(false);
				windowEntity.localPort_Change_btn.setEnabled(false);
				windowEntity.listener_btn.setEnabled(false);
				windowEntity.sendRequest_btn.setEnabled(false);
				windowEntity.remoteDisconnect_btn.setEnabled(true);
				windowEntity.connectionStatus_Get.setText("Sending");
				String temp="<-info: Switch to sending mode. ->\n";
				appendToText(temp);
			}
			else{
				windowEntity.remoteSettings_Change_btn.setEnabled(false);
				windowEntity.localPort_Change_btn.setEnabled(false);
				windowEntity.listener_btn.setEnabled(false);
				windowEntity.sendRequest_btn.setEnabled(false);
				windowEntity.remoteDisconnect_btn.setEnabled(true);
				windowEntity.connectionStatus_Get.setText("Connected");
			}
		}
	}
	
	public static void newMessage(String str,boolean isRemote){
		String temp="";
		if(isRemote)temp="Remote:"+ str +"\n";
		else temp="Loacal:"+ str + "\n";
		appendToText(temp);
	}
	
	
	//controller tools	
	public static void appendToText(String str){
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
		RefreshView.statusRefresh(1);//test
		//Model.PortListening();
	}
	
	public static void sendRequest_btn(){
		RefreshView.statusRefresh(2);//test
		//Model.SendRequest();
	}
	
	public static void remoteDisconnect_btn(){
		RefreshView.statusRefresh(0);//test
	}	
}
