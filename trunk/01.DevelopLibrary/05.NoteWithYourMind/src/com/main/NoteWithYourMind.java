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
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;

public class NoteWithYourMind extends Activity {
	public enum NewNoteKindEnum{
		NewNoteKind_Boot,
		NewNoteKind_Intent,
	}
	public static String ExtraData_MemoID = "com.main.ExtraData_MemoID";
	public static String ExtraData_NewNoteKind = "com.main.ExtraData_NewNoteKind";
	private static String m_strPassWord = "123456";
	
	public static final int ITEM0 = Menu.FIRST;
	public static final int ITEM1 = Menu.FIRST + 1;
	
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	
	private Calendar c;

	private int m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
	private NewNoteKindEnum m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Boot;


    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        Button btNewMemo = (Button)findViewById(R.id.B_main_NewMemo);
		Button btNewFolder = (Button)findViewById(R.id.B_main_NewFolder);
		btNewMemo.setVisibility(View.GONE);
		btNewFolder.setVisibility(View.GONE);
		
        m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
        m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Boot;
        Intent iExtraData = this.getIntent();
		m_ExtraData_MemoID = iExtraData.getIntExtra(ExtraData_MemoID, CMemoInfo.Id_Invalid );
		m_ExtraData_NewNoteKind = (NewNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_NewNoteKind);
		if(m_ExtraData_NewNoteKind==null){
			m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Boot;
		}
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        UpdateViewStatus();
        
		/*
		Button clBT = (Button) findViewById(R.id.B_main_Exit);
        clBT.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		NoteWithYourMind.this.finish();
        	}
        });
        */
        c = Calendar.getInstance();
        Button clBTSave = (Button) findViewById(R.id.B_main_Save);
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
        
        Button clBTView = (Button) findViewById(R.id.B_main_View);
        clBTView.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(NoteWithYourMind.this, ViewList.class);
        		startActivity(intent);
        	}
        });
        
        CheckBox clCheckBoxWarning = (CheckBox) findViewById(R.id.CB_main_IsWarning);
        clCheckBoxWarning.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	{
        		if(isChecked)
        		{
        			LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
        			final View DialogView = factory.inflate(R.layout.dateandtime, null);
        			AlertDialog dlg = new AlertDialog.Builder(NoteWithYourMind.this)
        				.setTitle("设置提醒日期")
        				.setView(DialogView)
        				.setPositiveButton("确定",null)
        				.setNegativeButton("取消",null)
        				.create();
        			dlg.show();
        			TimePicker clTP = (TimePicker) DialogView.findViewById(R.id.TimePicker01);
        			clTP.setIs24HourView(true);
        		}
        	}
        });
    }
    private void UpdateViewStatus(){
    	Button btViewList = (Button)findViewById(R.id.B_main_View);
    	if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Boot){
    		btViewList.setVisibility(View.VISIBLE);
    	}else{
    		btViewList.setVisibility(View.GONE);
    	}
    	if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){
    		Cursor curExtraMemo = m_clCNoteDBCtrl.getMemoRec(m_ExtraData_MemoID);
        	startManagingCursor(curExtraMemo);
        	if(curExtraMemo.getCount()>0){
        		curExtraMemo.moveToFirst();
        		int index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_detail);
    	    	String strDetail = curExtraMemo.getString(index);
    	    	index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_isremind);
    	    	int isRemind = curExtraMemo.getInt(index);
    	    	if(isRemind == CMemoInfo.IsRemind_Yes){
    	    		UpdateStatusOfMemoInfo(strDetail,true);
    	    	}else{
    	    		UpdateStatusOfMemoInfo(strDetail,false);
    	    	}
        	}else{
        		UpdateStatusOfMemoInfo("",false);
        	}

    	}else{
    		UpdateStatusOfMemoInfo("",false);
    	}
    	
    }
    private void UpdateStatusOfMemoInfo(String detail, boolean bIsRemind){
    	EditText etDetail = (EditText)findViewById(R.id.ET_main_Memo);
    	CheckBox cbIsRemind = (CheckBox)findViewById(R.id.CB_main_IsWarning);
    	etDetail.setText(detail);
    	cbIsRemind.setChecked(bIsRemind);
    }

    private void SaveEditData(String strMemoText)
    {
    	CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		clCMemoInfo.strDetail	=	strMemoText;
		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();
		if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){	
			m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo);
        }else{
    		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
    		clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;
    		clCMemoInfo.iType = CMemoInfo.Type_Memo;
        	m_clCNoteDBCtrl.Create(clCMemoInfo);
        }
    }

		/*
	 * menu.findItem(EXIT_ID);找到特定的MenuItem
	 * MenuItem.setIcon.可以设置menu按钮的背景
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ITEM0, 0, "皮肤设置");
		menu.add(0, ITEM1, 0, "加密设置");
		menu.findItem(ITEM1);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0: 
				/*   menu button push action */ 


				
			break;
		case ITEM1: 
				/*   menu button push action */ 


			break;

		}
		return super.onOptionsItemSelected(item);}
}
