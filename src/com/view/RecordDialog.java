package com.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class RecordDialog extends JDialog{
	
	
	MainFrame frame;
	RecordFile rf=new RecordFile();
	
	private JPanel contentPane;
	private JTable table;
	private RecordTable model;
	private JScrollPane span = null;
	
	public RecordDialog(MainFrame frame){
		super(frame,"下载记录");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.frame = frame;
		init();
    }
	
	public void init(){
		
		//窗体的基本信息
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		
		//将窗体显示在屏幕中央
		Toolkit tool = Toolkit.getDefaultToolkit();		//获取工具对象
		Dimension d = tool.getScreenSize();		//获取当前屏幕的尺寸	
		double h = d.getHeight();	  //获取屏幕的宽高
		double w = d.getWidth();	
		int x = (int)(w-500)/2;   	//窗体的x轴和y轴（居中）
		int y = (int)(h-300)/2;
		this.setLocation(x, y);    //设置窗体初始位置
		
		//载入下载记录信息
		Vector records=rf.read();
		
		model = new RecordTable(records);
		table = new JTable(model);
		table.setBounds(0, 0, 500, 270);
		
		table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        int[] width={25,175,125,175};
        table.setColumnModel(getColumn(table, width));
		
		span = new JScrollPane(table);
		span.setBounds(0, 0, 500, 270);
		contentPane.add(span, BorderLayout.CENTER);

	}
	
	public static TableColumnModel getColumn(JTable table, int[] width) {  
	    TableColumnModel columns = table.getColumnModel();  
	    for (int i = 0; i < width.length; i++) {  
	        TableColumn column = columns.getColumn(i);  
	        column.setPreferredWidth(width[i]);  
	    }  
	    return columns;  
	}
}
