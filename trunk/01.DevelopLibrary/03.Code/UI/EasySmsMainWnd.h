#ifndef __EASYSMSMAINWND_h__
#define __EASYSMSMAINWND_h__

#include"ContactorsWnd.h"
#include "EasySmsWndBase.h"
#include"NewSmsWnd.h"


class CEasySmsMainWnd : public CEasySmsWndBase
{
	
	MZ_DECLARE_DYNAMIC( CEasySmsMainWnd );

	public:

		CEasySmsMainWnd(void);

		virtual ~CEasySmsMainWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

	private:
		
		BOOL	SubInitialize();

	protected:

		UiButton_Image		m_LookSmsBtnImg;	//����鿴���Ŵ���

		UiButton_Image		m_SendSmsBtnImg;

	private:

};

#endif
