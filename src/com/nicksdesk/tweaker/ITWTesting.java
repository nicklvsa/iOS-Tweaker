package com.nicksdesk.tweaker;

import java.util.HashMap;

import com.nicksdesk.manager.ITWParser;

public class ITWTesting {

	public static void main(String[] args) {
		ITWParser parser = new ITWParser(System.getProperty("user.home") + "\\Desktop\\" + "testing.itw");
		HashMap<Object, Object> response = parser.parse();
		System.out.println(response.toString());
	}
}
