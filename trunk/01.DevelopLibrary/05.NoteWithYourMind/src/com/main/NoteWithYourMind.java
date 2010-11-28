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
	//前一个Activity的类型
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
	
	//皮肤设定和加密设定的Menu
	public static final int ITEM0	=	Menu.FIRST;
	public static final int ITEM1	=	Menu.FIRST + 1;
	
	//进行DB操作的类
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	
	//进行时间操作的类
//	private Calendar clCalendar	=	Calendar.getInstance();

	private int 			m_ExtraData_MemoID 		=	CMemoInfo.Id_Invalid;
	private NewNoteKindEnum m_ExtraData_NewNoteKind	=	NewNoteKindEnum.NewNoteKind_Unknown;
	
	CRemindInfo					m_clCRemindInfo	=	new	CRemindInfo( (byte) -1 );
	///////////////////////onStart////////////////////////////////////////////////
	public void onStart( )
	{
        //调用基类方法	-	zhu.t
		Intent	iExtraData	=	this.getIntent();
    	m_ExtraData_NewNoteKind	=	(NewNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_NewNoteKind);
    	
    	//从提醒画页迁入
    	if ( m_ExtraData_NewNoteKind == NewNoteKindEnum.RemindSetting_Kind )
    	{
    		m_clCRemindInfo	=	( CRemindInfo )iExtraData.getSerializableExtra( ExtraData_RemindSetting );
    		
    		//更新设定的提醒信息
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
        //调用基类方法
    	super.onCreate(savedInstanceState);
       	
    	//初始化为无效
        m_ExtraData_MemoID		=	CMemoInfo.Id_Invalid;
        m_ExtraData_NewNoteKind	=	NewNoteKindEnum.NewNoteKind_Unknown;

        //取得迁移该画面之前的Intent
        Intent	iExtraData	=	this.getIntent();
        
        //取得从前一个画页中传入的数据
		m_ExtraData_MemoID		=	iExtraData.getIntExtra(ExtraData_MemoID, CMemoInfo.Id_Invalid );
		m_ExtraData_NewNoteKind	=	(NewNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_NewNoteKind);
		
		//新建Memo或第一次程序启动做的处理，设置为无效值
		if ( m_ExtraData_NewNoteKind == null )
		{
			m_ExtraData_NewNoteKind	=	NewNoteKindEnum.NewNoteKind_Unknown;
		}
		
		//加载主画页的layout
		setContentView( R.layout.main );
		
		//创建DB操作类
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        
        //设定EditText的内容
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
    	
    	//点击保存Button，进行新增或更新操作
        ImageButton	clBTSave	=	(ImageButton) findViewById(R.id.B_main_Save);
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
        
        //点击Button迁移至RootList画页Activity
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
        
        //点击提醒设置Edit迁移至提醒设置画页Activity - zhu.t
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
       		UpdateStatusOfMemoInfo( "", false  );  //新建便签 不需要设置内容
		}
		
		//如果对一条Memo进行编辑
		else if( (  m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InRoot )
				||( m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InFolder ) )
		{
			//m_ExtraData_MemoID中保存被编辑记录的ID	
			if( m_ExtraData_MemoID != CMemoInfo.Id_Invalid )
			{
			   //根据ID取得该条记录的Cursor
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
	        			//取得Memo中的Text信息
			        	index	=	curExtraMemo.getColumnIndex( CNoteDBCtrl.KEY_detail );
	        	    	String strDetail = curExtraMemo.getString( index );
	        	    	
	        	    	index	=	curExtraMemo.getColumnIndex( CNoteDBCtrl.KEY_isremind );
	        	    	int isRemind = curExtraMemo.getInt(index);
	        	    	
	        	    	 //编辑便签 需要显示旧文字内容在edittext上
	        	    	if ( isRemind == CMemoInfo.IsRemind_Yes )
	        	    	{
	        	    		//显示提醒时间 
	        	    		UpdateStatusOfMemoInfo( strDetail,true );   
	        	    	}
	        	    	else
	        	    	{
	        	    		//不显示提醒时间
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
    
    //设置主画页中EditText的内容
    private void UpdateStatusOfMemoInfo( String detail, boolean bIsDisplayRemind )
    {
    	EditText etDetail = (EditText)findViewById(R.id.ET_main_Memo);
    	etDetail.setText( detail );
    	
    	if ( bIsDisplayRemind )
    	{
    		//显示提醒的时间和类型 - zhu.t
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
			//主画页下新建Memo
			clCMemoInfo.iIsEditEnable	=	CMemoInfo.IsEditEnable_Enable;
    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId			=	CMemoInfo.PreId_Root;
        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
        	
        	//保存提醒信息 - zhu.t
        	CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
        	clCRemindOperator.addRemind(_id, m_clCRemindInfo);

		}
		else if ( ( m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InRoot )
				||( m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InFolder ) )

		{
			//编辑Memo
			if( m_ExtraData_MemoID != CMemoInfo.Id_Invalid )
			{
				m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo );
				//判断是否需要更新提醒信息 - zhu.t
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
			//List画页下新建Memo
			if ( m_ExtraData_MemoID != CMemoInfo.Id_Invalid )
			{
	        	clCMemoInfo.iIsEditEnable	=	CMemoInfo.IsEditEnable_Enable;
	    		clCMemoInfo.iType			=	CMemoInfo.Type_Memo;
	        	clCMemoInfo.iPreId			=	m_ExtraData_MemoID;
	        	clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
	        	clCMemoInfo.dRemindTime		=	clCalendar.getTimeInMillis(); 
	        	long	_id	=	m_clCNoteDBCtrl.Create(clCMemoInfo);
	        	
	        	//保存提醒信息 - zhu.t
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
