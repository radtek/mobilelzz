#ifndef __SMSLOOKMSGWND_h__
#define __SMSLOOKMSGWND_h__

#include "ContactorsWnd.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"
#include "EasySmsUiCtrl.h"

class CSmsLookMsgWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsLookMsgWnd );

	public:

		CSmsLookMsgWnd(void);

		virtual ~CSmsLookMsgWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

		void	SetListInfo( ListItemEx* pListInfo );

	private:

		BOOL	SubInitialize();
		
	protected:

		virtual void	DoSthForItemBtnUpSelect( ListItemEx* pItem );

	private:

		ListItemEx*			m_pListInfo;

		CEasySmsUiCtrl		m_clCEasySmsUiCtrl;

};

#endif
