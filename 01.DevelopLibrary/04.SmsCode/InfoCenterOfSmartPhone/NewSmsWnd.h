
#ifndef __NewSmsWnd_h__
#define __NewSmsWnd_h__
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

#define PROGRESS_TIMER_ID		111


#define BUTTON_WIDTH_V	100
#define BUTTON_WIDTH_H	100

#define BUTTON_HEIGHT_VH 65
// �� CMzWndEx ��������������


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
  // ���ڵĳ�ʼ��
  virtual BOOL OnInitDialog();

  
  // ����������Ϣ�Ĵ�����
  virtual void OnMzCommand(WPARAM wParam, LPARAM lParam);

  LRESULT MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam);

  // ת���������Ҫ�������ڵ�λ�ã����ش˺�����Ӧ WM_SETTINGCHANGE ��Ϣ
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
	// �ı�
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