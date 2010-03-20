#ifndef __UiEditControl_h__
#define __UiEditControl_h__

#include<Sqlite/CppSQLite3U.h>
#define UiEditControl_Click		1001
class UiEditControl: public UiSingleLineEdit
{
	long m_lFlag;
	//HWND m_hParentWnd;

	void* m_pParent;
public:
	UiEditControl()
	{
		m_lFlag = 0;
		m_pParent = NULL;
	}
	virtual ~UiEditControl()
	{
	}
	void SetParent(void* pParent);

	void UpdateData(  long lFlag  );


	virtual void  OnFocused (UiWin *pWinPrev);
	
};
#endif //__UiEditControl_h__
