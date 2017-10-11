package xgy.work;

import java.io.*;
import com.view.*;

public class FileAccess implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	RandomAccessFile oSavedFile;
	
	//设置文件读写偏移量的文件指针
	long nPos;

	public FileAccess() throws IOException {
		this("", 0);
	}

	public FileAccess(String sName, long nPos) throws IOException {
		oSavedFile = new RandomAccessFile(sName, "rw");
		this.nPos = nPos;
		oSavedFile.seek(nPos);
	}

	//中断保护算法 实际服务器会处理 只是实验算法
	/*public long ReSet(long startpos , String sName , int remainder) throws IOException{
		oSavedFile = new RandomAccessFile(sName, "rw");
		//1024x+r<y
		long x  = 1024*((startpos-remainder)/1024 - 1);
		if(x < 0) x = 0;
		oSavedFile.seek(x);
		return x;
	}*/
	
	public RandomAccessFile getoSavedFile() {
		return oSavedFile;
	}

	public void setoSavedFile(RandomAccessFile oSavedFile) {
		this.oSavedFile = oSavedFile;
	}

	public synchronized int write(byte[] b, int nStart, int nLen) {
		int n = -1;
		try {
			oSavedFile.write(b, nStart, nLen);
			n = nLen;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return n;
	}
}
