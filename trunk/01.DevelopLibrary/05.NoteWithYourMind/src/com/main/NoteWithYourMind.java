package com.main;


import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

public class NoteWithYourMind extends Activity {
	public static String ExtraData_MemoID = "com.main.ExtraData_MemoID";
	private CheckBox m_clCheckBoxWarning;
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	private Calendar c;
	private static String m_strPassWord = "123456";
	private int m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
        Intent iExtraData = this.getIntent();
		m_ExtraData_MemoID = iExtraData.getIntExtra(ExtraData_MemoID, CMemoInfo.Id_Invalid );
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        UpdateViewStatus();
        
        Button clBT = (Button) findViewById(R.id.B_main_Exit);
        clBT.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		NoteWithYourMind.this.finish();
        	}
        });
        c = Calendar.getInstance();
        Button clBTSave = (Button) findViewById(R.id.B_main_Save);
        clBTSave.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		EditText memotext = (EditText) findViewById(R.id.ET_main_Memo);
        		String strMemoText = memotext.getText().toString();
        		if(strMemoText.length()>0){
        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
        			clCMemoInfo.strDetail	=	strMemoText;
            		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();
            		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
            		CheckBox CBEncode = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsEncode);
            		if(CBEncode.isChecked()){
            			clCMemoInfo.strPassword = m_strPassWord;
            		}else{
            			clCMemoInfo.strPassword = "";
            		}
            		CheckBox CBFolder = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsFolder);
            		if(CBFolder.isChecked()){
            			clCMemoInfo.iType = CMemoInfo.Type_Folder;
            		}else{
            			clCMemoInfo.iType = CMemoInfo.Type_Memo;
            		}
        			if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){
        				Cursor cur = m_clCNoteDBCtrl.getMemoRec(m_ExtraData_MemoID);
        				cur.moveToFirst();
        				int index = cur.getColumnIndex(CNoteDBCtrl.KEY_type);
        				int value = cur.getInt(index);
        				if(value == CMemoInfo.Type_Folder){
        					clCMemoInfo.iPreId	=	m_ExtraData_MemoID;
        					m_clCNoteDBCtrl.Create(clCMemoInfo);
        				}else{
        					m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo);
        				}     				
        	        }else{       	        	
                		clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;                		
                		m_clCNoteDBCtrl.Create(clCMemoInfo);
        	        }      			     		
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
        
        m_clCheckBoxWarning = (CheckBox) findViewById(R.id.CB_main_IsWarning);
        m_clCheckBoxWarning.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	{
        		if(m_clCheckBoxWarning.isChecked())
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
    	Cursor curExtraMemo;
    	EditText etMemoDetail = (EditText) findViewById(R.id.ET_main_Memo);
    	CheckBox CBFolder = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsFolder);
    	CheckBox CBEncode = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsEncode);
        if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){
        	curExtraMemo = m_clCNoteDBCtrl.getMemoRec(m_ExtraData_MemoID);
        	startManagingCursor(curExtraMemo);
        	curExtraMemo.moveToFirst();
        	int iIndex = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_detail);
        	String strDetail = curExtraMemo.getString(iIndex);
        	etMemoDetail.setText(strDetail);

       		CBFolder.setChecked(false);

        	iIndex = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_password);
        	String strPassWord = curExtraMemo.getString(iIndex);
        	if((strPassWord != null) && (strPassWord.length() > 0)){
        		CBEncode.setChecked(true);
        	}else{
        		CBEncode.setChecked(false);
        	}
        }else{
        	etMemoDetail.setText("");
        	CBFolder.setChecked(false);
        	CBEncode.setChecked(false);   	
        }
    }
}