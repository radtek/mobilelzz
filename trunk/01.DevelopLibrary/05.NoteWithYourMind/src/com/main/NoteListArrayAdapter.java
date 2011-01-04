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
	private String  m_SearchKeyWord = "";	

    protected LayoutInflater mInflater;
    private static final int mResource = R.layout.notelistitem; 

    public NoteListArrayAdapter(Context context, List<CMemoInfo> items) {
        super(context, mResource, items);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		m_ListItemSelectResult = new HashMap<String,ItemSelectResult>();
		m_ListCheckBoxMapItem = new HashMap<CheckBox,CheckBoxMapItem>();
		m_ListItemDetail = new HashMap<View,ItemDetail>();
		m_listPreDBID = CommonDefine.g_int_Invalid_ID;
		m_isRemind = CommonDefine.g_int_Invalid_ID;
		
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
        		//根据CheckBox和DBID的对应关系，更新DBID的选择状态
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

		/*step1更新CheckBox的状态
		 * 如果是选择状态
		 * 		如果记录是便签
		 * 			将日期控件隐藏，将CheckBox显示
		 * 		如果记录是文件夹
		 * 			如果文件夹是可选择状态
		 * 				将日期控件隐藏，将CheckBox显示
		 * 如果是正常状态
		 * 		将CheckBox隐藏，将日期控件显示
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
				//根据保存的当前记录的选择状态，更新CheckBox
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
		/*step2更新LittleIcon
		 * 如果是文件夹
		 * 		位置1放文件夹Icon
		 * 		如果设置了查看锁
		 * 			位置2放锁头Icon
		 * 如果是便签
		 * 		有文本内容，位置1放文本Icon
		 * 		有提醒时间，位置2放提醒Icon
		 * 		有语音内容，位置3放语音Icon
		 */
		View itemV = view.findViewById(R.id.notelistitem);
		TextView countTV = (TextView) view.findViewById(R.id.notelistitem_count);
		Button icon1 = (Button) view.findViewById(R.id.notelistitem_icon1);
		Button icon2 = (Button) view.findViewById(R.id.notelistitem_icon2);
		if(iTypeValue==CMemoInfo.Type_Folder){
			itemV.setBackgroundResource(R.drawable.notelistitem_folder_background);
			long Count = CommonDefine.getNoteDBCtrl(m_context).getRecCountInFolder(iIDValue);
			String strCount = "("+String.valueOf(Count)+")";
			countTV.setText(strCount);
			itemdetail.bIsFolder = true;
			if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
				icon1.setBackgroundResource(R.drawable.notelistitem_icon_lock);
				icon1.setVisibility(View.VISIBLE);
				icon2.setBackgroundDrawable(null);
				icon2.setVisibility(View.GONE);
				itemdetail.bIsEncode = true;
			}else{
				icon1.setBackgroundDrawable(null);
				icon2.setBackgroundDrawable(null);
				icon1.setVisibility(View.GONE);
				icon2.setVisibility(View.GONE);
				itemdetail.bIsEncode = false;
			}
		}else{
			countTV.setText("");
			itemV.setBackgroundColor(Color.argb(160, 160, 160, 160));
			int isRemindValue = getItem(position).iIsRemind;
			int iVoiceFlag = getItem(position).iIsHaveAudioData;
			if(isRemindValue == CMemoInfo.IsRemind_Yes){
				itemdetail.bIsRemind = true;
				icon1.setBackgroundResource(R.drawable.notelistitem_icon_alarm_valid);
				icon1.setVisibility(View.VISIBLE);
				if(iVoiceFlag == CMemoInfo.IsHaveAudioData_Yes){//for voice
					icon2.setBackgroundResource(R.drawable.notelistitem_icon_voice);
					icon2.setVisibility(View.VISIBLE);
				}else{
					icon2.setBackgroundDrawable(null);
					icon2.setVisibility(View.GONE);
				}
			}else{
				if(iVoiceFlag == CMemoInfo.IsHaveAudioData_Yes){//for voice
					icon1.setBackgroundResource(R.drawable.notelistitem_icon_voice);
					icon1.setVisibility(View.VISIBLE);
					icon2.setBackgroundDrawable(null);
					icon2.setVisibility(View.GONE);
				}else{
					icon1.setBackgroundDrawable(null);
					icon2.setBackgroundDrawable(null);
					icon1.setVisibility(View.GONE);
					icon2.setVisibility(View.GONE);
				}
			}
		}
		/*step3更新存储数据
		 * 更新m_ListItemDetail
		 * 		DBID、Item控件、是否是文件夹、是否是提醒、是否设置查看锁
		 * 更新m_ListCheckBoxMapItem
		 * 		DBID、CheckBox控件
		 */
		
		//建立CheckBox和DBID的对应关系
		CheckBoxMapItem mapItem = new CheckBoxMapItem();
		mapItem.checkBox = cbView;
		mapItem.iDBRecID = iIDValue;
		m_ListCheckBoxMapItem.put(cbView, mapItem);

		//建立Item控件和对应的DB记录信息的对应关系
		m_ListItemDetail.put(view, itemdetail);
		
		if( m_SearchKeyWord.length() > 0 ){

			int Pos = sDetail.indexOf(m_SearchKeyWord);
			int keyLength = m_SearchKeyWord.length();

			if( Pos !=  -1) 
			{
				SpannableStringBuilder style=new SpannableStringBuilder(sDetail);
		        style.setSpan(new ForegroundColorSpan(Color.argb(255, 0, 255, 255)),Pos,(Pos+keyLength),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tV.setText(style);
			}else{
					//error
			}

		}else{
			tV.setText(sDetail);
		}

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

	public void setSearchKeyWord(String str){
		m_SearchKeyWord = str;
	}
	
}

