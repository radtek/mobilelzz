#ifndef __CommonTypes_h__
#define __CommonTypes_h__


// 列表项的自定义数据
class MyListItemData
{
public:
	MyListItemData()
	{
		Selected = FALSE;
		lPID = 0;
	}
	BOOL Compare(MyListItemData* pTarget)
	{
		if(pTarget->lPID == lPID)
		{
			return TRUE;
		}
		else
		{
			return FALSE;
		}
	}
	CMzString StringTitle;  // 项的主文本
	CMzString StringDescription;  // 项的描述文本
	BOOL Selected; // 项是否被选中
	long lPID;
};

extern BOOL g_bH;

extern CDynamicArray<MyListItemData> g_ReciversList;

#endif // __CommonTypes_h__