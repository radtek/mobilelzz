package com.main;

import java.io.Serializable;

public class CRemindInfo implements Serializable
{

	public  String 	Interval	=	"间隔";
	public  String 	Cycel		=	"循环";
	public  String 	Once		=	"单次";
	
	private static final long serialVersionUID = -7060210544600464481L;
	
	byte		m_Week[]	=	new byte[7];
	
	byte		m_bType;	//间隔:1,循环:2,单次:3,无效:-1
	long		lTime;		//如果单次提醒表示日期年月日
							//如果是循环提醒则高4为表示小时，低位表示分钟
	
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
		}
		else if( 3 == m_bType )
		{
			strType	=	Once;
		}
		else
		{
			
		}
		
		return	strType + strInfo;
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
	}
	
}
