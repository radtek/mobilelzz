// TBBTable.h: interface for the CTBBTable class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(TrafficFuncLib__H)
#define TrafficFuncLib__H

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "ExternalDefine.h"

long Traffic_wcscatn(wchar_t* pwcsDest, wchar_t* pwcsSrc, long sCount);
long Traffic_wcscatn_pos(wchar_t* pwcsDest, wchar_t* pwcsSrc, long sCount, long lBeginCount);

unsigned short* Traffic_wcscpyn( unsigned short *pwstrDest, const unsigned short *pwstrSrc, unsigned long sizeMax );


long Traffic_MultiByte2WideChar( const char *pMultiByteStr, long lMultiByteCharSize, unsigned short *pusWideCharStr, long lWideCharSize );

#endif // TrafficFuncLib__H
