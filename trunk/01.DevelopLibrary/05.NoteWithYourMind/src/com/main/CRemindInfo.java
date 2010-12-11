package com.main;

import java.io.Serializable;
import java.util.Calendar;

public class CRemindInfo implements Serializable
{

	public  String 	Interval	=	"���";
	public  String 	Cycel		=	"ѭ��";
	public  String 	Once		=	"����";
	
	public static final long ONE_WEEK_TIME	=	60 * 60 * 24 * 7 * 1000;
	
	private static final long serialVersionUID = -7060210544600464481L;
	
	Byte		m_Week[]	=	new Byte[7];	//�����յ�����
	
	Byte		m_bType;	//���:1,ѭ��:2,����:3,��Ч:-1
	long		lTime;		//����������ѱ�ʾ����������
							//�����ѭ���������4Ϊ��ʾСʱ����λ��ʾ����
	byte		bRemindAble;
	
	public	void	setTime( long lhour, long lminute )
	{
		lhour	<<=	16;
		lhour	+=	lminute;
		
		lTime	=	lhour;
	}
	
	//���ݵ�ǰϵͳʱ�䣬����ȡ��ѭ������ �ĵ�һ��Ҫ���ѵ�ʱ��
	//�����ѵ���ʱ�ٴε��ø÷���ȡ����һ�����ѵ�ʱ��
	public	long	getFirstCycelRemindTime()	
	{
		long	lTime	=	-1;				//����ֵ
		if ( 2 == m_bType )					//2Ϊѭ������
		{
			//ȡ�õ�ǰʱ��
			Calendar clCalendar	=	Calendar.getInstance();
			clCalendar.setTimeInMillis(System.currentTimeMillis());
			clCalendar.set(Calendar.SECOND, 0);
			clCalendar.set(Calendar.MILLISECOND, 0);
			long	lCurTimer	=	clCalendar.getTimeInMillis();
			

			long	lTempTime	=	-1;					//��ʱ����
			long	lMinTime	=	Long.MAX_VALUE;		//��ֵΪ������Сʱ��Ϊ�´�����ʱ�䣬��ʼ��Ϊ�������ֵ
			long	lMaxTime	=	-1;					//��ֵΪ������Сʱ��Ϊ���ܵĵ�һ������ʱ�䣬��ʼ��Ϊ�������ֵ
			
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
					 
					 if ( lMinTemp > 0 )				//�ڱ���������
					 {					 
						 if ( lMinTemp < lMinTime )
						 {
							 lMinTime	=	lMinTemp;	//������С��ֵ
							 lTime		=	lTempTime;
						 }						 
					 }
					 else								//������������
					 {
						 if ( lMinTemp < lMaxTime )
						 {
							 lMaxTime	=	lMinTemp;
							 lTime		=	lTempTime;
						 }
					 }
				 }
			}
			
			if ( lMinTime == Long.MAX_VALUE )				//˵���������¸�����
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
			strInfo	=	hour.toString()+ "Сʱ" + minute.toString() + "���� ������";
		}
		else if( 2 == m_bType )
		{
			strType	=	Cycel;
			strInfo	=	"ÿ��" + " ";
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
			strInfo	+=	hour.toString()+ "Сʱ" + minute.toString() + "���� ����";
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
			
			strInfo	+=	" ����";
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
