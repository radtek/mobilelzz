#ifndef __CommonTypes_h__
#define __CommonTypes_h__


// �б�����Զ�������
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
	CMzString StringTitle;  // ������ı�
	CMzString StringDescription;  // ��������ı�
	BOOL Selected; // ���Ƿ�ѡ��
	long lPID;
	wchar_t	wcsfirstLetter[2];
};
extern COMMONLIB_API BOOL g_bH;

extern COMMONLIB_API CDynamicArray<MyListItemData> g_ReciversList;
extern  COMMONLIB_API BOOL  g_bContactShow;

#define	LICENSE
#define Invalid_ID			0xFFFFFFFF

#endif // __CommonTypes_h__