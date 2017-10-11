package com.view;
import java.io.IOException;
import java.util.Timer;
public class TimerTaskTest extends java.util.TimerTask {

	MainFrame frame;
    public TimerTaskTest(MainFrame frame){
		this.frame = frame;
    }
	public void run()  {  
		
			if(frame.isStartUpdate()){
				try {
					//update
					//***
					for(int j=0;j<frame.fileFetchNum;j++){
					frame.updateData(j);
					frame.updateUI();
					//frame.repaint();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
		}	
		   
		}  
}
