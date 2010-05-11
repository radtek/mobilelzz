#ifndef __DynamicArray_h__
#define __DynamicArray_h__

#define DEFAULT_Recivers_List_MemoryBlock		20
enum Move_Direction{
	Move_Direction_Forward,
	Move_Direction_Backward
};

template<class T>
class CDynamicArray
{
public:
	CDynamicArray()
	{
		bOwn = TRUE;
		m_pDynamicArrayData = NULL;
		m_lDynamicArrayDataCount = 0;
		m_lDynamicArrayDataMemoryCount = 0;
	}
	CDynamicArray(T* pTArray, long lCount)
	{
		bOwn = TRUE;
		m_pDynamicArrayData = pTArray;
		m_lDynamicArrayDataCount = lCount;
		m_lDynamicArrayDataMemoryCount = lCount;
	}
	virtual ~CDynamicArray()
	{
		if ( bOwn ){
			delete[] m_pDynamicArrayData;
			m_pDynamicArrayData = NULL;
			m_lDynamicArrayDataCount = 0;
			m_lDynamicArrayDataMemoryCount = 0;
		}		
	}

	long GetItemCount()
	{
		return m_lDynamicArrayDataCount;
	}
	void GetItem(long lIndex, T** ppT)
	{
		if(lIndex < 0 || lIndex >= m_lDynamicArrayDataCount)
		{
			*ppT = NULL;
		}
		else
		{
			*ppT = &m_pDynamicArrayData[lIndex];
		}
		return;
	}
	T* GetItem(long lIndex)
	{
		T* pReturnVal = NULL;
		if(lIndex < 0 || lIndex >= m_lDynamicArrayDataCount)
		{
		}
		else
		{
			pReturnVal = &m_pDynamicArrayData[lIndex];
		}
		return pReturnVal;
	}
	void AppendItem(T* pT)
	{
		if(m_lDynamicArrayDataCount < m_lDynamicArrayDataMemoryCount)
		{
			m_pDynamicArrayData[m_lDynamicArrayDataCount] = *pT;
			m_lDynamicArrayDataCount++;
		}
		else
		{
			if(m_pDynamicArrayData)
			{
				T* pTemp = new T[m_lDynamicArrayDataMemoryCount+DEFAULT_Recivers_List_MemoryBlock];
				if(pTemp)
				{
					for(int i = 0; i < m_lDynamicArrayDataCount; i++)
					{
						pTemp[i] = m_pDynamicArrayData[i];
					}
					delete[] m_pDynamicArrayData;
					m_pDynamicArrayData = pTemp;
					m_lDynamicArrayDataMemoryCount += DEFAULT_Recivers_List_MemoryBlock;
					m_pDynamicArrayData[m_lDynamicArrayDataCount] = *pT;
					m_lDynamicArrayDataCount++;
				}
			}
			else
			{
				m_pDynamicArrayData = new T[DEFAULT_Recivers_List_MemoryBlock];
				if(m_pDynamicArrayData)
				{
					m_lDynamicArrayDataMemoryCount = DEFAULT_Recivers_List_MemoryBlock;
					m_pDynamicArrayData[m_lDynamicArrayDataCount] = *pT;
					m_lDynamicArrayDataCount++;
				}
			}
		}
	}
	void DeleteItem(long lIndex)
	{
		//copy items move one pos before
		if ( lIndex > 0 )
		{
			MoveItemsByStep((lIndex+1), (m_lDynamicArrayDataCount-(lIndex+1)), Move_Direction_Forward, 1);
		}
		//update item count
		m_lDynamicArrayDataCount -= 1;
	}
	void DeleteItem(T* pT)
	{
		long lIndex = (pT-m_pDynamicArrayData)/sizeof(pT);
		//copy items move one pos before
		DeleteItem(lIndex);
	}
	void InsertItem(long lIndex, T* pT)
	{
		//move last item one pos after
		AppendItem(&m_pDynamicArrayData[m_lDynamicArrayDataCount-1]);
		MoveItemsByStep(lIndex, (m_lDynamicArrayDataCount-lIndex-1), Move_Direction_Backward, 1);
		m_pDynamicArrayData[lIndex] = *pT;
	}
	void MoveItemsByStep(long lIndex, long lCount, Move_Direction enDirection, long lSteps)
	{
		long lBeginPos = 0;
		if ( Move_Direction_Backward == enDirection ){
			if ( (lIndex + lCount + lSteps) > m_lDynamicArrayDataCount ){
				return;
			}
			lBeginPos = lIndex+lCount-1;
			for (int i = 0; i < lCount; i++ )
			{
				m_pDynamicArrayData[lBeginPos+lSteps] = m_pDynamicArrayData[lBeginPos];
				lBeginPos--;
			}
		}else{
			if ( ((lIndex - lSteps) < 0) || (lIndex > (m_lDynamicArrayDataCount-1)) ){
				return;
			}
			lBeginPos = lIndex;
			for (int i = 0; i < lCount; i++ )
			{
				m_pDynamicArrayData[lBeginPos-lSteps] = m_pDynamicArrayData[lBeginPos];
				lBeginPos++;
			}
		}

	}
	void Clear()
	{
		//delete[] m_pDynamicArrayData;
		m_lDynamicArrayDataCount = 0;
		//m_lDynamicArrayDataMemoryCount = 0;
	}
	T* Detatch()
	{
		bOwn = FALSE;
		return m_pDynamicArrayData;
	}
protected:
	T* m_pDynamicArrayData;
	long m_lDynamicArrayDataCount;
	long m_lDynamicArrayDataMemoryCount;
	
	BOOL bOwn;
};

#endif // __DynamicArray_h__