//包含MZFC库的头文件
#include"stdafx.h"

//此代码演示了：
//  创建和初始化应用程序
//  创建和初始化窗体
//  按钮控件的使用及其命令消息的处理

#include"MainWnd.h"


MZ_IMPLEMENT_DYNAMIC(CMainWnd)

// 从 CMzApp 派生的应用程序类
class CMainApp: public CMzApp
{
public:
  // 应用程序的主窗口
  CMainWnd m_MainWnd;

  // 应用程序的初始化
  virtual BOOL Init()
  {
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

