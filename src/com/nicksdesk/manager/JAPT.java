package com.nicksdesk.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import com.nicksdesk.tweaker.Main;
import com.nicksdesk.tweaker.TweakManagerFrame;
import com.nicksdesk.utilities.EnumeratingFileInputStream;
import com.nicksdesk.utilities.FileUtils;

public class JAPT {

	public Database db;
	
	private File sourcesFile;
	private File dbFile;
	public File downloadsDir;
	private File tempDir;
	
	private long workingState = 0;
	private long finishedState = 0;
	
	public ArrayList<String> sourcesList = new ArrayList<String>();
	public ArrayList<DEB> debsList = new ArrayList<DEB>();
	private HashMap<String, DEB> hashDEBList = new HashMap<String, DEB>();
	
	private final String createTableStmt = "CREATE TABLE IF NOT EXISTS packages ("
			+ "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "name varchar(100),"
			+ "provides varchar(100),"
			+ "depends varchar(100),"
			+ "filename varchar(100)"
			+ ")";
	
	public void init() throws Exception {
		sourcesFile = new File(Main.DATA_LOC + "sources.list");
		dbFile = new File(Main.DATA_LOC + "itweaker.db");
		downloadsDir = new File(Main.DATA_LOC + "downloads");
		tempDir = new File(Main.DATA_LOC + "temp");
		
		FileUtils.checkFiles(sourcesFile);
		FileUtils.checkDirs(downloadsDir, tempDir);
		
		BufferedReader reader = new BufferedReader(new FileReader(sourcesFile));
		while(reader.ready()) {
			String line = reader.readLine();
			if(!line.equals("") && !line.isEmpty()) {
				sourcesList.add(line);
			}
		}
		if(!sourcesList.contains("http://apt.saurik.com/dists/ios/675.00/main/binary-iphoneos-arm/")) {
			sourcesList.add("http://apt.saurik.com/dists/ios/675.00/main/binary-iphoneos-arm/");
		}
		reader.close();
		
		db = new Database();
		db.buildConnection("jdbc:sqlite:"+dbFile, "itweaker", "itweakerdb_pass");
		Statement stat = db.getStat();
		stat.execute(this.createTableStmt);
		this.checkSources();
	}
	
	public long getState() {
		return this.workingState;
	}
	
	public long getFinish() {
		return this.finishedState;
	}
	
	public boolean hasPackage(String pkgName) {
		return hashDEBList.containsKey(pkgName);
	}
	
	public void readDatabase() throws Exception {
		Statement stat = db.getStat();
		ResultSet pkgRes = stat.executeQuery("SELECT count(*) FROM packages;");
		this.finishedState = pkgRes.getInt(1);
		this.workingState = 0;
		ResultSet res = stat.executeQuery("SELECT * FROM packages;");
		while(res.next()) {
			this.workingState++;
			DEB deb = new DEB(res.getString("name"));
			deb.provides = res.getString("provides");
			deb.depends = res.getString("depends");
			deb.fileName = res.getString("filename");
			debsList.add(deb);
		}
		res.close();
		TweakManagerFrame.fillPackages(debsList);
		TweakManagerFrame.fillDownloads(downloadsDir);
		Console.log("Loaded database: " + debsList.size() + " packages loaded!");
		this.hashDEB();
	}
	
	private String checkURL(String url) throws Exception {
		String s = null;
		if(!url.endsWith("/")) {
			 url = url + "/";
		}
		try {
			URLConnection conn = new URL(url + "Packages.bz2").openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setConnectTimeout(2000);
			conn.getInputStream().close();
			s = url;
		} catch(IOException e) {
			URLConnection conn = new URL(url + "dists/stable/main/binary-iphoneos-arm/Packages.bz2").openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setConnectTimeout(12000);
			conn.getInputStream().close();
			s = url + "dists/stable/main/binary-iphoneos-arm";
		}
		if(s != null) {
			if(!s.endsWith("/"))  {
				s = s + "/";
			}
		}
		return s;
	}
	
	public void removeSource(String source) throws Exception {
		if(source.contains("apt.saurik.com")) {
			Console.err("Saurik\'s repo cannot be removed!");
			return;
		}
		if(sourcesList.contains(source)) {
			sourcesList.remove(source);
			PrintStream pw = new PrintStream(sourcesFile);
			for(String s : sourcesList) {
				pw.println(s);
			}
			pw.close();
			TweakManagerFrame.fillPackages(debsList);
		}
	}
	
	private void hashDEB() {
		for(DEB deb : debsList) {
			hashDEBList.put(deb.packageName, deb);
		}
	}
	
	public void downloadDeb(String packageName, boolean withDeps) throws Exception {
		ArrayList<String> toD = new ArrayList<String>();
		if(withDeps) {
			this.getDependencies(packageName, toD);
		} else {
			DEB deb = hashDEBList.get(packageName);
			if(deb == null) {
				if(!packageName.isEmpty() && !packageName.equals("")) {
					Console.err("Cannot locate " + packageName);
				}
				return;
			}
			toD.add(packageName);
		}
		PrintWriter pw = new PrintWriter(new File(tempDir, packageName + ".download"));
		for(String n : toD) {
			DEB deb = hashDEBList.get(n);
			File to = new File(downloadsDir, n+".deb");
			URLConnection conn = new URL(deb.fileName).openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			download(conn, to);
			/*Worker.createDaemon(new Runnable() {
				@Override
				public void run() {
					try {
						download(conn, to);
					} catch(Exception e) {
						Console.err(e.getMessage());
					}
				}
			});*/
			pw.println(n);
		}
		pw.close();
		TweakManagerFrame.fillDownloads(downloadsDir);
	}
	
	public ArrayList<String> getDependencies(String pkgName) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		this.getDependencies(pkgName, result);
		return result;
	}
	
	private void getDependencies(String pkgName, ArrayList<String> deps) throws Exception {
		if(pkgName.contentEquals("") || pkgName.isEmpty()) {
			return;
		}
		if(deps.contains(pkgName)) {
			deps.remove(pkgName);
			deps.add(pkgName);
			return;
		}
		DEB deb = hashDEBList.get(pkgName);
		if(deb == null) {
			ResultSet rs = db.getStat().executeQuery("SELECT name FROM packages WHERE provides LIKE '%"+pkgName+"%'");
			int i = 0;
			while(rs.next()) {
				this.getDependencies(rs.getString("name"), deps);
				i++;
			}
			if(i == 0) {
				 Console.err("Cannot locate " + pkgName);
			}
			return;
		}
		deps.add(pkgName);
		String[] manDeps = deb.depends.split("\\,");
		for(String dep : manDeps) {
			dep = dep.trim();
			this.getDependencies(dep, deps);
		}
	}
	
	public void shutdown() {
		db.shutdown();
	}
	
	public void checkSources() throws Exception {
		FileWriter fw = new FileWriter(sourcesFile, false);
		ArrayList<String> newURLs = new ArrayList<String>();
		for(String url : sourcesList) {
			String s = this.checkURL(url);
			if(s == null) {
				Console.err("Invalid Cydia repo provided: " + url);
			} else {
				newURLs.add(s);
				fw.write(s + "\n");
			}
		}
		fw.close();
		sourcesList = newURLs;
	}
	
	public void addSource(String source) throws Exception {
        String s = this.checkURL(source);
        if (s == null || source.isEmpty()) {
            System.out.println(source + " is not a valid Cydia repo!");
        } else {
            FileWriter fw = new FileWriter(sourcesFile, true);
            fw.write(s + "\n");
            fw.close();
            sourcesList.add(s);
            System.out.println("Added source " + source);
            this.updateSources(true);
            TweakManagerFrame.fillPackages(debsList);
        }
    }
	
	public void updateSources(boolean downloadSS) throws Exception {
		Statement stat = db.getStat();
		stat.executeUpdate("DELETE FROM packages;");
		debsList = new ArrayList<DEB>();
		int last = 0;
		for(String u : sourcesList) {
			String pre = new URL(u).getHost();
			File out = new File(tempDir, pre + ".source");
			if(downloadSS) {
				TweakManagerFrame.updateSources.setEnabled(false);
				TweakManagerFrame.reloadDB.setEnabled(false);
				TweakManagerFrame.manageSources.setEnabled(false);
				/*Worker.createDaemon(new Runnable() {
					@Override
					public void run() {*/
						try {
							URLConnection conn = new URL(u.toString() + "Packages.bz2").openConnection();
							conn.setRequestProperty("User-Agent", "Mozilla/5.0");
							File tempOut = new File(tempDir, pre + ".source.bz2");
							download(conn, tempOut);
							EnumeratingFileInputStream fis = new EnumeratingFileInputStream(tempOut);
							BZip2CompressorInputStream in = new BZip2CompressorInputStream(fis);
							finishedState = tempOut.length();
							FileUtils.checkFiles(out);
							OutputStream fOut = new FileOutputStream(out);
							int i = in.read();
							while(i != -1) {
								workingState = fis.getCount();
								fOut.write(i);
								i = in.read();
							}
							if(i == -1) {
								fOut.close();
								in.close();
								TweakManagerFrame.updateSources.setEnabled(true);
								TweakManagerFrame.reloadDB.setEnabled(true);
								TweakManagerFrame.manageSources.setEnabled(true);
							}
						} catch(Exception e) {
							Console.err(e.getMessage());
							TweakManagerFrame.updateSources.setEnabled(true);
							TweakManagerFrame.reloadDB.setEnabled(true);
							TweakManagerFrame.manageSources.setEnabled(true);
						}
					//}
				//});
			}
			BufferedReader reader = new BufferedReader(new FileReader(out));
			DEB deb = null;
			ArrayList<String> lines = new ArrayList<String>();
			while(reader.ready()) {
				String line = reader.readLine();
				lines.add(line);
			}
			this.finishedState = lines.size();
			this.workingState = 0;
			for(String line : lines) {
				this.workingState++;
				if(line.startsWith("Package")) {
					if(deb != null) {
						debsList.add(deb);
					}
					deb = new DEB(normalizeFor(line));
				}
				if(line.startsWith("Depends")) {
					if(deb == null) {
						continue;
					}
					deb.depends = normalizeFor(line).trim();
					deb.depends = deb.depends.replaceAll("\\({1}[^\\(]*\\)", "");
				}
				if(line.startsWith("Provides")) {
					if(deb == null) {
						continue;
					}
					deb.provides = normalizeFor(line);
				}
				if(line.startsWith("Filename")) {
					if(deb == null) {
						continue;
					}
					deb.fileName = u + normalizeFor(line);
					if(u.equals("http://apt.saurik.com/dists/ios/675.00/main/binary-iphoneos-arm/")) {
						deb.fileName = "http://apt.saurik.com/" + normalizeFor(line);
					}
					if(u.contains("apt.thebigboss")) {
						deb.fileName = deb.fileName.replace("dists/stable/main/binary-iphoneos-arm/", "");
					}
					deb.fileName = deb.fileName.replace("http://", "");
					deb.fileName = deb.fileName.replace("https://", "");
					deb.fileName = deb.fileName.replace("//", "/");
					deb.fileName = "http://" + deb.fileName;
				}
			}
			Console.log("Updated " + u + " and found " + (debsList.size() - last) + " new packages!");
			last = debsList.size();
			reader.close();
		}
		PreparedStatement pstat = db.getPreparedStat("INSERT INTO packages (name, depends, provides, filename) VALUES (?, ?, ?, ?);");
		for(DEB deb : debsList) {
			pstat.setString(1, deb.packageName);
			pstat.setString(2, deb.depends);
			pstat.setString(3, deb.provides);
			pstat.setString(4, deb.fileName);
			pstat.addBatch();
		}
		db.closePreparedStat();
		TweakManagerFrame.fillPackages(debsList);
		this.hashDEB();
	}
	
	public ArrayList<String> search(String pkgName) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = db.getStat().executeQuery("SELECT name FROM packages WHERE name like'%"+pkgName+"%';");
		while(rs.next()) {
			result.add(rs.getString("name"));
		}
		return result;
	}
	
	private void download(URLConnection conn, File out) throws Exception {
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		this.finishedState = conn.getContentLength();
		/*Worker.createDaemon(new Runnable() {
			@Override
			public void run() {*/
				try {
					InputStream in = conn.getInputStream();
					FileUtils.checkFiles(out);
					OutputStream fOut = new FileOutputStream(out);
					int i = in.read();
					int f = 0;
					while(i != -1) {
						f++;
						fOut.write(i);
						i = in.read();
						workingState = f;
					}
					fOut.close();
				} catch(Exception e) {
					Console.err(e.getMessage());
				}
			//}
		//});
	}
	
	private String normalizeFor(String s) {
		String normalize = "";
		try {
			normalize = s.split("\\:")[1].trim();
		} catch(Exception e) {
			normalize = "";
		}
		return normalize;
	}
}
