package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RemindActivity extends Activity	implements View.OnClickListener
{
	CRemindInfo	m_clCRemindInfo	= null;
	private		CDateDlg		m_clCDateDlg	=	null;
	private		CWeekDlg		m_clCWeekDlg	=	null;
	private		CTimeDlg		m_clCTimeDlg	=	null;
	
	private		boolean			m_IsEnable		=	true;
	private 	RadioGroup 		m_RadioGroupTime;
	private 	RadioButton 	rbTime,rbCountdown; 
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsetting2);
        m_clCRemindInfo		=	new		CRemindInfo ( (byte)-1 );
    	m_clCDateDlg		=	new		CDateDlg( RemindActivity.this );
    	m_clCWeekDlg		=	new		CWeekDlg( RemindActivity.this );
    	m_clCTimeDlg		=	new		CTimeDlg( RemindActivity.this );
    	m_RadioGroupTime	=	(RadioGroup)findViewById(R.id.timeRadioButton); 
        
        
        rbTime		=	(RadioButton)findViewById(R.id.TimeSetting); 
        rbCountdown	=	(RadioButton)findViewById(R.id.daojishi); 
        
        m_RadioGroupTime.setOnCheckedChangeListener(mChangeRadio); 
        
        Button	btTime	=	(Button) findViewById(R.id.TimeSettingBtnImg);
        btTime.setOnClickListener(this);

        Button	btCountdown	=	(Button) findViewById(R.id.daojishiBtnImg);
        btCountdown.setOnClickListener(this);
        
        Button	btOnceDate	=	(Button) findViewById(R.id.OnceRemindImg);
        btOnceDate.setOnClickListener(this);
        
        Button	btWeek		=	(Button) findViewById(R.id.EveryWeekImg);
        btWeek.setOnClickListener(this);
        
        Button	btMonth		=	(Button) findViewById(R.id.MonthSetting);
        btMonth.setOnClickListener(this);

        //根据从编辑画页传入的数据设置当前Activity的状态
        setInput();
        //设定确定
        settingCheck();
 
    }
    
    public void onClick(View view)
    {
    	switch(view.getId())
    	{
    		case R.id.TimeSettingBtnImg:
    			processSaveTime(view);
    			break;
    		case R.id.daojishiBtnImg:
    			processCountdown(view);
    			break;
    		case R.id.OnceRemindImg:
    			processOnceDate(view);
    			break;
    		case R.id.EveryWeekImg:
    			processWeek(view);
    			break;
    		case R.id.MonthSetting:
    			processMonth(view);
    			break;
    		default:
    			break;
    	}
    }
    
    //响应按下的处理
    private void processSaveTime(View view)
    {
    	m_clCTimeDlg.setDisplay( rbTime );
    }
    
    private void processCountdown(View view)
    {
    	
    }
    
    private void processOnceDate(View view)
    {
    	m_clCDateDlg.setDisplay();
    }
    
    private void processWeek(View view)
    {
    	m_clCWeekDlg.setDisplay();
    }
    
    private void processMonth(View view)
    {
    	
    }
    //end
    
    private	void	setInput()
    {
	  Intent iExtraData = getIntent();
      CRemindInfo	clTemp	=	(CRemindInfo)iExtraData.getSerializableExtra(NoteWithYourMind.ExtraData_RemindSetting);
      if ( null != clTemp )		//	取得传入数据非空
      {
      	m_clCRemindInfo	=	clTemp;
      	if( 1 == m_clCRemindInfo.m_bType )					//倒计时提醒
      	{
      		rbCountdown.setChecked(true); 
      		
      		int	iHour	=	0;
      		int	iMinute	=	0;
      		m_clCRemindInfo.getCutDownTime(iHour, iMinute);
      		
      		
      	}
      	else if( 2 == m_clCRemindInfo.m_bType )				//循环提醒
      	{       		
      		rbTime.setChecked(true);
         		int	iHour	=	0;
      		int	iMinute	=	0;
      		byte	week[]	=	new	byte[ 7 ];
      		m_clCRemindInfo.getWeekTime(iHour, iMinute, week );
      		m_clCTimeDlg.iHour		=	iHour;
      		m_clCTimeDlg.iMinute	=	iMinute;
      		
      		int iLength	=	week.length;
      		for( int i = 0; i < iLength; ++i )
      		{
      			m_clCWeekDlg.Week[ i ]	=	week[ i ];
      		}
      		     		     		
      	}
      	else if( 3 == m_clCRemindInfo.m_bType )				//单次提醒
      	{
      		rbTime.setChecked(true);
      		int	iHour	=	0;
      		int	iMinute	=	0;
      		int	iYear	=	0;
      		int	iMonth	=	0;
      		int	iDay	=	0;
      		m_clCRemindInfo.getNormalTime(iHour, iMinute, iYear, iMonth, iDay);
      		m_clCTimeDlg.iHour		=	iHour;
      		m_clCTimeDlg.iMinute	=	iMinute;
      		
      		m_clCDateDlg.iYear	=	iYear;
      		m_clCDateDlg.iMonth	=	iDay;
      		m_clCDateDlg.iDay	=	iMonth;
      	}
      }
      else
      {
      	
      }
    }
    
    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener()
    {
        public void onCheckedChanged(RadioGroup group, int checkedId)
        { 
        	if(checkedId == m_RadioGroupTime.getId())
        	{
        		
        	}
        	else if(checkedId==m_RadioGroupTime.getId())
        	{
        		
        	}
        }
    };
    
    
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
					m_clCRemindInfo.setWeekTime( m_clCTimeDlg.iHour , m_clCTimeDlg.iMinute, m_clCWeekDlg.Week );		
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
					m_clCRemindInfo.setNormalTime( m_clCTimeDlg.iHour, m_clCTimeDlg.iMinute, m_clCDateDlg.iYear, m_clCDateDlg.iMonth, m_clCDateDlg.iDay );
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
