package com.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;



public class BootReceiver extends BroadcastReceiver {    
    public void onReceive(Context ctx, Intent intent) { 
        //start activity    
//    	Toast toast = Toast.makeText(null, "收到启动通知", Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
//		toast.show();
//    	AlarmManager	alarmManager	=	(AlarmManager)ctx.getSystemService( Context.ALARM_SERVICE );
//    	Intent 			MyIntent		=	new Intent( ctx, AlarmReceiver.class );
//    	
//    	PendingIntent pendingIntent		=	PendingIntent.getBroadcast(ctx, 0,MyIntent, 0);
//    	alarmManager.setRepeating( AlarmManager.RTC, 0, 60 * 1000, pendingIntent );
    	CommonDefine.g_test = 2;
    }    
} 