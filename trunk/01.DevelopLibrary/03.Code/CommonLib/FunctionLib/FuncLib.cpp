#include "TrafficFuncLib.h"

long Traffic_wcscatn(wchar_t* pwstrDest, wchar_t* pwstrSrc, long sizeMax)
{
	unsigned short			*pwcDest		=	pwstrDest;
	const unsigned short	*pwcSrc			=	pwstrSrc;
	unsigned short			*pwcDestEnd		=	pwstrDest + sizeMax;
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
long Traffic_wcscatn_pos(wchar_t* pwstrDest, wchar_t* pwstrSrc, long sizeMax, long lBeginCount)
{
	unsigned short			*pwcDest		=	pwstrDest + lBeginCount - 1;
	const unsigned short	*pwcSrc			=	pwstrSrc;
	unsigned short			*pwcDestEnd		=	pwstrDest + sizeMax;

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

unsigned short* Traffic_wcscpyn( unsigned short *pwstrDest, const unsigned short *pwstrSrc, unsigned long sizeMax )
{
	if( sizeMax > 0 )
	{
		wcsncpy( pwstrDest, pwstrSrc, sizeMax-1 );
		pwstrDest[sizeMax-1]	=	L'\0';
	}
	return pwstrDest;
}

long Traffic_MultiByte2WideChar( const char *pMultiByteStr, 
							    long lMultiByteCharSize, 
								unsigned short *pusWideCharStr, 
								long lWideCharSize )
{
	// modify by wang-lu at 2009/08/17
	long	lRet	=	MultiByteToWideChar( 936, MB_PRECOMPOSED|MB_USEGLYPHCHARS, pMultiByteStr, 
											 lMultiByteCharSize, pusWideCharStr, lWideCharSize );
	return lRet;
}

