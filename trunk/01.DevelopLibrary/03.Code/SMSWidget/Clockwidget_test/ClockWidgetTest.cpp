/************************************************************************/
/*
 * Copyright (C) Meizu Technology Corporation Zhuhai China
 * All rights reserved.
 * �й��麣, ����Ƽ����޹�˾, ��Ȩ����.
 *
 * This file is a part of the Meizu Foundation Classes library.
 * Author:    
 * Create on: 2010-01-29
 */
/************************************************************************/

//�밴���Բ������д�ʵ�����룺
//����, ��VS2005/2008����һ��Win 32�����豸��Ŀ
//����Ŀ����ѡ��M8SDK, ����ѡ����Ŀ
//Ȼ��,����Ŀ���½�һ��cpp�ļ�,���˴����뿽����cpp�ļ���
//���,����M8SDK�İ����ĵ�,������Ŀ����
//����,�������д˳�����

//����MZFC���ͷ�ļ�
#include <mzfc_inc.h>

#include <ShellWidget/ShellWidget.h>

//�˴�����ʾ�ˣ�
//  �����ͳ�ʼ��Ӧ�ó���
//  �����ͳ�ʼ������
//  ����Widget������

#define MZ_IDC_TOOLBARPRO   101

typedef UiWidget* (*PFNCreateWidgetFromLibrary)(void*);

UiWidget* GetWidget( TCHAR* pszFilePath )
{
    UiWidget* pWidget = NULL;

    // ����DLL�ļ�
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

// �� CMzWndEx ��������������
class CSample1MainWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CSample1MainWnd);
public:
    UiToolBarPro m_ToolBar;
    UiWidget* m_pClockWidget;

protected:
  // ���ڵĳ�ʼ��
  virtual BOOL OnInitDialog()
  {
    // �����ȵ��û���ĳ�ʼ��
    if (!CMzWndEx::OnInitDialog())
    {
      return FALSE;
    }

    // ��ʼ�� UiToolBarPro �ؼ�
    m_ToolBar.SetID(MZ_IDC_TOOLBARPRO);
    m_ToolBar.SetPos(0, GetHeight() - MZM_HEIGHT_TOOLBARPRO, GetWidth(), MZM_HEIGHT_TOOLBARPRO);
    m_ToolBar.SetButton(TOOLBARPRO_LEFT_TEXTBUTTON, true, true, L"����Widget");
    m_ToolBar.SetButton(TOOLBARPRO_RIGHT_TEXTBUTTON, true, true, L"ж��Widget");

    AddUiWin(&m_ToolBar);

    return TRUE;
  }

  // ���� MZFC ��������Ϣ������
  virtual void OnMzCommand(WPARAM wParam, LPARAM lParam)
  {
      UINT_PTR id = LOWORD(wParam);
      switch(id)
      {
      case MZ_IDC_TOOLBARPRO:
          {
              int nIndex = lParam;

              // ������߹̶����ְ�������Ϣ������ Widget
              if (nIndex == TOOLBARPRO_LEFT_TEXTBUTTON)
              {
                  if (!m_pClockWidget)
                  {
                      // ����Widget����
                      m_pClockWidget = GetWidget(L"\\windows\\ClockWidget.dll");
                      int nW = 0;
                      int nH = 0;
                      m_pClockWidget->OnCalcItemSize(nW, nH);
                      m_pClockWidget->SetPos(50, 50, DESKTOPITEM_WIDTH * nW, DESKTOPITEM_HEIGHT * nH);

                      // ��Widget��ӵ�������
                      AddUiWin(m_pClockWidget);
                      // ����Widget
                      m_pClockWidget->StartWidget();
                  }
              }
              // �����ұ߹̶����ְ�ť����Ϣ��ж�� Widget
              else if (nIndex == TOOLBARPRO_RIGHT_TEXTBUTTON)
              {
                  if (m_pClockWidget)
                  {
                      // ��Ч�ؼ���������
                      m_pClockWidget->Invalidate(NULL);
                      // �Ӵ������Ƴ�ָ����Widget
                      RemoveUiWin(m_pClockWidget);
                      // ���¿ؼ����ڴ���
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

// �� CMzApp ������Ӧ�ó�����
class CSample1App: public CMzApp
{
public:
  // Ӧ�ó����������
  CSample1MainWnd m_MainWnd;

  // Ӧ�ó���ĳ�ʼ��
  virtual BOOL Init()
  {
	// ��ʼ�� COM ���
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
CSample1App theApp;

