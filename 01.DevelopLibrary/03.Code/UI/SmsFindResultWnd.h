#ifndef __SMSFINDRESULTWND_h__
#define __SMSFINDRESULTWND_h__

#include "ContactorsWnd.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"

class CSmsFindResultWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsFindResultWnd );

	public:

		CSmsFindResultWnd(void);

		virtual ~CSmsFindResultWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );
		
	protected:

		virtual void	DoSthForItemBtnUpSelect( ListItemEx* pItem );

	private:

		BOOL	SubInitialize();

	private:

		MzGridMenu m_GridMenu;

};

#endif
