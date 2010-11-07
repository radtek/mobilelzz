package com.main;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver
{
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	private 		Calendar 		m_clCalendar;
	
	public AlarmReceiver( Context context )
	{
		m_clCNoteDBCtrl	=	new	CNoteDBCtrl( context );
		m_clCalendar 	= 	Calendar.getInstance();
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Cursor clCursor	=	m_clCNoteDBCtrl.getRemindInfo();
		
		while( clCursor.moveToNext() )
		{
			/*取得每个提醒Memo的提醒时间*/
			int 	TimeIndex	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
			long	lDbTime		=	clCursor.getLong( TimeIndex );
			
			/*取得当前系统时间*/
			long	lCurTime	=	m_clCalendar.getTimeInMillis();	
			
			/*当提醒时间小于等于当前时间时，该Memo需要被提醒*/
			if ( lDbTime <= lCurTime )
			{
				/*显示提醒的内容*/
				int 	iDetailIndex 	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_detail );
				String	strDetail		=	clCursor.getString( iDetailIndex );
				
				/*--弹框--临时对应--*/
        		Toast toast	=	Toast.makeText( context, strDetail, Toast.LENGTH_SHORT );
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();
				
				
				/*更新提醒状态*/
				int		iIdIndex		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_id );
				int		iID				=	clCursor.getInt( iIdIndex );
				
				CMemoInfo clCMemoInfo	=	new	CMemoInfo();
				clCMemoInfo.iIsRemind	=	0;
				
				m_clCNoteDBCtrl.Update( iID, clCMemoInfo );
			}				
		}	
	}
}
