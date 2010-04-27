#ifndef __RecieversDynamicArray_h__
#define __RecieversDynamicArray_h__

// �б�����Զ�������
#include "DynamicArray.h"

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

class COMMONLIB_API CRecieversDynamicArray : public CDynamicArray<MyListItemData>
{
public:
	CRecieversDynamicArray()
	{
		m_lInitPos = 4;
	}

	virtual ~CRecieversDynamicArray()
	{
	}

	void AppendItem(MyListItemData* pT)
	{
		//append ; to item name

		CDynamicArray<MyListItemData>::AppendItem(pT);
		//calc item pos range
		
	}
	void DeleteItemByCursorPos(long lCursorPos)
	{
		//find item
		
		//CDynamicArray<MyListItemData>::DeleteItem(/*index*/);
		
	}
	void InsertItemByPos(long lPos)
	{
		//find pos

		//CDynamicArray<MyListItemData>::InsertItem(/*index*/);

	}

private:
	long m_lInitPos;
};

#endif // __RecieversDynamicArray_h__