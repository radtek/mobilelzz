package com.main;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class NoteWidget extends AppWidgetProvider
{
	public Context m_context;
	@Override
//	public void onReceive(Context context, Intent intent)
//	{
//
//	}
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds ){
		int id = appWidgetIds[0];
		int l = appWidgetIds.length;
		updateAppWidget(context, appWidgetManager, id);
		
	}
	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int id){
		RemoteViews  remoteViews = new RemoteViews(context.getPackageName(), R.layout.notewidget);
		//remoteViews.setTextViewText(R.id.widget_text, "HelloWorld");
		Intent intent = new Intent(context, RootViewList.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		PendingIntent pintent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.widget_B_Memo, pintent);
		appWidgetManager.updateAppWidget(id, remoteViews);
	}
}
