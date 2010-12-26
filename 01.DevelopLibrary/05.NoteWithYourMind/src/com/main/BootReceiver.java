package com.main;

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

		Cursor clCursor	=	clCNoteDBCtrl.getRemindInfo();	
		if ( !clCursor.moveToFirst() )
		{
			return;
		}
		
		if ( clCursor.getCount() > 0 )
		{
		
			do
			{
				int	iColumn	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_id );
				int	iID		=	clCursor.getInt( iColumn );
				
				iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtype );
				int	lType		=	clCursor.getInt( iColumn );
				if( CommonDefine.Remind_Type_CountDown == lType || CommonDefine.Remind_Type_Once == lType )		// 间隔:1,单次:3
				{
					int 	TimeIndex	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
					long	lDbTime		=	clCursor.getLong( TimeIndex );
					
					AlarmManager	alarmManager	=	(AlarmManager)ctx.getSystemService( Context.ALARM_SERVICE );
			    	Intent 			MyIntent		=	new Intent( ctx, AlarmReceiver.class );
			    	MyIntent.putExtra( "id", iID );
			    	
			    	
			    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(ctx, iID, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
			    	alarmManager.set( AlarmManager.RTC_WAKEUP, lDbTime, pendingIntent );				
				}
				else if ( CommonDefine.Remind_Type_Week == lType )					// 循环:2
				{
					CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
					CRemindInfo 	clCRemindInfo		=	new	CRemindInfo( CommonDefine.Remind_Type_Invalid );
					if( CommonDefine.E_FAIL == clCRemindOperator.getRemindInfo( clCursor, clCRemindInfo ) )
					{
						continue;
					}
					
					long	lTime	=	clCRemindInfo.getFirstCycelRemindTime();
					
					AlarmManager	alarmManager	=	(AlarmManager)ctx.getSystemService( Context.ALARM_SERVICE );
			    	Intent 			MyIntent		=	new Intent( ctx, AlarmReceiver.class );
			    	MyIntent.putExtra( "id", iID );
			    	
			    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(ctx, iID, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			    	alarmManager.set( AlarmManager.RTC_WAKEUP, lTime, pendingIntent );	
				}				
			}while( clCursor.moveToNext() );
		}
    }    
} 
