package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RemindActivity extends Activity	implements View.OnClickListener
{
	CRemindInfo	m_clCRemindInfo					=	null;
	private		CDateDlg		m_clCDateDlg	=	null;
	private		CWeekDlg		m_clCWeekDlg	=	null;
	private		CTimeDlg		m_clCTimeDlg	=	null;
	private		CCountdownDlg	m_clCCountdownDlg	=	null;
	
	
	private		boolean			m_IsEnable		=	true;
	private 	RadioGroup 		m_RadioGroupTime;
	private 	RadioGroup 		m_RadioGroupDate;
	private 	RadioButton 	rbTime,rbCountdown; 
	private		Byte			m_bType;
	
	//btn
	Button	btCountdown	=	null;
	Button	btTime		=	null;
	Button	btOnceDate	=	null;
	Button	btWeek		=	null;
	Button	btMonth		=	null;
	
	Button	btCancel	=	null;
	Button	btOK		=	null;	
	
	TextView	TimeTxt	=	null;
	TextView	CountDownTxt	=	null;
	TextView	DateTxt	=	null;
	
	TextView	WeekTxt[]	=	new	TextView[7];;
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsetting2);
        m_clCRemindInfo		=	new		CRemindInfo ( (byte)-1 );
    	m_clCDateDlg		=	new		CDateDlg( RemindActivity.this );
    	m_clCDateDlg.Initialize(m_bType);
    	m_clCWeekDlg		=	new		CWeekDlg( RemindActivity.this );
    	m_clCWeekDlg.Initialize(m_bType);
    	m_clCTimeDlg		=	new		CTimeDlg( RemindActivity.this);
    	m_clCTimeDlg.Initialize(m_bType);
    	m_clCCountdownDlg	=	new		CCountdownDlg( RemindActivity.this );
    	m_clCCountdownDlg.Initialize(m_bType);
   // 	WeekTxt				=	new	TextView[7];
    	m_RadioGroupTime	=	(RadioGroup)findViewById(R.id.timeRadioButton); 
    	m_RadioGroupDate	=	(RadioGroup)findViewById(R.id.dateRadioButton); 
        
    	TimeTxt				=	(TextView)findViewById(R.id.Time_txt);
    	DateTxt				=	(TextView)findViewById(R.id.OnceText);
    	CountDownTxt		=	(TextView)findViewById(R.id.daojishiTxt);
    	WeekTxt[0]			=	(TextView)findViewById(R.id.MonTxt);
    	WeekTxt[1]			=	(TextView)findViewById(R.id.TusTxt);
    	WeekTxt[2]			=	(TextView)findViewById(R.id.WedTxt);
    	WeekTxt[3]			=	(TextView)findViewById(R.id.ThrTxt);
    	WeekTxt[4]			=	(TextView)findViewById(R.id.FriTxt);
    	WeekTxt[5]			=	(TextView)findViewById(R.id.SatTxt);
    	WeekTxt[6]			=	(TextView)findViewById(R.id.SunTxt);
    	
        rbTime		=	(RadioButton)findViewById(R.id.TimeSetting); 
        rbCountdown	=	(RadioButton)findViewById(R.id.daojishi); 
        
        m_RadioGroupTime.setOnCheckedChangeListener(mChangeRadioTime);
        m_RadioGroupDate.setOnCheckedChangeListener(mChangeRadioDate);
        
        btTime	=	(Button) findViewById(R.id.TimeSettingBtnImg);
        btTime.setOnClickListener(this);

        btCountdown	=	(Button) findViewById(R.id.daojishiBtnImg);
        btCountdown.setOnClickListener(this);
        
        btOnceDate	=	(Button) findViewById(R.id.OnceRemindImg);
        btOnceDate.setOnClickListener(this);
        
        btWeek		=	(Button) findViewById(R.id.EveryWeekImg);
        btWeek.setOnClickListener(this);
        
        btCancel	=	(Button) findViewById(R.id.CancelBtn);
        btCancel.setOnClickListener(this);
        
        btOK		=	(Button) findViewById(R.id.OKBtn);
        btOK.setOnClickListener(this);
        

        //根据从编辑画页传入的数据设置当前Activity的状态
        setInput();

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
    		case R.id.CancelBtn:
    			
    			break;
       		case R.id.OKBtn:	
       			processOK();
    			break;   			
    		default:
    			break;
    	}
    }
    
    //响应按下的处理
    private void processSaveTime(View view)
    {
    	m_clCTimeDlg.setDisplay( TimeTxt );
 
    }
    
    private void processCountdown(View view)
    {
    	m_clCCountdownDlg.setDisplay(CountDownTxt);
    }
    
    private void processOnceDate(View view)
    {
    	m_clCDateDlg.setDisplay( DateTxt );
    }
    
    private void processWeek(View view)
    {
    	m_clCWeekDlg.setDisplay( WeekTxt );
    }
    private	void	processOK()
    {
    	m_clCRemindInfo.m_bType	=	m_bType;
    	
    	if( 2 == m_bType )		//循环提醒
		{				
			m_clCRemindInfo.setWeekTime( m_clCTimeDlg.iHour , m_clCTimeDlg.iMinute, m_clCWeekDlg.Week );	
		}
		else if( 1 == m_bType )	//倒计时提醒
		{
			m_clCRemindInfo.setCutDownTime( m_clCCountdownDlg.iHour, m_clCCountdownDlg.iMinute );
		}
		else if( 3 == m_bType )
		{	
			m_clCRemindInfo.setNormalTime( m_clCTimeDlg.iHour, m_clCTimeDlg.iMinute, m_clCDateDlg.iYear, m_clCDateDlg.iMonth, m_clCDateDlg.iDay );
		}
		
		Intent intent = new Intent(RemindActivity.this, NoteWithYourMind.class);  
		intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, m_clCRemindInfo );
		intent.putExtra( NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Update );
		
		startActivity(intent);	
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
    
    private RadioGroup.OnCheckedChangeListener mChangeRadioTime = new RadioGroup.OnCheckedChangeListener()
    {
        public void onCheckedChanged(RadioGroup group, int checkedId)
        { 
        	if(checkedId == R.id.TimeSetting)
        	{
        		m_clCTimeDlg.setDisplay( TimeTxt );
        		btTime.setVisibility(View.VISIBLE);
        		btCountdown.setVisibility(View.GONE);
        	}
        	else if(checkedId==R.id.daojishi)
        	{
        		m_clCCountdownDlg.setDisplay(CountDownTxt);
        		btTime.setVisibility(View.GONE);
        		btCountdown.setVisibility(View.VISIBLE);
        	}
        }
    };
    
    private RadioGroup.OnCheckedChangeListener mChangeRadioDate = new RadioGroup.OnCheckedChangeListener()
    {
        public void onCheckedChanged(RadioGroup group, int checkedId)
        { 
        	if(checkedId == R.id.OnceRemind)
        	{
        		m_clCDateDlg.setDisplay(DateTxt);
        		btOnceDate.setVisibility(View.VISIBLE);
        		btWeek.setVisibility(View.GONE);
        	}
        	else if(checkedId==R.id.EveryWeek)
        	{
        		m_clCWeekDlg.setDisplay( WeekTxt );
        		btWeek.setVisibility(View.VISIBLE);
        		btOnceDate.setVisibility(View.GONE);
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
}
