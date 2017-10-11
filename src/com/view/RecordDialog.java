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
		super(frame,"���ؼ�¼");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.frame = frame;
		init();
    }
	
	public void init(){
		
		//����Ļ�����Ϣ
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		
		//��������ʾ����Ļ����
		Toolkit tool = Toolkit.getDefaultToolkit();		//��ȡ���߶���
		Dimension d = tool.getScreenSize();		//��ȡ��ǰ��Ļ�ĳߴ�	
		double h = d.getHeight();	  //��ȡ��Ļ�Ŀ��
		double w = d.getWidth();	
		int x = (int)(w-500)/2;   	//�����x���y�ᣨ���У�
		int y = (int)(h-300)/2;
		this.setLocation(x, y);    //���ô����ʼλ��
		
		//�������ؼ�¼��Ϣ
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
