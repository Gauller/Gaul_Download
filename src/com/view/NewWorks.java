package com.view;

//这个类暂时没用上

import java.io.File;

import xgy.work.SiteFileFetch;
import xgy.work.SiteInfoBean;

public class NewWorks {
	
    public void  newWork (String url,String path,int count) throws Exception{

    	String DPath = url;
    	String SPath = path;
    	int Ithread = count;
    	
    	SiteInfoBean bean = new SiteInfoBean(DPath,SPath,Ithread);
    	
		SiteFileFetch fileFetch = new SiteFileFetch(bean);
		String string = fileFetch.isFileExist();
		if(string!=null){
			bean.setSFileName(string);//为了只打印一边不跑两遍判定
			File file2 = new File(bean.getSFilePath()+ File.separator + bean.getSFileName() + ".info");
			fileFetch.setTmpFile(file2);
			fileFetch.isFileExist();
		}
		fileFetch.start(); // 调用该线程的run()方法
    	
    }

}
