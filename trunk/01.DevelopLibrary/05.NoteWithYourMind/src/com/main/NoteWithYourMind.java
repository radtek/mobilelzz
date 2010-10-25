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
	
	CheckBox m_clCheckBoxWarning;
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	Calendar c;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        boolean bExistEncodeFolder = m_clCNoteDBCtrl.findEncodeFolder();
        if(!bExistEncodeFolder)
        {
    		CMemoInfo clCMemoInfo = new CMemoInfo();
    		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
    		clCMemoInfo.iPreId = CMemoInfo.PreId_Root;
    		clCMemoInfo.iType = CMemoInfo.Type_Folder;
    		clCMemoInfo.strDetail = CMemoInfo.Encode_Folder;
    		m_clCNoteDBCtrl.Create(clCMemoInfo);
        }
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
        		CMemoInfo clCMemoInfo	=	new	CMemoInfo();
        		clCMemoInfo.iPreId	=	0;
        		clCMemoInfo.strDetail	=	memotext.getText().toString();
        		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();
        		m_clCNoteDBCtrl.Create(clCMemoInfo);     		
        		memotext.setText("");
        		Toast toast = Toast.makeText(NoteWithYourMind.this, "保存成功", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();
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
}