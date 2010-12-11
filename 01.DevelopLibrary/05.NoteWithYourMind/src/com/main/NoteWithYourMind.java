package com.main;

/*对于编辑画面

编辑的类型分为：
未知
Memo（编辑已有）
文件夹（编辑已有）

未知
3个CheckBox都可选
all default
保存encode、detail、folder、remind，之后清空所有

Memo
加密和文件夹选项不可选
更新detail、remind
保存detail、remind

文件夹
文件夹和提醒不可选
更新加密、detail
保存加密、detail*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.ImageButton;
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
import android.os.Environment;


public class NoteWithYourMind extends Activity
{
	//前一个Activity的类型
	public	enum	OperationNoteKindEnum
	{
		OperationNoteKind_New,
		OperationNoteKind_Edit,
		OperationNoteKind_Update
	}
	
	public static String ExtraData_EditNoteID		=	"com.main.ExtraData_EditNoteID";
	public static String ExtraData_OperationNoteKind	=	"com.main.ExtraData_OperationNoteKind";
	public static String ExtraData_OperationPreID	=	"com.main.ExtraData_OperationPreID";
	
	public static String ExtraData_RemindSetting	=	"com.main.ExtraData_RemindSetting";
	
	//皮肤设定和加密设定的Menu
	public static final int ITEM0	=	Menu.FIRST;
	public static final int ITEM1	=	Menu.FIRST + 1;
	
	//进行DB操作的类
	private CNoteDBCtrl m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
	
	//进行时间操作的类
//	private Calendar clCalendar	=	Calendar.getInstance();

	private int 			m_ExtraData_EditNoteID 		=	CMemoInfo.Id_Invalid;
	private int 			m_ExtraData_PreID 		=	CMemoInfo.Id_Invalid;
	private OperationNoteKindEnum m_ExtraData_OperationNoteKind = null;
	
	CRemindInfo									m_clCRemindInfo	=	null;
	private boolean 							sdCardExit;
	private File 								myRecAudioFile;
	private File 								myRecAudioDir;
	private MediaRecorder 						mMediaRecorder01;
	private String 								strTempFile = "ex07_11_";
	private boolean 							isStopRecord;

	  
    ImageButton									clBTPlayRecord;
    ImageButton									clBTDeleteRecord;
    ImageButton									clBTRecord;
    ImageButton									clBTStartRecord;
    ImageButton									clBTStopRecord ;
	///////////////////////onStart////////////////////////////////////////////////
//	public void onNewIntent(Intent intent)
//	{
//		setIntent(intent);
//	}
	
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

    public void onCreate( Bundle savedInstanceState )
    {
    	super.onCreate(savedInstanceState);
		setContentView( R.layout.editnote );
		if(m_clCNoteDBCtrl==null){
			m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
			CommonDefine.m_clCNoteDBCtrl = m_clCNoteDBCtrl;
		}
    	//点击保存Button，进行新增或更新操作
        ImageButton	clBTSave	=	(ImageButton) findViewById(R.id.editnote_toolbar_save);
        clBTSave.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//取得Memo信息
        		EditText memotext = (EditText) findViewById(R.id.ET_main_Memo);
        		String strMemoText = memotext.getText().toString();
        		
        		//取得提醒信息 - zhu.t : 提醒信息已经保存在 m_clCRemindInfo中
        		     		
        		if( strMemoText.length()>0 )
        		{
        			//保存用户设定的Memo和提醒信息
        			SaveEditData( strMemoText );       				     		
 //       			UpdateViewStatus();
            		Toast toast = Toast.makeText(NoteWithYourMind.this, "保存成功", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
            		//迁移画页- zhu.t	-	未实现
            		NoteWithYourMind.this.finish();
            		
        		}
        		//无输入信息
        		else
        		{
        			Toast toast = Toast.makeText(NoteWithYourMind.this, "请输入内容", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
        		}
        	}
        });
        
        //录制语音Button
        clBTRecord	=	(ImageButton) findViewById(R.id.editnote_toolbar_recvoice);
        clBTRecord.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{	
        		clBTStartRecord.setVisibility(View.VISIBLE);
        		clBTStopRecord.setVisibility(View.VISIBLE);
        		clBTPlayRecord.setVisibility(View.VISIBLE);
        		clBTDeleteRecord.setVisibility(View.VISIBLE);	
        	}
        });
        
        //开始录制语音
        clBTStartRecord	=	(ImageButton) findViewById(R.id.IMG_B_REC);
        clBTStartRecord.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		try
                {
                  if (!sdCardExit)
                  {
                    Toast.makeText(NoteWithYourMind.this, "没有SDCard",Toast.LENGTH_LONG).show();
                    return;
                  }

                  myRecAudioFile = File.createTempFile(strTempFile, ".amr", myRecAudioDir);

                  mMediaRecorder01 = new MediaRecorder();
                  mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
                  mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                  mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                      
                  mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());
                  mMediaRecorder01.prepare();
                  mMediaRecorder01.start();

                  clBTStopRecord.setEnabled(true);
                  clBTPlayRecord.setEnabled(false);
                  clBTDeleteRecord.setEnabled(false);

                  isStopRecord = false;

                } catch (IOException e)
                {
                  e.printStackTrace();
                }
        	}
        });
        
        //停止录制语音
        clBTStopRecord =	(ImageButton) findViewById(R.id.IMG_B_STOP);
        clBTStopRecord.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		if (myRecAudioFile != null)
                {  
                  mMediaRecorder01.stop();
                  mMediaRecorder01.release();
                  mMediaRecorder01 = null;
                 
                  clBTStopRecord.setEnabled(false);

                  isStopRecord = true;
                }
        	}
        });

        //播放录制语音
        clBTPlayRecord	=	(ImageButton) findViewById(R.id.IMG_B_PLAY);
        clBTPlayRecord.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
                //if (myPlayFile != null && myPlayFile.exists())
                //{
                //  openFile(myPlayFile);
               // }       		
        	}
        });
        
        //删除录制语音
        clBTDeleteRecord	=	(ImageButton) findViewById(R.id.IMG_B_DELETE);
        clBTDeleteRecord.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//if (myPlayFile != null)
                //{
                 // adapter.remove(myPlayFile.getName());
                 
                 // if (myPlayFile.exists())
                  //  myPlayFile.delete();
                  //myTextView1.setText("ЧΘ埃");
                //}
        		
        	}
        });

        

        //点击提醒设置Edit迁移至提醒设置画页Activity - zhu.t
        ((EditText)findViewById(R.id.CB_main_IsWarning)).setOnClickListener(new View.OnClickListener()
        {			
			
			public void onClick(View v) 
			{
				Intent intent = new Intent();
				intent.setClass(NoteWithYourMind.this, RemindSettingActivity.class);
				if( (m_clCRemindInfo!=null)&&(m_clCRemindInfo.m_bType != -1) )
				{
					intent.putExtra( ExtraData_RemindSetting, m_clCRemindInfo ); 
				}
				startActivity(intent);
			}
        });
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
    	m_ExtraData_PreID = iExtraData.getIntExtra(ExtraData_OperationPreID, CommonDefine.g_int_Invalid_ID);
		m_ExtraData_OperationNoteKind	=	(OperationNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_OperationNoteKind);
		if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Edit )
		{
			m_ExtraData_EditNoteID		=	iExtraData.getIntExtra(ExtraData_EditNoteID, CMemoInfo.Id_Invalid );
			String strDetail = null;
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
			        }
			    }
			    curExtraMemo.close();
			}
			updateTime(m_clCRemindInfo);
			updateDetail(strDetail);
			updateVoice();
			
		}else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New )
		{
			//将时间设置为空
			updateTime(null);
			//将文本设置为空
			updateDetail(null);
			//将语音设置为空
			updateVoice();
		}else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Update )
		{
			CRemindInfo temp =	( CRemindInfo )iExtraData.getSerializableExtra( ExtraData_RemindSetting );
			m_clCRemindInfo = temp;
			updateTime(temp);
		}	
	}
    private void updateVoice(){
    	
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
    
    //设置主画页中EditText的内容
//    private void UpdateStatusOfMemoInfo( String detail, boolean bIsDisplayRemind )
//    {
//    	EditText etDetail = (EditText)findViewById(R.id.ET_main_Memo);
//    	etDetail.setText( detail );
//    	
//    	if ( bIsDisplayRemind )
//    	{
//    		//显示提醒的时间和类型 - zhu.t
//    		if ( m_ExtraData_EditNoteID	!= CMemoInfo.Id_Invalid )
//    		{
//        		EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
//        		
//    		}
//    	}
//    }

    private void SaveEditData(String strMemoText)
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
		

		if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New )
		{
    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId			=	m_ExtraData_PreID;
        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
        	
        	//保存提醒信息 - zhu.t
        	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
        	if(m_clCRemindInfo!=null){
        		clCRemindOperator.addRemind( this, _id, m_clCRemindInfo);	
        	}
		}
		else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Edit )

		{
			//编辑Memo
			if( m_ExtraData_EditNoteID != CMemoInfo.Id_Invalid )
			{
				m_clCNoteDBCtrl.Update(m_ExtraData_EditNoteID,clCMemoInfo );
				//判断是否需要更新提醒信息 - zhu.t
				CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
//				clCRemindOperator.getRemindInfo(m_ExtraData_EditNoteID, m_clCRemindInfo);
//				if ( m_clCRemindInfo.m_bType != -1 )
//				{
//					EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
//		    		EtOnce.setText( m_clCRemindInfo.getRemindInfoString());
//				}
			}
			else
			{
				//Error
			}
        }
    }

    private void EncodeSettingDlg()
	{
		if(CommonDefine.g_str_PassWord.equals(new String(""))){
			
				LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
				final View DialogView = factory.inflate(R.layout.dialog_passwordsetting, null);
				
				AlertDialog clDlgNewFolder = new AlertDialog.Builder(NoteWithYourMind.this)	
					.setIcon(R.drawable.clock)
					.setTitle("请设置您的私人密码")
					.setView(DialogView)
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
			        		EditText PassWord = (EditText) DialogView.findViewById(R.id.ET_passwordsetting);
			        		String strPassWord = PassWord.getText().toString();
			        		if(strPassWord.length()>0){
			            		m_clCNoteDBCtrl.setPassWord(strPassWord);
			            		CommonDefine.g_str_PassWord = strPassWord;
			            		PassWord.setText("");
			            		Toast toast = Toast.makeText(NoteWithYourMind.this, "私人密码已设置", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}else{
			        			Toast toast = Toast.makeText(NoteWithYourMind.this, "请输入私人密码", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}
							dialog.cancel();
						}
					})
					.setNegativeButton("取消",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
							dialog.cancel();
						}
					})
					.create();
				clDlgNewFolder.show(); 
		}else{
				LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
				final View DialogView = factory.inflate(R.layout.dialog_passwordchang, null);				
				AlertDialog clDlgNewFolder = new AlertDialog.Builder(NoteWithYourMind.this)	
					.setIcon(R.drawable.clock)
					.setView(DialogView)
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
			        		EditText PassWord_old = (EditText) DialogView.findViewById(R.id.ET_password_old);
			        		String strPassWord_old = PassWord_old.getText().toString();
			        		EditText PassWord_new = (EditText) DialogView.findViewById(R.id.ET_password_new);
			        		String strPassWord_new = PassWord_new.getText().toString();
			        		EditText PassWord_new2 = (EditText) DialogView.findViewById(R.id.ET_password_new2);
			        		String strPassWord_new2 = PassWord_new2.getText().toString();
			        		if(strPassWord_old == CommonDefine.g_str_PassWord){
								if(strPassWord_new.equals(strPassWord_new2)){	            		
				            		m_clCNoteDBCtrl.setPassWord(strPassWord_new);
				            		CommonDefine.g_str_PassWord = strPassWord_new;
									PassWord_old.setText("");
									PassWord_new.setText("");
									PassWord_new2.setText("");
									Toast toast = Toast.makeText(NoteWithYourMind.this, "新密码已设置", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
				            		toast.show();
									dialog.cancel();
								}else{
				        			Toast toast = Toast.makeText(NoteWithYourMind.this, "新密码不一致!请重新输入", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
									PassWord_new.setText("");
									PassWord_new2.setText("");
				            		toast.show();
								}									
			        		}else{
			        			PassWord_old.setText("");
								PassWord_new.setText("");
								PassWord_new2.setText("");
			        			Toast toast = Toast.makeText(NoteWithYourMind.this, "密码错误!请重新输入", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}

						}
					})
					.setNegativeButton("取消",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
							dialog.cancel();
						}
					})
					.create();
				clDlgNewFolder.show(); 
		}
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
			EncodeSettingDlg();
			break;

		}
		return super.onOptionsItemSelected(item);}
}
