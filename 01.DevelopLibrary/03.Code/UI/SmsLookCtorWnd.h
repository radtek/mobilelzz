#ifndef __SMSLOOKCTORWND_h__
#define __SMSLOOKCTORWND_h__

#include "UiEditControl.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"
#include "EasySmsUiCtrl.h"


class CSmsLookCtorWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsLookCtorWnd );

	public:

		CSmsLookCtorWnd(void);

		virtual ~CSmsLookCtorWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );


		BOOL	SubInitialize();

	private:
		
	protected:

		virtual void	DoSthForItemBtnUpSelect( ListItemEx* pItem );

	private:

		UiAlphabetBar				m_AlpBar;

		CEasySmsUiCtrl				m_clCEasySmsUiCtrl;

};

#endif
