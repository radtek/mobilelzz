package com.main;

import java.io.Serializable;
import java.util.Calendar;

public class CRemindInfo implements Serializable
{

	public  String 	Interval	=	"间隔";
	public  String 	Cycel		=	"循环";
	public  String 	Once		=	"单次";
	
	public static final long ONE_WEEK_TIME		=	60 * 60 * 24 * 7 * 1000;
	
	private static final long serialVersionUID	=	-7060210544600464481L;
	
	Byte		m_Week[]	=	new Byte[7];	//从周日到周六
	
	int			m_iType;				//间隔:1,循环:2,单次:3,无效:-1
	long		m_lTime;				//如果单次提醒表示日期年月日
										//如果是循环提醒则高4位表示小时，低4位表示分钟
	int			m_iRemindAble;
	int			m_iIsRemind;
	
	public	void	setWeekTime( int iHour, int iMinute, byte week[] )
	{
		iHour	<<=	16;
		iHour	+=	iMinute;
		
		m_lTime	=	iHour;
		
		int iLength	=	m_Week.length;
		for( int i = 0; i < iLength; ++i )
		{
			m_Week[ i ]	=	week[ i ];
		}
	}
	
	public	void	getWeekTime( CDateAndTime clCDateAndTime, byte week[] )
	{
		clCDateAndTime.iHour	=	(int) (m_lTime >> 16);
		clCDateAndTime.iMinute	=	(int) m_lTime & 0x0000ffff;
		
		if ( null != week )
		{
			int iLength	=	m_Week.length;
			for( int i = 0; i < iLength; ++i )
			{
				week[ i ]	=	m_Week[ i ];
			}		
		}

	}
	
	public	String	getCountDownBySting()	//取得距离下次提醒还有多长时间的字符串
	{	
		String	strTemp	=	null;
		
		if ( m_iType == CommonDefine.Remind_Type_CountDown || m_iType == CommonDefine.Remind_Type_Once )
		{	
			strTemp	=	SubCountdownByString( m_lTime );
		}
		else if ( m_iType == CommonDefine.Remind_Type_Week )
		{
			long	lTimeTemp	=	getFirstCycelRemindTime();
			
			strTemp	=	SubCountdownByString( lTimeTemp );
		}
		else
		{
			//error
		}
		

		return	strTemp;

	}
	
	private	String	SubCountdownByString( long _lTime )
	{
		String	strTemp		=	null;		//返回null为失败
		
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		long	lCur		=	clCalendar.getTimeInMillis();
		
		long	lTemp	=	_lTime - lCur;
		if ( 0 > lTemp )
		{
			return	strTemp;	
		}	
		
		int	iTimeTemp		=	(int)(lTemp /1000 / 60 / 60 / 24 / 365);
		if ( 0 < iTimeTemp )
		{
			strTemp = "下次提醒 : " + String.valueOf(iTimeTemp) +"年  后";
			return	strTemp;
		}
		
		iTimeTemp	=	(int)(lTemp /1000 / 60 / 60 / 24 );
		if ( 0 < iTimeTemp )
		{
			int hour	=	(int)(( lTemp /1000 / 60 / 60 )	-	iTimeTemp * 24 );
			strTemp = "下次提醒 : " + String.valueOf(iTimeTemp) +"天 " +String.valueOf(hour) + "小时后";
			return	strTemp;
		}
		
		int	iHour	=	(int)(lTemp / 1000 / 60/ 60 );
		lTemp   -=	( iHour * 1000 * 60 * 60 );
		
		int	iMinute	=	(int)( lTemp / 1000 / 60 );	
		
		if( 0 < iHour || 0 < iMinute )
		{
			strTemp = "下次提醒 : " + String.valueOf(iHour) + "小时" + String.valueOf(iMinute) + "分钟  后";
			return	strTemp;
		}
		else
		{
			strTemp = "下次提醒 : 小于 1 分钟";
		}
		

		return	strTemp;
	}
	
	public	void	setCutDownTime( int iHour, int iMinute )
	{
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		clCalendar.set(Calendar.SECOND, 0 );
		clCalendar.set(Calendar.MILLISECOND, 0 );
		
		m_lTime	=	clCalendar.getTimeInMillis();
		
		m_lTime	+=	( ( iHour * 60 + iMinute ) * 60 ) * 1000;
	}
	
	public	void	getCutDownTime( CDateAndTime clCDateAndTime )
	{
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		
		long	lCur	=	clCalendar.getTimeInMillis();
		long	lTemp	=	m_lTime	-	lCur;
		
		clCDateAndTime.iHour	=	new	Integer((int)(lTemp / 1000 / 60/ 60 ));
		lTemp   -=	( clCDateAndTime.iHour * 1000 * 60 * 60 );
		
		clCDateAndTime.iMinute	=	new	Integer((int)( lTemp / 1000 / 60 ));
	}
	
	public	void	setNormalTime( int iHour, int iMinute, int iYear, int iMonth, int iDay )
	{
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		clCalendar.set(Calendar.SECOND, 0 );
		clCalendar.set(Calendar.MILLISECOND, 0 );
		
		clCalendar.set(Calendar.YEAR, iYear );
		clCalendar.set(Calendar.MONTH, iMonth );
		clCalendar.set(Calendar.DAY_OF_MONTH, iDay );
		clCalendar.set(Calendar.HOUR_OF_DAY, iHour );
		clCalendar.set(Calendar.MINUTE, iMinute );
		
		m_lTime	=	clCalendar.getTimeInMillis();
	}
	
	public boolean	checkTime()
	{
		boolean		bflg	=	false;
		
		if( m_iType == CommonDefine.Remind_Type_CountDown || m_iType == CommonDefine.Remind_Type_Once )
		{
			Calendar clCalendar	=	Calendar.getInstance();
			clCalendar.setTimeInMillis(System.currentTimeMillis());
			
			long	lCur	=	clCalendar.getTimeInMillis();
			if ( lCur > m_lTime )
			{
				bflg	=	false;
			}
			else
			{
				bflg	=	true;
			}
		}
		else if( m_iType == CommonDefine.Remind_Type_Week )
		{
			
			for ( int i = 0; i < 7; ++i )
			{
				if ( 1 == m_Week[ i ] )
				{
					bflg	=	true;
					break;
				}
			}
		}
		else
		{
			//error
		}
		
		return	bflg;
	}
	
	public	void	getNormalTime( CDateAndTime clCDateAndTime )
	{
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis( m_lTime );
		
		clCDateAndTime.iYear	=	clCalendar.get(Calendar.YEAR);
		clCDateAndTime.iMonth	=	clCalendar.get(Calendar.MONTH);
		clCDateAndTime.iDay		=	clCalendar.get(Calendar.DAY_OF_MONTH);
		clCDateAndTime.iHour	=	clCalendar.get(Calendar.HOUR_OF_DAY);
		clCDateAndTime.iMinute	=	clCalendar.get(Calendar.MINUTE);
	}
	
	//根据当前系统时间，计算取得循环提醒 的第一次要提醒的时间
	//当提醒到来时再次调用该方法取得下一个提醒的时间
	public	long	getFirstCycelRemindTime()	
	{
		long	lTimeInnerMax	=	-1;				//返回值
		long	lTimeInnerMin	=	-1;				//返回值
		if ( m_iType == CommonDefine.Remind_Type_Week )	
		{
			//取得当前时间
			Calendar clCalendar	=	Calendar.getInstance();
			clCalendar.setTimeInMillis(System.currentTimeMillis());
			clCalendar.set(Calendar.SECOND, 0);
			clCalendar.set(Calendar.MILLISECOND, 0);
			long	lCurTimer	=	clCalendar.getTimeInMillis();
			

			long	lTempTime	=	-1;					//临时变量
			long	lMinTime	=	Long.MAX_VALUE;		//差值为正的最小时间为下次提醒时间，初始化为整数最大值
			long	lMaxTime	=	-1;					//差值为负的最小时间为下周的第一个提醒时间，初始化为负数最大值
			
			byte	bWeekTemp[]	=	new	byte[ 7 ];
			for( int i = 0; i < 6; ++i )
			{
				bWeekTemp[ i + 1 ]	=	m_Week[i];
			}
			
			bWeekTemp[ 0 ]	=	m_Week[6];
			
			for	( int i = 0; i < 7; ++i )
			{
				 if ( 1 == bWeekTemp[i] )
				 {
					clCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY + i );
					int	hour	=	(int)m_lTime >> 16;
					int	minute	=	(int)m_lTime & 0x0000ffff;
					 clCalendar.set( Calendar.HOUR_OF_DAY, hour);
					 clCalendar.set( Calendar.MINUTE, minute);
					 lTempTime	=	clCalendar.getTimeInMillis();
					 
					 lTempTime	-=	ONE_WEEK_TIME;
					 
					 long	lMinTemp	=	lTempTime	-	lCurTimer;
					 
					 if ( lMinTemp > 0 )				//在本星期提醒
					 {					 
						 if ( lMinTemp < lMinTime )
						 {
							 lMinTime			=	lMinTemp;	//保存最小的值
							 lTimeInnerMin		=	lTempTime;
						 }						 
					 }
					 else								//在下星期提醒
					 {
						 if ( lMinTemp < lMaxTime )
						 {
							 lMaxTime			=	lMinTemp;
							 lTimeInnerMax		=	lTempTime;
						 }
					 }
				 }
			}
			
			if ( lMinTime == Long.MAX_VALUE )				//说明提醒在下个星期
			{
				lTimeInnerMax	+=	ONE_WEEK_TIME;
				
				clCalendar.setTimeInMillis( lTimeInnerMax );		
				return	lTimeInnerMax;
			}
			else
			{
				clCalendar.setTimeInMillis( lTimeInnerMin );
				return	lTimeInnerMin;
			}
		}
		
		return	CommonDefine.E_FAIL;
	}
	

	CRemindInfo( int iType )
	{
		int	length		=	m_Week.length;
		for ( int i = 0; i < length; ++i )
		{
			m_Week[ i ]	=	(byte)CommonDefine.g_int_Invalid_ID;
		}
		
		m_iType			=	iType;
		m_lTime			=	0;
		
		m_iRemindAble	=	CommonDefine.g_int_Invalid_ID;
		m_iIsRemind		=	CommonDefine.g_int_Invalid_ID;
	}
	
}

class CDateAndTime
{
	int iYear;
	int iMonth;
	int	iDay;
	int iHour;
	int iMinute;
	
	CDateAndTime()
	{
		iYear	=	0;
		iMonth	=	0;
		iDay	=	0;
		iHour	=	0;
		iMinute	=	0;
	}
}
