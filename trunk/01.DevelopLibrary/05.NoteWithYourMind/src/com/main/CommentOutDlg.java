package com.main;

//package com.main;n
/* import���class */
import android.app.Activity;
import android.app.Dialog;

/* ʵ����������Dialog��Activity */
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
