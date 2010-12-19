package com.main;

import java.io.Serializable;
import java.util.Calendar;

public class CRemindInfo implements Serializable
{

	public  String 	Interval	=	"间隔";
	public  String 	Cycel		=	"循环";
	public  String 	Once		=	"单次";
	
	public static final long ONE_WEEK_TIME	=	60 * 60 * 24 * 7 * 1000;
	
	private static final long serialVersionUID = -7060210544600464481L;
	
	Byte		m_Week[]	=	new Byte[7];	//从周日到周六
	
	Byte		m_bType;	//间隔:1,循环:2,单次:3,无效:-1
	long		lTime;		//如果单次提醒表示日期年月日
							//如果是循环提醒则高4为表示小时，低位表示分钟
	byte		bRemindAble;
	
	public	void	setWeekTime( int iHour, int iMinute, byte week[] )
	{
		iHour	<<=	16;
		iHour	+=	iMinute;
		
		lTime	=	iHour;
		
		int iLength	=	m_Week.length;
		for( int i = 0; i < iLength; ++i )
		{
			m_Week[ i ]	=	week[ i ];
		}
	}
	
	public	void	getWeekTime( CDateAndTime clCDateAndTime, byte week[] )
	{
		clCDateAndTime.iHour	=	(int) (lTime >> 16);
		clCDateAndTime.iMinute	=	(int)lTime & 0x0000ffff;
		
		int iLength	=	m_Week.length;
		for( int i = 0; i < iLength; ++i )
		{
			week[ i ]	=	m_Week[ i ];
		}
	}
	
	public	String	getCountDownBySting()
	{	
		String	strTemp	=	null;
		
		if ( m_bType == 1 || m_bType == 3 )
		{	
			strTemp	=	SubCountdownByString( lTime );
		}
		else if ( m_bType == 2 )
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
		String	strTemp	=	null;
		
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		long	lCur	=	clCalendar.getTimeInMillis();
		
		long	lTemp	=	_lTime - lCur;
		if ( 0 > lTemp )
		{
			return	strTemp;
		}	
		
		int	iTimeTemp	=	0;
		iTimeTemp	=	(int)(lTemp /1000 / 60 / 60 / 24 / 365);
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
		
		lTime	=	clCalendar.getTimeInMillis();
		
		lTime	+=	( ( iHour * 60 + iMinute ) * 60 ) * 1000;
	}
	
	public	void	getCutDownTime( CDateAndTime clCDateAndTime )
	{
		Calendar clCalendar	=	Calendar.getInstance();
		clCalendar.setTimeInMillis(System.currentTimeMillis());
		
		long	lCur	=	clCalendar.getTimeInMillis();
		long	lTemp	=	lTime	-	lCur;
		
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
		
		lTime	=	clCalendar.getTimeInMillis();
	}
	
	public boolean	checkTime()
	{
		boolean		bflg	=	false;
		
		if( m_bType == 1 || m_bType == 3 )
		{
			Calendar clCalendar	=	Calendar.getInstance();
			clCalendar.setTimeInMillis(System.currentTimeMillis());
			
			long	lCur	=	clCalendar.getTimeInMillis();
			if ( lCur > lTime )
			{
				bflg	=	false;
			}
			else
			{
				bflg	=	true;
			}
		}
		else if( m_bType == 2 )
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
		clCalendar.setTimeInMillis( lTime );
		
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
		if ( m_bType == 2 )					//2为循环提醒
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
					int	hour	=	(int)lTime >> 16;
					int	minute	=	(int)lTime & 0x0000ffff;
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
		
		return	-1;
	}
	
	public	String	getRemindInfoString()
	{
		String	strType		=	null;
		String	strInfo		=	null;
		
		if( m_bType == 1 )
		{
			strType	=	Interval;
			Long	hour	=	lTime >> 16;
			Long	minute	=	lTime & 0x0000ffff;
			strInfo	=	hour.toString()+ "小时" + minute.toString() + "分钟 后提醒";
		}
		else if( m_bType == 2 )
		{
			strType	=	Cycel;
			strInfo	=	"每周" + " ";
			for ( int i = 0; i < 7; ++i )
			{
				if ( -1 != m_Week[i])
				{
					Integer	iTemp	=	i + 1;
					strInfo	+=	iTemp.toString();
					strInfo	+=	"/";
				}
			}
			
			Long	hour	=	lTime >> 16;
			Long	minute	=	lTime & 0x0000ffff;
			strInfo	+=	hour.toString()+ "小时" + minute.toString() + "分钟 提醒";
		}
		else if( m_bType == 3 )
		{
			strType	=	Once;
			
			Calendar clCalendar	=	Calendar.getInstance();
			clCalendar.setTimeInMillis(lTime);
			
			strInfo	= 	clCalendar.get(Calendar.YEAR) +"/" 
						+ clCalendar.get(Calendar.MONTH) + 1
						+"/"+clCalendar.get(Calendar.HOUR_OF_DAY) + " " 
						+ clCalendar.get(Calendar.HOUR_OF_DAY) + ":"
						+clCalendar.get(Calendar.MINUTE);
			
			strInfo	+=	" 提醒";
		}
		else
		{

		}
		
		return	"["+ strType + "]" + strInfo;
	}
	
	
	CRemindInfo( byte bType )
	{
		int	length	=	m_Week.length;
		for ( int i = 0; i < length; ++i )
		{
			m_Week[ i ]	=	-1;
		}
		
		m_bType			=	bType;
		lTime			=	0;
		
		bRemindAble		=	1;
	}
	
}
