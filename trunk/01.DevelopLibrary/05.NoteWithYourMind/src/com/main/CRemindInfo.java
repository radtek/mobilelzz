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
	
	public	void	setTime( long lhour, long lminute )
	{
		lhour	<<=	16;
		lhour	+=	lminute;
		
		lTime	=	lhour;
	}
	
	//根据当前系统时间，计算取得循环提醒 的第一次要提醒的时间
	//当提醒到来时再次调用该方法取得下一个提醒的时间
	public	long	getFirstCycelRemindTime()	
	{
		long	lTime	=	-1;				//返回值
		if ( 2 == m_bType )					//2为循环提醒
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
			
			for	( int i = 0; i < 7; ++i )
			{
				 if ( -1 != m_Week[i] )
				 {
					 clCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY + i );
					int	hour	=	(int)lTime >> 16;
					int	minute	=	(int)lTime & 0x0000ffff;
					 clCalendar.set( Calendar.HOUR_OF_DAY, hour);
					 clCalendar.set( Calendar.MINUTE, minute);
					 lTempTime	=	clCalendar.getTimeInMillis();
					 
					 long	lMinTemp	=	lTempTime	-	lCurTimer;
					 
					 if ( lMinTemp > 0 )				//在本星期提醒
					 {					 
						 if ( lMinTemp < lMinTime )
						 {
							 lMinTime	=	lMinTemp;	//保存最小的值
							 lTime		=	lTempTime;
						 }						 
					 }
					 else								//在下星期提醒
					 {
						 if ( lMinTemp < lMaxTime )
						 {
							 lMaxTime	=	lMinTemp;
							 lTime		=	lTempTime;
						 }
					 }
				 }
			}
			
			if ( lMinTime == Long.MAX_VALUE )				//说明提醒在下个星期
			{
				lTime	+=	ONE_WEEK_TIME;
			}
		}
		
		
		
		return	lTime;
	}
	
	public	String	getRemindInfoString()
	{
		String	strType		=	null;
		String	strInfo		=	null;
		
		if( 1 == m_bType )
		{
			strType	=	Interval;
			Long	hour	=	lTime >> 16;
			Long	minute	=	lTime & 0x0000ffff;
			strInfo	=	hour.toString()+ "小时" + minute.toString() + "分钟 后提醒";
		}
		else if( 2 == m_bType )
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
		else if( 3 == m_bType )
		{
			strType	=	Once;
			
			Calendar clCalendar	=	Calendar.getInstance();
			clCalendar.setTimeInMillis(lTime);
			
			strInfo	= 	clCalendar.get(Calendar.YEAR) +"/" 
						+ clCalendar.get(Calendar.MONTH) 
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
