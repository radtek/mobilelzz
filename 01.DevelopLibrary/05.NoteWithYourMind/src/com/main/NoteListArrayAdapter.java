package com.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

class ArrayListItem{
	CMemoInfo clDBRecInfo;
	boolean bIsSelected;
	ArrayListItem(){
		clDBRecInfo = new CMemoInfo();
		bIsSelected = false;
	}
}

public class NoteListArrayAdapter extends ArrayAdapter<ArrayListItem> {

	private HashMap<CheckBox,CheckBoxMapItem> m_ListCheckBoxMapItem;
	private boolean m_isSelectableStyle = false;
	private boolean m_isFolderSelectable = true;
	private LayoutInflater m_inflater;
	private Calendar m_c;
	private Cursor m_cursor;
	private CNoteDBCtrl m_clCNoteDBCtrl;
	private int m_listPreDBID;
	private int m_isRemind;
	private View m_DialogView;
	//private NoteListUICtrl m_NoteListUICtrl;
	//private String  m_strPassWord;
//	private int m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;	
	private String  m_SearchKeyWord = "";
	private AdapterCommonProcess m_AdapterComPro;	
	
	private List<ArrayListItem> m_arrayItems;

    private static final int mResource = R.layout.notelistitem; 

    public NoteListArrayAdapter(Context context, Cursor cur, List<ArrayListItem> items) {

        super(context, mResource, items);
		m_inflater = LayoutInflater.from(context);
		m_cursor = cur;
		m_arrayItems = items;
		m_ListCheckBoxMapItem = new HashMap<CheckBox,CheckBoxMapItem>();
		m_listPreDBID = CommonDefine.g_int_Invalid_ID;
		m_isRemind = CommonDefine.g_int_Invalid_ID;
		m_AdapterComPro = new AdapterCommonProcess(context);
		
	}
    
    /*
     * 将Cursor中的数据转换到Array中
     */
    public void initData(){
    	ConvertCursorToMemoInfo.ConvertItems( m_cursor , m_arrayItems);
    }
    
	public void updateCursor(){
		m_cursor.deactivate();
		m_cursor.requery();
		m_arrayItems.clear();
		ConvertCursorToMemoInfo.ConvertItems( m_cursor , m_arrayItems);
	}
	
	public long getItemId(int i){
		int iDBID = m_arrayItems.get(i).clDBRecInfo.iId;
		return iDBID;
	}
    
    public void sortData(ListUICtrlParam.ListSortTypeEnum enSortType){
    	switch(enSortType){
		case SortType_LastModify:
			Collections.sort(m_arrayItems, new SortByLastModifyTime());
			break;
		case SortType_RemindFirst:
			Collections.sort(m_arrayItems, new SortByRemindFirst());
			break;
		case SortType_VoiceFirst:
			Collections.sort(m_arrayItems, new SortByVoiceFirst());							
			break;
		case SortType_TextFirst:
			Collections.sort(m_arrayItems, new SortByTextFirst());	
			break;
		case SortType_ForRootList:
			Collections.sort(m_arrayItems, new SortForRootList());	
			break;
		default:
//			Collections.sort(m_arrayItems, new SortByLastModifyTime());
			break;
		}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = m_inflater.inflate(mResource, parent, false);
            CheckBox cbView = (CheckBox) view.findViewById(R.id.notelistitem_noteselect);
    		cbView.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
    			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            	{
        			CheckBoxMapItem mapItem = m_ListCheckBoxMapItem.get(buttonView);
        			if(mapItem!=null){
        				ArrayListItem clItem	=getItem(mapItem.iPosition);
        				clItem.bIsSelected  = isChecked;
        			}	        
            	}
    		});
        } else {
            view = convertView;
        }
//		CMemoInfo clMemoInfo = new CMemoInfo();
        ArrayListItem clItem	=getItem(position);	
        CheckBox cbView = (CheckBox) view.findViewById(R.id.notelistitem_noteselect);
		CheckBoxMapItem mapItem = new CheckBoxMapItem();
		mapItem.checkBox = cbView;
		mapItem.iPosition = position;
		m_ListCheckBoxMapItem.put(cbView, mapItem);
		
		m_AdapterComPro.bindView(view,clItem,m_SearchKeyWord,m_isSelectableStyle,m_isFolderSelectable);

        return view;
    }


	public void filterListByKeyWord(String str){
		m_SearchKeyWord = str;

		int Count = m_arrayItems.size();
		for(int i=Count-1;i>=0;i--)
		{			
			CMemoInfo clCMemoInfo	= m_arrayItems.get(i).clDBRecInfo;
			String sDetail=clCMemoInfo.strDetail;
			if( sDetail.indexOf(str) ==  -1) 
			{
				m_arrayItems.remove(i);
			}
		}
	}

	public void clearSelectResult(){
		int count = m_arrayItems.size();
		for(int i = 0; i < count; i++){
			ArrayListItem item = m_arrayItems.get(i);
			item.bIsSelected = false;
		}
	}

	public void setSelectableStyle(boolean bEnable){
		m_isSelectableStyle = bEnable;
	}
	public void setFolderSelectable(boolean bEnable){
		m_isFolderSelectable = bEnable;
	}
	
	public void setNoteDBCtrl(CNoteDBCtrl clCNoteDBCtrl){
		m_clCNoteDBCtrl = clCNoteDBCtrl;
	}
	public void getSelectItemDBID(ArrayList<DetailInfoOfSelectItem> alIDs){
		int count = m_arrayItems.size();
		for(int i = 0; i < count; i++){
			ArrayListItem item = m_arrayItems.get(i);
			if(item.bIsSelected){
				DetailInfoOfSelectItem detail = new DetailInfoOfSelectItem();
		    	detail.iDBRecID = item.clDBRecInfo.iId;
		    	detail.bIsHaveAudioData = item.clDBRecInfo.iIsHaveAudioData == CMemoInfo.IsHaveAudioData_Yes?true:false;
		    	detail.strAudioFileName = item.clDBRecInfo.strAudioFileName;
		    	detail.bIsFolder = item.clDBRecInfo.iType == CMemoInfo.Type_Folder?true:false;
		    	alIDs.add(detail);
			}
		}
		
//		Iterator iter = m_ListItemSelectResult.entrySet().iterator(); 
//		while (iter.hasNext()) { 
//			Map.Entry entry = (Map.Entry) iter.next(); 
//		    ItemSelectResult val = (ItemSelectResult)entry.getValue(); 
//		    if(val.bIsSelected){
//		    	DetailInfoOfSelectItem detail = new DetailInfoOfSelectItem();
//		    	detail.iDBRecID = Integer.valueOf((String)entry.getKey());
//		    	detail.bIsHaveAudioData = val.bIsHaveAudioData;
//		    	detail.strAudioFileName = val.strAudioFileName;
//		    	detail.bIsFolder = val.bIsFolder;
//		    	alIDs.add(detail);
//		    }
//		} 
	}
	public boolean isFolder(int position){
		boolean bRet = false;
		CMemoInfo clMemoInfo	=getItem(position).clDBRecInfo;

		if(clMemoInfo.iType == CMemoInfo.Type_Folder){
			bRet = true;
		}
		
		return bRet;
	}
//	public int getListPreDBID(){
//		return m_listPreDBID;
//	}
//	public int getIsRemind(){
//		return m_isRemind;
//	}

	public int getListDBID(int position){
		CMemoInfo clMemoInfo	=getItem(position).clDBRecInfo;
		
		return clMemoInfo.iId;
	}

	public boolean getListIsEncode(int position){
		boolean bRet = false;
		CMemoInfo clMemoInfo	=getItem(position).clDBRecInfo;

		if(clMemoInfo.iIsEncode == CMemoInfo.IsEncode_Yes){
			bRet = true;
		}
		return bRet;
	}	
	
}

