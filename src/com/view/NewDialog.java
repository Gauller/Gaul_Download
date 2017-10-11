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
	//获取线程数
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
		super(frame,"新建任务窗口");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.frame = frame;
		init();	
    }
    



	public void init(){
		
		//窗体的基本属性
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 262);
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
		int x = (int)(w-400)/2;   	//窗体的x轴和y轴（居中）
		int y = (int)(h-262)/2;
		this.setLocation(x, y);    //设置窗体初始位置
			
		//下载路径，线程，存取路径等控件的代码
		DownloadPath = new JLabel("\u8BF7\u8F93\u5165\u4E0B\u8F7D\u5730\u5740\uFF1A");
		DownloadPath.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		DownloadPath.setBounds(28, 10, 120, 23);
		contentPane.add(DownloadPath);
		
		Download_textField = new JTextField();
		Download_textField.setBounds(28, 43, 331, 21);
		contentPane.add(Download_textField);
		Download_textField.setColumns(10);


		thread = new JLabel("\u7EBF\u7A0B\u6570\u9009\u62E9\uFF1A");
		thread.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		thread.setBounds(28, 74, 84, 25);
		contentPane.add(thread);

//      获取线程数		
//		thread_textField = new JTextField();
//		thread_textField.setBounds(127, 77, 66, 21);
//		contentPane.add(thread_textField);
//		thread_textField.setColumns(10);
		
		//获取线程数，方法2 
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
		SavePath.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		SavePath.setBounds(28, 109, 120, 25);
		contentPane.add(SavePath);
		
		//选择保存路径
		JButton btnBrowse = new JButton("\u6D4F\u89C8");
		btnBrowse.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//产生一个文件选择器
				JFileChooser jChooser = new JFileChooser();
				//设置默认的打开目录,如果不设的话按照window的默认目录(我的文档)
				jChooser.setCurrentDirectory(new File("c:/"));
				//设置打开文件类型,此处设置成只能选择文件夹，不能选择文件
				jChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能打开文件夹
				//打开一个对话框
				int index = jChooser.showDialog(null, "打开文件");
				if (index == JFileChooser.APPROVE_OPTION) {
					//把获取到的文件的绝对路径显示在文本编辑框中
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

		//确定按钮下的事件
		btnSubmit = new JButton("\u786E\u5B9A");
		btnSubmit.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if("".equals(Save_textField.getText())){
					JOptionPane.showMessageDialog(contentPane,"地址不能为空");
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
						//文件不存在
						if(string!=null){
						//frame.startDown();
						bean.setSFileName(string);//为了只打印一边不跑两遍判定
						File file2 = new File(bean.getSFilePath()+ File.separator + bean.getSFileName() + ".info");
						frame.arrayfileFetch.get(frame.fileFetchNum).setTmpFile(file2);
						frame.arrayfileFetch.get(frame.fileFetchNum).isFileExist();
						}
						frame.list.add(bean);
						frame.fileFetchInformation.add(true);
						frame.arrayfileFetch.get(frame.fileFetchNum).start(); // 调用该线程的run()方法
						frame.arrayfileFetch.get(frame.fileFetchNum).setstartTime(System.currentTimeMillis());
						//update
						String n=frame.arrayfileFetch.get(frame.fileFetchNum).getFileName();
						frame.fileFetchInfo.add(n);
						frame.fileFetchState.add("false");//对这一行的下载添加一个默认为“false”的状态值 
						//***
						frame.fileFetchNum=frame.fileFetchNum+1;
						frame.addData(frame.fileFetchNum-1);

					}
					else{
						//文件已经存在
				    	JOptionPane.showMessageDialog(contentPane,"文件已经存在");
				    	frame.arrayfileFetch.remove(frame.fileFetchNum);
				    	return;
					}
				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//添加数据后，页面开始刷新
				frame.setStartUpdate(true);
				dispose();//dispose会隐藏窗体并释放窗体所占用的部分资源
     		}


		});
		btnSubmit.setBounds(176, 183, 84, 24);
		contentPane.add(btnSubmit);
		
		
		//取消按钮下的事件
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();//dispose会隐藏窗体并释放窗体所占用的部分资源
			}
		});
		btnCancel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
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
