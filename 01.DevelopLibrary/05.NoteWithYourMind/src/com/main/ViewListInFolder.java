package com.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class ViewListInFolder extends Activity
{
	public static String ExtraData_FolderID = "com.main.ExtraData_FolderID";
	private int m_Cur_FolderID = -1;
	private enum MoveIn_State{
		MoveIn_Invalid,
		MoveIn_SelectMoveItem,
		MoveIn_SelectFolder
	}
	private CNoteDBCtrl m_clCNoteDBCtrl = NoteWithYourMind.m_clCNoteDBCtrl;
	private boolean m_bIsDelete_InFolder = false;
	private MoveIn_State m_MoveIn_State_InFolder = MoveIn_State.MoveIn_Invalid;
	
	private NoteListCursorAdapter m_myAdapter;
	private Cursor m_clCursor;
	private AlertDialog m_dlgFolderList;
	private Cursor m_cursorFolderList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		m_bIsDelete_InFolder = false;
		m_MoveIn_State_InFolder = MoveIn_State.MoveIn_Invalid;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folderview);	
		Intent iExtraData = this.getIntent();
		m_Cur_FolderID = iExtraData.getIntExtra(ExtraData_FolderID, -1 );
		m_clCursor	=	m_clCNoteDBCtrl.getMemoInFolder(m_Cur_FolderID);
		if(m_clCursor!=null)
		{
			startManagingCursor(m_clCursor);
			int count = m_clCursor.getCount();
			if(count>=0)
			{
				TextView title = (TextView) findViewById(R.id.memolisttitle_infolder);
				Cursor cursor = m_clCNoteDBCtrl.getMemoRec(m_Cur_FolderID);
				cursor.moveToFirst();
				int index = cursor.getColumnIndex(CNoteDBCtrl.KEY_detail);
				String value = cursor.getString(index);
				title.setText("文件夹: "+value);
				m_myAdapter = new NoteListCursorAdapter(this,m_clCursor);
				ListView memoList = (ListView) findViewById(R.id.listviewmemo_infolder);
				memoList.setAdapter(m_myAdapter);
//zhu.t test
				memoList.setOnItemClickListener(new OnItemClickListener(){
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						NoteListCursorAdapter LA = (NoteListCursorAdapter)arg0.getAdapter();
						Cursor cur = LA.getCursor();
						cur.moveToPosition(arg2);
						int iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_id);
						int iIDValue = cur.getInt(iIndex);
						Intent toNew = new Intent();
		        		toNew.setClass(ViewListInFolder.this, NoteWithYourMind.class);
		        		toNew.putExtra(NoteWithYourMind.ExtraData_MemoID, iIDValue);
		        		toNew.putExtra(NoteWithYourMind.NewNoteKind,NoteWithYourMind.NewNoteKindEnum.NewNoteKind_Memo);
		        		startActivity(toNew);
					}		
				});
			}
		}
		Button clBTMemoR = (Button) findViewById(R.id.B_view_memo_return_infolder);
		clBTMemoR.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{      		
        		ViewListInFolder.this.finish();
        	}
        });
		Button clBTMemoNew = (Button) findViewById(R.id.B_view_memo_new_infolder);
		clBTMemoNew.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{     
        		Intent toNew = new Intent();
        		toNew.setClass(ViewListInFolder.this, NoteWithYourMind.class);
        		toNew.putExtra(NoteWithYourMind.ExtraData_MemoID, m_Cur_FolderID);
        		toNew.putExtra(NoteWithYourMind.NewNoteKind,NoteWithYourMind.NewNoteKindEnum.NewNoteKind_Folder);
        		startActivity(toNew);
        	}
        });
		Button clBTMemoDelete = (Button) findViewById(R.id.B_view_memo_delete_infolder);
		clBTMemoDelete.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		if(!m_bIsDelete_InFolder)
        		{
        			m_bIsDelete_InFolder = true;
            		TableLayout TL = (TableLayout) ViewListInFolder.this.findViewById(R.id.memolistmenu_infolder);            		
            		TL.setColumnCollapsed(0, true);
            		TL.setColumnCollapsed(1, true);
            		TL.setColumnCollapsed(2, true);	
            		TL.setColumnCollapsed(3, false);
            		TL.setColumnCollapsed(4, false);
            		TL.setColumnStretchable(0, false);
            		TL.setColumnStretchable(1, false);
            		TL.setColumnStretchable(2, false);
            		TL.setColumnStretchable(3, true);
            		TL.setColumnStretchable(4, true);            		  
            		TL.invalidate();
            		m_myAdapter = new NoteListCursorAdapter(ViewListInFolder.this,m_clCursor);
            		m_myAdapter.setSelectableStyle(true);
            		ListView memoList = (ListView) ViewListInFolder.this.findViewById(R.id.listviewmemo_infolder);
            		memoList.setAdapter(m_myAdapter);
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
        			Return2MemoList();
        		}	
        	}
        });
		
		Button clBTMemoMove = (Button) findViewById(R.id.B_view_memo_move_infolder);
		clBTMemoMove.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		if(m_MoveIn_State_InFolder == MoveIn_State.MoveIn_Invalid)
        		{
        			m_MoveIn_State_InFolder = MoveIn_State.MoveIn_SelectMoveItem;
            		TableLayout TL = (TableLayout) ViewListInFolder.this.findViewById(R.id.memolistmenu_infolder);            		
            		TL.setColumnCollapsed(0, true);
            		TL.setColumnCollapsed(1, true);
            		TL.setColumnCollapsed(2, false);
            		TL.setColumnCollapsed(3, true);	
            		TL.setColumnCollapsed(4, false);
            		TL.setColumnStretchable(0, false);
            		TL.setColumnStretchable(1, false);
            		TL.setColumnStretchable(2, true);
            		TL.setColumnStretchable(3, false);
            		TL.setColumnStretchable(4, true);            		
            		TL.invalidate();
            		m_myAdapter = new NoteListCursorAdapter(ViewListInFolder.this,m_clCursor);
            		m_myAdapter.setSelectableStyle(true);
            		ListView memoList = (ListView) ViewListInFolder.this.findViewById(R.id.listviewmemo_infolder);
            		memoList.setAdapter(m_myAdapter);
        		}	
        		else if(m_MoveIn_State_InFolder == MoveIn_State.MoveIn_SelectMoveItem)
        		{
        			m_MoveIn_State_InFolder = MoveIn_State.MoveIn_SelectFolder;
        			//show folder to select rec--->
        			LayoutInflater factory = LayoutInflater.from(ViewListInFolder.this);
        			final View DialogView = factory.inflate(R.layout.folderlist, null);
        			
        			m_dlgFolderList = new AlertDialog.Builder(ViewListInFolder.this)
        				.setTitle("请选择文件夹")
        				.setView(DialogView)
        				.setNegativeButton("取消",new DialogInterface.OnClickListener(){
        					public void onClick(DialogInterface dialog, int i)
        					{
        						m_MoveIn_State_InFolder = MoveIn_State.MoveIn_SelectMoveItem;
        						dialog.cancel();
        					}
        				})
        				.create();
        			ListView folderList = (ListView) DialogView.findViewById(R.id.folderlist_view);
        			m_cursorFolderList	=	m_clCNoteDBCtrl.getFolderInRoot();
        			startManagingCursor(m_cursorFolderList);
        			if(m_cursorFolderList!=null){
        				ListAdapter LA_FolderList = new SimpleCursorAdapter(
        						ViewListInFolder.this,
        						android.R.layout.simple_list_item_1,
        						m_cursorFolderList,
        						new String[]{CNoteDBCtrl.KEY_detail},
        						new int[]{android.R.id.text1}
        						);
        				folderList.setAdapter(LA_FolderList);
        			} 
        			folderList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
        					ListAdapter LA = (ListAdapter)arg0.getAdapter();
        					long id = LA.getItemId(arg2);
        					Toast toast = Toast.makeText(ViewListInFolder.this, "移动到文件夹 "+String.valueOf(id), Toast.LENGTH_SHORT);
                    		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
                    		toast.show();
                    		m_dlgFolderList.cancel();
                    		Return2MemoList();
        				}
        			});
        			m_dlgFolderList.show();      			
        		}else{
        			
        		}
        	}
        });
		
		Button clBTMemoCancel = (Button) findViewById(R.id.B_view_memo_cancel_infolder);
		clBTMemoCancel.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Return2MemoList();
         	}
        });
	}
	
	void Return2MemoList()
	{
		m_MoveIn_State_InFolder = MoveIn_State.MoveIn_Invalid;
		m_bIsDelete_InFolder = false;
		TableLayout TL = (TableLayout) this.findViewById(R.id.memolistmenu_infolder);  
		
		TL.setColumnCollapsed(0, false);
		TL.setColumnCollapsed(1, false);
		TL.setColumnCollapsed(2, false);	
		TL.setColumnCollapsed(3, false);
		TL.setColumnCollapsed(4, true);

		TL.setColumnStretchable(0, true);
		TL.setColumnStretchable(1, true);
		TL.setColumnStretchable(2, true);
		TL.setColumnStretchable(3, true);
		TL.setColumnStretchable(4, false); 
		TL.invalidate();
		m_clCursor	=	m_clCNoteDBCtrl.getMemoInFolder(m_Cur_FolderID);
		startManagingCursor(m_clCursor);
		m_myAdapter = new NoteListCursorAdapter(this,m_clCursor);
		ListView memoList = (ListView) this.findViewById(R.id.listviewmemo_infolder);
		memoList.setAdapter(m_myAdapter);
	}
	void findSelectItemDBID(ArrayList<Integer> alIDs)
	{
		int count = m_myAdapter.getCount();
		for(int i = 0; i < count; i++)
		{
			ListView memoList = (ListView) findViewById(R.id.listviewmemo_infolder);
			final CheckBox cb = (CheckBox)memoList.getChildAt(i).findViewById(R.id.memoitem_memoselect);
			if(cb!=null){
				boolean b = cb.isChecked();
				if(b==true){
					int iID = (int)m_myAdapter.getItemId(i);
					alIDs.add(new Integer(iID));
				}
			}
		}
	}
}
