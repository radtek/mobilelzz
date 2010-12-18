package com.main;

//package com.main;n
/* import相关class */
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/* 实际跳出闹铃Dialog的Activity */
public class CommentOutDlg extends Dialog
{
	private 	Context m_context;
	protected	int		iPosX;
	protected	int		iPosY;
	
	public CommentOutDlg(Context context){
		super(context);
		m_context 	=	context;
		iPosX		=	8;
		iPosY		=	10;
	}

}
