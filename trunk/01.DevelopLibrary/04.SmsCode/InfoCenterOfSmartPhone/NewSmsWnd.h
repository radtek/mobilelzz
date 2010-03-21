
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


#define BUTTON_WIDTH_V	150
#define BUTTON_WIDTH_H	150

#define BUTTON_HEIGHT_VH 65
// �� CMzWndEx ��������������


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
  // ��ť
  UiButton m_SendSmsBtn;
  UiButton m_ContactorsBtn;
  MzPopupProgress m_progress;
  long m_lCurProgress;

  // �ı�
  UiEditControl				m_Recievers;
  CMyEdit*					m_SmsMsgEdit;

  DWORD m_accMsg;
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

};
#endif //__NewSmsWnd_h__