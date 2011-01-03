package com.main;
import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class SDCardAccessor extends BroadcastReceiver {  
	  
    private static boolean m_sdcardAvailable = false;  
    private static boolean m_sdcardAvailabilityDetected = false;  
    private static String m_sdcardDir = null;
    private static final String 					m_strAudioFilePath = "/record";
    private static final String 					m_strAppFilePath = "/note";
    private static File				m_audioDataDir = null;
//    public static synchronized boolean detectSDCardAvailability() {  
//        boolean result = false;  
//        try {  
// 
//        } catch (Exception e) {  
//            // Can't create file, SD Card is not available  
//            e.printStackTrace();  
//        } finally {  
//            sdcardAvailabilityDetected = true;  
//            sdcardAvailable = result;  
//        }  
//        return result;  
//    }  
      
    /** 
     *  
     * @return SD is available ? 
     */  
    public static File getAudioDataDir(){
    	if(isSDCardAvailable()){
    		if(m_audioDataDir==null){
    			String strAppDir = m_sdcardDir + m_strAppFilePath;
    	    	File fAppDir = new File(strAppDir);
    	        if(!fAppDir.exists())
    	        {
    	        	fAppDir.mkdir();
    	        } 
    	        String strAudioDir =  strAppDir+m_strAudioFilePath;
    	        m_audioDataDir = new File(strAudioDir);
    	        if(!m_audioDataDir.exists()){
    	        	m_audioDataDir.mkdir();
    	        }	
    		}
    	}else{
    		m_audioDataDir = null;
    	}
    	return m_audioDataDir;
    }
    public static boolean isSDCardAvailable() {  
        if(!m_sdcardAvailabilityDetected) {  
        	m_sdcardAvailable = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        	m_sdcardAvailabilityDetected = true;  
        	if(m_sdcardAvailable && m_sdcardDir==null){
        		m_sdcardDir = Environment.getExternalStorageDirectory().toString();
        	}
        }  
        return  m_sdcardAvailable;  
    }  
      
    /* (non-Javadoc) 
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent) 
     */   
    public void onReceive(Context context, Intent intent) {  
        m_sdcardAvailabilityDetected = false; 
        if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){
        	m_sdcardAvailable = true;
        	if(m_sdcardDir==null){
        		m_sdcardDir = Environment.getExternalStorageDirectory().toString();
        	}
        }else{
        	m_sdcardAvailable = false;
        }
        m_sdcardAvailabilityDetected = true;  
    }  
  
}  