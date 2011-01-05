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

import com.main.R.id;

import android.content.DialogInterface;
import android.widget.EditText;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.ImageButton;

public class NoteListCursorAdapter extends CursorAdapter implements Serializable {
	private static final long serialVersionUID = -7060210544600464481L;

	private HashMap<String,ItemSelectResult> m_ListItemSelectResult;
	private HashMap<CheckBox,CheckBoxMapItem> m_ListCheckBoxMapItem;
	private HashMap<View,ItemDetail> m_ListItemDetail;
	private boolean m_isSelectableStyle = false;
	private boolean m_isFolderSelectable = true;
	private LayoutInflater m_inflater;
	private Cursor m_cursor;
	private Calendar m_c;
	private CNoteDBCtrl m_clCNoteDBCtrl;
	private int m_listPreDBID;
	private int m_isRemind;
	private View m_DialogView;
	private AdapterCommonProcess m_AdapterComPro;
	//private NoteListUICtrl m_NoteListUICtrl;
	//private String  m_strPassWord;
//	private int m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
	
	public NoteListCursorAdapter(Context context, Cursor c) {
		super(context, c);
		m_inflater = LayoutInflater.from(context);
		m_cursor = c;
		//m_NoteListUICtrl = NoteListUICtrl;
		m_ListItemSelectResult = new HashMap<String,ItemSelectResult>();
		m_ListCheckBoxMapItem = new HashMap<CheckBox,CheckBoxMapItem>();
		m_ListItemDetail = new HashMap<View,ItemDetail>();
		m_listPreDBID = CommonDefine.g_int_Invalid_ID;
		m_isRemind = CommonDefine.g_int_Invalid_ID;
		m_AdapterComPro = new AdapterCommonProcess(context);
	}
 
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		CheckBox cbView = (CheckBox) view.findViewById(R.id.notelistitem_noteselect);

		CMemoInfo clMemoInfo = new CMemoInfo();
		ConvertCursorToMemoInfo.ConvertItem( cursor ,clMemoInfo);
		
		m_AdapterComPro.bindView(view,cbView,clMemoInfo,m_isSelectableStyle,m_isFolderSelectable,m_ListCheckBoxMapItem,m_ListItemDetail,m_ListItemSelectResult );
	}
 
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = null;
		v = m_inflater.inflate(R.layout.notelistitem, parent, false);
		CheckBox cbView = (CheckBox) v.findViewById(R.id.notelistitem_noteselect);
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
    				ItemDetail detail = m_ListItemDetail.get(mapItem.itemView);
    				if(detail!=null){
    					result.bIsHaveAudioData = detail.bIsHaveAudioData;
    					result.strAudioFileName = detail.strAudioFileName;
    				}
    				m_ListItemSelectResult.put(String.valueOf(mapItem.iDBRecID), result);
    			}	        
        	}
		});

		return v;
	}
	public void clearSelectResult(){
		m_ListItemSelectResult.clear();
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
	public void getSelectItemDBID(ArrayList<DetailInfoOfSelectItem> alIDs){
		Iterator iter = m_ListItemSelectResult.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
		    ItemSelectResult val = (ItemSelectResult)entry.getValue(); 
		    if(val.bIsSelected){
		    	DetailInfoOfSelectItem detail = new DetailInfoOfSelectItem();
		    	detail.iDBRecID = Integer.valueOf((String)entry.getKey());
		    	detail.bIsHaveAudioData = val.bIsHaveAudioData;
		    	detail.strAudioFileName = val.strAudioFileName;
		    	alIDs.add(detail);
		    }
		} 
	}
	public boolean isFolder(View view){
		ItemDetail detail = m_ListItemDetail.get(view);
		return detail.bIsFolder;
	}
//	public int getListPreDBID(){
//		return m_listPreDBID;
//	}
//	public int getIsRemind(){
//		return m_isRemind;
//	}

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
