// stdafx.h : ��׼ϵͳ�����ļ��İ����ļ���
// ���Ǿ���ʹ�õ��������ĵ�
// �ض�����Ŀ�İ����ļ�
//

#include "resource.h"

#include<wtypes.h>
#include <mzfc_inc.h>
#include <acc_api.h>
#include <ShellNotifyMsg.h>
#include "UsbNotifyApi.h"
#include "CallNotifyApi.h"
//#include <string>
#include <vector>
using namespace std;


#include "CommonLib.h"
#include "Errors.h"
#include"FuncLib.h"
#include"CommonTypes.h"

#include "Core.h"
#include "windows.h"
#include "atlbase.h"
//��ʾͼƬ�Ĳ�ͬģʽ
static DWORD modeId[4]=
{
	MZ_PAINTMODE_NORMAL,                    //������ʾ
	MZ_PAINTMODE_TILE,                      //ƽ��
	MZ_PAINTMODE_STRETCH_H,                 //ˮƽ�������� (�����ñ߾�)
	MZ_PAINTMODE_STRETCH_H                  //ˮƽ�������� (���ñ߾�)
};

long	getScreenRandom ( int iRan = 16 );

#define UI_Recievers_String_Length			(512)
// TODO: �ڴ˴����ó�����Ҫ������ͷ�ļ�
