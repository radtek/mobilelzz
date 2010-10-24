package com.main;

public class CMemoInfo {
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
	}
	
}
