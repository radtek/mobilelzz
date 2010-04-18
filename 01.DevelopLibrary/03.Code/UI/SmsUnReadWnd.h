#ifndef __SMSUNREADWND_h__
#define __SMSUNREADWND_h__

#include "ContactorsWnd.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"
#include "EasySmsUiCtrl.h"

class CSmsUnReadWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsUnReadWnd );

	public:

		CSmsUnReadWnd(void);

		virtual ~CSmsUnReadWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );
		
	protected:

		virtual void	DoSthForItemBtnUpSelect( ListItemEx* pItem );

		virtual	void	DoSthForItemRemove( ListItemEx* pItem );

	private:

		BOOL	SubInitialize();

	private:

	CEasySmsUiCtrl		m_clCEasySmsUiCtrl;
};

#endif
