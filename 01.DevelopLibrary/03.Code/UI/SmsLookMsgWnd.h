#ifndef __SMSLOOKMSGWND_h__
#define __SMSLOOKMSGWND_h__

#include "ContactorsWnd.h"
#include "NewSmsWnd.h"
#include "EasySmsWndBase.h"

class CSmsLookMsgWnd	:	public	CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CSmsLookMsgWnd );

	public:

		CSmsLookMsgWnd(void);

		virtual ~CSmsLookMsgWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

	private:

		BOOL	SubInitialize();
		
	protected:

	private:

		UiToolbar_Text				m_Toolbar;

};

#endif
