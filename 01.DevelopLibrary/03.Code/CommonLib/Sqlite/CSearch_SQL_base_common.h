#include "Stdafx.h"

#define ROUND_UP( Size, Amount ) (((DWORD)(Size) +  ((Amount) - 1)) & ~((Amount) - 1))

const	unsigned long 		COLUMN_ALIGNVAL	= 8;




int			MB2WC(wchar_t* _wc,  const char* _ch);
int			WC2MB(char* _ch,  const wchar_t* _wc);

wchar_t*	wcscpy_as(wchar_t* _pstrDest,	size_t _numberOfElements,		const wchar_t* _pstrSource);
wchar_t*	wcscat_as(wchar_t* _pstrDest,	size_t _numberOfElements,		const wchar_t* _pstrSource);
wchar_t*	wcstok_as(wchar_t* _pstrToken,	const wchar_t* _pstrDelimit,	wchar_t** _ppcontext);


