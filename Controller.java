package swing;

import javax.swing.*;

public class Controller {
	public static void appendToText(JTextPane TextField,String str){
		str=TextField.getText()+str+"\n";
		TextField.setText(str);
		return;
	}
	
	public static void warningPane(String message,String title){
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	//buttons
	public static void localPort_Change_btn(JTextField LocalPortInput,JLabel ViewLabel,JTextPane TextField){
		//get new local port number from Input, change the value of local port variable, changes will be reflected in Viewlabel and TextField
		String Current = LocalPortInput.getText();
		String Previous=ViewLabel.getText();
		if(!Model.checkPortInput(Current))return;
		else{
			int temp=Integer.parseInt(Current);
			ViewLabel.setText(""+temp);//view
			Model.setPort(true, temp);//set the value of port variable
			appendToText(TextField,"Local port change from "+Previous+" to "+Current+".");
		}
	}
	
	public static void sendRequest_btn(JTextField RemotePortInput,JTextField RemoteAddressInput){
		Model.startSender();
	}
	
	public static void listener_btn(){
		Model.startListener();
	}
	
}
