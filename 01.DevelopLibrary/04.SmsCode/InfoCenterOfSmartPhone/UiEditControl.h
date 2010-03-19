#ifndef __UiEditControl_h__
#define __UiEditControl_h__

#include"ContactorsWnd.h"
#include<Sqlite/CppSQLite3U.h>

class UiEditControl: public UiSingleLineEdit
{
	CContactorsWnd m_ContactorsWnd;
public:
	UiEditControl()
	{
	}
	virtual ~UiEditControl()
	{
	}
	virtual void OnClick(size_t  nIndex )
	{
		int i = 0;
		//MzMessageBoxEx(m_hWnd, L"联系人列表",NULL);
	}
	virtual void  OnFocused (UiWin *pWinPrev) 
	{
		int i = 0;
		RECT rcWork = MzGetWorkArea();
		m_ContactorsWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
		m_ContactorsWnd.Show();
	}
};
#endif //__UiEditControl_h__
