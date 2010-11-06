package com.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.TableLayout;
import android.view.Window;
import android.widget.Button;
import android.view.View;

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
	
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.folderview);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        Button btSkin = (Button)findViewById(R.id.B_main_setting_skin);
		Button btEncode = (Button)findViewById(R.id.B_main_setting_encode);
		btSkin.setVisibility(View.GONE);
		btEncode.setVisibility(View.GONE);
        Button btNewFolder = (Button)findViewById(R.id.B_main_NewFolder);
		btNewFolder.setVisibility(View.GONE);

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
		ListView memoList = (ListView) findViewById(R.id.listviewmemo);
		LinearLayout toolbarLayout = (LinearLayout) findViewById(R.id.memolistmenu);
		m_NoteListUICtrl = new NoteListUICtrl(this, memoList, m_Cur_FolderID, toolbarLayout);
		m_NoteListUICtrl.initializeSource();

		Button clBTNewMemo = (Button) findViewById(R.id.B_main_NewMemo);
        clBTNewMemo.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(ViewListInFolder.this, NoteWithYourMind.class); 
        		intent.putExtra(NoteWithYourMind.ExtraData_MemoID, m_Cur_FolderID);
        		intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InFolder);
        		startActivity(intent);
        	}
        });
		
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
