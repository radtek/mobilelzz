package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
public class SearchResultViewList extends Activity 
implements ListActivityCtrl, View.OnClickListener
{

	
	public static String ExtraData_initListItemDBID		=	"com.main.ExtraData_RootList_initListItemDBID";
	
	private NoteListUICtrl m_NoteListUICtrl;
	private Calendar c;
	private CNoteDBCtrl		m_clCNoteDBCtrl;
	private View m_toolBarLayout;
	private int m_iContextMenu_DBID = CommonDefine.g_int_Invalid_ID;

	ListUICtrlParam m_SearchParam =null;
	private AlertDialog m_dlgSortTypeList;
	
//	public void onNewIntent(Intent intent){
//		setIntent(intent);
//	}
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        m_clCNoteDBCtrl = CommonDefine.getNoteDBCtrl(this);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.searchresultviewlist);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        
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
		
        Button clBTSortSetting = (Button) findViewById(R.id.searchresultviewlist_toolbar_SortSetting);
        clBTSortSetting.setOnClickListener(this);

		Button clBTMemoMore_delete = (Button) findViewById(R.id.toolbar_more_dlg_delete);
        clBTMemoMore_delete.setOnClickListener(this);
        Button clBTMemoMore_move = (Button) findViewById(R.id.toolbar_more_dlg_move);
        clBTMemoMore_move.setOnClickListener(this);
        
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
		case R.id.searchresultviewlist_toolbar_SortSetting:
			processSortSettingClick(view);
		    break;
		case R.id.search_toolbar_btn_search:	
			processSortClick();
			break;
		case R.id.toolbar_more_dlg_delete:
			m_NoteListUICtrl.processDeleteClick(view);
		    break;
		case R.id.toolbar_more_dlg_move:
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
	
	private void processSortClick(){

		EditText Keyword = (EditText) findViewById(R.id.search_toolbar_edittext);
		String strKeyword = Keyword.getText().toString();
		
		m_SearchParam.g_enListType  = ListUICtrlParam.ListTypeEnum.ListType_SearchResultList;
		m_SearchParam.g_bool_IsTextSearch = true;
		m_SearchParam.g_str_SearchKey = strKeyword;
		
		m_NoteListUICtrl.SetSearchParam( m_SearchParam);
		m_NoteListUICtrl.updateListData(CommonDefine.g_int_Invalid_ID);

		

	}


	public void processSortSettingClick(View view){

			final String[] mySortTypes = {"����޸�����","��������","��������","�ı�����"};

			//show folder to select rec--->
			LayoutInflater factory = LayoutInflater.from(this);
			final View DialogView = factory.inflate(R.layout.folderlist, null);
			 
			m_dlgSortTypeList = new AlertDialog.Builder(this)
				.setTitle("��ѡ������ʽ")
				.setView(DialogView)
				.setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{			
						dialog.cancel();
					}
				})
				.create();  
			ListView SortTypeList = (ListView) DialogView.findViewById(R.id.folderlist_view);
			
    		ListAdapter LA_SortTypeList = new ArrayAdapter( this,android.R.layout.simple_list_item_1,mySortTypes );
    		SortTypeList.setAdapter(LA_SortTypeList);
			
			SortTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){

					switch(arg2){
					case 0:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_Normal;
						break;
					case 1:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_RemindFirst;
						break;
					case 2:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_VoiceFirst;
						break;
					case 3:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_TextFirst;
						break;
					default:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_Normal;
						break;
					}
					processSortClick();
					m_dlgSortTypeList.cancel();
				}
			});
			m_dlgSortTypeList.show();      			
		
	}
	
	
	private void processNewNoteClick(View view){
		Intent intent = new Intent(SearchResultViewList.this, NoteWithYourMind.class);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_New);
		intent.putExtra(NoteWithYourMind.ExtraData_OperationPreID, CMemoInfo.PreId_Root);
		startActivity(intent);
	}
	
	public void updateToolbar(CommonDefine.ToolbarStatusEnum enStatus){
		CommonDefine.g_enToolbarStatus = enStatus;
		ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
		ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
		if(enStatus == CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
			btDelete.setVisibility(View.GONE);
			btCancel.setVisibility(View.GONE);
		}else{
//			btDelete.setVisibility(View.VISIBLE);
//			btMove.setVisibility(View.VISIBLE);
//			btCancel.setVisibility(View.VISIBLE);
		}
	}

}
