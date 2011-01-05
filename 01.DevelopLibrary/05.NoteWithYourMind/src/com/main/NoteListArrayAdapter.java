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
	private CNoteDBCtrl m_clCNoteDBCtrl;
	private int m_listPreDBID;
	private int m_isRemind;
	private View m_DialogView;
	//private NoteListUICtrl m_NoteListUICtrl;
	//private String  m_strPassWord;
//	private int m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;	
	private String  m_SearchKeyWord = "";
	private AdapterCommonProcess m_AdapterComPro;	

    private static final int mResource = R.layout.notelistitem; 

    public NoteListArrayAdapter(Context context, List<CMemoInfo> items) {

        super(context, mResource, items);
		m_inflater = LayoutInflater.from(context);

		m_ListItemSelectResult = new HashMap<String,ItemSelectResult>();
		m_ListCheckBoxMapItem = new HashMap<CheckBox,CheckBoxMapItem>();
		m_ListItemDetail = new HashMap<View,ItemDetail>();
		m_listPreDBID = CommonDefine.g_int_Invalid_ID;
		m_isRemind = CommonDefine.g_int_Invalid_ID;
		m_AdapterComPro = new AdapterCommonProcess(context);
		
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


	public void setSearchKeyWord(String str){
		m_SearchKeyWord = str;
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

