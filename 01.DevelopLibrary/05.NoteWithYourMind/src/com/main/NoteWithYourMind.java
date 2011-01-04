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
	//ǰһ��Activity������
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
	
	//����DB��������
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
//		Toast.makeText(NoteWithYourMind.this, "���绰��~~~~",Toast.LENGTH_LONG).show();
	}
	
	public void resumeMediaInteract(){
		if(clBTStopRecord.isEnabled()){
    		if(mMediaPlayer != null && m_isMediaPausingForPhoneStateChange){
    			mMediaPlayer.start();
    			mPlayThread.resume();
    			m_isMediaPausingForPhoneStateChange = false;
    		}
    	}
//		Toast.makeText(NoteWithYourMind.this, "�ҵ绰��~~~~",Toast.LENGTH_LONG).show();
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
    	//�������Button��������������²���
		m_SaveBT	=	(ImageButton) findViewById(R.id.editnote_toolbar_save);
		m_SaveBT.setOnClickListener(this);
		m_SaveAs	=	(ImageButton) findViewById(R.id.editnote_toolbar_saveas);
		m_SaveAs.setOnClickListener(this);
        
        //¼������Button
        clBTRecord	=	(ImageButton) findViewById(R.id.editnote_toolbar_recvoice);
        clBTRecord.setOnClickListener(this);
        
        //��ʼ¼������
        clBTStartRecord	=	(ImageButton) findViewById(R.id.IMG_B_REC);
        clBTStartRecord.setOnClickListener(this);
        
        //ֹͣ¼������
        clBTStopRecord =	(ImageButton) findViewById(R.id.IMG_B_STOP);
        clBTStopRecord.setOnClickListener(this);

        //����¼������
        clBTPlayRecord	=	(ImageButton) findViewById(R.id.IMG_B_PLAY);
        clBTPlayRecord.setOnClickListener(this);
        
        //ɾ��¼������
        clBTDeleteRecord	=	(ImageButton) findViewById(R.id.IMG_B_DELETE);
        clBTDeleteRecord.setOnClickListener(this);
        
      //��������
        ImageButton clBTSetRemind	=	(ImageButton) findViewById(R.id.editnote_toolbar_setremind);
        clBTSetRemind.setOnClickListener(this);
        
        //������ͣ������
        clBTAlarm	=	(ImageButton)findViewById(R.id.editnote_remindinfo_alarmIcon);
        clBTAlarm.setOnClickListener(this);
        
        //��ʱ��
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
	    
	    //������
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
    			//SeekBarȷ��λ�ú�����ָ��λ��
    		}
        };
        
        mProgressBar01.setOnSeekBarChangeListener(sbLis);
		/* ����MediaPlayer���� */
		mMediaPlayer		= new MediaPlayer();
    }
    
    
    ///////////////////////////////onCreateEnd/////////////////////////////////////////////////////////
    private void UpdateViewData()
    {
    	/*
    	 * �½���ǩ
    	 * 		�������ݸ���Ϊ��
    	 * �༭��ǩ
    	 * 		���ݱ༭�ı�ǩ��DBID����ȡ��ǩ���е���Ϣ���ֱ����ʱ�䡢�������ı�
    	 * ���±�ǩ
    	 * 		�����û�������ʱ���趨��ҳ���õ�������Ϣ�����±�ǩ�е�ʱ��
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
			   //����IDȡ�ø�����¼��Cursor
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
	        			//ȡ��Memo�е�Text��Ϣ
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
	            		}//����༭��MemoΪ�������趨���ѿ�Ϊ��ʾ
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
			//��ʱ������Ϊ��
			updateTime(null);
			//���ı�����Ϊ��
			updateDetail(null);
			//����������Ϊ��
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
        			Time.setText(String.format( String.format("ÿ��  %02d:%02d", clCDateAndTime.iHour, clCDateAndTime.iMinute) ));
        		}

    			    		
        	}
        	else
        	{
        		//error
        	}
    		
    		if ( CMemoInfo.IsRemind_Able_Yes == clRemindInfo.m_iRemindAble )
    		{
    			Status.setText("����");
    		}
    		else
    		{
    			Status.setText("ͣ��");
    		}
    		
    		String	strTemp	=	clRemindInfo.getCountDownBySting();
    		if ( null == strTemp ) 
    		{
    			CountDownTime.setText( "�ѹ��� ");
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
				Status.setText("ͣ��");
			}
			else
			{
				if( m_clCRemindInfo.checkTime())
				{
					m_clCRemindInfo.m_iRemindAble	=	CMemoInfo.IsRemind_Able_Yes; 
					Status.setText("����");
				}
				else
				{
	        		Toast toast = Toast.makeText(NoteWithYourMind.this, "ʱ���ѹ��ڣ���������ѻ�ҳ�����趨!", Toast.LENGTH_SHORT);
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
	 * ¼���Ͳ��ŵĴ��� begin
	 *--------------------------------------------------------------------------------------*/
	
	//¼��������¼�����
	private void processRevoiceClick(View view)
	{
		//�Ѿ��򿪣���ô�ر�¼�����
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
		//��¼�����
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
	
	//��ʼ¼��
	private void processRecClick(View view)
	{		
		if(myRecAudioFile != null)
		{
			//��ʾ�û�ɾ��ԭ��¼���ļ�
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("����ɾ��ԭ��¼���ļ���?")
			       .setCancelable(false)
			       .setPositiveButton("��", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   myRecAudioFile.delete();
			        	   myRecAudioFile = null;
			        	   RecVoice();
			           }
			       })
			       .setNegativeButton("��", new DialogInterface.OnClickListener() {
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
           Toast.makeText(NoteWithYourMind.this, "û��SDCard",Toast.LENGTH_LONG).show();
           return;
        }
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
		}
		
		//¼��ʱֻ����ͣ��ť�ɿ���
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
	
	//ֹͣ¼��/��ͣ����
	private void processStopClick(View view)
	{
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			//����״̬
			mMediaPlayer.stop();
			//chronometer.stop();
			mPlayThread.interrupt();
		}
		//¼��״̬
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
	
	//����¼���ļ�
	private void processPlayClick(View view)
	{
	 	try
		{
	 		if(mMediaPlayer != null)
			{
				/* ����MediaPlayer */
				mMediaPlayer.reset();
				/* ����Ҫ���ŵ��ļ���·�� */
				if(myRecAudioFile!=null && myRecAudioFile.exists())
				{
					mProgressBar01.setFocusable(true);
					//Toast.makeText(NoteWithYourMind.this, myRecAudioFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
					mMediaPlayer.setDataSource(myRecAudioFile.getAbsolutePath()); //"/sdcard/youchaihua.mp3"
					/* ׼������ */
					mMediaPlayer.prepare();
					/* ��ʼ���� */
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
					Toast.makeText(NoteWithYourMind.this, "�����ļ������� ���� δ����SD��",Toast.LENGTH_LONG).show();
				}
			}
		}catch (IOException e)
		{
			
		}
	}
	
	//ɾ��¼���ļ�
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
	

	  //����¼����һЩ�¼�������UI
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
	 * ¼���Ͳ��ŵĴ��� end
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
	     		Toast toast = Toast.makeText(NoteWithYourMind.this, "ʱ���趨�����������趨!", Toast.LENGTH_SHORT);
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
			.setTitle("��ѡ�� ���浽 ���ļ���")
			.setView(DialogView)
			.setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
					dialog.cancel();
				}
			})
			.create();
		ListView folderList = (ListView) DialogView.findViewById(R.id.folderlist_view);
		if(m_ExtraData_PreID!=CMemoInfo.PreId_Root){
			TextView tvRootFolder = new TextView(this);
			tvRootFolder.setText("��Ŀ¼");
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
			Toast toast = Toast.makeText(this, "��ǰû�п����ƶ������ļ���\n���Ƚ����ļ���", Toast.LENGTH_LONG);
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
    			clNoteInfo.strDetail="����***";
    		}else if(clNoteInfo.iIsHaveAudioData == CMemoInfo.IsHaveAudioData_Yes){
    			clNoteInfo.strDetail="����***";
    		}else{
    			Toast toast = Toast.makeText(NoteWithYourMind.this, "����������", Toast.LENGTH_SHORT);
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
        	String strMess = "��δ�������ݣ�";
        	if(bTextChanged){
        		strMess += "�ı� ";
        	}
        	if(bVoiceChanged){
        		strMess += "���� ";
        	}
        	if(bRemindChanged){
        		strMess += "���� ";
        	}
        	if(bTextChanged||bVoiceChanged||bRemindChanged){
        		new AlertDialog.Builder(this)
                //.setIcon(R.drawable.clock)
                .setTitle("������ʾ")
                .setMessage(strMess)
                .setPositiveButton("����",
                 new DialogInterface.OnClickListener()
                {
                  public void onClick(DialogInterface dialog, int whichButton)
                  {
                	  m_SaveBT.performClick();
                  }
                })
                .setNegativeButton("������",new DialogInterface.OnClickListener(){
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

