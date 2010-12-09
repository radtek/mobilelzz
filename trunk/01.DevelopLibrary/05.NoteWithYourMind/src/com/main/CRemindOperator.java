package com.main;

import java.util.HashMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public final class CRemindOperator
{
    private static final CRemindOperator _INSTANCE	=	new CRemindOperator();  
    
//    private	HashMap< Long, CRemindInfo >	m_hashMap;
    
    private CNoteDBCtrl m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
    private CRemindOperator()
    {
    	//读取所有要被提醒的记录保存到hashMap中，并将每条提醒设置到alarmManager中
    }  
      
    public synchronized static CRemindOperator getInstance()
    {  
        return _INSTANCE;  
    }  
    
    public	void	addRemind( Context context, long _id, CRemindInfo _clCRemindInfo )
    {
    	//将该提醒信息添加到alarmManager中，同时保存到Map中
    	//外面使用时需要先将该条提醒Insert到DB中，然后再调用该方法
    	//Insert时和提醒相关的属性可以设置为无效，这里会进行Update
    	if ( 1 == _clCRemindInfo.m_bType )
    	{
    		
    	}
    	else if ( 2 == _clCRemindInfo.m_bType )
    	{
    		
    	}
    	else if ( 3 == _clCRemindInfo.m_bType )
    	{		
			AlarmManager	alarmManager	=	(AlarmManager)context.getSystemService( Context.ALARM_SERVICE );
	    	Intent 			MyIntent		=	new Intent( context, AlarmReceiver.class );
	    	MyIntent.putExtra( "id", _id );
	    	
	    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast( context, (int)_id, MyIntent, 0);
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, _clCRemindInfo.lTime, pendingIntent);
    	}
    }
    
    public	void	alarmAlert( long _id )
    {
    	//一个提醒的时刻到来时调用该方法
    	//首先根据intent中保存的ID到HashMap中找到该条记录，判断提醒类型
    	//如果是单次提醒则将该条信息从HashMap中删除，并设置DB中Isable项为False
    	//如果是循环提醒则不做处理
    }
    
    public	void	editRemind( long _id, CRemindInfo _clCRemindInfo )
    {
    	//对一个提醒进行编辑时调用该方法。
    	//想将该提醒从alarmManager和HashMap中删除，然后调用addRemind方法
    }
    
   
    public	void	disableRemind( long _id )
    {
    	//将一条提醒设置为无效
    	//将其从HashMap和alarmManager中删除，并将DB中的IsAble项设为无效
    	//由于提醒不能转为Memo，所以该方法对删除和设置为Disable都好用
    }
    
    public	void	getRemindInfo( long _id, CRemindInfo _clCRemindInfo )
    {
    	//根据ID从DB中取得提醒信息
    }
    
}
