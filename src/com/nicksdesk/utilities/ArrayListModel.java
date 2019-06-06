package com.nicksdesk.utilities;

import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ArrayListModel implements ListModel<String> {

	private ArrayList<String> data;
	
	@Override
	public int getSize() {
		return data.size();
	}
	
	@Override
	public String getElementAt(int index) {
		if(index >= 0 && index < data.size()) {
			return data.get(index);
		} else {
			return null;
		}
	}
	
	@Override
    public void addListDataListener(ListDataListener l) {}

    @Override
    public void removeListDataListener(ListDataListener l) {}
	
    public void setData(ArrayList<String> data) {
    	this.data = data;
    }
    
    public void setData(String[] data) {
    	this.data = new ArrayList<String>();
    	for(String d : data) {
    		this.data.add(d);
    	}
    }
    
}
