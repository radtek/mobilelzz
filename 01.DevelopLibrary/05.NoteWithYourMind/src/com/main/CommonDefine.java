package com.main;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

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
	public static boolean 					g_bool_IsPassWordChecked = false;

	private static	CNoteDBCtrl				m_clCNoteDBCtrl = null;
	
	public static String 					g_strAudioFilePath = "/record";
	public static String 					g_strAppFilePath = "/note";
	public final static int					g_iMaxRecTime = 300;
	
	public static int 						E_FAIL	= -1;
	public static int 						S_OK	= 0;
	public static int 						Invalid_Value	= -1;
	

	public static int 						Remind_Type_Invalid		=	-1;
	public static int 						Remind_Type_CountDown	=	1;
	public static int 						Remind_Type_Week		=	2;
	public static int 						Remind_Type_Once		=	3;
	
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
		g_bool_IsTextSearch = false;
		g_str_SearchKey = "";
		g_int_PreID = -1;
		g_enListType = ListTypeEnum.ListType_NormalList;
		g_enSortType = ListSortTypeEnum.SortType_Normal;
	}
	public	enum	ListTypeEnum
	{
		ListType_NormalList,          //通过指定PreID，来提供PreID对应的子List
		ListType_SearchResultList,   //通过设定检索条件，来提供检索结果List
	}

	public	enum	ListSortTypeEnum
	{
		SortType_Normal,
		SortType_RemindFirst,
		SortType_VoiceFirst,
		SortType_TextFirst,
	}

	public ListTypeEnum g_enListType;
	public ListSortTypeEnum g_enSortType;
	
	public int g_int_PreID;
	
	public boolean g_bool_IsRemindSearch;
	public boolean g_bool_IsVoiceSearch;
	public boolean g_bool_IsTextSearch;	
	public String g_str_SearchKey;
}

class SortByRemindFirst implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {


		if(0 != CompareByRemindFirst(object1,object2)){
			return CompareByRemindFirst(object1,object2);	
		}

		return CompareByRemindTime(object1,object2);	

	}
	private int  CompareByRemindFirst(CMemoInfo object1, CMemoInfo object2) {
		return ( object1.iIsRemind - object2.iIsRemind );	
	}
		
	private int  CompareByRemindTime(CMemoInfo object1, CMemoInfo object2) {
		if(( object1.iIsRemind!=CMemoInfo.IsRemind_No )
			&& ( object2.iIsRemind!=CMemoInfo.IsRemind_No )){
			return 0;
		}

		if(( object1.iIsRemindAble!=CMemoInfo.IsRemind_Able_No )
			&& ( object2.iIsRemindAble!=CMemoInfo.IsRemind_Able_No )){

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

				T1	=	clCRemindInfo.getFirstCycelRemindTime();


			}

			if(( object2.RemindType == CommonDefine.Remind_Type_CountDown )
				||( object2.RemindType == CommonDefine.Remind_Type_Once ))
			{

				T2	=	object2.dRemindTime;

			}
			else if( object2.RemindType == CommonDefine.Remind_Type_Week ){

				CRemindInfo	clCRemindInfo	=	new	CRemindInfo( CommonDefine.Remind_Type_Week );
				
				clCRemindInfo.setWeekTime( object2 );

				T2	=	clCRemindInfo.getFirstCycelRemindTime();
			}		

			return (int)( T1 -T2 );

		}

		return ( object2.iIsRemindAble - object1.iIsRemindAble );	
	}


}


//通过提醒时间排序，最近的提醒时间最优，排在最上面!!!!!!!!!!???????
class SortByRemindTime implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return 0;	
	}
}

//通过便签创建时间排序，最新的创建的，排在最上面
class SortByCreateTime implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		long temp = object1.dCreateTime - object2.dCreateTime;
		if( temp < 0){
			return 1;			
		}else if( temp > 0){
			return -1;			
		}else {
			return 0;			
		}
	}
}

//通过便签修改时间排序，最新的修改的，排在最上面
class SortByLastModifyTime implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		long temp = object1.dLastModifyTime - object2.dLastModifyTime;
		if( temp < 0){
			return 1;			
		}else if( temp > 0){
			return -1;			
		}else {
			return 0;			
		}
	}
}

//通过提醒类型排序，循环提醒最优，排在最上面   功能暂时不启用
class SortByRemindType implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return 0;	
	}
}


//通过是否是加密排序，加密最优，排在最上面
class SortByEncodeFirst implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return ( object1.iIsEncode - object2.iIsEncode );	
	}
}


//通过是否有语音排序，带语音最优，排在最上面 ？？？
class SortByVoiceFirst implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return 0;	
	}
}
