package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
	
	private		CheckBox		m_cbRing		=	null;
	private		CheckBox		m_cbVibrate		=	null;
	private 	RadioGroup 		m_RadioGroupTime;
	private 	RadioGroup 		m_RadioGroupDate;
	public	static	int			m_iType			=	CommonDefine.g_int_Invalid_ID;
	
	//btn
	TextView	btCountdown	=	null;
	TextView	btTime		=	null;
	TextView	btOnceDate	=	null;
	LinearLayout	btWeek		=	null;
	
	Button	btCancel	=	null;
	Button	btOK		=	null;	
	
	TextView	TimeTxt	=	null;
	TextView	CountDownTxt	=	null;
	TextView	DateTxt			=	null;
	
	boolean		m_bInputflg		=	false;
	
	private 	RadioButton 	rbTime,	rbCountdown, rbOnce, rbWeek; 
	
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.remindsetting2);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		TextView tvTitleText = (TextView) findViewById(R.id.custom_title_text);
        tvTitleText.setText("设定提醒");
        ImageButton titleBack = (ImageButton) findViewById(R.id.custom_title_back);
        titleBack.setOnClickListener(this);
        
    	m_clCDateDlg		=	new		CDateDlg( RemindActivity.this);
    	m_clCWeekDlg		=	new		CWeekDlg( RemindActivity.this);
    	m_clCTimeDlg		=	new		CTimeDlg( RemindActivity.this);
    	m_clCCountdownDlg	=	new		CCountdownDlg( RemindActivity.this);

    	m_RadioGroupTime	=	(RadioGroup)findViewById(R.id.timeRadioButton); 
    	m_RadioGroupDate	=	(RadioGroup)findViewById(R.id.dateRadioButton); 
    	      
        m_RadioGroupTime.setOnCheckedChangeListener(mChangeRadioTime);
        m_RadioGroupDate.setOnCheckedChangeListener(mChangeRadioDate);
              
        btTime	=	(TextView) findViewById(R.id.Time_txt);
        btTime.setOnClickListener(this);

        btCountdown	=	(TextView) findViewById(R.id.daojishiTxt);
        btCountdown.setOnClickListener(this);
        
        btOnceDate	=	(TextView) findViewById(R.id.OnceText);
        btOnceDate.setOnClickListener(this);
        
        btWeek		=	(LinearLayout) findViewById(R.id.weekLayout);
        btWeek.setOnClickListener(this);
        
        btCancel	=	(Button) findViewById(R.id.DisableBtn);
        btCancel.setOnClickListener(this);
        
        btOK		=	(Button) findViewById(R.id.OKBtn);
        btOK.setOnClickListener(this);
        
        rbTime		=	(RadioButton)findViewById(R.id.TimeSetting); 
        rbCountdown	=	(RadioButton)findViewById(R.id.daojishi); 
        
        rbOnce		=	(RadioButton)findViewById(R.id.OnceRemind); 
        rbWeek		=	(RadioButton)findViewById(R.id.EveryWeek); 
        
        m_cbRing	=	( CheckBox )findViewById( R.id.RingCB);
        m_cbRing.setChecked(true);
        m_cbVibrate	=	( CheckBox )findViewById( R.id.VibrateCB);
        m_cbVibrate.setChecked(true);

        //根据从编辑画页传入的数据设置当前Activity的状态
        setInput();

    }
    
    public void onClick(View view)
    {
    	switch(view.getId())
    	{
    		case R.id.Time_txt:
    			processSaveTime(view);
    			break;
    		case R.id.daojishiTxt:
    			processCountdown(view);
    			break;
    		case R.id.OnceText:
    			processOnceDate(view);
    			break;
    		case R.id.weekLayout:
    			processWeek(view);
    			break;
    		case R.id.DisableBtn:
    			processAble( false );
    			break;
       		case R.id.OKBtn:	
       			processAble( true );
    			break;   
       		case R.id.custom_title_back:
    			this.finish();
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
        	intent.putExtra( NoteWithYourMind.ExtraData_RemindSetting, m_clCRemindInfo );
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
    		Toast toast = Toast.makeText(RemindActivity.this, "时间以过期，请重新设定!", Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
    		toast.show(); 
    		return;
    	}
    	
    	if( m_cbRing.isChecked() )
    	{
    		m_clCRemindInfo.m_iIsRing	=	CMemoInfo.Ring_On;
    	}
    	else
    	{
    		m_clCRemindInfo.m_iIsRing	=	CMemoInfo.Ring_Off;
    	}
    	
    	if( m_cbVibrate.isChecked() )
    	{
    		m_clCRemindInfo.m_iIsVibrate	=	CMemoInfo.Vibrate_On;
    	}
    	else
    	{
    		m_clCRemindInfo.m_iIsVibrate	=	CMemoInfo.Vibrate_Off;
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
      	 	
      	if( CMemoInfo.Ring_On == m_clCRemindInfo.m_iIsRing )
      	{
      		m_cbRing.setChecked(true);
      	}
      	else
      	{
      		m_cbRing.setChecked(false);
      	}
      	
      	if( CMemoInfo.Vibrate_On == m_clCRemindInfo.m_iIsVibrate )
      	{
      		m_cbVibrate.setChecked(true);
      	}
      	else
      	{
      		m_cbVibrate.setChecked(false);
      	}
      	
      	if( m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_CountDown )					//倒计时提醒
      	{
      		CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
      		m_clCRemindInfo.getCutDownTime( clCDateAndTime );
      		m_clCCountdownDlg.saveData( clCDateAndTime.iHour, clCDateAndTime.iMinute ); 
      		rbCountdown.setChecked(true);
      		
    		m_clCTimeDlg.hideView( R.id.timeLayout, R.id.TimeSetting);
    		m_clCDateDlg.hideView( R.id.dateLayout, R.id.OnceRemind);
    		m_clCWeekDlg.hideView( R.id.weekLayout, R.id.EveryWeek);
    		m_clCCountdownDlg.showView( R.id.countDownLayout, R.id.daojishi);
    		//倒计时提醒 - 设定日期和星期都不可点；设定日期星期和时间变黑
    		rbOnce.setClickable(false);
    		rbWeek.setClickable(false);
    		rbTime.setTextColor(Color.DKGRAY);
    		rbOnce.setTextColor(Color.DKGRAY);
    		rbWeek.setTextColor(Color.DKGRAY);
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
    		m_clCTimeDlg.showView( R.id.timeLayout, R.id.TimeSetting );
    		m_clCDateDlg.hideView( R.id.dateLayout, R.id.OnceRemind);
    		m_clCWeekDlg.showView( R.id.weekLayout, R.id.EveryWeek);
    		m_clCCountdownDlg.hideView( R.id.countDownLayout, R.id.daojishi);
    		
    		//循环提醒 - 倒计时和日期变黑
    		rbOnce.setTextColor(Color.DKGRAY);
    		rbCountdown.setTextColor(Color.DKGRAY);
    		
      	}
      	else if(  m_clCRemindInfo.m_iType == CommonDefine.Remind_Type_Once )				//单次提醒
      	{
      		CDateAndTime	clCDateAndTime	=	new	CDateAndTime();
      		
      		m_clCRemindInfo.getNormalTime( clCDateAndTime );
      		
      		m_clCDateDlg.saveData( clCDateAndTime.iYear, clCDateAndTime.iMonth, clCDateAndTime.iDay );
      		m_clCTimeDlg.saveData(clCDateAndTime.iHour, clCDateAndTime.iMinute);
      		
      		rbTime.setChecked(true);
      		rbOnce.setChecked(true);
      		
    		m_clCTimeDlg.showView( R.id.timeLayout, R.id.TimeSetting);
    		m_clCDateDlg.showView( R.id.dateLayout, R.id.OnceRemind);
    		m_clCWeekDlg.hideView( R.id.weekLayout, R.id.EveryWeek);
    		m_clCCountdownDlg.hideView( R.id.countDownLayout, R.id.daojishi);
    		
    		//单次提醒 - 倒计时和星期变黑
    		rbCountdown.setTextColor(Color.DKGRAY);
    		rbWeek.setTextColor(Color.DKGRAY);
      	}
      }
      else
      {
        m_clCRemindInfo		=	new		CRemindInfo ( CommonDefine.Remind_Type_Invalid );
  		m_clCTimeDlg.hideView( R.id.timeLayout, R.id.TimeSetting);
		m_clCDateDlg.hideView( R.id.dateLayout, R.id.OnceRemind);
		m_clCWeekDlg.hideView( R.id.weekLayout, R.id.EveryWeek);
		m_clCCountdownDlg.hideView( R.id.countDownLayout, R.id.daojishi);
      }
      
      m_bInputflg	=	false;
    }
    
    private RadioGroup.OnCheckedChangeListener mChangeRadioTime = new RadioGroup.OnCheckedChangeListener()
    {
        public void onCheckedChanged(RadioGroup group, int checkedId)
        { 
        	
        	
        	if(checkedId == R.id.TimeSetting)
        	{
        		RadioButton	rb	=	(RadioButton)findViewById(checkedId);
        		if( rb.isChecked())
        		{
            		if ( !m_bInputflg )
            		{
                		m_clCTimeDlg.setDisplay( );
            		}
            		m_clCTimeDlg.showView( R.id.timeLayout, R.id.TimeSetting);
            		m_clCCountdownDlg.hideView( R.id.countDownLayout, R.id.daojishi);
            		
            		//设定时间 - 星期和日期可点；时间、星期和日期变白；倒计时变黑
            		rbOnce.setClickable(true);
            		rbWeek.setClickable(true);
            		rbTime.setTextColor(Color.WHITE); 
            		rbOnce.setTextColor(Color.WHITE);           		
            		rbWeek.setTextColor(Color.WHITE);
            		rbCountdown.setTextColor(Color.DKGRAY);
        		}
        	}
        	else if(checkedId==R.id.daojishi)
        	{
        		RadioButton	rb	=	(RadioButton)findViewById(checkedId);
        		if( rb.isChecked())
        		{
        			if ( !m_bInputflg )
            		{
                		m_clCCountdownDlg.setDisplay();  			
            		}
            		m_clCTimeDlg.hideView( R.id.timeLayout, R.id.TimeSetting);
            		m_clCDateDlg.hideView( R.id.dateLayout, R.id.OnceRemind);
            		m_clCWeekDlg.hideView( R.id.weekLayout, R.id.EveryWeek);
            		m_clCCountdownDlg.showView( R.id.countDownLayout, R.id.daojishi);
            		
            		rbWeek.setChecked(false);          		
            		rbOnce.setChecked(false); 
            		
            		//倒计时设定 - 星期和日期不可点；时间、星期和日期变黑；倒计时变白
         		
            		rbOnce.setClickable(false);          		
            		rbWeek.setClickable(false);
            		rbOnce.setTextColor(Color.DKGRAY);
            		rbWeek.setTextColor(Color.DKGRAY);
            		rbTime.setTextColor(Color.DKGRAY);
            		
            		rbCountdown.setTextColor(Color.WHITE);
            		
            		m_RadioGroupDate.clearCheck();
        		}
        	}
        }
    };
    
    private RadioGroup.OnCheckedChangeListener mChangeRadioDate = new RadioGroup.OnCheckedChangeListener()
    {
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {      	
        	if(checkedId == R.id.OnceRemind)
        	{
        		RadioButton	rb	=	(RadioButton)findViewById(checkedId);
        		if ( rb.isChecked())
        		{
            		if ( !m_bInputflg )
            		{
                		m_clCDateDlg.setDisplay();     			
            		}
            		m_clCTimeDlg.showView( R.id.timeLayout, R.id.TimeSetting);
            		m_clCDateDlg.showView( R.id.dateLayout, R.id.OnceRemind);
            		m_clCWeekDlg.hideView( R.id.weekLayout, R.id.EveryWeek);
            		m_clCCountdownDlg.hideView( R.id.countDownLayout, R.id.daojishi);
            		
            		rbCountdown.setChecked(false); 
            		
            		//日期设定 - 星期变黑；日期变白
            		rbWeek.setTextColor(Color.DKGRAY);  
            		rbOnce.setTextColor(Color.WHITE);
//            		m_RadioGroupTime.clearCheck();
        		}

        	}
        	else if(checkedId==R.id.EveryWeek)
        	{
        		RadioButton	rb	=	(RadioButton)findViewById(checkedId);
        		if ( rb.isChecked())
        		{
            		if ( !m_bInputflg )
            		{
                		m_clCWeekDlg.setDisplay( );   			
            		}
            		m_clCTimeDlg.showView( R.id.timeLayout, R.id.TimeSetting);
            		m_clCDateDlg.hideView( R.id.dateLayout, R.id.OnceRemind);
            		m_clCWeekDlg.showView( R.id.weekLayout, R.id.EveryWeek);
            		m_clCCountdownDlg.hideView( R.id.countDownLayout, R.id.daojishi);
            		rbCountdown.setChecked(false); 
            		
            		//星期设定 - 日期变黑；星期变白
            		rbOnce.setTextColor(Color.DKGRAY);
            		rbWeek.setTextColor(Color.WHITE);
 //           		m_RadioGroupTime.clearCheck();
        		}
        	}
        }
    };
}
