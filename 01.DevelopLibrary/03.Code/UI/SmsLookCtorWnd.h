#ifndef __SMSLOOKCTORWND_h__
#define __SMSLOOKCTORWND_h__

#include"ContactorsWnd.h"
#include"NewSmsWnd.h"

class CSmsLookCtorList	:	public	UiList
{
	public:

		CSmsLookCtorList();

		virtual ~CSmsLookCtorList();

	public:
		// ��ĳһ�����ɾ��֮ǰ��delete ���Զ���������
		virtual void OnRemoveItem( int nIndex );

		virtual int OnLButtonUp( UINT fwKeys, int xPos, int yPos );

		virtual void DrawItem(HDC hdcDst, int nIndex, RECT* prcItem, RECT *prcWin, RECT *prcUpdate);


	protected:

	private:
	
		ImageContainer m_ImageContainer;
};


class CSmsLookCtorWnd	:	public	CMzWndEx
{
	
	MZ_DECLARE_DYNAMIC( CSmsLookCtorWnd );

	public:

		CSmsLookCtorWnd(void);

		virtual ~CSmsLookCtorWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

	private:
		
	protected:

	private:

		CSmsLookCtorList			m_List;

		UiAlphabetBar				m_AlpBar;

		UiToolbar_Text				m_Toolbar;

};

#endif
