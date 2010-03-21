
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


#define BUTTON_WIDTH_V	150
#define BUTTON_WIDTH_H	150

#define BUTTON_HEIGHT_VH 65
// 从 CMzWndEx 派生的主窗口类


class CMyEdit: public UiEdit
{
public:
	virtual ~CMyEdit()
	{
		ReleaseCapture();
	}

};

class CNewSmsWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CNewSmsWnd);
public:
   CNewSmsWnd()
   {
		m_lCurProgress = 0;
		m_SmsMsgEdit = new CMyEdit;


   }
   virtual ~CNewSmsWnd(){
	
		MzAccClose();  
   
   }
 
   void UpdateData( MyListItemData* pRecivers,long lReciversCount );
  // 按钮
  UiButton m_SendSmsBtn;
  UiButton m_ContactorsBtn;
  MzPopupProgress m_progress;
  long m_lCurProgress;

  // 文本
  UiEditControl				m_Recievers;
  CMyEdit*					m_SmsMsgEdit;

  DWORD m_accMsg;
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

};
#endif //__NewSmsWnd_h__