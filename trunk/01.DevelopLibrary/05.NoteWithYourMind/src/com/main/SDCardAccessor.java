package com.main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;
import android.util.Log;

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
    private static final String 					m_strBackupFilePath = "/backup";
    private static final String 					m_strBackupDataBase = "note.db";
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
    public static void copyDataBaseFile(Context context, File fpSource){
    	if(isSDCardAvailable()){
    		try{
    			
    			String strAppDir = m_sdcardDir + m_strAppFilePath;
    	    	File fAppDir = new File(strAppDir);
    	        if(!fAppDir.exists())
    	        {
    	        	fAppDir.mkdir();
    	        } 
    	        String strBackupDir =  strAppDir+m_strBackupFilePath;
    	        File backupDir = new File(strBackupDir);
    	        if(!backupDir.exists()){
    	        	backupDir.mkdir();
    	        }
    	        File fpTarget = new File(strBackupDir, m_strBackupDataBase);
        		if(fpTarget.exists()){
        			fpTarget.delete();
        		}
    			fpTarget.createNewFile();
    			FileChannel src = new FileInputStream(fpSource).getChannel();

    			FileChannel dst = new FileOutputStream(fpTarget).getChannel();

    			dst.transferFrom(src, 0, src.size());

    			src.close();

    			dst.close();
//        		copy(context, fpSource, fpTarget);
    		}catch(IOException e){
    			
    		}
    	}
    }
    public static void copy(Context context, File src, File dst) throws IOException {
//        InputStream in = new FileInputStream(src);
//        OutputStream out = new FileOutputStream(dst);
    	InputStream in = context.openFileInput(src.getAbsolutePath());
    	OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
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
        	if(fp.exists()){
        		fp.delete();	
        	}
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
    
    static List<String> fl = new ArrayList<String>();
    
    public static	List<String> getMyFile()
    {
    	//局部变量 记录文件路径
    	List<String> it=new ArrayList<String>();
    	
    	//指定文件目录
    	String	StrPath	=	Environment.getExternalStorageDirectory().toString();
    	File f=new File( StrPath );
    	
    	//递归
    	getFileList(f);
    	
    	//递归完毕，提出全局量fl里面的内容
    	for(int i = 0; i < fl.size(); i++)
    	{
    	  it.add(fl.get(i));
    	}
    	
    	return it;
    }
    
  //递归函数
    public static void getFileList(File file)
    {
    //列出指定路径下所有文件
       File[] files = file.listFiles();

    //遍历当前各个文件
       for(int i = 0; i < files.length; i++)
       {
         File f = files[i];
         if(f.isFile())
         {
    //如果是文件，则检查其扩展名是否为想要的图片类型
//           if(getImageFile(f.getPath()))
//           {
//
//    //是，则添加进全局量
//             fl.add(f.getPath());
//           }
        	 String	strFile	=	f.getPath();
        	 Log.d("File:", strFile );
        	 if( strFile == CNoteDBCtrl.DB_NAME )
        	 {
        		 
        	 }
         }else if(f.isDirectory())
         {

    //不是文件，而是文件夹，进一步检查这个文件夹下面文件
           getFileList(f);
      	 String	strFolder	=	f.getPath();
    	 Log.d("Folder:", strFolder );
         }
       }
    }
    
    public	static	void	Import( Context context, String strPath )
    {
    	SDDBCtrl	clSDDBCtrl	=	new	SDDBCtrl( context, strPath );
    	clSDDBCtrl.Import();
    }
}  

class SDDBCtrl	extends  SDSQLiteOpenHelper
{
	
	private	SQLiteDatabase 	m_db;
	private	Context 		m_context;
	CNoteDBCtrl 			m_clCNoteDBCtrl;
	
	private HashMap<Integer,Integer> 	m_FolderMap;

	public SDDBCtrl(Context context, String name)
	{
		super(context, name, null, CNoteDBCtrl.DB_VERSION);
		// TODO Auto-generated constructor stub
		
		m_db			=	this.getReadableDatabase();
		m_context		=	context;
		m_clCNoteDBCtrl = CommonDefine.getNoteDBCtrl( m_context );
		
		m_FolderMap	=	new	HashMap<Integer,Integer>();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public	void	Import()
	{
		if( null == m_db )
		{
			return;
		}
		
		ImportFolder();
		ImportMemo();
		
	}
	
	private	void	ImportFolder()
	{
		Cursor SDCursor	=	m_db.rawQuery("select * from " + CNoteDBCtrl.DB_TABLE + " where "+CNoteDBCtrl.KEY_type+"=?" 
							, new String[]{String.valueOf(CMemoInfo.Type_Folder)} );
		if ( !SDCursor.moveToFirst() )
		{
			SDCursor.close();
			return;
		}
		
		subImport( CMemoInfo.Type_Folder, SDCursor );
		
		SDCursor.close();
	}
	

	
	private	void	ImportMemo()
	{
		Cursor SDCursor	=	m_db.rawQuery("select * from " + CNoteDBCtrl.DB_TABLE + " where "+CNoteDBCtrl.KEY_type+"=?" 
							, new String[]{String.valueOf(CMemoInfo.Type_Memo)} );
		if ( !SDCursor.moveToFirst() )
		{
			SDCursor.close();
			return;
		}
		
		subImport( CMemoInfo.Type_Memo, SDCursor );
		
		SDCursor.close();
	}
	
	private	void	subImport( int iType, Cursor SDCursor )
	{
		do
		{
			int 	Index	=	SDCursor.getColumnIndex( CNoteDBCtrl.KEY_uuid );
			String	StrUUID	=	SDCursor.getString( Index );
			Index	=	SDCursor.getColumnIndex( CNoteDBCtrl.KEY_id );
			int		iSDid	=	SDCursor.getInt( Index );
			
			
			Cursor	RomCursor	=	m_clCNoteDBCtrl.selectRecByuuid( StrUUID );
			if ( RomCursor.moveToFirst() )			//存在UUID相同的记录
			{
				Index	=	SDCursor.getColumnIndex( CNoteDBCtrl.KEY_lastmodifytime );
				long	SDTime	=	SDCursor.getLong( Index );
				
				Index	=	RomCursor.getColumnIndex( CNoteDBCtrl.KEY_lastmodifytime );
				long	RomTime	=	SDCursor.getLong( Index );
				
				if( SDTime > RomTime )
				{
					Update( iType, SDCursor );	//UUID是相同的，所以id也一定相同，不需要放入HashMap中
				}
				else
				{
					//UUID相同，但是SD卡中LastModify较旧，不进行更新
				}
			}
			else		//没有找到该UUID是个新的Record，需要插入
			{
				int	iRomid	=	Insert( iType, SDCursor );
				if( iType == CMemoInfo.Type_Folder )
				{
					m_FolderMap.put(iSDid, iRomid);
				}
			}
			
			RomCursor.close();
			
		}while( SDCursor.moveToNext() );
		
	}
	
	private	int	Update( int iType, Cursor SD )
	{
		CMemoInfo clCMemoInfo	=	new	CMemoInfo();	
		ConvertCursorToMemoInfo.ConvertItem( SD, clCMemoInfo );
		if( iType == CMemoInfo.Type_Memo )
		{
			clCMemoInfo.iIsRemindAble	=	CMemoInfo.IsRemind_Able_No;
		}
		else
		{
			
		}
		
		return	m_clCNoteDBCtrl.Update(clCMemoInfo);
	}
	
	private int	Insert( int iType, Cursor SD )
	{
		CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		ConvertCursorToMemoInfo.ConvertItem( SD, clCMemoInfo );
		if( iType == CMemoInfo.Type_Memo )
		{
			int 	Index	=	SD.getColumnIndex( CNoteDBCtrl.KEY_preid );
			int		iSDPid	=	SD.getInt( Index );
			clCMemoInfo.iIsRemindAble	=	CMemoInfo.IsRemind_Able_No;
			Integer			iRomPid		=	m_FolderMap.get(iSDPid);
			if( null !=  iRomPid )
			{
				clCMemoInfo.iPreId		=	iRomPid;
			}
			else
			{
				
			}
		}
		
		clCMemoInfo.iId	=	CommonDefine.g_int_Invalid_ID;
		
		return	m_clCNoteDBCtrl.Update(clCMemoInfo);
	}
}
