#ifndef __CommonTypes_h__
#define __CommonTypes_h__


// �б�����Զ�������
class MyListItemData
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
extern BOOL g_bH;

extern CDynamicArray<MyListItemData> g_ReciversList;
extern  BOOL  g_bContactShow;

#define	LICENSE


#endif // __CommonTypes_h__