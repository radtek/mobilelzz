package com.main;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.TableLayout;
import android.view.Window;
import android.widget.Button;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FolderViewList extends Activity
{
	public static String ExtraData_FolderDBID = "com.main.ExtraData_DBID";	

	private String m_strFolderName = "";
	private Integer m_iEncodeFlag = CMemoInfo.IsEncode_Invalid;
	private Integer m_iRemindFlag = CMemoInfo.IsRemind_Invalid;
	private int m_iFolder_DBID = CommonDefine.g_int_Invalid_ID;
	private CNoteDBCtrl m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
	private NoteListUICtrl m_NoteListUICtrl;
	/** Called when the activity is first created. */
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.folderviewlist);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        
		Intent iExtraData = this.getIntent();
		m_iFolder_DBID = iExtraData.getIntExtra(ExtraData_FolderDBID, CMemoInfo.Id_Invalid);
		ListView list = (ListView) findViewById(R.id.folderviewlist_list);
        View toolbar = findViewById(R.id.folderviewlist_toolbar);
        m_NoteListUICtrl = new NoteListUICtrl(this, list, m_iFolder_DBID, toolbar);
        m_NoteListUICtrl.initializeSource();
        ImageButton clBTMemoNewNote = (ImageButton) findViewById(R.id.folderviewlist_toolbar_newnote);
        clBTMemoNewNote.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
        		Intent intent = new Intent(FolderViewList.this, NoteWithYourMind.class);
        		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_New);
        		intent.putExtra(NoteWithYourMind.ExtraData_OperationPreID, m_iFolder_DBID);
        		startActivity(intent);
        	}
        });
	}

	public void onStop()
	{
		super.onStop();
	}
	public void onResume()
	{
		super.onResume();
	}
	public void onDestroy()
	{
		super.onDestroy();
	}
	
//	private void BindListViewData(View vParent, Integer bIsRemind){
//		if(bIsRemind==CMemoInfo.IsRemind_Yes){
//			Cursor rootRemindListCursor = m_clCNoteDBCtrl.getRemindsByID(m_Folder_DBID);
//			if(rootRemindListCursor!=null){
//				startManagingCursor(rootRemindListCursor);
//			}
//			ListView remindlist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
//			registerForContextMenu(remindlist); 
//			m_ListAdapter = new NoteListCursorAdapter(FolderViewList.this, rootRemindListCursor);
//			remindlist.setAdapter(m_ListAdapter);
//		}else{
//			Cursor rootMemoListCursor = m_clCNoteDBCtrl.getMemosByID(m_Folder_DBID);
//			if(rootMemoListCursor!=null){
//				startManagingCursor(rootMemoListCursor);
//			}
//
//			ListView memolist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
//			registerForContextMenu(memolist); 
//			m_ListAdapter = new NoteListCursorAdapter(FolderViewList.this, rootMemoListCursor);
//			memolist.setAdapter(m_ListAdapter);
//		}
//	}
//	
//	private View composeLayout(String s, int i){
//		LayoutInflater factory = LayoutInflater.from(this);
//		LinearLayout layout = (LinearLayout)factory.inflate(R.layout.tabwidgetview, null, false); 
//		TextView tv = (TextView)layout.findViewById(R.id.tabindicatorview_text);  
//		tv.setText(s);  
//		ImageView iv = (ImageView)layout.findViewById(R.id.tabindicatorview_image);  
//		iv.setImageResource(i);  
//		iv.setScaleType(ImageView.ScaleType.CENTER);
//		return layout;  
//	}  
//
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inf = getMenuInflater();
//		inf.inflate(R.menu.menu_listctrl, menu);
//		return true;
//	}
//	
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.menu_delete: 
//				/*   menu button push action */ 
//			Intent intent = new Intent();
//    		intent.setClass(FolderViewList.this, ListItemEdit.class); 
//    		intent.putExtra(ListItemEdit.g_ExtraDataName_ExitType, ListItemEdit.ListItemEditTypeEnum.ListItemEditType_delete);
//    		int tabIndex = m_TabHost.getCurrentTab();
//    		if(tabIndex==0){
//    			CommonDefine.g_listAdapter = m_ListAdapter;
//    			CommonDefine.g_bIsRemind = false;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
//    			//intent.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
//    		}else if(tabIndex==1){
//    			CommonDefine.g_listAdapter = m_ListAdapter;
//    			CommonDefine.g_bIsRemind = true;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
//    			//intent.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_remindListAdapter); 
//    		}else{
//    			
//    		}
//    		startActivity(intent);	
//			break;
//		case R.id.menu_move: 
//				/*   menu button push action */ 
//			Intent intent1 = new Intent();
//			intent1.setClass(this, ListItemEdit.class); 
//			intent1.putExtra(ListItemEdit.g_ExtraDataName_ExitType, ListItemEdit.ListItemEditTypeEnum.ListItemEditType_move);
//    		int tabIndex1 = m_TabHost.getCurrentTab();
//    		if(tabIndex1==0){
//    			CommonDefine.g_listAdapter = m_ListAdapter;
//    			CommonDefine.g_bIsRemind = false;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
//    			//intent1.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
//    		}else if(tabIndex1==1){
//    			CommonDefine.g_listAdapter = m_ListAdapter;
//    			CommonDefine.g_bIsRemind = true;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
//    			//intent1.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_remindListAdapter); 
//    		}else{
//    			
//    		}
//    		startActivity(intent1);	
//			break;
//
//		}
//		return true;
//	}
	
}
