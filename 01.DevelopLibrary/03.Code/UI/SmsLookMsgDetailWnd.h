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
		CSmsLookMsgDetailWnd( LPCTSTR text );

		virtual ~CSmsLookMsgDetailWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

		void	SetText( LPCTSTR text );

		void	SetListInfo( ListItemEx* pListInfo );

	private:

		BOOL	SubInitialize();
		
	protected:

	private:

		UiToolbar_Text				m_Toolbar;

		UiEdit						m_UiEdit;

		MzGridMenu					m_GridMenu;

		CMzString					m_WndText;

		ListItemEx*					m_pListInfo;


};

#endif
