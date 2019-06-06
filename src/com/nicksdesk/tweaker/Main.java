package com.nicksdesk.tweaker;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.nicksdesk.manager.Console;
import com.nicksdesk.manager.JAPT;
import com.nicksdesk.utilities.JBDetector;
import com.nicksdesk.utilities.OS;
import com.nicksdesk.utilities.ZipUtils;

public class Main {

	public static Session session = null;
	public static Channel channel = null;
	public static ChannelSftp sftp = null;
	
	public static boolean toggleSetup = false;
	
	private static Properties config = new Properties();
	public static String CONFIG_LOC = System.getProperty("user.home");
	public static String DATA_LOC = System.getProperty("user.home");

	private static String username = "";
	private static String password = "";
	private static int port = 2222;
	private static String ip = "";
	
	private static ArrayList<File> files = new ArrayList<File>();
	private static ArrayList<File> tweaks = new ArrayList<File>();
	public static ArrayList<Component> threeTopButtons = new ArrayList<Component>();
	
	
	public static void main(String[] args) {
		/*
		 * add args parsing for no gui and special commands
		 */
		if(args.length > 0) {	
			for(String s : args) {
				if(s.equalsIgnoreCase("--debug")) {
					Console.setDoErrOutput(true);
					Console.log("[DEBUG] iTweaker will now log errors!");
				}
				if(s.equalsIgnoreCase("--disable-log")) {
					Console.setDoLogOutput(false);
					Console.log("[LOG] iTweaker disabled logging!");
				}
				if(s.equalsIgnoreCase("--configure")) {
					Console.setDoLogOutput(true);
					Console.log("[LOG] iTweaker is running in configure mode.");
				}
				if(s.equalsIgnoreCase("--offline-mode")) {
					Console.setDoLogOutput(true);
					Console.log("[LOG] iTweaker will run in offline mode!");
				}
			}
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(!threeTopButtons.isEmpty()) threeTopButtons.clear();
		threeTopButtons.add(TweakerFrame.setupButton);
		threeTopButtons.add(TweakerFrame.testConnectionButton);
		threeTopButtons.add(TweakerFrame.manageButton);
		
		if(OS.isMac()) {
			CONFIG_LOC = CONFIG_LOC + File.separator + "iTweaker" + File.separator;
			DATA_LOC = CONFIG_LOC + "data" + File.separator;
		} else if(OS.isWindows()) {
			CONFIG_LOC = CONFIG_LOC + File.separator + "Documents" + File.separator + "iTweaker" + File.separator;
			DATA_LOC = CONFIG_LOC + "data" + File.separator;
		} else {
			JOptionPane.showMessageDialog(null, "You are runing iOS Tweaker on an unsupported operating system!", "Error!", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		File configPath = new File(CONFIG_LOC);
		File dataPath = new File(DATA_LOC);
		
		if(configPath.exists() && configPath.isDirectory()) {
			try {
				config.load(new FileInputStream(CONFIG_LOC + "config.itw"));
				if(!dataPath.exists()) {
					dataPath.mkdir();
				}
				if(!config.isEmpty()) {
					if(config.containsKey("IP")) ip = config.getProperty("IP", "127.0.0.1");
					if(config.containsKey("PORT")) port = Integer.parseInt(config.getProperty("PORT", "2222"));
					if(config.containsKey("iUSER")) username = config.getProperty("iUSER", "root");
					if(config.containsKey("iPASS")) password = config.getProperty("iPASS", "alpine");
					startFrame();
					TweakerFrame.setupButton.setEnabled(false);
					TweakerFrame.manageButton.setEnabled(false);
					TweakerFrame.testConnectionButton.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "Error loading device configuration, please check it and restart iTweaker!", "Error!", JOptionPane.ERROR_MESSAGE);
					if(OS.isRuntimeJar()) {
						OS.restart();
					} else {
						System.exit(0);
					}
				}
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Please make sure to click \"Setup\" inside iTweaker to setup your device info!", "Hello!", JOptionPane.WARNING_MESSAGE);
				startFrame();
				TweakerFrame.manageButton.setEnabled(false);
				TweakerFrame.testConnectionButton.setEnabled(false);
			}
		} else {
			configPath.mkdirs();
			startFrame();
			TweakerFrame.setupButton.setEnabled(false);
			TweakerFrame.manageButton.setEnabled(false);
			TweakerFrame.testConnectionButton.setEnabled(true);
			JOptionPane.showMessageDialog(null, "This is your first time running, iTweaker will not exit... please relaunch iTweaker after it quits!", "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public static Properties getConfig() {
		return config;
	}
	
	public static ArrayList<File> getFiles() {
		return files;
	}
	
	private static void startFrame() {
		JAPT apt = new JAPT();
		try {
			apt.init();
		} catch(Exception e) {
			Console.err(e.getMessage());
		}
		TweakerFrame tweaker = new TweakerFrame(apt);
		tweaker.setResizable(false);
		tweaker.setLocationRelativeTo(null);
		tweaker.setVisible(true);
		TweakerFrame.setupPanel.setVisible(false);
	}
	
	public static void sendFiles() {
		ArrayList<File> notValidFiles = new ArrayList<File>();
		try {
			if(didMakeConnection()) {
				if(files.size() <= 0 && !files.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please select at least one file!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					sftp = (ChannelSftp) channel;
					if(JBDetector.isElectra11()) {
						sftp.mkdir("/usr/lib/TweakInject/itweaker_temp");
				        sftp.cd("/usr/lib/TweakInject/itweaker_temp");
					} else if(JBDetector.isUncover12()) {
						sftp.mkdir("/usr/lib/itweaker_temp");
				        sftp.cd("/usr/lib/itweaker_temp");
					}
			        
					for(File f : files) {
						if(isValidTweak(f)) {
							if(f.getName().endsWith(".deb")) {
								sftp.put(new FileInputStream(f), f.getName());
							}
							if(!tweaks.isEmpty()) {
								for(File tweak : tweaks) {
									if(!tweak.isFile()) continue;
									if(tweak.getName().endsWith(".dylib") || tweak.getName().endsWith(".plist")) {
										sftp.put(new FileInputStream(tweak), tweak.getName());
									} else {
										continue;
									}
								}
							}
						} else {
							notValidFiles.add(f);
						}
						
					}
					/*String response = sendCommand("dpkg -i *.deb");
					if(response != null) {
						Console.log(response);
						
					}*/
					if(!notValidFiles.isEmpty()) JOptionPane.showMessageDialog(null, "Files: ("+notValidFiles.toString()+") were not valid and or not in the correct format! \n Please reformat the tweak zip!", "Error!", JOptionPane.ERROR_MESSAGE);
					if(notValidFiles.isEmpty()) notValidFiles.clear();
					if(JBDetector.isElectra11()) {
						sendCommand("rm -rf /usr/lib/TweakInject/itweaker_temp");
						if(!TweakManagerFrame.downloadInstallSelectedTweakSwitching.isEnabled()) {
							TweakManagerFrame.downloadInstallSelectedTweakSwitching.setEnabled(true);
						}
						sendCommand("killall -9 SpringBoard");
						JOptionPane.showMessageDialog(null, "Successfully installed tweak(s)!", "Finished!", JOptionPane.INFORMATION_MESSAGE);
					} else if(JBDetector.isUncover12()) {
						sendCommand("rm -rf /usr/lib/itweaker_temp");
						if(!TweakManagerFrame.downloadInstallSelectedTweakSwitching.isEnabled()) {
							TweakManagerFrame.downloadInstallSelectedTweakSwitching.setEnabled(true);
						}
						sendCommand("killall -9 SpringBoard");
						JOptionPane.showMessageDialog(null, "Successfully installed tweak(s)!", "Finished!", JOptionPane.INFORMATION_MESSAGE);
					}
					files.clear();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Error making a connection with the iDevice!", "Error", JOptionPane.ERROR_MESSAGE);
				Console.err("Error making connection!");
				System.exit(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection() {
		if(channel != null && session != null) {
			channel.disconnect();
			session.disconnect();
		}
	}
	
	public static boolean isValidTweak(File file) {
		try {
			if(file.getName().endsWith(".deb")) {
				return true;
			} else {
				String itweakerTempFolderName = "temp_dir";
				String itweakerPath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "iTweaker" + File.separator + itweakerTempFolderName;
				File itweakerTempPath = new File(itweakerPath);
				if(!itweakerTempPath.exists()) itweakerTempPath.mkdirs();
				Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(itweakerTempPath + File.separator + file.getName()));
				ZipUtils.extract(file, itweakerTempPath);
				File tweakDir = new File(itweakerTempPath + File.separator + file.getName().replaceAll((file.getName().contains(".itw")) ? ".itw" : ".zip", "").trim());
				if(tweakDir.isDirectory()) {
					for(File fl : tweakDir.listFiles()) {
						Console.log(fl);
						if(!fl.isFile()) continue;
						if(fl.getName().endsWith(".dylib") || fl.getName().endsWith(".plist")) {
							tweaks.add(fl);
							return true;
						} else {
							continue;
						}
					}
				} else {
					return false;
				}
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String sendCommand(String command) {
	     if(didMakeConnection()) {
	    	 	try {
	    	 		StringBuilder outputBuffer = new StringBuilder();
	    		    Channel chan = session.openChannel("exec");
	    		    ((ChannelExec)chan).setCommand(command);
	    		    InputStream commandOutput = chan.getInputStream();
	    		    chan.connect();
	    		    int readByte = commandOutput.read();
	    		    while(readByte != 0xffffffff) {
	    		         outputBuffer.append((char)readByte);
	    		         readByte = commandOutput.read();
	    		    }
	    		    chan.disconnect();
	    		    return outputBuffer.toString();
	    	 	} catch(Exception e) {
	    	 		Console.err(e.getMessage());
	    	 		return null;
	    	 	}
	     } else {
	    	 return null;
	     }
	}
	
	public static void updateConfig(Properties config) {
		if(!config.isEmpty() && config != null) {
			username = config.getProperty("iUSER", "root");
			password = config.getProperty("iPASS", "alpine");
			port = Integer.parseInt(config.getProperty("PORT", "2222"));
			ip = config.getProperty("IP", "127.0.0.1");
		}
	}
	
	public static boolean didMakeConnection() {
		try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, ip, port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            //TODO: current temporary fix for session reconnection issue
            if(!session.isConnected()) {
            	session.connect();
            	channel = session.openChannel("sftp");
                channel.connect();
            }
            return (session.isConnected() && channel.isConnected());
        } catch (Exception ex) {
        	channel.disconnect();
        	session.disconnect();
            Console.err(ex.getMessage());
            return false;
        }
	}
	
}
