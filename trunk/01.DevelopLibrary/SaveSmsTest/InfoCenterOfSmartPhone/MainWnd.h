#ifndef __MainWnd_h__
#define __MainWnd_h__

#include"NewSmsWnd.h"
// 按钮控件的ID
#define MZ_IDC_NewSmsBtn  101

// 从 CMzWndEx 派生的主窗口类
class CMainWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CMainWnd);
public:
  // 窗口中的按钮控件
  UiButton m_btn;
	CNewSmsWnd m_NewSmsWnd;
protected:
  // 窗口的初始化
  virtual BOOL OnInitDialog()
  {
    // 必须先调用基类的初始化
    if (!CMzWndEx::OnInitDialog())
    {
      return FALSE;
    }

	// 初始化窗口中的控件
    m_btn.SetButtonType(MZC_BUTTON_GREEN);
    m_btn.SetPos(100,250,280,100);
    m_btn.SetID(MZ_IDC_NewSmsBtn);
    m_btn.SetText(L"发送短信");
    m_btn.SetTextColor(RGB(255,255,255));

	// 把控件添加到窗口中
    AddUiWin(&m_btn);

    return TRUE;
  }

  // 重载命令消息的处理函数
  virtual void OnMzCommand(WPARAM wParam, LPARAM lParam)
  {
    UINT_PTR id = LOWORD(wParam);
    switch(id)
    {
    case MZ_IDC_NewSmsBtn:
      {
        //if(1 == MzMessageBoxEx(m_hWnd, L"You have pressed Exit button, Really want exit?", L"Exit", MB_YESNO, false))
        //  PostQuitMessage(0);
		RECT rcWork = MzGetWorkArea();
		m_NewSmsWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
		m_NewSmsWnd.Show();
      }
      break;
    }
  }
};
#endif //__MainWnd_h__