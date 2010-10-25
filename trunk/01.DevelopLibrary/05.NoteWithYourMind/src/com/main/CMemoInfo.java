package com.main;

public class CMemoInfo {
	public final static Integer IsEditEnable_Invalid = -1;
	public final static Integer IsEditEnable_Disable = 0;
	public final static Integer IsEditEnable_Enable = 1;
	
	public final static Integer Type_Invalid = -1;
	public final static Integer Type_Folder = 0;
	public final static Integer Type_Memo = 1;
	
	public final static Integer IsRemind_Invalid = -1;
	public final static Integer IsRemind_Yes = 0;
	public final static Integer IsRemind_No = 1;
	
	public final static Integer PreId_Root = 0;
	
	public final static String Encode_Folder = "º”√‹";
	
	Integer		iId;
	Integer		iPreId;
	Integer		iType;
	Integer		iIsRemind;
	long		dRemindTime;
	long		dCreateTime;
	long		dLastModifyTime;
	Integer		iIsEditEnable;
	Integer		iRemindMask;
	String		strDetail;
	String		strPassword;
	
	CMemoInfo()
	{
		iId				=	-1;
		iPreId			=	-1;
		iType			=	-1;
		iIsRemind		=	-1;
		dRemindTime		=	-1;
		dCreateTime		=	-1;
		dLastModifyTime	=	-1;
		iIsEditEnable	=	-1;
		iRemindMask		=	-1;
		strDetail		=	null;
		strPassword		=	null;
	}
	
}
