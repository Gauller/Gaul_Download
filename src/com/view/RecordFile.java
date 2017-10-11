package com.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class RecordFile {
	
	private String FILENAME="RecordFile.txt";
	
	public RecordFile(){
		
	}

	//�ж���Ϣ�ļ��Ƿ���ڣ��������ڣ��½�һ���ļ�
	public void recordFileisExist(){
		File file = new File(FILENAME);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//��������Ϣд��RecordFile.txt�ļ�
	public void write(String name, String director){
		this.recordFileisExist();
		Date now=new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time = dateFormat.format( now );
        try {
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
            FileWriter fw = new FileWriter("RecordFile.txt", true);
            fw.write(name+"@#"+time+"#%"+director+"\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//���ļ��е�������Ϣ��ȡ������Vector�У����ڱ������ʾ
	public Vector read(){
		
		//�ж���Ϣ�ļ��Ƿ���ڣ��������ڣ��½�һ���ļ�
		this.recordFileisExist();
		
		File file = new File(FILENAME);
        BufferedReader reader = null;
        String name,time,director;
        Vector records=new Vector();
        
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line=1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                name=tempString.split("@")[0];
                time=tempString.split("#")[1];
                director=tempString.split("%")[1];
                Vector row=new Vector(4);
                row.add(0, line);
                row.add(1, name);
                row.add(2, time);
                row.add(3, director);
                records.add(row);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return records;
	}

	public String getFILENAME() {
		return FILENAME;
	}

	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}	
}
