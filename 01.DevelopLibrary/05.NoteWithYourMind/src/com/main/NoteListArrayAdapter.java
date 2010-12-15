package com.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
	private Calendar m_c;
	private CNoteDBCtrl m_clCNoteDBCtrl;
	private int m_listPreDBID;
	private int m_isRemind;
	private View m_DialogView;
	//private NoteListUICtrl m_NoteListUICtrl;
	//private String  m_strPassWord;
	

    protected LayoutInflater mInflater;
    private static final int mResource = R.layout.notelistitem; 

    public NoteListArrayAdapter(Context context, List<CMemoInfo> items) {
        super(context, mResource, items);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
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
        		//����CheckBox��DBID�Ķ�Ӧ��ϵ������DBID��ѡ��״̬
    			CheckBoxMapItem mapItem = m_ListCheckBoxMapItem.get(buttonView);
    			if(mapItem!=null){
    				result.iDBRecID = mapItem.iDBRecID;
    				m_ListItemSelectResult.put(String.valueOf(mapItem.iDBRecID), result);
    			}	        
        	}
		});

		TextView tV = (TextView)view.findViewById(R.id.notelistitem_notetext);
		TextView tvDate = (TextView)view.findViewById(R.id.notelistitem_notedate);
		
		int iTypeValue = getItem(position).iType;
		String sDetail = getItem(position).strDetail;
		
		ItemDetail itemdetail = new ItemDetail();
		itemdetail.vView = view;
		int iIDValue = getItem(position).iId;
		itemdetail.iDBRecID = iIDValue;

		/*step1����CheckBox��״̬
		 * �����ѡ��״̬
		 * 		�����¼�Ǳ�ǩ
		 * 			�����ڿؼ����أ���CheckBox��ʾ
		 * 		�����¼���ļ���
		 * 			����ļ����ǿ�ѡ��״̬
		 * 				�����ڿؼ����أ���CheckBox��ʾ
		 * ���������״̬
		 * 		��CheckBox���أ������ڿؼ���ʾ
		 */
		int iEncodeFlag = getItem(position).iIsEncode;
		if(m_isSelectableStyle){
			boolean bDisplay = true;
			if(iTypeValue==CMemoInfo.Type_Folder){
				if(m_isFolderSelectable){
					if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
						bDisplay=false;
					}else{
					}
				}else{
					bDisplay = false;
				}
			}else{
			}
			if(bDisplay){
				cbView.setVisibility(View.VISIBLE);
				//tvDate.setVisibility(View.GONE);
				//update CheckBox Status-begin
				//���ݱ���ĵ�ǰ��¼��ѡ��״̬������CheckBox
				ItemSelectResult result = m_ListItemSelectResult.get(String.valueOf(iIDValue));
				if(result!=null){
					cbView.setChecked(result.bIsSelected);
				}else{
					cbView.setChecked(false);
				}
				//update CheckBox Status-end
			}else{
				cbView.setVisibility(View.GONE);
			}
		}else{
			cbView.setVisibility(View.GONE);
			//tvDate.setVisibility(View.VISIBLE);
		}
		/*step2����LittleIcon
		 * ������ļ���
		 * 		λ��1���ļ���Icon
		 * 		��������˲鿴��
		 * 			λ��2����ͷIcon
		 * ����Ǳ�ǩ
		 * 		���ı����ݣ�λ��1���ı�Icon
		 * 		������ʱ�䣬λ��2������Icon
		 * 		���������ݣ�λ��3������Icon
		 */
		Button icon1 = (Button) view.findViewById(R.id.notelistitem_icon1);
		Button icon2 = (Button) view.findViewById(R.id.notelistitem_icon2);
		Button icon3 = (Button) view.findViewById(R.id.notelistitem_icon3);
		if(iTypeValue==CMemoInfo.Type_Folder){
			long Count = CommonDefine.m_clCNoteDBCtrl.getRecCountInFolder(iIDValue);
			String strCount = "("+String.valueOf(Count)+")";
			tvDate.setText(strCount);

			itemdetail.bIsFolder = true;
			icon1.setBackgroundResource(R.drawable.notelistitem_icon_folder);
			if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
				icon2.setBackgroundResource(R.drawable.notelistitem_icon_lock);
				icon3.setBackgroundDrawable(null);
				itemdetail.bIsEncode = true;
			}else{
				icon2.setBackgroundDrawable(null);
				icon3.setBackgroundDrawable(null);
				itemdetail.bIsEncode = false;
			}
		}else{
			tvDate.setText("");
			icon1.setBackgroundResource(R.drawable.notelistitem_icon_text);
			int isRemindValue = getItem(position).iIsRemind;
			if(isRemindValue == CMemoInfo.IsRemind_Yes){
				itemdetail.bIsRemind = true;
				icon2.setBackgroundResource(R.drawable.notelistitem_icon_alarm);
				if(false){//for voice
				}else{
					icon3.setBackgroundDrawable(null);
				}
			}else{
				if(false){//for voice
				}else{
					icon2.setBackgroundDrawable(null);
					icon3.setBackgroundDrawable(null);
				}
			}
		}
		/*step3���´洢����
		 * ����m_ListItemDetail
		 * 		DBID��Item�ؼ����Ƿ����ļ��С��Ƿ������ѡ��Ƿ����ò鿴��
		 * ����m_ListCheckBoxMapItem
		 * 		DBID��CheckBox�ؼ�
		 */
		
		//����CheckBox��DBID�Ķ�Ӧ��ϵ
		CheckBoxMapItem mapItem = new CheckBoxMapItem();
		mapItem.checkBox = cbView;
		mapItem.iDBRecID = iIDValue;
		m_ListCheckBoxMapItem.put(cbView, mapItem);

		//����Item�ؼ��Ͷ�Ӧ��DB��¼��Ϣ�Ķ�Ӧ��ϵ
		m_ListItemDetail.put(view, itemdetail);
		
		tV.setText(sDetail);

        return view;
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
	public int getListDBID(View view){
		ItemDetail detail = m_ListItemDetail.get(view);
		return detail.iDBRecID;
	}

	public boolean getListIsEncode(View view){
		ItemDetail detail = m_ListItemDetail.get(view);
		return detail.bIsEncode;
	}

}
