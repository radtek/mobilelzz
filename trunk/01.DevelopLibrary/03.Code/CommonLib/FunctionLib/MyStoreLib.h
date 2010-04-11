#include <Windows.h>

// ��֤������ݽṹ��
typedef struct COMMONLIB_API stMYSTORE_VERIFY_CONTEXT 
{
    DWORD Ver;              //< �汾��
    DWORD LicenseValid;     //< ֤���Ƿ�Ϸ���0���Ƿ���1���Ϸ���
    DWORD Expired;          //< �Ƿ���ڣ�0��δ���ڣ�1�����ڣ�
    DWORD ExpireDate;       //< ��������(��2010��1��28�գ� 20100128��
    BYTE  Reserved[19];     //< �����ֶ�

} MYSTORE_VERIFY_CONTEXT;

/*
 * ��֤MyStore������ĵ���Ʒ�ĺϷ���
 * @param pszFileName Ҫ��֤���ļ����ļ���������·����
 * @param pmyStoreVerifyContext ��֤�����Ϣ
 * @return 0�������ɹ���1����pszFileName�ļ�ʧ�ܣ�2:��License�ļ�ʧ�ܣ�3����֤ʧ�ܣ�4�����кŻ�ȡʧ��

 \code
 #include "MyStoreLib.h"
 #pragma comment(lib, "MyStoreLib.lib")
 #pragma comment(lib, "PlatformAPI.lib")
 #pragma comment(lib, "PhoneAdapter.lib")

    wchar_t pszFileName[MAX_PATH] = {0};
    GetModuleFileName(0, pszFileName, MAX_PATH);            // ��ǰ����EXE���ļ���
    MYSTORE_VERIFY_CONTEXT mystore = {0};
    DWORD dwRet = MyStoreVerify(pszFileName, &mystore);     // ��֤���ļ��Ƿ�Ϸ�

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

 \endcode

 */
DWORD COMMONLIB_API MyStoreVerify(const wchar_t* pszFileName, __out MYSTORE_VERIFY_CONTEXT* pmyStoreVerifyContext);

