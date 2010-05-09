#ifndef __EASYSMSMAINWND_h__
#define __EASYSMSMAINWND_h__

#include "EasySmsWndBase.h"

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

		UiButton_Image		m_LookSmsBtnImg;			//进入查看短信窗口

		UiButton_Image		m_SendSmsBtnImg;

		UiButton_Image		m_UnReadSmsBtnImg;

		UiButton_Image		m_UnFindSmsBtnImg;

		UiButton_Image		m_UnEncryptSmsBtnImg;

		UiButton_Image		m_UnSyncSmsBtnImg;

		UiButton_Image		m_UnSetUpSmsBtnImg;

		UiPicture			m_Picture;

		int					m_modeIndex;

	private:

};

#endif
