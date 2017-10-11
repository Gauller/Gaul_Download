package com.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;


import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

import xgy.work.*;

public class NewDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JPanel contentPane;
	private JTextField Save_textField;
	private JLabel SavePath;
	private JLabel DownloadPath;
	private JTextField Download_textField;
	private JLabel thread;
	//��ȡ�߳���
	private JComboBox comboBox;
	private JButton btnSubmit;
	private JButton btnCancel;
	private JTextField thread_textField;
	private JLabel threadMessage;
	//
	SiteFileFetch fileFetch=new SiteFileFetch();

	//

	
	//
	/**
	 * Create the frame.
	 */
    
	MainFrame frame;
    
    public NewDialog(MainFrame frame){
		super(frame,"�½����񴰿�");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.frame = frame;
		init();	
    }
    



	public void init(){
		
		//����Ļ�������
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 262);
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
		int x = (int)(w-400)/2;   	//�����x���y�ᣨ���У�
		int y = (int)(h-262)/2;
		this.setLocation(x, y);    //���ô����ʼλ��
			
		//����·�����̣߳���ȡ·���ȿؼ��Ĵ���
		DownloadPath = new JLabel("\u8BF7\u8F93\u5165\u4E0B\u8F7D\u5730\u5740\uFF1A");
		DownloadPath.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		DownloadPath.setBounds(28, 10, 120, 23);
		contentPane.add(DownloadPath);
		
		Download_textField = new JTextField();
		Download_textField.setBounds(28, 43, 331, 21);
		contentPane.add(Download_textField);
		Download_textField.setColumns(10);


		thread = new JLabel("\u7EBF\u7A0B\u6570\u9009\u62E9\uFF1A");
		thread.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		thread.setBounds(28, 74, 84, 25);
		contentPane.add(thread);

//      ��ȡ�߳���		
//		thread_textField = new JTextField();
//		thread_textField.setBounds(127, 77, 66, 21);
//		contentPane.add(thread_textField);
//		thread_textField.setColumns(10);
		
		//��ȡ�߳���������2 
		comboBox=new JComboBox();
		 comboBox.setBounds(127, 77, 66, 21);
		 comboBox.addItem("1");
		 comboBox.addItem("2");
		 comboBox.addItem("3");
		 comboBox.addItem("4");
		 comboBox.addItem("5");
		 comboBox.addItem("6");
		 comboBox.addItem("7");
		 comboBox.addItem("8");
		 comboBox.addItem("9");
		 comboBox.addItem("10");
		 comboBox.setSelectedItem("5");
		 contentPane.updateUI();
		 contentPane.add(comboBox);
	
		SavePath = new JLabel("\u9009\u62E9\u4FDD\u5B58\u8DEF\u5F84\uFF1A");
		SavePath.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		SavePath.setBounds(28, 109, 120, 25);
		contentPane.add(SavePath);
		
		//ѡ�񱣴�·��
		JButton btnBrowse = new JButton("\u6D4F\u89C8");
		btnBrowse.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//����һ���ļ�ѡ����
				JFileChooser jChooser = new JFileChooser();
				//����Ĭ�ϵĴ�Ŀ¼,�������Ļ�����window��Ĭ��Ŀ¼(�ҵ��ĵ�)
				jChooser.setCurrentDirectory(new File("c:/"));
				//���ô��ļ�����,�˴����ó�ֻ��ѡ���ļ��У�����ѡ���ļ�
				jChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//ֻ�ܴ��ļ���
				//��һ���Ի���
				int index = jChooser.showDialog(null, "���ļ�");
				if (index == JFileChooser.APPROVE_OPTION) {
					//�ѻ�ȡ�����ļ��ľ���·����ʾ���ı��༭����
					Save_textField.setText(jChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		Save_textField = new JTextField();
		Save_textField.setEditable(false);
		Save_textField.setBounds(28, 144, 240, 23);
		contentPane.add(Save_textField);
		Save_textField.setColumns(10);
		btnBrowse.setBounds(285, 144, 74, 23);
		contentPane.add(btnBrowse);

		//ȷ����ť�µ��¼�
		btnSubmit = new JButton("\u786E\u5B9A");
		btnSubmit.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if("".equals(Save_textField.getText())){
					JOptionPane.showMessageDialog(contentPane,"��ַ����Ϊ��");
					return;
				}
				String DPath = Download_textField.getText();
				String SPath = Save_textField.getText();
				String Sthread = comboBox.getSelectedItem().toString();
				System.out.println(Sthread);
				int Ithread= Integer.parseInt(Sthread) ;
				frame.setDPath(DPath);
				frame.setSPath(SPath);
				frame.setIthread(Ithread);
				try {
					frame.arrayfileFetch.add(fileFetch);
					SiteInfoBean bean = new SiteInfoBean(DPath,SPath,Ithread);	
					frame.arrayfileFetch.get(frame.fileFetchNum).setFileFetch(bean);			
					if(!frame.arrayfileFetch.get(frame.fileFetchNum).isFileExist_first()){
						
						String string = frame.arrayfileFetch.get(frame.fileFetchNum).isFileExist();
						//�ļ�������
						if(string!=null){
						//frame.startDown();
						bean.setSFileName(string);//Ϊ��ֻ��ӡһ�߲��������ж�
						File file2 = new File(bean.getSFilePath()+ File.separator + bean.getSFileName() + ".info");
						frame.arrayfileFetch.get(frame.fileFetchNum).setTmpFile(file2);
						frame.arrayfileFetch.get(frame.fileFetchNum).isFileExist();
						}
						frame.list.add(bean);
						frame.fileFetchInformation.add(true);
						frame.arrayfileFetch.get(frame.fileFetchNum).start(); // ���ø��̵߳�run()����
						frame.arrayfileFetch.get(frame.fileFetchNum).setstartTime(System.currentTimeMillis());
						//update
						String n=frame.arrayfileFetch.get(frame.fileFetchNum).getFileName();
						frame.fileFetchInfo.add(n);
						frame.fileFetchState.add("false");//����һ�е��������һ��Ĭ��Ϊ��false����״ֵ̬ 
						//***
						frame.fileFetchNum=frame.fileFetchNum+1;
						frame.addData(frame.fileFetchNum-1);

					}
					else{
						//�ļ��Ѿ�����
				    	JOptionPane.showMessageDialog(contentPane,"�ļ��Ѿ�����");
				    	frame.arrayfileFetch.remove(frame.fileFetchNum);
				    	return;
					}
				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//������ݺ�ҳ�濪ʼˢ��
				frame.setStartUpdate(true);
				dispose();//dispose�����ش��岢�ͷŴ�����ռ�õĲ�����Դ
     		}


		});
		btnSubmit.setBounds(176, 183, 84, 24);
		contentPane.add(btnSubmit);
		
		
		//ȡ����ť�µ��¼�
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();//dispose�����ش��岢�ͷŴ�����ռ�õĲ�����Դ
			}
		});
		btnCancel.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		btnCancel.setBounds(275, 183, 84, 24);
		contentPane.add(btnCancel);
				
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
		});
    	
		
    }



}
