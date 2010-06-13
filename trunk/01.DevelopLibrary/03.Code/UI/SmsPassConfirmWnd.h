#ifndef __SMSPASSCONFIRMWND_h__
#define __SMSPASSCONFIRMWND_h__

#include "UiEditControl.h"
#include "NewSmsWnd.h"
#include "EasySmsUiCtrl.h"
#include "EasySmsWndBase.h"


class CSmsPassConfirmWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsPassConfirmWnd );

	public:

		CSmsPassConfirmWnd(void);

		virtual ~CSmsPassConfirmWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );


		BOOL	SubInitialize();

		wchar_t*	GetPassWord();

	private:
		
	protected:

	private:

		UiSingleLineEdit					m_PassInput;

		UiPicture							m_Picture;
		CEasySmsUiCtrl						m_clCEasySmsUiCtrl;
		int									m_modeIndex;
		wchar_t								*m_pwPassWord;

};

#endif
