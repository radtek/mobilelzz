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

	
	public static String ExtraData_initListItemDBID		=	"com.main.ExtraData_RootList_initListItemDBID";
	
	private NoteListUICtrl m_NoteListUICtrl;
	private Calendar c;
	private CNoteDBCtrl		m_clCNoteDBCtrl;
	private View m_toolBarLayout;
	private int m_iContextMenu_DBID = CommonDefine.g_int_Invalid_ID;
	private CommonContainer m_bIsCommnetDisplay_more;
	
	private View m_vMoreAnim = null;

	ListUICtrlParam m_SearchParam =null;

	
//	public void onNewIntent(Intent intent){
//		setIntent(intent);
//	}
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
     
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.searchresultviewlist);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        
        m_bIsCommnetDisplay_more = new CommonContainer();
        m_bIsCommnetDisplay_more.setBOOL(false);
        m_vMoreAnim  = SearchResultViewList.this.findViewById(R.id.toolbar_more_dlg);
        
	m_SearchParam = new ListUICtrlParam();
   	m_SearchParam.g_enListType = ListUICtrlParam.ListTypeEnum.ListType_SearchResultList;
    	m_SearchParam.g_bool_IsTextSearch = true;
	m_SearchParam.g_str_SearchKey = "";	
	m_SearchParam.g_enSortType= ListUICtrlParam.ListSortTypeEnum.SortType_Normal;	
		
		ListView list = (ListView) findViewById(R.id.rootviewlist_list);
        m_toolBarLayout = findViewById(R.id.searchresultviewlist_toolbar);
        m_NoteListUICtrl = new NoteListUICtrl(this, list, m_toolBarLayout, m_SearchParam);
        m_NoteListUICtrl.initializeSource();
        
        ImageButton clBTStartSearch = (ImageButton) findViewById(R.id.search_toolbar_btn_search);
        clBTStartSearch.setOnClickListener(this);        
        
        ImageButton clBTMemoMore = (ImageButton) findViewById(R.id.searchresultviewlist_toolbar_more);
        clBTMemoMore.setOnClickListener(this);
		
        Button clBTMemoMore_delete = (Button) findViewById(R.id.toolbar_more_dlg_delete);
        clBTMemoMore_delete.setOnClickListener(this);
        Button clBTMemoMore_move = (Button) findViewById(R.id.toolbar_more_dlg_move);
        clBTMemoMore_move.setOnClickListener(this);

        Button clBTSortByRemindFirst = (Button) findViewById(R.id.searchresultviewlist_toolbar_SortByRemindFirst);
        clBTSortByRemindFirst.setOnClickListener(this);

        Button clBTSortByVoiceFirst = (Button) findViewById(R.id.searchresultviewlist_toolbar_SortByVoiceFirst);
        clBTSortByVoiceFirst.setOnClickListener(this);

        Button clBTSortByTextFirst = (Button) findViewById(R.id.searchresultviewlist_toolbar_SortByTextFirst);
        clBTSortByTextFirst.setOnClickListener(this);
        
	}
	
	public void onStop()
	{
		super.onStop();
	}
	public void onResume()
	{
		super.onResume();
//		Intent extra = getIntent();
//		if(extra!=null){
//			int initListItemDBID = extra.getIntExtra(ExtraData_initListItemDBID, CommonDefine.g_int_Invalid_ID);
//			if(initListItemDBID!=CommonDefine.g_int_Invalid_ID){
//				m_NoteListUICtrl.updateListData(initListItemDBID);
//			}else{
				
//			}
//		}else{
			
//		}
	}
	public void onDestroy()
	{
		super.onDestroy();
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
	public void onClick(View view){
		switch(view.getId()){
		case R.id.searchresultviewlist_toolbar_SortByRemindFirst:
		case R.id.searchresultviewlist_toolbar_SortByVoiceFirst:
		case R.id.searchresultviewlist_toolbar_SortByTextFirst:	
		case R.id.search_toolbar_btn_search:	
			processSortClick(view);
			break;

		case R.id.searchresultviewlist_toolbar_more:
			executeAnimation(m_vMoreAnim, R.anim.commentout, R.anim.commenthide, m_bIsCommnetDisplay_more);
			break;
		case R.id.toolbar_more_dlg_delete:
			executeAnimation(m_vMoreAnim, R.anim.commentout, R.anim.commenthide, m_bIsCommnetDisplay_more);
			m_NoteListUICtrl.processDeleteClick(view);
		    break;
		case R.id.toolbar_more_dlg_move:
			executeAnimation(m_vMoreAnim, R.anim.commentout, R.anim.commenthide, m_bIsCommnetDisplay_more);
			m_NoteListUICtrl.processMoveClick(view);
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
	
	private void processSortClick(View view){

		EditText Keyword = (EditText) findViewById(R.id.search_toolbar_edittext);
		String strKeyword = Keyword.getText().toString();

		switch(view.getId()){
		case R.id.searchresultviewlist_toolbar_SortByRemindFirst:
			m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_RemindFirst;
			break;
		case R.id.searchresultviewlist_toolbar_SortByVoiceFirst:
			m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_VoiceFirst;
			break;
		case R.id.searchresultviewlist_toolbar_SortByTextFirst:
			m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_TextFirst;
			break;
		default:
			m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_Normal;
			break;
		}		
		m_SearchParam.g_enListType  = ListUICtrlParam.ListTypeEnum.ListType_SearchResultList;
		m_SearchParam.g_bool_IsTextSearch = true;
		m_SearchParam.g_str_SearchKey = strKeyword;
		
		m_NoteListUICtrl.SetSearchParam( m_SearchParam);
		m_NoteListUICtrl.updateListData(CommonDefine.g_int_Invalid_ID);

		

	}
	
	private void processNewNoteClick(View view){
		Intent intent = new Intent(SearchResultViewList.this, NoteWithYourMind.class);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_New);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationPreID, CMemoInfo.PreId_Root);
		startActivity(intent);
	}
	
	public void updateToolbar(CommonDefine.ToolbarStatusEnum enStatus){
		CommonDefine.g_enToolbarStatus = enStatus;
		Button btSortByRemindFirst = (Button)m_toolBarLayout.findViewById(R.id.searchresultviewlist_toolbar_SortByRemindFirst);
		ImageButton btMore = (ImageButton)m_toolBarLayout.findViewById(R.id.searchresultviewlist_toolbar_more);
		ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
		ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
		if(enStatus == CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
			btSortByRemindFirst.setVisibility(View.VISIBLE);
			btDelete.setVisibility(View.GONE);
			btCancel.setVisibility(View.GONE);
		}else{
//			btDelete.setVisibility(View.VISIBLE);
//			btMove.setVisibility(View.VISIBLE);
//			btCancel.setVisibility(View.VISIBLE);
			btSortByRemindFirst.setVisibility(View.GONE);
		}
	}

}
