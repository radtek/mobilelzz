package com.main;

import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CheckBox;

class CommonDefine{
	public	enum	ToolbarStatusEnum
	{
		ToolbarStatus_Normal,
		ToolbarStatus_Delete,
		ToolbarStatus_Move
	}
	public static ToolbarStatusEnum 		g_enToolbarStatus = ToolbarStatusEnum.ToolbarStatus_Normal;
	public static int 						g_int_Invalid_ID 	= -1;
	public static int 						g_int_Invalid_Time 	= -1;
	public static int 						g_int_ListItemHeight = 65;
	public static String 					g_str_PassWord = "";
	public static String 					ExtraData_EditNoteID        =    "com.main.ExtraData_EditNoteID"; 
	public static String 					ExtraData_Remind_Flg        =    "com.main.ExtraData_Remind_Flg"; 
	public static String 					ExtraData_Remind_File		=    "ExtraData_Remind_File"; 
	public static boolean 					g_bool_IsPassWordChecked = false;

	private static	CNoteDBCtrl				m_clCNoteDBCtrl = null;
	
	public final static int					g_iMaxRecTime = 300;
	
	public static int 						E_FAIL	= -1;
	public static int 						S_OK	= 0;
	public static int 						Invalid_Value	= -1;
	

	public static int 						Remind_Type_Invalid		=	-1;
	public static int 						Remind_Type_CountDown	=	1;
	public static int 						Remind_Type_Week		=	2;
	public static int 						Remind_Type_Once		=	3;
	
	public static int 						WeekInvalid				=	-1;
	public static int 						WeekNext				=	1;
	public static int 						WeekCurrent				=	2;
	
	public static long 						Minute_5					=	1000 * 60 * 5;
	public static long 						Minute_10					=	1000 * 60 * 10;
	public static long 						Minute_15					=	1000 * 60 * 15;
	public static long 						Minute_20					=	1000 * 60 * 20;
	public static long 						Minute_30					=	1000 * 60 * 30;
	public static long 						Minute_60					=	1000 * 60 * 60;
	public static long 						Minute_120					=	1000 * 60 * 120;
	
	public static int 						iWorking					=	1;
	public static int 						iNotWorking					=	0;
	
	private static MediaPhoneCallListener 	m_MediaPhoneCallListener = null;
	
	static public CNoteDBCtrl getNoteDBCtrl(Context context){
		if(m_clCNoteDBCtrl==null){
			m_clCNoteDBCtrl = new CNoteDBCtrl(context);
		}else{
			
		}
		return m_clCNoteDBCtrl;
	}
	
	static public MediaPhoneCallListener getMediaPhoneCallListener(Context context){
		if(m_MediaPhoneCallListener==null){
			m_MediaPhoneCallListener = new MediaPhoneCallListener();
			m_MediaPhoneCallListener.initSource();
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			tm.listen(m_MediaPhoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);
		}else{
			
		}
		return m_MediaPhoneCallListener;
	}

	static public void  PrepareProc(CMemoInfo object){

		if(object.iIsRemind == CMemoInfo.IsRemind_Invalid ){
			object.iIsRemind = CMemoInfo.IsRemind_No;
		}

		if(object.iIsEncode == CMemoInfo.IsEncode_Invalid ){
			object.iIsEncode = CMemoInfo.IsEncode_No;
		}
		if(object.iIsHaveAudioData == CMemoInfo.IsHaveAudioData_Invalid ){
			object.iIsHaveAudioData = CMemoInfo.IsHaveAudioData_No;
		}

	}
	
}

interface MediaStatusControl{
	public void pauseMediaInteract();
	public void resumeMediaInteract();
}
class MediaPhoneCallListener extends PhoneStateListener{
	private ArrayList<MediaStatusControl> m_mediaControl = null;
	public void advise(MediaStatusControl mediaControl){
		m_mediaControl.add(mediaControl);
	}
	public void initSource(){
		m_mediaControl = new ArrayList<MediaStatusControl>();
	}
	public void unadvise(MediaStatusControl mediaControl){
		m_mediaControl.remove(mediaControl);
	}
	public void onCallStateChanged(int state, String incomingNumber){
		switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				if(m_mediaControl!=null){
					for(int i = 0; i < m_mediaControl.size(); i++){
						MediaStatusControl temp = m_mediaControl.get(i);
						if(temp!=null){
							temp.resumeMediaInteract();	
						}
					}
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
			case TelephonyManager.CALL_STATE_RINGING:
				if(m_mediaControl!=null){
					for(int i = 0; i < m_mediaControl.size(); i++){
						MediaStatusControl temp = m_mediaControl.get(i);
						if(temp!=null){
							temp.pauseMediaInteract();	
						}
					}
				}
				break;
		}
		super.onCallStateChanged(state, incomingNumber);
	}
}

class CommonContainer{
	private boolean b1 = false;
	public boolean getBOOL(){
		return b1;
	}
	public void setBOOL(boolean b){
		b1 = b;
	}
}

class ListUICtrlParam{

	ListUICtrlParam(){
		g_bool_IsRemindSearch = false;
		g_bool_IsVoiceSearch = false;
//		g_bool_IsTextSearch = false;
		g_str_SearchKey = "";
		g_int_PreID = -1;
		g_enListType = ListTypeEnum.ListType_NormalList;
		g_enSortType = ListSortTypeEnum.SortType_Invalid;
	}
	public void copy(ListUICtrlParam source){
		g_bool_IsRemindSearch = source.g_bool_IsRemindSearch;
		g_bool_IsVoiceSearch = source.g_bool_IsVoiceSearch;
		g_str_SearchKey = source.g_str_SearchKey;
		g_int_PreID = source.g_int_PreID;
		g_enListType = source.g_enListType;
		g_enSortType = source.g_enSortType;
	}
	public	enum	ListTypeEnum
	{
		ListType_NormalList,          //通过指定PreID，来提供PreID对应的子List
		ListType_SearchResultList,   //通过设定检索条件，来提供检索结果List
	}

	public	enum	ListSortTypeEnum
	{
		SortType_LastModify,
		SortType_RemindFirst,
		SortType_VoiceFirst,
		SortType_TextFirst,
		SortType_ForRootList,
		SortType_Invalid,
	}

	public ListTypeEnum g_enListType;
	public ListSortTypeEnum g_enSortType;
	
	public int g_int_PreID;
	
	public boolean g_bool_IsRemindSearch;
	public boolean g_bool_IsVoiceSearch;
//	public boolean g_bool_IsTextSearch;	
	public String g_str_SearchKey;
}

class SortByRemindFirst implements  Comparator<ArrayListItem> {

	public int  compare(ArrayListItem item1, ArrayListItem item2) {
		CMemoInfo object1  = item1.clDBRecInfo;
		CMemoInfo object2  = item2.clDBRecInfo;
		CommonDefine.PrepareProc(object1);
		CommonDefine.PrepareProc(object2);		

		if(object1.iIsRemind != object2.iIsRemind ){
			return (object1.iIsRemind - object2.iIsRemind);	
		}
		else if( object1.iIsRemind == CMemoInfo.IsRemind_No ){
			return (int)(object2.dLastModifyTime - object1.dLastModifyTime);
		}
		else if( object1.iIsRemind == CMemoInfo.IsRemind_Yes){

			if(object1.iIsRemindAble != object2.iIsRemindAble ){
				return (object2.iIsRemindAble - object1.iIsRemindAble);	
			}
			else if( object1.iIsRemindAble == CMemoInfo.IsRemind_Able_No){
				return (int)(object2.dLastModifyTime - object1.dLastModifyTime);
			}
			else if( object1.iIsRemindAble == CMemoInfo.IsRemind_Able_Yes){
				return (CompareByRemindTime(object1, object2));
			}
		}

		return 0;	

	}
		
	private int  CompareByRemindTime(CMemoInfo object1, CMemoInfo object2) {

		long T1=0;
		long T2=0;
			
		if(( object1.RemindType == CommonDefine.Remind_Type_CountDown )
			||( object1.RemindType == CommonDefine.Remind_Type_Once ))
		{

			T1	=	object1.dRemindTime;

		}
		else if( object1.RemindType == CommonDefine.Remind_Type_Week ){

			CRemindInfo	clCRemindInfo	=	new	CRemindInfo( CommonDefine.Remind_Type_Week );
			
			clCRemindInfo.setWeekTime( object1 );

			T1	=	clCRemindInfo.getFirstCycelRemindTime( null );


		}

		if(( object2.RemindType == CommonDefine.Remind_Type_CountDown )
			||( object2.RemindType == CommonDefine.Remind_Type_Once ))
		{

			T2	=	object2.dRemindTime;

		}
		else if( object2.RemindType == CommonDefine.Remind_Type_Week ){

			CRemindInfo	clCRemindInfo	=	new	CRemindInfo( CommonDefine.Remind_Type_Week );
			
			clCRemindInfo.setWeekTime( object2 );

			T2	=	clCRemindInfo.getFirstCycelRemindTime( null );
		}		

		return (int)( T1 -T2 );


	}


		
}

class SortForRootList implements  Comparator<ArrayListItem> {
	private Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
	public int  compare(ArrayListItem item1, ArrayListItem item2) {
		CMemoInfo object1  = item1.clDBRecInfo;
		CMemoInfo object2  = item2.clDBRecInfo;
		int iRet = 0;
		CommonDefine.PrepareProc(object1);
		CommonDefine.PrepareProc(object2);		
		if(object1.iType == CMemoInfo.Type_Folder && object2.iType == CMemoInfo.Type_Folder){
//			iRet = object1.strDetail.compareTo(object2.strDetail);
//			iRet = cmp.compare(object1.strDetail, object2.strDetail);
			iRet = HanZiStringCompare.compare(object1.strDetail, object2.strDetail, "gb2312");
		}else if(object1.iType == CMemoInfo.Type_Memo && object2.iType == CMemoInfo.Type_Memo){
			
		}else{
			iRet = object1.iType == CMemoInfo.Type_Folder ? -1:1;
		}
		return iRet;

	}
}


//通过便签修改时间排序，最新的修改的，排在最上面
class SortByLastModifyTime implements  Comparator<ArrayListItem> {

	public int  compare(ArrayListItem item1, ArrayListItem item2) {
		CMemoInfo object1  = item1.clDBRecInfo;
		CMemoInfo object2  = item2.clDBRecInfo;

		return (int)(object2.dLastModifyTime - object1.dLastModifyTime);	

	}

}


class SortByVoiceFirst implements  Comparator<ArrayListItem> {

	public int  compare(ArrayListItem item1, ArrayListItem item2) {
		CMemoInfo object1  = item1.clDBRecInfo;
		CMemoInfo object2  = item2.clDBRecInfo;
		CommonDefine.PrepareProc(object1);
		CommonDefine.PrepareProc(object2);

		if(object1.iIsHaveAudioData != object2.iIsHaveAudioData ){
			return (object2.iIsHaveAudioData - object1.iIsHaveAudioData);	
		}
		else if( object1.iIsHaveAudioData == CMemoInfo.IsHaveAudioData_No ){
			return (int)(object2.dLastModifyTime - object1.dLastModifyTime);
		}
		else if( object1.iIsHaveAudioData == CMemoInfo.IsHaveAudioData_Yes){
			return (int)(object2.dLastModifyTime - object1.dLastModifyTime);
		}
		return 0;	
	}
}

class SortByTextFirst implements  Comparator<ArrayListItem> {

	public int  compare(ArrayListItem item1, ArrayListItem item2) {
		CMemoInfo object1  = item1.clDBRecInfo;
		CMemoInfo object2  = item2.clDBRecInfo;
		CommonDefine.PrepareProc(object1);
		CommonDefine.PrepareProc(object2);

		int mask1=weighting(object1);
		int mask2=weighting(object2);

		if( (mask1 ==0) && (mask2 ==0)){
			//都是文本
			return (int)(object2.dLastModifyTime - object1.dLastModifyTime);
		}
		if(( (mask1 ==0) && (mask2 !=0))
			||( (mask1 !=0) && (mask2 ==0)))
		{
			//一个是文本，一个不是文本
			return (mask1 - mask2);
		}
		else{
			//都不是文本			
			return (int)(object2.dLastModifyTime - object1.dLastModifyTime);
		}

	}

	private int weighting( CMemoInfo object ){

		int mask = 0;
		if( object.iIsRemind == CMemoInfo.IsRemind_Yes){
			mask |= 0x00000001;
		}
		if( object.iIsHaveAudioData == CMemoInfo.IsHaveAudioData_Yes ){
			mask |= 0x00000010;
		}	

		return mask;
	}
}
	

class ConvertCursorToMemoInfo {

	static public void ConvertItems( Cursor cursor ,List<ArrayListItem> Items)
	{
		if ( cursor.getCount() > 0 )
		{			
			cursor.moveToFirst();			
			do
			{
				ArrayListItem clItem	=	new	ArrayListItem();
				ConvertItem(cursor,clItem.clDBRecInfo);

				Items.add(clItem);	
					
			}while( cursor.moveToNext() );			
		}

	}	

	static public void ConvertItem( Cursor cursor ,CMemoInfo clCMemoInfo)
	{
	
		int		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_id );
		clCMemoInfo.iId=	cursor.getInt( iColumn );
		
		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_preid );
		clCMemoInfo.iPreId =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_type );
		clCMemoInfo.iType =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_isremind );
		clCMemoInfo.iIsRemind =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_remindtime );
		clCMemoInfo.dRemindTime =	cursor.getLong( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_isremindable );
		clCMemoInfo.iIsRemindAble =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_remindtype );
		clCMemoInfo.RemindType =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_createtime );
		clCMemoInfo.dCreateTime =	cursor.getLong( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_lastmodifytime );
		clCMemoInfo.dLastModifyTime =	cursor.getLong( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_iseditenable );
		clCMemoInfo.iIsEditEnable =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_detail );
		clCMemoInfo.strDetail =	cursor.getString( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_password );
		clCMemoInfo.strPassword=	cursor.getString( iColumn );			

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_isencode );
		clCMemoInfo.iIsEncode =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_monday );
		int iMonday =	cursor.getInt( iColumn );
		clCMemoInfo.m_Week[ 0 ]	=	(byte)iMonday;

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_tuesday );
		int iTuesday =	cursor.getInt( iColumn );
		clCMemoInfo.m_Week[ 1 ]	=	(byte)iTuesday;

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_wednesday );
		int iWednesday =	cursor.getInt( iColumn );
		clCMemoInfo.m_Week[ 2 ]	=	(byte)iWednesday;

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_thursday );
		int iThursday =	cursor.getInt( iColumn );
		clCMemoInfo.m_Week[ 3 ]	=	(byte)iThursday;

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_friday );
		int iFriday =	cursor.getInt( iColumn );
		clCMemoInfo.m_Week[ 4 ]	=	(byte)iFriday;

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_staturday );
		int iStaturday =	cursor.getInt( iColumn );
		clCMemoInfo.m_Week[ 5 ]	=	(byte)iStaturday;

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_sunday );
		int iSunday =	cursor.getInt( iColumn );
		clCMemoInfo.m_Week[ 6 ]	=	(byte)iSunday;	

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_isHaveAudioData );
		clCMemoInfo.iIsHaveAudioData =	cursor.getInt( iColumn );


		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_audioDataName );
		clCMemoInfo.strAudioFileName=	cursor.getString( iColumn );
		
		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_isRing );
		clCMemoInfo.iRing =	cursor.getInt( iColumn );

		iColumn		=	cursor.getColumnIndex( CNoteDBCtrl.KEY_isVibrate );
		clCMemoInfo.iVibrate =	cursor.getInt( iColumn );
							
	}	

}
class HanZiStringCompare {  
    public static int compare(String s1, String s2, String charset) {  
    	int iRet = 0;
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray(); 
        int length = c1.length > c2.length? c2.length:c1.length;
        int i = 0;
        for ( ; i < length - 1; i++) {  
        	iRet = getHexString(String.valueOf(c1[i]), charset)  
            .compareTo(  
                    getHexString(String.valueOf(c2[i]),  
                            charset));
            if ( iRet != 0) {  
                break;
            }  
        }  
        if(i == length){
        	iRet = c1.length > c2.length? 1:-1;
        }
        return iRet;  
    }  
    public static String getHexString(String s, String charset) {  
        byte[] b = null;  
        StringBuffer sb = new StringBuffer();  
        try {  
            b = s.getBytes(charset);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        for (int i = 0; i < b.length; i++) {  
            sb.append(Integer.toHexString(b[i] & 0xFF));  
        }  
        return sb.toString();  
    }  
}  