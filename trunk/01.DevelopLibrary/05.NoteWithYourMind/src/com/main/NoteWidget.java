package com.main;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
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
		remoteViews.setTextViewText(R.id.widget_text, "HelloWorld");
		appWidgetManager.updateAppWidget(id, remoteViews);
	}
}
