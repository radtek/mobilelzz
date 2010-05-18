#ifndef __SMSPASSINPUTWND_h__
#define __SMSPASSINPUTWND_h__

#include "UiEditControl.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"


class CSmsPassInputWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsPassInputWnd );

	public:

		CSmsPassInputWnd(void);

		virtual ~CSmsPassInputWnd(void);

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
		int									m_id;

};

#endif
