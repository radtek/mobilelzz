//包含MZFC库的头文件
#include"stdafx.h"

//此代码演示了：
//  创建和初始化应用程序
//  创建和初始化窗体
//  按钮控件的使用及其命令消息的处理

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
  // 应用程序的主窗口
//  CNewSmsWnd m_MainWnd;
	CEasySmsMainWnd		m_MainWnd;
  // 应用程序的初始化
  virtual BOOL Init()
  {
	// 初始化 COM 组件

	  HANDLE  hSem = CreateSemaphore(NULL, 1,1, L"LZZEasySMS");
	  if( GetLastError() == ERROR_ALREADY_EXISTS )
	  {
		  // 关闭信号量句柄 
		  CloseHandle(hSem); 
		  HWND hw = FindWindow(NULL, L"易短信");
		  if( hw != NULL )
		  {
			  ::ShowWindow(hw,SW_SHOWMAXIMIZED); 
			  // 将主窗激活 
			  ::SetForegroundWindow(hw); 
		  }
		  exit(1); 
	  }


    CoInitializeEx(0, COINIT_MULTITHREADED);

	// 根据命令行，创建指定的窗口
	wchar_t	*pCmdLine	=	GetCommandLine();
	if ( L'\0' == pCmdLine[0] )			//创建主窗口
	{
		RECT rcWork = MzGetWorkArea();
		m_MainWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
		m_MainWnd.Show();
	}
// 	else if ( )
// 	{
// 	}

#if 0
	// 创建主窗口
    RECT rcWork = MzGetWorkArea();
    m_MainWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
    m_MainWnd.Show();
#endif
	// 成功则返回TRUE
    return TRUE;
  }
};

// 全局的应用程序对象
CMainApp theApp;

