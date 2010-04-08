//����MZFC���ͷ�ļ�
#include"stdafx.h"

//�˴�����ʾ�ˣ�
//  �����ͳ�ʼ��Ӧ�ó���
//  �����ͳ�ʼ������
//  ��ť�ؼ���ʹ�ü���������Ϣ�Ĵ���

#include"ContactorsWnd.h"
#include"MainWnd.h"
#include"EasySmsMainWnd.h"
#include "SmsLookCtorWnd.h"
#include "SmsLookMsgWnd.h"
#include "SmsReadWnd.h"

MZ_IMPLEMENT_DYNAMIC(CContactorsWnd)
MZ_IMPLEMENT_DYNAMIC(CNewSmsWnd)
MZ_IMPLEMENT_DYNAMIC(CMainWnd)
MZ_IMPLEMENT_DYNAMIC(CEasySmsMainWnd)
MZ_IMPLEMENT_DYNAMIC(CSmsLookCtorWnd)
MZ_IMPLEMENT_DYNAMIC(CSmsLookMsgWnd)
MZ_IMPLEMENT_DYNAMIC(CSmsReadWnd)

class CMainApp: public CMzApp
{
public:
  // Ӧ�ó����������
//  CNewSmsWnd m_MainWnd;
	CEasySmsMainWnd		m_MainWnd;
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

	// ���������У�����ָ���Ĵ���
	wchar_t	*pCmdLine	=	GetCommandLine();
	if ( L'\0' == pCmdLine[0] )			//����������
	{
		RECT rcWork = MzGetWorkArea();
		m_MainWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
		m_MainWnd.Show();
	}
// 	else if ( )
// 	{
// 	}

#if 0
	// ����������
    RECT rcWork = MzGetWorkArea();
    m_MainWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
    m_MainWnd.Show();
#endif
	// �ɹ��򷵻�TRUE
    return TRUE;
  }
};

// ȫ�ֵ�Ӧ�ó������
CMainApp theApp;

