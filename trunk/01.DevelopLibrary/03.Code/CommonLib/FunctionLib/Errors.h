#ifndef __Errors_h__
#define __Errors_h__

enum COMMONLIB_API APP_Result{
	APP_Result_S_OK					= 0x0,
	APP_Result_E_Fail				= 0x80000000,//最高位为1，表示失败
	APP_Result_InvalidInstance		= 0x80000001,//相当于无效Handle
	APP_Result_NullPointer			= 0x80000002,
	
	APP_Result_Param_Invalid		= 0x80000010,
	APP_Result_Param_NullPointer	= 0x80000010,
	APP_Result_Param_OutOfRange		= 0x80000010,
	
	APP_Result_Memory_Error			= 0x80000020,
	APP_Result_Memory_Full			= 0x80000021,
	APP_Result_Memory_ReadOut		= 0x80000022,
	APP_Result_Memory_WriteOut		= 0x80000023,
	APP_Result_Memory_ReadInvalid	= 0x80000024,
	APP_Result_Memory_WriteInvalid	= 0x80000025,

	APP_Result_File_Error			= 0x80000030,
	APP_Result_File_NotExist		= 0x80000031,
	APP_Result_File_EOF				= 0x80000032,
	APP_Result_File_InvalidOffset	= 0x80000033,
	
	APP_Result_Xml_Error			= 0x80000040,
	APP_Result_Xml_NodeNotExist		= 0x80000041,

};

#define FAILED_App(appR) ((long)(appR) < 0)
#define SUCCEEDED_App(appR) ((long)(appR) >= 0)

#endif // __Errors_h__