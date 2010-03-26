#include "stdafx.h"
#include "MyStoreLib.h"
#include "FuncLib.h"

long F_wcscatn(wchar_t* pwstrDest, wchar_t* pwstrSrc, long sizeMax)
{
	wchar_t			*pwcDest		=	pwstrDest;
	const wchar_t	*pwcSrc			=	pwstrSrc;
	wchar_t			*pwcDestEnd		=	pwstrDest + sizeMax;
	for( ; pwcDest+1 < pwcDestEnd; ++pwcDest ) {
		if( L'\0' == *pwcDest ) {
			break;
		}
	}
	long lCatCount = 0;
	BOOL bIsCatEnd = FALSE;
	for( ; pwcDest+1 < pwcDestEnd; ++pwcDest, ++pwcSrc ) {
		*pwcDest = *pwcSrc;
		lCatCount++;
		if( L'\0' == *pwcSrc ) {
			bIsCatEnd = TRUE;
			break;
		}
	}
	if( sizeMax > 0 )
	{
		*pwcDest	=	L'\0';
	}
	if ( bIsCatEnd )
	{
		--lCatCount;
	}
	else
	{
	}
	return lCatCount;
}
long F_wcscatn_pos(wchar_t* pwstrDest, wchar_t* pwstrSrc, long sizeMax, long lBeginCount)
{
	wchar_t			*pwcDest		=	pwstrDest + lBeginCount - 1;
	const wchar_t	*pwcSrc			=	pwstrSrc;
	wchar_t			*pwcDestEnd		=	pwstrDest + sizeMax;

	long lCatCount = 0;
	BOOL bIsCatEnd = FALSE;
	for( ; pwcDest+1 < pwcDestEnd; ++pwcDest, ++pwcSrc ) {
		*pwcDest = *pwcSrc;
		lCatCount++;
		if( L'\0' == *pwcSrc ) {
			break;
		}
	}
	if( sizeMax > 0 )
	{
		*pwcDest	=	L'\0';
	}
	if ( bIsCatEnd )
	{
		--lCatCount;
	}
	else
	{
	}
	return lCatCount;
}

wchar_t* F_wcscpyn( wchar_t *pwstrDest, const wchar_t *pwstrSrc, unsigned long sizeMax )
{
	if( sizeMax > 0 )
	{
		wcsncpy( pwstrDest, pwstrSrc, sizeMax-1 );
		pwstrDest[sizeMax-1]	=	L'\0';
	}
	return pwstrDest;
}

long S_MultiByte2WideChar( const char *pMultiByteStr, 
							    long lMultiByteCharSize, 
								wchar_t *pusWideCharStr, 
								long lWideCharSize )
{
	long	lRet	=	MultiByteToWideChar( 936, MB_PRECOMPOSED|MB_USEGLYPHCHARS, pMultiByteStr, 
											 lMultiByteCharSize, pusWideCharStr, lWideCharSize );
	return lRet;
}

BOOL F_LicenseProtect()
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


