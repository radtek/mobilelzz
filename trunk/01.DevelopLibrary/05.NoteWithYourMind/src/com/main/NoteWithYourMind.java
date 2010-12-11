package com.main;

/*���ڱ༭����

�༭�����ͷ�Ϊ��
δ֪
Memo���༭���У�
�ļ��У��༭���У�

δ֪
3��CheckBox����ѡ
all default
����encode��detail��folder��remind��֮���������

Memo
���ܺ��ļ���ѡ���ѡ
����detail��remind
����detail��remind

�ļ���
�ļ��к����Ѳ���ѡ
���¼��ܡ�detail
������ܡ�detail*/

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
	//ǰһ��Activity������
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
	
	//Ƥ���趨�ͼ����趨��Menu
	public static final int ITEM0	=	Menu.FIRST;
	public static final int ITEM1	=	Menu.FIRST + 1;
	
	//����DB��������
	private CNoteDBCtrl m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
	
	//����ʱ���������
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
    	//�������Button��������������²���
        ImageButton	clBTSave	=	(ImageButton) findViewById(R.id.editnote_toolbar_save);
        clBTSave.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		//ȡ��Memo��Ϣ
        		EditText memotext = (EditText) findViewById(R.id.ET_main_Memo);
        		String strMemoText = memotext.getText().toString();
        		
        		//ȡ��������Ϣ - zhu.t : ������Ϣ�Ѿ������� m_clCRemindInfo��
        		     		
        		if( strMemoText.length()>0 )
        		{
        			//�����û��趨��Memo��������Ϣ
        			SaveEditData( strMemoText );       				     		
 //       			UpdateViewStatus();
            		Toast toast = Toast.makeText(NoteWithYourMind.this, "����ɹ�", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
            		//Ǩ�ƻ�ҳ- zhu.t	-	δʵ��
            		NoteWithYourMind.this.finish();
            		
        		}
        		//��������Ϣ
        		else
        		{
        			Toast toast = Toast.makeText(NoteWithYourMind.this, "����������", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
        		}
        	}
        });
        
        //¼������Button
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
        
        //��ʼ¼������
        clBTStartRecord	=	(ImageButton) findViewById(R.id.IMG_B_REC);
        clBTStartRecord.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		try
                {
                  if (!sdCardExit)
                  {
                    Toast.makeText(NoteWithYourMind.this, "û��SDCard",Toast.LENGTH_LONG).show();
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
        
        //ֹͣ¼������
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

        //����¼������
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
        
        //ɾ��¼������
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
                  //myTextView1.setText("�����t��");
                //}
        		
        	}
        });

        

        //�����������EditǨ�����������û�ҳActivity - zhu.t
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
    	 * �½���ǩ
    	 * 		�������ݸ���Ϊ��
    	 * �༭��ǩ
    	 * 		���ݱ༭�ı�ǩ��DBID����ȡ��ǩ���е���Ϣ���ֱ����ʱ�䡢�������ı�
    	 * ���±�ǩ
    	 * 		�����û�������ʱ���趨��ҳ���õ�������Ϣ�����±�ǩ�е�ʱ��
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
			        }
			    }
			    curExtraMemo.close();
			}
			updateTime(m_clCRemindInfo);
			updateDetail(strDetail);
			updateVoice();
			
		}else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New )
		{
			//��ʱ������Ϊ��
			updateTime(null);
			//���ı�����Ϊ��
			updateDetail(null);
			//����������Ϊ��
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

    private void SaveEditData(String strMemoText)
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
		

		if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_New )
		{
    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId			=	m_ExtraData_PreID;
        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
        	
        	//����������Ϣ - zhu.t
        	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
        	if(m_clCRemindInfo!=null){
        		clCRemindOperator.addRemind( this, _id, m_clCRemindInfo);	
        	}
		}
		else if ( m_ExtraData_OperationNoteKind == OperationNoteKindEnum.OperationNoteKind_Edit )

		{
			//�༭Memo
			if( m_ExtraData_EditNoteID != CMemoInfo.Id_Invalid )
			{
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
					.setTitle("����������˽������")
					.setView(DialogView)
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
			        		EditText PassWord = (EditText) DialogView.findViewById(R.id.ET_passwordsetting);
			        		String strPassWord = PassWord.getText().toString();
			        		if(strPassWord.length()>0){
			            		m_clCNoteDBCtrl.setPassWord(strPassWord);
			            		CommonDefine.g_str_PassWord = strPassWord;
			            		PassWord.setText("");
			            		Toast toast = Toast.makeText(NoteWithYourMind.this, "˽������������", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}else{
			        			Toast toast = Toast.makeText(NoteWithYourMind.this, "������˽������", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}
							dialog.cancel();
						}
					})
					.setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
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
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
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
									Toast toast = Toast.makeText(NoteWithYourMind.this, "������������", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
				            		toast.show();
									dialog.cancel();
								}else{
				        			Toast toast = Toast.makeText(NoteWithYourMind.this, "�����벻һ��!����������", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
									PassWord_new.setText("");
									PassWord_new2.setText("");
				            		toast.show();
								}									
			        		}else{
			        			PassWord_old.setText("");
								PassWord_new.setText("");
								PassWord_new2.setText("");
			        			Toast toast = Toast.makeText(NoteWithYourMind.this, "�������!����������", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}

						}
					})
					.setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
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
			EncodeSettingDlg();
			break;

		}
		return super.onOptionsItemSelected(item);}
}
