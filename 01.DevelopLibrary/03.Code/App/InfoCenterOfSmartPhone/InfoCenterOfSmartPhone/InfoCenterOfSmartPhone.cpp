/************************************************************************/
/*
 * Copyright (C) Meizu Technology Corporation Zhuhai China
 * All rights reserved.
 * �й��麣, ����Ƽ����޹�˾, ��Ȩ����.
 *
 * This file is a part of the Meizu Foundation Classes library.
 * Author:    Michael
 * Create on: 2008-12-1
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

#include "MainWnd.h"

MZ_IMPLEMENT_DYNAMIC(CMainWnd)

// �� CMzApp ������Ӧ�ó�����
class CInfoCenterOfSmartPhoneApp: public CMzApp
{
public:
  // Ӧ�ó����������
  CMainWnd m_MainWnd;

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
CInfoCenterOfSmartPhoneApp theApp;
