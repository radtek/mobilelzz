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


import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.content.DialogInterface;
import android.content.res.Resources;


public class NoteWithYourMind extends Activity {
	public enum NewNoteKindEnum{
		NewNoteKind_Unknown,
		NewNoteKind_InRoot,
		EditNoteKind_InRoot,
		NewNoteKind_InFolder,
		EditNoteKind_InFolder
	}
	
	public static String ExtraData_MemoID = "com.main.ExtraData_MemoID";
	public static String ExtraData_NewNoteKind = "com.main.ExtraData_NewNoteKind";
	
	public static final int ITEM0 = Menu.FIRST;
	public static final int ITEM1 = Menu.FIRST + 1;
	
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	
	private Calendar c	=	Calendar.getInstance();

	private int m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
	private NewNoteKindEnum m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;

	/**判断是都进行提醒设置的flg**/
	private	static	boolean		bIsRemindSetting	=	true;
	
	/**判断AlertDialog中确认或取消的flg**/
	private	static	boolean		bIsCheck			=	false;
	
	/**设置提醒设置为不显示**/
	public	void	setRemindButtonGone()
	{
		Button btEveryday	=	(Button)findViewById(R.id.B_Remind_Everyday);
		Button btOnce		=	(Button)findViewById(R.id.B_Remind_Once);
		Button btOhter		=	(Button)findViewById(R.id.B_Remind_Other);
		
		btEveryday.setVisibility(View.GONE);
		btOnce.setVisibility(View.GONE);
		btOhter.setVisibility(View.GONE);
		
    	ImageButton btViewList	=	(ImageButton)findViewById(R.id.B_main_View);
    	if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Unknown)
    	{
    		btViewList.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		btViewList.setVisibility(View.INVISIBLE);
    	}
    	
    	ImageButton btSave		=	(ImageButton)findViewById(R.id.B_main_Save);
    	btSave.setVisibility(View.VISIBLE);
	}
	
	/**设置提醒设置为不显示**/
	public	void	setRemindButtonVisible()
	{
		Button btEveryday	=	(Button)findViewById(R.id.B_Remind_Everyday);
		Button btOnce		=	(Button)findViewById(R.id.B_Remind_Once);
		Button btOhter		=	(Button)findViewById(R.id.B_Remind_Other);
		
		btEveryday.setVisibility(View.VISIBLE);
		btOnce.setVisibility(View.VISIBLE);
		btOhter.setVisibility(View.VISIBLE);
		
    	ImageButton btViewList	=	(ImageButton)findViewById(R.id.B_main_View);
    	ImageButton btSave		=	(ImageButton)findViewById(R.id.B_main_Save);

    	btViewList.setVisibility(View.GONE);
    	btSave.setVisibility(View.GONE);
	}
	
	public	void	setCurrentDateAndTime( boolean bIsDateDisplay )
	{
		bIsCheck	=	false;
		c.setTimeInMillis(System.currentTimeMillis());
		LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
		final View DialogView = factory.inflate(R.layout.dateandtime, null);
		AlertDialog dlg = new AlertDialog.Builder(NoteWithYourMind.this)
			.setTitle("设置提醒日期")
			.setView(DialogView)
			.setPositiveButton("确定",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					bIsCheck	=	true;


    				EditText EtOnce = (EditText) findViewById(R.id.CB_main_IsWarning);
    				EtOnce.setText(c.get(Calendar.YEAR) +"/" + c.get(Calendar.MONTH) +"/"+c.get(Calendar.HOUR_OF_DAY) + " " 
    								+ c.get(Calendar.HOUR_OF_DAY) + ":" +c.get(Calendar.MINUTE) );

//					finish();
				}} )
			.setNegativeButton("取消",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					bIsCheck	=	false;
					dialog.cancel();
				}} )
			.create();

		dlg.show();
		DatePicker dateOPicker		=	(DatePicker) DialogView.findViewById(R.id.DatePicker01);
		//将日历初始化为当前系统时间，并设置其事件监听
		dateOPicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                //当日期更改时，在这里处理
                c.set( Calendar.YEAR, year);
                c.set( Calendar.MONTH, monthOfYear);
                c.set( Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        });

		if ( bIsDateDisplay )
		{
			dateOPicker.setEnabled(false);
		}
		
		TimePicker clTP = (TimePicker) DialogView.findViewById(R.id.TimePicker01);
		clTP.setIs24HourView(true);   	
		
	       //监听时间改变
		clTP.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
            {
                //时间改变时处理
                c.set( Calendar.HOUR_OF_DAY, hourOfDay);
                c.set( Calendar.MINUTE, minute);
            }
        });

        
	}
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        /**设置Title的部分暂时注释掉**/
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//		setContentView(R.layout.main);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
//        ImageButton btNewMemo = (ImageButton)findViewById(R.id.B_main_NewMemo);
//        ImageButton btNewFolder = (ImageButton)findViewById(R.id.B_main_NewFolder);
//		btNewMemo.setVisibility(View.GONE);
//		btNewFolder.setVisibility(View.GONE);
		
        m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
        m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;
        Intent iExtraData = this.getIntent();
		m_ExtraData_MemoID = iExtraData.getIntExtra(ExtraData_MemoID, CMemoInfo.Id_Invalid );
		m_ExtraData_NewNoteKind = (NewNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_NewNoteKind);
		if(m_ExtraData_NewNoteKind==null){
			m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;
		}
		
		setContentView(R.layout.main);
		int a = CommonDefine.g_test;

//		AlarmManager	alarmManager	=	(AlarmManager)getSystemService( Context.ALARM_SERVICE );
//    	Intent 			MyIntent		=	new Intent( this, AlarmReceiver.class );
//    	
//    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(this, 0,MyIntent, 0);
//    	alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10*1000), 60 * 1000, pendingIntent );
		
//		if(m_ExtraData_NewNoteKind==NewNoteKindEnum.NewNoteKind_Unknown){
//			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//			setContentView(R.layout.main);
//	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
//		}else{
//			setContentView(R.layout.main);
//		}
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        UpdateViewStatus();

    	Cursor curPassWord = m_clCNoteDBCtrl.getPassWord();

		startManagingCursor(curPassWord);
        curPassWord.moveToFirst();
		int count = curPassWord.getCount();
    	if(count > 0 ){	
			int index = curPassWord.getColumnIndex(CNoteDBCtrl.KEY_password);
			CommonDefine.g_str_PassWord = curPassWord.getString(index);					
		}
    	
    	/*取得提醒对象添加监听*/
    	Button btEveryday = (Button) findViewById(R.id.B_Remind_Everyday);
    	btEveryday.setOnClickListener(new Button.OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			setCurrentDateAndTime( true );
    		}
        });
    	
    	Button btOnce = (Button) findViewById(R.id.B_Remind_Once);
    	btOnce.setOnClickListener(new Button.OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			setCurrentDateAndTime( false ); 
    		}
        });
    	
    	Button btOhter = (Button) findViewById(R.id.B_Remind_Other);
    	btOhter.setOnClickListener(new Button.OnClickListener()
    	{
    		public void onClick(View v)
    		{
        		Intent intent = new Intent();
        		intent.setClass(NoteWithYourMind.this, RemindSettingActivity.class);
        		startActivity(intent);
    		}
        });
    	
//		Button clBTSkin = (Button) findViewById(R.id.B_main_setting_skin);
//        clBTSkin.setOnClickListener(new Button.OnClickListener(){
//        	public void onClick(View v)
//        	{
//        		
//        	}
//        });

//		Button clBTEncode = (Button) findViewById(R.id.B_main_setting_encode);
//        clBTEncode.setOnClickListener(new Button.OnClickListener(){
//        	public void onClick(View v)
//        	{
//        		EncodeSettingDlg();
//        	}
//        });

//        c = Calendar.getInstance();
        ImageButton clBTSave = (ImageButton) findViewById(R.id.B_main_Save);
        clBTSave.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		EditText memotext = (EditText) findViewById(R.id.ET_main_Memo);
        		String strMemoText = memotext.getText().toString();
        		if(strMemoText.length()>0){
        			SaveEditData(strMemoText);       				     		
        			UpdateViewStatus();
            		Toast toast = Toast.makeText(NoteWithYourMind.this, "保存成功", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
        		}else{
        			Toast toast = Toast.makeText(NoteWithYourMind.this, "请输入内容", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
        		}
        	}
        });
        
        ImageButton clBTView = (ImageButton) findViewById(R.id.B_main_View);
        clBTView.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(NoteWithYourMind.this, RootViewList.class);
        		startActivity(intent);
        	}
        });
        
        ((EditText)findViewById(R.id.CB_main_IsWarning)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( bIsRemindSetting )
				{
					bIsRemindSetting	=	false;
					setRemindButtonVisible();
					
				}
				else
				{
					bIsRemindSetting	=	true;
					setRemindButtonGone();
				}			
			}
		});

        
//        CheckBox clCheckBoxWarning = (CheckBox) findViewById(R.id.CB_main_IsWarning);
//        clCheckBoxWarning.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
//        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//        	{
//        		if(isChecked)
//        		{
//        			LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
//        			final View DialogView = factory.inflate(R.layout.dateandtime, null);
//        			AlertDialog dlg = new AlertDialog.Builder(NoteWithYourMind.this)
//        				.setTitle("设置提醒日期")
//        				.setView(DialogView)
//        				.setPositiveButton("确定",null)
//        				.setNegativeButton("取消",null)
//        				.create();
//        			dlg.show();
//        			TimePicker clTP = (TimePicker) DialogView.findViewById(R.id.TimePicker01);
//        			clTP.setIs24HourView(true);
//        		}
//        	}
//       });
    }
    private void UpdateViewStatus(){

    	setRemindButtonGone();

		if((m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InRoot)
			||(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InFolder))
		{
       		UpdateStatusOfMemoInfo("",false);  //新建便签 不需要显示任何文字在edittext上

		}
		else if((m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InRoot)
				||(m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InFolder))
		{
				if( m_ExtraData_MemoID != CMemoInfo.Id_Invalid ){
			   			Cursor curExtraMemo = m_clCNoteDBCtrl.getMemoRec(m_ExtraData_MemoID);
			        	startManagingCursor(curExtraMemo);
			        	if(curExtraMemo.getCount()>0){
			        		curExtraMemo.moveToFirst();
			        		int index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_type);
			        		int TypeValue = curExtraMemo.getInt(index);
			        		if(TypeValue==CMemoInfo.Type_Folder){
			        			UpdateStatusOfMemoInfo("",false);
			        		}else{
			        			index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_detail);
			        	    	String strDetail = curExtraMemo.getString(index);
			        	    	index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_isremind);
			        	    	int isRemind = curExtraMemo.getInt(index);
			        	    	if(isRemind == CMemoInfo.IsRemind_Yes){
			        	    		UpdateStatusOfMemoInfo(strDetail,true);    //编辑便签 需要显示旧文字内容在edittext上
			        	    	}else{
			        	    		UpdateStatusOfMemoInfo(strDetail,false);
			        	    	}
			        		}
			        	}else{
			        		UpdateStatusOfMemoInfo("",false);
			        	}

				}
				else{
					UpdateStatusOfMemoInfo("",false);
				}


		}
		else{
       		UpdateStatusOfMemoInfo("",false);
		}
			

	}
    private void UpdateStatusOfMemoInfo(String detail, boolean bIsRemind){
    	EditText etDetail = (EditText)findViewById(R.id.ET_main_Memo);
 //   	CheckBox cbIsRemind = (CheckBox)findViewById(R.id.CB_main_IsWarning);
    	etDetail.setText(detail);
 //   	cbIsRemind.setChecked(bIsRemind);
    }

    private void SaveEditData(String strMemoText)
    {
    	CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();
		clCMemoInfo.strDetail	=	strMemoText;
		

		if((m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InRoot)
			||(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Unknown))
		{
			clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
    		clCMemoInfo.iType = CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;
        	clCMemoInfo.dCreateTime = System.currentTimeMillis();
        	clCMemoInfo.dRemindTime = c.getTimeInMillis(); 
        	m_clCNoteDBCtrl.Create(clCMemoInfo);

		}
		else if((m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InRoot)
				||(m_ExtraData_NewNoteKind == NewNoteKindEnum.EditNoteKind_InFolder))

		{
			if( m_ExtraData_MemoID != CMemoInfo.Id_Invalid ){
				m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo);
			}else{
				//Error
			}
        }
		else if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_InFolder)
		{
			if( m_ExtraData_MemoID != CMemoInfo.Id_Invalid ){
	        	clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
	    		clCMemoInfo.iType = CMemoInfo.Type_Memo;
	        	clCMemoInfo.iPreId	=	m_ExtraData_MemoID;
	        	clCMemoInfo.dCreateTime = System.currentTimeMillis();
	        	clCMemoInfo.dRemindTime = c.getTimeInMillis(); 
	        	m_clCNoteDBCtrl.Create(clCMemoInfo);
			}else{
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
