
#ifndef __NewSmsWnd_h__
#define __NewSmsWnd_h__
//此代码演示了：
//  创建和初始化应用程序
//  创建和初始化窗体
//  按钮控件的使用及其命令消息的处理
//  单行文本编辑器
//  文字按钮工具条
//  响应屏幕旋转的消息以及处理旋转后ToolBarText的位置

#define MZ_IDC_SEND_SMS_BTN  101

#define IDC_PPM_OK    102
#define IDC_PPM_CANCEL  103

#define MZ_IDC_RECIEVERS_EDIT  104
#define MZ_IDC_SMS_MSG_EDIT  105

#define MZ_IDC_CONTACTORS_BTN  106

#define PROGRESS_TIMER_ID		111


#define BUTTON_WIDTH_V	100
#define BUTTON_WIDTH_H	100

#define BUTTON_HEIGHT_VH 65
// 从 CMzWndEx 派生的主窗口类


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
			SipSetCurrentIM(&g_clBackupSipID);
			this->SetSipMode(IM_SIP_MODE_GEL_PY,0);

		}
	
		return 0;
	}

};

class CNewSmsWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CNewSmsWnd);
public:
   CNewSmsWnd();
   virtual ~CNewSmsWnd();
   static DWORD WINAPI   ProxyRun(LPVOID lp);
   void Run();
  
protected:
  // 窗口的初始化
  virtual BOOL OnInitDialog();

  
  // 重载命令消息的处理函数
  virtual void OnMzCommand(WPARAM wParam, LPARAM lParam);

  LRESULT MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam);

  // 转屏后如果需要调整窗口的位置，重载此函数响应 WM_SETTINGCHANGE 消息
  virtual void OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName);

  bool SendSMS(IN LPCTSTR lpNumber,IN LPCTSTR lpszMessage);
  bool SendSMS_Wrapper(IN CMzString&  Number);

  void OnTimer(UINT_PTR nIDEvent);

  virtual void OnLButtonUp  ( UINT  fwKeys,  int  xPos,  int  yPos );
	
  static DWORD WINAPI ReadMessage(LPVOID lpParameter);

  HANDLE GetProcessHandle(int nID);

  HANDLE GetProcessHandle(LPCTSTR pName);

private:
	void ReadMessage();
	BOOL Normal();
private:
	CContactorsWnd*				m_pclContactorsWnd;			

	MzPopupProgress				m_progress;
	long						m_lCurProgress;
	UINT						m_uShowNotifyWnd;
	// 文本
	UiEditControl			m_Recievers;
	CMyEdit*					m_SmsMsgEdit;

	DWORD						m_accMsg;
	DWORD						m_smsMsg;
	HANDLE						m_hReadMessageThread;

	UiButton_Image				m_SendSmsBtn;

	UiButton_Image				m_ContactorsBtn;

	ImageContainer				m_imgContainer;

};
#endif //__NewSmsWnd_h__