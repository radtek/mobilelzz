// stdafx.h : 标准系统包含文件的包含文件，
// 或是经常使用但不常更改的
// 特定于项目的包含文件
//

#include<wtypes.h>
#include <mzfc_inc.h>
#include <acc_api.h>
#include <ShellNotifyMsg.h>
// 列表项的自定义数据
class MyListItemData
{
public:
  CMzString StringTitle;  // 项的主文本
  CMzString StringDescription;  // 项的描述文本
  BOOL Selected; // 项是否被选中

};

// TODO: 在此处引用程序需要的其他头文件
