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

	//判断信息文件是否存在，若不存在，新建一个文件
	public void recordFileisExist(){
		File file = new File(FILENAME);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//将下载信息写入RecordFile.txt文件
	public void write(String name, String director){
		this.recordFileisExist();
		Date now=new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time = dateFormat.format( now );
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter("RecordFile.txt", true);
            fw.write(name+"@#"+time+"#%"+director+"\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//将文件中的下载信息提取出存入Vector中，以在表格中显示
	public Vector read(){
		
		//判断信息文件是否存在，若不存在，新建一个文件
		this.recordFileisExist();
		
		File file = new File(FILENAME);
        BufferedReader reader = null;
        String name,time,director;
        Vector records=new Vector();
        
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line=1;
            // 一次读入一行，直到读入null为文件结束
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
