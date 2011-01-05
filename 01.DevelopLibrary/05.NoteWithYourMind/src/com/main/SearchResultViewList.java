package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import android.widget.TextView;
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
	private	EditText m_ETKeyword ;
	
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
		TextView tvTitleText = (TextView) findViewById(R.id.custom_title_text);
        tvTitleText.setText("搜索便签");
        
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
		
        ImageButton clBTSortSetting = (ImageButton) findViewById(R.id.searchresultviewlist_toolbar_SortSetting);
        clBTSortSetting.setOnClickListener(this);

        ImageButton clBTMemoMore_delete = (ImageButton) findViewById(R.id.toolbar_delete);
        clBTMemoMore_delete.setOnClickListener(this);
        ImageButton clBTMemoMore_move = (ImageButton) findViewById(R.id.toolbar_move);
        clBTMemoMore_move.setOnClickListener(this);
        
        m_ETKeyword = (EditText) findViewById(R.id.search_toolbar_edittext);
        m_ETKeyword.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable s) {
           
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               
            }        	
        	
			public void onTextChanged(CharSequence s, int start, int before,  int count) {   
//				updateContacts(s.toString());   

				String strKeyword = m_ETKeyword.getText().toString();
				
				m_SearchParam.g_enListType  = ListUICtrlParam.ListTypeEnum.ListType_SearchResultList;
				m_SearchParam.g_bool_IsTextSearch = true;
				m_SearchParam.g_str_SearchKey = strKeyword;
				
				m_NoteListUICtrl.SetSearchParam( m_SearchParam);
				m_NoteListUICtrl.updateListData(CommonDefine.g_int_Invalid_ID);

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
		case R.id.toolbar_delete:
			m_NoteListUICtrl.processDeleteClick(view);
		    break;
		case R.id.toolbar_move:
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

			final String[] mySortTypes = {"提醒优先","语音优先","文本优先","最近修改优先"};

			//show folder to select rec--->
			LayoutInflater factory = LayoutInflater.from(this);
			final View DialogView = factory.inflate(R.layout.folderlist, null);
			 
			m_dlgSortTypeList = new AlertDialog.Builder(this)
				.setTitle("请选择排序方式")
				.setView(DialogView)
				.setNegativeButton("取消",new DialogInterface.OnClickListener(){
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
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_RemindFirst;
						break;
					case 1:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_VoiceFirst;
						break;
					case 2:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_TextFirst;
						break;
					case 3:
						m_SearchParam.g_enSortType = ListUICtrlParam.ListSortTypeEnum.SortType_Normal;
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
//	
//	
//	private void processNewNoteClick(View view){
//		Intent intent = new Intent(SearchResultViewList.this, NoteWithYourMind.class);
//		intent.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_New);
//		intent.putExtra(NoteWithYourMind.ExtraData_OperationPreID, CMemoInfo.PreId_Root);
//		startActivity(intent);
//	}
	
	public void updateToolbar(CommonDefine.ToolbarStatusEnum enStatus){
		ImageButton btSort = (ImageButton)m_toolBarLayout.findViewById(R.id.searchresultviewlist_toolbar_SortSetting);
		CommonDefine.g_enToolbarStatus = enStatus;
		if(enStatus == CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal){
			ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
			ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
			ImageButton btMove = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_move);
			btSort.setVisibility(View.VISIBLE);
			btDelete.setVisibility(View.VISIBLE);
			btMove.setVisibility(View.VISIBLE);
			btCancel.setVisibility(View.GONE);
		}else{
			btSort.setVisibility(View.GONE);
		}
	}

}
