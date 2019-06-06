package com.nicksdesk.utilities;

import java.io.File;
import java.io.IOException;

import com.nicksdesk.manager.Console;

public class FileUtils {

	public static void checkFiles(File... files) throws IOException {
		for(File file : files) {
			Console.log(file);
			if(!file.exists()) {
				file.createNewFile();
			}
		}
	}
	
	public static void checkDirs(File... dirs) {
		for(File dir : dirs) {
			Console.log(dir);
			if(!dir.exists()) {
				 dir.mkdirs();
			}
		}
	}
	
	public static long getFolderSize(File dir) {
		long size = 0;
		if(dir.isDirectory()) {
			for(File f : dir.listFiles()) {
				if(f.isFile()) {
					size += f.length();
				} else {
					size += getFolderSize(f);
				}
			}
		} else if(dir.isFile()) {
			size += dir.length();
		} else {
			return -1;
		}
		return size;
	}
	
}
