#ifndef __UiEditControl_h__
#define __UiEditControl_h__

#include<Sqlite/CppSQLite3U.h>
#define UiEditControl_Click		1001
class UiEditControl: public UiSingleLineEdit
{
	long m_lFlag;
	//HWND m_hParentWnd;

	MyListItemData* m_pRecivers;
	long			m_lReciversCount;

	void* m_pParent;
public:
	UiEditControl()
	{
		m_lFlag = 0;
		m_pParent = NULL;
		m_pRecivers = NULL;
		m_lReciversCount = 0;
	}
	virtual ~UiEditControl()
	{
	}
	void SetParent(void* pParent);

	void UpdateData( MyListItemData* pRecivers,long lReciversCount );


	virtual void  OnFocused (UiWin *pWinPrev);
	
};
#endif //__UiEditControl_h__
