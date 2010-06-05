#ifndef __EASYSMSWNDBASE_h__
#define __EASYSMSWNDBASE_h__

//#define		UI_TEST

#include "../CommonLib/FunctionLib/CommonTypes.h"

class CEasySmsWndBase;

typedef struct ItemData_t
{
	long		lPid;
	long		lSid;
	long		lCnt;
	bool		bIsLock;
	bool		bIsRead;
	wchar_t		cFirst;
	CComBSTR	bstrTelNo;
	CComBSTR	bstrName;
	CComBSTR	bstrTime;
	CComBSTR	bstrDetail;
//	char	cIcon;
}stItemData;

/////////////////CEasySmsListBase/////////////////////////////////////////////////////////
class CEasySmsListBase	:	public	UiListEx
{
public:
	
	friend	CEasySmsWndBase;
	
	CEasySmsListBase();

	virtual ~CEasySmsListBase();

public:
	// 当某一项即将被删除之前，delete 其自定义项数据
//	virtual void OnRemoveItem( int nIndex );

	virtual int OnLButtonUp( UINT fwKeys, int xPos, int yPos );

protected:
	void	setEasySmsWndBase( CEasySmsWndBase *pCEasySmsWndBase );

private:

	ImageContainer		m_imgContainer_base;

	CEasySmsWndBase		*m_pCEasySmsWndBase;
};


///////////////////////CEasySmsWndBase///////////////////////////////////////////////////
class CEasySmsWndBase : public CMzWndEx
{
	
	MZ_DECLARE_DYNAMIC( CEasySmsWndBase );

	public:

		friend	CEasySmsListBase;

		CEasySmsWndBase(void);

		virtual ~CEasySmsWndBase(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual LRESULT MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam);

	protected:

		virtual	int		DoModalBase( CMzWndEx *pCMzWndEx );

		virtual	void	DoSthForItemRemove( ListItemEx* pItem );

		virtual void	DoSthForItemBtnUpSelect( ListItemEx* pItem );

		virtual void	DoSthForItemSelect( ListItemEx* pItem );

		virtual void	DoSthForTooBarHoldPress( int	nIndex );

		virtual void	ReturnToMainWnd();

	private:
		

	protected:

		CEasySmsListBase	m_list_base;

		UiToolbar_Text		m_toolBar_base;

		ImageContainer		m_imgContainer_base;

	private:

};

#endif
