package com.main;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.view.Window;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.Toast;


public class ListItemEdit extends Activity
{
	//private NoteListUICtrl m_NoteListUICtrl;
	public final static String g_ExtraDataName_ListCursor = "com.main.ExtraDataName_ListCursor";
	public final static String g_ExtraDataName_ExitType = "com.main.ExtraDataName_ExitType";
	private enum MoveIn_State{
		MoveIn_Invalid,
		MoveIn_SelectMoveItem,
		MoveIn_SelectFolder
	}
	private MoveIn_State m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
	public enum ListItemEditTypeEnum{
		ListItemEditType_delete,
		ListItemEditType_move
	}
	private ListItemEditTypeEnum m_ListItemEditType;
	private CNoteDBCtrl m_clCNoteDBCtrl = CommonDefine.m_clCNoteDBCtrl;
	private NoteListCursorAdapter m_ListAdapter;
	private NoteListCursorAdapter m_sourceListAdapter;
	
	private AlertDialog m_dlgFolderList;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);	
        setContentView(R.layout.listitemedit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		
		Intent intent = getIntent();
		Cursor cursor = null;
		m_sourceListAdapter = CommonDefine.g_listAdapter;
		if(!CommonDefine.g_bIsRemind){
			cursor = m_clCNoteDBCtrl.getMemosByID(CommonDefine.g_preID);
		}else{
			cursor = m_clCNoteDBCtrl.getRemindsByID(CommonDefine.g_preID);
		}
		if(cursor.getCount()>0){
			int a = 0;
		}
		startManagingCursor(cursor);
		m_ListAdapter = new NoteListCursorAdapter(this, cursor);
		m_ListAdapter.setSelectableStyle(true);
		m_ListItemEditType = (ListItemEditTypeEnum)intent.getSerializableExtra(g_ExtraDataName_ExitType);
		if(m_ListItemEditType == ListItemEditTypeEnum.ListItemEditType_delete){
			m_ListAdapter.setFolderSelectable(true);
		}else{
			m_ListAdapter.setFolderSelectable(false);
		}
		ListView targetList = (ListView)findViewById(R.id.listedit_list);
		targetList.setAdapter(m_ListAdapter);
		targetList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				CheckBox cb = (CheckBox) arg1.findViewById(R.id.noteitem_noteselect);
				if(cb!=null){
					((View)cb).performClick();
				}
			}
		});
		updateToolBar();
		Button clBTMemoDelete = (Button) findViewById(R.id.B_view_memo_delete);
		clBTMemoDelete.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
    			//delete rec--->
    			ArrayList<Integer> alIDs = new ArrayList<Integer>();
    			m_ListAdapter.getSelectItemDBID(alIDs);
    			if(alIDs.size()>0){
    				Integer[] needDeleteIDs = (Integer[])alIDs.toArray(new Integer[0]);
        			m_clCNoteDBCtrl.Delete(needDeleteIDs);
        			m_sourceListAdapter.updateCursor();
    			}
    			ListItemEdit.this.finish();
        	}
        });
		
		Button clBTMemoMoveIn = (Button) findViewById(R.id.B_view_memo_move);
		clBTMemoMoveIn.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		if(m_MoveIn_State == MoveIn_State.MoveIn_Invalid)
        		{
        			/*m_MoveIn_State = MoveIn_State.MoveIn_SelectMoveItem;
        			//update toolbar
        			updateToolBar();
        			updateListData();
            		if(m_myAdapter!=null){
            			m_myAdapter.setSelectableStyle(true);
            			m_myAdapter.setFolderSelectable(false);
            		}*/
        		}	
        		else if(m_MoveIn_State == MoveIn_State.MoveIn_SelectMoveItem)
        		{
        			m_MoveIn_State = MoveIn_State.MoveIn_SelectFolder;
        			ArrayList<Integer> alIDs = new ArrayList<Integer>();
        			m_ListAdapter.getSelectItemDBID(alIDs);
        			if(alIDs.size()<=0){
        				m_MoveIn_State = MoveIn_State.MoveIn_Invalid;
        				ListItemEdit.this.finish();
                		return;
        			}
        			//show folder to select rec--->
        			LayoutInflater factory = LayoutInflater.from(ListItemEdit.this);
        			final View DialogView = factory.inflate(R.layout.folderlist, null);
        			
        			m_dlgFolderList = new AlertDialog.Builder(ListItemEdit.this)
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
        			if(m_ListAdapter.getListPreDBID()!=CMemoInfo.PreId_Root){
        				TextView tvRootFolder = new TextView(ListItemEdit.this);
        				tvRootFolder.setText("根目录");
        				tvRootFolder.setPadding(2, 0, 0, 0);
        				tvRootFolder.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        				tvRootFolder.setHeight(CommonDefine.g_int_ListItemHeight);
        				tvRootFolder.setTextColor(Color.WHITE);       				
        				TextPaint tp = tvRootFolder.getPaint(); 
        				tp.setFakeBoldText(true); 
        				folderList.addHeaderView(tvRootFolder);
        			}
        			Cursor cursorFolderList = null;
        			if(m_ListAdapter.getIsRemind()==CMemoInfo.IsRemind_Yes){        				
        				cursorFolderList = m_clCNoteDBCtrl.getRemindFolderInRoot();
        			}else{
        				cursorFolderList = m_clCNoteDBCtrl.getMemoFolderInRoot();
        			}
        			ListItemEdit.this.startManagingCursor(cursorFolderList);
        			if(cursorFolderList.getCount()>0){
            			if(cursorFolderList!=null){
            				ListAdapter LA_FolderList = new SimpleCursorAdapter(
            						ListItemEdit.this,
            						android.R.layout.simple_list_item_1,
            						cursorFolderList,
            						new String[]{CNoteDBCtrl.KEY_detail},
            						new int[]{android.R.id.text1}
            						);
            				folderList.setAdapter(LA_FolderList);
            			} 
        			}else{
        				Toast toast = Toast.makeText(ListItemEdit.this, "当前没有可以移动到的文件夹\n请先建立文件夹", Toast.LENGTH_LONG);
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
        					m_ListAdapter.getSelectItemDBID(alIDs);
        					Move2Folder(alIDs, (int)id);
                    		m_dlgFolderList.cancel();
                    		ListItemEdit.this.finish();
        				}
        			});
        			m_dlgFolderList.show();      			
        		}else{
        			
        		}
        	}
        });
		Button clBTMemoCancel = (Button) findViewById(R.id.B_view_memo_cancel);
		clBTMemoCancel.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		ListItemEdit.this.finish();
         	}
        });

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
	private void updateToolBar(){
		Button btDelete = (Button)findViewById(R.id.B_view_memo_delete);
		Button btCancel = (Button)findViewById(R.id.B_view_memo_cancel);
		Button btMove = (Button)findViewById(R.id.B_view_memo_move);
		if(m_ListItemEditType == ListItemEditTypeEnum.ListItemEditType_delete){		
			btMove.setVisibility(View.GONE);
			btDelete.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
		}else if(m_ListItemEditType == ListItemEditTypeEnum.ListItemEditType_move){		
			btDelete.setVisibility(View.GONE);	
			btMove.setBackgroundResource(R.drawable.buttonshape);
			btCancel.setVisibility(View.VISIBLE);
			m_MoveIn_State = MoveIn_State.MoveIn_SelectMoveItem; 
		}else{
			btMove.setBackgroundResource(android.R.drawable.btn_default);
			btMove.setVisibility(View.VISIBLE);
			btDelete.setBackgroundResource(android.R.drawable.btn_default);
			btDelete.setVisibility(View.VISIBLE);	
			btCancel.setVisibility(View.GONE);
		}
	}

}
