#include"stdafx.h"

#include"UiEditControl.h"
#include"NewSmsWnd.h"

CNewSmsWnd::~CNewSmsWnd()
{
	delete[] m_pRecivers;
	m_pRecivers = NULL;
	m_lReciversCount = 0;
}

BOOL CNewSmsWnd::OnInitDialog()
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
	//m_Recievers.SetParentWnd(m_hWnd);
	// 初始化收件人控件，并添加到窗口中
	RECT rc = {0};
	int height = 0;
	int width = 0;
	HWND hWnd = FindWindow(L"CTaskBar", 0);
	if(hWnd != 0)
	{
		::GetWindowRect(hWnd, &rc);
		height = rc.bottom - rc.top;
		width = rc.right - rc.left;
	}

	if(width>480)
	{
		m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH_H, 50);
		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()-m_Recievers.GetHeight()));
		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_H),0,BUTTON_WIDTH_H,50);
	}
	else
	{
		long lWidth = GetWidth();
		long lHeight = GetHeight();
		RECT rc = MzGetWindowRect();
		RECT rc2 = MzGetClientRect();
		RECT rc3 = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		m_Recievers.SetPos(0, 0, lWidth-BUTTON_WIDTH_V, 50);
		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (lHeight-m_Recievers.GetHeight()));
		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_V),0,BUTTON_WIDTH_V,50);
	}
	
	m_Recievers.SetText(L"收件人:");  // set the tips text
	m_Recievers.SetID(MZ_IDC_RECIEVERS_EDIT);
	m_Recievers.SetParent((void*)this);
	//m_Recievers.SetTextColor(RGB(0,0,0)); // you could also set the color of text
	//m_Recievers.SetEnable(0);
	//m_Recievers.SetColorBg(RGB(0,0,0));
	AddUiWin(&m_Recievers); // don't forget to add the control to the window
	// 初始化短信文本控件，并添加到窗口中
	m_SmsMsgEdit.SetSipMode(IM_SIP_MODE_KEEP,0);
	
	m_SmsMsgEdit.SetTextColor(RGB(255,0,0)); // you could also set the color of text
	AddUiWin(&m_SmsMsgEdit); // don't forget to add the control to the window

	m_SendSmsBtn.SetButtonType(MZC_BUTTON_ORANGE);
	
	m_SendSmsBtn.SetID(MZ_IDC_SEND_SMS_BTN);
	m_SendSmsBtn.SetText(L"发送");
	//m_ContactorsBtn.SetTextColor(RGB(255,255,255));
	AddUiWin(&m_SendSmsBtn);

	return TRUE;
}

void CNewSmsWnd::OnMzCommand(WPARAM wParam, LPARAM lParam)
{
	UINT_PTR id = LOWORD(wParam);
	switch(id)
	{
		case MZ_IDC_SEND_SMS_BTN:
		{
			MzChangeDisplaySettingsEx(DMDO_90);
		}

	  break;
	}

}

LRESULT CNewSmsWnd::MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam)
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
				g_bH = TRUE;
			}
			break;
		  case SCREEN_PORTRAIT_N:
			{
				MzChangeDisplaySettingsEx(DMDO_270);
				g_bH = FALSE;
			}
			break;
		  case SCREEN_LANDSCAPE_N:
			{
				MzChangeDisplaySettingsEx(DMDO_180);
				g_bH = TRUE;
			}
			break;
		  case SCREEN_LANDSCAPE_P:
			{
				MzChangeDisplaySettingsEx(DMDO_0);
				g_bH = FALSE;
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
void CNewSmsWnd::OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName)
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
		g_bH = TRUE;
		RECT rc = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_HEIGHT(rc)+rc.top, RECT_WIDTH(rc)  );
		m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH_H, 50);

		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()-m_Recievers.GetHeight()));

		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_H),0,BUTTON_WIDTH_H,50);
  }

  //竖屏
	if (devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0)
	{
		g_bH = FALSE;
		RECT rc = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH_V, 50);

		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()-m_Recievers.GetHeight()));

		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_V),0,BUTTON_WIDTH_V,50);
	}
}

void CNewSmsWnd::UpdateData( MyListItemData* pRecivers,long lReciversCount )
{
	m_pRecivers = pRecivers;
	m_lReciversCount = lReciversCount;
}