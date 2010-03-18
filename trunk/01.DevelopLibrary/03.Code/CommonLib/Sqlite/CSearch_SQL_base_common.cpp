

#include	"CSearch_SQL_base_common.h"


int		MB2WC(wchar_t* _pwc,  const char* _pch)
{
	int			buf_ln	=	MultiByteToWideChar(CP_ACP, 0, _pch, -1, _pwc, 0);
							MultiByteToWideChar(CP_ACP, 0, _pch, -1, _pwc, buf_ln);
	
	return	buf_ln;
}

int		WC2MB(char* _pch,  wchar_t* _pwc)
{
	int			buf_ln	=	WideCharToMultiByte(CP_ACP, 0, _pwc, -1, _pch, 0,		0, 0);
							WideCharToMultiByte(CP_ACP, 0, _pwc, -1, _pch, buf_ln,	0, 0);
	
	return	buf_ln;
}


wchar_t*	wcscpy_as(wchar_t* _pstrDest,  size_t _numberOfElements,  const wchar_t* _pstrSource)
{
	return	wcsncpy(_pstrDest, _pstrSource, min(_numberOfElements, wcslen(_pstrSource) + 1));
}

wchar_t*	wcscat_as(wchar_t* _pstrDest,  size_t _numberOfElements,  const wchar_t* _pstrSource)
{
	return	wcsncat(_pstrDest, _pstrSource, min(_numberOfElements - wcslen(_pstrDest), wcslen(_pstrSource) + 1));
}

wchar_t*	wcstok_as(wchar_t* _pstrToken,  const wchar_t* _pstrDelimit,  wchar_t** _ppcontext)
{
	return	wcstok(_pstrToken, _pstrDelimit);
}


