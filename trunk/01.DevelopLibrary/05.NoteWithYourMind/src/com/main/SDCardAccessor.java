package com.main;
import java.io.File;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

interface SDCardStatusChangedCtrl{
	public void ChangedAvailable();
	public void ChangedUnAvailable();
}

public class SDCardAccessor extends BroadcastReceiver {  
	private enum SDCardStatusChanged{
		SDCardStatusChanged_Available,
		SDCardStatusChanged_UnAvailable
	}
    private static boolean m_sdcardAvailable = false;  
    private static boolean m_sdcardAvailabilityDetected = false;  
    private static String m_sdcardDir = null;
    private static final String 					m_strAudioFilePath = "/record";
    private static final String 					m_strAppFilePath = "/note";
    private static File				m_audioDataDir = null;
    private static ArrayList<SDCardStatusChangedCtrl> m_ctrlList = null;
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
    public static void advise(SDCardStatusChangedCtrl ctrl){
    	if(m_ctrlList == null){
    		m_ctrlList = new ArrayList<SDCardStatusChangedCtrl>();
    	}else{
    		m_ctrlList.add(ctrl);
    	}
    }
    public static void unadvise(SDCardStatusChangedCtrl ctrl){
    	if(m_ctrlList!=null){
    		m_ctrlList.remove(ctrl);
    	}
    }
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
    public static boolean deleteAudioDataFile(String audioFileName){
    	boolean bIsDeleteSuccess = false;
    	if(isSDCardAvailable()){
        	File fp = new File(m_audioDataDir, audioFileName);
        	fp.delete();
        	fp = null;
        	bIsDeleteSuccess = true;
    	}else{
    		bIsDeleteSuccess = false;
    	}
    	return bIsDeleteSuccess;
    }
    public static boolean isSDCardAvailable() {  
        if(!m_sdcardAvailabilityDetected) {  
        	m_sdcardAvailable = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        	m_sdcardAvailabilityDetected = true;  
        	if(m_sdcardAvailable ){
        		if(m_sdcardDir==null){
        			m_sdcardDir = Environment.getExternalStorageDirectory().toString();	
        		}
        		notifyCtrl(SDCardStatusChanged.SDCardStatusChanged_Available);
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
        	notifyCtrl(SDCardStatusChanged.SDCardStatusChanged_Available);
        	if(m_sdcardDir==null){
        		m_sdcardDir = Environment.getExternalStorageDirectory().toString();
        	}
        }else{
        	notifyCtrl(SDCardStatusChanged.SDCardStatusChanged_UnAvailable);
        	m_sdcardAvailable = false;
        }
        m_sdcardAvailabilityDetected = true;  
    }  
    private static void notifyCtrl(SDCardStatusChanged status){
    	if(m_ctrlList!=null){
    		int size = m_ctrlList.size();
        	for(int i = 0; i < size; i++){
        		SDCardStatusChangedCtrl temp = m_ctrlList.get(i);
        		if(temp!=null){
        			if(status == SDCardStatusChanged.SDCardStatusChanged_Available){
        				temp.ChangedAvailable();
        			}else{
        				temp.ChangedUnAvailable();	
        			}
        		}
        	}	
    	}
    }
}  