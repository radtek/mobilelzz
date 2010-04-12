#ifndef __SMSLOOKMSGDETAILWND_h__
#define __SMSLOOKMSGDETAILWND_h__

#include "ContactorsWnd.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"

class CSmsLookMsgDetailWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsLookMsgDetailWnd );

	public:

		CSmsLookMsgDetailWnd(void);

		virtual ~CSmsLookMsgDetailWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

		void	SetText( LPCTSTR text );

	private:

		BOOL	SubInitialize();
		
	protected:

	private:

		UiToolbar_Text				m_Toolbar;

		UiEdit						m_UiEdit;

		MzGridMenu					m_GridMenu;


};

#endif
