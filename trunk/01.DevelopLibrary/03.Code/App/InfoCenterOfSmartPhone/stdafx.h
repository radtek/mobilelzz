// stdafx.h : 标准系统包含文件的包含文件，
// 或是经常使用但不常更改的
// 特定于项目的包含文件
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
//显示图片的不同模式
static DWORD modeId[4]=
{
	MZ_PAINTMODE_NORMAL,                    //正常显示
	MZ_PAINTMODE_TILE,                      //平铺
	MZ_PAINTMODE_STRETCH_H,                 //水平方向拉伸 (不设置边距)
	MZ_PAINTMODE_STRETCH_H                  //水平方向拉伸 (设置边距)
};

long	getScreenRandom ( int iRan = 16 );

#define UI_Recievers_String_Length			(512)
// TODO: 在此处引用程序需要的其他头文件
