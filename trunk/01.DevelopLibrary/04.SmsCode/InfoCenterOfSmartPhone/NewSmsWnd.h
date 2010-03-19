
#ifndef __NewSmsWnd_h__
#define __NewSmsWnd_h__
#include <acc_api.h>
#include <ShellNotifyMsg.h>
#include"UiEditControl.h"
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


#define BUTTON_WIDTH	150
// 从 CMzWndEx 派生的主窗口类
class CNewSmsWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CNewSmsWnd);
public:
   
  // 按钮
  UiButton m_SendSmsBtn;
  UiButton m_ContactorsBtn;

  // 文本
  UiEditControl				m_Recievers;
  UiEdit					m_SmsMsgEdit;

  DWORD m_accMsg;
protected:
  // 窗口的初始化
  virtual BOOL OnInitDialog()
  {
    // 必须先调用基类的初始化
    if (!CMzWndEx::OnInitDialog())
    {
      return FALSE;
    }
  
    //打开重力感应设备
    MzAccOpen();

    //获得重力感应改变的消息
    m_accMsg = MzAccGetMessage();

    // 初始化收件人控件，并添加到窗口中
    m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH, 50);
	m_Recievers.SetText(L"收件人:");  // set the tips text
	m_Recievers.SetID(MZ_IDC_RECIEVERS_EDIT);
    //m_Recievers.SetTextColor(RGB(0,0,0)); // you could also set the color of text
	//m_Recievers.SetEnable(0);
	//m_Recievers.SetColorBg(RGB(0,0,0));
    AddUiWin(&m_Recievers); // don't forget to add the control to the window
	// 初始化短信文本控件，并添加到窗口中
	m_SmsMsgEdit.SetSipMode(IM_SIP_MODE_KEEP,0);
	m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()-m_Recievers.GetHeight()));
    m_SmsMsgEdit.SetTextColor(RGB(255,0,0)); // you could also set the color of text
    AddUiWin(&m_SmsMsgEdit); // don't forget to add the control to the window

    m_SendSmsBtn.SetButtonType(MZCV2_BUTTON_BLACK);
	m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH),0,BUTTON_WIDTH,50);
    m_SendSmsBtn.SetID(MZ_IDC_SEND_SMS_BTN);
    m_SendSmsBtn.SetText(L"发送");
    //m_ContactorsBtn.SetTextColor(RGB(255,255,255));
	AddUiWin(&m_SendSmsBtn);

    return TRUE;
  }
  
  // 重载命令消息的处理函数
  virtual void OnMzCommand(WPARAM wParam, LPARAM lParam)
  {
    UINT_PTR id = LOWORD(wParam);
   switch(id)
    {
		case MZ_IDC_SEND_SMS_BTN:
		{

		}
		case MZ_IDC_CONTACTORS_BTN:
		{

		}
		case MZ_IDC_RECIEVERS_EDIT:
		{
			MzMessageBoxEx(m_hWnd, L"联系人列表",NULL);
		}


      break;
    }
	
  }

  LRESULT MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam)
  {
    switch(message)
    {
    default:
      {
        if (message == m_accMsg)
        {
            // 转屏
          switch(wParam)
          {
          case SCREEN_PORTRAIT_P:
            {
                MzChangeDisplaySettingsEx(DMDO_90);
            }
            break;
          case SCREEN_PORTRAIT_N:
            {
                MzChangeDisplaySettingsEx(DMDO_270);
            }
            break;
          case SCREEN_LANDSCAPE_N:
            {
                MzChangeDisplaySettingsEx(DMDO_180);
            }
            break;
          case SCREEN_LANDSCAPE_P:
            {
                MzChangeDisplaySettingsEx(DMDO_0);
            }
            break;
          }
        }
        
      }
      break;
    }
      return CMzWndEx::MzDefWndProc(message,wParam,lParam);
  }

  // 转屏后如果需要调整窗口的位置，重载此函数响应 WM_SETTINGCHANGE 消息
  virtual void OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName)
  {
      //设置新的屏幕方向的窗口大小及控件位置
      DEVMODE  devMode;
      memset(&devMode, 0, sizeof(DEVMODE));
      devMode.dmSize = sizeof(DEVMODE);
      devMode.dmFields = DM_DISPLAYORIENTATION;
      ChangeDisplaySettingsEx(NULL, &devMode, NULL, CDS_TEST, NULL);

      //横屏
      if (devMode.dmDisplayOrientation == DMDO_90 || devMode.dmDisplayOrientation == DMDO_270)
      {
          RECT rc = MzGetWorkArea();
          SetWindowPos(m_hWnd, rc.left, rc.top,720, RECT_WIDTH(rc) );

      }

      //竖屏
      if (devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0)
      {
		 
      }
  }

};
#endif //__NewSmsWnd_h__