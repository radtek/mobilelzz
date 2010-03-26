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
	GetModuleFileName(0, pszFileName, MAX_PATH);            // ��ǰ����EXE���ļ���
	MYSTORE_VERIFY_CONTEXT mystore = {0};
	DWORD dwRet = MyStoreVerify(pszFileName, &mystore);     // ��֤���ļ��Ƿ�Ϸ�

	bool	bRlt	=	false;

	switch(dwRet)
	{
	case 0:
		{
			//��֤���Ϊ: �Ϸ�
			//  �����Ƿ񳬹�������
			//  ������Ӧ�ó����������

			RETAILMSG(1, (L"��֤���Ϊ: �Ϸ�, LicenseValid:%u, Expired:%u, ��������ֹ����:%u\n", 
				mystore.LicenseValid, mystore.Expired, mystore.ExpireDate ));

			if(mystore.Expired)
			{
				// ����������
				// ...
			}
			else
			{
				// δ����������
				// ...
				bRlt	=	true;
			}
		}
		break;
	case 1:
		{
			//��֤���Ϊ: ��pszFileName�ļ�ʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ��pszFileName�ļ�ʧ��\n"));
		}
		break;
	case 2:
		{
			//��֤���Ϊ: ��License�ļ�ʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ��License�ļ�ʧ��\n"));
		}
		break;
	case 3:
		{
			//��֤���Ϊ: ��֤ʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ��֤ʧ��\n"));
		}
		break;
	case 4:
		{
			//��֤���Ϊ: ���кŻ�ȡʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ���кŻ�ȡʧ��\n"));
		}
		break;
	}

	return	bRlt;
}


