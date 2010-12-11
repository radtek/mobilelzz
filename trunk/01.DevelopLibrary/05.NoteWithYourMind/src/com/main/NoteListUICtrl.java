
package com.main;

import java.util.ArrayList;

import com.main.NoteListCursorAdapter.ItemDetail;

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
import android.widget.ImageButton;
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
	
	private CNoteDBCtrl m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
	private boolean m_bIsDelete = false;
	private MoveIn_State m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
	
	private AlertDialog m_dlgFolderList;
	public ListView m_targetList;
	private int m_iPreID = CommonDefine.g_int_Invalid_ID;
	private View m_toolBarLayout;
	public Activity m_sourceManager;
	private NoteListCursorAdapter m_myAdapter;

	//private String m_strPassWord;

	NoteListUICtrl(Activity sourceManager, ListView target, int iPreID, View toolBarLayout){
		m_sourceManager = sourceManager;
		m_targetList = target;
		m_iPreID = iPreID;
		m_toolBarLayout = toolBarLayout;
		m_myAdapter = null;
	}
	
	public void releaseSource(){
		m_sourceManager = null;
		m_targetList = null;
		m_toolBarLayout = null;
		m_myAdapter = null;
		m_dlgFolderList = null;
	}
	
	public void initializeSource(){
		updateListData(CommonDefine.g_int_Invalid_ID);
		m_targetList.setDividerHeight(3);
		m_targetList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(m_bIsDelete||m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
					CheckBox cb = (CheckBox) arg1.findViewById(R.id.notelistitem_noteselect);
					if((cb!=null)&&(cb.getVisibility()==View.VISIBLE)){
						((View)cb).performClick();
					}
				}else{
					NoteListCursorAdapter LA = (NoteListCursorAdapter)arg0.getAdapter();
					Cursor cur = LA.getCursor();
					cur.moveToPosition(arg2);
					int iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_type);
					int iValue = cur.getInt(iIndex);
					iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_id);
					int iIDValue = cur.getInt(iIndex);
					if(iValue == CMemoInfo.Type_Folder){
						Intent intent = new Intent();
						intent.setClass(m_sourceManager, FolderViewList.class);							
						intent.putExtra(FolderViewList.ExtraData_FolderDBID, iIDValue);
						m_sourceManager.startActivity(intent);
					}else if(iValue == CMemoInfo.Type_Memo){
						Intent toNew = new Intent();
		        		toNew.setClass(m_sourceManager, NoteWithYourMind.class);
		        		toNew.putExtra(NoteWithYourMind.ExtraData_OperationNoteKind, NoteWithYourMind.OperationNoteKindEnum.OperationNoteKind_Edit);
		        		toNew.putExtra(NoteWithYourMind.ExtraData_EditNoteID, iIDValue);
		        			        		
		        		m_sourceManager.startActivity(toNew);
					}else{
						
					}
				}
			}
		});
		ImageButton clBTMemoDelete = (ImageButton) m_toolBarLayout.findViewById(R.id.toolbar_delete);
		clBTMemoDelete.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		if(!m_bIsDelete)
        		{
        			m_bIsDelete = true;
        			updateToolBar();
        			updateListData(CommonDefine.g_int_Invalid_ID);
        		}	
        		else
        		{
        			//delete rec--->
        			ArrayList<Integer> alIDs = new ArrayList<Integer>();
        			findSelectItemDBID(alIDs);
        			if(alIDs.size()>0){
        				Integer[] needDeleteIDs = (Integer[])alIDs.toArray(new Integer[0]);
            			m_clCNoteDBCtrl.Delete(needDeleteIDs);
						CRemindOperator	clCRemindOperator	=	CRemindOperator.getInstance();
						clCRemindOperator.disableRemind( m_sourceManager, needDeleteIDs );
        			}
        			Return2TargetList();
        			updateListData(CommonDefine.g_int_Invalid_ID);
        		}
        	}
        });
		
		ImageButton clBTMemoMoveIn = (ImageButton) m_toolBarLayout.findViewById(R.id.toolbar_move);
		clBTMemoMoveIn.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
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
        			ArrayList<Integer> alIDs = new ArrayList<Integer>();
        			findSelectItemDBID(alIDs);
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
        			Cursor cursorFolderList	=	m_clCNoteDBCtrl.getMemoFolderInRoot();
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
                    		Return2TargetList();
                    		updateListData(CommonDefine.g_int_Invalid_ID);
        				}
        			});
        			m_dlgFolderList.show();      			
        		}else{
        			
        		}
        	}
        });
		ImageButton clBTMemoCancel = (ImageButton) m_toolBarLayout.findViewById(R.id.toolbar_cancel);
		clBTMemoCancel.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Return2TargetList();
        		updateListData(CommonDefine.g_int_Invalid_ID);
         	}
        });

	}
	
	private void updateToolBar(){
		ImageButton btDelete = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_delete);
		ImageButton btCancel = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_cancel);
		ImageButton btMove = (ImageButton)m_toolBarLayout.findViewById(R.id.toolbar_move);
		if(m_bIsDelete){		
			btMove.setVisibility(View.GONE);
			//btDelete.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
			((ListActivityCtrl)m_sourceManager).updateToolbar(CommonDefine.ToolbarStatusEnum.ToolbarStatus_Delete);
		}else if(m_MoveIn_State==MoveIn_State.MoveIn_SelectMoveItem){		
			btDelete.setVisibility(View.GONE);	
			//btMove.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
			((ListActivityCtrl)m_sourceManager).updateToolbar(CommonDefine.ToolbarStatusEnum.ToolbarStatus_Move);
		}else{
			((ListActivityCtrl)m_sourceManager).updateToolbar(CommonDefine.ToolbarStatusEnum.ToolbarStatus_Normal);
		}
	}
	
	public void updateListData(int initListItemDBID){
		if(m_myAdapter==null){
			Cursor cursor = m_clCNoteDBCtrl.getNotesByID(m_iPreID);
			m_sourceManager.startManagingCursor(cursor);
			m_myAdapter = new NoteListCursorAdapter(m_sourceManager, cursor);
			m_targetList.setAdapter(m_myAdapter);
		}else{
			if(m_bIsDelete || m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
    			m_myAdapter.setSelectableStyle(true);
			}else{
				m_myAdapter.setSelectableStyle(false);
			}
			if(m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem){
				m_myAdapter.setFolderSelectable(false);
			}
			m_myAdapter.updateCursor();
			m_myAdapter.notifyDataSetChanged();
		}
		if(initListItemDBID!=CommonDefine.g_int_Invalid_ID){
			/*
			 * 查找initListItemDBID对应的pos
			 * 移动list光标到pos
			 */
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
		m_myAdapter.clearSelectResult();
		//update toolbar
		updateToolBar();
	}
	private void findSelectItemDBID(ArrayList<Integer> alIDs)
	{
		int count = m_myAdapter.getCount();
		for(int i = 0; i < count; i++)
		{
			final CheckBox cb = (CheckBox)m_targetList.getChildAt(i).findViewById(R.id.notelistitem_noteselect);
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
