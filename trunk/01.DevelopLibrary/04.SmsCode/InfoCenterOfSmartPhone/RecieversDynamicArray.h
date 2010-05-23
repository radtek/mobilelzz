#ifndef __RecieversDynamicArray_h__
#define __RecieversDynamicArray_h__

// 列表项的自定义数据
#include "DynamicArray.h"
#define Invalid_4Byte		0xFFFFFFFF
// 列表项的自定义数据
class MyListItemData
{
public:
	MyListItemData()
	{
		Selected = FALSE;
		lPID = 0;
		memset(wcsfirstLetter, 0x0, sizeof(wcsfirstLetter));
		memset(wcsNameLetter, 0x0, sizeof(wcsNameLetter));
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
	wchar_t	wcsNameLetter[10];
};

class CRecieversDynamicArray : public CDynamicArray<MyListItemData>
{
public:
	CRecieversDynamicArray()
	{
		//m_lInitPos = 0;
	}

	virtual ~CRecieversDynamicArray()
	{
	}

	//void AppendItem(MyListItemData* pT)
	//{
	//	//append ; to item name
	//	pT->StringTitle += L";";
	//	pT->lEndPos = pT->StringTitle.Length()-1;
	//	if ( m_lDynamicArrayDataCount ){
	//		MyListItemData* pTempLast = CDynamicArray<MyListItemData>::GetItem(m_lDynamicArrayDataCount-1);
	//		if ( pTempLast ){
	//			//calc last item pos range
	//			pT->lBeginPos += (pTempLast->lEndPos+1);
	//			pT->lEndPos += (pTempLast->lEndPos+1);							
	//		}
	//	}else{
	//		pT->lBeginPos += m_lInitPos;
	//		pT->lEndPos += m_lInitPos;
	//	}
	//	CDynamicArray<MyListItemData>::AppendItem(pT);
	//}
	//void DeleteItemByCursorPos(long lCursorPos, long* plWillCursorPos)
	//{
	//	//find item index by pos
	//	long lIndex = FindItemIndexByPos(lCursorPos, plWillCursorPos);
	//	if ( Invalid_4Byte != lIndex ){
	//		DeleteItem(lIndex);
	//		UpdateItemPosRangeFromIdx(lIndex);
	//	}			
	//}
	//void InsertItemByPos(MyListItemData* pT, long lPos)
	//{
	//	//find item index by pos
	//	long lIndex = FindItemIndexByPos(lPos);
	//	if ( lIndex != Invalid_4Byte ){			
	//		pT->StringTitle += L";";
	//		InsertItem(lIndex, pT);
	//		UpdateItemPosRangeFromIdx(lIndex);
	//	}else{
	//		AppendItem(pT);
	//	}
	//	
	//}
	//long FindItemIndexByPos(long lPos, long* plWillCursorPos = NULL)
	//{
	//	for ( int i = 0; i < m_lDynamicArrayDataCount; i++ )
	//	{
	//		if ( (m_pDynamicArrayData[i].lBeginPos <= lPos) && 
	//			(m_pDynamicArrayData[i].lEndPos >= lPos) ){
	//				if ( plWillCursorPos ){
	//					if ( i >= 1 ){
	//						*plWillCursorPos = m_pDynamicArrayData[i-1].lEndPos+1;
	//					}						
	//				}
	//				return i;
	//		}
	//	}
	//	return Invalid_4Byte;
	//}
	//void FindWillPos(long lCurPos, long& lWillPos)
	//{
	//	long lIndex = FindItemIndexByPos(lCurPos);
	//	if ( Invalid_4Byte != lIndex ){
	//		//if ( Invalid_4Byte != m_pDynamicArrayData[lIndex].lPID ){
	//			lWillPos = m_pDynamicArrayData[lIndex].lEndPos+1;
	//		//}
	//	}
	//	
	//	return;
	//}
	//void UpdateItemPosRangeFromIdx(long lIndex)
	//{
	//	MyListItemData* pTemp = GetItem(lIndex-1);
	//	for ( int i = 0; i < (m_lDynamicArrayDataCount-lIndex); i++ )
	//	{
	//		if ( pTemp ){
	//			m_pDynamicArrayData[lIndex+i].lBeginPos = pTemp->lEndPos+1;
	//		}else{
	//			m_pDynamicArrayData[lIndex+i].lBeginPos = 0;
	//		}			
	//		m_pDynamicArrayData[lIndex+i].lEndPos = 
	//			m_pDynamicArrayData[lIndex+i].lBeginPos+m_pDynamicArrayData[lIndex+i].StringTitle.Length()-1;
	//		pTemp = &m_pDynamicArrayData[lIndex+i];
	//	}

	//}

	MyListItemData* FindItemByPID(long lPID)
	{
		for( int i = 0; i < m_lDynamicArrayDataCount; i++ )
		{
			if ( lPID == m_pDynamicArrayData[i].lPID ){
				return &m_pDynamicArrayData[i];
			}
		}
		return NULL;
	}

private:
	//long m_lInitPos;
};

#endif // __RecieversDynamicArray_h__