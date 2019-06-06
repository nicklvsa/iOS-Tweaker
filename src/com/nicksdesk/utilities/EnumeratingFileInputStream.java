package com.nicksdesk.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EnumeratingFileInputStream extends FileInputStream {

	private long count;
	
	public EnumeratingFileInputStream(File file) throws FileNotFoundException {
		super(file);
	}
	
	public EnumeratingFileInputStream(String name) throws FileNotFoundException {
		super(name);
	}
	
	public long getCount() {
		return this.count;
	}
	
	@Override 
	public int read() throws IOException {
		count++;
		return super.read();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		count += b.length;
		return super.read(b);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		count += len;
		return super.read(b, off, len);
	}
	
}
