// stdafx.cpp : ֻ������׼�����ļ���Դ�ļ�
// InfoCenterOfSmartPhone.pch ����ΪԤ����ͷ
// stdafx.obj ������Ԥ����������Ϣ

#include "stdafx.h"

// TODO: �� STDAFX.H ��
// �����κ�����ĸ���ͷ�ļ����������ڴ��ļ�������
long	getScreenRandom ( int iRan  )
{
	srand( GetTickCount() );

	return	( rand() % iRan );
}