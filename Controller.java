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
	public static void warningPane(String message,String title){
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
		/*
		 * print module should be added later
		 */
	}
	
	public static void statusChange(int newStatus){//apply status changes to view	
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
		}
		else if(newStatus==2){
			windowEntity.remoteSettings_Change_btn.setEnabled(false);
			windowEntity.localPort_Change_btn.setEnabled(false);
			windowEntity.listener_btn.setEnabled(false);
			windowEntity.sendRequest_btn.setEnabled(false);
			windowEntity.remoteDisconnect_btn.setEnabled(true);
			windowEntity.connectionStatus_Get.setText("Sending");
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
	
	//public tools end
	
	//controller tools
	
	public static void appendToText(String str){
		str=windowEntity.textPane.getText()+str+"\n";
		windowEntity.textPane.setText(str);
		return;
	}
	//controller tools end
	
	
	//buttons
	public static void remoteSettings_Change_btn(){
		
	}
	
	/*!!!!!!!!!!!!!!!!!!!!!!        localPort_Change_btn() needs redesign          !!!!!!!!!!!!!!!!!!!!!!!!*/
	public static void localPort_Change_btn(JTextField LocalPortInput,JLabel ViewLabel,JTextPane TextPane){
		//get new local port number from Input, change the value of local port variable, changes will be reflected in Viewlabel and TextField
		String Current = LocalPortInput.getText();
		String Previous=ViewLabel.getText();
		if(!Model.checkPortInput(Current))return;
		else{
			int temp=Integer.parseInt(Current);
			ViewLabel.setText(""+temp);//view
			Model.setPort(true, temp);//set the value of port variable
			appendToText("Local port change from "+Previous+" to "+Current+".");
		}
	}
	
	public static void listener_btn(){
		statusChange(1);//test
		//Model.startListener();
	}
	
	public static void sendRequest_btn(){
		statusChange(2);//test
		//Model.startSender();
	}
	
	public static void remoteDisconnect_btn(){
		statusChange(0);//test
	}	
}
