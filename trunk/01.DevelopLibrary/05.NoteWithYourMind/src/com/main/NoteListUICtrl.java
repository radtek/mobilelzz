
package com.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

class NoteListUICtrl{
	private enum MoveIn_State{
		MoveIn_Invalid,
		MoveIn_SelectMoveItem,
		MoveIn_SelectFolder
	}
	private int m_ButtonID_delete = 0;
	private int m_ButtonID_move = 1;
	private int m_ButtonID_cancel = 2;
	
	CNoteDBCtrl m_clCNoteDBCtrl = NoteWithYourMind.m_clCNoteDBCtrl;
	private boolean m_bIsDelete = false;
	private MoveIn_State m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
	
	private AlertDialog m_dlgFolderList;
	private ListView m_targetList;
	private int m_iPreID = CommonDefine.g_int_Invalid_ID;
	private LinearLayout m_toolBarLayout;
	private Activity m_sourceManager;
	private NoteListCursorAdapter m_myAdapter;
	NoteListUICtrl(Activity sourceManager, ListView target, int iPreID, LinearLayout toolBarLayout){
		m_sourceManager = sourceManager;
		m_targetList = target;
		m_iPreID = iPreID;
		m_toolBarLayout = toolBarLayout;
	}
	
	public void releaseSource(){
		m_sourceManager = null;
		m_targetList = null;
		m_toolBarLayout = null;
		m_myAdapter = null;
		m_dlgFolderList = null;
	}
	
	public void initializeSource(){
		updateListData();
		m_targetList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				NoteListCursorAdapter LA = (NoteListCursorAdapter)arg0.getAdapter();
				Cursor cur = LA.getCursor();
				cur.moveToPosition(arg2);
				int iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_type);
				int iValue = cur.getInt(iIndex);
				iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_id);
				int iIDValue = cur.getInt(iIndex);
				if(iValue == CMemoInfo.Type_Folder){
					Intent intent = new Intent();
					intent.setClass(m_sourceManager, ViewListInFolder.class);							
					intent.putExtra(ViewListInFolder.ExtraData_FolderID, iIDValue);
					m_sourceManager.startActivity(intent);
				}else if(iValue == CMemoInfo.Type_Memo){
					Intent toNew = new Intent();
	        		toNew.setClass(m_sourceManager, NoteWithYourMind.class);
	        		toNew.putExtra(NoteWithYourMind.ExtraData_MemoID, iIDValue);
	        		if(m_iPreID==CMemoInfo.PreId_Root){
	        			toNew.putExtra(NoteWithYourMind.ExtraData_NewNoteKind,NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InRoot);
	        		}else{
	        			toNew.putExtra(NoteWithYourMind.ExtraData_NewNoteKind,NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InFolder);
	        		}
	        		
	        		m_sourceManager.startActivity(toNew);
				}else{
					
				}
			}
		});
		Button clBTMemoDelete = (Button) m_toolBarLayout.getChildAt(m_ButtonID_delete);
		clBTMemoDelete.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		if(!m_bIsDelete)
        		{
        			m_bIsDelete = true;
        			updateToolBar();
        			updateListData();
        			if(m_myAdapter!=null){
            			m_myAdapter.setSelectableStyle(true);
            		}
        		}	
        		else
        		{
        			//delete rec--->
        			ArrayList<Integer> alIDs = new ArrayList<Integer>();
        			findSelectItemDBID(alIDs);
        			if(alIDs.size()>0){
        				Integer[] needDeleteIDs = (Integer[])alIDs.toArray(new Integer[0]);
            			m_clCNoteDBCtrl.Delete(needDeleteIDs);
        			}
        			Return2TargetList();
        			updateListData();
        		}
        	}
        });
		
		Button clBTMemoMoveIn = (Button) m_toolBarLayout.getChildAt(m_ButtonID_move);
		clBTMemoMoveIn.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		if(m_MoveIn_State == MoveIn_State.MoveIn_Invalid)
        		{
        			m_MoveIn_State = MoveIn_State.MoveIn_SelectMoveItem;
        			//update toolbar
        			updateToolBar();
        			updateListData();
            		if(m_myAdapter!=null){
            			m_myAdapter.setSelectableStyle(true);
            			m_myAdapter.setFolderSelectable(false);
            		}
        		}	
        		else if(m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem)
        		{
        			m_MoveIn_State = MoveIn_State.MoveIn_SelectFolder;
        			ArrayList<Integer> alIDs = new ArrayList<Integer>();
        			findSelectItemDBID(alIDs);
        			if(alIDs.size()<=0){
        				m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
        				updateListData();
                		Return2TargetList();
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
        			if(m_iPreID!=CMemoInfo.PreId_Root){
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
        					ArrayList<Integer> alIDs = new ArrayList<Integer>();
        					findSelectItemDBID(alIDs);
        					Move2Folder(alIDs, (int)id);
                    		m_dlgFolderList.cancel();
                    		updateListData();
                    		Return2TargetList();
        				}
        			});
        			m_dlgFolderList.show();      			
        		}else{
        			
        		}
        	}
        });
		Button clBTMemoCancel = (Button) m_toolBarLayout.getChildAt(m_ButtonID_cancel);
		clBTMemoCancel.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		updateListData();
        		if(m_myAdapter!=null){
        			m_myAdapter.setSelectableStyle(false);
        		}
        		Return2TargetList();
         	}
        });

	}
	
	private void updateToolBar(){
		Button btDelete = (Button)m_toolBarLayout.getChildAt(m_ButtonID_delete);
		Button btCancel = (Button)m_toolBarLayout.getChildAt(m_ButtonID_cancel);
		Button btMove = (Button)m_toolBarLayout.getChildAt(m_ButtonID_move);
		if(m_bIsDelete){		
			btMove.setVisibility(View.GONE);
			btDelete.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
		}else if(m_MoveIn_State==MoveIn_State.MoveIn_SelectMoveItem){		
			btDelete.setVisibility(View.GONE);	
			btMove.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
		}else{
			btMove.setBackgroundResource(android.R.drawable.btn_default);
			btMove.setVisibility(View.VISIBLE);
			btDelete.setBackgroundResource(android.R.drawable.btn_default);
			btDelete.setVisibility(View.VISIBLE);	
			btCancel.setVisibility(View.GONE);
		}
	}
	
	public void updateListData(){
		Cursor clCursor = null;
		if(m_iPreID!=CommonDefine.g_int_Invalid_ID){
			clCursor = m_clCNoteDBCtrl.getMemosByID(m_iPreID);
		}
		if(clCursor!=null)
		{
			m_sourceManager.startManagingCursor(clCursor);
			int count = clCursor.getCount();
			if(count>=0)
			{
				m_myAdapter = new NoteListCursorAdapter(m_sourceManager,clCursor);
				m_targetList.setAdapter(m_myAdapter);
			}
		}
	}
	private void Return2TargetList()
	{
		m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
		m_bIsDelete = false;

		//update toolbar
		updateToolBar();
	}
	private void findSelectItemDBID(ArrayList<Integer> alIDs)
	{
		int count = m_myAdapter.getCount();
		for(int i = 0; i < count; i++)
		{
			final CheckBox cb = (CheckBox)m_targetList.getChildAt(i).findViewById(R.id.memoitem_memoselect);
			if(cb!=null){
				boolean b = cb.isChecked();
				if(b==true){
					int iID = (int)m_myAdapter.getItemId(i);
					alIDs.add(new Integer(iID));
				}
			}
		}
	}
	private void Move2Folder(ArrayList<Integer> alIDs, int id)
	{
		int count = alIDs.size();
		CMemoInfo clRec = new CMemoInfo();
		clRec.iPreId = id;
		for(int i = 0; i < count; i++ )
		{
			int iId = alIDs.get(i);
			m_clCNoteDBCtrl.Update(iId, clRec);
		}
	}
	
}