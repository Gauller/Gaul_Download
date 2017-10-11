package xgy.work;

import java.beans.beancontext.BeanContext;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.view.*;

public class SiteFileFetch extends Thread{
	SiteInfoBean siteInfoBean = null; // 文件信息 Bean
	long[] nStartPos; // 开始位置
	long[] nEndPos; // 结束位置
	FileSplitterFetch[] fileSplitterFetch; // 子线程对象
	long nFileLength; // 文件长度
	boolean bFirst = true; // 是否第一次取文件
	public boolean isbFirst() {
		return bFirst;
	}
	public void setbFirst(boolean bFirst) {
		this.bFirst = bFirst;
	}

	private boolean bStop = false; // 停止标志
	File tmpFile; // 文件下载的临时信息
	//速度
	long[] nLength;
	long leftLengthBefore = 0;
	long leftLengthNow = 0;
	double speeds;
	//计算下载所花时间
	long startTime=0;
	//显示
	public String FileName;
	//是否完成下载
	boolean isDone=false;
	//
	public File getTmpFile() {
		return tmpFile;
	}
	public void setTmpFile(File tmpFile) {
		this.tmpFile = tmpFile;
	}

	DataOutputStream output; // 输出到文件的输出流


	//构造器  需要文件信息 SiteInfoBean
	public SiteFileFetch(SiteInfoBean bean) throws IOException {
		siteInfoBean = bean;
		FileName=bean.getSFileName();
		// tmpFile = File.createTempFile ("zhong","1111",new
		// File(bean.getSFilePath()));
		tmpFile = new File(bean.getSFilePath() + File.separator	//separator windows corresponds to\ while unix is /
				+ bean.getSFileName() + ".info");
		//判断临时文件是否存在 以此也可以判定是否进行过存储
		//***
		/*if (tmpFile.exists()) {
			bFirst = false;
			read_nPos();
		} else {
		//从文件信息 SiteInfoBean提取段数的数据
			nStartPos = new long[bean.getNSplitter()];
			nEndPos = new long[bean.getNSplitter()];
		}*/
	}
	//构造一个空的
	public SiteFileFetch() {
		// TODO Auto-generated constructor stub
	}
	//对空的进行设置
	public void setFileFetch(SiteInfoBean bean) throws IOException{
		siteInfoBean = bean;
		FileName=bean.getSFileName();
		tmpFile = new File(bean.getSFilePath() + File.separator	//separator windows corresponds to\ while unix is /
				+ bean.getSFileName() + ".info");
	}
	
	public boolean isFileExist_first(){
		String tempAddressString = siteInfoBean.getSFilePath() + File.separator + siteInfoBean.getSFileName();
		File file = new File(tempAddressString);
			//不行起始劃分空間
		if(file.exists()){
			if(getFileSize() == file.length()){
				nFileLength= getFileSize();
				System.out.println("already exist");//不能对分段数进行修改
				//分段判定应该在isFileExist后面***
				bFirst = false;

				return true;
			}
			else return false;
		}
		else return false;
	}

	
	
	
	
	
	public String isFileExist(){
		int tempnum = 1;
		String tempString = "("+tempnum+")";
		String tempAddressString = siteInfoBean.getSFilePath() + File.separator + siteInfoBean.getSFileName();
		File file = new File(tempAddressString);
		if(tmpFile.exists()){
			//不行起始劃分空間
				while(true && tempnum < 60000){
					if(new File(siteInfoBean.getSFilePath() + File.separator + tempString+ siteInfoBean.getSFileName()).exists()) 
						tempnum++;
					else {
						System.out.println("有重名不同文件 已更名");
						//File file2 = new File(tempAddressString+tempString + ".info");
						//***
						//setTmpFile(file2);
						return (tempString+ siteInfoBean.getSFileName());
					}
				}
				return null;
		}
		else{
			System.out.println("new download");
			nFileLength= getFileSize();
			//从文件信息 SiteInfoBean提取段数的数据
			nStartPos = new long[siteInfoBean.getNSplitter()];
			nEndPos = new long[siteInfoBean.getNSplitter()];
			return null;
		}
	}	

	public void run() 
	 {
	 // 获得文件长度
	 // 分割文件
	 // 实例 FileSplitterFetch 
	 // 启动 FileSplitterFetch 线程
	 // 等待子线程返回
	 try{ 
		 if(bFirst)  {
			 newFile();
			 newLoadingMainFile();
			 //前台在这里修改线程数量   setNSplitter(threadNum);
		 }
		 else	read_nPos();
		 // 启动子线程
		 fileSplitterFetch = new FileSplitterFetch[nStartPos.length]; 
		 for(int i=0;i<nStartPos.length;i++) 
		 { 
			 fileSplitterFetch[i] = new FileSplitterFetch(siteInfoBean.getSSiteURL(), 
					 siteInfoBean.getSFilePath() + File.separator + siteInfoBean.getSFileName(), 
					 nStartPos[i],nEndPos[i],i); 
			 Utility.log("Thread " + i + " , nStartPos = " + nStartPos[i] + ", nEndPos = " 
					 + nEndPos[i]); 
			 fileSplitterFetch[i].start(); 
		 } 
		 // 等待子线程结束
		 //int count = 0; 
		 // 是否结束 while 循环
		 boolean breakWhile = false; 
		 while(!bStop) 
		 { 
			 write_nPos(); 
			 //显示速度和下载百分比
			 showSpeed();
			 showPercent();
			 Utility.sleep(500); 
			 breakWhile = true; 
			 for(int i=0;i<nStartPos.length;i++) { 
				 if(!fileSplitterFetch[i].bDownOver) 
				 { 
					 breakWhile = false; 
					 break; 
				 } 
		     } 
			 if(breakWhile) 
			 break; 
			 //count++; 
			 //if(count>4) 
			 // siteStop(); 
		 } 
		 if(!bStop){
			 isDone=true;
			 System.out.println("已下载100%");
			 System.out.println("文件下载结束！"); 
		 //下载完之后将临时文件删除
			 tmpFile.delete();
		 }
		 //this.join();
		 for(int i=0;i<nStartPos.length;i++) 
		 { 
			 
			 fileSplitterFetch[i].join();
			 System.out.println("运行到最后1");
		 }
		 System.out.println("运行到最后2");
	}catch(Exception e){e.printStackTrace ();} 
	 }	
	
	//计算速度
	public double getSpeed() throws IOException{
		DataInputStream input = new DataInputStream(new FileInputStream(
				tmpFile));
		int nCount = input.readInt();
		nStartPos = new long[nCount];
		nEndPos = new long[nCount];
		nLength=new long[nCount];
		long tmp_leftLengthNow=0;
		for(int i=0;i<nStartPos.length;i++){						
			nStartPos[i] = input.readLong();
			nEndPos[i] = input.readLong();
			nLength[i]=nEndPos[i]-nStartPos[i];
			tmp_leftLengthNow += nLength[i];
		 }
		leftLengthNow = tmp_leftLengthNow;
		//System.out.println("1:"+leftLengthNow+"  2:"+leftLengthBefore);
		if(leftLengthBefore == 0){
		    double speed=0;
		    leftLengthBefore = leftLengthNow;
		    input.close();
		    return speed;
		}
		else{
			 double speed=(leftLengthBefore-leftLengthNow)/512;
		     leftLengthBefore=leftLengthNow;
		     
		     //System.out.println("1:"+leftLengthNow+" 2:"+leftLengthBefore);
	         input.close();
			 return speed;
		}
		
	}
	//秒转时间格式
	public static String changeTime(int second){  
        int h = 0;  
        int d = 0;  
        int s = 0;  
        int temp = second%3600;  
             if(second>3600){  
               h= second/3600;  
                    if(temp!=0){  
               if(temp>60){  
               d = temp/60;  
            if(temp%60!=0){  
               s = temp%60;  
              }  
            }else{  
               s = temp;  
            }  
           }  
          }else{  
              d = second/60;  
           if(second%60!=0){  
              s = second%60;  
           }  
          }  

         return h+"时"+d+"分"+s+"秒";  
       }  

	public void showSpeed() throws IOException{
		//保护判断
		 if (tmpFile.exists()) {
				speeds = getSpeed();
				System.out.println("下载速度:"+speeds+"kB/s");
				
				if(speeds == 0){
					System.out.println("剩余时间-时-分-秒");System.out.println("---------------------");
				}		
				else{
					double lefttime=leftLengthNow/(speeds*1024);
					int x=(int)lefttime;
					System.out.println("剩余时间"+changeTime(x));System.out.println("---------------------");
				}
				
		 }
		
	}
	

	//--------------------------------------------------------
	//刘佳兴 0324
	//得到剩余时间
	public String getRemainTime()throws IOException{
		String rt;
        if(speeds == 0){
			rt="-----";
		}		
		else{
			double lefttime=leftLengthNow/(speeds*1024);
			int x=(int)lefttime;
			rt=changeTime(x);
		}
		return rt;
	}
	//得到文件名
	public String getFileName(){
		return FileName;
	}
	
	//得到下载百分比
	public long getPercent(){
		if(nFileLength!=0){
		long temp_percent = (nFileLength-leftLengthNow)*100/nFileLength;
		if(temp_percent==100 && isDone == false){
			temp_percent = 0;
		}
		DecimalFormat dFormat = new DecimalFormat("#.00");  
		dFormat.format(temp_percent);
		return temp_percent;
		}
		else{
			return 0;
		}
	}
	//显示下载百分比
	public void showPercent(){
		long percent=getPercent();
		System.out.println("已下载"+percent+"%");

		
	}
	
	
	//--------------------------------
	//
	// 获得文件长度
	public long getFileSize() {
		int nFileLength = -1;
		try {
			URL url = new URL(siteInfoBean.getSSiteURL());
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			//***
			httpConnection.setRequestProperty("User-Agent", "RANGE");

			System.out.println("********************************"+url);
			
			int responseCode = httpConnection.getResponseCode();

			if (responseCode >= 400) {
				processErrorCode(responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				// DataInputStream in = new
				// DataInputStream(httpConnection.getInputStream ());
				// Utility.log(in.readLine());
				sHeader = httpConnection.getHeaderFieldKey(i);
				System.out.println(sHeader+":"+httpConnection.getHeaderField(i));
				if (sHeader != null) {
					if (sHeader.equalsIgnoreCase("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection
								.getHeaderField(sHeader));
						System.out.println(nFileLength);
						break;
					}
				} else
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Utility.log(nFileLength);
		return nFileLength;
	}

	//***
	//key
	// 保存下载信息（文件指针位置）
	private void write_nPos() {
		try {
			output = new DataOutputStream(new FileOutputStream(tmpFile));
			output.writeInt(nStartPos.length);
			for (int i = 0; i < nStartPos.length; i++) {
				// output.writeLong(nPos[i]);
				output.writeLong(fileSplitterFetch[i].nStartPos);
				output.writeLong(fileSplitterFetch[i].nEndPos);
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读取保存的下载信息（文件指针位置）
	private void read_nPos() {
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(tmpFile));
			int nCount = input.readInt();
			//中断续传 取出之前的线程数
			siteInfoBean.setNSplitter(nCount);
			nStartPos = new long[nCount];
			nEndPos = new long[nCount];
			for (int i = 0; i < nStartPos.length; i++) {
				nStartPos[i] = input.readLong();
				nEndPos[i] = input.readLong();
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processErrorCode(int nErrorCode) {
		System.err.println("Error Code : " + nErrorCode);
	}

	// 停止文件下载
	public void siteStop() {
		System.out.println("停止SiteFileFetch");
		System.out.println(nStartPos.length);
		for (int i = 0; i < nStartPos.length; i++) {
			if(fileSplitterFetch[i].isAlive()){
				fileSplitterFetch[i].splitterStop();
				System.out.println(i);
			}
		}
		setbStop(true);
		
		
	}
	public void isSpaceEnough() {
		File file = new File(siteInfoBean.getSFilePath());
		if(file.getFreeSpace()>(nFileLength + 10485760)) System.out.println(file.getFreeSpace());//多预留10k给tmep
		else System.err.println("out of space");
	}
	
	public void spaceAllocation() throws IOException{
		 RandomAccessFile file =new RandomAccessFile(siteInfoBean.getSFilePath()+File.separator+siteInfoBean.getSFileName(),"rw");
		 file.setLength(nFileLength);
		 file.close();
	 }
	
	public void newFile() throws IOException{
		//判断文件大小 以便进行分段
		 nFileLength = getFileSize(); 	
		 if(nFileLength == -1) 
		 { 
			 System.err.println("File Length is not known!"); 
		 } 
		 else if(nFileLength == -2) 
		 { 
			 System.err.println("File is not access!"); 
		 } 
		 else 
		 { 
			 //优先分配空间 可以解决当文件已下载或进行下载时的比对
			 isSpaceEnough();
			 spaceAllocation();
			 for(int i=0;i<nStartPos.length;i++) 
			 { 
				 nStartPos[i] = (long)(i*(nFileLength/nStartPos.length)); 
			 } 
			 for(int i=0;i<nEndPos.length-1;i++) 
			 { 
				 nEndPos[i] = nStartPos[i+1]; 
			 } 
			 nEndPos[nEndPos.length-1] = nFileLength; 
		 } 			 	 
	}

	//插入信息文件
	private void newLoadingMainFile() {
		try {
			DataOutputStream output_1 = new DataOutputStream(new FileOutputStream("MainFile",true));
			output_1.writeUTF(siteInfoBean.getSSiteURL());
			output_1.writeUTF(siteInfoBean.getSFilePath());
			output_1.writeUTF(siteInfoBean.getSFileName());
			output_1.writeInt(siteInfoBean.getNSplitter());
			output_1.close();
            System.out.println("插入"+siteInfoBean.getSFileName()+"下载信息到MainFile文件成功");				
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	//删除信息文件
	public void deleteMainFile() {
		ArrayList<SiteInfoBean> _list = new ArrayList<SiteInfoBean>();
		try {
			DataInputStream _input = new DataInputStream(new FileInputStream("MainFile"));
			while(true){
				//System.out.println(input1.available());
				SiteInfoBean _bean = new SiteInfoBean();
				_bean.setSSiteURL(_input.readUTF());
				_bean.setSFilePath(_input.readUTF());
				_bean.setSFileName(_input.readUTF());
				_bean.setNSplitter(_input.readInt());
				_list.add(_bean);
				if(_input.available()<=0){
					_input.close();
					DataOutputStream _output = new DataOutputStream(new FileOutputStream("MainFile"));
					_output.flush();
					_output.close();
					break;
				}
			}
			Iterator<SiteInfoBean> iterator = _list.iterator();
			DataOutputStream output8 = new DataOutputStream(new FileOutputStream("MainFile",true));
			while(iterator.hasNext()){
				SiteInfoBean bean_1 = iterator.next();
				if(bean_1.getSFileName().equals(siteInfoBean.getSFileName())){continue;}
				else {
					output8.writeUTF(bean_1.getSSiteURL());
					output8.writeUTF(bean_1.getSFilePath());
					output8.writeUTF(bean_1.getSFileName());
					output8.writeInt(bean_1.getNSplitter());
				}
			}
			output8.close();
			
		} catch (Exception e) {
		// TODO: handle exception
		}
	}

	public boolean getbStop() {
		return bStop;
	}

	public void setbStop(boolean bStop) {
		this.bStop = bStop;
	}

	public double getSpeeds() {
		return speeds;
	}

	public void setSpeeds(double speeds) {
		this.speeds = speeds;
	}


	public long getstartTime() {
		return startTime;
	}

	public void setstartTime(long startTime) {
		this.startTime = startTime;
	}
	public boolean isDone() {
		return isDone;
	}
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}



	
	
}
