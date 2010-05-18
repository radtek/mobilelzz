#ifndef __SMSENCRYTPCTORWND_h__
#define __SMSENCRYTPCTORWND_h__

#include "UiEditControl.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"
#include "EasySmsUiCtrl.h"


class CSmsEncrytpCtorWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsEncrytpCtorWnd );

	public:

		CSmsEncrytpCtorWnd(void);

		virtual ~CSmsEncrytpCtorWnd(void);

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
