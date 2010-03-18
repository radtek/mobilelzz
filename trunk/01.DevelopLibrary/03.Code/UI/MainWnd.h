#ifndef __MainWnd_h__
#define __MainWnd_h__

#define MZ_IDC_TESTBTN1  101

class CMainWnd : public CMzWndEx
{
public:
	CMainWnd();
	~CMainWnd();
	MZ_DECLARE_DYNAMIC(CMainWnd);
public:
  // 窗口中的按钮控件
  UiButton m_btn;

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
    m_btn.SetID(MZ_IDC_TESTBTN1);
    m_btn.SetText(L"Hello MZFC!");
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
    case MZ_IDC_TESTBTN1:
      {
        if(1 == MzMessageBoxEx(m_hWnd, L"You have pressed Exit button, Really want exit?", L"Exit", MB_YESNO, false))
          PostQuitMessage(0);
      }
      break;
    }
  }
protected:
	
private:
		
}

#endif __MainWnd_h__