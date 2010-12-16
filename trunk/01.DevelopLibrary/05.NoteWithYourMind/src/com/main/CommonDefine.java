package com.main;

class CommonDefine{
	public	enum	ToolbarStatusEnum
	{
		ToolbarStatus_Normal,
		ToolbarStatus_Delete,
		ToolbarStatus_Move
	}
	public static ToolbarStatusEnum g_enToolbarStatus = ToolbarStatusEnum.ToolbarStatus_Normal;
	public static int g_int_Invalid_ID = -1;
	public static int g_int_ListItemHeight = 65;
	public static String g_str_PassWord = "";
	public static boolean g_bool_IsPassWordChecked = false;
	
	public static NoteListCursorAdapter g_listAdapter = null;
	public static int g_preID = -1;
	public static boolean g_bIsRemind = false;
	public static int g_test = -1;
	public static	CNoteDBCtrl		m_clCNoteDBCtrl = null;
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
		ListType_NormalList,          //通过指定PreID，来提供PreID对应的子List
		ListType_SearchResultList,   //通过设定检索条件，来提供检索结果List
	}
	public ListTypeEnum g_enListType;
	
	public int g_int_PreID;
	
	public boolean g_bool_IsRemindSearch;
	public boolean g_bool_IsVoiceSearch;
	public boolean g_bool_IsTextSearch;	
	public String g_str_SearchKey;
}