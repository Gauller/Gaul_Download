package xgy.work;

import java.io.*;
import com.view.*;
import java.net.*;

public class FileSplitterFetch extends Thread {
	String sURL; // File URL
	String sName;
	long nStartPos; // File Snippet Start Position
	long nEndPos; // File Snippet End Position
	int nThreadID; // Thread's ID
	boolean bDownOver = false; // Downing is over
	boolean bStop = false; // Stop identical
	FileAccess fileAccess = null; // File Access interface
	//
	boolean bPause=false;

	public FileSplitterFetch(String sURL, String sName, long nStart, long nEnd,
			int id) throws IOException {
		this.sURL = sURL;
		this.nStartPos = nStart;
		this.nEndPos = nEnd;
		nThreadID = id;
		this.sName = sName;
		fileAccess = new FileAccess(sName, nStartPos);
	}

	public void run() {
		
		/*final long StartPos = nStartPos;
		final int remainder = Integer.parseInt(String.valueOf(StartPos % 1024));
		// 当该线程未下载完毕切为暂停时
		//
		try {
			nStartPos = fileAccess.ReSet(nStartPos, sName, remainder);
			Utility.log("ReSetnStartPos:" + String.valueOf(nStartPos)
					+ "  Remainder:" + remainder);
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		if (nStartPos < nEndPos && !bStop) {

			try {
				URL url = new URL(sURL);
				HttpURLConnection httpConnection = (HttpURLConnection) url
						.openConnection();
				httpConnection.setRequestProperty("User-Agent", "RANGE");
				String sProperty = "bytes=" + nStartPos + "-";
				httpConnection.setRequestProperty("RANGE", sProperty);
				System.out.println(httpConnection.getResponseCode());
				Utility.log(sProperty);
				InputStream input = httpConnection.getInputStream();
				// logResponseHead(httpConnection);
				byte[] b = new byte[1024];
				int nRead;
				while (nStartPos < nEndPos && !bStop) {
					// 防混淆越界测试判定 无实际用处此判定
					if (nEndPos - nStartPos < 1024) {
						nRead = input.read(b,0,(Integer.parseInt(String.valueOf(nEndPos - nStartPos))));
						int x = fileAccess.write(b, 0, nRead);
						//Utility.log("nStart:" + nStartPos + " nRead:" + nRead);
						nStartPos += x;
					} else {
						nRead = input.read(b, 0, 1024);
						int x = fileAccess.write(b, 0, nRead);
						//Utility.log("nStart:" + nStartPos + " nRead:" + nRead);
						nStartPos += x;
					}
					// if(nThreadID == 1)
					// Utility.log("nStartPos = " + nStartPos + ", nEndPos = " +
					// nEndPos);
				}
				if(!bStop){
				Utility.log("Thread " + nThreadID + " is over!");
				bDownOver = true;
				}
				input.close();
				fileAccess.getoSavedFile().close();
				System.out.println("停止FileSpliter");
				// nPos = fileAccessI.write (b,0,nRead);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(nStartPos == nEndPos) bDownOver = true;

	}

	// 打印回应的头信息
	public void logResponseHead(HttpURLConnection con) {
		for (int i = 1;; i++) {
			String header = con.getHeaderFieldKey(i);
			if (header != null)
				// responseHeaders.put(header,httpConnection.getHeaderField(header));
				Utility.log(header + " : " + con.getHeaderField(header));
			else
				break;
		}
	}

	public void splitterStop() {
		bStop = true;
	}
}
