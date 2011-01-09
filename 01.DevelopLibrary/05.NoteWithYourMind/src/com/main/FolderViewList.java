package com.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class FolderViewList extends Activity 
				implements ListActivityCtrl, View.OnClickListener
{
	public static String ExtraData_FolderDBID = "com.main.ExtraData_DBID";	
	public static String ExtraData_initListItemDBID		=	"com.main.ExtraData_RootList_initListItemDBID";
	private String m_strFolderName = "";
	private Integer m_iEncodeFlag = CMemoInfo.IsEncode_Invalid;
	private Integer m_iRemindFlag = CMemoInfo.IsRemind_Invalid;
	private int m_iFolder_DBID = CommonDefine.g_int_Invalid_ID;
	private NoteListUICtrl m_NoteListUICtrl;
	private View m_toolBarLayout;
	private boolean m_bIsListInit = false;
	private ListUICtrlParam  UICtrlParam;
	
	/** Called when the activity is first created. */
	public void onNewIntent(Intent intent){
		setIntent(intent);
	}
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.folderviewlist);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        ImageButton titleBack = (ImageButton) findViewById(R.id.custom_title_back);
        titleBack.setOnClickListener(this);
        
		Intent iExtraData = this.getIntent();
		m_iFolder_DBID = iExtraData.getIntExtra(ExtraData_FolderDBID, CMemoInfo.Id_Invalid);
		Cursor cursor = CommonDefine.getNoteDBCtrl(this).getNoteRec(m_iFolder_DBID);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex(CNoteDBCtrl.KEY_detail);
		m_strFolderName = cursor.getString(index);
		TextView tvTitleText = (TextView) findViewById(R.id.custom_title_text);
        tvTitleText.setText(m_strFolderName);
        cursor.close();
		
		ListView list = (ListView) findViewById(R.id.folderviewlist_list);
		m_toolBarLayout = findViewById(R.id.folderviewlist_toolbar);

		UICtrlParam = new ListUICtrlParam();
		UICtrlParam.g_enListType = ListUICtrlParam.ListTypeEnum.ListType_NormalList;
		UICtrlParam.g_int_PreID= m_iFolder_DBID;
		
        m_NoteListUICtrl = new NoteListUICtrl(this, list, m_toolBarLayout,UICtrlParam );
        m_NoteListUICtrl.initializeSource();
        m_bIsListInit = true;
        
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
	public void onClick(View view){

		switch(view.getId()){

		case R.id.custom_title_back:
			this.finish();
			break;
		default:
		}
	}
		
	public void onStop()
	{
		super.onStop();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if(CommonDefine.g_enToolbarStatus!=CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
            	m_NoteListUICtrl.processCancelClick(null);
                return true;	
        	}else{
        		return super.onKeyDown(keyCode, event); 
        	}
        } 
        return super.onKeyDown(keyCode, event); 
    }
	public void onResume()
	{
		super.onResume();
		Intent extra = getIntent();
		int initListItemDBID = CommonDefine.g_int_Invalid_ID;
		if(extra!=null){
			initListItemDBID = extra.getIntExtra(ExtraData_initListItemDBID, CommonDefine.g_int_Invalid_ID);
		}else{
		}
		if(m_bIsListInit){
			m_bIsListInit = false;
		}else{
			if(extra!=null){
				m_iFolder_DBID = extra.getIntExtra(ExtraData_FolderDBID, CMemoInfo.Id_Invalid);
				if(m_iFolder_DBID!=CommonDefine.g_int_Invalid_ID){
					Cursor cursor = CommonDefine.getNoteDBCtrl(this).getNoteRec(m_iFolder_DBID);
					cursor.moveToFirst();
					int index = cursor.getColumnIndex(CNoteDBCtrl.KEY_detail);
					m_strFolderName = cursor.getString(index);
					TextView tvTitleText = (TextView) findViewById(R.id.custom_title_text);
			        tvTitleText.setText(m_strFolderName);
			        cursor.close();
				}
			}
			
			m_NoteListUICtrl.updateListData(initListItemDBID);
		}
		setIntent(null);
	}
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	public void updateToolbar(CommonDefine.ToolbarStatusEnum enStatus){
		ImageButton btNewNote = (ImageButton)m_toolBarLayout.findViewById(R.id.folderviewlist_toolbar_newnote);
		CommonDefine.g_enToolbarStatus = enStatus;
		if(enStatus == CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
			ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
			ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
			ImageButton btMove = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_move);
			btNewNote.setVisibility(View.VISIBLE);
			btDelete.setVisibility(View.VISIBLE);
			btMove.setVisibility(View.VISIBLE);
			btCancel.setVisibility(View.GONE);
		}else{
			btNewNote.setVisibility(View.GONE);
		}
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
