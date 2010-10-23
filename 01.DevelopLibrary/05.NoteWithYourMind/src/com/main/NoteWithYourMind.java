package com.main;


import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;

public class NoteWithYourMind extends Activity {
	
	CheckBox m_clCheckBoxWarning;
	private	CNoteDBCtrl		m_clCNoteDBCtrl;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 /////zhu.t test start
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this, null, null, 0 );
        m_clCNoteDBCtrl.getWritableDatabase();
//test end  
        //EditText clET = (EditText) findViewById(R.id.ET_main_Memo);
        Button clBT = (Button) findViewById(R.id.B_main_Exit);
        clBT.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		NoteWithYourMind.this.finish();
        	}
        });
        
        Button clBTSave = (Button) findViewById(R.id.B_main_Save);
        clBTSave.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
 /////zhu.t test start   
        		CMemoInfo clCMemoInfo	=	new	CMemoInfo();
        		clCMemoInfo.iPreId	=	0;
        		clCMemoInfo.strDetail	=	"我是天才!!!!!";
        		m_clCNoteDBCtrl.Create(clCMemoInfo);
        		
        		Cursor clCursor	=	m_clCNoteDBCtrl.getMemoRootInfo();
        		
       
        		
        		int icolumnDetail	=	clCursor.getColumnIndex("Detail");
        		int icolumnID	=	clCursor.getColumnIndex("ID");
        		
        		if(clCursor.moveToFirst())
        		{ 
        			String name	=	clCursor.getString(icolumnDetail);
        			int  id		=	clCursor.getInt(icolumnID);
        			Toast toast = Toast.makeText(NoteWithYourMind.this,name/*"保存成功"*/, Toast.LENGTH_SHORT);
	        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
	        		toast.show();
        		}
 //test end 
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