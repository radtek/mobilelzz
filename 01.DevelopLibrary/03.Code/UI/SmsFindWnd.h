#ifndef __SMSFINDWND_h__
#define __SMSFINDWND_h__

#include "UiEditControl.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"


class CSmsFindWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsFindWnd );

	public:

		CSmsFindWnd(void);

		virtual ~CSmsFindWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );


		BOOL	SubInitialize();

	private:
		
	protected:

	private:

		UiSingleLineEdit					m_ContactorsEdit;

		UiButton							m_ContactorsBtn;


		UiSingleLineEdit					m_InfoEdit;

};

#endif
