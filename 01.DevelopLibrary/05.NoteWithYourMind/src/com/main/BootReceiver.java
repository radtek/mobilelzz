package com.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class BootReceiver extends BroadcastReceiver {    
    public void onReceive(Context ctx, Intent intent) { 
        //start activity    
    	AlarmManager alarmManager = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
    	Intent MyIntent = new Intent( ctx, AlarmReceiver.class );
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0,MyIntent, 0);
    	alarmManager.setRepeating(AlarmManager.RTC, 0, 60 * 1000, pendingIntent);
    }    
} 