#include <Windows.h>

// 验证结果数据结构体
typedef struct COMMONLIB_API stMYSTORE_VERIFY_CONTEXT 
{
    DWORD Ver;              //< 版本号
    DWORD LicenseValid;     //< 证书是否合法（0：非法，1：合法）
    DWORD Expired;          //< 是否过期（0：未过期，1：过期）
    DWORD ExpireDate;       //< 过期日期(如2010年1月28日： 20100128）
    BYTE  Reserved[19];     //< 保留字段

} MYSTORE_VERIFY_CONTEXT;

/*
 * 验证MyStore软件中心的商品的合法法
 * @param pszFileName 要验证的文件的文件名（完整路径）
 * @param pmyStoreVerifyContext 验证结果信息
 * @return 0：操作成功，1：打开pszFileName文件失败，2:打开License文件失败，3：验证失败，4：序列号获取失败

 \code
 #include "MyStoreLib.h"
 #pragma comment(lib, "MyStoreLib.lib")
 #pragma comment(lib, "PlatformAPI.lib")
 #pragma comment(lib, "PhoneAdapter.lib")

    wchar_t pszFileName[MAX_PATH] = {0};
    GetModuleFileName(0, pszFileName, MAX_PATH);            // 当前进程EXE的文件名
    MYSTORE_VERIFY_CONTEXT mystore = {0};
    DWORD dwRet = MyStoreVerify(pszFileName, &mystore);     // 验证此文件是否合法

    switch(dwRet)
    {
    case 0:
        {
            //验证结果为: 合法
            //  检验是否超过试用期
            //  决定让应用程序继续运行

            RETAILMSG(1, (L"验证结果为: 合法, LicenseValid:%u, Expired:%u, 试用期终止日期:%u\n", 
                mystore.LicenseValid, mystore.Expired, mystore.ExpireDate ));

            if(mystore.Expired)
            {
                // 超过试用期
                // ...
            }
            else
            {
                // 未超过试用期
                // ...
            }
        }
        break;
    case 1:
        {
            //验证结果为: 打开pszFileName文件失败
            // ...退出应用程序
            RETAILMSG(1, (L"验证结果为: 打开pszFileName文件失败\n"));
        }
        break;
    case 2:
        {
            //验证结果为: 打开License文件失败
            // ...退出应用程序
            RETAILMSG(1, (L"验证结果为: 打开License文件失败\n"));
        }
        break;
    case 3:
        {
            //验证结果为: 验证失败
            // ...退出应用程序
            RETAILMSG(1, (L"验证结果为: 验证失败\n"));
        }
        break;
    case 4:
        {
            //验证结果为: 序列号获取失败
            // ...退出应用程序
            RETAILMSG(1, (L"验证结果为: 序列号获取失败\n"));
        }
        break;
    }

 \endcode

 */
DWORD COMMONLIB_API MyStoreVerify(const wchar_t* pszFileName, __out MYSTORE_VERIFY_CONTEXT* pmyStoreVerifyContext);

