package com.main;

import java.io.Serializable;

public class CRemindInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7060210544600464481L;
	byte		m_b1;		//��һ������
	byte		m_b2;
	byte		m_b3;
	byte		m_b4;
	byte		m_b5;
	byte		m_b6;
	byte		m_b7;
	
	
	byte		m_bType;	//ѭ��:0,����:1 ,��Ч:-1
	long		lTime;		//����������ѱ�ʾ����������
							//�����ѭ���������4Ϊ��ʾСʱ����λ��ʾ����
	
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
