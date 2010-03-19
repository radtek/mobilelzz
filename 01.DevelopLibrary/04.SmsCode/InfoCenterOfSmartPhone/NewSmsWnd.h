
#ifndef __NewSmsWnd_h__
#define __NewSmsWnd_h__
#include <acc_api.h>
#include <ShellNotifyMsg.h>
#include"UiEditControl.h"
//�˴�����ʾ�ˣ�
//  �����ͳ�ʼ��Ӧ�ó���
//  �����ͳ�ʼ������
//  ��ť�ؼ���ʹ�ü���������Ϣ�Ĵ���
//  �����ı��༭��
//  ���ְ�ť������
//  ��Ӧ��Ļ��ת����Ϣ�Լ�������ת��ToolBarText��λ��

#define MZ_IDC_SEND_SMS_BTN  101

#define IDC_PPM_OK    102
#define IDC_PPM_CANCEL  103

#define MZ_IDC_RECIEVERS_EDIT  104
#define MZ_IDC_SMS_MSG_EDIT  105

#define MZ_IDC_CONTACTORS_BTN  106


#define BUTTON_WIDTH	150
// �� CMzWndEx ��������������
class CNewSmsWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CNewSmsWnd);
public:
   
  // ��ť
  UiButton m_SendSmsBtn;
  UiButton m_ContactorsBtn;

  // �ı�
  UiEditControl				m_Recievers;
  UiEdit					m_SmsMsgEdit;

  DWORD m_accMsg;
protected:
  // ���ڵĳ�ʼ��
  virtual BOOL OnInitDialog()
  {
    // �����ȵ��û���ĳ�ʼ��
    if (!CMzWndEx::OnInitDialog())
    {
      return FALSE;
    }
  
    //��������Ӧ�豸
    MzAccOpen();

    //���������Ӧ�ı����Ϣ
    m_accMsg = MzAccGetMessage();

    // ��ʼ���ռ��˿ؼ�������ӵ�������
    m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH, 50);
	m_Recievers.SetText(L"�ռ���:");  // set the tips text
	m_Recievers.SetID(MZ_IDC_RECIEVERS_EDIT);
    //m_Recievers.SetTextColor(RGB(0,0,0)); // you could also set the color of text
	//m_Recievers.SetEnable(0);
	//m_Recievers.SetColorBg(RGB(0,0,0));
    AddUiWin(&m_Recievers); // don't forget to add the control to the window
	// ��ʼ�������ı��ؼ�������ӵ�������
	m_SmsMsgEdit.SetSipMode(IM_SIP_MODE_KEEP,0);
	m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()-m_Recievers.GetHeight()));
    m_SmsMsgEdit.SetTextColor(RGB(255,0,0)); // you could also set the color of text
    AddUiWin(&m_SmsMsgEdit); // don't forget to add the control to the window

    m_SendSmsBtn.SetButtonType(MZCV2_BUTTON_BLACK);
	m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH),0,BUTTON_WIDTH,50);
    m_SendSmsBtn.SetID(MZ_IDC_SEND_SMS_BTN);
    m_SendSmsBtn.SetText(L"����");
    //m_ContactorsBtn.SetTextColor(RGB(255,255,255));
	AddUiWin(&m_SendSmsBtn);

    return TRUE;
  }
  
  // ����������Ϣ�Ĵ�����
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
			MzMessageBoxEx(m_hWnd, L"��ϵ���б�",NULL);
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
            // ת��
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

  // ת���������Ҫ�������ڵ�λ�ã����ش˺�����Ӧ WM_SETTINGCHANGE ��Ϣ
  virtual void OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName)
  {
      //�����µ���Ļ����Ĵ��ڴ�С���ؼ�λ��
      DEVMODE  devMode;
      memset(&devMode, 0, sizeof(DEVMODE));
      devMode.dmSize = sizeof(DEVMODE);
      devMode.dmFields = DM_DISPLAYORIENTATION;
      ChangeDisplaySettingsEx(NULL, &devMode, NULL, CDS_TEST, NULL);

      //����
      if (devMode.dmDisplayOrientation == DMDO_90 || devMode.dmDisplayOrientation == DMDO_270)
      {
          RECT rc = MzGetWorkArea();
          SetWindowPos(m_hWnd, rc.left, rc.top,720, RECT_WIDTH(rc) );

      }

      //����
      if (devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0)
      {
		 
      }
  }

};
#endif //__NewSmsWnd_h__