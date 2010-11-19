package com.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.app.AlertDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.DialogInterface;
import android.widget.EditText;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.ImageButton;

public class NoteListCursorAdapter extends CursorAdapter implements Serializable {
	private static final long serialVersionUID = -7060210544600464481L;
	public class ItemSelectResult{
		int iDBRecID;
		boolean bIsSelected;
		int iItemPos;
		ItemSelectResult(){
			iDBRecID = CommonDefine.g_int_Invalid_ID;
			bIsSelected = false;
		}
	}
	public class CheckBoxMapItem{
		int iDBRecID;
		CheckBox checkBox;
		CheckBoxMapItem(){
			iDBRecID = CommonDefine.g_int_Invalid_ID;
			checkBox = null;
		}
	}
	public class ItemDetail{
		int iDBRecID;
		View vView;
		boolean bIsFolder;
		boolean bIsRemind;
		ItemDetail(){
			iDBRecID = CommonDefine.g_int_Invalid_ID;
			vView = null;
			bIsFolder = false;
			bIsRemind = false;
		}
	}
	private HashMap<String,ItemSelectResult> m_ListItemSelectResult;
	private HashMap<CheckBox,CheckBoxMapItem> m_ListCheckBoxMapItem;
	private HashMap<View,ItemDetail> m_ListItemDetail;
	private boolean m_isSelectableStyle = false;
	private boolean m_isFolderSelectable = true;
	private Context m_context;
	private LayoutInflater m_inflater;
	private Cursor m_cursor;
	private Calendar m_c;
	private CNoteDBCtrl m_clCNoteDBCtrl;
	private int m_listPreDBID;
	private int m_isRemind;
	private View m_DialogView;
	//private NoteListUICtrl m_NoteListUICtrl;
	//private String  m_strPassWord;
	private int m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
	
	public NoteListCursorAdapter(Context context, Cursor c) {
		super(context, c);
		m_context = context;
		m_inflater = LayoutInflater.from(context);
		m_cursor = c;
		//m_NoteListUICtrl = NoteListUICtrl;
		m_ListItemSelectResult = new HashMap<String,ItemSelectResult>();
		m_ListCheckBoxMapItem = new HashMap<CheckBox,CheckBoxMapItem>();
		m_ListItemDetail = new HashMap<View,ItemDetail>();
		m_listPreDBID = CommonDefine.g_int_Invalid_ID;
		m_isRemind = CommonDefine.g_int_Invalid_ID;
	}
 
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ItemDetail itemdetail = new ItemDetail();
		itemdetail.vView = view;
		CheckBox cbView = (CheckBox) view.findViewById(R.id.noteitem_noteselect);
		int iIDIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_id);
		int iIDValue = cursor.getInt(iIDIndex);
		itemdetail.iDBRecID = iIDValue;
		ItemSelectResult result = m_ListItemSelectResult.get(String.valueOf(iIDValue));
		if(result!=null){
			cbView.setChecked(result.bIsSelected);
		}		
		CheckBoxMapItem mapItem = new CheckBoxMapItem();
		mapItem.checkBox = cbView;
		mapItem.iDBRecID = iIDValue;
		m_ListCheckBoxMapItem.put(cbView, mapItem);
		
		int iTypeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_type);
		int iTypeValue = cursor.getInt(iTypeIndex);
		int iDetailIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_detail);
		String sDetail = cursor.getString(iDetailIndex);
		TextView tV = (TextView)view.findViewById(R.id.noteitem_notetext);
		tV.setCompoundDrawablePadding(15);
		if(iTypeValue==CMemoInfo.Type_Folder){
			itemdetail.bIsFolder = true;
			if(m_isFolderSelectable){
			}else{
				cbView.setVisibility(View.GONE);
			}
			int iEncodeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_isencode);
			int iEncodeFlag = cursor.getInt(iEncodeIndex);
			if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
				//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_FolderLocked, null, null, null);
				tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.folderlocked, 0, 0, 0);
			}else{
				//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_Folder, null, null, null);
				tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.folder, 0, 0, 0);
			}
		}else{
			//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_Memo, null, null, null);
			tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.memo, 0, 0, 0);
		}
		tV.setText(sDetail);
		if(m_listPreDBID == CommonDefine.g_int_Invalid_ID){
			int iPreIDIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_preid);
			m_listPreDBID = cursor.getInt(iPreIDIndex);
		}
		if(m_isRemind == CommonDefine.g_int_Invalid_ID){
			int isRemindIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_isremind);
			m_isRemind = cursor.getInt(isRemindIndex);
		}
		m_ListItemDetail.put(view, itemdetail);
	}
 
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = null;
		if(m_isSelectableStyle)
		{
			v = m_inflater.inflate(R.layout.notelistitemselect, parent, false);
			CheckBox cbView = (CheckBox) v.findViewById(R.id.noteitem_noteselect);
			cbView.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	        	{
	        		if(isChecked)
	        		{
	        			CheckBoxMapItem mapItem = m_ListCheckBoxMapItem.get(buttonView);
	        			ItemSelectResult result = new ItemSelectResult();
	        			result.bIsSelected = true;
	        			result.iDBRecID = mapItem.iDBRecID;
	        			if(mapItem!=null){
	        				m_ListItemSelectResult.put(String.valueOf(mapItem.iDBRecID), result);
	        			}	        			
	        		}else{
	        			CheckBoxMapItem mapItem = m_ListCheckBoxMapItem.get(buttonView);
	        			ItemSelectResult result = new ItemSelectResult();
	        			result.bIsSelected = false;
	        			result.iDBRecID = mapItem.iDBRecID;
	        			if(mapItem!=null){
	        				m_ListItemSelectResult.put(String.valueOf(mapItem.iDBRecID), result);
	        			}	        
	        		}
	        	}
			});
		}
		else{
			v = m_inflater.inflate(R.layout.notelistitem, parent, false);
		}	
		
		return v;
/*		Button clBTFolder=null;
		Button  clBTLockIf=null;		
		if(iTypeValue == CMemoInfo.Type_Folder){
			clBTFolder = (Button) v.findViewById(R.id.B_FolderItem_FolderIcon);
			//clBTFolder.setBackgroundColor(Color.argb(0, 0, 0, 0));
	        clBTFolder.setOnClickListener(new Button.OnClickListener(){
	        	public void onClick(View v)
	        	{		
	        		m_DBId2BeEdited = findDBIdByFolderButton(v);
	        		m_DialogView = m_inflater.inflate(R.layout.dialognewfolder, null);
					AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
						.setIcon(R.drawable.clock)
						.setTitle("请编辑文件夹名称")
						.setView(m_DialogView)
						.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int i)
							{
								EditText FolderNameText = (EditText) m_DialogView.findViewById(R.id.foldername_edit);
				        		String strFolderNameText = FolderNameText.getText().toString();
				        		if(strFolderNameText.length()>0){
				        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		 						    m_c = Calendar.getInstance();
									clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
				            		clCMemoInfo.strDetail	=	strFolderNameText;
				            		m_clCNoteDBCtrl.Update(m_DBId2BeEdited,clCMemoInfo); 
//				            		m_NoteListUICtrl.updateListData();
				            		m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
				            		FolderNameText.setText("");

				            		Toast toast = Toast.makeText(m_context, "保存成功", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
				            		toast.show();
				        		}else{
				        			Toast toast = Toast.makeText(m_context, "请输入文件夹名称", Toast.LENGTH_SHORT);
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
						clDlgNewFolder.show();      			
	        	}   
			});

			int Index = cursor.getColumnIndex(CNoteDBCtrl.KEY_isencode);
			int iEncodeValue = cursor.getInt(Index);
			if(iEncodeValue == CMemoInfo.IsEncode_Yes){
				clBTLockIf = (Button)  v.findViewById(R.id.B_FolderItem_LockIcon);
				clBTLockIf.setBackgroundResource(R.drawable.itemicon_lock);
			}else{
				clBTLockIf = (Button)  v.findViewById(R.id.B_FolderItem_LockIcon);
				clBTLockIf.setBackgroundResource(R.drawable.itemicon_unlock);
			}	
			//clBTLockIf.setBackgroundColor(Color.argb(0, 0, 0, 0));
	        clBTLockIf.setOnClickListener(new Button.OnClickListener(){
        		public void onClick(View v)
	        	{
        			m_DBId2BeEdited = findDBIdByEncodeButton(v);
        			int iEncodeValue=CMemoInfo.IsEncode_Invalid;
        			Cursor cur = m_clCNoteDBCtrl.getMemoRec(m_DBId2BeEdited);
        			if(cur!=null){
        				cur.moveToFirst();
        				int Index = cur.getColumnIndex(CNoteDBCtrl.KEY_isencode);
    					iEncodeValue = cur.getInt(Index);
        			}
        			cur = null;
					if(iEncodeValue == CMemoInfo.IsEncode_Yes){
	
						m_DialogView = m_inflater.inflate(R.layout.dialog_encodesetting, null);
						
						AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
							.setIcon(R.drawable.clock)
							.setTitle("取消加密请输入密码")
							.setView(m_DialogView)
							.setPositiveButton("确定",new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int i)
								{
					        		EditText PassWord = (EditText) m_DialogView.findViewById(R.id.ET_encodesetting);
					        		String strPassWord = PassWord.getText().toString();
					        		if(strPassWord.length()>0){
										if(strPassWord.equals(CommonDefine.g_str_PassWord)){
											CMemoInfo clCMemoInfo	=	new	CMemoInfo();
				 						    m_c = Calendar.getInstance();
											clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
						            		clCMemoInfo.iIsEncode	=	CMemoInfo.IsEncode_No;
						            		m_clCNoteDBCtrl.Update(m_DBId2BeEdited,clCMemoInfo);     		
						            		m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
//						            		m_NoteListUICtrl.updateListData();
						            		Toast toast = Toast.makeText(m_context, "加密已取消", Toast.LENGTH_SHORT);
						            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
						            		toast.show();
										}else{
						        			Toast toast = Toast.makeText(m_context, "密码错误!请重新输入", Toast.LENGTH_SHORT);
						            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
						            		toast.show();
										}
					        		}else{
					        			Toast toast = Toast.makeText(m_context, "请输入私人密码", Toast.LENGTH_SHORT);
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
						clDlgNewFolder.show();      							       
					}else{
						AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
							.setIcon(R.drawable.clock)
							.setTitle("设置为加密文件夹")
							.setPositiveButton("确定",new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int i)
								{
				        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		 						    m_c = Calendar.getInstance();
									clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
				            		clCMemoInfo.iIsEncode	=	CMemoInfo.IsEncode_Yes;
				            		m_clCNoteDBCtrl.Update(m_DBId2BeEdited,clCMemoInfo);     		
				            		m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
//				            		m_NoteListUICtrl.updateListData();
				            		Toast toast = Toast.makeText(m_context, "已设置为加密文件夹", Toast.LENGTH_SHORT);
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
						clDlgNewFolder.show();     
					}						
	        	}
		    });
		}else if(iTypeValue == CMemoInfo.Type_Memo){
			
		}else{	
			
		}
		CursorDataHolder clHolder = new CursorDataHolder();
		clHolder.btFolder = (View)clBTFolder;
		clHolder.btEncode = (View)clBTLockIf;
		int iIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_id);
		int iValue = cursor.getInt(iIndex);
		clHolder.iDBID = iValue;
		m_CursorDataHolder.add(clHolder);
		*/
		
	}
	public void updateCursor(){
		m_cursor.deactivate();
		m_cursor.requery();
	}
	public void setSelectableStyle(boolean bEnable){
		m_isSelectableStyle = bEnable;
	}
	public void setFolderSelectable(boolean bEnable){
		m_isFolderSelectable = bEnable;
	}
	
	public Cursor getCursor(){
		return m_cursor;
	}
	public void setNoteDBCtrl(CNoteDBCtrl clCNoteDBCtrl){
		m_clCNoteDBCtrl = clCNoteDBCtrl;
	}
	public void getSelectItemDBID(ArrayList<Integer> alIDs){
		Iterator iter = m_ListItemSelectResult.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
		    
		    ItemSelectResult val = (ItemSelectResult)entry.getValue(); 
		    if(val.bIsSelected){
		    	String key = (String)entry.getKey(); 
		    	alIDs.add(Integer.valueOf(key));
		    }
		} 
	}
	public boolean isFolder(View view){
		ItemDetail detail = m_ListItemDetail.get(view);
		return detail.bIsFolder;
	}
	public int getListPreDBID(){
		return m_listPreDBID;
	}
	public int getIsRemind(){
		return m_isRemind;
	}
	/*public void TransforPassWord(String strPassWord){
		m_strPassWord = strPassWord;
	}*/
//	private int findDBIdByFolderButton(View v2BeFinded){
//		int iDBId = CommonDefine.g_int_Invalid_ID;
//		int iCount = m_CursorDataHolder.size();
//		for(int i = 0; i < iCount; i++){
//			CursorDataHolder clHolder = m_CursorDataHolder.get(i);
//			if(clHolder.btFolder == v2BeFinded){
//				iDBId = clHolder.iDBID;
//			}
//		}
//		return iDBId;
//	}
//	private int findDBIdByEncodeButton(View v2BeFinded){
//		int iDBId = CommonDefine.g_int_Invalid_ID;
//		int iCount = m_CursorDataHolder.size();
//		for(int i = 0; i < iCount; i++){
//			CursorDataHolder clHolder = m_CursorDataHolder.get(i);
//			if(clHolder.btEncode == v2BeFinded){
//				iDBId = clHolder.iDBID;
//			}
//		}
//		return iDBId;
//	}
}
