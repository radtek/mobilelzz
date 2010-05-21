// stdafx.cpp : 只包括标准包含文件的源文件
// InfoCenterOfSmartPhone.pch 将作为预编译头
// stdafx.obj 将包含预编译类型信息

#include "stdafx.h"
BOOL g_bH = FALSE;
CReciversList g_ReciversList;

BOOL	g_bContactShow = FALSE;

//是否是试用版的flg
BOOL	g_bIsTrial		=	FALSE;
// TODO: 在 STDAFX.H 中
// 引用任何所需的附加头文件，而不是在此文件中引用

 

bool	LicenseProtect()
{
	wchar_t pszFileName[MAX_PATH] = {0};
	GetModuleFileName(0, pszFileName, MAX_PATH);            // 当前进程EXE的文件名
	MYSTORE_VERIFY_CONTEXT mystore = {0};
	DWORD dwRet = MyStoreVerify(pszFileName, &mystore);     // 验证此文件是否合法

	bool	bRlt	=	false;

	switch(dwRet)
	{
	case 0:
		{
			//验证结果为: 合法
			//  检验是否超过试用期
			//  决定让应用程序继续运行

			RETAILMSG(1, (L"验证结果为: 合法, LicenseValid:%u, Expired:%u, 试用期终止日期:%u\n", 
				mystore.LicenseValid, mystore.Expired, mystore.ExpireDate ));

			if(mystore.Expired)
			{
				// 超过试用期
				// ...
			}
			else
			{
				// 未超过试用期
				// ...
				bRlt	=	true;
			}
		}
		break;
	case 1:
		{
			//验证结果为: 打开pszFileName文件失败
			// ...退出应用程序
			RETAILMSG(1, (L"验证结果为: 打开pszFileName文件失败\n"));
		}
		break;
	case 2:
		{
			//验证结果为: 打开License文件失败
			// ...退出应用程序
			RETAILMSG(1, (L"验证结果为: 打开License文件失败\n"));
		}
		break;
	case 3:
		{
			//验证结果为: 验证失败
			// ...退出应用程序
			RETAILMSG(1, (L"验证结果为: 验证失败\n"));
		}
		break;
	case 4:
		{
			//验证结果为: 序列号获取失败
			// ...退出应用程序
			RETAILMSG(1, (L"验证结果为: 序列号获取失败\n"));
		}
		break;
	}

	return	bRlt;
}
