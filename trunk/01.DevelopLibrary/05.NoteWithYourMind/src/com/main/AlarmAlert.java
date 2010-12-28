package com.main;

//package com.main;n
/* import相关class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;

/* 实际跳出闹铃Dialog的Activity */
public class AlarmAlert extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
		final MediaPlayer mp;
		final Vibrator vibrator;
    super.onCreate(savedInstanceState);
    /* 跳出的闹铃警示  */
    Intent iExtraData = getIntent();
    int id	=	iExtraData.getIntExtra("id", -1);
    
    CNoteDBCtrl	clCNoteDBCtrl	=	new	CNoteDBCtrl( AlarmAlert.this );
    Cursor clCursor	=	clCNoteDBCtrl.getNoteRec(id);
    if( !clCursor.moveToFirst() )
    {
		clCursor.close();
		return;
    }
    
	int	iColumn	=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_detail );
	String	strDetail		=	clCursor.getString(iColumn);
    
    mp =MediaPlayer.create(this, R.raw.bks);
    mp.start();
    vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); 
    long[] pattern = {800, 50, 400, 30};
    vibrator.vibrate(pattern, 2);
    
    new AlertDialog.Builder(AlarmAlert.this)
        //.setIcon(R.drawable.clock)
        .setTitle("闹钟响了!!")
        .setMessage("消息:" + strDetail )
        .setPositiveButton("关掉他",
         new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int whichButton)
          {
            /* 关闭Activity */
        	  mp.stop();
        	  mp.release();
        	  vibrator.cancel();
        	  AlarmAlert.this.finish();
          }
        })
        .show();
  } 
}
