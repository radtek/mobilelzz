package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
public class RootViewList extends Activity 
implements ListActivityCtrl, View.OnClickListener
{
	public static String ExtraData_initListItemDBID		=	"com.main.ExtraData_RootList_initListItemDBID";
	private NoteListUICtrl m_NoteListUICtrl;
	private Calendar c;
	private CNoteDBCtrl		m_clCNoteDBCtrl;
	private View m_toolBarLayout;
	private int m_iContextMenu_DBID = CommonDefine.g_int_Invalid_ID;
	private boolean m_bIsCommnetDisplay_more = false;
	private boolean m_bIsCommnetDisplay_search = false;
//	public void onNewIntent(Intent intent){
//		setIntent(intent);
//	}
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//创建DB操作类
		if(CommonDefine.m_clCNoteDBCtrl==null){
			CommonDefine.m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
		}
        m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
//    	Cursor	curPassWord	=	m_clCNoteDBCtrl.getPassWord();
//        curPassWord.moveToFirst();
//		int count = curPassWord.getCount();
//    	if ( count > 0 )
//    	{	
//			int index = curPassWord.getColumnIndex(CNoteDBCtrl.KEY_password);
//			CommonDefine.g_str_PassWord = curPassWord.getString(index);					
//		}
//    	curPassWord.close();
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.rootviewlist);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        
        ListView list = (ListView) findViewById(R.id.rootviewlist_list);
        registerForContextMenu(list);
        View m_toolBarLayout = findViewById(R.id.rootviewlist_toolbar);
        m_NoteListUICtrl = new NoteListUICtrl(this, list, CMemoInfo.PreId_Root, m_toolBarLayout);
        m_NoteListUICtrl.initializeSource();
        
        ImageButton clBTMemoNewFolder = (ImageButton) findViewById(R.id.rootviewlist_toolbar_newfolder);
		clBTMemoNewFolder.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
        		PopUpNewFolderDlg(); 			
        	}
        });
		ImageButton clBTMemoNewNote = (ImageButton) findViewById(R.id.rootviewlist_toolbar_newnote);
        clBTMemoNewNote.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
        		Intent intent = new Intent(RootViewList.this, NoteWithYourMind.class);
        		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_New);
        		intent.putExtra(NoteWithYourMind.ExtraData_OperationPreID, CMemoInfo.PreId_Root);
        		startActivity(intent);
        	}
        });
        ImageButton clBTMemoMore = (ImageButton) findViewById(R.id.rootviewlist_toolbar_more);
        clBTMemoMore.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
				View vdlgback  = RootViewList.this.findViewById(R.id.toolbar_more_dlg);
				if(!m_bIsCommnetDisplay_more){
					m_bIsCommnetDisplay_more = true;
					vdlgback.setVisibility(View.VISIBLE);
					Animation anim = AnimationUtils.loadAnimation(RootViewList.this, R.anim.commentout);
					vdlgback.startAnimation(anim);
				}else{
					m_bIsCommnetDisplay_more=false;
					Animation anim = AnimationUtils.loadAnimation(RootViewList.this, R.anim.commenthide);
					vdlgback.startAnimation(anim);
					vdlgback.setVisibility(View.GONE);
				}
        	}
        });
        Button clBTMemoMore_delete = (Button) findViewById(R.id.toolbar_more_dlg_delete);
        clBTMemoMore_delete.setOnClickListener(this);
        
        ImageButton clBTMemoSearch = (ImageButton) findViewById(R.id.rootviewlist_toolbar_search);
        clBTMemoSearch.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
				View vdlgback  = RootViewList.this.findViewById(R.id.toolbar_search_dlg);
				if(!m_bIsCommnetDisplay_search){
					m_bIsCommnetDisplay_search = true;
					vdlgback.setVisibility(View.VISIBLE);
					Animation anim = AnimationUtils.loadAnimation(RootViewList.this, R.anim.commentdisplay_left_bottom);
					vdlgback.startAnimation(anim);
				}else{
					m_bIsCommnetDisplay_search=false;
					Animation anim = AnimationUtils.loadAnimation(RootViewList.this, R.anim.commenthide_left_bottom);
					vdlgback.startAnimation(anim);
					vdlgback.setVisibility(View.GONE);
				}
        	}
        });
        Button clBTMemoSearch_remind = (Button) findViewById(R.id.toolbar_search_dlg_remind);
        clBTMemoSearch_remind.setOnClickListener(this);
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
			int initListItemDBID = extra.getIntExtra(ExtraData_initListItemDBID, CommonDefine.g_int_Invalid_ID);
			if(initListItemDBID!=CommonDefine.g_int_Invalid_ID){
				m_NoteListUICtrl.updateListData(initListItemDBID);
			}else{
				
			}
		}else{
			
		}
	}
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.toolbar_more_dlg_delete:
			m_bIsCommnetDisplay_more=false;
			View vdlgback  = RootViewList.this.findViewById(R.id.toolbar_more_dlg);
	        Animation anim = AnimationUtils.loadAnimation(RootViewList.this, R.anim.commenthide);
	        vdlgback.startAnimation(anim);
	        vdlgback.setVisibility(View.GONE);
		    break;
		case R.id.toolbar_more_dlg_move:
			break;
		case R.id.toolbar_search_dlg_remind:
			m_bIsCommnetDisplay_search=false;
			View vdlgback1  = RootViewList.this.findViewById(R.id.toolbar_search_dlg);
	        Animation anim1 = AnimationUtils.loadAnimation(RootViewList.this, R.anim.commenthide_left_bottom);
	        vdlgback1.startAnimation(anim1);
	        vdlgback1.setVisibility(View.GONE);
			break;
		default:
		}
	}
	
	public void updateToolbar(CommonDefine.ToolbarStatusEnum enStatus){
		ImageButton btNewFolder = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_newfolder);
		ImageButton btNewNote = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_newnote);
		ImageButton btSearch = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_search);
		ImageButton btMore = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_more);
		if(enStatus == CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
			ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
			ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
			ImageButton btMove = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_move);
			btNewNote.setVisibility(View.VISIBLE);
			btSearch.setVisibility(View.VISIBLE);
			btNewFolder.setVisibility(View.VISIBLE);
			btMore.setVisibility(View.VISIBLE);
			btDelete.setVisibility(View.GONE);
			btMove.setVisibility(View.GONE);
			btCancel.setVisibility(View.GONE);
		}else{
			btNewNote.setVisibility(View.GONE);
			btSearch.setVisibility(View.GONE);
			btNewFolder.setVisibility(View.GONE);
			btMore.setVisibility(View.GONE);
		}
	}

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
	private void PopUpNewFolderDlg(){
		LayoutInflater factory = LayoutInflater.from(RootViewList.this);
		final View DialogView = factory.inflate(R.layout.dialognewfolder, null);
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
	        			CreateFolderRec(strFolderNameText);
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
	private void CreateFolderRec(String strDetail){
		CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;
		clCMemoInfo.iType	=	CMemoInfo.Type_Folder;
		c = Calendar.getInstance();
		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
		clCMemoInfo.strDetail	=	strDetail;
		clCMemoInfo.dCreateTime = c.getTimeInMillis();
		m_clCNoteDBCtrl.Create(clCMemoInfo); 
		m_NoteListUICtrl.updateListData(CommonDefine.g_int_Invalid_ID);
		
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu_listctrl, menu);
		return true;
	}
	
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.menu_delete: 
//				/*   menu button push action */ 
//			Intent intent = new Intent();
//    		intent.setClass(RootViewList.this, ListItemEdit.class); 
//    		intent.putExtra(ListItemEdit.g_ExtraDataName_ExitType, ListItemEdit.ListItemEditTypeEnum.ListItemEditType_delete);
//    		int tabIndex = m_TabHost.getCurrentTab();
//    		if(tabIndex==0){
//    			CommonDefine.g_listAdapter = m_memoListAdapter;
//    			CommonDefine.g_bIsRemind = false;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
//    			//intent.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
//    		}else if(tabIndex==1){
//    			CommonDefine.g_listAdapter = m_remindListAdapter;
//    			CommonDefine.g_bIsRemind = true;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
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
//    			CommonDefine.g_listAdapter = m_memoListAdapter;
//    			CommonDefine.g_bIsRemind = false;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
//    			//intent1.putExtra(ListItemEdit.g_ExtraDataName_ListCursor, m_memoListAdapter); 
//    		}else if(tabIndex1==1){
//    			CommonDefine.g_listAdapter = m_remindListAdapter;
//    			CommonDefine.g_bIsRemind = true;
//    			CommonDefine.g_preID = CMemoInfo.PreId_Root;
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
	public void onCreateContextMenu(ContextMenu menu, View v,  
            ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo); 

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		if(m_NoteListUICtrl.isFolder(((ListView)v).getChildAt(info.position))){
			menu.add(0, 0, 0, "修改名称");  
			if(m_NoteListUICtrl.getListIsEncode(((ListView)v).getChildAt(info.position))){
				menu.add(0, 1, 0, "取消查看锁"); 
			}else{
				menu.add(0, 1, 0, "设置查看锁"); 
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
		  default:  
		    return super.onContextItemSelected(item);  
		  }  
	}  

	private void ChangeFolderName(AdapterContextMenuInfo info){

		//	首先取得list上对应文件夹名称 设置dialog上文本框内显示旧文件夹名  
		Cursor cursorFolderList = null;	
		String oldfolderName =""; 
		ListView list = (ListView) findViewById(R.id.rootviewlist_list);
		m_iContextMenu_DBID = m_NoteListUICtrl.getListDBID(((ListView)list).getChildAt(info.position));
		
		cursorFolderList = m_clCNoteDBCtrl.getNoteRec(m_iContextMenu_DBID);
		if(cursorFolderList.getCount()>0){
			cursorFolderList.moveToFirst();
			int index = cursorFolderList.getColumnIndex(CNoteDBCtrl.KEY_detail);
			oldfolderName = cursorFolderList.getString(index);
		}
		cursorFolderList.close();

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
						m_clCNoteDBCtrl.Update(m_iContextMenu_DBID,clCMemoInfo);
						m_NoteListUICtrl.updateListData(CommonDefine.g_int_Invalid_ID);
						
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
		ListView list = (ListView) findViewById(R.id.rootviewlist_list);
		m_iContextMenu_DBID = m_NoteListUICtrl.getListDBID(((ListView)list).getChildAt(info.position));
		
		cursorFolderList = m_clCNoteDBCtrl.getNoteRec(m_iContextMenu_DBID);
       	startManagingCursor(cursorFolderList);
		if(cursorFolderList.getCount()>0){
			cursorFolderList.moveToFirst();
			int index = cursorFolderList.getColumnIndex(CNoteDBCtrl.KEY_isencode);
			iEncodeFlag = cursorFolderList.getInt(index);
		}
		cursorFolderList.close();
		// 弹出对应的dialog
		if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
			//弹出取消加密的dialog
			PopUpCancleFolderEncodeDlg();
		}else{
			//弹出设置加密的dialog
			PopUpSetFolderEncodeDlg();
		}
		
	}
	private void PopUpSetFolderEncodeDlg(){
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
						m_clCNoteDBCtrl.Update(m_iContextMenu_DBID,clCMemoInfo);
						m_NoteListUICtrl.updateListData(CommonDefine.g_int_Invalid_ID);
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

	private void PopUpCancleFolderEncodeDlg(){

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
			            		m_clCNoteDBCtrl.Update(m_iContextMenu_DBID,clCMemoInfo);     		
			            		m_NoteListUICtrl.updateListData(CommonDefine.g_int_Invalid_ID);
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
//
//	private void ChangeMemoToRemind(AdapterContextMenuInfo info){
//
//		String oldfolderName =""; 
//		m_iDBID = CommonDefine.g_int_Invalid_ID;
//
//		ListView memolist = (ListView) m_vMemoList.findViewById(R.id.RootViewListContent_List);
//		m_iDBID = m_memoListAdapter.getListDBID(((ListView)memolist).getChildAt(info.position));
//
//		// 弹出dialog
//		LayoutInflater factory = LayoutInflater.from(RootViewList.this);
//		final View DialogView = factory.inflate(R.layout.dateandtime, null);
//
//
//		c = Calendar.getInstance();	
//		c.getTimeInMillis();
//		final int Year=c.get(Calendar.YEAR);
//		final int Month=c.get(Calendar.MONTH);
//		final int Day=c.get(Calendar.DAY_OF_MONTH);
//
//		DatePicker clDP = (DatePicker) DialogView.findViewById(R.id.DatePicker01);
//		clDP.init(Year, Month, Day, new DatePicker.OnDateChangedListener(){
//		    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
//
//			}
//		});
//		
//		TimePicker clTP = (TimePicker) DialogView.findViewById(R.id.TimePicker01);
//		clTP.setIs24HourView(true);		
//		clTP.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
//		clTP.setCurrentMinute(c.get(Calendar.MINUTE));
//		clTP.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
//		      public void onTimeChanged(TimePicker view,int hourOfDay,int minute){
//
//		      }
//	    });
//		
//		
//		AlertDialog clDlg = new AlertDialog.Builder(RootViewList.this)	
//			.setIcon(R.drawable.clock)
//			.setTitle("设置提醒日期")
//			.setView(DialogView)
//			.setPositiveButton("确定",new DialogInterface.OnClickListener(){
//				public void onClick(DialogInterface dialog, int i)
//				{
//
//				}
//			})
//			.setNegativeButton("取消",new DialogInterface.OnClickListener(){
//				public void onClick(DialogInterface dialog, int i)
//				{
//					dialog.cancel();
//				}
//			})
//			.create();
//			clDlg.show();
//		
//	}

}
