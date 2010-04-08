//����MZFC���ͷ�ļ�
#include"stdafx.h"

//�˴�����ʾ�ˣ�
//  �����ͳ�ʼ��Ӧ�ó���
//  �����ͳ�ʼ������
//  ��ť�ؼ���ʹ�ü���������Ϣ�Ĵ���

#include"ContactorsWnd.h"
#include"MainWnd.h"

MZ_IMPLEMENT_DYNAMIC(CContactorsWnd)
MZ_IMPLEMENT_DYNAMIC(CNewSmsWnd)
MZ_IMPLEMENT_DYNAMIC(CMainWnd)

// �� CMzApp ������Ӧ�ó�����
class CMainApp: public CMzApp
{
public:
  // Ӧ�ó����������
  CNewSmsWnd m_MainWnd;

  // Ӧ�ó���ĳ�ʼ��
  virtual BOOL Init()
  {
	// ��ʼ�� COM ���

	  HANDLE  hSem = CreateSemaphore(NULL, 1,1, L"LZZEasySMS");
	  if( GetLastError() == ERROR_ALREADY_EXISTS )
	  {
		  // �ر��ź������ 
		  CloseHandle(hSem); 
		  HWND hw = FindWindow(NULL, L"�׶���");
		  if( hw != NULL )
		  {
			  ::ShowWindow(hw,SW_SHOWMAXIMIZED); 
			  // ���������� 
			  ::SetForegroundWindow(hw); 
		  }
		  exit(1); 
	  }


    CoInitializeEx(0, COINIT_MULTITHREADED);


	// ����������
    RECT rcWork = MzGetWorkArea();
    m_MainWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
    m_MainWnd.Show();

	// �ɹ��򷵻�TRUE
    return TRUE;
  }
};

// ȫ�ֵ�Ӧ�ó������
CMainApp theApp;
