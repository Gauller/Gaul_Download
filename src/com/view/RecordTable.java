package com.view;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class RecordTable extends AbstractTableModel{
	
	
	private Vector records=null;
	private String[] titlelist={"ID","�ļ���","���ʱ��","�ļ�Ŀ¼"};

	public RecordTable(){
		records=new Vector();
	}
	public RecordTable(Vector records){
		this.records=records;
	}
	
	
	
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return titlelist.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return records.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return ((Vector)records.get(arg0)).get(arg1);
	}
	
	public void changeTabledata(){
    	fireTableCellUpdated(0,2);
    }
	
    public String getColumnName(int col) {
        return titlelist[col];
    }

}
