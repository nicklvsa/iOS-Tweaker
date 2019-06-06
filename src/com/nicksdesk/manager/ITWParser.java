package com.nicksdesk.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ITWParser {

	private String path = null;
	
	public ITWParser(String path) {
		this.path = path;
	}
	
	public HashMap<Object, Object> parse() {
		ArrayList<String> use = new ArrayList<String>();
		if(path != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
				HashMap<Object, Object> returnable = new HashMap<Object, Object>();
				String line;
				while((line = reader.readLine()) != null) {
					if(line.startsWith("//")) continue;
					if(line.startsWith("#")) continue;
					
					if(line.startsWith("using")) use.add(line.substring(line.indexOf("using")+5, line.indexOf(";")));
					if(line.startsWith("-")) {
						switch(line.substring(line.indexOf("-")+1, line.indexOf("(")).trim()) {
							case "HOOK_CONFIG":
								for(int i = 0; i < use.size(); i++) {
									switch(use.get(i).trim()) {
										case "itw":
											line = reader.readLine();
											String type = line.substring(line.indexOf("type:")+5).trim();
											line = reader.readLine();
											String path = line.substring(line.indexOf("path:")+5).trim();
											returnable.put(type, path);
										break;
										default:
											returnable.put(new Object(), new Object());
										break;
									}
								}
							break;
							default:
								reader.close();
							continue;
						}
					} else {
						continue;
					}
					reader.close();
					return returnable;
				}
			} catch(Exception e) {
				e.printStackTrace();
				return new HashMap<Object, Object>();
			}
		} else {
			return new HashMap<Object, Object>();
		}
		return new HashMap<Object, Object>();
	}
	
}
