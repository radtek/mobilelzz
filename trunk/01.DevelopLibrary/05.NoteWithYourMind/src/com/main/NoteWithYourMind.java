package com.main;

import java.text.SimpleDateFormat;
import java.util.*; 
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Chronometer.OnChronometerTickListener;
import android.util.*;
import android.net.Uri;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

@SuppressWarnings("unused")
public class NoteWithYourMind extends Activity implements View.OnClickListener
{
	//前一个Activity的类型
	public	enum	OperationNoteKindEnum
	{
		OperationNoteKind_New,
		OperationNoteKind_Edit,
		OperationNoteKind_Update
	}
	
	public static String 								ExtraData_EditNoteID		=	"com.main.ExtraData_EditNoteID";
	public static String 								ExtraData_OperationNoteKind	=	"com.main.ExtraData_OperationNoteKind";
	public static String 								ExtraData_OperationPreID	=	"com.main.ExtraData_OperationPreID";
	
	public static String 								ExtraData_RemindSetting	=	"com.main.ExtraData_RemindSetting";
	
	//皮肤设定和加密设定的Menu
	public static final int 							ITEM0	=	Menu.FIRST;
	public static final int 							ITEM1	=	Menu.FIRST + 1;
	
	//进行DB操作的类
	private CNoteDBCtrl 								m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
	
	//进行时间操作的类
//	private Calendar clCalendar	=	Calendar.getInstance();

	private int 												m_ExtraData_EditNoteID 		=	CMemoInfo.Id_Invalid;
	private int 												m_ExtraData_PreID 		=	CMemoInfo.Id_Invalid;
	private OperationNoteKindEnum 								m_ExtraData_OperationNoteKind = null;
	
	CRemindInfo													m_clCRemindInfo	=	null;
	private boolean 											sdCardExit;
	private File 												myRecAudioFile = null;
	private File 												myRecAudioDir = null;
	private File												SdCardDir;
	private MediaRecorder 										mMediaRecorder01;
	

	ImageButton													clBT_Rec_BackGroud;  
	ImageButton													clBTPlayRecord;
	ImageButton													clBTDeleteRecord;
	ImageButton													clBTRecord;
	ImageButton													clBTStartRecord;
	ImageButton													clBTStopRecord ;
    
	private TextView	 										mchronometer;
	protected static final int 									GUI_STOP_NOTIFIER = 0x108;
	protected static final int 									GUI_THREADING_NOTIFIER = 0x109;
	private SeekBar		 										mProgressBar01;
//	public int 													intCounter=0;
	
	private  Thread												nThread;
	private  Thread												mPlayThread;
	
	/* MediaPlayer对象 */
	public MediaPlayer											mMediaPlayer		= null;
	private int													m_iCurrentVoiceDataDuration = CommonDefine.g_int_Invalid_ID;
	
	private boolean												mIsSoundFileExist   = false;
	private boolean												mIsOpenRecordPanel  = false;
	private boolean 											mIsRecordSound		= false;
	
	///////////////////////onStart////////////////////////////////////////////////
	public void onNewIntent(Intent intent)
	{
		setIntent(intent);
	}
	
//	public void onDestroy()
//	{
//		if(m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New){
//			Intent intent = new Intent(this, RootViewList.class);
//			startActivity(intent);
//		}
//		super.onDestroy();
//	}
	
	public void onResume()
	{
		super.onResume();
		
		UpdateViewData();
    	
	}
	
	////////////////////////////////////////////////////////////////////
    /** Called when the activity is first created. */
	///////////////////////////////onCreateStart///////////////////////////////////////////////////////////////////
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
    	super.onCreate(savedInstanceState);
		setContentView( R.layout.editnote );
		if(m_clCNoteDBCtrl==null){
			m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
			CommonDefine.m_clCNoteDBCtrl = m_clCNoteDBCtrl;
		}
		
		m_clCRemindInfo	=	new	CRemindInfo( (byte) -1 );
    	//点击保存Button，进行新增或更新操作
        ImageButton	clBTSave	=	(ImageButton) findViewById(R.id.editnote_toolbar_save);
        clBTSave.setOnClickListener(this);
        
        //录制语音Button
        clBTRecord	=	(ImageButton) findViewById(R.id.editnote_toolbar_recvoice);
        clBTRecord.setOnClickListener(this);
        
        //开始录制语音
        clBTStartRecord	=	(ImageButton) findViewById(R.id.IMG_B_REC);
        clBTStartRecord.setOnClickListener(this);
        
        //停止录制语音
        clBTStopRecord =	(ImageButton) findViewById(R.id.IMG_B_STOP);
        clBTStopRecord.setOnClickListener(this);

        //播放录制语音
        clBTPlayRecord	=	(ImageButton) findViewById(R.id.IMG_B_PLAY);
        clBTPlayRecord.setOnClickListener(this);
        
        //删除录制语音
        clBTDeleteRecord	=	(ImageButton) findViewById(R.id.IMG_B_DELETE);
        clBTDeleteRecord.setOnClickListener(this);
        
      //设置提醒
        ImageButton clBTSetRemind	=	(ImageButton) findViewById(R.id.editnote_toolbar_setremind);
        clBTSetRemind.setOnClickListener(this);
        
        
        //计时器
		mchronometer = (TextView) findViewById(R.id.rec_time);
		//chronometer.setOnChronometerTickListener(this);
		//chronometer.setFormat("MM::SS/05:00");
			
		//检查是否存在SD卡		
	    sdCardExit = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);   
	    if (sdCardExit)
	    {
	    	SdCardDir = Environment.getExternalStorageDirectory();
	    	String AudioDir = SdCardDir.toString() + CommonDefine.g_strAudioFilePath;   
	        myRecAudioDir = new File(AudioDir);
	        if(!myRecAudioDir.exists())
	        {
	        	myRecAudioDir.mkdir();
	        } 
	    }
	    
	    //进度条
	    mProgressBar01 = (SeekBar)findViewById(R.id.progress_horizontal);
	    mProgressBar01.setIndeterminate(false);
        mProgressBar01.setMax(CommonDefine.g_iMaxRecTime);
        mProgressBar01.setProgress(0);
        
        OnSeekBarChangeListener sbLis=new OnSeekBarChangeListener(){
        	 
    		public void onProgressChanged(SeekBar seekBar, int progress,
    				boolean fromUser) {
    			// TODO Auto-generated method stub  
    			if(mMediaPlayer != null &&  mMediaPlayer.isPlaying())
    			{
	       			 mMediaPlayer.seekTo(mProgressBar01.getProgress());
			   		 int cur_sec  = mMediaPlayer.getCurrentPosition()/1000%60;
					 int cur_min  = mMediaPlayer.getCurrentPosition()/1000160;
					 int total_sec = mMediaPlayer.getDuration()/1000%60;
					 int total_min = mMediaPlayer.getDuration()/1000/60;
					 mchronometer.setText(String.format("%02d:%02d/%02d:%02d", cur_min,cur_sec,  total_min ,total_sec));   				
    				
    			}

    		}
     
    		public void onStartTrackingTouch(SeekBar seekBar) {
    			// TODO Auto-generated method stub
    		}
    		public void onStopTrackingTouch(SeekBar seekBar) {
    			// TODO Auto-generated method stub
    			//mp.seekTo(sb.getProgress());
    			
    			if(mMediaPlayer != null &&  mMediaPlayer.isPlaying())
    			{
	       			 mMediaPlayer.seekTo(mProgressBar01.getProgress());
			   		 int cur_sec  = mMediaPlayer.getCurrentPosition()/1000%60;
					 int cur_min  = mMediaPlayer.getCurrentPosition()/1000160;
					 int total_sec = mMediaPlayer.getDuration()/1000%60;
					 int total_min = mMediaPlayer.getDuration()/1000/60;
					 mchronometer.setText(String.format("%02d:%02d/%02d:%02d", cur_min,cur_sec,  total_min ,total_sec));   				
    				
    			}

    			 //chronometer.setBase(mProgressBar01.getProgress());
    			//SeekBar确定位置后，跳到指定位置
    		}
        };
        
        mProgressBar01.setOnSeekBarChangeListener(sbLis);
		/* 构建MediaPlayer对象 */
		mMediaPlayer		= new MediaPlayer();
    }
    
    
    ///////////////////////////////onCreateEnd/////////////////////////////////////////////////////////
    private void UpdateViewData()
    {
    	/*
    	 * 新建便签
    	 * 		所有内容更新为空
    	 * 编辑便签
    	 * 		根据编辑的便签的DBID，获取便签具有的信息，分别更新时间、语音、文本
    	 * 更新便签
    	 * 		根据用户在提醒时间设定画页设置的提醒信息，更新便签中的时间
    	 */
    	Intent iExtraData = getIntent();
		m_ExtraData_OperationNoteKind	=	(OperationNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_OperationNoteKind);
		
		String strDetail = null;
		String strAudioFileName = null;
		if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Edit )
		{
			m_ExtraData_PreID = iExtraData.getIntExtra(ExtraData_OperationPreID, CommonDefine.g_int_Invalid_ID);
			m_ExtraData_EditNoteID		=	iExtraData.getIntExtra(ExtraData_EditNoteID, CMemoInfo.Id_Invalid );
			
			if( m_ExtraData_EditNoteID != CMemoInfo.Id_Invalid )
			{
			   //根据ID取得该条记录的Cursor
				Cursor	curExtraMemo	=	m_clCNoteDBCtrl.getNoteRec( m_ExtraData_EditNoteID );
			    if ( curExtraMemo.getCount() > 0 )
			    {
			        curExtraMemo.moveToFirst();
			        int	index		=	curExtraMemo.getColumnIndex( CNoteDBCtrl.KEY_type );
			        int TypeValue	=	curExtraMemo.getInt( index );
			        
			        if ( TypeValue==CMemoInfo.Type_Folder)
			        {
			        	//error
			        }
			        else
			        {
	        			//取得Memo中的Text信息
			        	index	=	curExtraMemo.getColumnIndex( CNoteDBCtrl.KEY_detail );
	        	    	strDetail = curExtraMemo.getString( index );
	        	    	
	        	    	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
	            		clCRemindOperator.getRemindInfo(curExtraMemo, m_clCRemindInfo);
	            		
	            		index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_audioDataName);
	            		String temp = curExtraMemo.getString(index);
	            		if((temp!=null) && (!temp.equals(""))){
	            			strAudioFileName = temp;
	            		}
			        }
			    }
			    curExtraMemo.close();
			}
			updateTime(m_clCRemindInfo);
			updateDetail(strDetail);
			updateVoice(strAudioFileName);
			
		}else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New )
		{
			m_ExtraData_PreID = iExtraData.getIntExtra(ExtraData_OperationPreID, CommonDefine.g_int_Invalid_ID);
			//将时间设置为空
			updateTime(null);
			//将文本设置为空
			updateDetail(null);
			//将语音设置为空
			updateVoice(null);
		}else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Update )
		{
			CRemindInfo temp =	( CRemindInfo )iExtraData.getSerializableExtra( ExtraData_RemindSetting );
			m_clCRemindInfo = temp;
			updateTime(temp);
		}
		ScrollView etNoteText = (ScrollView)findViewById(R.id.editnote_noteinfo_scroll);
		etNoteText.scrollBy(0, -100);
	}
    private void updateVoice(String strAudioFileName){
    	if(strAudioFileName!=null && sdCardExit ){
    		myRecAudioFile = new File(myRecAudioDir, strAudioFileName);
    		openVoicePanel();
    	}else{
    		
    	}
    	
    }
    private void updateTime(CRemindInfo clRemindInfo){
    	
    	TextView	Week[]	=	new	TextView[ 7 ];
    	TextView	Time	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx0);
    	Week[0]	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx1);
    	Week[1]	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx2);
    	Week[2]	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx3);
    	Week[3]	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx4);
    	Week[4]	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx5);
    	Week[5]	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx6);
    	Week[6]	=	(TextView)findViewById(R.id.editnote_remindinfo_remindsettingdata_tx7);
    	TextView	Type	=	(TextView)findViewById(R.id.editnote_remindinfo_type);
    	
    	TextView	Status	=	(TextView)findViewById(R.id.editnote_remindinfo_status);
    	TextView	CountDownTime	=	(TextView)findViewById(R.id.editnote_remindinfo_nextremind);
    	
    	
    	if(clRemindInfo!=null)
    	{
    		if ( 1 == clRemindInfo.m_iType ||  3 == clRemindInfo.m_iType )
        	{
    			for ( int i = 0; i < 7 ; ++i )
    			{
    				Week[i].setVisibility(View.GONE);
    			}
    			Time.setVisibility(View.VISIBLE);
    			
    			CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
    			clRemindInfo.getNormalTime( clCDateAndTime );
    			Time.setText("提醒时间 : " + String.valueOf(clCDateAndTime.iYear) + "年" + String.valueOf(clCDateAndTime.iMonth+1) + "月"
    						+ String.valueOf(clCDateAndTime.iDay)+ "日" + String.valueOf(clCDateAndTime.iHour) + "小时" 
    						+ String.valueOf(clCDateAndTime.iMinute) + "分");
    			
    		if ( 1 == clRemindInfo.m_iType )
        	{
    				Type.setText("提醒类型 : 倒计时" );
    			}
    			else
    			{
    				Type.setText("提醒类型 : 单次" );
    			}
        		
        	}
        	else if( 2 == clRemindInfo.m_iType )
        	{
    			for ( int i = 0; i < 7 ; ++i )
    			{
    				Week[i].setVisibility(View.VISIBLE);
    				if( 1 == clRemindInfo.m_Week[i])
    				{
    					Week[i].setTextColor(Color.GREEN);
    				}
    				else
    				{
    					Week[i].setTextColor(Color.WHITE);
    				}
    			}
    			Time.setVisibility(View.GONE); 
    			
    			Type.setText("提醒类型 : 循环" );
        		
        	}
        	else
        	{
        		//error
        	}
    		
    		
    		String	strTemp	=	clRemindInfo.getCountDownBySting();
    		if ( null == strTemp ) 
    		{
    			Status.setText("停用");
    			CountDownTime.setText( "下次提醒 : 已过期 ");
    		}
    		else
    		{
    			Status.setText("启用");
    			CountDownTime.setText( strTemp );
    			clRemindInfo.m_iRemindAble	=	0;
    		}
    		
    	}
    }
    private void updateDetail(String strDetail){
    	EditText EtOnce = (EditText) findViewById(R.id.ET_main_Memo);
    	if(strDetail!=null){
    		EtOnce.setText( strDetail );
    	}else{
    		EtOnce.setText( "" );
    	}
    	EtOnce.clearFocus();
    }
    
	public void onClick(View view){
		switch(view.getId()){
		case R.id.editnote_toolbar_save:
			processSaveClick(view);
		    break;
		case R.id.editnote_toolbar_recvoice:
			processRevoiceClick(view);
			break;
		case R.id.IMG_B_REC:
			processRecClick(view);
			break;
		case R.id.IMG_B_STOP:
			processStopClick(view);
			break;
		case R.id.IMG_B_PLAY:
			processPlayClick(view);
			break;
		case R.id.IMG_B_DELETE:
			processDeleteClick(view);
			break;
		case R.id.editnote_toolbar_setremind:
			processSetRemindClick(view);
			break;
		default:
		}
	}
	
	private void processSetRemindClick(View view){
		Intent intent = new Intent();
		intent.setClass(NoteWithYourMind.this, RemindActivity.class);
		if( (m_clCRemindInfo!=null)&&(m_clCRemindInfo.m_iType != -1) )
		{
			intent.putExtra( ExtraData_RemindSetting, m_clCRemindInfo ); 
		}
		startActivity(intent);
	}
	
	/*--------------------------------------------------------------------------------------
	 * 录音和播放的处理 begin
	 *--------------------------------------------------------------------------------------*/
	
	//录音处理，打开录音面板
	private void processRevoiceClick(View view)
	{
		//已经打开，那么关闭录音面板
		if( mIsOpenRecordPanel )
		{
			hideVoicePanel();
		}
		else
		{	
			openVoicePanel();
		}
		
			
	}
	private void hideVoicePanel(){
		View vVoicePanel = findViewById(R.id.editnote_voiceinfo); 
		vVoicePanel.setVisibility(View.GONE);
		mIsOpenRecordPanel = false;
	}
	private void openVoicePanel(){
		//打开录音面板
		View vVoicePanel = findViewById(R.id.editnote_voiceinfo); 
		vVoicePanel.setVisibility(View.VISIBLE);
		
		if( myRecAudioFile!=null && myRecAudioFile.exists())
		{			
			clBTPlayRecord.setEnabled(true);
			clBTDeleteRecord.setEnabled(true);
		}
		else
		{
			clBTPlayRecord.setEnabled(false);
			clBTDeleteRecord.setEnabled(false);
			
		}
		clBTStopRecord.setEnabled(false);
		clBTStartRecord.setEnabled(true);
		
		mIsOpenRecordPanel = true;
	}
	
	//开始录音
	private void processRecClick(View view)
	{		
		if (!sdCardExit)
        {
           Toast.makeText(NoteWithYourMind.this, "没有SDCard",Toast.LENGTH_LONG).show();
           return;
        }
  		 
		 
		if(myRecAudioFile != null)
		{
			//提示用户删除原有录音文件
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("你想删除原有录音文件吗?")
			       .setCancelable(false)
			       .setPositiveButton("是", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   myRecAudioFile.delete();
			        	   myRecAudioFile = null;
			           }
			       })
			       .setNegativeButton("否", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();	
		}
		
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
		}
		
		//录音时只有暂停按钮可控制
		clBTStartRecord.setEnabled(false);
		clBTStopRecord.setEnabled(true);
		clBTPlayRecord.setEnabled(false);
		clBTDeleteRecord.setEnabled(false);
		
		mIsRecordSound = true;

 	    Date d = new Date();
        d.toString();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddkkmmss");
        String filename = sdf.format(d);    	        

        try {
			myRecAudioFile = File.createTempFile(filename, ".3gp", myRecAudioDir);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        mMediaRecorder01 = new MediaRecorder();
        mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            
        mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
     
        try {
			mMediaRecorder01.prepare();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        mMediaRecorder01.start();
                
	    mProgressBar01.setIndeterminate(false);
        mProgressBar01.setMax(CommonDefine.g_iMaxRecTime);
        mProgressBar01.setProgress(0);
                
        nThread = new Thread(new Runnable()
        {
			public void run()
			{
				int mMaxTime = CommonDefine.g_iMaxRecTime;
				for (int i=0;i<mMaxTime+1;i++)
				{
				    try 
				    {
						Thread.sleep(1000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					Message m = new Message();
					m.arg1 = i;
				    if( i==mMaxTime )
				    {
				      m.what = NoteWithYourMind.GUI_STOP_NOTIFIER;
				      NoteWithYourMind.this.myMessageHandler.sendMessage(m);
				      break;
				    }
				    else
				    {
				      m.what = NoteWithYourMind.GUI_THREADING_NOTIFIER;
				      NoteWithYourMind.this.myMessageHandler.sendMessage(m); 
				    }   
				}
			}
        });
        nThread.start();       
	}
	
	//停止录音/暂停播放
	private void processStopClick(View view)
	{
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			//播放状态
			mMediaPlayer.stop();
			//chronometer.stop();
			mPlayThread.interrupt();
		}
		//录音状态
		if (myRecAudioFile != null && mIsRecordSound )
		{  
			mMediaRecorder01.stop();
			mMediaRecorder01.release();
			mMediaRecorder01 = null;
			//chronometer.stop();
			mIsRecordSound = false;	
			nThread.interrupt();
		}		
		clBTStartRecord.setEnabled(true);
		clBTStopRecord.setEnabled(false);
		clBTPlayRecord.setEnabled(true);
		clBTDeleteRecord.setEnabled(true);
		  
	}
	
	//播放录音文件
	private void processPlayClick(View view)
	{
	 	try
		{
	 		if(mMediaPlayer != null)
			{
				/* 重置MediaPlayer */
				mMediaPlayer.reset();
				/* 设置要播放的文件的路径 */
				if(myRecAudioFile!=null && myRecAudioFile.exists())
				{
					mProgressBar01.setFocusable(true);
					//Toast.makeText(NoteWithYourMind.this, myRecAudioFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
					mMediaPlayer.setDataSource(myRecAudioFile.getAbsolutePath()); //"/sdcard/youchaihua.mp3"
					/* 准备播放 */
					mMediaPlayer.prepare();
					/* 开始播放 */
					m_iCurrentVoiceDataDuration = mMediaPlayer.getDuration();
					mMediaPlayer.start();	
					clBTStartRecord.setEnabled(false);
					clBTStopRecord.setEnabled(true);
					clBTPlayRecord.setEnabled(false);
					clBTDeleteRecord.setEnabled(false);
				
			        
				    mProgressBar01.setIndeterminate(false);
				    
				    mProgressBar01.setMax(m_iCurrentVoiceDataDuration);
			        mProgressBar01.setProgress(0);
			                
			        mPlayThread = new PlayWatcherThread(1000);
			        
			        mPlayThread.start();  
				}
			}
				

		}catch (IOException e)
		{
			
		}
		

	}
	
	//删除录音文件
	private void processDeleteClick(View view)
	{
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();			
		}
				
  		if (myRecAudioFile != null && sdCardExit)
  		{
  			if (myRecAudioFile.exists())
  				myRecAudioFile.delete();       
			
			myRecAudioFile = null;
		}
  		hideVoicePanel();
	}
	

	  //处理录音的一些事件，更新UI
	  Handler myMessageHandler = new Handler()
	  {
	    // @Override 
		    public void handleMessage(Message msg)
		    { 
		      switch (msg.what)
		      { 
		        case NoteWithYourMind.GUI_STOP_NOTIFIER:
		          Thread.currentThread().interrupt();
		          clBTStopRecord.performClick();
		          if (myRecAudioFile != null && mIsRecordSound )
		           {  
		           }
		          else
		          {
		        	  mProgressBar01.setProgress(m_iCurrentVoiceDataDuration);
						int total_sec = m_iCurrentVoiceDataDuration/1000%60;
						int total_min = m_iCurrentVoiceDataDuration/1000/60;
						mchronometer.setText(String.format("%02d:%02d/%02d:%02d", total_min,total_sec,  total_min ,total_sec));
		          }
		          break;
		         
		        case NoteWithYourMind.GUI_THREADING_NOTIFIER:
		        	if(!Thread.currentThread().isInterrupted() && mIsRecordSound  )
			         { 
			            mProgressBar01.setProgress(msg.arg1);
		        		 int cur_sec  = msg.arg1%60;
		        		 int cur_min  = msg.arg1/60;
						 mchronometer.setText(String.format("%02d:%02d/%02d:%02d", cur_min,cur_sec,  (CommonDefine.g_iMaxRecTime/60) ,0));
			         }
		        	 if( mMediaPlayer.isPlaying() )
		        	 {
		        		 int a = mMediaPlayer.getCurrentPosition();
//		        		 int b = mMediaPlayer.getDuration();
//		        		 mProgressBar01.setProgress((a*mMaxTime)/b  );	        		 
//		        		 mProgressBar01.setProgress(intCounter);
		        		 mProgressBar01.setProgress(a);
		        		        		 
		        		 int cur_sec  = a/1000%60;
		        		 int cur_min  = a/1000/60;
		        		 int total_sec = m_iCurrentVoiceDataDuration/1000%60;
		        		 int total_min = m_iCurrentVoiceDataDuration/1000/60;
						 mchronometer.setText(String.format("%02d:%02d/%02d:%02d", cur_min,cur_sec,  total_min ,total_sec));
		        	 }
		        	
		          break;
		      } 
		      super.handleMessage(msg); 
		    }
	  };
	  	
	/*--------------------------------------------------------------------------------------
	 * 录音和播放的处理 end
	 *--------------------------------------------------------------------------------------*/
	
	private void processSaveClick(View view){
		//取得Memo信息
		EditText memotext = (EditText) findViewById(R.id.ET_main_Memo);
		String strMemoText = memotext.getText().toString();
		
		//取得提醒信息 - zhu.t : 提醒信息已经保存在 m_clCRemindInfo中
		if( null != m_clCRemindInfo )
		{
			if(  -1 != m_clCRemindInfo.m_iType && !m_clCRemindInfo.checkTime())
			{
        		Toast toast = Toast.makeText(NoteWithYourMind.this, "提醒时间设定错误,请重新设定!", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();
        		return;
			}
		}
		     		
		if( strMemoText.length()>0 )
		{
			//保存用户设定的Memo和提醒信息
			if ( 0 == SaveEditData( strMemoText ) )
			{
        		Toast toast = Toast.makeText(NoteWithYourMind.this, "保存成功", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();
        		NoteWithYourMind.this.finish();
			}
    		
		}
		//无输入信息
		else
		{
			Toast toast = Toast.makeText(NoteWithYourMind.this, "请输入内容", Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
    		toast.show();
		}
	}
    
    private int SaveEditData(String strMemoText)
    {
    	/*
    	 如果时间有效
    	 	保存时间
    	 如果语音有效
    	 	保存语音
    	 如果文本有效
    	 	保存文本
    	*/
    	CMemoInfo		clCMemoInfo	=	new	CMemoInfo();
    	
    	Calendar		clCalendar	=	Calendar.getInstance();
		clCMemoInfo.dLastModifyTime =	clCalendar.getTimeInMillis();
		clCMemoInfo.strDetail		=	strMemoText;
		
    	if ( (m_clCRemindInfo!=null) && (-1 != m_clCRemindInfo.m_iType) )
    	{
    		clCMemoInfo.iIsRemind		=	CMemoInfo.IsRemind_Yes;
    		clCMemoInfo.iIsRemindAble	=	1;
    	}
    	else
    	{
    		clCMemoInfo.iIsRemind		=	CMemoInfo.IsRemind_No;
    		clCMemoInfo.iIsRemindAble	=	0;
    	}

		if((myRecAudioFile!=null) && (myRecAudioFile.isFile())){
			clCMemoInfo.iIsHaveAudioData = CMemoInfo.IsHaveAudioData_Yes;
			clCMemoInfo.strAudioFileName = myRecAudioFile.getName();
		}
    	
    	if ( m_ExtraData_PreID != CMemoInfo.Id_Invalid )
		{
    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId			=	m_ExtraData_PreID;
        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
        	
        	//保存提醒信息 - zhu.t
        	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
        	if( m_clCRemindInfo != null && -1 != m_clCRemindInfo.m_iType ){
        		if ( -1 == clCRemindOperator.addRemind( this, _id, m_clCRemindInfo) )
				{
					//设置提醒失败
        			return	-1;
				}				
        	}
		}
    	else if ( m_ExtraData_EditNoteID != CMemoInfo.Id_Invalid )
		{
			//编辑Memo
			m_clCNoteDBCtrl.Update(m_ExtraData_EditNoteID,clCMemoInfo );
			//判断是否需要更新提醒信息 - zhu.t
			CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
			if ( m_clCRemindInfo!= null )
			{
				if ( -1 == clCRemindOperator.editRemind(this, m_ExtraData_EditNoteID, m_clCRemindInfo ) )
				{
					//设置提醒失败
        			return	-1;				
				}
			}
        }
    	
    	return 0;
    }

	 /*
	 * menu.findItem(EXIT_ID);找到特定的MenuItem
	 * MenuItem.setIcon.可以设置menu按钮的背景
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu_mian_setting, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0: 
				/*   menu button push action */ 
				
			break;
		case ITEM1: 
				/*   menu button push action */ 
			//EncodeSettingDlg();
			break;

		}
		return super.onOptionsItemSelected(item);
	}
	class PlayWatcherThread extends Thread {  
	    int milliseconds;  
	      
	    public PlayWatcherThread(int i){  
	        milliseconds = i;  
	    }  
		public void run()
		{
			setPriority(MIN_PRIORITY);
			for (int i=0;i<m_iCurrentVoiceDataDuration+1;i++)
			{
			    try 
			    {
					sleep(milliseconds);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Message m = new Message();
				m.arg1 = i;
			    if( i==m_iCurrentVoiceDataDuration  || !mMediaPlayer.isPlaying())
			    {
					m.arg1 = m_iCurrentVoiceDataDuration;
					m.what = NoteWithYourMind.GUI_STOP_NOTIFIER;
					NoteWithYourMind.this.myMessageHandler.sendMessage(m);
					break;
			    }
			    else
			    {
					m.what = NoteWithYourMind.GUI_THREADING_NOTIFIER;
					NoteWithYourMind.this.myMessageHandler.sendMessage(m); 
			    }   
			}
		}
	}  
}
