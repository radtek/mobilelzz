//包含MZFC库的头文件
#include"stdafx.h"

//此代码演示了：
//  创建和初始化应用程序
//  创建和初始化窗体
//  按钮控件的使用及其命令消息的处理

#include"ContactorsWnd.h"
#include"MainWnd.h"

MZ_IMPLEMENT_DYNAMIC(CContactorsWnd)
MZ_IMPLEMENT_DYNAMIC(CNewSmsWnd)
MZ_IMPLEMENT_DYNAMIC(CMainWnd)

// 从 CMzApp 派生的应用程序类
class CMainApp: public CMzApp
{
public:
  // 应用程序的主窗口
  CNewSmsWnd m_MainWnd;

  // 应用程序的初始化
  virtual BOOL Init()
  {
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


	  if(!LicenseProtect())
	  {
//		  MzMessageBoxEx(NULL,L"授权文件校验失败，请重新下载安装",MB_OK);
		  MzMessageBoxEx(NULL,L"授权文件校验失败，您目前使用的是该软件的试用版",MB_OK);
		  g_bIsTrial	=	TRUE;
//		  exit(0);
	  }

	// 初始化 COM 组件




    CoInitializeEx(0, COINIT_MULTITHREADED);


	// 创建主窗口
    RECT rcWork = MzGetWorkArea();
    m_MainWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0);
    m_MainWnd.Show();

	// 成功则返回TRUE
    return TRUE;
  }
};

// 全局的应用程序对象
CMainApp theApp;

