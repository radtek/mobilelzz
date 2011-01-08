package com.main;

//package com.main;n
/* import相关class */
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CAlarmAlertDlg extends Dialog implements View.OnClickListener , MediaStatusControl
{
	
	MediaPlayer 	mp;
	Vibrator 		vibrator;
	
	Activity		m_Activity;
	int				m_iIsRing;
	int				m_iIsVibrate;
	int				m_id;
	
	private SharedPreferences sp;
	

	public CAlarmAlertDlg(Activity context)
	{
		super(context);
		
		m_Activity		=	context;
		m_iIsRing		=	-1;
		m_iIsVibrate	=	-1;

	}
	
	public void setDisplay( String	_Detail, int _id, int _iIsRing, int _iIsVibrate )
	{
        setContentView( R.layout.alarmalertdlg );
        CommonDefine.getMediaPhoneCallListener(m_Activity).advise(this);
        setProperty();

        setTitle("提醒");
        
        Button	btCancel	=	(Button)findViewById(R.id.CloseBt);
        btCancel.setOnClickListener(this);
        
        Button	btAgain		=	(Button)findViewById(R.id.RemindAgainBt);
        btAgain.setOnClickListener(this);
        
        TextView	TxtDetail	=	(TextView)findViewById(R.id.RemindInfoTxt);
        TxtDetail.setText( _Detail );
        TxtDetail.setOnClickListener(this);
             
        sp = m_Activity.getSharedPreferences(CommonDefine.ExtraData_Remind_File, Context.MODE_WORLD_WRITEABLE); 
        if( null == sp )
        {
			m_Activity.finish();
			return;
        }
        if ( CommonDefine.iWorking	!=	sp.getInt(CommonDefine.ExtraData_Remind_Flg, -1) )
        {
        	sp.edit().putInt(CommonDefine.ExtraData_Remind_Flg, CommonDefine.iWorking);
        	sp.edit().commit();
            m_iIsRing		=	_iIsRing;
            m_iIsVibrate	=	_iIsVibrate;
            m_id			=	_id;
        	
        	AudioManager audio = (AudioManager) m_Activity.getSystemService(Context.AUDIO_SERVICE);
            switch (audio.getRingerMode())
            {
            case AudioManager.RINGER_MODE_SILENT: 			//无声，不震动
            		m_iIsRing		=	CMemoInfo.Ring_Off;
            		m_iIsVibrate	=	CMemoInfo.Vibrate_Off;
            		break;
            case AudioManager.RINGER_MODE_VIBRATE:			//无声，震动
            		m_iIsRing		=	CMemoInfo.Ring_Off;	
            		m_iIsVibrate	=	CMemoInfo.Vibrate_On;
            		break;
            case AudioManager.RINGER_MODE_NORMAL:			//有声，震动    		
            		m_iIsRing		=	CMemoInfo.Ring_On;
            		m_iIsVibrate	=	CMemoInfo.Vibrate_On;
            		break;
        		default:
        			break;
            	
            }
            
    		if( CMemoInfo.Ring_On == m_iIsRing )
    		{
    		    mp =MediaPlayer.create(m_Activity, R.raw.bks);
    		    mp.start();
    		}
    		
    		if ( CMemoInfo.Vibrate_On == m_iIsVibrate )
    		{
    		    vibrator = (Vibrator) m_Activity.getSystemService(android.content.Context.VIBRATOR_SERVICE); 
    		    long[] pattern = {1000, 1000, 1000, 500};
    		    if( null != vibrator )
    		    {
    		    	vibrator.vibrate( pattern, 0 );
    		    }		   
    		}  
        }
          
        show(); 
    }
    private void setProperty()
    {
        Window						window	=	getWindow();
        WindowManager.LayoutParams	wl		=	window.getAttributes();
        
        wl.x	=	8;											
        wl.y	=	10;
        
//        wl.width	=	300;
       
        window.setAttributes(wl);        
   }
	public void onClick(View view)
	{
        switch( view.getId() )
        {
	        case R.id.CloseBt:
	        	processClose();
	        	cancel();
	            break;
	        case R.id.RemindAgainBt:
	        	processAgain();
	            break;
	        case R.id.RemindInfoTxt:
	        	processEdit();
	        	break;	        
	        default:
	        	break;
        }
    }
	
	void	processAgain()
	{
		final String[] AgainTypes = {"5分钟后","10分钟后","15分钟后","20分钟后","30分钟后","1小时后","2小时后"};
		new AlertDialog.Builder(m_Activity) 
		.setTitle("请选择再次提醒的时间") 
		.setNegativeButton("取消",new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int i)
			{			
				dialog.cancel();
			}
		})
		.setSingleChoiceItems(AgainTypes, 1, new DialogInterface.OnClickListener() { //此处数字为选项的下标，从0开始， 表示默认哪项被选中 
		public void onClick(DialogInterface dialog, int item) { 
			
			switch( item )
			{
			case	0:
				sudProcessAgain( CommonDefine.Minute_5 );
				break;
			case	1:
				sudProcessAgain( CommonDefine.Minute_10 );
				break;
			case	2:
				sudProcessAgain( CommonDefine.Minute_15 );
				break;
			case	3:
				sudProcessAgain( CommonDefine.Minute_20 );
				break;
			case	4:
				sudProcessAgain( CommonDefine.Minute_30 );
				break;
			case	5:
				sudProcessAgain( CommonDefine.Minute_60 );
				break;
			case	6:
				sudProcessAgain( CommonDefine.Minute_120 );
				break;
				
			default:
				break;
			}
			dialog.cancel(); 
			sp.edit().putInt(CommonDefine.ExtraData_Remind_Flg, CommonDefine.iNotWorking);
			sp.edit().commit();
			m_Activity.finish();
		} 
		}).show();//显示对话框 
	}
	
	void	sudProcessAgain( long _lMinute )
	{
		Calendar clCalendar     =     Calendar.getInstance();
        clCalendar.setTimeInMillis(System.currentTimeMillis());
        
        long	lTime	=	clCalendar.getTimeInMillis();
        
        lTime	+=	_lMinute;
		
		AlarmManager	alarmManager	=	(AlarmManager)m_Activity.getSystemService( Context.ALARM_SERVICE );
    	Intent 			MyIntent		=	new Intent( m_Activity, AlarmReceiver.class );
    	MyIntent.putExtra( CommonDefine.ExtraData_EditNoteID, m_id );
    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( m_Activity, m_id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
    	alarmManager.set(AlarmManager.RTC_WAKEUP, lTime, pendingIntent);
	}
	
	void	processEdit()
	{
		  if( CMemoInfo.Ring_On == m_iIsRing )
		  {
	    	  mp.stop();
	    	  mp.release();	  
		  }
		  
		  if ( CMemoInfo.Vibrate_On == m_iIsVibrate )
		  {
			  vibrator.cancel();
		  }
		
		Intent intent = new Intent();
		intent.setClass(m_Activity, NoteWithYourMind.class);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Edit);
		intent.putExtra(NoteWithYourMind.ExtraData_EditNoteID, m_id);
		
		m_Activity.startActivity(intent);
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        { 
        	processClose();
        	return	true;
        }
        
        return super.onKeyDown(keyCode, event); 
    }
	
	void	processClose()
	{
		CommonDefine.getMediaPhoneCallListener(m_Activity).unadvise(this);
		
		  if( CMemoInfo.Ring_On == m_iIsRing )
		  {
	    	  mp.stop();
	    	  mp.release();	  
		  }
		  
		  if ( CMemoInfo.Vibrate_On == m_iIsVibrate )
		  {
			  vibrator.cancel();
		  }
		  sp.edit().putInt(CommonDefine.ExtraData_Remind_Flg, CommonDefine.iNotWorking);
		  sp.edit().commit();
		  m_Activity.finish();
	}
	
	@Override
	public void pauseMediaInteract() {
		// TODO Auto-generated method stub
		if( null != mp )
		{
			mp.pause();
			vibrator.cancel();
		}
	}
	
	@Override
	public void resumeMediaInteract() {
		// TODO Auto-generated method stub
		if( null != mp )
		{
		}
	}
	
}