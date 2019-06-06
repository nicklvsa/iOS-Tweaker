package com.nicksdesk.utilities;

import java.io.File;
import java.util.ArrayList;

import com.nicksdesk.manager.Console;

public class OS {
	
	private static String name = System.getProperty("os.name");
	
	public static boolean isWindows() {
		return name.contains("Win");
	}
	
	public static boolean isMac() {
		return name.contains("Mac");
	}
	
	public static boolean isRuntimeJar() {
		return !System.getProperty("java.class.path").contains("eclipse");
	}
	
	public static void restart() {
		try {
			final String home = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			final File itweaker = new File(OS.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if(!itweaker.getName().endsWith(".jar")) return;
			final ArrayList<String> cmd = new ArrayList<String>();
			cmd.add(home);
			cmd.add("-jar");
			cmd.add(itweaker.getPath());
			final ProcessBuilder p = new ProcessBuilder(cmd);
			p.start();
			System.exit(0);
		} catch(Exception e) {
			Console.err(e.getMessage());
		}
	}
}
