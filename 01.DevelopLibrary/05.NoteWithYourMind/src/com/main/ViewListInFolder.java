package com.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
		
		TextView tvFolderName = (TextView)findViewById(R.id.memolisttitle_infolder);
		CNoteDBCtrl clCNoteDBCtrl = new CNoteDBCtrl(this);
		Cursor folderRec = clCNoteDBCtrl.getMemoRec(m_Cur_FolderID);
		if(folderRec.getCount()>0){
			folderRec.moveToFirst();
			int index = folderRec.getColumnIndex(CNoteDBCtrl.KEY_detail);
			String folderName = folderRec.getString(index);
			tvFolderName.setText(folderName);
		}
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
