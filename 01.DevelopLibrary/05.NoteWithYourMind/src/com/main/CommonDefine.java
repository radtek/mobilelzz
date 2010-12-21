package com.main;

import java.util.Comparator;

class CommonDefine{
	public	enum	ToolbarStatusEnum
	{
		ToolbarStatus_Normal,
		ToolbarStatus_Delete,
		ToolbarStatus_Move
	}
	public static ToolbarStatusEnum 		g_enToolbarStatus = ToolbarStatusEnum.ToolbarStatus_Normal;
	public static int 						g_int_Invalid_ID = -1;
	public static int 						g_int_ListItemHeight = 65;
	public static String 					g_str_PassWord = "";
	public static boolean 					g_bool_IsPassWordChecked = false;

	public static	CNoteDBCtrl				m_clCNoteDBCtrl = null;
	
	public static String 					g_strAudioFilePath = "/note/record";
	public final static int					g_iMaxRecTime = 300;
	
	public static int 						E_FAIL	= -1;
	public static int 						S_OK	= 0;
	public static int 						Invalid_Value	= -1;
	

	public static int 						Remind_Type_Invalid		=	-1;
	public static int 						Remind_Type_CountDown	=	1;
	public static int 						Remind_Type_Week		=	2;
	public static int 						Remind_Type_Once		=	3;

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
	}
	public	enum	ListTypeEnum
	{
		ListType_NormalList,          //ͨ��ָ��PreID�����ṩPreID��Ӧ����List
		ListType_SearchResultList,   //ͨ���趨�������������ṩ�������List
	}
	public ListTypeEnum g_enListType;
	
	public int g_int_PreID;
	
	public boolean g_bool_IsRemindSearch;
	public boolean g_bool_IsVoiceSearch;
	public boolean g_bool_IsTextSearch;	
	public String g_str_SearchKey;
}


// ͨ���Ƿ������������������ţ�����������
class SortByRemindFirst implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return ( object1.iIsRemind - object2.iIsRemind );	
	}
}

//ͨ������ʱ���������������ʱ�����ţ�����������!!!!!!!!!!???????
class SortByRemindTime implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return 0;	
	}
}

//ͨ����ǩ����ʱ���������µĴ����ģ�����������
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

//ͨ����ǩ�޸�ʱ���������µ��޸ĵģ�����������
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

//ͨ��������������ѭ���������ţ�����������   ������ʱ������
class SortByRemindType implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return 0;	
	}
}


//ͨ���Ƿ��Ǽ������򣬼������ţ�����������
class SortByEncodeFirst implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return ( object1.iIsEncode - object2.iIsEncode );	
	}
}


//ͨ���Ƿ����������򣬴��������ţ����������� ������
class SortByVoiceFirst implements  Comparator<CMemoInfo> {

	public int  compare(CMemoInfo object1, CMemoInfo object2) {
		return 0;	
	}
}