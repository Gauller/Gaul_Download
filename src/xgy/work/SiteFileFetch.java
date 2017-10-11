package xgy.work;

import java.beans.beancontext.BeanContext;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.view.*;

public class SiteFileFetch extends Thread{
	SiteInfoBean siteInfoBean = null; // �ļ���Ϣ Bean
	long[] nStartPos; // ��ʼλ��
	long[] nEndPos; // ����λ��
	FileSplitterFetch[] fileSplitterFetch; // ���̶߳���
	long nFileLength; // �ļ�����
	boolean bFirst = true; // �Ƿ��һ��ȡ�ļ�
	public boolean isbFirst() {
		return bFirst;
	}
	public void setbFirst(boolean bFirst) {
		this.bFirst = bFirst;
	}

	private boolean bStop = false; // ֹͣ��־
	File tmpFile; // �ļ����ص���ʱ��Ϣ
	//�ٶ�
	long[] nLength;
	long leftLengthBefore = 0;
	long leftLengthNow = 0;
	double speeds;
	//������������ʱ��
	long startTime=0;
	//��ʾ
	public String FileName;
	//�Ƿ��������
	boolean isDone=false;
	//
	public File getTmpFile() {
		return tmpFile;
	}
	public void setTmpFile(File tmpFile) {
		this.tmpFile = tmpFile;
	}

	DataOutputStream output; // ������ļ��������


	//������  ��Ҫ�ļ���Ϣ SiteInfoBean
	public SiteFileFetch(SiteInfoBean bean) throws IOException {
		siteInfoBean = bean;
		FileName=bean.getSFileName();
		// tmpFile = File.createTempFile ("zhong","1111",new
		// File(bean.getSFilePath()));
		tmpFile = new File(bean.getSFilePath() + File.separator	//separator windows corresponds to\ while unix is /
				+ bean.getSFileName() + ".info");
		//�ж���ʱ�ļ��Ƿ���� �Դ�Ҳ�����ж��Ƿ���й��洢
		//***
		/*if (tmpFile.exists()) {
			bFirst = false;
			read_nPos();
		} else {
		//���ļ���Ϣ SiteInfoBean��ȡ����������
			nStartPos = new long[bean.getNSplitter()];
			nEndPos = new long[bean.getNSplitter()];
		}*/
	}
	//����һ���յ�
	public SiteFileFetch() {
		// TODO Auto-generated constructor stub
	}
	//�ԿյĽ�������
	public void setFileFetch(SiteInfoBean bean) throws IOException{
		siteInfoBean = bean;
		FileName=bean.getSFileName();
		tmpFile = new File(bean.getSFilePath() + File.separator	//separator windows corresponds to\ while unix is /
				+ bean.getSFileName() + ".info");
	}
	
	public boolean isFileExist_first(){
		String tempAddressString = siteInfoBean.getSFilePath() + File.separator + siteInfoBean.getSFileName();
		File file = new File(tempAddressString);
			//������ʼ���ֿ��g
		if(file.exists()){
			if(getFileSize() == file.length()){
				nFileLength= getFileSize();
				System.out.println("already exist");//���ܶԷֶ��������޸�
				//�ֶ��ж�Ӧ����isFileExist����***
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
			//������ʼ���ֿ��g
				while(true && tempnum < 60000){
					if(new File(siteInfoBean.getSFilePath() + File.separator + tempString+ siteInfoBean.getSFileName()).exists()) 
						tempnum++;
					else {
						System.out.println("��������ͬ�ļ� �Ѹ���");
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
			//���ļ���Ϣ SiteInfoBean��ȡ����������
			nStartPos = new long[siteInfoBean.getNSplitter()];
			nEndPos = new long[siteInfoBean.getNSplitter()];
			return null;
		}
	}	

	public void run() 
	 {
	 // ����ļ�����
	 // �ָ��ļ�
	 // ʵ�� FileSplitterFetch 
	 // ���� FileSplitterFetch �߳�
	 // �ȴ����̷߳���
	 try{ 
		 if(bFirst)  {
			 newFile();
			 newLoadingMainFile();
			 //ǰ̨�������޸��߳�����   setNSplitter(threadNum);
		 }
		 else	read_nPos();
		 // �������߳�
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
		 // �ȴ����߳̽���
		 //int count = 0; 
		 // �Ƿ���� while ѭ��
		 boolean breakWhile = false; 
		 while(!bStop) 
		 { 
			 write_nPos(); 
			 //��ʾ�ٶȺ����ذٷֱ�
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
			 System.out.println("������100%");
			 System.out.println("�ļ����ؽ�����"); 
		 //������֮����ʱ�ļ�ɾ��
			 tmpFile.delete();
		 }
		 //this.join();
		 for(int i=0;i<nStartPos.length;i++) 
		 { 
			 
			 fileSplitterFetch[i].join();
			 System.out.println("���е����1");
		 }
		 System.out.println("���е����2");
	}catch(Exception e){e.printStackTrace ();} 
	 }	
	
	//�����ٶ�
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
	//��תʱ���ʽ
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

         return h+"ʱ"+d+"��"+s+"��";  
       }  

	public void showSpeed() throws IOException{
		//�����ж�
		 if (tmpFile.exists()) {
				speeds = getSpeed();
				System.out.println("�����ٶ�:"+speeds+"kB/s");
				
				if(speeds == 0){
					System.out.println("ʣ��ʱ��-ʱ-��-��");System.out.println("---------------------");
				}		
				else{
					double lefttime=leftLengthNow/(speeds*1024);
					int x=(int)lefttime;
					System.out.println("ʣ��ʱ��"+changeTime(x));System.out.println("---------------------");
				}
				
		 }
		
	}
	

	//--------------------------------------------------------
	//������ 0324
	//�õ�ʣ��ʱ��
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
	//�õ��ļ���
	public String getFileName(){
		return FileName;
	}
	
	//�õ����ذٷֱ�
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
	//��ʾ���ذٷֱ�
	public void showPercent(){
		long percent=getPercent();
		System.out.println("������"+percent+"%");

		
	}
	
	
	//--------------------------------
	//
	// ����ļ�����
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
	// ����������Ϣ���ļ�ָ��λ�ã�
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

	// ��ȡ�����������Ϣ���ļ�ָ��λ�ã�
	private void read_nPos() {
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(tmpFile));
			int nCount = input.readInt();
			//�ж����� ȡ��֮ǰ���߳���
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

	// ֹͣ�ļ�����
	public void siteStop() {
		System.out.println("ֹͣSiteFileFetch");
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
		if(file.getFreeSpace()>(nFileLength + 10485760)) System.out.println(file.getFreeSpace());//��Ԥ��10k��tmep
		else System.err.println("out of space");
	}
	
	public void spaceAllocation() throws IOException{
		 RandomAccessFile file =new RandomAccessFile(siteInfoBean.getSFilePath()+File.separator+siteInfoBean.getSFileName(),"rw");
		 file.setLength(nFileLength);
		 file.close();
	 }
	
	public void newFile() throws IOException{
		//�ж��ļ���С �Ա���зֶ�
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
			 //���ȷ���ռ� ���Խ�����ļ������ػ��������ʱ�ıȶ�
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

	//������Ϣ�ļ�
	private void newLoadingMainFile() {
		try {
			DataOutputStream output_1 = new DataOutputStream(new FileOutputStream("MainFile",true));
			output_1.writeUTF(siteInfoBean.getSSiteURL());
			output_1.writeUTF(siteInfoBean.getSFilePath());
			output_1.writeUTF(siteInfoBean.getSFileName());
			output_1.writeInt(siteInfoBean.getNSplitter());
			output_1.close();
            System.out.println("����"+siteInfoBean.getSFileName()+"������Ϣ��MainFile�ļ��ɹ�");				
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	//ɾ����Ϣ�ļ�
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
