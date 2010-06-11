#ifndef __SMSPASSDELETEWND_h__
#define __SMSPASSDELETEWND_h__

#include "UiEditControl.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"


class CSmsPassDeleteWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsPassDeleteWnd );

	public:

		CSmsPassDeleteWnd(void);

		virtual ~CSmsPassDeleteWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );


		BOOL	SubInitialize();

		void	SetID ( long id );

	private:
		
	protected:

	private:

		UiSingleLineEdit					m_PassInput;

		UiSingleLineEdit					m_PassInput_Again;

		UiPicture							m_Picture;

		int									m_modeIndex;

		CEasySmsUiCtrl						m_clCEasySmsUiCtrl;

};

#endif
