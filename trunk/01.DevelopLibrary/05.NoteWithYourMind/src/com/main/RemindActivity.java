package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RemindActivity extends Activity
{
	CRemindInfo	m_clCRemindInfo	= null;
	private		CDateDlg		m_clCDateDlg = null;
	private		CWeekDlg		m_clCWeekDlg = null;
	private		CTimeDlg		m_clCTimeDlg = null;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsetting2);
        m_clCRemindInfo	=	new	CRemindInfo ( (byte)-1 );
    	m_clCDateDlg	=	new		CDateDlg( RemindActivity.this );
    	m_clCWeekDlg	=	new		CWeekDlg( RemindActivity.this );
    	m_clCTimeDlg	=	new		CTimeDlg( RemindActivity.this );
        RadioGroup rg	=	(RadioGroup)findViewById(R.id.timeRadioButton); 
        
        RadioButton rbTime		=	(RadioButton)findViewById(R.id.TimeSetting); 
        RadioButton rbCountdown	=	(RadioButton)findViewById(R.id.daojishi); 

        Intent iExtraData = getIntent();
        CRemindInfo	clTemp	=	(CRemindInfo)iExtraData.getSerializableExtra(NoteWithYourMind.ExtraData_RemindSetting);
        if ( null != clTemp )
        {
        	m_clCRemindInfo	=	clTemp;
        	if( 1 == m_clCRemindInfo.m_bType )
        	{
        		rbCountdown.setChecked(true); 
        		
        		int	iHour	=	0;
        		int	iMinute	=	0;
        		m_clCRemindInfo.getCutDownTime(iHour, iMinute);
        		
        		
        	}
        	else if( 2 == m_clCRemindInfo.m_bType )
        	{       		
        		rbTime.setChecked(true);
           		int	iHour	=	0;
        		int	iMinute	=	0;
        		byte	week[]	=	new	byte[ 7 ];
        		m_clCRemindInfo.getWeekTime(iHour, iMinute, week );
        		m_clCTimeDlg.bHour		=	(byte)iHour;
        		m_clCTimeDlg.bMinute	=	(byte)iMinute;
        		
        		int iLength	=	week.length;
        		for( int i = 0; i < iLength; ++i )
        		{
        			m_clCWeekDlg.Week[ i ]	=	week[ i ];
        		}
        		     		     		
        	}
        	else if( 3 == m_clCRemindInfo.m_bType )
        	{
        		rbTime.setChecked(true);
        		int	iHour	=	0;
        		int	iMinute	=	0;
        		int	iYear	=	0;
        		int	iMonth	=	0;
        		int	iDay	=	0;
        		m_clCRemindInfo.getNormalTime(iHour, iMinute, iYear, iMonth, iDay);
        		m_clCTimeDlg.bHour		=	(byte)iHour;
        		m_clCTimeDlg.bMinute	=	(byte)iMinute;
        		
        		m_clCDateDlg.iYear	=	iYear;
        		m_clCDateDlg.bMonth	=	(byte) iDay;
        		m_clCDateDlg.bDay	=	(byte) iMonth;
        	}
        }
        else
        {
        	
        }

        //设定确定
        settingCheck();
        //停止启动
        settingAble();
 
    }
    
    void	settingAble()
    {
    	Button	BtAble	=	(Button) findViewById(R.id.RemindAble);
    	BtAble.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    }
    
    String	getDayofWeek( Calendar	clCalendar )
    {
    	String	strTemp	=	null;
    	switch( clCalendar.get(Calendar.DAY_OF_WEEK) )
    	{
    	case	1:
    		strTemp	=	"日";
    		break;
    	case	2:
    		strTemp	=	"一";
    		break;
    	case	3:
    		strTemp	=	"二";
    		break;
    	case	4:
    		strTemp	=	"三";
    		break;
    	case	5:
    		strTemp	=	"四";
    		break;
    	case	6:
    		strTemp	=	"五";
    		break;
    	case	7:
    		strTemp	=	"六";
    		break;
    	}
    	
    	return	strTemp;
    }
    
    void	settingCheck()
    {
    	Button	clCheck	=	(Button) findViewById(R.id.FixBtn);
    	clCheck.setOnClickListener( new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( 2 == m_clCRemindInfo.m_bType )		//循环提醒
				{				
					m_clCRemindInfo.setWeekTime( m_clCTimeDlg.bHour , m_clCTimeDlg.bMinute, m_clCWeekDlg.Week );		
				}
				else if( 1 == m_clCRemindInfo.m_bType )	//倒计时提醒
				{
//					Calendar clCalendar	=	Calendar.getInstance();
//					
//					EditText Xiaoshi	=	(EditText) findViewById(R.id.xiaoshi2);
//					EditText Fenzhong	=	(EditText) findViewById(R.id.fenzhong2);
//					
//					long	lhour		=	Long.valueOf( Xiaoshi.getText().toString() );
//					long	lminute		=	Long.valueOf( Fenzhong.getText().toString() );
//					
//					clCalendar.setTimeInMillis(System.currentTimeMillis());
//					clCalendar.set(Calendar.SECOND, 0 );
//					clCalendar.set(Calendar.MILLISECOND, 0 );
//					
//					long	lTime	=	clCalendar.getTimeInMillis();
//					lTime	+=	( lhour * 60 + lminute ) *1000;
//					
//					m_clCRemindInfo.lTime		=	lTime;				
				}
				else if( 3 == m_clCRemindInfo.m_bType )
				{	
					m_clCRemindInfo.setNormalTime( m_clCTimeDlg.bHour, m_clCTimeDlg.bMinute, m_clCDateDlg.iYear, m_clCDateDlg.bMonth, m_clCDateDlg.bDay );
				}
				
				Intent intent = new Intent(RemindActivity.this, NoteWithYourMind.class);  
//				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
				intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, m_clCRemindInfo );
				intent.putExtra( NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Update );
				
				startActivity(intent);	
			}
    		
    	});
    }
}
