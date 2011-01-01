package com.main;

//package com.main;n
/* import相关class */
import android.app.Activity;
import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;

/* 实际跳出闹铃Dialog的Activity */
public class CommentOutDlg extends Dialog
{
	protected 	Activity 	m_context;
	protected	int			m_iPosX;
	protected	int			m_iPosY;
	
	protected	int			m_iType;
	
	public	CommentOutDlg( Activity context )
	{
		super(context);
		m_context 	=	context;
		m_iPosX		=	8;
		m_iPosY		=	10;
		
		m_iType		=	CommonDefine.Remind_Type_Invalid;
	}
	
	public	void	hideView( int id )
	{
		View	vw	=	 m_context.findViewById(id);
		if( null != vw )
		{
			vw.setVisibility(View.GONE);
		}	
	}
	
	public	void	showView( int id )
	{
		View	vw	=	 m_context.findViewById(id);
		if( null != vw )
		{
			vw.setVisibility(View.VISIBLE);
		}	
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			RemindActivity.m_iType	=	m_iType;			
			return true;
		}
		
		return super.onKeyDown(keyCode, event); 
	}

}
