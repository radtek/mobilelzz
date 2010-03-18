#include "stdafx.h"
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

