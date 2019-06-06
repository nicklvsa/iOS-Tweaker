package com.nicksdesk.tweaker;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.nicksdesk.manager.Console;
import com.nicksdesk.manager.DEB;
import com.nicksdesk.manager.JAPT;
import com.nicksdesk.manager.Worker;
import com.nicksdesk.utilities.ArrayListModel;
import com.nicksdesk.utilities.OS;

public class TweakManagerFrame extends JFrame {

	private static final long serialVersionUID = 123049322668358907L;
	
	public JAPT apt;
	private String currentPackageSelected = null;
	private String currentDownloadSelected = null;
	
	private static ArrayListModel pkgModel;
	private static ArrayListModel dwnModel;
	
	private int tabState = 0;
		
    public TweakManagerFrame(JAPT apt) {
    	this.apt = apt;
        initComponents();
    }
    
    //TODO: remove
    @Deprecated
    public TweakManagerFrame() {
    	initComponents();
    }
    
    public void init() {
    	Worker.checkDeviceConnectionValidity(10000);
    	fillPackages(apt.debsList);
    	fillDownloads(apt.downloadsDir);
    	try {
    		Worker.createDaemon(new Runnable() {
				@Override
				public void run() {
					try {
						apt.updateSources(false);
						apt.readDatabase();
					} catch(Exception e) {
						Console.err(e.getMessage());
					}
				}
    		});
    	} catch(Exception e) {
    		Console.err(e.getMessage());
    	}
    }
                          
    private void initComponents() {
    	tweakListModel = new DefaultListModel<String>();
    	jProgressBar1 = new JProgressBar();
        buttonGroup1 = new ButtonGroup();
        jLabel1 = new JLabel();
        jPanel1 = new JPanel();
        jLabel2 = new JLabel();
        jLabel6 = new JLabel();
        selectTweaks = new JButton();
        jScrollPane1 = new JScrollPane();
        showTweaksList = new JList<>();
        installTweaks = new JButton();
        jLabel7 = new JLabel();
        packagesListPane = new JTabbedPane();
        downloadInstallSelectedTweakSwitching = new JButton();
        packagesScrollPane = new JScrollPane();
        listPackages = new JList<>();
        downloadedScrollPane = new JScrollPane();
        listDownloads = new JList<>();
        jPanel3 = new JPanel();
        manageSources = new JButton();
        reloadDB = new JButton();
        updateSources = new JButton();
        jSeparator2 = new JSeparator();
        jPanel2 = new JPanel();
        jLabel3 = new JLabel();
        knowWhatImDoing = new JRadioButton();
        dontKnowWhatImDoing = new JRadioButton();
        commandField = new JTextField();
        sendCommand = new JButton();
        jLabel4 = new JLabel();
        respringButton = new JButton();
        uicacheButton = new JButton();
        shutdownButton = new JButton();
        rebootButton = new JButton();
        jLabel5 = new JLabel();
        quitButton = new JButton();
        backButton = new JButton();
        commandHelpButton = new JButton();
        jSeparator1 = new JSeparator();

        jProgressBar1.setEnabled(false);
        jProgressBar1.setToolTipText("This will be enabled in future releases!");
        
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); 
        jLabel1.setText("iTweaker - Device Manager");

        jPanel1.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); 
        jLabel2.setText("Tweak Center");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        jLabel6.setText("Install Tweak");

        selectTweaks.setText("Select Deb");
        selectTweaks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                selectTweaksActionPerformed(evt);
            }
        });

        listPackages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPackages.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent evt) {
        		listPackagesValueChanged(evt);
        	}
        });
        
        listDownloads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDownloads.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent evt) {
        		listDownloadsValueChanged(evt);
            }
        });
        
        showTweaksList.setModel(tweakListModel);
        jScrollPane1.setViewportView(showTweaksList);

        installTweaks.setText("Install Tweaks!");
        installTweaks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                installTweaksActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        jLabel7.setText("Package Manager");

        packagesListPane.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent evt) {
        		packagesListPaneStateChanged(evt);
        	}
        });
        
        jProgressBar1.setStringPainted(true);
        
        packagesScrollPane.setViewportView(listPackages);

        packagesListPane.addTab("Packages", packagesScrollPane);

        downloadedScrollPane.setViewportView(listDownloads);

        packagesListPane.addTab("Downloaded", downloadedScrollPane);

        jPanel3.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        manageSources.setText("Manage Sources");
        manageSources.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                manageSourcesActionPerformed(evt);
            }
        });

        reloadDB.setText("Reload");
        reloadDB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reloadDBActionPerformed(evt);
            }
        });

        updateSources.setText("Update");
        updateSources.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateSourcesActionPerformed(evt);
            }
        });

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(manageSources, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(reloadDB, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateSources, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(manageSources)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(reloadDB)
                    .addComponent(updateSources))
                .addGap(19, 19, 19))
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jLabel6)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2, GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addComponent(selectTweaks, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(installTweaks, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(22, 22, 22)))
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(124, 124, 124))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(packagesListPane, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(downloadInstallSelectedTweakSwitching, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(selectTweaks)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(installTweaks)
                        .addGap(16, 16, 16)
                        .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(packagesListPane, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(downloadInstallSelectedTweakSwitching)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        jPanel2.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); 
        jLabel3.setText("Command Center");

        downloadInstallSelectedTweakSwitching.setText("Download Selected Tweak");
        downloadInstallSelectedTweakSwitching.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                downloadInstallSelectedTweakSwitchingActionPerformed(evt);
            }
        });
        
        buttonGroup1.add(knowWhatImDoing);
        knowWhatImDoing.setText("I know what I'm doing.");
        knowWhatImDoing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                knowWhatImDoingActionPerformed(evt);
            }
        });

        buttonGroup1.add(dontKnowWhatImDoing);
        dontKnowWhatImDoing.setText("I have no idea what I'm doing.");
        dontKnowWhatImDoing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dontKnowWhatImDoingActionPerformed(evt);
            }
        });

        sendCommand.setText("Execute!");
        sendCommand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendCommandActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        jLabel4.setText("Quick Commands");

        respringButton.setText("Respring");
        respringButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                respringButtonActionPerformed(evt);
            }
        });

        uicacheButton.setText("UICache");
        uicacheButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                uicacheButtonActionPerformed(evt);
            }
        });

        shutdownButton.setText("Shutdown");
        shutdownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                shutdownButtonActionPerformed(evt);
            }
        });

        rebootButton.setText("Reboot");
        rebootButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                rebootButtonActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); 
        jLabel5.setText("More quick commands coming soon...");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(commandField)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sendCommand, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                    .addComponent(dontKnowWhatImDoing)
                    .addComponent(jLabel3)
                    .addComponent(knowWhatImDoing)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(respringButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(uicacheButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(shutdownButton)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rebootButton)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(knowWhatImDoing)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dontKnowWhatImDoing)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(commandField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendCommand))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(respringButton)
                    .addComponent(uicacheButton)
                    .addComponent(shutdownButton)
                    .addComponent(rebootButton))
                .addGap(18, 18, 18)
                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        quitButton.setText("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        backButton.setText("Go Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        commandHelpButton.setText("Help");
        commandHelpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                commandHelpButtonActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(quitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(backButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(commandHelpButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(commandHelpButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quitButton))
                    .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        this.pack();
    }                   

    private void knowWhatImDoingActionPerformed(ActionEvent evt) {                                                
        dontKnowWhatImDoing.setSelected(false);
        knowWhatImDoing.setSelected(true);
        sendCommand.setEnabled(true);
        commandField.setEditable(true);
    }                                               

    private void dontKnowWhatImDoingActionPerformed(ActionEvent evt) {                                                    
        dontKnowWhatImDoing.setSelected(true);
        knowWhatImDoing.setSelected(false);
        sendCommand.setEnabled(false);
        commandField.setEditable(false);
    }      
    
    private void packagesListPaneStateChanged(ChangeEvent evt) {
    	tabState = packagesListPane.getSelectedIndex();
    	switch(tabState) {
    		case 0:
    			downloadInstallSelectedTweakSwitching.setText("Download Selected Tweak");
    		break;
    		case 1:
    			Worker.createDaemon(new Runnable() {
					@Override
					public void run() {
						try {
		    				apt.readDatabase();
		    			} catch(Exception e) {
		    				Console.err(e.getMessage());
		    			}
					}
    			});
    			downloadInstallSelectedTweakSwitching.setText("Install Selected Tweak");
    		break;
    	}
    }

    private void installTweaksActionPerformed(ActionEvent evt) {                                              
        installTweaks.setEnabled(false);
        Worker.createDaemon(new Runnable() {
    	   public void run() {
    		   if(!Main.getFiles().isEmpty()) {
    	        	Main.sendFiles();
    	        	installTweaks.setEnabled(true);
    	        	JOptionPane.showMessageDialog(null, "Files sent over successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
    	       } else {
    	        	JOptionPane.showMessageDialog(null, "Please select at least one tweak to install!", "Error!", JOptionPane.ERROR_MESSAGE);
    	        	installTweaks.setEnabled(true);
    	       }
    	   }
       });
    }                                             

    private void shutdownButtonActionPerformed(ActionEvent evt) {                                               
        shutdownButton.setEnabled(false);
    	uicacheButton.setEnabled(false);
    	respringButton.setEnabled(false);
    	rebootButton.setEnabled(false);
    	Worker.createDaemon(new Runnable() {
        	public void run() {
        		String response = Main.sendCommand("halt");
        		if(response != null) {
        			shutdownButton.setEnabled(true);
        	    	uicacheButton.setEnabled(true);
        	    	respringButton.setEnabled(true);
        	    	rebootButton.setEnabled(true);
        	    	JOptionPane.showMessageDialog(null, "iDevice is shutting down...", "Done!", JOptionPane.INFORMATION_MESSAGE);
        		} else {
        			JOptionPane.showMessageDialog(null, "Command did not execute!", "Error!", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        });
    }                                              

    private void rebootButtonActionPerformed(ActionEvent evt) {                                             
        shutdownButton.setEnabled(false);
    	uicacheButton.setEnabled(false);
    	respringButton.setEnabled(false);
    	rebootButton.setEnabled(false);
    	Worker.createDaemon(new Runnable() {
        	public void run() {
        		String response = Main.sendCommand("reboot");
        		if(response != null) {
        			shutdownButton.setEnabled(true);
        	    	uicacheButton.setEnabled(true);
        	    	respringButton.setEnabled(true);
        	    	rebootButton.setEnabled(true);
        	    	JOptionPane.showMessageDialog(null, "iDevice is rebooting...", "Done!", JOptionPane.INFORMATION_MESSAGE);
        		} else {
        			JOptionPane.showMessageDialog(null, "Command did not execute!", "Error!", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        });
    }                       
    
    private void listPackagesValueChanged(ListSelectionEvent evt) {
    	currentPackageSelected = pkgModel.getElementAt(listPackages.getSelectedIndex());
    }
    
    private void listDownloadsValueChanged(ListSelectionEvent evt) {
    	currentDownloadSelected = dwnModel.getElementAt(listDownloads.getSelectedIndex());
    }
    
    private void downloadInstallSelectedTweakSwitchingActionPerformed(ActionEvent evt) {                                                                      
        switch(tabState) {
        	case 0:
        		try {
        			String pkgName = listPackages.getSelectedValue();
            		apt.downloadDeb(pkgName, true);
        		} catch(Exception e) {
        			Console.err(e.getMessage());
        		}
        	break;
        	case 1:
        		if(currentDownloadSelected.contains(".deb")) currentDownloadSelected = currentDownloadSelected.substring(0, currentDownloadSelected.length() - 4);
        		if(!apt.hasPackage(currentDownloadSelected)) {
        			JOptionPane.showMessageDialog(null, "The package " + currentDownloadSelected + " does not exist!", "Error!", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		downloadInstallSelectedTweakSwitching.setEnabled(false);
        		Worker.createDaemon(new Runnable() {
					@Override
					public void run() {
						try {
		        			ArrayList<String> deps = new ArrayList<String>();
		            		deps = apt.getDependencies(currentDownloadSelected);
		            		Collections.reverse(deps);
		            		for(String f : deps) {
		            			f = (Main.DATA_LOC + "downloads" + File.separator + f) + ".deb";
		            			Main.getFiles().add(new File(f));
		            		}
		            		Main.sendFiles();
		        		} catch(Exception e) {
		        			e.printStackTrace();
		        			downloadInstallSelectedTweakSwitching.setEnabled(true);
		        		}
					}
        		});
        	break;
        }
    }                                                                     

    private void uicacheButtonActionPerformed(ActionEvent evt) {                                              
        shutdownButton.setEnabled(false);
    	uicacheButton.setEnabled(false);
    	respringButton.setEnabled(false);
    	rebootButton.setEnabled(false);
    	Worker.createDaemon(new Runnable() {
        	public void run() {
        		String response = Main.sendCommand("uicache");
        		if(response != null) {
        			shutdownButton.setEnabled(true);
        	    	uicacheButton.setEnabled(true);
        	    	respringButton.setEnabled(true);
        	    	rebootButton.setEnabled(true);
        	    	JOptionPane.showMessageDialog(null, "iDevice is refreshing springboard...", "Done!", JOptionPane.INFORMATION_MESSAGE);
        		} else {
        			JOptionPane.showMessageDialog(null, "Command did not execute!", "Error!", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        });
    }                                             

    private void respringButtonActionPerformed(ActionEvent evt) {                                               
        shutdownButton.setEnabled(false);
    	uicacheButton.setEnabled(false);
    	respringButton.setEnabled(false);
    	rebootButton.setEnabled(false);
    	Worker.createDaemon(new Runnable() {
        	public void run() {
        		String response = Main.sendCommand("killall -HUP SpringBoard");
        		if(response != null) {
        			shutdownButton.setEnabled(true);
        	    	uicacheButton.setEnabled(true);
        	    	respringButton.setEnabled(true);
        	    	rebootButton.setEnabled(true);
        	    	JOptionPane.showMessageDialog(null, "iDevice is respringing...", "Done!", JOptionPane.INFORMATION_MESSAGE);
        		} else {
        			JOptionPane.showMessageDialog(null, "Command did not execute!", "Error!", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        });
    }                                              

    private void sendCommandActionPerformed(ActionEvent evt) {                                            
        sendCommand.setEnabled(false);
    	Worker.createDaemon(new Runnable() {
        	public void run() {
        		String command = commandField.getText();
                if(!command.equalsIgnoreCase("") && command != null) {
                	String response = Main.sendCommand(command);
                	sendCommand.setEnabled(true);
                	JOptionPane.showMessageDialog(null, response, "Command Response:", JOptionPane.INFORMATION_MESSAGE);
                	commandField.setText("");
                } else {
                	JOptionPane.showMessageDialog(null, "You left the command field empty!", "Error!", JOptionPane.ERROR_MESSAGE);
                	sendCommand.setEnabled(true);
                	commandField.setText("");
                }
        	}
        });
    }                                           

    private void commandHelpButtonActionPerformed(ActionEvent evt) {                                                  
        try {
            Desktop.getDesktop().browse(new URL("https://www.google.com/search?q=linux+terminal+commands&oq=linux+terminal+commands&aqs=chrome..69i57.4069j0j9&sourceid=chrome&ie=UTF-8").toURI());
    	} catch(Exception e) {
            e.printStackTrace();
    	}
    }                                                 

    private void backButtonActionPerformed(ActionEvent evt) {                                           
        Main.getFiles().clear();
        TweakerFrame tweaker = new TweakerFrame(this.apt);
        tweaker.setLocationRelativeTo(null);
        tweaker.setResizable(false);
        tweaker.setVisible(true);
        TweakerFrame.setupPanel.setVisible(false);
        TweakerFrame.setupButton.setEnabled(false);
        TweakerFrame.manageButton.setEnabled(false);
        this.dispose();
    }                                          

    private void quitButtonActionPerformed(ActionEvent evt) {                                           
        Main.closeConnection();
        System.exit(0);
        //OS.restart();
    }                                          

    private void selectTweaksActionPerformed(ActionEvent evt) {                                             
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //TODO: add custom file extension for easier tweak installation
        //chooser.addChoosableFileFilter(new FileNameExtensionFilter("iTweaker File (.itw)", "zip"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Debian File (.deb)", "deb"));
        chooser.setAcceptAllFileFilterUsed(false);
        int response = chooser.showOpenDialog(this);
        if(response == JFileChooser.APPROVE_OPTION) {
        	File tweak = new File(chooser.getSelectedFile().getAbsolutePath());
        	Main.getFiles().add(tweak);
        	tweakListModel.addElement(chooser.getSelectedFile().getName());
        } else {
        	JOptionPane.showMessageDialog(null, "Please select a valid iTweaker file or valid Debian file!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setPBMax(long max) {
    	jProgressBar1.setMaximum((int)max);
    }
    
    public void resetPB() {
    	jProgressBar1.setMaximum(100);
        jProgressBar1.setValue(0);
    }
    
    public void setProgressBar(long percent) {
        jProgressBar1.setValue((int) percent);
    }
    
    public static void fillPackages(ArrayList<DEB> pkgs) {
    	ArrayList<String> s = new ArrayList<String>();
    	for(DEB deb : pkgs) {
    		s.add(deb.packageName);
    	}
    	fillPackagesWith(s);
    }
    
    public static void fillDownloads(File dir) {
    	ArrayListModel lm = new ArrayListModel();
    	lm.setData(dir.list());
    	dwnModel = lm;
    	listDownloads.setModel(lm);
    }
    
    public static void fillPackagesWith(ArrayList<String> pkgs) {
    	ArrayListModel lm = new ArrayListModel();
    	lm.setData(pkgs);
    	pkgModel = lm;
    	listPackages.setModel(lm);
    }

    private void manageSourcesActionPerformed(ActionEvent evt) {                                              
        String source = JOptionPane.showInputDialog(null, "", "Enter a new Cydia source:", JOptionPane.INFORMATION_MESSAGE);
        try {
        	if(!source.isEmpty() && !source.equals("")) {
            	apt.addSource(source);
            	
            } else {
            	JOptionPane.showMessageDialog(null, "Please enter a valid source!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e) {
        	Console.err(e.getMessage());
        }
    }                                             

    private void reloadDBActionPerformed(ActionEvent evt) {                                         
        Worker.createDaemon(new Runnable() {
			@Override
			public void run() {
				try {
					apt.readDatabase();
				} catch(Exception e) {
					Console.err(e.getMessage());
				}
			}
        });
    }                                        

    private void updateSourcesActionPerformed(ActionEvent evt) {                                              
        Worker.createDaemon(new Runnable() {
			@Override
			public void run() {
				try {
					apt.updateSources(true);
				} catch(Exception e) {
					Console.err(e.getMessage());
				}
			}
        });
    }                                             
                    
    public static JButton backButton;
    public ButtonGroup buttonGroup1;
    public JTextField commandField;
    public static JButton commandHelpButton;
    public JRadioButton dontKnowWhatImDoing;
    public JScrollPane downloadedScrollPane;
    public static JButton downloadInstallSelectedTweakSwitching;
    public static JButton installTweaks;
    public JLabel jLabel1;
    public JLabel jLabel2;
    public JLabel jLabel3;
    public JLabel jLabel4;
    public JLabel jLabel5;
    public JLabel jLabel6;
    public JLabel jLabel7;
    public JPanel jPanel1;
    public JPanel jPanel2;
    public JPanel jPanel3;
    public JScrollPane jScrollPane1;
    public JSeparator jSeparator1;
    public JSeparator jSeparator2;
    public JRadioButton knowWhatImDoing;
    public static JList<String> listDownloads;
    public static JList<String> listPackages;
    public static JButton manageSources;
    public static JTabbedPane packagesListPane;
    public static JScrollPane packagesScrollPane;
    public static JButton quitButton;
    public static JButton rebootButton;
    public static JButton reloadDB;
    public static JButton respringButton;
    public static JButton selectTweaks;
    public JButton sendCommand;
    public JList<String> showTweaksList;
    public static JButton shutdownButton;
    public static JButton uicacheButton;
    public static JButton updateSources;
    public JProgressBar jProgressBar1;
    public DefaultListModel<String> tweakListModel;
                   
}
