#ifndef __EASYSMSMAINWND_h__
#define __EASYSMSMAINWND_h__

#include"ContactorsWnd.h"
#include"NewSmsWnd.h"


class CEasySmsMainWnd : public CMzWndEx
{
	
	MZ_DECLARE_DYNAMIC( CEasySmsMainWnd );

	public:

		CEasySmsMainWnd(void);

		virtual ~CEasySmsMainWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

	private:
		
		BOOL	Initialize_Btn_Img();

	protected:

		UiButton_Image		m_LookSmsBtnImg;	//进入查看短信窗口

		UiButton_Image		m_SendSmsBtnImg;

		ImageContainer		m_imgContainer;

	private:

};

#endif
