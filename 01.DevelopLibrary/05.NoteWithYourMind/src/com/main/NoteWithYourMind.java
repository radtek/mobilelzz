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
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
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
	//ǰһ��Activity������
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
	
	//Ƥ���趨�ͼ����趨��Menu
	public static final int 							ITEM0	=	Menu.FIRST;
	public static final int 							ITEM1	=	Menu.FIRST + 1;
	
	public static final int								mMaxTime = 120;
	
	//����DB��������
	private CNoteDBCtrl 								m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
	
	//����ʱ���������
//	private Calendar clCalendar	=	Calendar.getInstance();

	private int 												m_ExtraData_EditNoteID 		=	CMemoInfo.Id_Invalid;
	private int 												m_ExtraData_PreID 		=	CMemoInfo.Id_Invalid;
	private OperationNoteKindEnum 								m_ExtraData_OperationNoteKind = null;
	
	CRemindInfo													m_clCRemindInfo	=	new	CRemindInfo( (byte) -1 );
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
    
	private Chronometer 										chronometer;
	protected static final int 									GUI_STOP_NOTIFIER = 0x108;
	protected static final int 									GUI_THREADING_NOTIFIER = 0x109;
	private ProgressBar 										mProgressBar01;
	public int 													intCounter=0;
	
	private  Thread												nThread;
	
	/* MediaPlayer���� */
	public MediaPlayer											mMediaPlayer		= null;
	
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
    	//�������Button��������������²���
        ImageButton	clBTSave	=	(ImageButton) findViewById(R.id.editnote_toolbar_save);
        clBTSave.setOnClickListener(this);
        
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
        
        
        //��ʱ��
		chronometer = (Chronometer) findViewById(R.id.rec_time);
		//chronometer.setOnChronometerTickListener(this);
		//chronometer.setFormat("%s","MM:SS");
			
		//����Ƿ����SD��		
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
		
		//�������¼���ļ� ��¼�����
		if(mIsSoundFileExist)
		{
			clBTStartRecord.setVisibility(View.VISIBLE);
			clBTStopRecord.setVisibility(View.VISIBLE);
			clBTPlayRecord.setVisibility(View.VISIBLE);
			clBTDeleteRecord.setVisibility(View.VISIBLE);
			mIsOpenRecordPanel = true;
		}
	    
	    //������
	    mProgressBar01 = (ProgressBar)findViewById(R.id.progress_horizontal);
	    mProgressBar01.setIndeterminate(false);
        mProgressBar01.setMax(mMaxTime);
        mProgressBar01.setProgress(0);
        
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
			//��ʱ������Ϊ��
			updateTime(null);
			//���ı�����Ϊ��
			updateDetail(null);
			//����������Ϊ��
			updateVoice(null);
		}else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Update )
		{
			CRemindInfo temp =	( CRemindInfo )iExtraData.getSerializableExtra( ExtraData_RemindSetting );
			m_clCRemindInfo = temp;
			updateTime(temp);
		}	
	}
    private void updateVoice(String strAudioFileName){
    	if(strAudioFileName!=null){
    		myRecAudioFile = new File(myRecAudioDir, strAudioFileName);
    		openVoicePanel();
    	}else{
    		
    	}
    	
    }
    private void updateTime(CRemindInfo clRemindInfo){
    	EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
    	if(clRemindInfo!=null){
    		EtOnce.setText( clRemindInfo.getRemindInfoString());
    	}else{
    		EtOnce.setText( "" );
    	}
    }
    private void updateDetail(String strDetail){
    	EditText EtOnce = (EditText) findViewById(R.id.ET_main_Memo);
    	if(strDetail!=null){
    		EtOnce.setText( strDetail );
    	}else{
    		EtOnce.setText( "" );
    	}
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
		if( (m_clCRemindInfo!=null)&&(m_clCRemindInfo.m_bType != -1) )
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
			clBTStartRecord.setVisibility(View.GONE);
			clBTStopRecord.setVisibility(View.GONE);
			clBTPlayRecord.setVisibility(View.GONE);
			clBTDeleteRecord.setVisibility(View.GONE);
			mIsOpenRecordPanel = false;
			

		}
		else
		{	
			openVoicePanel();
		}
		
			
	}
	private void openVoicePanel(){
		//��¼�����
		clBTStartRecord.setVisibility(View.VISIBLE);
		clBTStopRecord.setVisibility(View.VISIBLE);
		clBTStopRecord.setEnabled(false);
		clBTPlayRecord.setVisibility(View.VISIBLE);
		clBTDeleteRecord.setVisibility(View.VISIBLE);
		mIsOpenRecordPanel = true;
	}
	
	//��ʼ¼��
	private void processRecClick(View view)
	{		
		if (!sdCardExit)
        {
           Toast.makeText(NoteWithYourMind.this, "û��SDCard",Toast.LENGTH_LONG).show();
           return;
        }
  		 
		 
		if(myRecAudioFile != null)
		{
			//��ʾ�û�ɾ��ԭ��¼���ļ�
			
		}
		
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.pause();
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
			myRecAudioFile = File.createTempFile(filename, ".amr", myRecAudioDir);
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
        
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
                
        nThread = new Thread(new Runnable()
        {
          public void run()
          {
            for (int i=0;i<mMaxTime+1;i++)
            {
                intCounter = i;
                try 
                {
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
                if( i==mMaxTime )
                {
                  Message m = new Message();
                  m.what = NoteWithYourMind.GUI_STOP_NOTIFIER;
                  NoteWithYourMind.this.myMessageHandler.sendMessage(m);
                  break;
                }
                else
                {
                  Message m = new Message();
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
			mMediaPlayer.pause();
		}
		else
		{
			//¼��״̬
			if (myRecAudioFile != null && mIsRecordSound )
			{  
				mMediaRecorder01.stop();
				mMediaRecorder01.release();
				mMediaRecorder01 = null;
				chronometer.stop();
				mIsRecordSound = false;			
			}		
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
			if(mMediaPlayer != null &&  mMediaPlayer.isPlaying() )
			{
				Toast.makeText(NoteWithYourMind.this, "isPlaying",Toast.LENGTH_SHORT).show();
				mMediaPlayer.start();			
			}
			else
			{
				Toast.makeText(NoteWithYourMind.this, "will Playing",Toast.LENGTH_SHORT).show();
				/* ����MediaPlayer */
				mMediaPlayer.reset();
				/* ����Ҫ���ŵ��ļ���·�� */
				Toast.makeText(NoteWithYourMind.this, myRecAudioFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
				mMediaPlayer.setDataSource(myRecAudioFile.getAbsolutePath());
				/* ׼������ */
				mMediaPlayer.prepare();
				/* ��ʼ���� */
				mMediaPlayer.start();
			}
				
			clBTStartRecord.setEnabled(false);
			clBTStopRecord.setEnabled(true);
			clBTPlayRecord.setEnabled(false);
			clBTDeleteRecord.setEnabled(false);
			
			
		}catch (IOException e)
		{
			
		}
	}
	
	//ɾ��¼���ļ�
	private void processDeleteClick(View view)
	{
		if(mMediaPlayer != null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();			
		}
				
  		if (myRecAudioFile != null)
  		{
  			if (myRecAudioFile.exists())
  				myRecAudioFile.delete();       
			
			myRecAudioFile = null;
		}
		clBTStartRecord.setEnabled(true);
		clBTStopRecord.setEnabled(false);
		clBTPlayRecord.setEnabled(false);
		clBTDeleteRecord.setEnabled(false);
		
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
		         
		          if (myRecAudioFile != null)
		           {  
		              mMediaRecorder01.stop();
		              mMediaRecorder01.release();
		              mMediaRecorder01 = null;
		             
		              clBTStopRecord.setEnabled(false);
		
		              mIsRecordSound = false;
		              
		           }
		           chronometer.stop();
		          break;
		         
		        case NoteWithYourMind.GUI_THREADING_NOTIFIER:
		        	if(!Thread.currentThread().isInterrupted() && mIsRecordSound )
			         {
			            mProgressBar01.setProgress(intCounter);
			         }
		          break;
		      } 
		      super.handleMessage(msg); 
		    }
	  };
	  	
	/*--------------------------------------------------------------------------------------
	 * ¼���Ͳ��ŵĴ��� end
	 *--------------------------------------------------------------------------------------*/
	
	private void processSaveClick(View view){
		//ȡ��Memo��Ϣ
		EditText memotext = (EditText) findViewById(R.id.ET_main_Memo);
		String strMemoText = memotext.getText().toString();
		
		//ȡ��������Ϣ - zhu.t : ������Ϣ�Ѿ������� m_clCRemindInfo��
		     		
		if( strMemoText.length()>0 )
		{
			//�����û��趨��Memo��������Ϣ
			if ( 0 == SaveEditData( strMemoText ) )
			{
        		Toast toast = Toast.makeText(NoteWithYourMind.this, "����ɹ�", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();
        		NoteWithYourMind.this.finish();
			}
    		
		}
		//��������Ϣ
		else
		{
			Toast toast = Toast.makeText(NoteWithYourMind.this, "����������", Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
    		toast.show();
		}
	}
    
    //��������ҳ��EditText������
//    private void UpdateStatusOfMemoInfo( String detail, boolean bIsDisplayRemind )
//    {
//    	EditText etDetail = (EditText)findViewById(R.id.ET_main_Memo);
//    	etDetail.setText( detail );
//    	
//    	if ( bIsDisplayRemind )
//    	{
//    		//��ʾ���ѵ�ʱ������� - zhu.t
//    		if ( m_ExtraData_EditNoteID	!= CMemoInfo.Id_Invalid )
//    		{
//        		EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
//        		
//    		}
//    	}
//    }

    private int SaveEditData(String strMemoText)
    {
    	/*
    	 ���ʱ����Ч
    	 	����ʱ��
    	 ���������Ч
    	 	��������
    	 ����ı���Ч
    	 	�����ı�
    	*/
    	CMemoInfo		clCMemoInfo	=	new	CMemoInfo();
    	
    	Calendar		clCalendar	=	Calendar.getInstance();
		clCMemoInfo.dLastModifyTime =	clCalendar.getTimeInMillis();
		clCMemoInfo.strDetail		=	strMemoText;
		
    	if ( (m_clCRemindInfo!=null) && (-1 != m_clCRemindInfo.m_bType) )
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
    	
//		if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New )
    	if ( m_ExtraData_PreID != CMemoInfo.Id_Invalid )
		{
    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId			=	m_ExtraData_PreID;
        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
        	
        	//����������Ϣ - zhu.t
        	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
        	if(m_clCRemindInfo!=null){
        		if ( -1 == clCRemindOperator.addRemind( this, _id, m_clCRemindInfo) )
				{
					//��������ʧ��
        			return	-1;
				}				
        	}
		}
	//	else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Edit )
    	else if ( m_ExtraData_EditNoteID != CMemoInfo.Id_Invalid )
		{
			//�༭Memo
			m_clCNoteDBCtrl.Update(m_ExtraData_EditNoteID,clCMemoInfo );
			//�ж��Ƿ���Ҫ����������Ϣ - zhu.t
			CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
//				clCRemindOperator.getRemindInfo(m_ExtraData_EditNoteID, m_clCRemindInfo);
//				if ( m_clCRemindInfo.m_bType != -1 )
//				{
//					EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
//		    		EtOnce.setText( m_clCRemindInfo.getRemindInfoString());
//				}

        }
    	
    	return 0;
    }

	 /*
	 * menu.findItem(EXIT_ID);�ҵ��ض���MenuItem
	 * MenuItem.setIcon.��������menu��ť�ı���
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
		return super.onOptionsItemSelected(item);}
}
