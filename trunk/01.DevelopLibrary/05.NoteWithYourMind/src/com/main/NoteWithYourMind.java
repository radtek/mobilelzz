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



public class NoteWithYourMind extends Activity
{
	//ǰһ��Activity������
	public	enum	NewNoteKindEnum
	{
		NewNoteKind_Unknown,
		NewNoteKind_InRoot,
		EditNoteKind_InRoot,
		NewNoteKind_InFolder,
		EditNoteKind_InFolder,
		RemindSetting_Kind,
	}
	
	public static String ExtraData_MemoID		=	"com.main.ExtraData_MemoID";
	public static String ExtraData_NewNoteKind	=	"com.main.ExtraData_NewNoteKind";
	
	public static String ExtraData_RemindSetting	=	"com.main.ExtraData_RemindSetting";
	
	//Ƥ���趨�ͼ����趨��Menu
	public static final int ITEM0	=	Menu.FIRST;
	public static final int ITEM1	=	Menu.FIRST + 1;
	
	//����DB��������
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	
	//����ʱ���������
//	private Calendar clCalendar	=	Calendar.getInstance();

	private int 			m_ExtraData_MemoID 		=	CMemoInfo.Id_Invalid;
	private NewNoteKindEnum m_ExtraData_NewNoteKind	=	NewNoteKindEnum.NewNoteKind_Unknown;
	
	CRemindInfo					m_clCRemindInfo	=	new	CRemindInfo( (byte) -1 );
	///////////////////////onStart////////////////////////////////////////////////
	public void onStart( )
	{
        //���û��෽��	-	zhu.t
		Intent	iExtraData	=	this.getIntent();
    	m_ExtraData_NewNoteKind	=	(NewNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_NewNoteKind);
    	
    	//�����ѻ�ҳǨ��
    	if ( m_ExtraData_NewNoteKind == NewNoteKindEnum.RemindSetting_Kind )
    	{
    		m_clCRemindInfo	=	( CRemindInfo )iExtraData.getSerializableExtra( ExtraData_RemindSetting );
    		
    		//�����趨��������Ϣ
    		EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
    		EtOnce.setText( m_clCRemindInfo.getRemindInfoString());
    	}
    	super.onStart();
	}
	
	////////////////////////////////////////////////////////////////////
    /** Called when the activity is first created. */
	///////////////////////////////onCreateStart///////////////////////////////////////////////////////////////////
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        //���û��෽��
    	super.onCreate(savedInstanceState);
       	
    	//��ʼ��Ϊ��Ч
        m_ExtraData_MemoID		=	CMemoInfo.Id_Invalid;
        m_ExtraData_NewNoteKind	=	NewNoteKindEnum.NewNoteKind_Unknown;

        //ȡ��Ǩ�Ƹû���֮ǰ��Intent
        Intent	iExtraData	=	this.getIntent();
        
        //ȡ�ô�ǰһ����ҳ�д��������
		m_ExtraData_MemoID		=	iExtraData.getIntExtra(ExtraData_MemoID, CMemoInfo.Id_Invalid );
		m_ExtraData_NewNoteKind	=	(NewNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_NewNoteKind);
		
		//�½�Memo���һ�γ����������Ĵ�������Ϊ��Чֵ
		if ( m_ExtraData_NewNoteKind == null )
		{
			m_ExtraData_NewNoteKind	=	NewNoteKindEnum.NewNoteKind_Unknown;
		}
		
		//��������ҳ��layout
		setContentView( R.layout.main );
		
		//����DB������
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        
        //�趨EditText������
        UpdateViewStatus();

    	Cursor	curPassWord	=	m_clCNoteDBCtrl.getPassWord();
		startManagingCursor ( curPassWord );
        curPassWord.moveToFirst();
		int count = curPassWord.getCount();
    	if ( count > 0 )
    	{	
			int index = curPassWord.getColumnIndex(CNoteDBCtrl.KEY_password);
			CommonDefine.g_str_PassWord = curPassWord.getString(index);					
		}
    	
    	//�������Button��������������²���
        ImageButton	clBTSave	=	(ImageButton) findViewById(R.id.B_main_Save);
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
        
        //���ButtonǨ����RootList��ҳActivity
        ImageButton	clBTView = (ImageButton) findViewById(R.id.B_main_View);
        clBTView.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(NoteWithYourMind.this, RootViewList.class);
        		startActivity(intent);
        	}
        });
        
        //�����������EditǨ�����������û�ҳActivity - zhu.t
        ((EditText)findViewById(R.id.CB_main_IsWarning)).setOnClickListener(new View.OnClickListener()
        {			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(NoteWithYourMind.this, RemindSettingActivity.class);
				if( m_clCRemindInfo.m_bType != -1 )
				{
					intent.putExtra( ExtraData_RemindSetting, m_clCRemindInfo ); 
				}
				startActivity(intent);
			}
        });
    }
    ///////////////////////////////onCreateEnd/////////////////////////////////////////////////////////
    private void UpdateViewStatus()
    {
		if( (  m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InRoot )
			||(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InFolder) )
		{
       		UpdateStatusOfMemoInfo( "", false  );  //�½���ǩ ����Ҫ��������
		}
		
		//�����һ��Memo���б༭
		else if( (  m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InRoot )
				||( m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InFolder ) )
		{
			//m_ExtraData_MemoID�б��汻�༭��¼��ID	
			if( m_ExtraData_MemoID != CMemoInfo.Id_Invalid )
			{
			   //����IDȡ�ø�����¼��Cursor
				Cursor	curExtraMemo	=	m_clCNoteDBCtrl.getMemoRec( m_ExtraData_MemoID );
			    startManagingCursor( curExtraMemo );
			    if ( curExtraMemo.getCount() > 0 )
			    {
			        curExtraMemo.moveToFirst();
			        int	index		=	curExtraMemo.getColumnIndex( CNoteDBCtrl.KEY_type );
			        int TypeValue	=	curExtraMemo.getInt( index );
			        
			        if ( TypeValue==CMemoInfo.Type_Folder)
			        {
			        	UpdateStatusOfMemoInfo( "", false );
			        }
			        else
			        {
	        			//ȡ��Memo�е�Text��Ϣ
			        	index	=	curExtraMemo.getColumnIndex( CNoteDBCtrl.KEY_detail );
	        	    	String strDetail = curExtraMemo.getString( index );
	        	    	
	        	    	index	=	curExtraMemo.getColumnIndex( CNoteDBCtrl.KEY_isremind );
	        	    	int isRemind = curExtraMemo.getInt(index);
	        	    	
	        	    	 //�༭��ǩ ��Ҫ��ʾ������������edittext��
	        	    	if ( isRemind == CMemoInfo.IsRemind_Yes )
	        	    	{
	        	    		//��ʾ����ʱ�� 
	        	    		UpdateStatusOfMemoInfo( strDetail,true );   
	        	    	}
	        	    	else
	        	    	{
	        	    		//����ʾ����ʱ��
	        	    		UpdateStatusOfMemoInfo( strDetail,false );
	        	    	}
			        }
			    }
			    else
			    {
			        UpdateStatusOfMemoInfo("",false);
			    }
			}
			else
			{
				UpdateStatusOfMemoInfo("",false);
			}
		}
		else
		{
       		UpdateStatusOfMemoInfo("",false);
		}
	}
    
    //��������ҳ��EditText������
    private void UpdateStatusOfMemoInfo( String detail, boolean bIsDisplayRemind )
    {
    	EditText etDetail = (EditText)findViewById(R.id.ET_main_Memo);
    	etDetail.setText( detail );
    	
    	if ( bIsDisplayRemind )
    	{
    		//��ʾ���ѵ�ʱ������� - zhu.t
    		if ( m_ExtraData_MemoID	!= CMemoInfo.Id_Invalid )
    		{
        		EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
        		CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
        		clCRemindOperator.getRemindInfo(m_ExtraData_MemoID, m_clCRemindInfo);
        		EtOnce.setText( m_clCRemindInfo.getRemindInfoString());
    		}
    	}
    }

    private void SaveEditData(String strMemoText)
    {
    	CMemoInfo		clCMemoInfo	=	new	CMemoInfo();
    	
    	Calendar		clCalendar	=	Calendar.getInstance();
		clCMemoInfo.dLastModifyTime =	clCalendar.getTimeInMillis();
		clCMemoInfo.strDetail		=	strMemoText;
		

		if ( (  m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InRoot )
			||( m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Unknown ) )
		{
			//����ҳ���½�Memo
			clCMemoInfo.iIsEditEnable	=	CMemoInfo.IsEditEnable_Enable;
    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId			=	CMemoInfo.PreId_Root;
        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
        	
        	//����������Ϣ - zhu.t
        	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
        	clCRemindOperator.addRemind(_id, m_clCRemindInfo);

		}
		else if ( ( m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InRoot )
				||( m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InFolder ) )

		{
			//�༭Memo
			if( m_ExtraData_MemoID != CMemoInfo.Id_Invalid )
			{
				m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo );
				//�ж��Ƿ���Ҫ����������Ϣ - zhu.t
				CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
				clCRemindOperator.getRemindInfo(m_ExtraData_MemoID, m_clCRemindInfo);
				if ( m_clCRemindInfo.m_bType != -1 )
				{
					EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
		    		EtOnce.setText( m_clCRemindInfo.getRemindInfoString());
				}
			}
			else
			{
				//Error
			}
        }
		else if ( m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InFolder )
		{
			//List��ҳ���½�Memo
			if ( m_ExtraData_MemoID != CMemoInfo.Id_Invalid )
			{
	        	clCMemoInfo.iIsEditEnable	=	CMemoInfo.IsEditEnable_Enable;
	    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
	        	clCMemoInfo.iPreId			=	m_ExtraData_MemoID;
	        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
	        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
	        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
	        	
	        	//����������Ϣ - zhu.t
	        	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
	        	clCRemindOperator.addRemind(_id, m_clCRemindInfo);
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
