// stdafx.cpp : ֻ������׼�����ļ���Դ�ļ�
// InfoCenterOfSmartPhone.pch ����ΪԤ����ͷ
// stdafx.obj ������Ԥ����������Ϣ

#include "stdafx.h"
BOOL g_bH = FALSE;
CReciversList g_ReciversList;

BOOL	g_bContactShow = FALSE;

//�Ƿ������ð��flg
BOOL	g_bIsTrial		=	FALSE;
// TODO: �� STDAFX.H ��
// �����κ�����ĸ���ͷ�ļ����������ڴ��ļ�������

 

bool	LicenseProtect()
{
	wchar_t pszFileName[MAX_PATH] = {0};
	GetModuleFileName(0, pszFileName, MAX_PATH);            // ��ǰ����EXE���ļ���
	MYSTORE_VERIFY_CONTEXT mystore = {0};
	DWORD dwRet = MyStoreVerify(pszFileName, &mystore);     // ��֤���ļ��Ƿ�Ϸ�

	bool	bRlt	=	false;

	switch(dwRet)
	{
	case 0:
		{
			//��֤���Ϊ: �Ϸ�
			//  �����Ƿ񳬹�������
			//  ������Ӧ�ó����������

			RETAILMSG(1, (L"��֤���Ϊ: �Ϸ�, LicenseValid:%u, Expired:%u, ��������ֹ����:%u\n", 
				mystore.LicenseValid, mystore.Expired, mystore.ExpireDate ));

			if(mystore.Expired)
			{
				// ����������
				// ...
			}
			else
			{
				// δ����������
				// ...
				bRlt	=	true;
			}
		}
		break;
	case 1:
		{
			//��֤���Ϊ: ��pszFileName�ļ�ʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ��pszFileName�ļ�ʧ��\n"));
		}
		break;
	case 2:
		{
			//��֤���Ϊ: ��License�ļ�ʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ��License�ļ�ʧ��\n"));
		}
		break;
	case 3:
		{
			//��֤���Ϊ: ��֤ʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ��֤ʧ��\n"));
		}
		break;
	case 4:
		{
			//��֤���Ϊ: ���кŻ�ȡʧ��
			// ...�˳�Ӧ�ó���
			RETAILMSG(1, (L"��֤���Ϊ: ���кŻ�ȡʧ��\n"));
		}
		break;
	}

	return	bRlt;
}
