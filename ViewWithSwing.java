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
import javax.swing.JTabbedPane;

public class ViewWithSwing extends JFrame{
	public JTextPane textPane;
	
	public JTextField remoteIPAddress_Change_Input;
	public JTextField remotePort_Change_Input;
	public JTextField localPort_Change_Input;
	
	public JButton remoteSettings_Change_btn;//btn that click to confirm changes about remote IP and port
	public JButton localPort_Change_btn;//btn that click to confirm changes about local port
	public JButton listener_btn;//btn which initiates listener(status 1)
	public JButton sendRequest_btn;//btn which initiates sender(status 2)
	public JButton remoteDisconnect_btn;//btn which stops any current operation including listening(status 1), sending(status 2)and connected/communicating(status 3)
	
	public JLabel localPort_Get;//label which shows current local port
	public JLabel connectionStatus_Get;//label which shows current status
	public JLabel remoteIPAddress_Get;//label which shows current remote IP address
	public JLabel remotePort_Get;//label which shows current remote port
	
	public ViewWithSwing() {
		setTitle("Title");
		getContentPane().setBackground(UIManager.getColor("Button.background"));
		getContentPane().setLayout(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//JTextPane TextPane = new JTextPane();
		textPane = new JTextPane();
		textPane.setBounds(10, 10, 236, 444);
		getContentPane().add(textPane);
						
						JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
						tabbedPane.setBounds(256, 10, 230, 443);
						getContentPane().add(tabbedPane);
						
						JPanel connectionSettings = new JPanel();
						tabbedPane.addTab("Settings", null, connectionSettings, null);
						
						JPanel remoteSettings = new JPanel();
						remoteSettings.setToolTipText("");
						remoteSettings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Remote Host Settings", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
						
						JPanel localSettings = new JPanel();
						localSettings.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Local Settings", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
						
						JPanel connectionControl = new JPanel();
						connectionControl.setBorder(new TitledBorder(null, "Connection Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						GroupLayout gl_connectionSettings = new GroupLayout(connectionSettings);
						gl_connectionSettings.setHorizontalGroup(
							gl_connectionSettings.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_connectionSettings.createSequentialGroup()
									.addGroup(gl_connectionSettings.createParallelGroup(Alignment.TRAILING)
										.addComponent(connectionControl, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 230, Short.MAX_VALUE)
										.addComponent(remoteSettings, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
										.addComponent(localSettings, Alignment.LEADING, 0, 0, Short.MAX_VALUE))
									.addContainerGap())
						);
						gl_connectionSettings.setVerticalGroup(
							gl_connectionSettings.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_connectionSettings.createSequentialGroup()
									.addComponent(remoteSettings, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(localSettings, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(connectionControl, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
									.addContainerGap(61, Short.MAX_VALUE))
						);
						
						JPanel connectionControlForm = new JPanel();
						GroupLayout gl_connectionControl = new GroupLayout(connectionControl);
						gl_connectionControl.setHorizontalGroup(
							gl_connectionControl.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_connectionControl.createSequentialGroup()
									.addComponent(connectionControlForm, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
									.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						);
						gl_connectionControl.setVerticalGroup(
							gl_connectionControl.createParallelGroup(Alignment.LEADING)
								.addComponent(connectionControlForm, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
						);
						GridBagLayout gbl_connectionControlForm = new GridBagLayout();
						gbl_connectionControlForm.columnWidths = new int[]{112, 95, 0};
						gbl_connectionControlForm.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
						gbl_connectionControlForm.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
						gbl_connectionControlForm.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
						connectionControlForm.setLayout(gbl_connectionControlForm);
						
						JLabel connectionStatus = new JLabel("Status:");
						GridBagConstraints gbc_connectionStatus = new GridBagConstraints();
						gbc_connectionStatus.fill = GridBagConstraints.HORIZONTAL;
						gbc_connectionStatus.insets = new Insets(0, 0, 5, 5);
						gbc_connectionStatus.gridx = 0;
						gbc_connectionStatus.gridy = 0;
						connectionControlForm.add(connectionStatus, gbc_connectionStatus);
						
						connectionStatus_Get = new JLabel("uninitialized");
						GridBagConstraints gbc_connectionStatus_Get = new GridBagConstraints();
						gbc_connectionStatus_Get.insets = new Insets(0, 0, 5, 0);
						gbc_connectionStatus_Get.fill = GridBagConstraints.HORIZONTAL;
						gbc_connectionStatus_Get.gridx = 1;
						gbc_connectionStatus_Get.gridy = 0;
						connectionControlForm.add(connectionStatus_Get, gbc_connectionStatus_Get);
						
						JLabel remoteIPAddress = new JLabel("Remote IP:");
						GridBagConstraints gbc_remoteIPAddress = new GridBagConstraints();
						gbc_remoteIPAddress.fill = GridBagConstraints.HORIZONTAL;
						gbc_remoteIPAddress.insets = new Insets(0, 0, 5, 5);
						gbc_remoteIPAddress.gridx = 0;
						gbc_remoteIPAddress.gridy = 1;
						connectionControlForm.add(remoteIPAddress, gbc_remoteIPAddress);
						
						remoteIPAddress_Get = new JLabel("uninitialized");
						GridBagConstraints gbc_remoteIPAddress_Get = new GridBagConstraints();
						gbc_remoteIPAddress_Get.fill = GridBagConstraints.HORIZONTAL;
						gbc_remoteIPAddress_Get.insets = new Insets(0, 0, 5, 0);
						gbc_remoteIPAddress_Get.gridx = 1;
						gbc_remoteIPAddress_Get.gridy = 1;
						connectionControlForm.add(remoteIPAddress_Get, gbc_remoteIPAddress_Get);
						
						JLabel remotePort = new JLabel("Remote port:");
						GridBagConstraints gbc_remotePort = new GridBagConstraints();
						gbc_remotePort.fill = GridBagConstraints.HORIZONTAL;
						gbc_remotePort.insets = new Insets(0, 0, 5, 5);
						gbc_remotePort.gridx = 0;
						gbc_remotePort.gridy = 2;
						connectionControlForm.add(remotePort, gbc_remotePort);
						
						remotePort_Get = new JLabel("uninitialized");
						GridBagConstraints gbc_remotePort_Get = new GridBagConstraints();
						gbc_remotePort_Get.fill = GridBagConstraints.HORIZONTAL;
						gbc_remotePort_Get.insets = new Insets(0, 0, 5, 0);
						gbc_remotePort_Get.gridx = 1;
						gbc_remotePort_Get.gridy = 2;
						connectionControlForm.add(remotePort_Get, gbc_remotePort_Get);
						
						JLabel listener = new JLabel("Launch listener");
						GridBagConstraints gbc_listener = new GridBagConstraints();
						gbc_listener.anchor = GridBagConstraints.WEST;
						gbc_listener.insets = new Insets(0, 0, 5, 5);
						gbc_listener.gridx = 0;
						gbc_listener.gridy = 3;
						connectionControlForm.add(listener, gbc_listener);
						
						//Listener btn event
						listener_btn = new JButton("Launch");
						listener_btn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								Controller.listener_btn();
							}
						});
						GridBagConstraints gbc_listener_btn = new GridBagConstraints();
						gbc_listener_btn.insets = new Insets(0, 0, 5, 0);
						gbc_listener_btn.fill = GridBagConstraints.HORIZONTAL;
						gbc_listener_btn.gridx = 1;
						gbc_listener_btn.gridy = 3;
						connectionControlForm.add(listener_btn, gbc_listener_btn);
						
						//Request sending
						sendRequest_btn = new JButton("Send");
						sendRequest_btn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Controller.sendRequest_btn();
							}
						});
						
						JLabel sendRequest = new JLabel("Send request");
						GridBagConstraints gbc_sendRequest = new GridBagConstraints();
						gbc_sendRequest.anchor = GridBagConstraints.WEST;
						gbc_sendRequest.insets = new Insets(0, 0, 5, 5);
						gbc_sendRequest.gridx = 0;
						gbc_sendRequest.gridy = 4;
						connectionControlForm.add(sendRequest, gbc_sendRequest);
						GridBagConstraints gbc_sendRequest_btn = new GridBagConstraints();
						gbc_sendRequest_btn.insets = new Insets(0, 0, 5, 0);
						gbc_sendRequest_btn.fill = GridBagConstraints.HORIZONTAL;
						gbc_sendRequest_btn.gridx = 1;
						gbc_sendRequest_btn.gridy = 4;
						connectionControlForm.add(sendRequest_btn, gbc_sendRequest_btn);
						
						JLabel remoteDisconnect = new JLabel("Disconnect");
						GridBagConstraints gbc_remoteDisconnect = new GridBagConstraints();
						gbc_remoteDisconnect.anchor = GridBagConstraints.WEST;
						gbc_remoteDisconnect.insets = new Insets(0, 0, 0, 5);
						gbc_remoteDisconnect.gridx = 0;
						gbc_remoteDisconnect.gridy = 5;
						connectionControlForm.add(remoteDisconnect, gbc_remoteDisconnect);
						
						remoteDisconnect_btn = new JButton("Disconnect");
						remoteDisconnect_btn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Controller.remoteDisconnect_btn();
							}
						});
						GridBagConstraints gbc_remoteDisconnect_btn = new GridBagConstraints();
						gbc_remoteDisconnect_btn.fill = GridBagConstraints.HORIZONTAL;
						gbc_remoteDisconnect_btn.gridx = 1;
						gbc_remoteDisconnect_btn.gridy = 5;
						connectionControlForm.add(remoteDisconnect_btn, gbc_remoteDisconnect_btn);
						connectionControl.setLayout(gl_connectionControl);
						
						JPanel localForm = new JPanel();
						GroupLayout gl_localSettings = new GroupLayout(localSettings);
						gl_localSettings.setHorizontalGroup(
							gl_localSettings.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_localSettings.createSequentialGroup()
									.addComponent(localForm, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
									.addContainerGap())
						);
						gl_localSettings.setVerticalGroup(
							gl_localSettings.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_localSettings.createSequentialGroup()
									.addComponent(localForm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addContainerGap(105, Short.MAX_VALUE))
						);
						GridBagLayout gbl_localForm = new GridBagLayout();
						gbl_localForm.columnWidths = new int[]{78, 135, 0};
						gbl_localForm.rowHeights = new int[]{15, 21, 23, 0};
						gbl_localForm.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
						gbl_localForm.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
						localForm.setLayout(gbl_localForm);
						
						JLabel localPort = new JLabel("Current Port:");
						GridBagConstraints gbc_localPort = new GridBagConstraints();
						gbc_localPort.fill = GridBagConstraints.HORIZONTAL;
						gbc_localPort.insets = new Insets(0, 0, 5, 5);
						gbc_localPort.gridx = 0;
						gbc_localPort.gridy = 0;
						localForm.add(localPort, gbc_localPort);
						
						localPort_Get = new JLabel("Default(1520)");
						GridBagConstraints gbc_localPort_Get = new GridBagConstraints();
						gbc_localPort_Get.fill = GridBagConstraints.HORIZONTAL;
						gbc_localPort_Get.insets = new Insets(0, 0, 5, 0);
						gbc_localPort_Get.gridx = 1;
						gbc_localPort_Get.gridy = 0;
						localForm.add(localPort_Get, gbc_localPort_Get);
						
						JLabel localPort_Change = new JLabel("Change port:");
						GridBagConstraints gbc_localPort_Change = new GridBagConstraints();
						gbc_localPort_Change.anchor = GridBagConstraints.WEST;
						gbc_localPort_Change.insets = new Insets(0, 0, 5, 5);
						gbc_localPort_Change.gridx = 0;
						gbc_localPort_Change.gridy = 1;
						localForm.add(localPort_Change, gbc_localPort_Change);
						
						localPort_Change_Input = new JTextField();
						GridBagConstraints gbc_localPort_Change_Input = new GridBagConstraints();
						gbc_localPort_Change_Input.fill = GridBagConstraints.HORIZONTAL;
						gbc_localPort_Change_Input.insets = new Insets(0, 0, 5, 0);
						gbc_localPort_Change_Input.gridx = 1;
						gbc_localPort_Change_Input.gridy = 1;
						localForm.add(localPort_Change_Input, gbc_localPort_Change_Input);
						localPort_Change_Input.setColumns(10);
						
						//LocalPort_Change_btn
						localPort_Change_btn = new JButton("Confirm");
						localPort_Change_btn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								//LocalPort_Change_btn clicked event
								Controller.localPort_Change_btn();
							}
						});
						
								GridBagConstraints gbc_localPort_Change_btn = new GridBagConstraints();
								gbc_localPort_Change_btn.gridx = 1;
								gbc_localPort_Change_btn.gridy = 2;
								localForm.add(localPort_Change_btn, gbc_localPort_Change_btn);
								localSettings.setLayout(gl_localSettings);
								
								JPanel remoteForm = new JPanel();
								GridBagLayout gbl_remoteForm = new GridBagLayout();
								gbl_remoteForm.columnWidths = new int[]{72, 135, 0};
								gbl_remoteForm.rowHeights = new int[]{21, 21, 0, 0};
								gbl_remoteForm.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
								gbl_remoteForm.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
								remoteForm.setLayout(gbl_remoteForm);
								
								JLabel remoteIPAddress_Change = new JLabel("IP address:");
								GridBagConstraints gbc_remoteIPAddress_Change = new GridBagConstraints();
								gbc_remoteIPAddress_Change.fill = GridBagConstraints.HORIZONTAL;
								gbc_remoteIPAddress_Change.insets = new Insets(0, 0, 5, 5);
								gbc_remoteIPAddress_Change.gridx = 0;
								gbc_remoteIPAddress_Change.gridy = 0;
								remoteForm.add(remoteIPAddress_Change, gbc_remoteIPAddress_Change);
								
								remoteIPAddress_Change_Input = new JTextField();
								GridBagConstraints gbc_remoteIPAddress_Change_Input = new GridBagConstraints();
								gbc_remoteIPAddress_Change_Input.fill = GridBagConstraints.HORIZONTAL;
								gbc_remoteIPAddress_Change_Input.insets = new Insets(0, 0, 5, 0);
								gbc_remoteIPAddress_Change_Input.gridx = 1;
								gbc_remoteIPAddress_Change_Input.gridy = 0;
								remoteForm.add(remoteIPAddress_Change_Input, gbc_remoteIPAddress_Change_Input);
								remoteIPAddress_Change_Input.setColumns(10);
								
								JLabel remotePort_Change = new JLabel("Remote port:");
								GridBagConstraints gbc_remotePort_Change = new GridBagConstraints();
								gbc_remotePort_Change.fill = GridBagConstraints.HORIZONTAL;
								gbc_remotePort_Change.insets = new Insets(0, 0, 5, 5);
								gbc_remotePort_Change.gridx = 0;
								gbc_remotePort_Change.gridy = 1;
								remoteForm.add(remotePort_Change, gbc_remotePort_Change);
								
								remotePort_Change_Input = new JTextField();
								GridBagConstraints gbc_remotePort_Change_Input = new GridBagConstraints();
								gbc_remotePort_Change_Input.insets = new Insets(0, 0, 5, 0);
								gbc_remotePort_Change_Input.fill = GridBagConstraints.HORIZONTAL;
								gbc_remotePort_Change_Input.gridx = 1;
								gbc_remotePort_Change_Input.gridy = 1;
								remoteForm.add(remotePort_Change_Input, gbc_remotePort_Change_Input);
								remotePort_Change_Input.setColumns(10);
								GroupLayout gl_remoteSettings = new GroupLayout(remoteSettings);
								gl_remoteSettings.setHorizontalGroup(
									gl_remoteSettings.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_remoteSettings.createSequentialGroup()
											.addComponent(remoteForm, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
											.addContainerGap())
								);
								gl_remoteSettings.setVerticalGroup(
									gl_remoteSettings.createParallelGroup(Alignment.LEADING)
										.addComponent(remoteForm, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
								);
								
								remoteSettings_Change_btn = new JButton("Confirm");
								remoteSettings_Change_btn.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										Controller.remoteSettings_Change_btn();
									}
								});
								GridBagConstraints gbc_remoteSettings_Change_btn = new GridBagConstraints();
								gbc_remoteSettings_Change_btn.gridx = 1;
								gbc_remoteSettings_Change_btn.gridy = 2;
								remoteForm.add(remoteSettings_Change_btn, gbc_remoteSettings_Change_btn);
								remoteSettings.setLayout(gl_remoteSettings);
								connectionSettings.setLayout(gl_connectionSettings);
						
						JPanel application = new JPanel();
						tabbedPane.addTab("Application", null, application, null);
		setBackground(new Color(240, 240, 240));
	}
	
	public void LinkWithController(){
		
	}
}
