package com.nicksdesk.manager;

public class Console {

	private static boolean doErrOutput = false;
	private static boolean doLogOutput = true;
	
	public static void log(Object message) {
		if(doLogOutput) {
			System.out.println(message);
		}
	}
	
	public static void err(Object message) {
		if(doErrOutput) {
			System.err.println(message);
		}
	}
	
	public static void setDoErrOutput(boolean errOut) {
		doErrOutput = errOut;
	}
	
	public static void setDoLogOutput(boolean logOut) {
		doLogOutput = logOut;
	}
	
}