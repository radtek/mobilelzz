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
};

extern BOOL g_bH;

extern CDynamicArray<MyListItemData> g_ReciversList;

#endif // __CommonTypes_h__