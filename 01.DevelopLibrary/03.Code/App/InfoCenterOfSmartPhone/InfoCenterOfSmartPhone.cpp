//����MZFC���ͷ�ļ�
#include"stdafx.h"

//�˴�����ʾ�ˣ�
//  �����ͳ�ʼ��Ӧ�ó���
//  �����ͳ�ʼ������
//  ��ť�ؼ���ʹ�ü���������Ϣ�Ĵ���

#include "ContactorsWnd.h"
#include "EasySmsMainWnd.h"
#include "SmsLookCtorWnd.h"
#include "SmsLookMsgWnd.h"
#include "SmsUnReadWnd.h"
#include "EasySmsWndBase.h"
#include "SmsLookMsgDetailWnd.h"
#include "SmsFindWnd.h"
#include "SmsFindResultWnd.h"
#include "SmsEncrytpCtorWnd.h"
#include "SmsPassInputWnd.h"
#include "SmsPassConfirmWnd.h"

MZ_IMPLEMENT_DYNAMIC( CEasySmsWndBase )
MZ_IMPLEMENT_DYNAMIC( CContactorsWnd )
MZ_IMPLEMENT_DYNAMIC( CNewSmsWnd )
MZ_IMPLEMENT_DYNAMIC( CEasySmsMainWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsLookCtorWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsLookMsgWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsUnReadWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsLookMsgDetailWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsFindResultWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsFindWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsEncrytpCtorWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsPassInputWnd )
MZ_IMPLEMENT_DYNAMIC( CSmsPassConfirmWnd )



class CMainApp: public CMzApp
{
public:
  // Ӧ�ó����������
	CNewSmsWnd			m_SmsWnd;
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

		if ( MzGetParam ( MZGP_APP_START_ANIMATION ) ==TRUE )   
		{
			m_MainWnd.AnimateWindow( getScreenRandom() , true);
		}

		m_MainWnd.Show();
	}
 	else if ( 0 == wcscmp( pCmdLine, L"NewSms" ) )
 	{
		RECT rcWork = MzGetWorkArea();
		m_SmsWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);

		if ( MzGetParam ( MZGP_APP_START_ANIMATION ) ==TRUE )   
		{
			m_SmsWnd.AnimateWindow( getScreenRandom() , true);
		}

		m_SmsWnd.DoModal();
//		m_SmsWnd.Show();
 	}

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

