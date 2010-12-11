package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class RemindSettingActivity extends Activity 
{
   
    static	private		boolean		bIsSelect	=	false;
    
    TabHost m_TabHost;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsettingnew);
        
        //设置TabHost
        m_TabHost = (TabHost)findViewById(android.R.id.tabhost);
        m_TabHost.setup(/*this.getLocalActivityManager()*/);
        
        TabSpec specLater = m_TabHost.newTabSpec( "3" );
        specLater.setIndicator("间隔提醒");
        specLater.setContent(R.id.remindinterval);
        m_TabHost.addTab(specLater);
        
        TimePicker clTimePicker01 = (TimePicker)findViewById(R.id.TimePicker01);
        clTimePicker01.setIs24HourView(true);
        TimePicker clTimePicker02 = (TimePicker)findViewById(R.id.TimePicker02);
        clTimePicker02.setIs24HourView(true);
        TimePicker clTimePicker03 = (TimePicker)findViewById(R.id.TimePicker03);
        clTimePicker03.setIs24HourView(true);
        
        Button clTimeFix = (Button) findViewById(R.id.TimeFix);
        clTimeFix.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				TimePicker clTimePicker = (TimePicker)findViewById(R.id.TimePicker03);
				CRemindInfo	clCRemindInfo	=	new	CRemindInfo ( (byte)1 );
				
				long	lhour	=	clTimePicker.getCurrentHour();
				long	lminute	=	clTimePicker.getCurrentMinute();
				
				lhour	<<=	16;
				lhour	+=	lminute;
				
				clCRemindInfo.lTime	=	lhour;
				
				Intent intent = new Intent(RemindSettingActivity.this, NoteWithYourMind.class);  
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
				intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, clCRemindInfo );
				startActivity(intent);
			}
        });
        Button clTimeCancel = (Button) findViewById(R.id.TimeCancel);
        clTimeCancel.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				doNothingAndReturn();
			}    	
        });   

        ////////////////////////////////////////////////////////////
        TabSpec specCycel = m_TabHost.newTabSpec("2");
        specCycel.setIndicator("循环提醒");
        specCycel.setContent(R.id.remindcycel);
        m_TabHost.addTab(specCycel);
        
        Button clCycelFix = (Button) findViewById(R.id.CycelFix);
        clCycelFix.setOnClickListener(new Button.OnClickListener()
        {

			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	CheckBox	checkBox1	=	( CheckBox )findViewById( R.id.C1);
		    	CheckBox	checkBox2	=	( CheckBox )findViewById( R.id.C2);
		    	CheckBox	checkBox3	=	( CheckBox )findViewById( R.id.C3);
		    	CheckBox	checkBox4	=	( CheckBox )findViewById( R.id.C4);
		    	CheckBox	checkBox5	=	( CheckBox )findViewById( R.id.C5);
		    	CheckBox	checkBox6	=	( CheckBox )findViewById( R.id.C6);
		    	CheckBox	checkBox7	=	( CheckBox )findViewById( R.id.C7);
		    	
		    	CRemindInfo	clCRemindInfo	=	new	CRemindInfo ( (byte)2 );
		    	byte	bCheckFlg		=	0;
		    	
		    	if( checkBox1.isChecked())
		    	{
		    		clCRemindInfo.m_Week[0]	=	1;
		    		bCheckFlg	=	1;
		    	}
		    	if( checkBox2.isChecked())
		    	{
		    		clCRemindInfo.m_Week[1]	=	1;
		    		bCheckFlg	=	1;
		    	}
		    	if( checkBox3.isChecked())
		    	{
		    		clCRemindInfo.m_Week[2]	=	1;
		    		bCheckFlg	=	1;
		    	}
		    	if( checkBox4.isChecked())
		    	{
		    		clCRemindInfo.m_Week[3]	=	1;
		    		bCheckFlg	=	1;
		    	}
		    	if( checkBox5.isChecked())
		    	{
		    		clCRemindInfo.m_Week[4]	=	1;
		    		bCheckFlg	=	1;
		    	}
		    	if( checkBox6.isChecked())
		    	{
		    		clCRemindInfo.m_Week[5]	=	1;
		    		bCheckFlg	=	1;
		    	}
		    	if( checkBox7.isChecked())
		    	{
		    		clCRemindInfo.m_Week[6]	=	1;
		    		bCheckFlg	=	1;
		    	}
		    	
		    	if ( 0 == bCheckFlg )
		    	{
            		Toast toast = Toast.makeText(RemindSettingActivity.this, "没有设置星期，请重新设置!", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();		
		    	}
		    	else
		    	{
			    	TimePicker clTimePicker = (TimePicker)findViewById(R.id.TimePicker02);
			    	
					long	lhour	=	clTimePicker.getCurrentHour();
					long	lminute	=	clTimePicker.getCurrentMinute();
					
					lhour	<<=	16;
					lhour	+=	lminute;
					
					clCRemindInfo.lTime	=	lhour;
					
					Intent intent = new Intent(RemindSettingActivity.this, NoteWithYourMind.class);  
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
					intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, clCRemindInfo );
					startActivity(intent);	    		
		    	}

			}
        	
        });
        
        Button clCycelCancel = (Button) findViewById(R.id.CycelCancel);
        clCycelCancel.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				doNothingAndReturn();
			}
        	
        });  
        
        //////////////////////////////////////////////////////////////
        TabSpec specOnce = m_TabHost.newTabSpec("1");
        specOnce.setIndicator("定时提醒");
        specOnce.setContent(R.id.remindonce);
        m_TabHost.addTab(specOnce);   
        
        Button clDateandTimeFix = (Button) findViewById(R.id.DateandTimeFix);
        clDateandTimeFix.setOnClickListener(new Button.OnClickListener()
        {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimePicker clTimePicker = (TimePicker)findViewById(R.id.TimePicker01);
				DatePicker clDatePicker = (DatePicker)findViewById(R.id.DatePicker01);
				
				Calendar clCalendar	=	Calendar.getInstance();
				clCalendar.set(Calendar.YEAR, clDatePicker.getYear());
				clCalendar.set(Calendar.MONTH, clDatePicker.getMonth());
				clCalendar.set(Calendar.DATE, clDatePicker.getDayOfMonth());
				
				clCalendar.set(Calendar.HOUR_OF_DAY, clTimePicker.getCurrentHour());
				clCalendar.set(Calendar.MINUTE, clTimePicker.getCurrentMinute());
				
				clCalendar.set(Calendar.SECOND, 0);
				
				long	lCur	=	System.currentTimeMillis();
				long	lSet	=	clCalendar.getTimeInMillis();
				
				if( lCur > lSet )
				{
            		Toast toast = Toast.makeText(RemindSettingActivity.this, "小于当前时间，请重新设置!", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
				}
				else
				{
					CRemindInfo	clCRemindInfo	=	new	CRemindInfo ( (byte)3 );
					clCRemindInfo.lTime			=	clCalendar.getTimeInMillis();
					
					Intent intent = new Intent(RemindSettingActivity.this, NoteWithYourMind.class);  
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
					intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, clCRemindInfo );
					startActivity(intent);
				}
			}
        	
        });
        
        Button clDateandTimeCancel = (Button) findViewById(R.id.DateandTimeCancel);
        clDateandTimeCancel.setOnClickListener(new Button.OnClickListener()
        {

			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				doNothingAndReturn();
			}
        	
        });  

        //点击确定时迁移画页，将NewNoteKindEnum设置为RemindSetting_Kind，并且设置为SingleTop
/////////////////////////////////////////////////////////////////////////////////////////        
        Button clSelect = (Button) findViewById(R.id.remind_select);
        clSelect.setOnClickListener(new ImageButton.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( bIsSelect )
				{
					/**反选**/
					bIsSelect	=	false;					
				}
				else
				{
					/**选择**/
					bIsSelect	=	true;
				}
				
				setCheckBoxFlg( bIsSelect );
			}
        });
    }
    
    private void doNothingAndReturn()
    {
		Intent intent = new Intent(RemindSettingActivity.this, NoteWithYourMind.class);  
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
		//startActivity(intent);	
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	Intent intent = new Intent(this, NoteWithYourMind.class);
    	//startActivity(intent);
    }

	void	setCheckBoxFlg( boolean bFlg )
    {
    	CheckBox	checkBox1	=	( CheckBox )findViewById( R.id.C1);
    	CheckBox	checkBox2	=	( CheckBox )findViewById( R.id.C2);
    	CheckBox	checkBox3	=	( CheckBox )findViewById( R.id.C3);
    	CheckBox	checkBox4	=	( CheckBox )findViewById( R.id.C4);
    	CheckBox	checkBox5	=	( CheckBox )findViewById( R.id.C5);
    	CheckBox	checkBox6	=	( CheckBox )findViewById( R.id.C6);
    	CheckBox	checkBox7	=	( CheckBox )findViewById( R.id.C7);
    	
    	checkBox1.setChecked( bFlg );
    	checkBox2.setChecked( bFlg );
    	checkBox3.setChecked( bFlg );
    	checkBox4.setChecked( bFlg );
    	checkBox5.setChecked( bFlg );
    	checkBox6.setChecked( bFlg );
    	checkBox7.setChecked( bFlg );
    }
	 
}
