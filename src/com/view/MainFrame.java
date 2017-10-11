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
	//  要加锁等待全部暂停、下载完成后在开始别的摁钮
	String DPath;
	String SPath;
	int Ithread;
	//新建空的FileFetch
	SiteFileFetch fileFetch=new SiteFileFetch();
	ArrayList<SiteFileFetch> arrayfileFetch=new ArrayList<SiteFileFetch>();
	ArrayList<String> fileFetchState=new ArrayList<String>();//用来储存每个下载的状态信息
	ArrayList<String> fileFetchInfo=new ArrayList<String>();//用来存储每个下载的文件名
	ArrayList<Boolean> fileFetchInformation=new ArrayList<Boolean>();
	
	int fileFetchNum=0;
	//设置页面是否开始刷新
	boolean startUpdate=false;
	//存储下载文件的信息
	ArrayList<SiteInfoBean> list = new ArrayList<SiteInfoBean>();
	//

	//
	/**
	 * 程序主入口
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
		
		//主窗体的一些属性
		setTitle("\u65AD\u70B9\u7EED\u4F20");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 5, 5, 5));
		setContentPane(contentPane);
		
		setResizable(false);

		
		//将窗体显示在屏幕中央		
		Toolkit tool = Toolkit.getDefaultToolkit();     //获取工具对象
		Dimension d = tool.getScreenSize();  	//获取当前屏幕的尺寸
		double h = d.getHeight();		//获取屏幕的宽高
		double w = d.getWidth();
		int x = (int)(w-800)/2;		//窗体的x轴和y轴（居中）
		int y = (int)(h-600)/2;
		this.setLocation(x, y);
		contentPane.setLayout(null);

		
		//右键菜单
				final JPopupMenu popup = new JPopupMenu();
				JMenuItem item1=new JMenuItem("开始");
				JMenuItem item2=new JMenuItem("暂停");
				JMenuItem item3=new JMenuItem("删除");
				popup.add(item1);
				popup.add(item2);
				popup.add(item3);
				popup.getComponent(0).setEnabled(false);
			
		
		
		
				
		/**
		 * 工具栏上的控件代码
		 */
		//工具栏
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 794, 36);
		toolBar.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		contentPane.add(toolBar);

		//新建下载
		JButton btnCreate =
				new JButton(" \u65B0\u5EFA ");
		btnCreate.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton_newActionPerformed(e);
			}
		});
		toolBar.add(btnCreate);
		

		
		//全部开始文件下载
		JButton btnStart = new JButton("全部开始");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Iterator<Boolean> iter = fileFetchInformation.iterator();
				int num = 0;
				while(iter.hasNext()){

					if(iter.next()==false){
						//将其开始
				    	System.out.println("开始第"+(num+1)+"行");
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
		btnStart.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		toolBar.add(btnStart);
		
	
		//全部暂停文件下载
		JButton btnStop = new JButton("全部暂停");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Iterator<Boolean> iter = fileFetchInformation.iterator();
				System.out.println(fileFetchInformation.size());
				int num = 0;
				while(iter.hasNext()){
					if(iter.next()==true){
						//将其暂停
						arrayfileFetch.get(num).siteStop();	
						try {
							SiteFileFetch fetch = new SiteFileFetch(list.get(num));
							arrayfileFetch.set(num, fetch);
							arrayfileFetch.get(num).setbFirst(false);
							fileFetchInformation.set(num,false);
							//model.updateRow(num, fileFetchInfo.get(num), "已暂停", "-----", "usedTime", "-----");	
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
		btnStop.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		toolBar.add(btnStop);
		
		//全部删除下载文件
		JButton btnDelete = new JButton("全部删除");
		btnDelete.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {
				/*
				 * 1.将SiteFileFetch内的数据全部清空
				 * 2.将临时文件和下载文件删除（今后可以改进为跳出对话框提示是否删除原文件）
				 * 3.将该行数据删除
				 */
				//先都暂停

				  @SuppressWarnings("static-access")
				int result= resultJOptionPane.showConfirmDialog(null, "确认全部删除吗?", "全部删除", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		            if(result == JOptionPane.YES_OPTION){

						Iterator<Boolean> iter = fileFetchInformation.iterator();
						System.out.println("filefor"+fileFetchInformation.size());
						int num = 0;
						while(iter.hasNext()){
							if(iter.next()==true){
								//将其停止
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
		btnDelete.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		toolBar.add(btnDelete);
		
		
		//下载历史记录
		JButton btnRecord = new JButton("下载记录");
		btnRecord.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		btnRecord.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				jButton_record(e);
			}
		});
		toolBar.add(btnRecord);
		
		//主体表格
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

		//在此载入下载文件的信息
		loadingMainFile();
		//右键菜单里的三个事件监听器
		item1.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		    	//开始的事件
		    	int rowNum = table.getSelectedRow();
		    	System.out.println("开始的行数"+rowNum);
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
		    	//暂停的事件
		    	int rowNum = table.getSelectedRow();
		    	System.out.println("暂停第"+rowNum+1+"行");
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
				//可以不写
				//model.updateRow(rowNum, list.get(rowNum).getSFileName(), "已暂停", "-----", "usedTime", "-----");	
				
		    }
		});
		
		item3.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		    	//删除的事件
		  	  @SuppressWarnings("static-access")
			int result= resultJOptionPane.showConfirmDialog(null, "确认删除该下载吗?", "全部删除",
						 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		            if(result == JOptionPane.YES_OPTION){
		    	//文件：Info和下载文件;MainFrame;删除list和arrayXXX等动态数组;fileFetchNum-1;在页面上将这一行数据删除
		        int rowNum = table.getSelectedRow();
		    	String s=list.get(rowNum).getSFilePath() +File.separator+ list.get(rowNum).getSFileName();
		    	System.out.println("删除: "+rowNum+" rowNum");
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
		    	//***计时器可以停1s
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
	
	//新建一个任务，并添加到table中
	public void jButton_newActionPerformed(java.awt.event.ActionEvent evt) {
		NewDialog nd= new NewDialog(this);
	    nd.setVisible(true);
	 }
	//查看下载记录
	public void jButton_record(java.awt.event.ActionEvent evt) {
		RecordDialog rd= new RecordDialog(this);
	    rd.setVisible(true);
	 }
	
	//添加一行数据
	//***
	public  void addData(int i)throws IOException {
		
		model.addRow(arrayfileFetch.get(i).getFileName(),"载入",arrayfileFetch.get(i).getSpeeds(),"0:00:00",arrayfileFetch.get(i).getRemainTime());		
	}
	//更新一行数据
	public void updateData(int num)throws IOException{
		String usedTime=null;
		if(fileFetchState.get(num).equals("true"))
		{
			//已下载完成后
			//model.updateRow(num, fileFetchInfo.get(num), "已下载", "-----", usedTime, "-----");	
			fileFetchState.remove(num);
	    	String s=list.get(num).getSFilePath() +File.separator+ list.get(num).getSFileName();
			System.out.println(list.get(num).getSFileName()+"  下载成功");
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
	    	//***计时器可以停1s
	    	fileFetchNum = fileFetchNum - 1;
	    	fileFetchInformation.remove(num);
	    	model.removeRow(num);
	    	updateUI();
			
		}
		else if(fileFetchInformation.get(num)==false){
			model.updateRow(num, list.get(num).getSFileName(), "已暂停", "-----", "-----", "-----");	
		}
			
		else
		{
		   //判断如果已经下载完成，则将相应的状态值置为true
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
	//删除当前选择的行
	public void removeData() {
		int rowNum = table.getSelectedRow();
		model.removeRow(rowNum);
	}	

	public void startDown(){
		try {		
			SiteInfoBean bean = new SiteInfoBean(DPath,SPath,Ithread);	
			arrayfileFetch.get(fileFetchNum).setFileFetch(bean);
			arrayfileFetch.get(fileFetchNum).isFileExist_first();
			arrayfileFetch.get(fileFetchNum).start(); // 调用该线程的run()方法	
			arrayfileFetch.get(fileFetchNum).setstartTime(System.currentTimeMillis());
			//update
			String n=arrayfileFetch.get(fileFetchNum).getFileName();
			fileFetchInfo.add(n);
			fileFetchState.add("false");//对这一行的下载添加一个默认为“false”的状态值 

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
			//文件一定存在
			/*if(string!=null){
				bean.setSFileName(string);//为了只打印一边不跑两遍判定
				File file2 = new File(bean.getSFilePath()+ File.separator + bean.getSFileName() + ".info");
				arrayfileFetch.get(fileFetchNum).setTmpFile(file2);
				arrayfileFetch.get(fileFetchNum).isFileExist();
			}*/
			arrayfileFetch.get(num).start(); // 调用该线程的run()方法	
			arrayfileFetch.get(num).setstartTime(System.currentTimeMillis());
			//update
			//String n=arrayfileFetch.get(num).getFileName();
			//fileFetchInfo.add(n);
			//fileFetchState.add("false");//对这一行的下载添加一个默认为“false”的状态值 

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		   }
	}
	/*
	* 毫秒转化
	*/
	public static String formatTime(long ms) {	            
	            if(ms==0){
	            	return 0+"时"+0+"分"+0+"秒";
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
	             String strDay = day < 10 ? "0" + day : "" + day; //天
	             String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
	             String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
	             String strSecond = second < 10 ? "0" + second : "" + second;//秒
	             String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
	             strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
	             if(day>1){
	            	 return strDay+" 天"+strHour+" 时"+strMinute+" 分"+strSecond+"秒";
	             }else{
	             return strHour+" 时"+strMinute + "分钟 " + strSecond + "秒";
	              }
	             

	            
	   }

	//程序开始时读取下载信息
		public void loadingMainFile(){
			System.out.println("载入下载信息文件");
			try {
				mainFileisExist();//先判断文件是否存在，若不存在，建一个空文件
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
					//添加数据后，页面开始刷新
					setStartUpdate(true);
					System.out.println("input.available():"+input.available());

				}
				input.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
        //判断信息文件是否存在，若不存在，新建一个文件
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