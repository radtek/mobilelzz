package com.main;

//package com.main;n
/* import���class */
import android.app.Activity;
import android.app.Dialog;

/* ʵ����������Dialog��Activity */
public class CommentOutDlg extends Dialog
{
	protected 	Activity 	m_context;
	protected	int			m_iPosX;
	protected	int			m_iPosY;
	
	public	CommentOutDlg( Activity context )
	{
		super(context);
		m_context 	=	context;
		m_iPosX		=	8;
		m_iPosY		=	10;
	}

}
