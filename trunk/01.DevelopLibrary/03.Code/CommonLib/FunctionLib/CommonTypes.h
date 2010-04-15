#ifndef __CommonTypes_h__
#define __CommonTypes_h__


// 列表项的自定义数据
class COMMONLIB_API MyListItemData
{
public:
	MyListItemData()
	{
		Selected = FALSE;
		lPID = 0;
		memset(wcsfirstLetter, 0x0, sizeof(wcsfirstLetter));
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
	wchar_t	wcsfirstLetter[2];
};
extern COMMONLIB_API BOOL g_bH;

extern COMMONLIB_API CDynamicArray<MyListItemData> g_ReciversList;
extern  COMMONLIB_API BOOL  g_bContactShow;

#define	LICENSE
#define Invalid_ID			0xFFFFFFFF

#endif // __CommonTypes_h__