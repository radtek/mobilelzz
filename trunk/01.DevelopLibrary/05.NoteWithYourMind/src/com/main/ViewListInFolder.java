package com.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class ViewListInFolder extends Activity
{
	public static String ExtraData_FolderID = "com.main.ExtraData_FolderID";
	private int m_Cur_FolderID = CommonDefine.g_int_Invalid_ID;
	private NoteListUICtrl m_NoteListUICtrl;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folderview);	
		Intent iExtraData = this.getIntent();
		m_Cur_FolderID = iExtraData.getIntExtra(ExtraData_FolderID, CommonDefine.g_int_Invalid_ID );
		ListView memoList = (ListView) findViewById(R.id.listviewmemo_infolder);
		LinearLayout toolbarLayout = (LinearLayout) findViewById(R.id.memolistmenu_infolder);
		m_NoteListUICtrl = new NoteListUICtrl(this, memoList, m_Cur_FolderID, toolbarLayout);
		m_NoteListUICtrl.initializeSource();
	}
	public void onStop()
	{
		super.onStop();
		//m_NoteListUICtrl.releaseSource();
		//m_NoteListUICtrl = null;
	}
	public void onDestroy()
	{
		super.onDestroy();
		m_NoteListUICtrl.releaseSource();
		m_NoteListUICtrl = null;
	}
}
