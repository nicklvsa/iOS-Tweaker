package com.nicksdesk.utilities;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.nicksdesk.manager.Console;
import com.nicksdesk.tweaker.Main;

public class JBDetector {

	private static boolean getJailbreak(String jbPath) {
		if(Main.didMakeConnection()) {
			Main.sftp = (ChannelSftp) Main.channel;
			try {
				Main.sftp.lstat(jbPath);
				return true;
			} catch(SftpException e) {
				if(e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
					return false;
				}
			}
		} else {
			return false;
		}
		return false;
	}
	
	private static String checkiOSVersion() {
		if(Main.didMakeConnection()) {
			try {
				String response = Main.sendCommand("sw_vers");
				return response.split("ProductVersion:")[1];
			} catch(Exception e) {
				Console.err(e.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}

	public static boolean isElectra11() {
		return getJailbreak("/usr/lib/TweakInject/");
	}
	
	public static boolean isUncover12() {
		System.out.println(checkiOSVersion());
		return (getJailbreak("/usr/lib/substrate/") && getJailbreak("/var/lib/undecimus/"));
	}
	
	public static boolean isDoubleHelix10() {
		//TODO: add ios 10 support
		return false;
	}
	
}
