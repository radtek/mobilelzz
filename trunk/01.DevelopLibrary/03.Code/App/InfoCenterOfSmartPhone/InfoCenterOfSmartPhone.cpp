//����MZFC���ͷ�ļ�
#include"stdafx.h"

//�˴�����ʾ�ˣ�
//  �����ͳ�ʼ��Ӧ�ó���
//  �����ͳ�ʼ������
//  ��ť�ؼ���ʹ�ü���������Ϣ�Ĵ���

#include"MainWnd.h"


MZ_IMPLEMENT_DYNAMIC(CMainWnd)

// �� CMzApp ������Ӧ�ó�����
class CMainApp: public CMzApp
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
CMainApp theApp;

