// stdafx.h : ��׼ϵͳ�����ļ��İ����ļ���
// ���Ǿ���ʹ�õ��������ĵ�
// �ض�����Ŀ�İ����ļ�
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
// TODO: �ڴ˴����ó�����Ҫ������ͷ�ļ�
