
package com.main;

import java.util.ArrayList;
import java.util.Calendar;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.opengl.Visibility;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
 

class NoteListUICtrl  implements View.OnClickListener, AdapterView.OnItemClickListener{
	private enum MoveIn_State{
		MoveIn_Invalid,
		MoveIn_SelectMoveItem,
		MoveIn_SelectFolder
	}
	
	private CNoteDBCtrl m_clCNoteDBCtrl = null;
	private boolean m_bIsDelete = false;
	private MoveIn_State m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
	
	private AlertDialog m_dlgFolderList;
	private AlertDialog m_dlgSortTypeList;	
	public ListView m_targetList;
	private ListUICtrlParam m_ListUICtrlParam;
	private View m_toolBarLayout;
	public Activity m_sourceManager;
	private NoteListCursorAdapter m_myAdapter;
	private NoteListArrayAdapter  m_myArrayListAdapter;
	
	//private String m_strPassWord;

	NoteListUICtrl(Activity sourceManager, ListView target, View toolBarLayout,ListUICtrlParam CtrlParam){
		m_sourceManager = sourceManager;
		m_targetList = target;
		m_ListUICtrlParam = CtrlParam;
		m_toolBarLayout = toolBarLayout;
		m_myAdapter = null;
		m_myArrayListAdapter = null;
		m_clCNoteDBCtrl = CommonDefine.getNoteDBCtrl(sourceManager);
	}
	
	public void releaseSource(){
		m_sourceManager = null;
		m_targetList = null;
		m_toolBarLayout = null;
		m_myAdapter = null;
		m_myArrayListAdapter = null;
		m_dlgFolderList = null;
		m_dlgSortTypeList = null;
	}
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.toolbar_delete:
			processDeleteClick(view);
		    break;
		case R.id.toolbar_move:
			processMoveClick(view);
			break;
		case R.id.toolbar_cancel:
			processCancelClick(view);
			break;
		default:
		}
	}
	
	public void processDeleteClick(View view){
		if(!m_bIsDelete)
		{
			m_bIsDelete = true;
			updateToolBar();
			updateListData(CommonDefine.g_int_Invalid_ID);
		}	
		else
		{
			//delete rec--->
			ArrayList<DetailInfoOfSelectItem> alIDs = new ArrayList<DetailInfoOfSelectItem>();
			if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_NormalList){

				m_myAdapter.getSelectItemDBID(alIDs);
			}
			else if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_SearchResultList){
				m_myArrayListAdapter.getSelectItemDBID(alIDs);
				m_myArrayListAdapter = null;
			}

			if(alIDs.size()>0){
    			boolean b = m_clCNoteDBCtrl.Delete(alIDs);
    			if(!b){
    				Toast toast = Toast.makeText(m_sourceManager, "部分记录删除失败\n请确认SD卡是否可用", Toast.LENGTH_LONG);
    				toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
    			}
				CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance(m_sourceManager);
				clCRemindOperator.disableRemind( m_sourceManager, alIDs );
			}
			Return2TargetList();
			updateListData(CommonDefine.g_int_Invalid_ID);

			

		}
	}
	
	public void processMoveClick(View view){
		if(m_MoveIn_State == MoveIn_State.MoveIn_Invalid)
		{
			m_MoveIn_State = MoveIn_State.MoveIn_SelectMoveItem;
			//update toolbar
			updateToolBar();
			updateListData(CommonDefine.g_int_Invalid_ID);
		}	
		else if(m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem)
		{
			m_MoveIn_State = MoveIn_State.MoveIn_SelectFolder;
			ArrayList<DetailInfoOfSelectItem> alIDs = new ArrayList<DetailInfoOfSelectItem>();
			if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_NormalList){
				m_myAdapter.getSelectItemDBID(alIDs);
			}
			else if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_SearchResultList){

				m_myArrayListAdapter.getSelectItemDBID(alIDs);
			}

			if(alIDs.size()<=0){
				m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
				Return2TargetList();
				updateListData(CommonDefine.g_int_Invalid_ID);
        		return;
			}
			//show folder to select rec--->
			LayoutInflater factory = LayoutInflater.from(m_sourceManager);
			final View DialogView = factory.inflate(R.layout.folderlist, null);
			
			m_dlgFolderList = new AlertDialog.Builder(m_sourceManager)
				.setTitle("请选择文件夹")
				.setView(DialogView)
				.setNegativeButton("取消",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
						m_MoveIn_State = MoveIn_State.MoveIn_SelectMoveItem;
						dialog.cancel();
					}
				})
				.create();
			ListView folderList = (ListView) DialogView.findViewById(R.id.folderlist_view);
			if(m_ListUICtrlParam.g_int_PreID!=CMemoInfo.PreId_Root){
				TextView tvRootFolder = new TextView(m_sourceManager);
				tvRootFolder.setText("根目录");
				tvRootFolder.setPadding(2, 0, 0, 0);
				tvRootFolder.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
				tvRootFolder.setHeight(CommonDefine.g_int_ListItemHeight);
				tvRootFolder.setTextColor(Color.WHITE);       				
				TextPaint tp = tvRootFolder.getPaint(); 
				tp.setFakeBoldText(true); 
				folderList.addHeaderView(tvRootFolder);
			}
			Cursor cursorFolderList	=	m_clCNoteDBCtrl.getFolderInRoot();
			m_sourceManager.startManagingCursor(cursorFolderList);
			if(cursorFolderList.getCount()>0){
    			if(cursorFolderList!=null){
    				ListAdapter LA_FolderList = new SimpleCursorAdapter(
    						m_sourceManager,
    						android.R.layout.simple_list_item_1,
    						cursorFolderList,
    						new String[]{CNoteDBCtrl.KEY_detail},
    						new int[]{android.R.id.text1}
    						);
    				folderList.setAdapter(LA_FolderList);
    			} 
			}else{
				Toast toast = Toast.makeText(m_sourceManager, "当前没有可以移动到的文件夹\n请先建立文件夹", Toast.LENGTH_LONG);
        		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
        		toast.show();
			}
			
			folderList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
					ListAdapter LA = (ListAdapter)arg0.getAdapter();
					long id = LA.getItemId(arg2);
					if(id<0){
						id = 0;
					}
					ArrayList<DetailInfoOfSelectItem> alIDs = new ArrayList<DetailInfoOfSelectItem>();

					if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_NormalList){
						m_myAdapter.getSelectItemDBID(alIDs);
					}
					else if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_SearchResultList){

						m_myArrayListAdapter.getSelectItemDBID(alIDs);
						m_myArrayListAdapter = null;
					}
					Move2Folder(alIDs, (int)id);
            		m_dlgFolderList.cancel();
            		Return2TargetList();
            		updateListData(CommonDefine.g_int_Invalid_ID);
				}
			});
			m_dlgFolderList.show();      			
		}else{
			
		}
	}
	
	public void processCancelClick(View view){
		Return2TargetList();
		updateListData(CommonDefine.g_int_Invalid_ID);
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1,
			int arg2, long arg3){
		if(m_bIsDelete||m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
			CheckBox cb = (CheckBox) arg1.findViewById(R.id.notelistitem_noteselect);
			if((cb!=null)&&(cb.getVisibility()==View.VISIBLE)){
				((View)cb).performClick();
			}
		}else{

			if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_NormalList){
				
				NoteListCursorAdapter LA = (NoteListCursorAdapter)arg0.getAdapter();
				Cursor cur = LA.getCursor();
				cur.moveToPosition(arg2);
				int iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_type);
				int iValue = cur.getInt(iIndex);
				iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_id);
				int iIDValue = cur.getInt(iIndex);
				if(iValue == CMemoInfo.Type_Folder){
					boolean bIntent = true;
					int iEncodeIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_isencode);
					int iEncodeValue = cur.getInt(iEncodeIndex);
					if(iEncodeValue==CMemoInfo.IsEncode_Yes){
						if(!CommonDefine.g_bool_IsPassWordChecked){
							confirmPassword(iIDValue);
							bIntent = false;
						}
					}else{
					}
					if(bIntent){
						Intent intent = new Intent();
						intent.setClass(m_sourceManager, FolderViewList.class);							
						intent.putExtra(FolderViewList.ExtraData_FolderDBID, iIDValue);
						m_sourceManager.startActivity(intent);
					}
				}else if(iValue == CMemoInfo.Type_Memo){
					Intent toNew = new Intent();
	        		toNew.setClass(m_sourceManager, NoteWithYourMind.class);
	        		toNew.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Edit);
	        		toNew.putExtra(NoteWithYourMind.ExtraData_EditNoteID, iIDValue);
	        			        		
	        		m_sourceManager.startActivity(toNew);
				}else{
					
				}

			}
			else if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_SearchResultList){

				NoteListArrayAdapter LA = (NoteListArrayAdapter)arg0.getAdapter();

				CMemoInfo clCMemoInfo	=	new	CMemoInfo();
				clCMemoInfo = LA.getItem(arg2);
			
				Intent toNew = new Intent();
        		toNew.setClass(m_sourceManager, NoteWithYourMind.class);
        		toNew.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Edit);
        		toNew.putExtra(NoteWithYourMind.ExtraData_EditNoteID, clCMemoInfo.iId );	        			        		
        		toNew.putExtra(NoteWithYourMind.ExtraData_HighLightWord, m_ListUICtrlParam.g_str_SearchKey);	
				m_sourceManager.startActivity(toNew);

			}


		}
	}
	public void initializeSource(){
		updateListData(CommonDefine.g_int_Invalid_ID);
		m_targetList.setDividerHeight(3);
		m_targetList.setOnItemClickListener(this);
		ImageButton clBTMemoDelete = (ImageButton) m_toolBarLayout.findViewById(R.id.toolbar_delete);
		clBTMemoDelete.setOnClickListener(this);
		
		ImageButton clBTMemoMoveIn = (ImageButton) m_toolBarLayout.findViewById(R.id.toolbar_move);
		clBTMemoMoveIn.setOnClickListener(this);
		ImageButton clBTMemoCancel = (ImageButton) m_toolBarLayout.findViewById(R.id.toolbar_cancel);
		clBTMemoCancel.setOnClickListener(this);

	}
	
	private void updateToolBar(){
		ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
		ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
		ImageButton btMove = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_move);
		if(m_bIsDelete){		
			btDelete.setVisibility(View.VISIBLE);
			btMove.setVisibility(View.GONE);
			//btDelete.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
			((ListActivityCtrl)m_sourceManager).updateToolbar(CommonDefine.ToolbarStatusEnum.ToolbarStatus_Delete);
		}else if(m_MoveIn_State==MoveIn_State.MoveIn_SelectMoveItem){	
			btMove.setVisibility(View.VISIBLE);
			btDelete.setVisibility(View.GONE);	
			//btMove.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
			((ListActivityCtrl)m_sourceManager).updateToolbar(CommonDefine.ToolbarStatusEnum.ToolbarStatus_Move);
		}else{
			((ListActivityCtrl)m_sourceManager).updateToolbar(CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal);
		}
	}
	
	public void updateListData(int initListItemDBID){

		if(m_ListUICtrlParam.g_enListType == ListUICtrlParam.ListTypeEnum.ListType_NormalList){

			/*如果是普通的List，使用preID到DB里进行检索, 用CursorAdapter与ListView进行绑定*/
			if(m_myAdapter==null){
					/*使用preID到DB里进行检索, 用CursorAdapter与ListView进行绑定*/
				if( m_ListUICtrlParam.g_int_PreID != CMemoInfo.Id_Invalid){
					Cursor cursor = m_clCNoteDBCtrl.getNotesByID(m_ListUICtrlParam.g_int_PreID);
					m_sourceManager.startManagingCursor(cursor);
					m_myAdapter = new NoteListCursorAdapter(m_sourceManager, cursor);
					m_targetList.setAdapter(m_myAdapter);
				}else{
					//error
				}	


			}else{
				if(m_bIsDelete || m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
	    			m_myAdapter.setSelectableStyle(true);
				}else{
					m_myAdapter.setSelectableStyle(false);
				}
				if(m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
					m_myAdapter.setFolderSelectable(false);
				}else{
					m_myAdapter.setFolderSelectable(true);
				}
				m_myAdapter.updateCursor();
				m_myAdapter.notifyDataSetChanged();
			}


		}
		else{
				if(m_myArrayListAdapter==null){
					if( m_ListUICtrlParam.g_bool_IsTextSearch )
					{
						Cursor cursor = m_clCNoteDBCtrl.getAllNotEncodeMemo();
						m_sourceManager.startManagingCursor(cursor);
						//cursor转化为ArrayList

						List<CMemoInfo> Items = new ArrayList<CMemoInfo>();
						ConvertCursorToMemoInfo.ConvertItems( cursor ,Items);
						
						if(m_ListUICtrlParam.g_str_SearchKey != ""){
							FilterArrayListbySearchParam( Items );
						}

						switch(m_ListUICtrlParam.g_enSortType){
						case SortType_Normal:
							Collections.sort(Items, new SortByLastModifyTime());
							break;
						case SortType_RemindFirst:
							Collections.sort(Items, new SortByRemindFirst());
							break;
						case SortType_VoiceFirst:
							Collections.sort(Items, new SortByVoiceFirst());							
							break;
						case SortType_TextFirst:
							Collections.sort(Items, new SortByTextFirst());	
							break;
						default:
							Collections.sort(Items, new SortByLastModifyTime());
							break;
						}

						m_myArrayListAdapter = new NoteListArrayAdapter( m_sourceManager,  Items);
						m_myArrayListAdapter.setSearchKeyWord(m_ListUICtrlParam.g_str_SearchKey);						
						m_targetList.setAdapter(m_myArrayListAdapter);
					}
					else{
							//功能暂不开发
					}

				}else{

					if(m_bIsDelete || m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
		    			m_myArrayListAdapter.setSelectableStyle(true);
					}else{
						m_myArrayListAdapter.setSelectableStyle(false);
					}
					if(m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
						m_myArrayListAdapter.setFolderSelectable(false);
					}else{
						m_myArrayListAdapter.setFolderSelectable(true);
					}

					m_myArrayListAdapter.notifyDataSetChanged();

				}

		}


		if(initListItemDBID!=CommonDefine.g_int_Invalid_ID){
			/*
			 * 查找initListItemDBID对应的pos
			 * 移动list光标到pos
			 */
			int initPos = CommonDefine.g_int_Invalid_ID;
			int count = m_myAdapter.getCount();
			for(int i = 0; i < count; i++ ){
				if(initListItemDBID == m_myAdapter.getItemId(i)){
					initPos = i;
				}
			}
			m_targetList.setSelectionFromTop(initPos, 50);
		}
	}
	
	public boolean isFolder(View view){
		return m_myAdapter.isFolder(view);
	}
	
	public boolean getListIsEncode(View view){
		return m_myAdapter.getListIsEncode(view);
	}
	
	public int getListDBID(View view){
		return m_myAdapter.getListDBID(view);
	}
	
	private void Return2TargetList()
	{
		m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
		m_bIsDelete = false;
		if(m_myAdapter!=null){
			m_myAdapter.clearSelectResult();	
		}
		if(m_myArrayListAdapter!=null){
			m_myArrayListAdapter.clearSelectResult();	
		}
		//update toolbar
		updateToolBar();
	}
//	private void findSelectItemDBID(ArrayList<Integer> alIDs)
//	{
//		int count = m_myAdapter.getCount();
//		for(int i = 0; i < count; i++)
//		{
//			View v = m_targetList.getChildAt(i);
//			if(v!=null)
//			{
//				CheckBox cb = (CheckBox)v.findViewById(R.id.notelistitem_noteselect);
//				if(cb!=null){
//					boolean b = cb.isChecked();
//					if(b==true){
//						int iID = (int)m_myAdapter.getItemId(i);
//						alIDs.add(new Integer(iID));
//					}
//				}
//			}
//		}
//	}
	private void Move2Folder(ArrayList<DetailInfoOfSelectItem> alIDs, int id)
	{
		int count = alIDs.size();
		CMemoInfo clRec = new CMemoInfo();
		clRec.iPreId = id;
		for(int i = 0; i < count; i++ )
		{
			int iId = alIDs.get(i).iDBRecID;
			clRec.iId = iId;
			clRec.iType = CMemoInfo.Type_Memo;
			m_clCNoteDBCtrl.Update(clRec);
		}
	}
	private void confirmPassword(int iDBRecID){
		final int DBID = iDBRecID;
		LayoutInflater factory = LayoutInflater.from(m_sourceManager);
		final View DialogView = factory.inflate(R.layout.dialog_encodesetting, null);

		AlertDialog clDlgChangeFolder = new AlertDialog.Builder(m_sourceManager)	
				.setTitle("请输入确认密码")
				.setView(DialogView)
				.setPositiveButton("确定",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
						EditText PassWord = (EditText) DialogView.findViewById(R.id.ET_encodesetting);
						String strNewPassWord = PassWord.getText().toString();
		        		if(strNewPassWord.length()>0){
							if(strNewPassWord.equals(CommonDefine.g_str_PassWord)){
								CommonDefine.g_bool_IsPassWordChecked = true; 
								Intent intent = new Intent();
								intent.setClass(m_sourceManager, FolderViewList.class);							
								intent.putExtra(FolderViewList.ExtraData_FolderDBID, DBID);
								m_sourceManager.startActivity(intent);	
								dialog.cancel();
							}else{
			        			Toast toast = Toast.makeText(m_sourceManager, "密码错误!请重新输入", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
							}
		        		}else{
		        			Toast toast = Toast.makeText(m_sourceManager, "请输入确认密码", Toast.LENGTH_SHORT);
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



	private void FilterArrayListbySearchParam( List<CMemoInfo> Items){

		int Count = Items.size();
		for(int i=Count-1;i>=0;i--)
		{			
			CMemoInfo clCMemoInfo	= Items.get(i);
			String sDetail=clCMemoInfo.strDetail;
			if( sDetail.indexOf(m_ListUICtrlParam.g_str_SearchKey) ==  -1) 
			{
				Items.remove(i);
			}
		}
	}

	public void SetSearchParam(  ListUICtrlParam CtrlParam ){

		m_ListUICtrlParam = CtrlParam;
		m_myArrayListAdapter = null;		
	}
}
