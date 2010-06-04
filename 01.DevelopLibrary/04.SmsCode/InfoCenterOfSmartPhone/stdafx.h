// stdafx.h : 标准系统包含文件的包含文件，
// 或是经常使用但不常更改的
// 特定于项目的包含文件
//

#include<wtypes.h>
#include <mzfc_inc.h>
#include <acc_api.h>
#include <ShellNotifyMsg.h>
#include <sms.h>
#include <sipapi.h>
#include <vector>
using namespace std;
//zhu.t add for License	at 2010-3-22
#include "MyStoreLib.h"
#pragma comment(lib, "MyStoreLib.lib")
#pragma comment(lib, "PlatformAPI.lib")
#pragma comment(lib, "PhoneAdapter.lib")
//end
#define	LICENSE
#define UI_Recievers_String_Length				(512)
//extern BOOL g_bH;
extern  CLSID g_clBackupSipID;
extern  CLSID g_clMeizuSipID;
extern  BOOL  g_bContactShow;
extern  BOOL  g_bIsTrial;
#include "RecieversDynamicArray.h"

extern CRecieversDynamicArray g_ReciversList;

#include "FuncLib.h"
// TODO: 在此处引用程序需要的其他头文件
