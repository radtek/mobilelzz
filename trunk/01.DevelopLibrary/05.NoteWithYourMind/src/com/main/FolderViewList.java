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

public class FolderViewList extends ActivityGroup
{
	public static String ExtraData_FolderDBID = "com.main.ExtraData_DBID";	
	private int m_Folder_DBID = CMemoInfo.Id_Invalid;

	private String m_strFolderName = "";
	private Integer m_iEncodeFlag = CMemoInfo.IsEncode_Invalid;
	private Integer m_iRemindFlag = CMemoInfo.IsRemind_Invalid;
	
	private CNoteDBCtrl m_clCNoteDBCtrl = RootViewList.m_clCNoteDBCtrl;
	private boolean m_isOnCreating = false;

	private View  m_vListInFolder;
	private TabHost m_TabHost;	

	private NoteListCursorAdapter m_ListAdapter;

	/** Called when the activity is first created. */
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.folderview);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        m_isOnCreating = true;
		Intent iExtraData = this.getIntent();

		m_Folder_DBID = iExtraData.getIntExtra(ExtraData_FolderDBID, CMemoInfo.Id_Invalid);
		
		Cursor Cursor = m_clCNoteDBCtrl.getMemoRec(m_Folder_DBID);		
       	startManagingCursor(Cursor);
		
		if(Cursor.getCount()>0){
			Cursor.moveToFirst();
			int index = Cursor.getColumnIndex(m_clCNoteDBCtrl.KEY_detail);
			m_strFolderName= Cursor.getString(index);

			index = Cursor.getColumnIndex(m_clCNoteDBCtrl.KEY_isencode);
			m_iEncodeFlag= Cursor.getInt(index);						

			index = Cursor.getColumnIndex(m_clCNoteDBCtrl.KEY_isremind);
			m_iRemindFlag= Cursor.getInt(index);	
		}		

		//取得TabHost对象
		m_TabHost = (TabHost)findViewById(android.R.id.tabhost);
		m_TabHost.setup(this.getLocalActivityManager());	
		TabWidget tw = m_TabHost.getTabWidget();
		TabSpec specmemo = m_TabHost.newTabSpec("0");
		if(m_iEncodeFlag==CMemoInfo.IsEncode_Yes)
		{
			specmemo.setIndicator(composeLayout(m_strFolderName,R.drawable.folderlocked));
		}else if(m_iEncodeFlag==CMemoInfo.IsEncode_No){
			specmemo.setIndicator(composeLayout(m_strFolderName,R.drawable.folder));
		}
		specmemo.setContent(R.id.MemoOrRemindlistInFolder);
		m_TabHost.addTab(specmemo);
		
		TabSpec specNewNote = m_TabHost.newTabSpec("1");
		specNewNote.setIndicator(composeLayout("新建便签",R.drawable.tabnewnote));
		
//		Intent intent = new Intent();
//		intent.setClass(this, NoteWithYourMind.class); 
//		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InFolder); 
//		intent.putExtra(NoteWithYourMind.ExtraData_MemoID,m_Folder_DBID ); 

		specNewNote.setContent(/*intent*/R.id.newnote);
		m_TabHost.addTab(specNewNote);

        m_vListInFolder = (View) findViewById(R.id.MemoOrRemindlistInFolder);

		//暂时不支持在文件夹下增加二级文件夹，删除新建文件夹button
        Button clBTMemoNewFolder = (Button) m_vListInFolder.findViewById(R.id.RootViewListContent_NewFolder_B);
		clBTMemoNewFolder.setVisibility(View.GONE);
		
        BindListViewData(m_vListInFolder, m_iRemindFlag);


		m_TabHost.setOnTabChangedListener(new OnTabChangeListener(){
	    	public void onTabChanged(String tabId){
	    		if(tabId.equals(String.valueOf(1))){
	    			if(!m_isOnCreating){
	    				Intent intent = new Intent();
		    			intent.setClass(FolderViewList.this, NoteWithYourMind.class); 
		    			intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InFolder); 
		    			intent.putExtra(NoteWithYourMind.ExtraData_MemoID,m_Folder_DBID ); 
		    			startActivity(intent);
	    			}
//	    			m_LastTabIndex = 1;
//	    			m_TabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tabshape);
//	    			m_TabHost.getTabWidget().getChildAt(0).setBackgroundDrawable(null);
//					m_ListAdapter.updateCursor();
//					m_ListAdapter.notifyDataSetChanged();	
	    		}else if(tabId.equals(String.valueOf(0))){
//	    			m_LastTabIndex = 0;
	    			m_TabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabindicatorview)
	    			.setBackgroundResource(R.drawable.tabshape_selected);
//	    			m_TabHost.getTabWidget().getChildAt(1).setBackgroundDrawable(null);
//					m_ListAdapter.updateCursor();
//					m_ListAdapter.notifyDataSetChanged();	
	    		}else{
	    			
	    		}
	    	}
	    });
		m_TabHost.setCurrentTab(1);
		m_TabHost.setCurrentTab(0);
		m_isOnCreating = false;
        ListView MemoList = (ListView) m_vListInFolder.findViewById(R.id.RootViewListContent_List);
		MemoList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				NoteListCursorAdapter LA = (NoteListCursorAdapter)arg0.getAdapter();
				Cursor cur = LA.getCursor();
				cur.moveToPosition(arg2);
				int iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_type);
				int iType = cur.getInt(iIndex);
				iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_id);
				int iDBID = cur.getInt(iIndex);
				if(iType == CMemoInfo.Type_Folder){
					Intent intent = new Intent();
					intent.setClass(FolderViewList.this, FolderViewList.class);							
					intent.putExtra(FolderViewList.ExtraData_FolderDBID, iDBID);
					startActivity(intent);
				}else if(iType == CMemoInfo.Type_Memo){
					Intent intent = new Intent();
	        		intent.setClass( FolderViewList.this, NoteWithYourMind.class);
					intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.EditNoteKind_InFolder); 
					intent.putExtra(NoteWithYourMind.ExtraData_MemoID,iDBID);	        		
	        		startActivity(intent);
				}else{
					
				}
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
		m_TabHost.setCurrentTab(0);
	}
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	private void BindListViewData(View vParent, Integer bIsRemind){
		if(bIsRemind==CMemoInfo.IsRemind_Yes){
			Cursor rootRemindListCursor = m_clCNoteDBCtrl.getRemindsByID(m_Folder_DBID);
			if(rootRemindListCursor!=null){
				startManagingCursor(rootRemindListCursor);
			}
			ListView remindlist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
			registerForContextMenu(remindlist); 
			m_ListAdapter = new NoteListCursorAdapter(FolderViewList.this, rootRemindListCursor);
			remindlist.setAdapter(m_ListAdapter);
		}else{
			Cursor rootMemoListCursor = m_clCNoteDBCtrl.getMemosByID(m_Folder_DBID);
			if(rootMemoListCursor!=null){
				startManagingCursor(rootMemoListCursor);
			}

			ListView memolist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
			registerForContextMenu(memolist); 
			m_ListAdapter = new NoteListCursorAdapter(FolderViewList.this, rootMemoListCursor);
			memolist.setAdapter(m_ListAdapter);
		}
	}
	
	private View composeLayout(String s, int i){
		LayoutInflater factory = LayoutInflater.from(this);
		LinearLayout layout = (LinearLayout)factory.inflate(R.layout.tabwidgetview, null, false); 
		TextView tv = (TextView)layout.findViewById(R.id.tabindicatorview_text);  
		tv.setText(s);  
		ImageView iv = (ImageView)layout.findViewById(R.id.tabindicatorview_image);  
		iv.setImageResource(i);  
		iv.setScaleType(ImageView.ScaleType.CENTER);
		return layout;  
	}  

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu_listctrl, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_delete: 
				/*   menu button push action */ 
			Intent intent = new Intent();
    		intent.setClass(FolderViewList.this, ListItemEdit.class); 
    		intent.putExtra(ListItemEdit.g_ExtraDataName_ExitType, ListItemEdit.ListItemEditTypeEnum.ListItemEditType_delete);
    		int tabIndex = m_TabHost.getCurrentTab();
    		if(tabIndex==0){
    			CommonDefine.g_listAdapter = m_ListAdapter;
    			CommonDefine.g_bIsRemind = false;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
    			//intent.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
    		}else if(tabIndex==1){
    			CommonDefine.g_listAdapter = m_ListAdapter;
    			CommonDefine.g_bIsRemind = true;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
    			//intent.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_remindListAdapter); 
    		}else{
    			
    		}
    		startActivity(intent);	
			break;
		case R.id.menu_move: 
				/*   menu button push action */ 
			Intent intent1 = new Intent();
			intent1.setClass(this, ListItemEdit.class); 
			intent1.putExtra(ListItemEdit.g_ExtraDataName_ExitType, ListItemEdit.ListItemEditTypeEnum.ListItemEditType_move);
    		int tabIndex1 = m_TabHost.getCurrentTab();
    		if(tabIndex1==0){
    			CommonDefine.g_listAdapter = m_ListAdapter;
    			CommonDefine.g_bIsRemind = false;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
    			//intent1.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
    		}else if(tabIndex1==1){
    			CommonDefine.g_listAdapter = m_ListAdapter;
    			CommonDefine.g_bIsRemind = true;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root+1;
    			//intent1.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_remindListAdapter); 
    		}else{
    			
    		}
    		startActivity(intent1);	
			break;

		}
		return true;
	}
	
}
