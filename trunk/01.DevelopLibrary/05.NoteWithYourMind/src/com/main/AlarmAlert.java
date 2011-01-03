package com.main;

//package com.main;n
/* import���class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;

/* ʵ����������Dialog��Activity */
public class AlarmAlert extends Activity implements MediaStatusControl
{

	MediaPlayer 	mp;
	Vibrator 		vibrator;
	  
	int			iIsRing;
	int			iIsVibrate;
	public void onDestroy()
	{
		super.onDestroy();
		CommonDefine.getMediaPhoneCallListener(this).unadvise(this);
	}
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		CommonDefine.getMediaPhoneCallListener(this).advise(this);
		/* ���������徯ʾ  */
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
		
		iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_isRing );
		iIsRing		=	clCursor.getInt(iColumn);
		
		iColumn		=	clCursor.getColumnIndex( CNoteDBCtrl.KEY_isVibrate );
		iIsVibrate	=	clCursor.getInt(iColumn);
		
		if( CMemoInfo.Ring_On == iIsRing )
		{
		    mp =MediaPlayer.create(this, R.raw.bks);
		    mp.start();
		}
		
		if ( CMemoInfo.Vibrate_On == iIsVibrate )
		{
		    vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); 
		    long[] pattern = {1000, 1000, 1000, 500};
		    vibrator.vibrate( pattern, 0 );
		}
		 
		new AlertDialog.Builder(AlarmAlert.this)
		    //.setIcon(R.drawable.clock)
		.setTitle("��������!!")
		.setMessage("��Ϣ:" + strDetail )
		.setPositiveButton("�ص���",
		new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
			/* �ر�Activity */
				  if( CMemoInfo.Ring_On == iIsRing )
				  {
			    	  mp.stop();
			    	  mp.release();	  
				  }
				  
				  if ( CMemoInfo.Vibrate_On == iIsVibrate )
				  {
					  vibrator.cancel();
				  }
				  
				  AlarmAlert.this.finish();
			  }
		})
		.show();
		
		clCursor.close();
	}
	
	@Override
	public void pauseMediaInteract() {
		// TODO Auto-generated method stub
		if( null != mp )
		{
			mp.pause();
			vibrator.cancel();
		}
	}
	
	@Override
	public void resumeMediaInteract() {
		// TODO Auto-generated method stub
		if( null != mp )
		{
		}
	} 
}
