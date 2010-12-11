package com.main;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.widget.Toast;

public final class CRemindOperator
{
    private static final CRemindOperator _INSTANCE	=	new CRemindOperator();  
    
    
    private CNoteDBCtrl m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
    private CRemindOperator()
    {
    	//��ȡ����Ҫ�����ѵļ�¼���浽hashMap�У�����ÿ���������õ�alarmManager��
    }  
      
    public synchronized static CRemindOperator getInstance()
    {  
        return _INSTANCE;  
    }  
    
    public	int	addRemind( Context context, long _id, CRemindInfo _clCRemindInfo )
    {
    	//����ʹ��ʱ��Ҫ�Ƚ���������Insert��DB�У�Ȼ���ٵ��ø÷���
    	//Insertʱ��������ص����Կ�������Ϊ��Ч����������Update
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		if( null == m_clCNoteDBCtrl )
    	{
    		m_clCNoteDBCtrl	=	new	CNoteDBCtrl( context );
    	}
		
    	if ( 1 == _clCRemindInfo.m_bType )
    	{
    		if ( clCalendar.getTimeInMillis() > _clCRemindInfo.lTime )
    		{
        		Toast toast = Toast.makeText(context, "����ʱ�䲻��ȷ������������!", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();	
        		
        		return	-1;
    		}
    		
    		
    		CMemoInfo		clCMemoInfo	=	new	CMemoInfo();
    		clCMemoInfo.iIsRemind		=	1;
    		clCMemoInfo.iIsRemindAble	=	(int)(_clCRemindInfo.bRemindAble);
    		clCMemoInfo.RemindType		=	1;
    		clCMemoInfo.dRemindTime		=	_clCRemindInfo.lTime;
    		m_clCNoteDBCtrl.Update((int)_id, clCMemoInfo);
    		
			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
	    	MyIntent.putExtra( "id", _id );
	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)_id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, _clCRemindInfo.lTime, pendingIntent);
    	}
    	else if ( 2 == _clCRemindInfo.m_bType )
    	{
	    	CMemoInfo		clCMemoInfo	=	new	CMemoInfo();
    		clCMemoInfo.iIsRemind		=	1;
    		clCMemoInfo.iIsRemindAble	=	(int)(_clCRemindInfo.bRemindAble);
	    	clCMemoInfo.RemindType		=	2;
	    	clCMemoInfo.dRemindTime		=	_clCRemindInfo.lTime;
	    	clCMemoInfo.m_Week			=	_clCRemindInfo.m_Week;
	    	m_clCNoteDBCtrl.Update((int)_id, clCMemoInfo);
	    	
	    	long firtTime	=	_clCRemindInfo.getFirstCycelRemindTime();
			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
	    	MyIntent.putExtra( "id", _id );
	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)_id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, firtTime, pendingIntent);
    		
    	}
    	else if ( 3 == _clCRemindInfo.m_bType )
    	{		
    		if ( clCalendar.getTimeInMillis() > _clCRemindInfo.lTime )
    		{
        		Toast toast = Toast.makeText(context, "����ʱ�䲻��ȷ������������!", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();	
        		
        		return	-1;
    		}
    				
    		AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
	    	MyIntent.putExtra( "id", _id );
	    	
	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)_id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, _clCRemindInfo.lTime, pendingIntent);
	    	
	    	CMemoInfo		clCMemoInfo	=	new	CMemoInfo();
	    	
	    	clCMemoInfo.RemindType		=	3;
	    	clCMemoInfo.dRemindTime		=	_clCRemindInfo.lTime;
    		clCMemoInfo.iIsRemind		=	1;
    		clCMemoInfo.iIsRemindAble	=	(int)(_clCRemindInfo.bRemindAble);
	    	m_clCNoteDBCtrl.Update((int)_id, clCMemoInfo);
    	}
    	
    	return	0;
    }
    
    public	void	alarmAlert( Context context, int _id )
    {
    	//һ�����ѵ�ʱ�̵���ʱ���ø÷������趨�µ�����
    	//�����ѭ��������������
    	
    	if( null == m_clCNoteDBCtrl )
    	{
    		m_clCNoteDBCtrl	=	new	CNoteDBCtrl( context );
    	}
    	
    	CRemindInfo		clCRemindInfo	=	new	CRemindInfo( (byte)-1 );
    	getRemindInfo( context, _id, clCRemindInfo );
 	
	    if ( 1 == clCRemindInfo.m_bType || 3 == clCRemindInfo.m_bType )
	    {
	    	CMemoInfo	clCMemoInfo	=	new	CMemoInfo();
	    	clCMemoInfo.iIsRemindAble	=	-1;
	    	m_clCNoteDBCtrl.Update(_id, clCMemoInfo );
	    }
	    else if( 2 == clCRemindInfo.m_bType )		//ѭ������
	    {
	    	long	lTime	=	clCRemindInfo.getFirstCycelRemindTime();
	    	
			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
	    	MyIntent.putExtra( "id", _id );
	    	
	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)_id, MyIntent, 0);
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, lTime, pendingIntent);
	    }
	    else
	    {
	    	//Error
	    }	
    		
    }
    
    public	void	editRemind( Context context, long _id, CRemindInfo _clCRemindInfo )
    {
    	//��һ�����ѽ��б༭ʱ���ø÷�����
    	AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	MyIntent.putExtra( "id", _id );
    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)_id, MyIntent, 0);
    	alarmManager.cancel(pendingIntent);
    	
    	addRemind( context, _id, _clCRemindInfo  );
    }
    
   
    public	void	disableRemind( Context context, Integer[] needDeleteIDs )
    {
    	//��һ����������Ϊ��Ч
    	//�������Ѳ���תΪMemo�����Ը÷�����ɾ��������ΪDisable������
    	AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	for( int i = 0; i < needDeleteIDs.length; ++i )
    	{
        	MyIntent.putExtra( "id", needDeleteIDs[i] );
        	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)needDeleteIDs[i], MyIntent, 0);
        	alarmManager.cancel(pendingIntent);
    	}

    }
    
    public	void	getRemindInfo( Cursor cur, CRemindInfo _clCRemindInfo )
    {
    	//����ID��DB��ȡ��������Ϣ
    	cur.moveToFirst();
        int	index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_isremind );
        int isRemind	=	cur.getInt( index );
        _clCRemindInfo.bRemindAble	=	(byte)isRemind;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_remindtype );
        int isRemindType			=	cur.getInt( index );
        _clCRemindInfo.m_bType		=	(byte)isRemindType;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
        long remindTime				=	cur.getLong(index);
        _clCRemindInfo.lTime		=	remindTime;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_monday );
        int iMonday			=	cur.getInt( index );
        _clCRemindInfo.m_Week[ 0 ]	=	(byte)iMonday;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_tuesday );
        int iTuesday			=	cur.getInt( index );
        _clCRemindInfo.m_Week[ 1 ]	=	(byte)iTuesday;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_wednesday );
        int iWednesday			=	cur.getInt( index );
        _clCRemindInfo.m_Week[ 2 ]	=	(byte)iWednesday;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_thursday );
        int iThursday			=	cur.getInt( index );
        _clCRemindInfo.m_Week[ 3 ]	=	(byte)iThursday;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_friday );
        int iFriday			=	cur.getInt( index );
        _clCRemindInfo.m_Week[ 4 ]	=	(byte)iFriday;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_staturday );
        int iStaturday			=	cur.getInt( index );
        _clCRemindInfo.m_Week[ 5 ]	=	(byte)iStaturday;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_sunday );
        int iSunday			=	cur.getInt( index );
        _clCRemindInfo.m_Week[ 6 ]	=	(byte)iSunday;
        
    }
    
    public	void	getRemindInfo( Context context, int id, CRemindInfo _clCRemindInfo )
    {
    	if( null == m_clCNoteDBCtrl )
    	{
    		m_clCNoteDBCtrl	=	new	CNoteDBCtrl( context );
    	}
    	Cursor cur	=	m_clCNoteDBCtrl.getRemindByID(id);
    	
    	getRemindInfo( cur, _clCRemindInfo );
    }
    
}
