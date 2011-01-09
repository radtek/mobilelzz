package com.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.app.AlertDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import android.content.DialogInterface;
import android.widget.EditText;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.ImageButton;

public class NoteListArrayAdapter extends ArrayAdapter<CMemoInfo> {

	private HashMap<String,ItemSelectResult> m_ListItemSelectResult;
	private HashMap<CheckBox,CheckBoxMapItem> m_ListCheckBoxMapItem;
	private HashMap<View,ItemDetail> m_ListItemDetail;
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
	
	private List<CMemoInfo> m_arrayItems;

    private static final int mResource = R.layout.notelistitem; 

    public NoteListArrayAdapter(Context context, Cursor cur, List<CMemoInfo> items) {

        super(context, mResource, items);
		m_inflater = LayoutInflater.from(context);
		m_cursor = cur;
		m_arrayItems = items;
		m_ListItemSelectResult = new HashMap<String,ItemSelectResult>();
		m_ListCheckBoxMapItem = new HashMap<CheckBox,CheckBoxMapItem>();
		m_ListItemDetail = new HashMap<View,ItemDetail>();
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
		int iDBID = m_arrayItems.get(i).iId;
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
        } else {
            view = convertView;
        }

		CheckBox cbView = (CheckBox) view.findViewById(R.id.notelistitem_noteselect);
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
    					result.bIsFolder = detail.bIsFolder;
    				}
    				m_ListItemSelectResult.put(String.valueOf(mapItem.iDBRecID), result);
    			}	        
        	}
		});


		CMemoInfo clMemoInfo = new CMemoInfo();

		clMemoInfo	=getItem(position);	
		
		m_AdapterComPro.bindView(view,cbView,clMemoInfo,m_isSelectableStyle,m_isFolderSelectable,m_ListCheckBoxMapItem,m_ListItemDetail,m_ListItemSelectResult );


        return view;
    }


	public void filterListByKeyWord(String str){
		m_SearchKeyWord = str;

		int Count = m_arrayItems.size();
		for(int i=Count-1;i>=0;i--)
		{			
			CMemoInfo clCMemoInfo	= m_arrayItems.get(i);
			String sDetail=clCMemoInfo.strDetail;
			if( sDetail.indexOf(str) ==  -1) 
			{
				m_arrayItems.remove(i);
			}
		}
	}

	public void clearSelectResult(){
		m_ListItemSelectResult.clear();
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
		Iterator iter = m_ListItemSelectResult.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
		    ItemSelectResult val = (ItemSelectResult)entry.getValue(); 
		    if(val.bIsSelected){
		    	DetailInfoOfSelectItem detail = new DetailInfoOfSelectItem();
		    	detail.iDBRecID = Integer.valueOf((String)entry.getKey());
		    	detail.bIsHaveAudioData = val.bIsHaveAudioData;
		    	detail.strAudioFileName = val.strAudioFileName;
		    	detail.bIsFolder = val.bIsFolder;
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
	
}

