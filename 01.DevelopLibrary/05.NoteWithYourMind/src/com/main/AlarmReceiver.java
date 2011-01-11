package com.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



 
public class AlarmReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		int id	=	intent.getIntExtra(CommonDefine.ExtraData_EditNoteID, -1);
		if ( -1 == id )
		{
			return;
		}
		
	    //¸üÐÂÌáÐÑ×´Ì¬
	    CRemindOperator	clCRemindOperator	=	  CRemindOperator.getInstance(context);  
	    clCRemindOperator.alarmAlert(context, id );
		
	    Intent iIntent = new Intent(context, AlarmAlert.class);    
	    iIntent.putExtra(CommonDefine.ExtraData_EditNoteID, id);
	    iIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity( iIntent );
	}
}
