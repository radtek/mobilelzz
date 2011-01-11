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
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/* 实际跳出闹铃Dialog的Activity */
public class AlarmAlert extends Activity implements View.OnClickListener , MediaStatusControl
{
	MediaPlayer 	mp;
	Vibrator 		m_vibrator;
	int				m_id;
	
	boolean			m_bRingflg;
	boolean			m_bVibrateflg;
	
	private SharedPreferences 	m_sp;
	
	
	public void onDestroy()
	{
		super.onDestroy();
		CommonDefine.getMediaPhoneCallListener(this).unadvise(this);
	}
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView( R.layout.alarmalert );
		/* 跳出的闹铃警示  */
		Intent iExtraData = getIntent();
		int id	=	iExtraData.getIntExtra(CommonDefine.ExtraData_EditNoteID, -1);
		if( -1 == id )
		{
			finish();
			return;
		}	
		
		CNoteDBCtrl	clCNoteDBCtrl	=	new	CNoteDBCtrl( AlarmAlert.this );
		Cursor clCursor	=	clCNoteDBCtrl.getNoteRec(id);
		if( !clCursor.moveToFirst() )
		{
			clCursor.close();
			finish();
			return;
		}
		
		m_bRingflg		=	false;
		m_bVibrateflg	=	false;
		
		int	iColumn	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_detail );
		String	strDetail		=	clCursor.getString(iColumn);
		
		iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_isRing );
		int	iIsRing	=	clCursor.getInt(iColumn);
		
		iColumn			=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_isVibrate );
		int	iIsVibrate	=	clCursor.getInt(iColumn);
		clCursor.close();

		m_id			=	id;
        CommonDefine.getMediaPhoneCallListener(this).advise(this);
  
        Button	btCancel	=	(Button)findViewById(R.id.CloseBt);
        btCancel.setOnClickListener(this);
        
        Button	btAgain		=	(Button)findViewById(R.id.RemindAgainBt);
        btAgain.setOnClickListener(this);
        
        TextView	TxtDetail	=	(TextView)findViewById(R.id.RemindInfoTxt);
        TxtDetail.setText( strDetail );
        TxtDetail.setOnClickListener(this);
             
        m_sp = getSharedPreferences(CommonDefine.ExtraData_Remind_File, Context.MODE_WORLD_WRITEABLE); 
        if( null == m_sp )
        {
			finish();
			return;
        }
		
        SharedPreferences R_Sp	= getSharedPreferences(CommonDefine.ExtraData_Remind_File, Context.MODE_WORLD_READABLE);	
        int iTemp	= R_Sp.getInt(CommonDefine.ExtraData_Remind_Flg, -1);
        
        if ( CommonDefine.iWorking	!=	iTemp )
        {
        	SharedPreferences.Editor editor = m_sp.edit();
        	editor.putInt(CommonDefine.ExtraData_Remind_Flg, CommonDefine.iWorking);
        	editor.commit();
         
            int	iRingTemp	=	-1;
            int	iVibrate	=	-1;
        	
        	AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            switch (audio.getRingerMode())
            {
            case AudioManager.RINGER_MODE_SILENT: 			//无声，不震动
            	iRingTemp	=	CMemoInfo.Ring_Off;
            	iVibrate	=	CMemoInfo.Vibrate_Off;
            		break;
            case AudioManager.RINGER_MODE_VIBRATE:			//无声，震动
            	iRingTemp	=	CMemoInfo.Ring_Off;	
            	iVibrate	=	CMemoInfo.Vibrate_On;
            		break;
            case AudioManager.RINGER_MODE_NORMAL:			//有声，震动    		
            	iRingTemp	=	CMemoInfo.Ring_On;
            	iVibrate	=	CMemoInfo.Vibrate_On;
            		break;
        		default:
        			break;
            	
            }
            
    		if( CMemoInfo.Ring_On == iIsRing &&  iRingTemp == CMemoInfo.Ring_On )
    		{
    		    mp = MediaPlayer.create( this, R.raw.bks);
    		    mp.start();
    		    m_bRingflg	=	true;
    		}
   		
    		if ( CMemoInfo.Vibrate_On == iIsVibrate && iVibrate == CMemoInfo.Vibrate_On )
    		{
    		    m_vibrator = (Vibrator) getSystemService(android.content.Context.VIBRATOR_SERVICE); 
    		    long[] pattern = {1000, 1000, 1000, 500};
    		    if( null != m_vibrator )
    		    {
    		    	m_vibrator.vibrate( pattern, 0 );
    		    	m_bVibrateflg	=	true;
    		    }	  		    
    		} 
        }

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
        switch( arg0.getId() )
        {
	        case R.id.CloseBt:
	        	processClose();
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
		  if( m_bRingflg )
		  {
	    	  mp.stop();
	    	  mp.release();	
	    	  m_bRingflg	=	false;
		  }
		  
		  if ( m_bVibrateflg )
		  {
			  m_vibrator.cancel();
			  m_bVibrateflg	=	false;
		  }
		
		final String[] AgainTypes = {"5分钟后","10分钟后","15分钟后","20分钟后","30分钟后","1小时后","2小时后"};
		new AlertDialog.Builder(this) 
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
			SharedPreferences.Editor editor = m_sp.edit();
        	editor.putInt(CommonDefine.ExtraData_Remind_Flg, CommonDefine.iNotWorking);
        	editor.commit();
			finish();
		} 
		}).show();//显示对话框 
	}
	
	void	sudProcessAgain( long _lMinute )
	{
		Calendar clCalendar     =     Calendar.getInstance();
        clCalendar.setTimeInMillis(System.currentTimeMillis());
        
        long	lTime	=	clCalendar.getTimeInMillis();
        
        lTime	+=	_lMinute;
		
		AlarmManager	alarmManager	=	(AlarmManager)getSystemService( Context.ALARM_SERVICE );
    	Intent 			MyIntent		=	new Intent( this, AlarmReceiver.class );
    	MyIntent.putExtra( CommonDefine.ExtraData_EditNoteID, m_id );
    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( this, m_id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
    	alarmManager.set(AlarmManager.RTC_WAKEUP, lTime, pendingIntent);
	}
	
	void	processEdit()
	{
		  if( m_bRingflg )
		  {
	    	  mp.stop();
	    	  mp.release();
	    	  m_bRingflg	=	false;
		  }
		  
		  if ( m_bVibrateflg )
		  {
			  m_vibrator.cancel();
			  m_bVibrateflg	=	false;
		  }
		
		Intent intent = new Intent();
		intent.setClass(this, NoteWithYourMind.class);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Edit);
		intent.putExtra(NoteWithYourMind.ExtraData_EditNoteID, m_id);
		
		this.startActivity(intent);
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
		  if( m_bRingflg )
		  {
	    	  mp.stop();
	    	  mp.release();	  
	    	  m_bRingflg	=	false;
		  }
		  
		  if ( m_bVibrateflg )
		  {
			  m_vibrator.cancel();
			  m_bVibrateflg	=	false;
		  }
		  SharedPreferences.Editor editor = m_sp.edit();
	      editor.putInt(CommonDefine.ExtraData_Remind_Flg, CommonDefine.iNotWorking);
	      editor.commit();
	      
		  finish();
	}
	
	@Override
	public void pauseMediaInteract() {
		// TODO Auto-generated method stub
		  if( m_bRingflg )
		  {
	    	  mp.stop();
	    	  mp.release();	  
	    	  m_bRingflg	=	false;
		  }
		  
		  if ( m_bVibrateflg )
		  {
			  m_vibrator.cancel();
			  m_bVibrateflg	=	false;
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
