package com.nicksdesk.manager;

public class DEB {

	public String packageName;
	public String fileName;
	public String provides = "";
	public String depends = "";
	
	public DEB(String packageName) {
		this.packageName = packageName;
	}
	
	@Override
	public String toString() {
		return "DEB{"+"packagename="+packageName+",provides="+provides+",depends="+depends+",filename="+fileName+"}";
	}
	
}
