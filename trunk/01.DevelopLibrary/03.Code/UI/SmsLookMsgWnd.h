#ifndef __SMSLOOKMSGWND_h__
#define __SMSLOOKMSGWND_h__

#include"ContactorsWnd.h"
#include"NewSmsWnd.h"

class CSmsLookMsgList	:	public	UiList
{
	public:

		CSmsLookMsgList();

		virtual ~CSmsLookMsgList();

	public:
		// 当某一项即将被删除之前，delete 其自定义项数据
		virtual void OnRemoveItem( int nIndex );

		virtual int OnLButtonUp( UINT fwKeys, int xPos, int yPos );

		virtual void DrawItem(HDC hdcDst, int nIndex, RECT* prcItem, RECT *prcWin, RECT *prcUpdate);


	protected:

	private:
	
		ImageContainer m_ImageContainer;
};


class CSmsLookMsgWnd	:	public	CMzWndEx
{
	
	MZ_DECLARE_DYNAMIC( CSmsLookMsgWnd );

	public:

		CSmsLookMsgWnd(void);

		virtual ~CSmsLookMsgWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

	private:
		
	protected:

	private:

		CSmsLookMsgList			m_List;

		UiAlphabetBar				m_AlpBar;

		UiToolbar_Text				m_Toolbar;

};

#endif
