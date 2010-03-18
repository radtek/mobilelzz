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
  // �����еİ�ť�ؼ�
  UiButton m_btn;

protected:
  // ���ڵĳ�ʼ��
  virtual BOOL OnInitDialog()
  {
    // �����ȵ��û���ĳ�ʼ��
    if (!CMzWndEx::OnInitDialog())
    {
      return FALSE;
    }

	// ��ʼ�������еĿؼ�
    m_btn.SetButtonType(MZC_BUTTON_GREEN);
    m_btn.SetPos(100,250,280,100);
    m_btn.SetID(MZ_IDC_TESTBTN1);
    m_btn.SetText(L"Hello MZFC!");
    m_btn.SetTextColor(RGB(255,255,255));

	// �ѿؼ���ӵ�������
    AddUiWin(&m_btn);

    return TRUE;
  }

  // ����������Ϣ�Ĵ�����
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