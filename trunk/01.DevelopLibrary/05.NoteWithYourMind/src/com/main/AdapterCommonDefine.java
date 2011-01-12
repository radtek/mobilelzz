package com.main;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


class DetailInfoOfSelectItem{
	int iDBRecID;
	boolean bIsHaveAudioData;
	boolean bIsFolder;
	String strAudioFileName;
	DetailInfoOfSelectItem(){
		iDBRecID = CommonDefine.g_int_Invalid_ID;
		bIsHaveAudioData = false;
		bIsFolder = false;
		strAudioFileName = null;
	}
}

//class ItemSelectResult{
//	int iDBRecID;
//	boolean bIsSelected;
//	int iItemPos;
//	boolean bIsHaveAudioData;
//	boolean bIsFolder;
//	String strAudioFileName;
//	ItemSelectResult(){
//		iDBRecID = CommonDefine.g_int_Invalid_ID;
//		bIsSelected = false;
//		bIsHaveAudioData = false;
//		bIsFolder = false;
//		strAudioFileName = null;
//	}
//}
class CheckBoxMapItem{
	int iPosition;
	CheckBox checkBox;
//	View itemView;
	CheckBoxMapItem(){
		iPosition = CommonDefine.g_int_Invalid_ID;
		checkBox = null;
//		itemView = null;
	}
}
//class ItemDetail{
//	int iDBRecID;
//	View vView;
//	boolean bIsFolder;
//	boolean bIsRemind;
//	boolean bIsEncode;
//	boolean bIsHaveAudioData;
//	String strAudioFileName;
//	ItemDetail(){
//		iDBRecID = CommonDefine.g_int_Invalid_ID;
//		vView = null;
//		bIsFolder = false;
//		bIsRemind = false;
//		bIsEncode = false;
//		bIsHaveAudioData = false;
//		strAudioFileName = null;
//	}
//}

class AdapterCommonProcess{


	private Context m_context;

	AdapterCommonProcess( Context context){
		m_context = context;

	}

	
	public void bindView ( View view , ArrayListItem clItem,
			String  strSearchKeyWord, boolean isSelectableStyle,boolean isFolderSelectable
			){
		CheckBox cbView = (CheckBox) view.findViewById(R.id.notelistitem_noteselect);
		
		TextView tV = (TextView)view.findViewById(R.id.notelistitem_notetext);
		TextView tvDate = (TextView)view.findViewById(R.id.notelistitem_notedate);
		CMemoInfo clMemoInfo = clItem.clDBRecInfo;
		long	lLastModify		=	clMemoInfo.dLastModifyTime;
		
		tvDate.setText(CMemoInfo.getTimeForListItem(lLastModify));

		int iTypeValue = clMemoInfo.iType;

		String sDetail = clMemoInfo.strDetail;;
		
		int iIDValue = clMemoInfo.iId;

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
		int iEncodeFlag = clMemoInfo.iIsEncode;

		if(isSelectableStyle){
			boolean bDisplay = true;
			if(iTypeValue==CMemoInfo.Type_Folder){
				if(isFolderSelectable){
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
				cbView.setChecked(clItem.bIsSelected);
				
			}else{
				cbView.setVisibility(View.GONE);
			}
		}else{
			cbView.setVisibility(View.GONE);
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
			if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
				icon1.setBackgroundResource(R.drawable.notelistitem_icon_lock);
				icon1.setVisibility(View.VISIBLE);
				icon2.setBackgroundDrawable(null);
				icon2.setVisibility(View.GONE);
				countTV.setText("");
			}else{
				icon1.setBackgroundDrawable(null);
				icon2.setBackgroundDrawable(null);
				icon1.setVisibility(View.GONE);
				icon2.setVisibility(View.GONE);
				long Count = CommonDefine.getNoteDBCtrl(m_context).getRecCountInFolder(iIDValue);
				String strCount = "("+String.valueOf(Count)+")";
				countTV.setText(strCount);
			}
		}else{
			countTV.setText("");
			itemV.setBackgroundColor(Color.argb(160, 255, 255, 255));
//			bigIcon.setBackgroundResource(R.drawable.notelistitem_bigicon_text);
			int isRemindValue = clMemoInfo.iIsRemind;

			int isRemindableValue = clMemoInfo.iIsRemindAble;

			int iVoiceFlag = clMemoInfo.iIsHaveAudioData;

			String strAudioFileName = clMemoInfo.strAudioFileName;

			if(isRemindValue == CMemoInfo.IsRemind_Yes){
				if(isRemindableValue==CMemoInfo.IsEditEnable_Enable){
					icon1.setBackgroundResource(R.drawable.notelistitem_icon_alarm_valid);
				}else{
					icon1.setBackgroundResource(R.drawable.notelistitem_icon_alarm_invalid);
				}
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

		
		if( strSearchKeyWord != null ){
			if( strSearchKeyWord.length() > 0 ){
				SpannableStringBuilder style=new SpannableStringBuilder(sDetail);
				int keyLength = strSearchKeyWord.length();
				int MemoLength = sDetail.length();
				int Pos = 0;
				int lastFindPos = -keyLength;

				while(( MemoLength-(lastFindPos+keyLength))>= keyLength ){					
					Pos = sDetail.indexOf(strSearchKeyWord,(lastFindPos+keyLength) );
					if( Pos !=  -1) {						
						style.setSpan(new ForegroundColorSpan(Color.argb(255, 0, 255, 255)),Pos,(Pos+keyLength),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						lastFindPos = Pos;
					}else{
						break;
					}
				}
					
				tV.setText(style);
			}else{
				tV.setText( sDetail );
			}
		}
		else{
			tV.setText( sDetail );
		}

		return ;
	}
	

}
