package com.main;

import java.util.ArrayList;
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
    private static CRemindOperator _INSTANCE	=	null;  
    private CNoteDBCtrl m_clCNoteDBCtrl = null;
    private CRemindOperator(Context context)
    {
    	m_clCNoteDBCtrl = CommonDefine.getNoteDBCtrl(context);
    }  
      
    public synchronized static CRemindOperator getInstance(Context context)
    {  
    	if(_INSTANCE == null){
    		_INSTANCE = new CRemindOperator(context);
    	}
        return _INSTANCE;  
    }  
    public int processRemind(Context context, int _id, CRemindInfo _clCRemindInfo){
    	int ret = CommonDefine.E_FAIL;
    	if(_clCRemindInfo==null){
    		return ret;
    	}
    	if(_id==CommonDefine.g_int_Invalid_ID){
    		ret = addRemind( context, _id, _clCRemindInfo);
    	}else{
    		ret = editRemind( context, _id, _clCRemindInfo);
    	}
    	return ret;
    }
    
    private	int	addRemind( Context context, int _id, CRemindInfo _clCRemindInfo )
    {
    	//外面使用时需要先将该条提醒Insert到DB中，然后再调用该方法
    	//Insert时和提醒相关的属性可以设置为无效，这里会进行Update
    	if ( _clCRemindInfo.m_iRemindAble == CMemoInfo.IsRemind_Able_Yes 
    		&& _clCRemindInfo.m_iIsRemind == CMemoInfo.IsRemind_Yes )
    	{
    		Calendar clCalendar	=	Calendar.getInstance();
    		clCalendar.setTimeInMillis(System.currentTimeMillis());
    	
        	if ( CommonDefine.Remind_Type_CountDown == _clCRemindInfo.m_iType
        	  || CommonDefine.Remind_Type_Once == _clCRemindInfo.m_iType )
        	{
        		if ( clCalendar.getTimeInMillis() > _clCRemindInfo.m_lTime )
        		{
            		Toast toast = Toast.makeText(context, "提醒时间不正确，请重新设置!", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();	
            		
            		return	CommonDefine.E_FAIL;
        		}
        		
    			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	    	MyIntent.putExtra( "id", _id );
    	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, _id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
    	    	alarmManager.set(AlarmManager.RTC_WAKEUP, _clCRemindInfo.m_lTime, pendingIntent);			


        	}
        	else if ( CommonDefine.Remind_Type_Week == _clCRemindInfo.m_iType )
        	{   		
    	    	long firtTime	=	_clCRemindInfo.getFirstCycelRemindTime();
    			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	    	MyIntent.putExtra( "id", _id );
    	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, _id, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    	    	alarmManager.set(AlarmManager.RTC_WAKEUP, firtTime, pendingIntent);				
        	}
        	else
        	{		
        		//error
        	}
        	
    	}
		
    	return	CommonDefine.S_OK;
    }
    
    public	void	alarmAlert( Context context, int _id )
    {
    	//一个提醒的时刻到来时调用该方法，设定新的提醒
    	//如果是循环提醒则不做处理
    	
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
	    	clCMemoInfo.iId = _id;
	    	m_clCNoteDBCtrl.Update( clCMemoInfo );
	    }
	    else if( CommonDefine.Remind_Type_Week == clCRemindInfo.m_iType )		//循环提醒
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
    
    private	int	editRemind( Context context, int _id, CRemindInfo _clCRemindInfo )
    {
    	//对一个提醒进行编辑时调用该方法。
    	AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    	MyIntent.putExtra( "id", _id );
    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, _id, MyIntent, 0);
    	alarmManager.cancel(pendingIntent);
    	
    	return	addRemind( context, _id, _clCRemindInfo  );
    }
    
   
    public	void	disableRemind( Context context, ArrayList<DetailInfoOfSelectItem> needDeleteIDs )
    {
    	//将一条提醒设置为无效
    	//由于提醒不能转为Memo，所以该方法对删除和设置为Disable都好用
    	AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
    	
    	for( int i = 0; i < needDeleteIDs.size(); ++i )
    	{
    		Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
    		MyIntent.putExtra( "id", needDeleteIDs.get(i).iDBRecID );
        	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, needDeleteIDs.get(i).iDBRecID, MyIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        	alarmManager.cancel(pendingIntent);
    	}

    }
    
    public	int	getRemindInfo( Cursor cur, CRemindInfo _clCRemindInfo )
    {
    	//根据ID从DB中取得提醒信息
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
    	
		int	iTemp	=	getRemindInfo( cur, _clCRemindInfo );
    	
		cur.close();
		return	iTemp;
    }
    
}
