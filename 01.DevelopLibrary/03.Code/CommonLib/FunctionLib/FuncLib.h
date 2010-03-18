// TBBTable.h: interface for the CTBBTable class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(FuncLib__H)
#define FuncLib__H

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

long F_wcscatn(wchar_t* pwcsDest, wchar_t* pwcsSrc, long sCount);
long F_wcscatn_pos(wchar_t* pwcsDest, wchar_t* pwcsSrc, long sCount, long lBeginCount);

unsigned short* F_wcscpyn( unsigned short *pwstrDest, const unsigned short *pwstrSrc, unsigned long sizeMax );


long S_MultiByte2WideChar( const char *pMultiByteStr, long lMultiByteCharSize, unsigned short *pusWideCharStr, long lWideCharSize );

#endif // FuncLib__H
