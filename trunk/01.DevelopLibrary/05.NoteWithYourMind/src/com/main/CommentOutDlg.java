package com.main;

//package com.main;n
/* import���class */
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/* ʵ����������Dialog��Activity */
public class CommentOutDlg extends Dialog
{
	protected 	Context m_context;
	protected	int		iPosX;
	protected	int		iPosY;
	protected	Byte	m_bType;
	
	public CommentOutDlg(Context context){
		super(context);
		m_context 	=	context;
		iPosX		=	8;
		iPosY		=	10;
		m_bType		=	-1;
	}

}