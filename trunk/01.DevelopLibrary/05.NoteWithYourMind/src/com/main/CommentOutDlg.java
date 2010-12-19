package com.main;

//package com.main;n
/* import相关class */
import android.app.Activity;
import android.app.Dialog;

/* 实际跳出闹铃Dialog的Activity */
public class CommentOutDlg extends Dialog
{
	protected 	Activity m_context;
	protected	int		iPosX;
	protected	int		iPosY;
	
	public CommentOutDlg(Activity context){
		super(context);
		m_context 	=	context;
		iPosX		=	8;
		iPosY		=	10;
	}

}
