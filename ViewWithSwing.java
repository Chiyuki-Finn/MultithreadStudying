package swing;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.Toolkit;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewWithSwing extends JFrame{
	public JTextField RemoteIPAddress_Input;
	public JTextField RemotePort_Input;
	public JTextField LocalPort_Change_Input;
	public ViewWithSwing() {
		setTitle("Title");
		getContentPane().setBackground(UIManager.getColor("Button.background"));
		getContentPane().setLayout(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextPane TextField = new JTextPane();
		TextField.setBounds(10, 10, 236, 444);
		getContentPane().add(TextField);
		
		JPanel RightControl = new JPanel();
		RightControl.setBounds(256, 10, 230, 444);
		getContentPane().add(RightControl);
		
		JPanel RemoteSettings = new JPanel();
		RemoteSettings.setToolTipText("");
		RemoteSettings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Remote Host Setting", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JPanel LocalSettings = new JPanel();
		LocalSettings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Local Settings", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JPanel ConnectionControl = new JPanel();
		ConnectionControl.setBorder(new TitledBorder(null, "Connection Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_RightControl = new GroupLayout(RightControl);
		gl_RightControl.setHorizontalGroup(
			gl_RightControl.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_RightControl.createSequentialGroup()
					.addGroup(gl_RightControl.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_RightControl.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(LocalSettings, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
							.addComponent(RemoteSettings, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 230, Short.MAX_VALUE))
						.addComponent(ConnectionControl, GroupLayout.PREFERRED_SIZE, 230, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_RightControl.setVerticalGroup(
			gl_RightControl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_RightControl.createSequentialGroup()
					.addComponent(RemoteSettings, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(LocalSettings, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ConnectionControl, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(128, Short.MAX_VALUE))
		);
		
		JPanel panel = new JPanel();
		GroupLayout gl_ConnectionControl = new GroupLayout(ConnectionControl);
		gl_ConnectionControl.setHorizontalGroup(
			gl_ConnectionControl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ConnectionControl.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_ConnectionControl.setVerticalGroup(
			gl_ConnectionControl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ConnectionControl.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
					.addContainerGap())
		);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{112, 95, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel ConnectionStatus = new JLabel("Status:");
		GridBagConstraints gbc_ConnectionStatus = new GridBagConstraints();
		gbc_ConnectionStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_ConnectionStatus.insets = new Insets(0, 0, 5, 5);
		gbc_ConnectionStatus.gridx = 0;
		gbc_ConnectionStatus.gridy = 0;
		panel.add(ConnectionStatus, gbc_ConnectionStatus);
		
		JLabel ConnectionStatus_Get = new JLabel("Unknown");
		GridBagConstraints gbc_ConnectionStatus_Get = new GridBagConstraints();
		gbc_ConnectionStatus_Get.insets = new Insets(0, 0, 5, 0);
		gbc_ConnectionStatus_Get.fill = GridBagConstraints.HORIZONTAL;
		gbc_ConnectionStatus_Get.gridx = 1;
		gbc_ConnectionStatus_Get.gridy = 0;
		panel.add(ConnectionStatus_Get, gbc_ConnectionStatus_Get);
		
		JLabel Listener = new JLabel("Launch listener");
		GridBagConstraints gbc_Listener = new GridBagConstraints();
		gbc_Listener.anchor = GridBagConstraints.WEST;
		gbc_Listener.insets = new Insets(0, 0, 5, 5);
		gbc_Listener.gridx = 0;
		gbc_Listener.gridy = 1;
		panel.add(Listener, gbc_Listener);
		
		//Listener btn event
		JButton Listener_btn = new JButton("Launch");
		Listener_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		Listener_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		GridBagConstraints gbc_Listener_btn = new GridBagConstraints();
		gbc_Listener_btn.insets = new Insets(0, 0, 5, 0);
		gbc_Listener_btn.fill = GridBagConstraints.HORIZONTAL;
		gbc_Listener_btn.gridx = 1;
		gbc_Listener_btn.gridy = 1;
		panel.add(Listener_btn, gbc_Listener_btn);
		
		JLabel SendRequest = new JLabel("Send request");
		GridBagConstraints gbc_SendRequest = new GridBagConstraints();
		gbc_SendRequest.anchor = GridBagConstraints.WEST;
		gbc_SendRequest.insets = new Insets(0, 0, 5, 5);
		gbc_SendRequest.gridx = 0;
		gbc_SendRequest.gridy = 2;
		panel.add(SendRequest, gbc_SendRequest);
		
		//Request sending
		JButton SendRequest_btn = new JButton("Send");
		SendRequest_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		SendRequest_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		GridBagConstraints gbc_SendRequest_btn = new GridBagConstraints();
		gbc_SendRequest_btn.insets = new Insets(0, 0, 5, 0);
		gbc_SendRequest_btn.fill = GridBagConstraints.HORIZONTAL;
		gbc_SendRequest_btn.gridx = 1;
		gbc_SendRequest_btn.gridy = 2;
		panel.add(SendRequest_btn, gbc_SendRequest_btn);
		
		JLabel RemoteDisconnect = new JLabel("Disconnect");
		GridBagConstraints gbc_RemoteDisconnect = new GridBagConstraints();
		gbc_RemoteDisconnect.anchor = GridBagConstraints.WEST;
		gbc_RemoteDisconnect.insets = new Insets(0, 0, 0, 5);
		gbc_RemoteDisconnect.gridx = 0;
		gbc_RemoteDisconnect.gridy = 3;
		panel.add(RemoteDisconnect, gbc_RemoteDisconnect);
		
		JButton RemoteDisconnect_btn = new JButton("Disconnect");
		GridBagConstraints gbc_RemoteDisconnect_btn = new GridBagConstraints();
		gbc_RemoteDisconnect_btn.fill = GridBagConstraints.HORIZONTAL;
		gbc_RemoteDisconnect_btn.gridx = 1;
		gbc_RemoteDisconnect_btn.gridy = 3;
		panel.add(RemoteDisconnect_btn, gbc_RemoteDisconnect_btn);
		ConnectionControl.setLayout(gl_ConnectionControl);
		
		JPanel LocalForm = new JPanel();
		GroupLayout gl_LocalSettings = new GroupLayout(LocalSettings);
		gl_LocalSettings.setHorizontalGroup(
			gl_LocalSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_LocalSettings.createSequentialGroup()
					.addComponent(LocalForm, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_LocalSettings.setVerticalGroup(
			gl_LocalSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_LocalSettings.createSequentialGroup()
					.addComponent(LocalForm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(105, Short.MAX_VALUE))
		);
		GridBagLayout gbl_LocalForm = new GridBagLayout();
		gbl_LocalForm.columnWidths = new int[]{78, 135, 0};
		gbl_LocalForm.rowHeights = new int[]{15, 21, 23, 0};
		gbl_LocalForm.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_LocalForm.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		LocalForm.setLayout(gbl_LocalForm);
		
		JLabel LocalPort = new JLabel("Current Port:");
		GridBagConstraints gbc_LocalPort = new GridBagConstraints();
		gbc_LocalPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_LocalPort.insets = new Insets(0, 0, 5, 5);
		gbc_LocalPort.gridx = 0;
		gbc_LocalPort.gridy = 0;
		LocalForm.add(LocalPort, gbc_LocalPort);
		
		JLabel LocalPort_Get = new JLabel("Default(1520)");
		GridBagConstraints gbc_LocalPort_Get = new GridBagConstraints();
		gbc_LocalPort_Get.fill = GridBagConstraints.HORIZONTAL;
		gbc_LocalPort_Get.insets = new Insets(0, 0, 5, 0);
		gbc_LocalPort_Get.gridx = 1;
		gbc_LocalPort_Get.gridy = 0;
		LocalForm.add(LocalPort_Get, gbc_LocalPort_Get);
		
		JLabel LocalPort_Change = new JLabel("Change port:");
		GridBagConstraints gbc_LocalPort_Change = new GridBagConstraints();
		gbc_LocalPort_Change.anchor = GridBagConstraints.WEST;
		gbc_LocalPort_Change.insets = new Insets(0, 0, 5, 5);
		gbc_LocalPort_Change.gridx = 0;
		gbc_LocalPort_Change.gridy = 1;
		LocalForm.add(LocalPort_Change, gbc_LocalPort_Change);
		
		LocalPort_Change_Input = new JTextField();
		GridBagConstraints gbc_LocalPort_Change_Input = new GridBagConstraints();
		gbc_LocalPort_Change_Input.fill = GridBagConstraints.HORIZONTAL;
		gbc_LocalPort_Change_Input.insets = new Insets(0, 0, 5, 0);
		gbc_LocalPort_Change_Input.gridx = 1;
		gbc_LocalPort_Change_Input.gridy = 1;
		LocalForm.add(LocalPort_Change_Input, gbc_LocalPort_Change_Input);
		LocalPort_Change_Input.setColumns(10);
		
		//LocalPort_Change_btn
		JButton LocalPort_Change_btn = new JButton("Confirm");
		LocalPort_Change_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		LocalPort_Change_btn.addMouseListener(new MouseAdapter() {	
			//LocalPort_Change_btn clicked event
			public void mouseClicked(MouseEvent e) {
				swing.Controller.localPort_Change_btn(LocalPort_Change_Input,LocalPort_Get,TextField);
			}
		});
		GridBagConstraints gbc_LocalPort_Change_btn = new GridBagConstraints();
		gbc_LocalPort_Change_btn.fill = GridBagConstraints.HORIZONTAL;
		gbc_LocalPort_Change_btn.gridx = 1;
		gbc_LocalPort_Change_btn.gridy = 2;
		LocalForm.add(LocalPort_Change_btn, gbc_LocalPort_Change_btn);
		LocalSettings.setLayout(gl_LocalSettings);
		
		JPanel RemoteForm = new JPanel();
		GridBagLayout gbl_RemoteForm = new GridBagLayout();
		gbl_RemoteForm.columnWidths = new int[]{72, 135, 0};
		gbl_RemoteForm.rowHeights = new int[]{21, 21, 0};
		gbl_RemoteForm.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_RemoteForm.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		RemoteForm.setLayout(gbl_RemoteForm);
		
		JLabel RemoteIPAddress = new JLabel("IP address:");
		GridBagConstraints gbc_RemoteIPAddress = new GridBagConstraints();
		gbc_RemoteIPAddress.anchor = GridBagConstraints.WEST;
		gbc_RemoteIPAddress.insets = new Insets(0, 0, 5, 5);
		gbc_RemoteIPAddress.gridx = 0;
		gbc_RemoteIPAddress.gridy = 0;
		RemoteForm.add(RemoteIPAddress, gbc_RemoteIPAddress);
		
		RemoteIPAddress_Input = new JTextField();
		GridBagConstraints gbc_RemoteIPAddress_Input = new GridBagConstraints();
		gbc_RemoteIPAddress_Input.fill = GridBagConstraints.HORIZONTAL;
		gbc_RemoteIPAddress_Input.insets = new Insets(0, 0, 5, 0);
		gbc_RemoteIPAddress_Input.gridx = 1;
		gbc_RemoteIPAddress_Input.gridy = 0;
		RemoteForm.add(RemoteIPAddress_Input, gbc_RemoteIPAddress_Input);
		RemoteIPAddress_Input.setColumns(10);
		
		JLabel RemotePort = new JLabel("Remote port:");
		GridBagConstraints gbc_RemotePort = new GridBagConstraints();
		gbc_RemotePort.anchor = GridBagConstraints.WEST;
		gbc_RemotePort.insets = new Insets(0, 0, 0, 5);
		gbc_RemotePort.gridx = 0;
		gbc_RemotePort.gridy = 1;
		RemoteForm.add(RemotePort, gbc_RemotePort);
		
		RemotePort_Input = new JTextField();
		GridBagConstraints gbc_RemotePort_Input = new GridBagConstraints();
		gbc_RemotePort_Input.fill = GridBagConstraints.HORIZONTAL;
		gbc_RemotePort_Input.gridx = 1;
		gbc_RemotePort_Input.gridy = 1;
		RemoteForm.add(RemotePort_Input, gbc_RemotePort_Input);
		RemotePort_Input.setColumns(10);
		GroupLayout gl_RemoteSettings = new GroupLayout(RemoteSettings);
		gl_RemoteSettings.setHorizontalGroup(
			gl_RemoteSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_RemoteSettings.createSequentialGroup()
					.addComponent(RemoteForm, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_RemoteSettings.setVerticalGroup(
			gl_RemoteSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_RemoteSettings.createSequentialGroup()
					.addComponent(RemoteForm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(44, Short.MAX_VALUE))
		);
		RemoteSettings.setLayout(gl_RemoteSettings);
		RightControl.setLayout(gl_RightControl);
		setBackground(new Color(240, 240, 240));
	}
}
