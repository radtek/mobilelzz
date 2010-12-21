package com.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



 
public class AlarmReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		int id	=	intent.getIntExtra("id", -1);
		
	    Intent iIntent = new Intent(context, AlarmAlert.class);    
	    iIntent.putExtra("id", id);
	    iIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity( iIntent );
	    
	    //¸üÐÂÌáÐÑ×´Ì¬
	    CRemindOperator	clCRemindOperator	=	  CRemindOperator.getInstance();  
	    clCRemindOperator.alarmAlert(context, id );
	}
}
