#ifndef __UiEditControl_h__
#define __UiEditControl_h__

#define UiEditControl_Click		1001
class CContactorsWnd;

class UiEditControl: public UiSingleLineEdit
{
	long m_lFlag;

	void* m_pParent;
	CContactorsWnd* m_pclContactorsWnd;
public:
	UiEditControl();

	virtual ~UiEditControl();

	void SetParent(void* pParent);

	void UpdateData(  long lFlag  );

	void UpdateTextByRecievers();

	virtual void  OnFocused (UiWin *pWinPrev);

	
	//zds 2010/03/21 19:39
	int OnLButtonUp123  ( UINT  fwKeys,  int  xPos,  int  yPos );
	//zds 2010/03/21 19:39
	virtual int OnLButtonUp  ( UINT  fwKeys,  
		int  xPos,  
		int  yPos   
		);

	virtual int  OnKeyDown (int nVirtKey, DWORD lKeyData);

};
#endif //__UiEditControl_h__
