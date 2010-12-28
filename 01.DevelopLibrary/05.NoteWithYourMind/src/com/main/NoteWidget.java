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
		intent.putExtra(RootViewList.ExtraData_initListItemDBID, CommonDefine.g_int_Invalid_ID);
		PendingIntent pintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.widget_B_Memo, pintent);
		
//		Intent intentRemind = new Intent(context, RootViewList.class);
//		intentRemind.putExtra(RootViewList.ExtraData_initListItemDBID, CommonDefine.g_int_Invalid_ID);
//		PendingIntent pintentRemind = PendingIntent.getActivity(context, 1, intentRemind, PendingIntent.FLAG_UPDATE_CURRENT);
//		remoteViews.setOnClickPendingIntent(R.id.widget_B_Remind, pintentRemind);
//		
		Intent intentNewNote = new Intent(context, NoteWithYourMind.class);
		intentNewNote.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_New);
		intentNewNote.putExtra(NoteWithYourMind.ExtraData_OperationPreID, CMemoInfo.PreId_Root);
		PendingIntent pintentNewNote = PendingIntent.getActivity(context, 2, intentNewNote, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.widget_B_NewNote, pintentNewNote);
		
		appWidgetManager.updateAppWidget(id, remoteViews);
	}
}
