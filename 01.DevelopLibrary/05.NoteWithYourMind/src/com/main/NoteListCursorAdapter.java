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
		boolean bIsEncode;
		ItemDetail(){
			iDBRecID = CommonDefine.g_int_Invalid_ID;
			vView = null;
			bIsFolder = false;
			bIsRemind = false;
			bIsEncode = false;
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
		/*
		 * 如果是选择状态
		 * 		如果记录是便签
		 * 			将日期控件隐藏，将CheckBox显示
		 * 		如果记录是文件夹
		 * 			如果文件夹是可选择状态
		 * 				将日期控件隐藏，将CheckBox显示
		 * 如果是正常状态
		 * 		将CheckBox隐藏，将日期控件显示
		 */
		ItemDetail itemdetail = new ItemDetail();
		itemdetail.vView = view;
		
		//update CheckBox Status-begin
		//根据保存的当前记录的选择状态，更新CheckBox
		CheckBox cbView = (CheckBox) view.findViewById(R.id.notelistitem_noteselect);
		int iIDIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_id);
		int iIDValue = cursor.getInt(iIDIndex);
		itemdetail.iDBRecID = iIDValue;
		ItemSelectResult result = m_ListItemSelectResult.get(String.valueOf(iIDValue));
		if(result!=null){
			cbView.setChecked(result.bIsSelected);
		}		
		//update CheckBox Status-end
		
		//建立CheckBox和DBID的对应关系
		CheckBoxMapItem mapItem = new CheckBoxMapItem();
		mapItem.checkBox = cbView;
		mapItem.iDBRecID = iIDValue;
		m_ListCheckBoxMapItem.put(cbView, mapItem);
		
		int iTypeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_type);
		int iTypeValue = cursor.getInt(iTypeIndex);
		int iDetailIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_detail);
		String sDetail = cursor.getString(iDetailIndex);
		TextView tV = (TextView)view.findViewById(R.id.notelistitem_notetext);
		if(iTypeValue==CMemoInfo.Type_Folder){
			itemdetail.bIsFolder = true;
			if(m_isFolderSelectable){
			}else{
				if(cbView!=null){
					cbView.setVisibility(View.GONE);
				}
			}
			int iEncodeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_isencode);
			int iEncodeFlag = cursor.getInt(iEncodeIndex);
			if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
				//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_FolderLocked, null, null, null);
				tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.folderlocked, 0, 0, 0);
				itemdetail.bIsEncode = true;
			}else{
				//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_Folder, null, null, null);
				tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.folder, 0, 0, 0);
				itemdetail.bIsEncode = false;
			}
		}else{
			if(cbView!=null){
				cbView.setVisibility(View.VISIBLE);
			}
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
		//建立Item控件和对应的DB记录信息的对应关系
		m_ListItemDetail.put(view, itemdetail);
	}
 
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = null;
		v = m_inflater.inflate(R.layout.notelistitem, parent, false);
		CheckBox cbView = (CheckBox) v.findViewById(R.id.noteitem_noteselect);
		cbView.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	{
				ItemSelectResult result = new ItemSelectResult();
        		if(isChecked)
        		{
        			result.bIsSelected = true;        			
        		}else{
        			result.bIsSelected = false;        
        		}
        		//根据CheckBox和DBID的对应关系，更新DBID的选择状态
    			CheckBoxMapItem mapItem = m_ListCheckBoxMapItem.get(buttonView);
    			if(mapItem!=null){
    				result.iDBRecID = mapItem.iDBRecID;
    				m_ListItemSelectResult.put(String.valueOf(mapItem.iDBRecID), result);
    			}	        
        	}
		});
		return v;
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

	public int getListDBID(View view){
		ItemDetail detail = m_ListItemDetail.get(view);
		return detail.iDBRecID;
	}

	public boolean getListIsEncode(View view){
		ItemDetail detail = m_ListItemDetail.get(view);
		return detail.bIsEncode;
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
