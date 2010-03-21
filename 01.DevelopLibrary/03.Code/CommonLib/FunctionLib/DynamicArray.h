#ifndef __DynamicArray_h__
#define __DynamicArray_h__

#define DEFAULT_Recivers_List_MemoryBlock		20
template<class T>
class CDynamicArray
{
public:
	CDynamicArray()
	{
		m_pDynamicArrayData = NULL;
		m_lDynamicArrayDataCount = 0;
		m_lDynamicArrayDataMemoryCount = 0;
	}
	virtual ~CDynamicArray()
	{
		delete[] m_pDynamicArrayData;
		m_pDynamicArrayData = NULL;
		m_lDynamicArrayDataCount = 0;
		m_lDynamicArrayDataMemoryCount = 0;
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
	void Clear()
	{
		//delete[] m_pDynamicArrayData;
		m_lDynamicArrayDataCount = 0;
		//m_lDynamicArrayDataMemoryCount = 0;
	}
private:
	T* m_pDynamicArrayData;
	long m_lDynamicArrayDataCount;
	long m_lDynamicArrayDataMemoryCount;

};

#endif // __DynamicArray_h__