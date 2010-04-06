/************************************************************************/
/*
 * Copyright (C) Meizu Technology Corporation Zhuhai China
 * All rights reserved.
 * 中国珠海, 魅族科技有限公司, 版权所有.
 *
 * This file is a part of the Meizu Foundation Classes library.
 * Author:    
 * Create on: 2010-01-29
 */
/************************************************************************/

//请按照以步骤运行此实例代码：
//首先, 打开VS2005/2008创建一个Win 32智能设备项目
//在项目向导中选择M8SDK, 并勾选空项目
//然后,在项目中新建一个cpp文件,将此处代码拷贝到cpp文件中
//最后,按照M8SDK的帮助文档,配置项目属性
//现在,可以运行此程序了

//包含MZFC库的头文件
#include <mzfc_inc.h>

#include <ShellWidget/ShellWidget.h>

//此代码演示了：
//  创建和初始化应用程序
//  创建和初始化窗体
//  测试Widget桌面插件

#define MZ_IDC_TOOLBARPRO   101

typedef UiWidget* (*PFNCreateWidgetFromLibrary)(void*);

UiWidget* GetWidget( TCHAR* pszFilePath )
{
    UiWidget* pWidget = NULL;

    // 载入DLL文件
    HMODULE h = LoadLibrary(pszFilePath);
    if(h)
    {
        PFNCreateWidgetFromLibrary proc = (PFNCreateWidgetFromLibrary)GetProcAddress(h, L"CreateWidgetInstance");
        if(proc)
        {
            pWidget = proc(0);
        }
    }

    return pWidget;
}

// 从 CMzWndEx 派生的主窗口类
class CSample1MainWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CSample1MainWnd);
public:
    UiToolBarPro m_ToolBar;
    UiWidget* m_pClockWidget;

protected:
  // 窗口的初始化
  virtual BOOL OnInitDialog()
  {
    // 必须先调用基类的初始化
    if (!CMzWndEx::OnInitDialog())
    {
      return FALSE;
    }

    // 初始化 UiToolBarPro 控件
    m_ToolBar.SetID(MZ_IDC_TOOLBARPRO);
    m_ToolBar.SetPos(0, GetHeight() - MZM_HEIGHT_TOOLBARPRO, GetWidth(), MZM_HEIGHT_TOOLBARPRO);
    m_ToolBar.SetButton(TOOLBARPRO_LEFT_TEXTBUTTON, true, true, L"加载Widget");
    m_ToolBar.SetButton(TOOLBARPRO_RIGHT_TEXTBUTTON, true, true, L"卸载Widget");

    AddUiWin(&m_ToolBar);

    return TRUE;
  }

  // 重载 MZFC 的命令消息处理函数
  virtual void OnMzCommand(WPARAM wParam, LPARAM lParam)
  {
      UINT_PTR id = LOWORD(wParam);
      switch(id)
      {
      case MZ_IDC_TOOLBARPRO:
          {
              int nIndex = lParam;

              // 处理左边固定文字按键的消息，加载 Widget
              if (nIndex == TOOLBARPRO_LEFT_TEXTBUTTON)
              {
                  if (!m_pClockWidget)
                  {
                      // 创建Widget对象
                      m_pClockWidget = GetWidget(L"\\windows\\ClockWidget.dll");
                      int nW = 0;
                      int nH = 0;
                      m_pClockWidget->OnCalcItemSize(nW, nH);
                      m_pClockWidget->SetPos(50, 50, DESKTOPITEM_WIDTH * nW, DESKTOPITEM_HEIGHT * nH);

                      // 把Widget添加到窗口中
                      AddUiWin(m_pClockWidget);
                      // 启动Widget
                      m_pClockWidget->StartWidget();
                  }
              }
              // 处理右边固定文字按钮的消息，卸载 Widget
              else if (nIndex == TOOLBARPRO_RIGHT_TEXTBUTTON)
              {
                  if (m_pClockWidget)
                  {
                      // 无效控件所在区域
                      m_pClockWidget->Invalidate(NULL);
                      // 从窗口中移除指定的Widget
                      RemoveUiWin(m_pClockWidget);
                      // 更新控件所在窗口
                      m_pClockWidget->Update();

                      m_pClockWidget->EndWidget();
                      delete m_pClockWidget;
                      m_pClockWidget = NULL;
                  }
              }
          }
          break;
      }
  } 

};

MZ_IMPLEMENT_DYNAMIC(CSample1MainWnd)

// 从 CMzApp 派生的应用程序类
class CSample1App: public CMzApp
{
public:
  // 应用程序的主窗口
  CSample1MainWnd m_MainWnd;

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
CSample1App theApp;

