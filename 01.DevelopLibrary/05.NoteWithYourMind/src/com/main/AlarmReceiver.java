package com.main;


import java.util.Calendar;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver
{
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	private 		Calendar 		m_clCalendar;
	
//	public AlarmReceiver( Context context )
//	{
//		m_clCNoteDBCtrl	=	new	CNoteDBCtrl( context );
//		m_clCalendar 	= 	Calendar.getInstance();
//	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
//		Cursor clCursor	=	m_clCNoteDBCtrl.getRemindInfo();
////temp
//	    Intent i = new Intent(context, AlarmAlert.class);
//        
//	    Bundle bundleRet = new Bundle();
//	    bundleRet.putString("STR_CALLER", "");
//	    i.putExtras(bundleRet);
//	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    context.startActivity(i);
////		
//		while( clCursor.moveToNext() )
//		{
//			/*ȡ��ÿ������Memo������ʱ��*/
//			int 	TimeIndex	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
//			long	lDbTime		=	clCursor.getLong( TimeIndex );
//			
//			/*ȡ�õ�ǰϵͳʱ��*/
//			long	lCurTime	=	m_clCalendar.getTimeInMillis();	
//			
//			/*������ʱ��С�ڵ��ڵ�ǰʱ��ʱ����Memo��Ҫ������*/
//			if ( lDbTime <= lCurTime )
//			{
//				/*��ʾ���ѵ�����*/
//				int 	iDetailIndex 	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_detail );
//				String	strDetail		=	clCursor.getString( iDetailIndex );
//				
//				/*--����--��ʱ��Ӧ--*/
////			    Intent i = new Intent(context, AlarmAlert.class);
////		        
////			    Bundle bundleRet = new Bundle();
////			    bundleRet.putString("STR_CALLER", "");
////			    i.putExtras(bundleRet);
////			    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			    context.startActivity(i);
//				/*��������״̬*/
//				int		iIdIndex		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_id );
//				int		iID				=	clCursor.getInt( iIdIndex );
//				
//				CMemoInfo clCMemoInfo	=	new	CMemoInfo();
//				clCMemoInfo.iIsRemind	=	0;
//				
//				m_clCNoteDBCtrl.Update( iID, clCMemoInfo );
//			}				
//		}	
	}
}
