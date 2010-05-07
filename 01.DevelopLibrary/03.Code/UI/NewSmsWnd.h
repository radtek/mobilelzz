
#ifndef __NewSmsWnd_h__
#define __NewSmsWnd_h__


#define PROGRESS_TIMER_ID				111


#define BUTTON_WIDTH_V					100
#define BUTTON_WIDTH_H					100

#define BUTTON_HEIGHT_VH				65
// 从 CMzWndEx 派生的主窗口类

#include "EasySmsWndBase.h"

class CMyEdit: public UiEdit
{
public:
	virtual ~CMyEdit()
	{
		ReleaseCapture();
	}
	int OnLButtonUp123  ( UINT  fwKeys,  int  xPos,  int  yPos )
	{

		RECT EditRc = GetClientRect();
		if( (yPos <= EditRc.bottom && yPos >= EditRc.top) &&( xPos <= EditRc.right && xPos >=EditRc.left) )	
		{
			this->SetSipMode(IM_SIP_MODE_KEEP,0);

		}
	
		return 0;
	}

};


class CContactorsWnd;
class CNewSmsWnd : public /*CMzWndEx*/ CEasySmsWndBase
{

	MZ_DECLARE_DYNAMIC( CNewSmsWnd );

	public:
	   CNewSmsWnd()
	   {
			m_lCurProgress = 0;
			m_SmsMsgEdit = new CMyEdit;
			m_pclContactorsWnd  = NULL;	
	   }
	   virtual ~CNewSmsWnd();

	   static DWORD WINAPI   ProxyRun(LPVOID lp);

	   void Run();
	 
	   void UpdateData( MyListItemData* pRecivers,long lReciversCount );

		// 按钮
	//  UiButton m_SendSmsBtn;
	//	UiButton					m_ContactorsBtn;
		MzPopupProgress				m_progress;
		long						m_lCurProgress;
		UINT						m_uShowNotifyWnd;
		// 文本
		UiEditControl				m_Recievers;
		CMyEdit*					m_SmsMsgEdit;

		DWORD						m_accMsg;
		DWORD						m_smsMsg;
		HANDLE						m_hReadMessageThread;

	protected:
		// 窗口的初始化
		virtual BOOL OnInitDialog();

		// 重载命令消息的处理函数
		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

		LRESULT MzDefWndProc( UINT message, WPARAM wParam, LPARAM lParam );

		// 转屏后如果需要调整窗口的位置，重载此函数响应 WM_SETTINGCHANGE 消息
		virtual void OnSettingChange( DWORD wFlag, LPCTSTR pszSectionName );

		bool SendSMS( IN LPCTSTR lpNumber, IN LPCTSTR lpszMessage );
		bool SendSMS_Wrapper( IN CMzString&  Number );

		void OnTimer( UINT_PTR nIDEvent );

		//virtual void  OnLButtonUp  ( UINT  fwKeys,  int  xPos,  int  yPos );
		static  DWORD WINAPI ReadMessage( LPVOID lpParameter );

		private:
		void ReadMessage();

		UiButton_Image		m_SendSmsBtn;
//		UiToolbar_Text		m_toolBar;
//		UiButton_Image		m_SendSmsBack;

		ImageContainer		m_imgContainer;
		BOOL Normal();
private:
	CContactorsWnd* m_pclContactorsWnd;
};
#endif //__NewSmsWnd_h__