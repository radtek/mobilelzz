package com.main;

import java.util.Calendar;

public class CMemoInfo {
	public final static Integer IsEditEnable_Invalid = -1;
	public final static Integer IsEditEnable_Disable = 0;
	public final static Integer IsEditEnable_Enable = 1;
	
	public final static Integer Type_Invalid = -1;
	public final static Integer Type_Folder = 0;
	public final static Integer Type_Memo = 1;
	//public final static Integer Type_PassWord = 2;

	
	public final static Integer IsRemind_Invalid = -1;
	public final static Integer IsRemind_Yes = 0;
	public final static Integer IsRemind_No = 1;
	
	public final static Integer IsRemind_Able_Yes = 1;
	public final static Integer IsRemind_Able_No = 0;
	
	public final static Integer Ring_On 	= 1;
	public final static Integer Ring_Off 	= 0;
	
	public final static Integer Vibrate_On	= 1;
	public final static Integer Vibrate_Off = 0;
	
	public final static Integer PreId_Root = 0;
	public final static Integer Id_Invalid = -1;

	public final static Integer IsEncode_Invalid = -1;
	public final static Integer IsEncode_Yes = 0;
	public final static Integer IsEncode_No = 1;
	
	public final static Integer IsHaveAudioData_Invalid = -1;
	public final static Integer IsHaveAudioData_No = 0;
	public final static Integer IsHaveAudioData_Yes = 1;
	
	Integer		iId;
	Integer		iPreId;
	Integer		iType;
	Integer		iIsRemind;
	long		dRemindTime;
	long		dCreateTime;
	long		dLastModifyTime;
	Integer		iIsEditEnable;
	Integer		iIsRemindAble;
	Integer		RemindType;
	String		strDetail;
	String		strPassword;
	Integer		iIsEncode;	
	Byte		m_Week[]	=	new Byte[7];
	
	Integer		iVibrate;
	Integer		iRing;
	Integer		iIsHaveAudioData;
	String		strAudioFileName;
	CMemoInfo()
	{
		iId				=	-1;
		iPreId			=	-1;
		iType			=	-1;
		iIsRemind		=	-1;
		dRemindTime		=	-1;
		dCreateTime		=	-1;
		dLastModifyTime	=	-1;
		iIsEditEnable	=	IsEditEnable_Enable;
		iIsRemindAble	=	-1;
		RemindType		=	-1;
		strDetail		=	null;
		strPassword		=	null;
		iIsEncode		=	-1;
		iVibrate		=	-1;
		iRing			=	-1;
		int	length	=	m_Week.length;
		for ( int i = 0; i < length; ++i )
		{
			m_Week[ i ]	=	-1;
		}
		iIsHaveAudioData = IsHaveAudioData_Invalid;
		strAudioFileName = null;
	}
	
	static	String	getTimeForListItem( long _lTime )
	{
		String	strTemp	=	null;
		Calendar clCalendar     =     Calendar. getInstance();		
		clCalendar.setTimeInMillis(_lTime);
		
		Calendar clCalendarCur     =     Calendar. getInstance();
		clCalendarCur.setTimeInMillis(System.currentTimeMillis());
		if( clCalendar.get(Calendar.YEAR) == clCalendarCur.get(Calendar.YEAR)
			&& clCalendar.get(Calendar.MONTH) == clCalendarCur.get(Calendar.MONTH)
			&& clCalendar.get(Calendar.DAY_OF_MONTH) == clCalendarCur.get(Calendar.DAY_OF_MONTH))
		{
			strTemp		=	String.format( "%02d:%02d", clCalendar.get(Calendar.HOUR_OF_DAY), clCalendar.get(Calendar.MINUTE) );
		}
		else if( System.currentTimeMillis() - _lTime < CRemindInfo.ONE_WEEK_TIME )
		{
			strTemp		=	CDateDlg.getDayofWeek(clCalendar);
		}
		else
		{
			strTemp		=	String.format( "%04d/%02d/%02d", clCalendar.get(Calendar.YEAR),clCalendar.get(Calendar.DAY_OF_MONTH) + 1, clCalendar.get(Calendar.DAY_OF_MONTH) );
		}
		
		return	strTemp;
	}
	
}
