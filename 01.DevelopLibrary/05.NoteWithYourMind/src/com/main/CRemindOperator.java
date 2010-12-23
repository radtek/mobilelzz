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
    }  
      
    public synchronized static CRemindOperator getInstance()
    {  
        return _INSTANCE;  
    }  
    
    public	int	addRemind( Context context, int _id, CRemindInfo _clCRemindInfo )
    {
    	//����ʹ��ʱ��Ҫ�Ƚ���������Insert��DB�У�Ȼ���ٵ��ø÷���
    	//Insertʱ��������ص����Կ�������Ϊ��Ч����������Update
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
	
    	if ( CommonDefine.Remind_Type_CountDown == _clCRemindInfo.m_iType
    	  || CommonDefine.Remind_Type_Once == _clCRemindInfo.m_iType )
    	{
    		if ( clCalendar.getTimeInMillis() > _clCRemindInfo.m_lTime )
    		{
        		Toast toast = Toast.makeText(context, "����ʱ�䲻��ȷ������������!", Toast.LENGTH_SHORT);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();	
        		
        		return	CommonDefine.E_FAIL;
    		}
    		
    		
    		if ( _clCRemindInfo.m_iRemindAble	==	CMemoInfo.IsRemind_Able_Yes )
    		{
    			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	    	MyIntent.putExtra( "id", _id );
    	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, _id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
    	    	alarmManager.set(AlarmManager.RTC_WAKEUP, _clCRemindInfo.m_lTime, pendingIntent);			
    		}

    	}
    	else if ( CommonDefine.Remind_Type_Week == _clCRemindInfo.m_iType )
    	{   		
    		if ( _clCRemindInfo.m_iRemindAble	==	CMemoInfo.IsRemind_Able_Yes  )
    		{
    	    	long firtTime	=	_clCRemindInfo.getFirstCycelRemindTime();
    			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	    	MyIntent.putExtra( "id", _id );
    	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, _id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    	    	alarmManager.set(AlarmManager.RTC_WAKEUP, firtTime, pendingIntent);		
    		} 		
    	}
    	else
    	{		
    		//error
    	}
    	
    	return	CommonDefine.S_OK;
    }
    
    public	void	alarmAlert( Context context, int _id )
    {
    	//һ�����ѵ�ʱ�̵���ʱ���ø÷������趨�µ�����
    	//�����ѭ��������������
    	
    	if( null == m_clCNoteDBCtrl )
    	{
    		m_clCNoteDBCtrl	=	new	CNoteDBCtrl( context );
    	}
    	
    	CRemindInfo		clCRemindInfo	=	new	CRemindInfo( CommonDefine.Remind_Type_Invalid );
    	
    	if ( CommonDefine.E_FAIL == getRemindInfo( context, _id, clCRemindInfo ) )
    	{
    		return;
    	}
 	
	    if ( CommonDefine.Remind_Type_CountDown == clCRemindInfo.m_iType || CommonDefine.Remind_Type_Once == clCRemindInfo.m_iType )
	    {
	    	CMemoInfo	clCMemoInfo	=	new	CMemoInfo();
	    	clCMemoInfo.iIsRemindAble	=	CMemoInfo.IsRemind_Able_No;
	    	m_clCNoteDBCtrl.Update(_id, clCMemoInfo );
	    }
	    else if( CommonDefine.Remind_Type_Week == clCRemindInfo.m_iType )		//ѭ������
	    {
	    	long	lTime	=	clCRemindInfo.getFirstCycelRemindTime();
	    	
			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
	    	MyIntent.putExtra( "id", _id );
	    	
	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)_id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, lTime, pendingIntent);
	    }
	    else
	    {
	    	//Error
	    }	
    		
    }
    
    public	int	editRemind( Context context, int _id, CRemindInfo _clCRemindInfo )
    {
    	//��һ�����ѽ��б༭ʱ���ø÷�����
    	AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	MyIntent.putExtra( "id", _id );
    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, _id, MyIntent, 0);
    	alarmManager.cancel(pendingIntent);
    	
    	return	addRemind( context, _id, _clCRemindInfo  );
    }
    
   
    public	void	disableRemind( Context context, Integer[] needDeleteIDs )
    {
    	//��һ����������Ϊ��Ч
    	//�������Ѳ���תΪMemo�����Ը÷�����ɾ��������ΪDisable������
    	AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	
    	for( int i = 0; i < needDeleteIDs.length; ++i )
    	{
    		Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    		MyIntent.putExtra( "id", needDeleteIDs[i] );
        	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)needDeleteIDs[i], MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        	alarmManager.cancel(pendingIntent);
    	}

    }
    
    public	int	getRemindInfo( Cursor cur, CRemindInfo _clCRemindInfo )
    {
    	//����ID��DB��ȡ��������Ϣ
    	if ( !cur.moveToFirst() )
    	{
    		return	CommonDefine.E_FAIL;
    	}
    	
        int	index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_isremindable );
        int isRemindable	=	cur.getInt( index );
        _clCRemindInfo.m_iRemindAble	=	isRemindable;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_isremind );
        int isRemind	=	cur.getInt( index );
        _clCRemindInfo.m_iIsRemind	=	isRemind;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_remindtype );
        int isRemindType			=	cur.getInt( index );
        _clCRemindInfo.m_iType		=	isRemindType;
        
        index		=	cur.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
        long remindTime				=	cur.getLong(index);
        _clCRemindInfo.m_lTime		=	remindTime;
        
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
        
        return	CommonDefine.S_OK;    
    }
    
    public	int	getRemindInfo( Context context, int id, CRemindInfo _clCRemindInfo )
    {
    	if( null == m_clCNoteDBCtrl )
    	{
    		m_clCNoteDBCtrl	=	new	CNoteDBCtrl( context );
    	}
    	
    	Cursor cur	=	m_clCNoteDBCtrl.getRemindByID(id);
    	
    	return	getRemindInfo( cur, _clCRemindInfo );
    }
    
}