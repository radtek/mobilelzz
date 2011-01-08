package com.main;

//package com.main;n
/* import相关class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

/* 实际跳出闹铃Dialog的Activity */
public class AlarmAlert extends Activity
{
 
	int			iIsRing;
	int			iIsVibrate;
	public void onDestroy()
	{
		super.onDestroy();
	}
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		/* 跳出的闹铃警示  */
		Intent iExtraData = getIntent();
		int id	=	iExtraData.getIntExtra(CommonDefine.ExtraData_EditNoteID, -1);
		
		CNoteDBCtrl	clCNoteDBCtrl	=	new	CNoteDBCtrl( AlarmAlert.this );
		Cursor clCursor	=	clCNoteDBCtrl.getNoteRec(id);
		if( !clCursor.moveToFirst() )
		{
			clCursor.close();
			return;
		}
		
		int	iColumn	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_detail );
		String	strDetail		=	clCursor.getString(iColumn);
		
		iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_isRing );
		iIsRing		=	clCursor.getInt(iColumn);
		
		iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_isVibrate );
		iIsVibrate	=	clCursor.getInt(iColumn);
		clCursor.close();
		CAlarmAlertDlg	clCAlarmAlertDlg	=	new	CAlarmAlertDlg( AlarmAlert.this);
		clCAlarmAlertDlg.setDisplay(strDetail, id, iIsRing, iIsVibrate );
//		new AlertDialog.Builder(AlarmAlert.this)
//		    //.setIcon(R.drawable.clock)
//		.setTitle("闹钟响了!!")
//		.setMessage("消息:" + strDetail )
//		.setPositiveButton("关掉他",
//		new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface dialog, int whichButton)
//			{
//			/* 关闭Activity */

//			  }
//		})
//		.show();
		
//		clCursor.close();
	}
	
 
}
