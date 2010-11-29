package com.main;

import java.io.Serializable;

public class CRemindInfo implements Serializable
{

	public  String 	Interval	=	"���";
	public  String 	Cycel		=	"ѭ��";
	public  String 	Once		=	"����";
	
	private static final long serialVersionUID = -7060210544600464481L;
	
	byte		m_Week[]	=	new byte[7];
	
	byte		m_bType;	//���:1,ѭ��:2,����:3,��Ч:-1
	long		lTime;		//����������ѱ�ʾ����������
							//�����ѭ���������4Ϊ��ʾСʱ����λ��ʾ����
	
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
