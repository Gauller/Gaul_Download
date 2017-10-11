package com.view;


//这个类定义了表格的模型和一些方法

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{
	
	private static final Object ID = null;

	private Vector content = null;
	
	private String[] title_name = {"ID","文件名","下载进度","下载速度","已用时间","剩余时间"};

	public TableModel(){
		content = new Vector();
	}
	
	public TableModel(int count){
		content = new Vector(count);
	}
	
	
	public void addRow(String name,long temp_percent,double speed,String time1,String time2){
		Vector v = new Vector();
		v.add(0,new Integer(content.size()+1));
		v.add(1,name);
		v.add(2,new Long(temp_percent));
		v.add(3,new Double(speed));
		v.add(4,time1);
		v.add(5,time2);
		content.add(v);
		
	}
	
	public void addRow(String name,String loading,double speed,String time1,String time2){
		Vector v = new Vector();
		v.add(0,new Integer(content.size()+1));
		v.add(1,name);
		v.add(2,loading);
		v.add(3,new Double(speed));
		v.add(4,time1);
		v.add(5,time2);
		content.add(v);
		
	}
	
	
	public void updateRow(int rowNum,String name,long temp_percent,double speed,String time1,String time2){
		int size = content.size();
		Vector v = new Vector();
		v.add(0,rowNum+1);
		v.add(1,name);
		v.add(2,new Long(temp_percent));
		v.add(3,new Double(speed));
		v.add(4,time1);
		v.add(5,time2);
		content.remove(rowNum);
		content.add(rowNum,v);
	}
	
	public void updateRow(int rowNum,String name,String percent,String speed,String time1,String time2){
		int size = content.size();
		Vector v = new Vector();
		v.add(0,rowNum+1);
		v.add(1,name);
		v.add(2,percent);
		v.add(3,speed);
		v.add(4,time1);
		v.add(5,time2);
		content.remove(rowNum);
		content.add(rowNum,v);
	}
	
    public void removeRow(int row) {
        content.remove(row);
    }
 
    
    public void removeRows(int row, int count) {
        for (int i = 0; i < count; i++) {
            if (content.size() > row) {
                content.remove(row);
            }
        }
    }
 
    public void changeTabledata(){
    	fireTableCellUpdated(0,2);
    }
	
    public String getColumnName(int col) {
        return title_name[col];
    }
	
	public int getColumnCount() {
        return title_name.length;
    }
 
    public int getRowCount() {
        return content.size();
    }
 
    public Object getValueAt(int row, int col) {	
        return ((Vector)content.get(row)).get(col);
    }
 
	//public void setValueAt(Object value, int row, int col) {
		//data[rowIndex][columnIndex] = value;
		//fireTableCellUpdated(row,col);
	//}
}
