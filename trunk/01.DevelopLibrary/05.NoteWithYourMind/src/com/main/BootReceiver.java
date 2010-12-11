package com.main;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;



public class BootReceiver extends BroadcastReceiver { 

	public	class	CWeekInfo
	{
		CWeekInfo()
		{
			m_bWeekNo	=	-1;
			m_IsAble	=	-1;
			m_Time		=	-1;
		}
		
		byte	m_bWeekNo;
		byte	m_IsAble;
		long	m_Time;
	}
	
    public void onReceive(Context ctx, Intent intent) 
    {
    	
		CNoteDBCtrl	clCNoteDBCtrl	=	new	CNoteDBCtrl( ctx );
		Calendar	clCalendar 		= 	Calendar.getInstance();
		
		
		Cursor clCursor	=	clCNoteDBCtrl.getRemindInfo();
		if ( clCursor.getCount() > 0 )
		{
			CWeekInfo	clCurrent	=	new		CWeekInfo();
			clCalendar.setTimeInMillis(System.currentTimeMillis());
			int		iID		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_id );
			long	lID		=	clCursor.getLong( iID );
				
			clCursor.moveToFirst();
			do
			{
				int		iType		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtype );
				long	lType		=	clCursor.getLong( iType );
				if( 1 == lType )						// 间隔:1,
				{
					
				}
				else if ( 2 == lType )					// 循环:2
				{
					clCurrent.m_bWeekNo		=	(byte)clCalendar.get( Calendar.DAY_OF_WEEK );
				}
				else if ( 3 == lType )					// 单次:3
				{
					int 	TimeIndex	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
					long	lDbTime		=	clCursor.getLong( TimeIndex );
					
					AlarmManager	alarmManager	=	(AlarmManager)ctx.getSystemService( Context.ALARM_SERVICE );
			    	Intent 			MyIntent		=	new Intent( ctx, AlarmReceiver.class );
			    	MyIntent.putExtra( "id", lID );
			    	
			    	
			    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(ctx, (int)lID,MyIntent, 0);
			    	alarmManager.set( AlarmManager.RTC_WAKEUP, lDbTime, pendingIntent );
				}
					
			}while( clCursor.moveToNext() );
		}
		
        //start activity    
//    	Toast toast = Toast.makeText(null, "收到启动通知", Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
//		toast.show();
//    	AlarmManager	alarmManager	=	(AlarmManager)ctx.getSystemService( Context.ALARM_SERVICE );
//    	Intent 			MyIntent		=	new Intent( ctx, AlarmReceiver.class );
//    	
//    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(ctx, 0,MyIntent, 0);
//    	alarmManager.setRepeating( AlarmManager.RTC, 0, 60 * 1000, pendingIntent );
//    	CommonDefine.g_test = 2;
    }    
} 
