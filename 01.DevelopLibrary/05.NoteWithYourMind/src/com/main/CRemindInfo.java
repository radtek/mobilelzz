package com.main;

public class CRemindInfo {

	boolean		m_b1;
	boolean		m_b2;
	boolean		m_b3;
	boolean		m_b4;
	boolean		m_b5;
	boolean		m_b6;
	boolean		m_b7;
	
	public	int	getRemindInfoIntger()
	{
		m_iRemindInfo	=	0;
		
		if( 1 == m_iType )
		{
			m_iRemindInfo	=	0x10000FF;
		}
		else if ( 2 == m_iType )
		{
			
		}
		else if ( 3 == m_iType )
		{
			
		}
		
		return	m_iRemindInfo;
	}
	
	public	String	getRemindInfoString()
	{
		return	m_strRemindInfo;
	}
	
	public	void	setRemindInfoIntger( int iInfo )
	{
		
	}
	
	CRemindInfo( int iType )
	{
		m_b1	=	false; m_b2	=	false; m_b3	=	false; m_b4	=	false;
		m_b5	=	false; m_b6	=	false; m_b7	=	false;
		
		m_iType			=	iType;
		m_iRemindInfo	=	0;
	}
	
	private		int			m_iRemindInfo;
	private		String		m_strRemindInfo;
	
	/**1:EveryDay;2:Once;3:Other**/
	private		int			m_iType;	
	
	public static final int		iMask1		= 0x0000000F;	
}
