package com.nicksdesk.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Database {

	private Statement stat;
	private PreparedStatement pstat;
	private Connection conn;
	
	private String url;
	private String user;
	private String pass;
	
	public Statement getStat() {
		try {
			if(conn.isClosed()) {
				this.buildConnection(url, user, pass);
			}
			return stat;
		} catch(Exception e) {
			Console.err(e.getMessage());
			return null;
		}
	}
	
	public PreparedStatement getPreparedStat(String sql) {
		try {
			if(conn.isClosed()) {
				this.buildConnection(url, user, pass);
			}
			conn.setAutoCommit(false);
			if(pstat == null) {
				pstat = conn.prepareStatement(sql);
			}
			return pstat;
		} catch(Exception e) {
			Console.err(e.getMessage());
			return null;
		}
	}
	
	public void closePreparedStat() throws Exception {
		pstat.executeBatch();
		pstat.close();
		pstat = null;
		conn.setAutoCommit(true);
	}
	
	public void buildConnection(String url, String user, String pass) {
		try {
			this.url = url;
			this.user = user;
			this.pass = pass;
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(url);
			stat = conn.createStatement();
		} catch(Exception e) {
			Console.err(e.getMessage());
		}
	}
	
	public void shutdown() {
		try {
			stat.close();
			conn.close();
		} catch(Exception e) {
			Console.err(e.getMessage());
		}
	}
	
}
