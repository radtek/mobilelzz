//����MZFC���ͷ�ļ�
#include"stdafx.h"

//�˴�����ʾ�ˣ�
//  �����ͳ�ʼ��Ӧ�ó���
//  �����ͳ�ʼ������
//  ��ť�ؼ���ʹ�ü���������Ϣ�Ĵ���

#include"ContactorsWnd.h"
#include "NewSmsWnd.h"

MZ_IMPLEMENT_DYNAMIC(CContactorsWnd)
MZ_IMPLEMENT_DYNAMIC(CNewSmsWnd)

// �� CMzApp ������Ӧ�ó�����
class CMainApp: public CMzApp
{
public:
	CMainApp()
	{
		m_pMainWnd = NULL;
	}
	virtual ~CMainApp()
	{
		if ( NULL != m_pMainWnd ){
			delete m_pMainWnd;
			m_pMainWnd = NULL;
		}
	}
	// Ӧ�ó����������
	CNewSmsWnd* m_pMainWnd;

	// Ӧ�ó���ĳ�ʼ��
	virtual BOOL Init()
	{
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
		if(!F_LicenseProtect())
		{
			MzMessageBoxEx(NULL,L"��Ȩ�ļ�У��ʧ�ܣ���Ŀǰʹ�õ��Ǹ���������ð�",MB_OK);
			g_bIsTrial	=	TRUE;
		}

		// ��ʼ�� COM ���
		CoInitializeEx(0, COINIT_MULTITHREADED);
		// ����������
		RECT rcWork = MzGetWorkArea();
		if ( NULL != m_pMainWnd ){
			delete m_pMainWnd;
			m_pMainWnd = NULL;
		}
		m_pMainWnd = new CNewSmsWnd;
		m_pMainWnd->Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
		m_pMainWnd->Show();
		HWND hw = FindWindow(NULL, L"�׶���");
		if( hw != NULL )
		{
			::ShowWindow(hw,SW_SHOWMAXIMIZED); 
			// ���������� 
			::SetForegroundWindow(hw); 
		}
		// �ɹ��򷵻�TRUE
		return TRUE;
	}
};

// ȫ�ֵ�Ӧ�ó������
CMainApp theApp;

