package com.view;

// �������ʱû����

public class TableInfo {
	private String Filename;
	private long temp_percent;
	private double speed;
	private String usetime;  //����ʱ��
    private String timeleft; //ʣ��ʱ��
    
    public void setFilename(String Filename){
    	this.Filename = Filename;
    }
    public String getFilename(){
    	return Filename;
    }
    
    public void settemp_percent(long temp_percent){
    	this.temp_percent = temp_percent;
    }
    public long gettemp_percent(){
    	return temp_percent;
    }
    
    public void setspeed(double speed){
    	this.speed = speed;
    }
    public double getspeed(){
    	return speed;
    }
    
    public void setusetime(String usetime){
    	this.usetime = usetime;
    }
    public String getusetime(){
    	return usetime;
    }
    
    public void settimeleft(String timeleft){
    	this.timeleft = timeleft;
    }
    public String gettimeleft(){
    	return timeleft;
    }

}

