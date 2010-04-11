// TBBTable.h: interface for the CTBBTable class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(FuncLib__H)
#define FuncLib__H

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

long COMMONLIB_API F_wcscatn(wchar_t* pwcsDest, wchar_t* pwcsSrc, long sCount);
long COMMONLIB_API F_wcscatn_pos(wchar_t* pwcsDest, wchar_t* pwcsSrc, long sCount, long lBeginCount);

COMMONLIB_API wchar_t*  F_wcscpyn( wchar_t *pwstrDest, const wchar_t *pwstrSrc, unsigned long sizeMax );


long COMMONLIB_API S_MultiByte2WideChar( const char *pMultiByteStr, long lMultiByteCharSize, unsigned short *pusWideCharStr, long lWideCharSize );

BOOL	COMMONLIB_API F_LicenseProtect();

#endif // FuncLib__H
