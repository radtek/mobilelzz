package com.main;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;



public class BootReceiver extends BroadcastReceiver { 

    public void onReceive(Context ctx, Intent intent) 
    {
    	
		CNoteDBCtrl	clCNoteDBCtrl	=	new	CNoteDBCtrl( ctx );
		Calendar	clCalendar 		= 	Calendar.getInstance();
		
		
		Cursor clCursor	=	clCNoteDBCtrl.getRemindInfo();
		if ( clCursor.getCount() > 0 )
		{
			clCalendar.setTimeInMillis(System.currentTimeMillis());
			int		iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_id );
			int	lID		=	clCursor.getInt( iColumn );
				
			clCursor.moveToFirst();
			do
			{
				iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtype );
				int	lType		=	clCursor.getInt( iColumn );
				if( 1 == lType || 3 == lType )						// 间隔:1,单次:3
				{
					int 	TimeIndex	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
					long	lDbTime		=	clCursor.getLong( TimeIndex );
					
					AlarmManager	alarmManager	=	(AlarmManager)ctx.getSystemService( Context.ALARM_SERVICE );
			    	Intent 			MyIntent		=	new Intent( ctx, AlarmReceiver.class );
			    	MyIntent.putExtra( "id", lID );
			    	
			    	
			    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(ctx, lID,MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
			    	alarmManager.set( AlarmManager.RTC_WAKEUP, lDbTime, pendingIntent );				
				}
				else if ( 2 == lType )					// 循环:2
				{
					CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
					CRemindInfo clCRemindInfo	=	new	CRemindInfo( (byte)-1 );
					clCRemindOperator.getRemindInfo( ctx, lID, clCRemindInfo);
					
					long	lTime	=	clCRemindInfo.getFirstCycelRemindTime();
					
					AlarmManager	alarmManager	=	(AlarmManager)ctx.getSystemService( Context.ALARM_SERVICE );
			    	Intent 			MyIntent		=	new Intent( ctx, AlarmReceiver.class );
			    	MyIntent.putExtra( "id", lID );
			    	
			    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(ctx, lID, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			    	alarmManager.set( AlarmManager.RTC_WAKEUP, lTime, pendingIntent );	
				}

					
			}while( clCursor.moveToNext() );
		}
    }    
} 
