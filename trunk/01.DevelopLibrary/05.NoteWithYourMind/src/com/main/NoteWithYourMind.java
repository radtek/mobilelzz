package com.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressWarnings("unused")
public class NoteWithYourMind extends Activity 
implements View.OnClickListener, MediaStatusControl, SDCardStatusChangedCtrl
{
	//前一个Activity的类型
	public	enum	OperationNoteKindEnum
	{
		OperationNoteKind_New,
		OperationNoteKind_Edit,
		OperationNoteKind_Update
	}
	
	public static String 										ExtraData_EditNoteID		=	"com.main.ExtraData_EditNoteID";
	public static String 										ExtraData_OperationNoteKind	=	"com.main.ExtraData_OperationNoteKind";
	public static String 										ExtraData_OperationPreID	=	"com.main.ExtraData_OperationPreID";
	
	public static String 										ExtraData_RemindSetting	=	"com.main.ExtraData_RemindSetting";
	
	//进行DB操作的类
	private CNoteDBCtrl 										m_clCNoteDBCtrl = null;

	private int 												m_ExtraData_EditNoteID 		=	CMemoInfo.Id_Invalid;
	private int 												m_ExtraData_PreID 		=	CMemoInfo.Id_Invalid;
	private OperationNoteKindEnum 								m_ExtraData_OperationNoteKind = null;
	
	private CRemindInfo											m_clCRemindInfo	=	null;
	
	private File 												myRecAudioFile = null;
	private MediaRecorder 										mMediaRecorder01;
	private boolean												mIsOpenRecordPanel  = false;
	private boolean 											mIsRecordSound		= false;
	private ImageButton											clBT_Rec_BackGroud;  
	private ImageButton											clBTPlayRecord;
	private ImageButton											clBTDeleteRecord;
	private ImageButton											clBTRecord;
	private ImageButton											clBTStartRecord;
	private ImageButton											clBTStopRecord ;
	private TextView	 										mchronometer;
	protected static final int 									GUI_STOP_NOTIFIER = 0x108;
	protected static final int 									GUI_THREADING_NOTIFIER = 0x109;
	private SeekBar		 										mProgressBar01;
	private  Thread												nThread;
	private  Thread												mPlayThread;
	private MediaPlayer											mMediaPlayer		= null;
	private int													m_iCurrentVoiceDataDuration = CommonDefine.g_int_Invalid_ID;
	private boolean												m_isMediaPausingForPhoneStateChange = false;
	
	private CMemoInfo											m_NoteInfoFromDB = null;
	private ImageButton											m_SaveBT = null;
	private ImageButton											m_SaveAs = null;
	
	private AlertDialog 										m_dlgFolderList = null;
	private ImageButton											clBTAlarm;
	///////////////////////onStart////////////////////////////////////////////////
	public void onNewIntent(Intent intent)
	{
		setIntent(intent);
	}
	
	public void pauseMediaInteract(){
		if(clBTStopRecord.isEnabled()){
    		if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
    			m_isMediaPausingForPhoneStateChange = true;
    			mPlayThread.suspend();
    			mMediaPlayer.pause();
    		}
    	}
//		Toast.makeText(NoteWithYourMind.this, "来电话了~~~~",Toast.LENGTH_LONG).show();
	}
	
	public void resumeMediaInteract(){
		if(clBTStopRecord.isEnabled()){
    		if(mMediaPlayer != null && m_isMediaPausingForPhoneStateChange){
    			mMediaPlayer.start();
    			mPlayThread.resume();
    			m_isMediaPausingForPhoneStateChange = false;
    		}
    	}
//		Toast.makeText(NoteWithYourMind.this, "挂电话了~~~~",Toast.LENGTH_LONG).show();
	}
	public void ChangedAvailable(){
		
	}
	public void ChangedUnAvailable(){
		
	}

	public void onResume()
	{
		super.onResume();
		
		UpdateViewData();
    	
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		CommonDefine.getMediaPhoneCallListener(this).unadvise(this);
		SDCardAccessor.unadvise(this);
	}
	
	////////////////////////////////////////////////////////////////////
    /** Called when the activity is first created. */
	///////////////////////////////onCreateStart///////////////////////////////////////////////////////////////////
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
    	super.onCreate(savedInstanceState);
		setContentView( R.layout.editnote );
		CommonDefine.getMediaPhoneCallListener(this).advise(this);
		SDCardAccessor.advise(this);
		m_clCNoteDBCtrl = CommonDefine.getNoteDBCtrl(this);
		m_NoteInfoFromDB = new CMemoInfo();
//		m_clCRemindInfo	=	new	CRemindInfo( CommonDefine.Remind_Type_Invalid );
    	//点击保存Button，进行新增或更新操作
		m_SaveBT	=	(ImageButton) findViewById(R.id.editnote_toolbar_save);
		m_SaveBT.setOnClickListener(this);
		m_SaveAs	=	(ImageButton) findViewById(R.id.editnote_toolbar_saveas);
		m_SaveAs.setOnClickListener(this);
        
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
        
        //启动或停用提醒
        clBTAlarm	=	(ImageButton)findViewById(R.id.editnote_remindinfo_alarmIcon);
        clBTAlarm.setOnClickListener(this);
        
        //计时器
		mchronometer = (TextView) findViewById(R.id.rec_time);
		//chronometer.setOnChronometerTickListener(this);
		//chronometer.setFormat("MM::SS/05:00");
			
//	    m_bSDCardExit = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);   
//	    if (m_bSDCardExit)
//	    {
//	    	File SdCardDir = Environment.getExternalStorageDirectory();
//	    	String strAppDir = SdCardDir.toString() + CommonDefine.g_strAppFilePath;
//	    	File fAppDir = new File(strAppDir);
//	        if(!fAppDir.exists())
//	        {
//	        	fAppDir.mkdir();
//	        } 
//	        String strAudioDir =  strAppDir+CommonDefine.g_strAudioFilePath;
//	        myRecAudioDir = new File(strAudioDir);
//	        if(!myRecAudioDir.exists()){
//	        	myRecAudioDir.mkdir();
//	        }
//	    }
	    
	    //进度条
	    mProgressBar01 = (SeekBar)findViewById(R.id.progress_horizontal);
	    mProgressBar01.setIndeterminate(false);
        mProgressBar01.setMax(CommonDefine.g_iMaxRecTime);
        mProgressBar01.setProgress(0);
        
        OnSeekBarChangeListener sbLis=new OnSeekBarChangeListener(){
        	 
    		public void onProgressChanged(SeekBar seekBar, int progress,
    				boolean fromUser) {
    			// TODO Auto-generated method stub  
    			//if(mMediaPlayer != null &&  mMediaPlayer.isPlaying())
    			//{
	       		//	 mMediaPlayer.seekTo(mProgressBar01.getProgress());
			   //		 int cur_sec  = mMediaPlayer.getCurrentPosition()/1000%60;
				//	 int cur_min  = mMediaPlayer.getCurrentPosition()/1000160;
				//	 int total_sec = mMediaPlayer.getDuration()/1000%60;
				//	 int total_min = mMediaPlayer.getDuration()/1000/60;
				//	 mchronometer.setText(String.format("%02d:%02d/%02d:%02d", cur_min,cur_sec,  total_min ,total_sec));   				
    				
    			//}

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
	        	    	m_NoteInfoFromDB.strDetail = strDetail;
	        	    	index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_preid);
	        	    	m_ExtraData_PreID = curExtraMemo.getInt(index);
	        	    	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance(this);
	        	    	m_clCRemindInfo	=	new	CRemindInfo( CommonDefine.Remind_Type_Invalid );
	            		if ( CommonDefine.E_FAIL == clCRemindOperator.getRemindInfo(curExtraMemo, m_clCRemindInfo))
	            		{
	            			m_clCRemindInfo	=	null;
	            		}//如果编辑的Memo为提醒则设定提醒框为显示
	            		else if( CMemoInfo.IsRemind_Yes == m_clCRemindInfo.m_iIsRemind )
	            		{
	            			setRemindDisplay(true);
	            		}
	            		else
	            		{
	            			setRemindDisplay(false);
	            			m_clCRemindInfo	=	null;
	            		}
	            		
	            		index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_audioDataName);
	            		String temp = curExtraMemo.getString(index);
	            		if((temp!=null) && (!temp.equals(""))){
	            			m_NoteInfoFromDB.strAudioFileName = temp;
	            			strAudioFileName = temp;
	            		}
			        }
			    }
			    curExtraMemo.close();
			}
			updateTime(m_clCRemindInfo);
			updateDetail(strDetail);
			updateVoice(strAudioFileName);
			
		}
		else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New )
		{
			m_ExtraData_PreID = iExtraData.getIntExtra(ExtraData_OperationPreID, CommonDefine.g_int_Invalid_ID);
			//将时间设置为空
			updateTime(null);
			//将文本设置为空
			updateDetail(null);
			//将语音设置为空
			updateVoice(null);
		}
		else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Update )
		{
			CRemindInfo	clTemp	=	( CRemindInfo )iExtraData.getSerializableExtra( ExtraData_RemindSetting );
			if( null != clTemp && CMemoInfo.IsRemind_Yes == clTemp.m_iIsRemind )
			{
				setRemindDisplay(true);
				m_clCRemindInfo	=	clTemp;
			}
			
			updateTime( clTemp );
		}
		
		ScrollView etNoteText = (ScrollView)findViewById(R.id.editnote_noteinfo_scroll);
		etNoteText.scrollBy(0, -100);
	}
    private void updateVoice(String strAudioFileName){
    	if(strAudioFileName!=null && SDCardAccessor.isSDCardAvailable() ){
    		myRecAudioFile = new File(SDCardAccessor.getAudioDataDir(), strAudioFileName);
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
    	
    	TextView	Status	=	(TextView)findViewById(R.id.editnote_remindinfo_status);
    	TextView	CountDownTime	=	(TextView)findViewById(R.id.editnote_remindinfo_nextremind);
    	
    	
    	if(clRemindInfo!=null)
    	{
    		setRemindDisplay(true);
    		if ( CommonDefine.Remind_Type_CountDown == clRemindInfo.m_iType ||  CommonDefine.Remind_Type_Once == clRemindInfo.m_iType )
        	{
    			for ( int i = 0; i < 7 ; ++i )
    			{
    				Week[i].setVisibility(View.GONE);
    			}
    			Time.setVisibility(View.VISIBLE);
    			
    			CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
    			clRemindInfo.getNormalTime( clCDateAndTime );
//    			Time.setText( String.valueOf(clCDateAndTime.iYear) + "/" + String.valueOf(clCDateAndTime.iMonth+1) + "/"
//    						+ String.valueOf(clCDateAndTime.iDay) + " " + String.valueOf(clCDateAndTime.iHour) + ":" 
//    						+ String.valueOf(clCDateAndTime.iMinute));
                Calendar clCalendar     =     Calendar. getInstance();
                clCalendar.set(Calendar.YEAR, clCDateAndTime.iYear);
                clCalendar.set(Calendar.MONTH, clCDateAndTime.iMonth);
                clCalendar.set(Calendar.DAY_OF_MONTH, clCDateAndTime.iDay);

                
    			Time.setText( String.format("%04d/%02d/%02d %02d:%02d", clCDateAndTime.iYear, clCDateAndTime.iMonth + 1
    						, clCDateAndTime.iDay, clCDateAndTime.iHour, clCDateAndTime.iMinute) + " " + CDateDlg.getDayofWeek(clCalendar) );
	    			   		
	        }
        	else if( CommonDefine.Remind_Type_Week == clRemindInfo.m_iType )
        	{
        		boolean		bIsEveryDay	=	true;
        		
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
    					bIsEveryDay	=	false;
    				}
    			}
        		
    			CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
    			clRemindInfo.getWeekTime( clCDateAndTime, null );
    			
        		if( !bIsEveryDay )
        		{
     //   			Time.setText( String.valueOf(clCDateAndTime.iHour) + ":" + String.valueOf(clCDateAndTime.iMinute));
        			Time.setText( String.format("%02d:%02d", clCDateAndTime.iHour, clCDateAndTime.iMinute));			
        		}
        		else
        		{
        			for ( int i = 0; i < 7 ; ++i )
        			{
        				Week[i].setVisibility(View.GONE);
        			}
        			Time.setText(String.format( String.format("每天  %02d:%02d", clCDateAndTime.iHour, clCDateAndTime.iMinute) ));
        		}

    			    		
        	}
        	else
        	{
        		//error
        	}
    		
    		if ( CMemoInfo.IsRemind_Able_Yes == clRemindInfo.m_iRemindAble )
    		{
    			Status.setText("启用");
    		}
    		else
    		{
    			Status.setText("停用");
    		}
    		
    		String	strTemp	=	clRemindInfo.getCountDownBySting();
    		if ( null == strTemp ) 
    		{
    			CountDownTime.setText( "已过期 ");
    		}
    		else
    		{
    			CountDownTime.setText( strTemp );
    		}
    	}
//    	else{
//    		setRemindDisplay(false);
//    	}
    }
    private void updateDetail(String strDetail){
    	EditText EtOnce = (EditText) findViewById(R.id.ET_main_Memo);
    	if(strDetail!=null){
    		EtOnce.setText( strDetail );
    	}else{
    		EtOnce.setText( "" );
    	}
    	EtOnce.setSelected(false);
    	EtOnce.clearFocus();
    }
    
	public void onClick(View view){
		switch(view.getId()){
		case R.id.editnote_toolbar_save:
			processSaveClick(view);
		    break;
		case R.id.editnote_toolbar_saveas:
			processSaveAsClick(view);
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
		case R.id.editnote_remindinfo_alarmIcon:
			processEnableAlarm( view );
			break;
		default:
		}
	}
	
	private void processEnableAlarm(View view)
	{
		if( null != m_clCRemindInfo )
		{
			TextView	Status	=	(TextView)findViewById(R.id.editnote_remindinfo_status);
			if( m_clCRemindInfo.m_iRemindAble == CMemoInfo.IsRemind_Able_Yes )
			{
				m_clCRemindInfo.m_iRemindAble	=	CMemoInfo.IsRemind_Able_No;
				Status.setText("停用");
			}
			else
			{
				if( m_clCRemindInfo.checkTime())
				{
					m_clCRemindInfo.m_iRemindAble	=	CMemoInfo.IsRemind_Able_Yes; 
					Status.setText("启用");
				}
				else
				{
	        		Toast toast = Toast.makeText(NoteWithYourMind.this, "时间已过期，请进入提醒画页重新设定!", Toast.LENGTH_SHORT);
	        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
	        		toast.show();  	
				}
			}
		}
	}
	
	private void processSetRemindClick(View view){
		Intent intent = new Intent();
		intent.setClass(NoteWithYourMind.this, RemindActivity.class);
		if( ( m_clCRemindInfo != null ) && ( m_clCRemindInfo.m_iType != CommonDefine.Remind_Type_Invalid ) )
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
			        	   RecVoice();
			           }
			       })
			       .setNegativeButton("否", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();	
			alert.show();
		}else{
			RecVoice();	
		}
	}
	
	private void RecVoice(){
		File tempRecAudioDir = SDCardAccessor.getAudioDataDir();
		if (tempRecAudioDir==null)
        {
           Toast.makeText(NoteWithYourMind.this, "没有SDCard",Toast.LENGTH_LONG).show();
           return;
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
			myRecAudioFile = File.createTempFile(filename, ".3gp", tempRecAudioDir);
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
				}else{
					Toast.makeText(NoteWithYourMind.this, "语音文件不存在 或者 未插入SD卡",Toast.LENGTH_LONG).show();
				}
			}
		}catch (IOException e)
		{
			
		}
	}
	
	//删除录音文件
	private void processDeleteClick(View view)
	{
//		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
//		{
//			mMediaPlayer.stop();			
//		}
//				
		myRecAudioFile = null;
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
		        	  mProgressBar01.setProgress(0);
						int total_sec = m_iCurrentVoiceDataDuration/1000%60;
						int total_min = m_iCurrentVoiceDataDuration/1000/60;
						mchronometer.setText(String.format("%02d:%02d/%02d:%02d", 0,0,  total_min ,total_sec));
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
	private boolean CheckText(){
		boolean bRet = false;
		EditText etDetail = (EditText) findViewById(R.id.ET_main_Memo);
		String temp = etDetail.getText().toString();
		if(m_NoteInfoFromDB.strDetail!=null){
			if(!temp.equals(m_NoteInfoFromDB.strDetail)){
				bRet = true;
			}
		}else{
			if(!temp.equals("")){
				bRet = true;
			}
		}
		
		return bRet;
	}
	private boolean CheckVoice(){
		boolean bRet = false;
		if(myRecAudioFile!=null&&m_NoteInfoFromDB.strAudioFileName!=null){
			if(!myRecAudioFile.getName().equals(m_NoteInfoFromDB.strAudioFileName)){
				bRet = true;
			}
		}else if(myRecAudioFile==null&&m_NoteInfoFromDB.strAudioFileName==null){
		}else{
			bRet = true;
			
		}

		return bRet;	
	}
	private boolean CheckRemind(){
		boolean bRet = false;
		
		if( m_ExtraData_EditNoteID != CMemoInfo.Id_Invalid && null != m_clCRemindInfo )
		{
			CRemindInfo _clCRemindInfo	=	new		CRemindInfo( CommonDefine.Remind_Type_Invalid );
			CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance(this);
			if( CommonDefine.S_OK == clCRemindOperator.getRemindInfo( NoteWithYourMind.this, m_ExtraData_EditNoteID, _clCRemindInfo ) )
			{
				if ( !m_clCRemindInfo.IsEql( _clCRemindInfo ))
				{
					bRet	=	true;
				}
			}		
		}
		else if( m_ExtraData_EditNoteID == CMemoInfo.Id_Invalid && null != m_clCRemindInfo )
		{
			bRet	=	true;
		}
		
		return bRet;
	}
	private void FillTextInfo(CMemoInfo clNoteInfo){
		EditText etDetail = (EditText) findViewById(R.id.ET_main_Memo);
		clNoteInfo.strDetail = etDetail.getText().toString();

	}
	private void FillVoiceInfo(CMemoInfo clNoteInfo){
		clNoteInfo.iIsHaveAudioData = myRecAudioFile==null?CMemoInfo.IsHaveAudioData_No:CMemoInfo.IsHaveAudioData_Yes;
		clNoteInfo.strAudioFileName = myRecAudioFile==null?null:myRecAudioFile.getName();

	}
	private void FillRemindInfo(CMemoInfo clNoteInfo){
		if ( null != m_clCRemindInfo )
		{
			clNoteInfo.iIsRemind		=	m_clCRemindInfo.m_iIsRemind;
			clNoteInfo.iIsRemindAble	=	m_clCRemindInfo.m_iRemindAble;
			clNoteInfo.RemindType		=	m_clCRemindInfo.m_iType;
			clNoteInfo.dRemindTime		=	m_clCRemindInfo.m_lTime;
			clNoteInfo.m_Week			=	m_clCRemindInfo.m_Week;	
			clNoteInfo.iRing			=	m_clCRemindInfo.m_iIsRing;
			clNoteInfo.iVibrate			=	m_clCRemindInfo.m_iIsVibrate;
		}
		
	}
	private void processSaveClick(View view){
		if(clBTStopRecord.isEnabled()){
    		clBTStopRecord.performClick();
    	}
		
		if( null != m_clCRemindInfo && m_clCRemindInfo.m_iIsRemind == CMemoInfo.IsRemind_Yes && m_clCRemindInfo.m_iRemindAble == CMemoInfo.IsRemind_Able_Yes)
		{
			if ( !m_clCRemindInfo.checkTime())
			{
	     		Toast toast = Toast.makeText(NoteWithYourMind.this, "时间设定错误，请重新设定!", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();  
        		
        		return;
			}
		}
		
		CMemoInfo clNoteInfo = new CMemoInfo(); 
		FillTextInfo(clNoteInfo);
		FillVoiceInfo(clNoteInfo);
		FillRemindInfo(clNoteInfo);
		int _id = SaveChagedNoteInfo(clNoteInfo);
		CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance(this);
		clCRemindOperator.processRemind(this, _id, m_clCRemindInfo);
		
		if(_id != CommonDefine.E_FAIL){
			if(clNoteInfo.iPreId==CMemoInfo.PreId_Root){
				Intent intent = new Intent();
				intent.putExtra(RootViewList.ExtraData_initListItemDBID, _id);
				intent.setClass(this, RootViewList.class);
				startActivity(intent);
			}else{
				int valueEncode = CommonDefine.g_int_Invalid_ID;
				Cursor cr = m_clCNoteDBCtrl.getNoteRec(clNoteInfo.iPreId);
				if(cr!=null){
					cr.moveToFirst();
					int index = cr.getColumnIndex(CNoteDBCtrl.KEY_isencode);
					valueEncode = cr.getInt(index);
					cr.close();
				}
				if(valueEncode==CMemoInfo.IsEncode_Yes){
					Intent intent = new Intent();
					intent.putExtra(RootViewList.ExtraData_initListItemDBID, clNoteInfo.iPreId);
					intent.setClass(this, RootViewList.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent();
					intent.putExtra(FolderViewList.ExtraData_initListItemDBID, _id);
					intent.putExtra(FolderViewList.ExtraData_FolderDBID, clNoteInfo.iPreId);
					intent.setClass(this, FolderViewList.class);
					startActivity(intent);
				}
			}
			this.finish();	
		}
		
	}
	
	private void processSaveAsClick(View view){
		LayoutInflater factory = LayoutInflater.from(this);
		final View DialogView = factory.inflate(R.layout.folderlist, null);
		
		m_dlgFolderList = new AlertDialog.Builder(this)
			.setTitle("请选择 保存到 的文件夹")
			.setView(DialogView)
			.setNegativeButton("取消",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
					dialog.cancel();
				}
			})
			.create();
		ListView folderList = (ListView) DialogView.findViewById(R.id.folderlist_view);
		if(m_ExtraData_PreID!=CMemoInfo.PreId_Root){
			TextView tvRootFolder = new TextView(this);
			tvRootFolder.setText("根目录");
			tvRootFolder.setPadding(2, 0, 0, 0);
			tvRootFolder.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
			tvRootFolder.setHeight(CommonDefine.g_int_ListItemHeight);
			tvRootFolder.setTextColor(Color.WHITE);       				
			TextPaint tp = tvRootFolder.getPaint(); 
			tp.setFakeBoldText(true); 
			folderList.addHeaderView(tvRootFolder);
		}
		Cursor cursorFolderList	=	m_clCNoteDBCtrl.getMemoFolderInRoot();
		startManagingCursor(cursorFolderList);
		if(cursorFolderList.getCount()>0){
			if(cursorFolderList!=null){
				ListAdapter LA_FolderList = new SimpleCursorAdapter(
						this,
						android.R.layout.simple_list_item_1,
						cursorFolderList,
						new String[]{CNoteDBCtrl.KEY_detail},
						new int[]{android.R.id.text1}
						);
				folderList.setAdapter(LA_FolderList);
				folderList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
						ListAdapter LA = (ListAdapter)arg0.getAdapter();
						long id = LA.getItemId(arg2);
						if(id<0){
							id = 0;
						}
						m_ExtraData_PreID = (int)id;
						m_SaveBT.performClick();
		        		m_dlgFolderList.cancel();

					}
				});
				m_dlgFolderList.show(); 
			} 
		}else{
			Toast toast = Toast.makeText(this, "当前没有可以移动到的文件夹\n请先建立文件夹", Toast.LENGTH_LONG);
    		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
    		toast.show();
		}
	}
    
    private int SaveChagedNoteInfo(CMemoInfo clNoteInfo)
    {
    	clNoteInfo.iId = m_ExtraData_EditNoteID;
    	if(m_ExtraData_PreID!=CommonDefine.g_int_Invalid_ID){
    		clNoteInfo.iPreId			=	m_ExtraData_PreID;	
    	}
    	clNoteInfo.iType			=	CMemoInfo.Type_Memo;
    	if(clNoteInfo.strDetail==null || clNoteInfo.strDetail.equals("")){
    		if(clNoteInfo.iIsRemind == CMemoInfo.IsRemind_Yes){
    			clNoteInfo.strDetail="提醒***";
    		}else if(clNoteInfo.iIsHaveAudioData == CMemoInfo.IsHaveAudioData_Yes){
    			clNoteInfo.strDetail="语音***";
    		}else{
    			Toast toast = Toast.makeText(NoteWithYourMind.this, "请输入内容", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();
        		return CommonDefine.E_FAIL;
    		}
    	}
    	int	_id	=	m_clCNoteDBCtrl.Update(clNoteInfo);
    	m_NoteInfoFromDB = clNoteInfo;
    	
    	return _id;
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
			    if( i==m_iCurrentVoiceDataDuration  || (!mMediaPlayer.isPlaying() && !m_isMediaPausingForPhoneStateChange))
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if(clBTStopRecord.isEnabled()){
        		clBTStopRecord.performClick();
        	}
        	boolean bTextChanged = CheckText();
        	boolean bVoiceChanged = CheckVoice();
        	boolean bRemindChanged = CheckRemind();
        	String strMess = "有未保存内容：";
        	if(bTextChanged){
        		strMess += "文本 ";
        	}
        	if(bVoiceChanged){
        		strMess += "语音 ";
        	}
        	if(bRemindChanged){
        		strMess += "提醒 ";
        	}
        	if(bTextChanged||bVoiceChanged||bRemindChanged){
        		new AlertDialog.Builder(this)
                //.setIcon(R.drawable.clock)
                .setTitle("保存提示")
                .setMessage(strMess)
                .setPositiveButton("保存",
                 new DialogInterface.OnClickListener()
                {
                  public void onClick(DialogInterface dialog, int whichButton)
                  {
                	  m_SaveBT.performClick();
                  }
                })
                .setNegativeButton("不保存",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
						NoteWithYourMind.this.finish();
					}
				})
                .show();
        		return true;
        	}
        } 
        return super.onKeyDown(keyCode, event); 
    }
	
	public	void	setRemindDisplay(boolean bIsDisplay)
	{
		View	SV	=	findViewById(R.id.editnote_remindinfo);
		if(bIsDisplay){
			SV.setVisibility(View.VISIBLE);	
		}else{
			SV.setVisibility(View.GONE);
		}
	}
}

