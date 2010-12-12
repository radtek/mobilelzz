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
	public static int m_iBackCount = 0;
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