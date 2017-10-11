package xgy.work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import com.view.*;

public class SiteInfoBean {
	private String sSiteURL; // Site's URL
	private String sFilePath; // Saved File's Path
	private String sFileName; // Saved File's Name
	private int nSplitter; // Count of Splited Downloading File

	public SiteInfoBean() {
		// default value of nSplitter is 5
		this("", "", "", 5);
	}

	public SiteInfoBean(String sURL, String sPath, int nSplitter) throws IOException{
		sSiteURL = sURL;
		sFilePath = sPath;
		this.nSplitter = nSplitter;
		//发送请求得到名字
		URL url = new URL(sSiteURL);  
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//conn.setFollowRedirects(true);
		int ret = conn.getResponseCode();
		String fn=URLDecoder.decode(conn.getURL().toString(),"UTF-8");
		System.out.println("------------"+ret+fn);
		System.out.println("------------"+ret+fn.substring(fn.lastIndexOf("/")+1));
    	this.sFileName = fn.substring(fn.lastIndexOf("/")+1);
		sFileName = sFileName.replaceAll("[\"*?<>/:]" , "");
		sFileName = sFileName.replace("\\", "");
		
		/*conn.setRequestMethod("GET");
		conn.setInstanceFollowRedirects(false);
		conn.addRequestProperty("Accept-Charset", "UTF-8");
		conn.addRequestProperty("Referer", sSiteURL);
		conn.connect();
		String location = conn.getHeaderField("Location");
		url = new URL(location);
		conn = (HttpURLConnection) url.openConnection();
		String fn=URLDecoder.decode(conn.getURL().toString(),"UTF-8");
		System.out.println(fn);*/
	
		if(sFileName == null){
			System.err.println("without an exact name");
			this.sFileName = "default.rar";
		}
	}
	
	public SiteInfoBean(String sURL, String sPath) throws IOException{
		this(sURL, sPath, 5);
	}
	
	public SiteInfoBean(String sURL, String sPath, String sName, int nSpiltter) {
		sSiteURL = sURL;
		sFilePath = sPath;
		sFileName = sName;
		this.nSplitter = nSpiltter;
	}

	public String getSSiteURL() {
		return sSiteURL;
	}

	public void setSSiteURL(String value) {
		sSiteURL = value;
	}

	public String getSFilePath() {
		return sFilePath;
	}

	public void setSFilePath(String value) {
		sFilePath = value;
	}

	public String getSFileName() {
		return sFileName;
	}

	public void setSFileName(String value) {
		sFileName = value;
	}

	public int getNSplitter() {
		return nSplitter;
	}

	public void setNSplitter(int nCount) {
		nSplitter = nCount;
	}
}
