package com.main;

public class CRemindInfo {

	byte		m_b1;		//周一到周日
	byte		m_b2;
	byte		m_b3;
	byte		m_b4;
	byte		m_b5;
	byte		m_b6;
	byte		m_b7;
	
	
	byte		m_bType;	//循环或单次
	long		lTime;		//如果单次提醒表示日期年月日
							//如果是循环提醒则高4为表示小时，低位表示分钟
	
	public	String	getRemindInfoString()
	{
		String	str = null;
		
		return	str;
	}
	
	
	CRemindInfo( byte bType )
	{
		m_b1	=	-1; m_b2	=	-1; m_b3	=	-1; m_b4	=	-1;
		m_b5	=	-1; m_b6	=	-1; m_b7	=	-1;
		
		m_bType			=	bType;
		lTime			=	0;
	}
	
}
