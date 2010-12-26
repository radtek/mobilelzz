package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RemindActivity extends Activity	implements View.OnClickListener
{
	CRemindInfo	m_clCRemindInfo					=	null;
	private		CDateDlg		m_clCDateDlg	=	null;
	private		CWeekDlg		m_clCWeekDlg	=	null;
	private		CTimeDlg		m_clCTimeDlg	=	null;
	private		CCountdownDlg	m_clCCountdownDlg	=	null;
	
	
	private 	RadioGroup 		m_RadioGroupTime;
	private 	RadioGroup 		m_RadioGroupDate;
	public	static	int			m_iType	=	CommonDefine.g_int_Invalid_ID;
	
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
	
	boolean		m_bInputflg		=	false;
	
	private 	RadioButton 	rbTime,	rbCountdown, rbOnce, rbWeek; 
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remindsetting2);
        m_clCRemindInfo		=	new		CRemindInfo ( CommonDefine.Remind_Type_Invalid );
    	m_clCDateDlg		=	new		CDateDlg( RemindActivity.this);
    	m_clCWeekDlg		=	new		CWeekDlg( RemindActivity.this);
    	m_clCTimeDlg		=	new		CTimeDlg( RemindActivity.this);
    	m_clCCountdownDlg	=	new		CCountdownDlg( RemindActivity.this);

    	m_RadioGroupTime	=	(RadioGroup)findViewById(R.id.timeRadioButton); 
    	m_RadioGroupDate	=	(RadioGroup)findViewById(R.id.dateRadioButton); 
    	      
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
        
        btCancel	=	(Button) findViewById(R.id.DisableBtn);
        btCancel.setOnClickListener(this);
        
        btOK		=	(Button) findViewById(R.id.OKBtn);
        btOK.setOnClickListener(this);
        
        rbTime		=	(RadioButton)findViewById(R.id.TimeSetting); 
        rbCountdown	=	(RadioButton)findViewById(R.id.daojishi); 
        
        rbOnce		=	(RadioButton)findViewById(R.id.OnceRemind); 
        rbWeek	=	(RadioButton)findViewById(R.id.EveryWeek); 

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
    		case R.id.DisableBtn:
    			processAble( false );
    			break;
       		case R.id.OKBtn:	
       			processAble( true );
    			break;   			
    		default:
    			break;
    	}
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        { 
        	Intent intent = new Intent(RemindActivity.this, NoteWithYourMind.class);
        	intent.putExtra( NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Update );
        	startActivity(intent);
        	return true;
        } 
        return super.onKeyDown(keyCode, event); 
    } 
    
    //响应按下的处理
    private void processSaveTime(View view)
    {
    	m_clCTimeDlg.setDisplay( );
 
    }
    
    private void processCountdown(View view)
    {
    	m_clCCountdownDlg.setDisplay();
    }
    
    private void processOnceDate(View view)
    {
    	m_clCDateDlg.setDisplay( );
    }
    
    private void processWeek(View view)
    {
    	m_clCWeekDlg.setDisplay( );
    }
    
    private	void	processAble( boolean bAble )
    {
    	m_clCRemindInfo.m_iType	=	m_iType;
    	
    	if( m_iType != CommonDefine.Remind_Type_CountDown && ( CommonDefine.g_int_Invalid_Time == m_clCTimeDlg.m_iHour 
    			|| CommonDefine.g_int_Invalid_Time == m_clCTimeDlg.m_iMinute ) )
    	{
    		Toast toast = Toast.makeText(RemindActivity.this, "时间设定错误，请重新设定!", Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
    		toast.show(); 
    		return;
    	}
    	
    	if( m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_Week )		//循环提醒
		{				
			m_clCRemindInfo.setWeekTime( m_clCTimeDlg.m_iHour , m_clCTimeDlg.m_iMinute, m_clCWeekDlg.m_bWeek );	
		}
		else if( m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_CountDown )	//倒计时提醒
		{
			m_clCRemindInfo.setCutDownTime( m_clCCountdownDlg.m_iHour, m_clCCountdownDlg.m_iMinute );
		}
		else if( m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_Once )
		{	
			m_clCRemindInfo.setNormalTime( m_clCTimeDlg.m_iHour, m_clCTimeDlg.m_iMinute, m_clCDateDlg.m_iYear, m_clCDateDlg.m_iMonth, m_clCDateDlg.m_iDay );
		}
		else
		{
			m_clCRemindInfo.m_iType		=	CommonDefine.Remind_Type_Once;
            Calendar clCalendar     =     Calendar. getInstance();
            clCalendar.setTimeInMillis(System. currentTimeMillis());

			m_clCRemindInfo.setNormalTime( m_clCTimeDlg.m_iHour, m_clCTimeDlg.m_iMinute, clCalendar.get(Calendar.YEAR)
					, clCalendar.get(Calendar.MONTH), clCalendar.get(Calendar.DAY_OF_MONTH) );
		}
    	
    		
    	if ( bAble )
    	{
        	if( m_clCRemindInfo.checkTime())
        	{
        		m_clCRemindInfo.m_iRemindAble	=	CMemoInfo.IsRemind_Able_Yes; 
        		m_clCRemindInfo.m_iIsRemind		=	CMemoInfo.IsRemind_Yes;
        		Intent intent = new Intent(RemindActivity.this, NoteWithYourMind.class);  
        		intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, m_clCRemindInfo );
        		intent.putExtra( NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Update );
        		
        		startActivity(intent);	
        	}
        	else
        	{
        		Toast toast = Toast.makeText(RemindActivity.this, "时间设定错误，请重新设定!", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();  			
        	}	
    	}
    	else
    	{
    		m_clCRemindInfo.m_iRemindAble	=	CMemoInfo.IsRemind_Able_No;  
    		m_clCRemindInfo.m_iIsRemind		=	CMemoInfo.IsRemind_Yes;
    		Intent intent = new Intent(RemindActivity.this, NoteWithYourMind.class);  
    		intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, m_clCRemindInfo );
    		intent.putExtra( NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Update );
    		
    		startActivity(intent);			
    	}
    }
    
    //end
    
    private	void	setInput()
    {
	  Intent iExtraData = getIntent();
      CRemindInfo	clTemp	=	(CRemindInfo)iExtraData.getSerializableExtra(NoteWithYourMind.ExtraData_RemindSetting);
      m_iType	=	CommonDefine.g_int_Invalid_ID;
      m_bInputflg	=	true;
      
      if ( null != clTemp )		//	取得传入数据非空
      {
      	m_clCRemindInfo	=	clTemp;
      	if( m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_CountDown )					//倒计时提醒
      	{
      		CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
      		m_clCRemindInfo.getCutDownTime( clCDateAndTime );
      		m_clCCountdownDlg.saveData( clCDateAndTime.iHour, clCDateAndTime.iMinute ); 
      		rbCountdown.setChecked(true);
      	}
      	else if( m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_Week)				//循环提醒
      	{       		
      		CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
      		byte	week[]	=	new	byte[ 7 ];
      		m_clCRemindInfo.getWeekTime( clCDateAndTime, week );
      		
      		rbTime.setChecked(true);
      		rbWeek.setChecked(true);
      		
      		m_clCTimeDlg.saveData(clCDateAndTime.iHour, clCDateAndTime.iMinute);
      		m_clCWeekDlg.setInputSatus ( week );  		     		
      	}
      	else if(  m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_Once )				//单次提醒
      	{
      		CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
      		
      		m_clCRemindInfo.getNormalTime( clCDateAndTime );
      		
      		m_clCDateDlg.saveData( clCDateAndTime.iYear, clCDateAndTime.iMonth, clCDateAndTime.iDay );
      		m_clCTimeDlg.saveData(clCDateAndTime.iHour, clCDateAndTime.iMinute);
      		
      		rbTime.setChecked(true);
      		rbOnce.setChecked(true);
      		
      	}
      }
      else
      {
      	 
      }
      
      m_bInputflg	=	false;
    }
    
    private RadioGroup.OnCheckedChangeListener mChangeRadioTime = new RadioGroup.OnCheckedChangeListener()
    {
        public void onCheckedChanged(RadioGroup group, int checkedId)
        { 
        	if(checkedId == R.id.TimeSetting)
        	{
        		if ( !m_bInputflg )
        		{
            		m_clCTimeDlg.setDisplay( );
        		}
        		btTime.setVisibility(View.VISIBLE);
        		btCountdown.setVisibility(View.GONE);

        	}
        	else if(checkedId==R.id.daojishi)
        	{
        		if ( !m_bInputflg )
        		{
            		m_clCCountdownDlg.setDisplay();  			
        		}
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
        		if ( !m_bInputflg )
        		{
            		m_clCDateDlg.setDisplay();     			
        		}
        		btOnceDate.setVisibility(View.VISIBLE);
        		btWeek.setVisibility(View.GONE); 

        	}
        	else if(checkedId==R.id.EveryWeek)
        	{
        		if ( !m_bInputflg )
        		{
            		m_clCWeekDlg.setDisplay( );   			
        		}
        		btWeek.setVisibility(View.VISIBLE);
        		btOnceDate.setVisibility(View.GONE);   
        	}
        }
    };
}
