/************************************************************************/
/*
 * Copyright (C) Meizu Technology Corporation Zhuhai China
 * All rights reserved.
 * 中国珠海, 魅族科技有限公司, 版权所有.
 *
 * This file is a part of the Meizu Foundation Classes library.
 * Author:    Michael
 * Create on: 2008-12-1
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

#include "MainWnd.h"

MZ_IMPLEMENT_DYNAMIC(CMainWnd)

// 从 CMzApp 派生的应用程序类
class CInfoCenterOfSmartPhoneApp: public CMzApp
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
CInfoCenterOfSmartPhoneApp theApp;

