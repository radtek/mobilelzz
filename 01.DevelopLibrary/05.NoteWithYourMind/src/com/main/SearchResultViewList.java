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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
public class SearchResultViewList extends Activity 
implements ListActivityCtrl, View.OnClickListener
{

	public	enum	SearchKindEnum
	{
		SearchKind_Remind,
		SearchKind_Voice,
		SearchKind_DeepSearch
	}
	
	public static String ExtraData_SearchKind		=	"com.main.ExtraData_SearchResultList_SearchKind";
	public static String ExtraData_initListItemDBID		=	"com.main.ExtraData_RootList_initListItemDBID";
	
	private ListUICtrlParam  m_ExtraData_SearchParam;
	private NoteListUICtrl m_NoteListUICtrl;
	private Calendar c;
	private CNoteDBCtrl		m_clCNoteDBCtrl;
	private View m_toolBarLayout;
	private int m_iContextMenu_DBID = CommonDefine.g_int_Invalid_ID;
	private CommonContainer m_bIsCommnetDisplay_more;
	private CommonContainer m_bIsCommnetDisplay_search;
	
	private View m_vSearchAnim = null;
	private View m_vMoreAnim = null;
//	public void onNewIntent(Intent intent){
//		setIntent(intent);
//	}
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
     
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.rootviewlist);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        
        m_bIsCommnetDisplay_more = new CommonContainer();
        m_bIsCommnetDisplay_more.setBOOL(false);
        m_bIsCommnetDisplay_search = new CommonContainer();
        m_bIsCommnetDisplay_search.setBOOL(false);
        m_vSearchAnim  = SearchResultViewList.this.findViewById(R.id.toolbar_search_dlg);
        m_vMoreAnim  = SearchResultViewList.this.findViewById(R.id.toolbar_more_dlg);
        
    	Intent iExtraData = getIntent();
    	SearchKindEnum SearchKind =	(SearchKindEnum)iExtraData.getSerializableExtra(ExtraData_SearchKind);
   		m_ExtraData_SearchParam.g_enListType = ListUICtrlParam.ListTypeEnum.ListType_SearchResultList;
    	m_ExtraData_SearchParam.g_bool_IsRemindSearch = true;
		
		ListView list = (ListView) findViewById(R.id.rootviewlist_list);
        m_toolBarLayout = findViewById(R.id.rootviewlist_toolbar);
        m_NoteListUICtrl = new NoteListUICtrl(this, list, m_toolBarLayout, m_ExtraData_SearchParam);
        m_NoteListUICtrl.initializeSource();
        
        ImageButton clBTMemoNewFolder = (ImageButton) findViewById(R.id.rootviewlist_toolbar_newfolder);
		clBTMemoNewFolder.setOnClickListener(this);
		
		ImageButton clBTMemoNewNote = (ImageButton) findViewById(R.id.rootviewlist_toolbar_newnote);
        clBTMemoNewNote.setOnClickListener(this);
        
        ImageButton clBTMemoMore = (ImageButton) findViewById(R.id.rootviewlist_toolbar_more);
        clBTMemoMore.setOnClickListener(this);
        Button clBTMemoMore_delete = (Button) findViewById(R.id.toolbar_more_dlg_delete);
        clBTMemoMore_delete.setOnClickListener(this);
        Button clBTMemoMore_move = (Button) findViewById(R.id.toolbar_more_dlg_move);
        clBTMemoMore_move.setOnClickListener(this);
        Button clBTMemoMore_SetPassword = (Button) findViewById(R.id.toolbar_more_dlg_setpassword);
        clBTMemoMore_SetPassword.setOnClickListener(this);
        
        ImageButton clBTMemoSearch = (ImageButton) findViewById(R.id.rootviewlist_toolbar_search);
        clBTMemoSearch.setOnClickListener(this);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (CommonDefine.m_iBackCount == 0)) { 
        	if(CommonDefine.g_enToolbarStatus!=CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
        		CommonDefine.m_iBackCount++;
            	m_NoteListUICtrl.processCancelClick(null);
                return true;	
        	}else{
        		CommonDefine.m_iBackCount=0;
        		return super.onKeyDown(keyCode, event); 
        	}
        } 
    	CommonDefine.m_iBackCount=0;
        return super.onKeyDown(keyCode, event); 
    }
	public void onClick(View view){
		switch(view.getId()){
		case R.id.toolbar_more_dlg_delete:
			executeAnimation(m_vMoreAnim, R.anim.commentout, R.anim.commenthide, m_bIsCommnetDisplay_more);
			m_NoteListUICtrl.processDeleteClick(view);
		    break;
		case R.id.toolbar_more_dlg_move:
			executeAnimation(m_vMoreAnim, R.anim.commentout, R.anim.commenthide, m_bIsCommnetDisplay_more);
			m_NoteListUICtrl.processMoveClick(view);
			break;
		case R.id.toolbar_search_dlg_remind:
			executeAnimation(m_vSearchAnim, R.anim.commentdisplay_left_bottom, R.anim.commenthide_left_bottom, m_bIsCommnetDisplay_search);
			processSearchClick(view);
			break;
		case R.id.rootviewlist_toolbar_newfolder:
			processNewFolderClick(view);
			break;
		case R.id.rootviewlist_toolbar_newnote:
			processNewNoteClick(view);
			break;
		case R.id.rootviewlist_toolbar_more:
			executeAnimation(m_vMoreAnim, R.anim.commentout, R.anim.commenthide, m_bIsCommnetDisplay_more);
			break;
		case R.id.rootviewlist_toolbar_search:
			executeAnimation(m_vSearchAnim, R.anim.commentdisplay_left_bottom, R.anim.commenthide_left_bottom, m_bIsCommnetDisplay_search);
			break;
		case R.id.toolbar_more_dlg_setpassword:
			executeAnimation(m_vMoreAnim, R.anim.commentout, R.anim.commenthide, m_bIsCommnetDisplay_more);
//			EncodeSettingDlg();
			break;
		default:
		}
	}

	private void executeAnimation(View view2BeExe, int iDisplayAnimID, int iHideAnimID, CommonContainer bIsAnimExecuting ){
		if(!bIsAnimExecuting.getBOOL()){
			bIsAnimExecuting.setBOOL(true);
			view2BeExe.setVisibility(View.VISIBLE);
			Animation anim = AnimationUtils.loadAnimation(SearchResultViewList.this, iDisplayAnimID);
			view2BeExe.startAnimation(anim);
		}else{
			bIsAnimExecuting.setBOOL(false);
			Animation anim = AnimationUtils.loadAnimation(SearchResultViewList.this, iHideAnimID);
			view2BeExe.startAnimation(anim);
			view2BeExe.setVisibility(View.GONE);
		}
	}
	
	private void processSearchClick(View view){

		Intent intent = new Intent(SearchResultViewList.this, SearchResultViewList.class);

		startActivity(intent);

	}
	
	private void processMoreClick(View view){

	}
	
	private void processNewNoteClick(View view){
		Intent intent = new Intent(SearchResultViewList.this, NoteWithYourMind.class);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_New);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationPreID, CMemoInfo.PreId_Root);
		startActivity(intent);
	}
	
	private void processNewFolderClick(View view){
		PopUpNewFolderDlg();
	}
	
	public void updateToolbar(CommonDefine.ToolbarStatusEnum enStatus){
		ImageButton btNewFolder = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_newfolder);
		ImageButton btNewNote = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_newnote);
		ImageButton btSearch = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_search);
		ImageButton btMore = (ImageButton)m_toolBarLayout.findViewById(R.id.rootviewlist_toolbar_more);
		ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
		ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
		ImageButton btMove = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_move);
		if(enStatus == CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
			btNewNote.setVisibility(View.VISIBLE);
			btSearch.setVisibility(View.VISIBLE);
			btNewFolder.setVisibility(View.VISIBLE);
			btMore.setVisibility(View.VISIBLE);
			btDelete.setVisibility(View.GONE);
			btMove.setVisibility(View.GONE);
			btCancel.setVisibility(View.GONE);
		}else{
//			btDelete.setVisibility(View.VISIBLE);
//			btMove.setVisibility(View.VISIBLE);
//			btCancel.setVisibility(View.VISIBLE);
			btNewNote.setVisibility(View.GONE);
			btSearch.setVisibility(View.GONE);
			btNewFolder.setVisibility(View.GONE);
			btMore.setVisibility(View.GONE);
		}
	}


	private void PopUpNewFolderDlg(){
		LayoutInflater factory = LayoutInflater.from(SearchResultViewList.this);
		final View DialogView = factory.inflate(R.layout.dialognewfolder, null);
		AlertDialog clDlgNewFolder = new AlertDialog.Builder(SearchResultViewList.this)	
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
	        			Toast toast = Toast.makeText(SearchResultViewList.this, "请输入文件夹名称", Toast.LENGTH_SHORT);
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

}
