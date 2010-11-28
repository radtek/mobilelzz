package com.main;

import android.app.Activity;
import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.view.Window;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import java.util.Calendar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import android.widget.TimePicker;
import android.widget.DatePicker;
public class RootViewList extends ActivityGroup
{
	public static String ExtraData_initTabID		=	"com.main.ExtraData_RootList_initTabID";
	//private NoteListUICtrl m_NoteListUICtrl;
	private Calendar c;
	TabHost m_TabHost;
	
	//进行DB操作的类
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	private NoteListCursorAdapter m_memoListAdapter;
	private NoteListCursorAdapter m_remindListAdapter;
	private int m_LastTabIndex = CommonDefine.g_int_Invalid_ID;
	private View  m_vMemoList;
	private View  m_vRemindList;	
	private int   m_iDBID = CommonDefine.g_int_Invalid_ID;	
	
	public void onNewIntent(Intent intent){
		setIntent(intent);
	}
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//创建DB操作类
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.view);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

		//取得TabHost对象
		m_TabHost = (TabHost)findViewById(android.R.id.tabhost);
		m_TabHost.setup(this.getLocalActivityManager());	
		TabWidget tw = m_TabHost.getTabWidget();
		TabSpec specmemo = m_TabHost.newTabSpec("1");
		specmemo.setIndicator(composeLayout("备忘",R.drawable.tabmemo));
		//specmemo.setIndicator("备忘");
		specmemo.setContent(R.id.memolist);
		m_TabHost.addTab(specmemo);
		
		TabSpec specremind = m_TabHost.newTabSpec("2");
		specremind.setIndicator(composeLayout("提醒",R.drawable.tabremind));
		//specremind.setIndicator("提醒");
		specremind.setContent(R.id.remindlist);
		m_TabHost.addTab(specremind);  
		
		TabSpec specNewNote = m_TabHost.newTabSpec("3");
		//specNewNote.setIndicator("新建便签");
		specNewNote.setIndicator(composeLayout("新建便签",R.drawable.tabnewnote));
//		Intent intent = new Intent();
//		intent.setClass(this, NoteWithYourMind.class); 
//		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InRoot); 

		specNewNote.setContent(/*intent*/R.id.newnote);
		m_TabHost.addTab(specNewNote);

//
		
        m_vMemoList = (View) findViewById(R.id.memolist);
        
        Button clBTMemoNewFolder = (Button) m_vMemoList.findViewById(R.id.RootViewListContent_NewFolder_B);
		clBTMemoNewFolder.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
        		PopUpNewFolderDlg(CMemoInfo.IsRemind_No); 			
        	}
        });
		m_vRemindList = (View) findViewById(R.id.remindlist);
        Button clBTMemoNewFolder2 = (Button) m_vRemindList.findViewById(R.id.RootViewListContent_NewFolder_B);
        clBTMemoNewFolder2.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
        		PopUpNewFolderDlg(CMemoInfo.IsRemind_Yes); 			
        	}
        });
        BindListViewData(m_vRemindList, CMemoInfo.IsRemind_Yes);
        BindListViewData(m_vMemoList, CMemoInfo.IsRemind_No);


		m_TabHost.setOnTabChangedListener(new OnTabChangeListener(){
	    	public void onTabChanged(String tabId){
	    		if(tabId.equals(String.valueOf(3))){
//	    			m_LastTabIndex = 2;
	    			Intent intent = new Intent();
	    			intent.setClass(RootViewList.this, NoteWithYourMind.class); 
	    			intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InRoot); 
	    			startActivity(intent);
//	    			m_TabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tabshape);
//	    			m_TabHost.getTabWidget().getChildAt(1).setBackgroundDrawable(null);
//	    			m_TabHost.getTabWidget().getChildAt(0).setBackgroundDrawable(null);
	    		}else if(tabId.equals(String.valueOf(2))){
	    			m_LastTabIndex = 1;
	    			m_TabHost.getTabWidget().getChildAt(1).findViewById(R.id.tabindicatorview)
	    			.setBackgroundResource(R.drawable.tabshape_selected);
	    			//m_TabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tabshape_normal);
	    			m_TabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabindicatorview)
	    			.setBackgroundResource(R.drawable.tabshape_normal);
					m_remindListAdapter.updateCursor();
					m_remindListAdapter.notifyDataSetChanged();

	    		}else if(tabId.equals(String.valueOf(1))){
	    			m_LastTabIndex = 0;
	    			m_TabHost.getTabWidget().getChildAt(0).findViewById(R.id.tabindicatorview)
	    			.setBackgroundResource(R.drawable.tabshape_selected);
	    			m_TabHost.getTabWidget().getChildAt(1).findViewById(R.id.tabindicatorview)
	    			.setBackgroundResource(R.drawable.tabshape_normal);
	    			//m_TabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tabshape_normal);
					m_memoListAdapter.updateCursor();
					m_memoListAdapter.notifyDataSetChanged();					

	    		}else{
	    			
	    		}
	    	}
	    });
		m_TabHost.setCurrentTab(1);
		m_TabHost.setCurrentTab(0);

		
        ListView MemoList = (ListView) m_vMemoList.findViewById(R.id.RootViewListContent_List);
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
					intent.setClass(RootViewList.this, FolderViewList.class);							
					intent.putExtra(FolderViewList.ExtraData_FolderDBID, iDBID);
					startActivity(intent);
				}else if(iType == CMemoInfo.Type_Memo){

					Intent intent = new Intent();
	        		intent.setClass( RootViewList.this, NoteWithYourMind.class);
					intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.EditNoteKind_InRoot); 
					intent.putExtra(NoteWithYourMind.ExtraData_MemoID,iDBID);	        		
	        		startActivity(intent);
				}else{
					
				}
			}
		});


        ListView RemindList = (ListView) m_vRemindList.findViewById(R.id.RootViewListContent_List);
		RemindList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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
					intent.setClass(RootViewList.this, FolderViewList.class);							
					intent.putExtra(FolderViewList.ExtraData_FolderDBID, iDBID);
					startActivity(intent);
				}else if(iType == CMemoInfo.Type_Memo){

					//
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
		Intent extra = getIntent();
		if(extra!=null){
			int initTabId = extra.getIntExtra(ExtraData_initTabID, CommonDefine.g_int_Invalid_ID);
			if(initTabId!=CommonDefine.g_int_Invalid_ID){
				m_TabHost.setCurrentTab(initTabId);
			}else{
				m_TabHost.setCurrentTab(m_LastTabIndex);
			}
			setIntent(null);
		}else{
			m_TabHost.setCurrentTab(m_LastTabIndex);
		}
	}
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	private void BindListViewData(View vParent, Integer bIsRemind){
		if(bIsRemind==CMemoInfo.IsRemind_Yes){
			Cursor rootRemindListCursor = m_clCNoteDBCtrl.getRemindsByID(CMemoInfo.PreId_Root);
			if(rootRemindListCursor!=null){
				startManagingCursor(rootRemindListCursor);
			}
			ListView remindlist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
			registerForContextMenu(remindlist); 
			m_remindListAdapter = new NoteListCursorAdapter(RootViewList.this, rootRemindListCursor);
			remindlist.setAdapter(m_remindListAdapter);
		}else{
			Cursor rootMemoListCursor = m_clCNoteDBCtrl.getMemosByID(CMemoInfo.PreId_Root);
			if(rootMemoListCursor!=null){
				startManagingCursor(rootMemoListCursor);
			}
			ListView memolist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
			registerForContextMenu(memolist); 
			m_memoListAdapter = new NoteListCursorAdapter(RootViewList.this, rootMemoListCursor);
			memolist.setAdapter(m_memoListAdapter);
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
	private void PopUpNewFolderDlg(Integer bIsRemind){
		LayoutInflater factory = LayoutInflater.from(RootViewList.this);
		final View DialogView = factory.inflate(R.layout.dialognewfolder, null);
		final Integer g_bIsRemind = bIsRemind;
		AlertDialog clDlgNewFolder = new AlertDialog.Builder(RootViewList.this)	
			.setIcon(R.drawable.clock)
			.setTitle("请输入文件夹名称")
			.setView(DialogView)
			.setPositiveButton("确定",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
	        		EditText FolderNameText = (EditText) DialogView.findViewById(R.id.foldername_edit);
	        		String strFolderNameText = FolderNameText.getText().toString();
	        		if(strFolderNameText.length()>0){
	        			CreateFolderRec(strFolderNameText, g_bIsRemind);
	            		dialog.cancel();
	        		}else{
	        			Toast toast = Toast.makeText(RootViewList.this, "请输入文件夹名称", Toast.LENGTH_SHORT);
	            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
	            		toast.show();
	        		}	
				}
			})
			.setNegativeButton("取消",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
					dialog.cancel();
				}
			})
			.create();
		clDlgNewFolder.show();    
	}
	private void CreateFolderRec(String strDetail, Integer bIsRemind){
		CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;
		clCMemoInfo.iType	=	CMemoInfo.Type_Folder;
		clCMemoInfo.iIsRemind	=	bIsRemind;
		c = Calendar.getInstance();
		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
		clCMemoInfo.strDetail	=	strDetail;
		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
		clCMemoInfo.iIsEncode = CMemoInfo.IsEncode_No;
		clCMemoInfo.strPassword = null;
		clCMemoInfo.dCreateTime = c.getTimeInMillis();
		m_clCNoteDBCtrl.Create(clCMemoInfo); 
		if(bIsRemind==CMemoInfo.IsRemind_Yes){
			m_remindListAdapter.updateCursor();
			m_remindListAdapter.notifyDataSetChanged();
		}else{
			m_memoListAdapter.updateCursor();
			m_memoListAdapter.notifyDataSetChanged();
		}
		
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
    		intent.setClass(RootViewList.this, ListItemEdit.class); 
    		intent.putExtra(ListItemEdit.g_ExtraDataName_ExitType, ListItemEdit.ListItemEditTypeEnum.ListItemEditType_delete);
    		int tabIndex = m_TabHost.getCurrentTab();
    		if(tabIndex==0){
    			CommonDefine.g_listAdapter = m_memoListAdapter;
    			CommonDefine.g_bIsRemind = false;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
    			//intent.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
    		}else if(tabIndex==1){
    			CommonDefine.g_listAdapter = m_remindListAdapter;
    			CommonDefine.g_bIsRemind = true;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
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
    			CommonDefine.g_listAdapter = m_memoListAdapter;
    			CommonDefine.g_bIsRemind = false;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
    			//intent1.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
    		}else if(tabIndex1==1){
    			CommonDefine.g_listAdapter = m_remindListAdapter;
    			CommonDefine.g_bIsRemind = true;
    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
    			//intent1.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_remindListAdapter); 
    		}else{
    			
    		}
    		startActivity(intent1);	
			break;

		}
		return true;
	}
	public void onCreateContextMenu(ContextMenu menu, View v,  
            ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo); 
		int tabIndex = m_TabHost.getCurrentTab();
		NoteListCursorAdapter listadapter = null;
		if(tabIndex==0){
			listadapter = m_memoListAdapter;

		}else if(tabIndex==1){
			listadapter = m_remindListAdapter;

		}else{
			
		}
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		if(listadapter.isFolder(((ListView)v).getChildAt(info.position))){
			menu.add(0, 0, 0, "修改名称");  
			if(listadapter.getListIsEncode(((ListView)v).getChildAt(info.position))){
				menu.add(0, 1, 0, "取消查看锁"); 
			}else{
				menu.add(0, 1, 0, "设置查看锁"); 
			}
		}else{
			if(tabIndex==0){
				menu.add(0, 3, 0, "转换为提醒便签"); 
			}else if(tabIndex==1){
				menu.add(0, 4, 0, "转换为普通便签"); 
			}else{

			}
		}
		
	}  
	public boolean onContextItemSelected(MenuItem item) {  
		  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();  
		  switch (item.getItemId()) {  
		  case 0:  
			ChangeFolderName(info);
		    return true;  
		  case 1:  
			ChangeFolderEncode(info);	
		    return true;  
		  case 3:  
			ChangeMemoToRemind(info);	
		    return true; 
		  case 4:  
//			ChangeRemindToMemo(info);	
		    return true; 
		  default:  
		    return super.onContextItemSelected(item);  
		  }  
	}  

	private void ChangeFolderName(AdapterContextMenuInfo info){

		//	首先取得list上对应文件夹名称 设置dialog上文本框内显示旧文件夹名  
		Cursor cursorFolderList = null;	
		String oldfolderName =""; 
		m_iDBID = CommonDefine.g_int_Invalid_ID;
		
		final int tabIndex = m_TabHost.getCurrentTab();
		NoteListCursorAdapter listadapter = null;
		if(tabIndex==0){
			listadapter = m_memoListAdapter;
			ListView memolist = (ListView) m_vMemoList.findViewById(R.id.RootViewListContent_List);
			m_iDBID = listadapter.getListDBID(((ListView)memolist).getChildAt(info.position));
		
		}else if(tabIndex==1){
			listadapter = m_remindListAdapter;
			ListView remindlist = (ListView) m_vRemindList.findViewById(R.id.RootViewListContent_List);
			m_iDBID = listadapter.getListDBID(((ListView)remindlist).getChildAt(info.position));

		}else{
			
		}
		
		cursorFolderList = m_clCNoteDBCtrl.getMemoRec(m_iDBID);
       	startManagingCursor(cursorFolderList);
		if(cursorFolderList.getCount()>0){
			cursorFolderList.moveToFirst();
			int index = cursorFolderList.getColumnIndex(CNoteDBCtrl.KEY_detail);
			oldfolderName = cursorFolderList.getString(index);
		}

		LayoutInflater factory = LayoutInflater.from(RootViewList.this);
		final View DialogView = factory.inflate(R.layout.dialognewfolder, null);
		final EditText FolderNameText = (EditText) DialogView.findViewById(R.id.foldername_edit);
		FolderNameText.setText(oldfolderName);

		// 弹出dialog

		AlertDialog clDlgChangeFolder = new AlertDialog.Builder(RootViewList.this)	
			.setIcon(R.drawable.clock)
			.setTitle("请输入新文件夹名称")
			.setView(DialogView)
			.setPositiveButton("确定",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
//	        		EditText FolderNameText = (EditText) DialogView.findViewById(R.id.foldername_edit);
	        		String strFolderNameNew = FolderNameText.getText().toString();
	        		if(strFolderNameNew.length()>0){
						CMemoInfo clCMemoInfo	=	new	CMemoInfo();
						clCMemoInfo.strDetail = strFolderNameNew;
					    c = Calendar.getInstance();
						clCMemoInfo.dLastModifyTime = c.getTimeInMillis();	
						m_clCNoteDBCtrl.Update(m_iDBID,clCMemoInfo);
						if(tabIndex==0){
							m_memoListAdapter.updateCursor();
							m_memoListAdapter.notifyDataSetChanged();					
						}else if(tabIndex==1){
							m_remindListAdapter.updateCursor();
							m_remindListAdapter.notifyDataSetChanged();
						}else{
							
						}
						
						dialog.cancel();
	        		}else{
	        			Toast toast = Toast.makeText(RootViewList.this, "请输入文件夹名称", Toast.LENGTH_SHORT);
	            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
	            		toast.show();
	        		}	
				}
			})
			.setNegativeButton("取消",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
					dialog.cancel();
				}
			})
			.create();
		clDlgChangeFolder.show();
		
	}

	private void ChangeFolderEncode(AdapterContextMenuInfo info){

		//	首先取得list上对应文件夹的加密现状:已加密?未加密?
		Cursor cursorFolderList = null;	
		int iEncodeFlag = CMemoInfo.IsEncode_No;
		m_iDBID = CommonDefine.g_int_Invalid_ID;
		
		final int tabIndex = m_TabHost.getCurrentTab();
		NoteListCursorAdapter listadapter = null;
		if(tabIndex==0){
			listadapter = m_memoListAdapter;
			ListView memolist = (ListView) m_vMemoList.findViewById(R.id.RootViewListContent_List);
			m_iDBID = listadapter.getListDBID(((ListView)memolist).getChildAt(info.position));
		
		}else if(tabIndex==1){
			listadapter = m_remindListAdapter;
			ListView remindlist = (ListView) m_vRemindList.findViewById(R.id.RootViewListContent_List);
			m_iDBID = listadapter.getListDBID(((ListView)remindlist).getChildAt(info.position));

		}else{
			
		}

		cursorFolderList = m_clCNoteDBCtrl.getMemoRec(m_iDBID);
       	startManagingCursor(cursorFolderList);
		if(cursorFolderList.getCount()>0){
			cursorFolderList.moveToFirst();
			int index = cursorFolderList.getColumnIndex(CNoteDBCtrl.KEY_isencode);
			iEncodeFlag = cursorFolderList.getInt(index);
		}
		// 弹出对应的dialog
		if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
			//弹出取消加密的dialog
			PopUpCancleFolderEncodeDlg(tabIndex);
		}else{
			//弹出设置加密的dialog
			PopUpSetFolderEncodeDlg( tabIndex );
		}
		
	}
	private void PopUpSetFolderEncodeDlg( final int tabIndex){
		AlertDialog clDlgChangeFolder = new AlertDialog.Builder(RootViewList.this)	
				.setIcon(R.drawable.clock)
				.setTitle("设置查看锁后需输入密码方可查看")
				.setPositiveButton("确定",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{

						CMemoInfo clCMemoInfo	=	new	CMemoInfo();
						clCMemoInfo.iIsEncode = CMemoInfo.IsEncode_Yes;						;
						c = Calendar.getInstance();
						clCMemoInfo.dLastModifyTime = c.getTimeInMillis();	
						m_clCNoteDBCtrl.Update(m_iDBID,clCMemoInfo);
						if(tabIndex==0){
							m_memoListAdapter.updateCursor();
							m_memoListAdapter.notifyDataSetChanged();					
						}else if(tabIndex==1){
							m_remindListAdapter.updateCursor();
							m_remindListAdapter.notifyDataSetChanged();
						}else{
							
						}
	        			Toast toast = Toast.makeText(RootViewList.this, "已设置查看锁", Toast.LENGTH_SHORT);
	            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
	            		toast.show();
						dialog.cancel();
					}
				})
				.setNegativeButton("取消",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
						dialog.cancel();
					}
				})
				.create();
			clDlgChangeFolder.show();
	}

	private void PopUpCancleFolderEncodeDlg( final int tabIndex){

		LayoutInflater factory = LayoutInflater.from(RootViewList.this);
		final View DialogView = factory.inflate(R.layout.dialog_encodesetting, null);

		AlertDialog clDlgChangeFolder = new AlertDialog.Builder(RootViewList.this)	
				.setIcon(R.drawable.clock)
				.setTitle("取消查看锁请输入密码")
				.setView(DialogView)
				.setPositiveButton("确定",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
						EditText PassWord = (EditText) DialogView.findViewById(R.id.ET_encodesetting);
						String strNewPassWord = PassWord.getText().toString();
		        		if(strNewPassWord.length()>0){
							if(strNewPassWord.equals(CommonDefine.g_str_PassWord)){
								CMemoInfo clCMemoInfo	=	new	CMemoInfo();
	 						    c = Calendar.getInstance();
								clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
			            		clCMemoInfo.iIsEncode	=	CMemoInfo.IsEncode_No;
			            		m_clCNoteDBCtrl.Update(m_iDBID,clCMemoInfo);     		
								if(tabIndex==0){
									m_memoListAdapter.updateCursor();
									m_memoListAdapter.notifyDataSetChanged();					
								}else if(tabIndex==1){
									m_remindListAdapter.updateCursor();
									m_remindListAdapter.notifyDataSetChanged();
								}else{
									
								}
			            		Toast toast = Toast.makeText(RootViewList.this, "已取消查看锁", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
							}else{
			        			Toast toast = Toast.makeText(RootViewList.this, "密码错误!请重新输入", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
							}
		        		}else{
		        			Toast toast = Toast.makeText(RootViewList.this, "请输入查看密码", Toast.LENGTH_SHORT);
		            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
		            		toast.show();
		        		}
						dialog.cancel();
					}
				})
				.setNegativeButton("取消",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
						dialog.cancel();
					}
				})
				.create();
			clDlgChangeFolder.show();
	}

	private void ChangeMemoToRemind(AdapterContextMenuInfo info){

		String oldfolderName =""; 
		m_iDBID = CommonDefine.g_int_Invalid_ID;

		ListView memolist = (ListView) m_vMemoList.findViewById(R.id.RootViewListContent_List);
		m_iDBID = m_memoListAdapter.getListDBID(((ListView)memolist).getChildAt(info.position));

		// 弹出dialog
		LayoutInflater factory = LayoutInflater.from(RootViewList.this);
		final View DialogView = factory.inflate(R.layout.dateandtime, null);


		c = Calendar.getInstance();	
		c.getTimeInMillis();
		final int Year=c.get(Calendar.YEAR);
		final int Month=c.get(Calendar.MONTH);
		final int Day=c.get(Calendar.DAY_OF_MONTH);

		DatePicker clDP = (DatePicker) DialogView.findViewById(R.id.DatePicker01);
		clDP.init(Year, Month, Day, new DatePicker.OnDateChangedListener(){
		    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){

			}
		});
		
		TimePicker clTP = (TimePicker) DialogView.findViewById(R.id.TimePicker01);
		clTP.setIs24HourView(true);		
		clTP.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		clTP.setCurrentMinute(c.get(Calendar.MINUTE));
		clTP.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
		      public void onTimeChanged(TimePicker view,int hourOfDay,int minute){

		      }
	    });
		
		
		AlertDialog clDlg = new AlertDialog.Builder(RootViewList.this)	
			.setIcon(R.drawable.clock)
			.setTitle("设置提醒日期")
			.setView(DialogView)
			.setPositiveButton("确定",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{

				}
			})
			.setNegativeButton("取消",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
					dialog.cancel();
				}
			})
			.create();
			clDlg.show();
		
	}

}
