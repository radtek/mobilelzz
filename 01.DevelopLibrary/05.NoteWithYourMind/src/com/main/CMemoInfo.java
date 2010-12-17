package com.main;

public class CMemoInfo {
	public final static Integer IsEditEnable_Invalid = -1;
	public final static Integer IsEditEnable_Disable = 0;
	public final static Integer IsEditEnable_Enable = 1;
	
	public final static Integer Type_Invalid = -1;
	public final static Integer Type_Folder = 0;
	public final static Integer Type_Memo = 1;
	//public final static Integer Type_PassWord = 2;

	
	public final static Integer IsRemind_Invalid = -1;
	public final static Integer IsRemind_Yes = 0;
	public final static Integer IsRemind_No = 1;
	
	public final static Integer PreId_Root = 0;
	public final static Integer Id_Invalid = -1;

	public final static Integer IsEncode_Invalid = -1;
	public final static Integer IsEncode_Yes = 0;
	public final static Integer IsEncode_No = 1;
	
	public final static Integer IsHaveAudioData_Invalid = -1;
	public final static Integer IsHaveAudioData_No = 0;
	public final static Integer IsHaveAudioData_Yes = 1;
	
	Integer		iId;
	Integer		iPreId;
	Integer		iType;
	Integer		iIsRemind;
	long		dRemindTime;
	long		dCreateTime;
	long		dLastModifyTime;
	Integer		iIsEditEnable;
	Integer		iIsRemindAble;
	Integer		RemindType;
	String		strDetail;
	String		strPassword;
	Integer		iIsEncode;	
	Byte		m_Week[]	=	new Byte[7];
	
	Integer		iIsHaveAudioData;
	String		strAudioFileName;
	CMemoInfo()
	{
		iId				=	-1;
		iPreId			=	-1;
		iType			=	-1;
		iIsRemind		=	-1;
		dRemindTime		=	-1;
		dCreateTime		=	-1;
		dLastModifyTime	=	-1;
		iIsEditEnable	=	IsEditEnable_Enable;
		iIsRemindAble	=	-1;
		RemindType		=	-1;
		strDetail		=	null;
		strPassword		=	null;
		iIsEncode		=	-1;
		int	length	=	m_Week.length;
		for ( int i = 0; i < length; ++i )
		{
			m_Week[ i ]	=	-1;
		}
		iIsHaveAudioData = IsHaveAudioData_No;
		strAudioFileName = null;
	}
	
}
