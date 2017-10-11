package com.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
//import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Timer;
import java.util.ArrayList;


import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




import xgy.work.*;




public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JPanel contentPane;
	private JTable table;
	private TableModel model;
	private JScrollPane span = null;
	private JOptionPane resultJOptionPane;
 
	//
	//  http://umcdn.uc.cn/down/4584/UCBrowser_V4.0.4368.0_4584_(Build150306)_1427130001.exe
	//  http://down.360safe.com/setup.exe
	//  http://down.360safe.com/inst.exe
	//  http://dl1sw.baidu.com/c468e7d23f3ab21c513db150fcf70d70/kdfll.exe
	//  Ҫ�����ȴ�ȫ����ͣ��������ɺ��ڿ�ʼ�����ť
	String DPath;
	String SPath;
	int Ithread;
	//�½��յ�FileFetch
	SiteFileFetch fileFetch=new SiteFileFetch();
	ArrayList<SiteFileFetch> arrayfileFetch=new ArrayList<SiteFileFetch>();
	ArrayList<String> fileFetchState=new ArrayList<String>();//��������ÿ�����ص�״̬��Ϣ
	ArrayList<String> fileFetchInfo=new ArrayList<String>();//�����洢ÿ�����ص��ļ���
	ArrayList<Boolean> fileFetchInformation=new ArrayList<Boolean>();
	
	int fileFetchNum=0;
	//����ҳ���Ƿ�ʼˢ��
	boolean startUpdate=false;
	//�洢�����ļ�����Ϣ
	ArrayList<SiteInfoBean> list = new ArrayList<SiteInfoBean>();
	//

	//
	/**
	 * ���������
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);	
					
				    Timer timer = new Timer();  
					timer.schedule(new TimerTaskTest(frame), 1200, 1000);  
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public MainFrame() throws IOException {
		
		//�������һЩ����
		setTitle("\u65AD\u70B9\u7EED\u4F20");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 5, 5, 5));
		setContentPane(contentPane);
		
		setResizable(false);

		
		//��������ʾ����Ļ����		
		Toolkit tool = Toolkit.getDefaultToolkit();     //��ȡ���߶���
		Dimension d = tool.getScreenSize();  	//��ȡ��ǰ��Ļ�ĳߴ�
		double h = d.getHeight();		//��ȡ��Ļ�Ŀ��
		double w = d.getWidth();
		int x = (int)(w-800)/2;		//�����x���y�ᣨ���У�
		int y = (int)(h-600)/2;
		this.setLocation(x, y);
		contentPane.setLayout(null);

		
		//�Ҽ��˵�
				final JPopupMenu popup = new JPopupMenu();
				JMenuItem item1=new JMenuItem("��ʼ");
				JMenuItem item2=new JMenuItem("��ͣ");
				JMenuItem item3=new JMenuItem("ɾ��");
				popup.add(item1);
				popup.add(item2);
				popup.add(item3);
				popup.getComponent(0).setEnabled(false);
			
		
		
		
				
		/**
		 * �������ϵĿؼ�����
		 */
		//������
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 794, 36);
		toolBar.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		contentPane.add(toolBar);

		//�½�����
		JButton btnCreate =
				new JButton(" \u65B0\u5EFA ");
		btnCreate.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton_newActionPerformed(e);
			}
		});
		toolBar.add(btnCreate);
		

		
		//ȫ����ʼ�ļ�����
		JButton btnStart = new JButton("ȫ����ʼ");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Iterator<Boolean> iter = fileFetchInformation.iterator();
				int num = 0;
				while(iter.hasNext()){

					if(iter.next()==false){
						//���俪ʼ
				    	System.out.println("��ʼ��"+(num+1)+"��");
				    	SPath=list.get(num).getSFilePath();
				    	Ithread=list.get(num).getNSplitter();
				    	DPath=list.get(num).getSSiteURL();
				    	arrayfileFetch.get(num).setbFirst(false);
				    	fileFetchInformation.set(num,true);
				    	startDown_exist(num);
				    	num=num+1;
					}
				}
				
				
				
			}
		});
		btnStart.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		toolBar.add(btnStart);
		
	
		//ȫ����ͣ�ļ�����
		JButton btnStop = new JButton("ȫ����ͣ");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Iterator<Boolean> iter = fileFetchInformation.iterator();
				System.out.println(fileFetchInformation.size());
				int num = 0;
				while(iter.hasNext()){
					if(iter.next()==true){
						//������ͣ
						arrayfileFetch.get(num).siteStop();	
						try {
							SiteFileFetch fetch = new SiteFileFetch(list.get(num));
							arrayfileFetch.set(num, fetch);
							arrayfileFetch.get(num).setbFirst(false);
							fileFetchInformation.set(num,false);
							//model.updateRow(num, fileFetchInfo.get(num), "����ͣ", "-----", "usedTime", "-----");	
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
						
					}
					num++;
				}
				
				//btnStop.setEnabled(false);
			}
		});
		btnStop.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		toolBar.add(btnStop);
		
		//ȫ��ɾ�������ļ�
		JButton btnDelete = new JButton("ȫ��ɾ��");
		btnDelete.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {
				/*
				 * 1.��SiteFileFetch�ڵ�����ȫ�����
				 * 2.����ʱ�ļ��������ļ�ɾ���������ԸĽ�Ϊ�����Ի�����ʾ�Ƿ�ɾ��ԭ�ļ���
				 * 3.����������ɾ��
				 */
				//�ȶ���ͣ

				  @SuppressWarnings("static-access")
				int result= resultJOptionPane.showConfirmDialog(null, "ȷ��ȫ��ɾ����?", "ȫ��ɾ��", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		            if(result == JOptionPane.YES_OPTION){

						Iterator<Boolean> iter = fileFetchInformation.iterator();
						System.out.println("filefor"+fileFetchInformation.size());
						int num = 0;
						while(iter.hasNext()){
							if(iter.next()==true){
								//����ֹͣ
								arrayfileFetch.get(num).siteStop();			
								try {
					    			arrayfileFetch.get(num).join();
					    			System.out.println("num:"+num);
					    		} catch (InterruptedException e1) {
					    			// TODO Auto-generated catch block
					    			e1.printStackTrace();
					    		}
							}
							num++;			
						}
						File file,fileInfo;
						Iterator<SiteInfoBean> iterator = list.iterator();	
						while(iterator.hasNext()){
							SiteInfoBean _site = iterator.next();
							String string = _site.getSFilePath() + File.separator + _site.getSFileName();
							file = new File(string);
							System.out.println(file.delete());
							fileInfo = new File(string+".info");
							System.out.println("fileInfo.delete()"+fileInfo.delete());
						}
						
						int total = fileFetchNum;
						fileFetchNum = 0;
						System.out.println("total:"+total);
						for(int i=0;i<total;i++){
							model.removeRow(0);
						}
						updateUI();
						arrayfileFetch.clear();
						list.clear();
						fileFetchInfo.clear();

						//File file_mainFile = new File(System.getProperty("user.dir")+"\\MainFile");
						try {
							DataOutputStream output3 = new DataOutputStream(new FileOutputStream("MainFile"));
							
							byte[] b = {1};
							try {
								output3.write(b, 0, 0);
								output3.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						/*File file_new = new File(System.getProperty("user.dir")+"\\MainFile");
						if(!file_new.exists()){
							try {
								file_new.createNewFile();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}*/

						fileFetchInformation.clear();
						fileFetchState.clear();
		            }
		            else {
		            	return;

		            }

			}	
		});
		btnDelete.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		toolBar.add(btnDelete);
		
		
		//������ʷ��¼
		JButton btnRecord = new JButton("���ؼ�¼");
		btnRecord.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		btnRecord.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				jButton_record(e);
			}
		});
		toolBar.add(btnRecord);
		
		//������
		model = new TableModel(20);
		table = new JTable(model);
		table.setBounds(0, 575, 798, -539);
		
		table.setRowSelectionAllowed(true);
		//table.setColumnSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
		
		span = new JScrollPane(table);
		span.setBounds(0, 36, 798, 539);
		contentPane.add(span, BorderLayout.CENTER);


		MouseInputListener mil = new MouseInputListener() {

			public void mouseClicked(MouseEvent e) {
				processEvent(e);
				
			}

			public void mousePressed(MouseEvent e) {
				processEvent(e);
			}

			public void mouseReleased(MouseEvent e) {
				processEvent(e);
				if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
					int row=table.getSelectedRow();
					if(fileFetchInformation.get(row)){
						popup.getComponent(1).setEnabled(true);
						popup.getComponent(0).setEnabled(false);
					}
					else{
						popup.getComponent(1).setEnabled(false);
						popup.getComponent(0).setEnabled(true);
					}
					popup.show(table, e.getX(), e.getY());
				}
			}

			public void mouseEntered(MouseEvent e) {
				processEvent(e);
			}

			public void mouseExited(MouseEvent e) {
				processEvent(e);
			}

			public void mouseDragged(MouseEvent e) {
				processEvent(e);
			}

			public void mouseMoved(MouseEvent e) {
				processEvent(e);
			}

			private void processEvent(MouseEvent e) {
				if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
					MouseEvent ne = new MouseEvent(
							e.getComponent(), e.getID(), 
							e.getWhen(), MouseEvent.BUTTON1_MASK, 
							e.getX(), e.getY(), e.getClickCount(), false);
					table.dispatchEvent(ne);
				}
			}

		};
		table.addMouseListener(mil);
		table.addMouseMotionListener(mil);

		//�ڴ����������ļ�����Ϣ
		loadingMainFile();
		//�Ҽ��˵���������¼�������
		item1.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		    	//��ʼ���¼�
		    	int rowNum = table.getSelectedRow();
		    	System.out.println("��ʼ������"+rowNum);
		    	SPath=list.get(rowNum).getSFilePath();
		    	Ithread=list.get(rowNum).getNSplitter();
		    	DPath=list.get(rowNum).getSSiteURL();
		    	arrayfileFetch.get(rowNum).setbFirst(true);
		    	fileFetchInformation.set(rowNum,true);
		    	startDown_exist(rowNum);
		    }

			/*private void startDown_1() {
				// TODO Auto-generated method stub
				try {
					SiteInfoBean bean = new SiteInfoBean(DPath,SPath,Ithread);
					SiteFileFetch fetch = new SiteFileFetch(bean);
					fetch.setbFirst(false);
					fetch.start();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}*/
		});
		
		item2.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		    	//��ͣ���¼�
		    	int rowNum = table.getSelectedRow();
		    	System.out.println("��ͣ��"+rowNum+1+"��");
				arrayfileFetch.get(rowNum).siteStop();
				try {
					arrayfileFetch.get(rowNum).join();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					SiteFileFetch fetch = new SiteFileFetch(list.get(rowNum));
					arrayfileFetch.set(rowNum, fetch);
					arrayfileFetch.get(rowNum).setbFirst(false);
					fileFetchInformation.set(rowNum,false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//���Բ�д
				//model.updateRow(rowNum, list.get(rowNum).getSFileName(), "����ͣ", "-----", "usedTime", "-----");	
				
		    }
		});
		
		item3.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		    	//ɾ�����¼�
		  	  @SuppressWarnings("static-access")
			int result= resultJOptionPane.showConfirmDialog(null, "ȷ��ɾ����������?", "ȫ��ɾ��",
						 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		            if(result == JOptionPane.YES_OPTION){
		    	//�ļ���Info�������ļ�;MainFrame;ɾ��list��arrayXXX�ȶ�̬����;fileFetchNum-1;��ҳ���Ͻ���һ������ɾ��
		        int rowNum = table.getSelectedRow();
		    	String s=list.get(rowNum).getSFilePath() +File.separator+ list.get(rowNum).getSFileName();
		    	System.out.println("ɾ��: "+rowNum+" rowNum");
		    	System.out.println(s);
		    	if(fileFetchInformation.get(rowNum)==true){
		    		arrayfileFetch.get(rowNum).siteStop();
		    		try {
		    			arrayfileFetch.get(rowNum).join();
		    		} catch (InterruptedException e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		    	}
		    	//fileFetchInformation.set(rowNum,false);
		    	arrayfileFetch.get(rowNum).deleteMainFile();
		    	list.remove(rowNum);
		    	arrayfileFetch.remove(rowNum);
		    	fileFetchState.remove(rowNum);
		    	fileFetchInfo.remove(rowNum);
		    	//***��ʱ������ͣ1s
		    	fileFetchNum = fileFetchNum - 1;
		    	fileFetchInformation.remove(rowNum);
		    	model.removeRow(rowNum);
		    	File file,fileInfo;
		    	file=new File(s);
		    	String sInfo=s+".info";
		    	fileInfo=new File(sInfo);
		    	fileInfo.delete();
		    	System.out.println(file.delete());
		    	updateUI();
		    	
		            }
		        else{
		            	return;
		           }
		            }
		});
   
	}
	
	//�½�һ�����񣬲���ӵ�table��
	public void jButton_newActionPerformed(java.awt.event.ActionEvent evt) {
		NewDialog nd= new NewDialog(this);
	    nd.setVisible(true);
	 }
	//�鿴���ؼ�¼
	public void jButton_record(java.awt.event.ActionEvent evt) {
		RecordDialog rd= new RecordDialog(this);
	    rd.setVisible(true);
	 }
	
	//���һ������
	//***
	public  void addData(int i)throws IOException {
		
		model.addRow(arrayfileFetch.get(i).getFileName(),"����",arrayfileFetch.get(i).getSpeeds(),"0:00:00",arrayfileFetch.get(i).getRemainTime());		
	}
	//����һ������
	public void updateData(int num)throws IOException{
		String usedTime=null;
		if(fileFetchState.get(num).equals("true"))
		{
			//��������ɺ�
			//model.updateRow(num, fileFetchInfo.get(num), "������", "-----", usedTime, "-----");	
			fileFetchState.remove(num);
	    	String s=list.get(num).getSFilePath() +File.separator+ list.get(num).getSFileName();
			System.out.println(list.get(num).getSFileName()+"  ���سɹ�");
			try {
				arrayfileFetch.get(num).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	arrayfileFetch.get(num).deleteMainFile();
	    	writeIntxt(list.get(num));
	    	list.remove(num);
	    	arrayfileFetch.remove(num);
	    	fileFetchInfo.remove(num);
	    	//***��ʱ������ͣ1s
	    	fileFetchNum = fileFetchNum - 1;
	    	fileFetchInformation.remove(num);
	    	model.removeRow(num);
	    	updateUI();
			
		}
		else if(fileFetchInformation.get(num)==false){
			model.updateRow(num, list.get(num).getSFileName(), "����ͣ", "-----", "-----", "-----");	
		}
			
		else
		{
		   //�ж�����Ѿ�������ɣ�����Ӧ��״ֵ̬��Ϊtrue
		   if(arrayfileFetch.get(num).isDone()){
			fileFetchState.remove(num);
			fileFetchState.add(num, "true");
			System.out.println(num+"---"+fileFetchState.get(num));
		   }
		usedTime=formatTime(System.currentTimeMillis()-arrayfileFetch.get(num).getstartTime());
		model.updateRow(num,arrayfileFetch.get(num).getFileName(),arrayfileFetch.get(num).getPercent()+"%",arrayfileFetch.get(num).getSpeeds()+" kb/s",usedTime,arrayfileFetch.get(num).getRemainTime());
		}
	}
	public void updateUI(){
		table.updateUI();
	}

	public void updateUI2(){
		table.setModel(model);
	}
	//ɾ����ǰѡ�����
	public void removeData() {
		int rowNum = table.getSelectedRow();
		model.removeRow(rowNum);
	}	

	public void startDown(){
		try {		
			SiteInfoBean bean = new SiteInfoBean(DPath,SPath,Ithread);	
			arrayfileFetch.get(fileFetchNum).setFileFetch(bean);
			arrayfileFetch.get(fileFetchNum).isFileExist_first();
			arrayfileFetch.get(fileFetchNum).start(); // ���ø��̵߳�run()����	
			arrayfileFetch.get(fileFetchNum).setstartTime(System.currentTimeMillis());
			//update
			String n=arrayfileFetch.get(fileFetchNum).getFileName();
			fileFetchInfo.add(n);
			fileFetchState.add("false");//����һ�е��������һ��Ĭ��Ϊ��false����״ֵ̬ 

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		   }
	}
	
	
	public void startDown_exist(int num){
		try {		
			SiteInfoBean bean = new SiteInfoBean(DPath,SPath,Ithread);	
			arrayfileFetch.get(num).setFileFetch(bean);
			arrayfileFetch.get(num).isFileExist_first();
			//�ļ�һ������
			/*if(string!=null){
				bean.setSFileName(string);//Ϊ��ֻ��ӡһ�߲��������ж�
				File file2 = new File(bean.getSFilePath()+ File.separator + bean.getSFileName() + ".info");
				arrayfileFetch.get(fileFetchNum).setTmpFile(file2);
				arrayfileFetch.get(fileFetchNum).isFileExist();
			}*/
			arrayfileFetch.get(num).start(); // ���ø��̵߳�run()����	
			arrayfileFetch.get(num).setstartTime(System.currentTimeMillis());
			//update
			//String n=arrayfileFetch.get(num).getFileName();
			//fileFetchInfo.add(n);
			//fileFetchState.add("false");//����һ�е��������һ��Ĭ��Ϊ��false����״ֵ̬ 

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		   }
	}
	/*
	* ����ת��
	*/
	public static String formatTime(long ms) {	            
	            if(ms==0){
	            	return 0+"ʱ"+0+"��"+0+"��";
	            }
		         int ss = 1000;
	             int mi = ss * 60;
	             int hh = mi * 60;
	             int dd = hh * 24;

	            
	             long day = ms / dd;	            
	             long hour = (ms - day * dd) / hh;	            
	             long minute = (ms - day * dd - hour * hh) / mi;	            
	             long second = (ms - day * dd - hour * hh - minute * mi) / ss;	       
	             long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
	             String strDay = day < 10 ? "0" + day : "" + day; //��
	             String strHour = hour < 10 ? "0" + hour : "" + hour;//Сʱ
	             String strMinute = minute < 10 ? "0" + minute : "" + minute;//����
	             String strSecond = second < 10 ? "0" + second : "" + second;//��
	             String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//����
	             strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
	             if(day>1){
	            	 return strDay+" ��"+strHour+" ʱ"+strMinute+" ��"+strSecond+"��";
	             }else{
	             return strHour+" ʱ"+strMinute + "���� " + strSecond + "��";
	              }
	             

	            
	   }

	//����ʼʱ��ȡ������Ϣ
		public void loadingMainFile(){
			System.out.println("����������Ϣ�ļ�");
			try {
				mainFileisExist();//���ж��ļ��Ƿ���ڣ��������ڣ���һ�����ļ�
				DataInputStream input = new DataInputStream(new FileInputStream("MainFile"));	
				while(true){
					SiteInfoBean bean = new SiteInfoBean();
					bean.setSSiteURL(input.readUTF());
					bean.setSFilePath(input.readUTF());
					bean.setSFileName(input.readUTF());
					bean.setNSplitter(input.readInt());
					list.add(bean);
					System.out.println(input.available());
					if(input.available()<=0){break;}
					
				}
				Iterator<SiteInfoBean> iterator = list.iterator();	
				while(iterator.hasNext()){
					fileFetchInformation.add(true);
					SiteInfoBean _bean=new SiteInfoBean();
					_bean =iterator.next();
					//System.out.println(_bean.getSSiteURL()+_bean.getSFilePath()+_bean.getSFileName()+_bean.getNSplitter());
					SiteFileFetch sf = new SiteFileFetch(_bean);
					DPath=_bean.getSSiteURL();
					SPath=_bean.getSFilePath();
					Ithread=_bean.getNSplitter();
					arrayfileFetch.add(sf);
					startDown();
					try {
						System.out.println("fileFetchNum"+fileFetchNum);
						addData(fileFetchNum);

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					fileFetchNum=fileFetchNum+1;
					//������ݺ�ҳ�濪ʼˢ��
					setStartUpdate(true);
					System.out.println("input.available():"+input.available());

				}
				input.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
        //�ж���Ϣ�ļ��Ƿ���ڣ��������ڣ��½�һ���ļ�
		public void mainFileisExist(){
			File file = new File("MainFile");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private static void writeIntxt(SiteInfoBean bean) throws IOException {
			// TODO Auto-generated method stub
			RecordFile rf=new RecordFile();
			rf.write(bean.getSFileName(), bean.getSFilePath());
			
		}


	public String getDPath() {
		return DPath;
	}


	public void setDPath(String dPath) {
		DPath = dPath;
	}


	public String getSPath() {
		return SPath;
	}


	public void setSPath(String sPath) {
		SPath = sPath;
	}


	public int getIthread() {
		return Ithread;
	}


	public void setIthread(int ithread) {
		Ithread = ithread;
	}

	public boolean isStartUpdate() {
		return startUpdate;
	}

	public void setStartUpdate(boolean startUpdate) {
		this.startUpdate = startUpdate;
	}
	




}