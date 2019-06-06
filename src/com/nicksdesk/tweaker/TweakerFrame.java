package com.nicksdesk.tweaker;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.nicksdesk.manager.JAPT;
import com.nicksdesk.manager.Worker;

/**
 *
 * @author Nicklvsa
 */
public class TweakerFrame extends JFrame {

	public JAPT apt;
	private static final long serialVersionUID = -8277375691651700479L;
	
	public TweakerFrame(JAPT apt) {
		this.apt = apt;
        initComponents();
    }
           
    private void initComponents() {
        quitButton = new JButton();
        setupButton = new JButton();
        testConnectionButton = new JButton();
        manageButton = new JToggleButton();
        jLabel1 = new JLabel();
        setupPanel = new JPanel();
        deviceIPField = new JTextField();
        sshPortField = new JTextField();
        deviceUsernameField = new JTextField();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        devicePasswordField = new JPasswordField();
        saveDeviceConfigButton = new JToggleButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        quitButton.setText("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        setupButton.setText("Setup");
        setupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setupButtonActionPerformed(evt);
            }
        });

        testConnectionButton.setText("Test Connection");
        testConnectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                testConnectionButtonActionPerformed(evt);
            }
        });

        manageButton.setText("Tweak Manager");
        manageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                manageButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Tweak Installer for Electra");

        setupPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        jLabel2.setText("Device IP:");
        jLabel3.setText("SSH Port:");
        jLabel4.setText("Device Username:");
        jLabel5.setText("Device Password:");

        saveDeviceConfigButton.setText("Save Device Configuration");
        saveDeviceConfigButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveDeviceConfigButtonActionPerformed(evt);
            }
        });

        GroupLayout setupPanelLayout = new GroupLayout(setupPanel);
        setupPanel.setLayout(setupPanelLayout);
        setupPanelLayout.setHorizontalGroup(
            setupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(setupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(saveDeviceConfigButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(setupPanelLayout.createSequentialGroup()
                        .addGroup(setupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(setupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(deviceUsernameField)
                            .addComponent(sshPortField)
                            .addComponent(deviceIPField)))
                    .addGroup(setupPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(devicePasswordField)))
                .addContainerGap())
        );
        setupPanelLayout.setVerticalGroup(
            setupPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(setupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(deviceIPField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(setupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(sshPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(setupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(deviceUsernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(setupPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(devicePasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveDeviceConfigButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(setupPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(quitButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(setupButton, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(testConnectionButton, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(manageButton, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(setupButton)
                    .addComponent(testConnectionButton)
                    .addComponent(manageButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(setupPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(quitButton)
                .addContainerGap())
        );
        this.pack();
    }                     

    private void setupButtonActionPerformed(ActionEvent evt) {                                            
    	Main.toggleSetup = !Main.toggleSetup;
    	setupPanel.setVisible(Main.toggleSetup);
    }                                           

    private void testConnectionButtonActionPerformed(ActionEvent evt) {                                                     
        testConnectionButton.setEnabled(false);
    	Worker.createDaemon(new Runnable() {
        	public void run() {
        		if(Main.didMakeConnection()) {
                	JOptionPane.showMessageDialog(null, "Successfully established a connection with your device!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                	testConnectionButton.setEnabled(true);
                	manageButton.setEnabled(true);
                	setupButton.setEnabled(false);
                } else {
                	JOptionPane.showMessageDialog(null, "Could not make a connection with your device, please check your config and try again!\nTo edit your device's info, if you're on a mac, go to your home folder, iTweaker, and edit config.itw\nIf you are on a windows pc, go to your documents folder, iTweaker, and edit config.itw", "Error!", JOptionPane.ERROR_MESSAGE);
                	if(!Main.threeTopButtons.isEmpty()) {
                		setupButton.setEnabled(true);
                		testConnectionButton.setEnabled(false);
                		manageButton.setEnabled(false);
                	}
                }
        	}
        });
    }                                                    

    private void manageButtonActionPerformed(ActionEvent evt) { 
    	TweakManagerFrame tweakManagerFrame = new TweakManagerFrame(this.apt);
    	tweakManagerFrame.init();
    	tweakManagerFrame.setLocationRelativeTo(null);
    	tweakManagerFrame.setResizable(false);
    	tweakManagerFrame.setVisible(true);
    	tweakManagerFrame.dontKnowWhatImDoing.setSelected(true);
    	tweakManagerFrame.knowWhatImDoing.setSelected(false);
    	tweakManagerFrame.sendCommand.setEnabled(false);
    	tweakManagerFrame.commandField.setEditable(false);
        this.dispose();
    }                                            

    private void saveDeviceConfigButtonActionPerformed(ActionEvent evt) {                                                       
        String ip = deviceIPField.getText();
        String port = sshPortField.getText();
        String user = deviceUsernameField.getText();
        String pass = new String(devicePasswordField.getPassword()).toString();
        if(!ip.isEmpty() && !port.isEmpty() && !user.isEmpty() && !pass.isEmpty()) {
        	try {
        		Main.getConfig().put("IP", ip);
            	Main.getConfig().put("PORT", port);
            	Main.getConfig().put("iUSER", user);
            	Main.getConfig().put("iPASS", pass);
            	Main.getConfig().store(new FileOutputStream(Main.CONFIG_LOC + "config.itw"), null);
            	Main.getConfig().load(new FileInputStream(Main.CONFIG_LOC + "config.itw"));
            	Main.updateConfig(Main.getConfig());
            	JOptionPane.showMessageDialog(null, "Successfully saved your device config!", "Success!", JOptionPane.INFORMATION_MESSAGE);
            	setupButton.setEnabled(false);
            	setupPanel.setVisible(false);
            	testConnectionButton.setEnabled(true);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        } else {
        	JOptionPane.showMessageDialog(null, "You left a field empty!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }                                                      

    private void quitButtonActionPerformed(ActionEvent evt) {                                           
        Main.closeConnection();
    	System.exit(0);
    }                                          
              
    public static JTextField deviceIPField;
    public static JPasswordField devicePasswordField;
    public static JTextField deviceUsernameField;
    public static JLabel jLabel1;
    public static JLabel jLabel2;
    public static JLabel jLabel3;
    public static JLabel jLabel4;
    public static JLabel jLabel5;
    public static JToggleButton manageButton;
    public static JButton quitButton;
    public static JToggleButton saveDeviceConfigButton;
    public static JButton setupButton;
    public static JPanel setupPanel;
    public static JTextField sshPortField;
    public static JButton testConnectionButton;                  
}
