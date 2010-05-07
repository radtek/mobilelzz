#ifndef __RecieversDynamicArray_h__
#define __RecieversDynamicArray_h__

// 列表项的自定义数据
#include "DynamicArray.h"
#define Invalid_4Byte		0xFFFFFFFF
class COMMONLIB_API MyListItemData
{
public:
	MyListItemData()
	{
		Selected = FALSE;
		lPID = 0;
		memset(wcsfirstLetter, 0x0, sizeof(wcsfirstLetter));
		lBeginPos = 0;
		lEndPos = 0;
	}
	MyListItemData(const MyListItemData& Source)
	{
		StringTitle = Source.StringTitle;
		StringDescription = Source.StringDescription;
		Selected = Source.Selected;
		lPID = Source.lPID;
		wcsfirstLetter[0] = Source.wcsfirstLetter[0];
		lBeginPos = Source.lBeginPos;
		lEndPos = Source.lEndPos;
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

	long lBeginPos;
	long lEndPos;
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
		pT->StringTitle += L";";
		pT->lEndPos = pT->StringTitle.Length();
		if ( m_lDynamicArrayDataCount ){
			MyListItemData* pTempLast = CDynamicArray<MyListItemData>::GetItem(m_lDynamicArrayDataCount-1);
			if ( pTempLast ){
				//calc last item pos range
				pT->lBeginPos += (pTempLast->lEndPos+1);
				pT->lEndPos += (pTempLast->lEndPos+1);							
			}
		}else{
			pT->lBeginPos += m_lInitPos;
			pT->lEndPos += m_lInitPos;
		}
		CDynamicArray<MyListItemData>::AppendItem(pT);
	}
	void DeleteItemByCursorPos(long lCursorPos)
	{
		//find item index by pos
		long lIndex = FindItemIndexByPos(lCursorPos);
		DeleteItem(lIndex);
		UpdateItemPosRangeFromIdx(lIndex);
	}
	void InsertItemByPos(MyListItemData* pT, long lPos)
	{
		//find item index by pos
		long lIndex = FindItemIndexByPos(lPos);
		InsertItem(lIndex, pT);
		UpdateItemPosRangeFromIdx(lIndex);
	}
	long FindItemIndexByPos(long lPos)
	{
		for ( int i = 0; i < m_lDynamicArrayDataCount; i++ )
		{
			if ( (m_pDynamicArrayData[i].lBeginPos <= lPos) && 
				(m_pDynamicArrayData[i].lEndPos >= lPos) ){
					return i;
			}
		}
		return Invalid_4Byte;
	}
	
	void UpdateItemPosRangeFromIdx(long lIndex)
	{
		MyListItemData* pTemp = GetItem(lIndex-1);
		for ( int i = 0; i < (m_lDynamicArrayDataCount-lIndex); i++ )
		{
			m_pDynamicArrayData[lIndex+i].lBeginPos = pTemp->lEndPos+1;
			m_pDynamicArrayData[lIndex+i].lEndPos = 
				m_pDynamicArrayData[lIndex+i].lBeginPos+m_pDynamicArrayData[lIndex+i].StringTitle.Length()-1;
			pTemp = &m_pDynamicArrayData[lIndex+i];
		}

	}

private:
	long m_lInitPos;
};

#endif // __RecieversDynamicArray_h__