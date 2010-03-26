// stdafx.h : ��׼ϵͳ�����ļ��İ����ļ���
// ���Ǿ���ʹ�õ��������ĵ�
// �ض�����Ŀ�İ����ļ�
//

#include<wtypes.h>
#include <mzfc_inc.h>
#include <acc_api.h>
#include <ShellNotifyMsg.h>

//zhu.t add for License	at 2010-3-22
#include "MyStoreLib.h"
#pragma comment(lib, "MyStoreLib.lib")
#pragma comment(lib, "PlatformAPI.lib")
#pragma comment(lib, "PhoneAdapter.lib")
//end
#define	LICENSE

extern BOOL g_bH;

extern  BOOL  g_bContactShow;

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

#define DEFAULT_Recivers_List_MemoryBlock		20

class CReciversList
{
public:
	CReciversList()
	{
		m_pReciversData = NULL;
		m_lReciversDataCount = 0;
		m_lReciversDataMemoryCount = 0;
	}
	virtual ~CReciversList()
	{
		delete[] m_pReciversData;
		m_pReciversData = NULL;
		m_lReciversDataCount = 0;
		m_lReciversDataMemoryCount = 0;
	}

	long GetItemCount()
	{
		return m_lReciversDataCount;
	}
	void GetItem(long lIndex, MyListItemData** ppMyListItemData)
	{
		if(lIndex < 0 || lIndex >= m_lReciversDataCount)
		{
			*ppMyListItemData = NULL;
		}
		else
		{
			*ppMyListItemData = &m_pReciversData[lIndex];
		}
		return;
	}
	MyListItemData* GetItem(long lIndex)
	{
		MyListItemData* pReturnVal = NULL;
		if(lIndex < 0 || lIndex >= m_lReciversDataCount)
		{
		}
		else
		{
			pReturnVal = &m_pReciversData[lIndex];
		}
		return pReturnVal;
	}
	void AppendItem(MyListItemData* pMyListItemData)
	{
		if(m_lReciversDataCount < m_lReciversDataMemoryCount)
		{
			m_pReciversData[m_lReciversDataCount] = *pMyListItemData;
			m_lReciversDataCount++;
		}
		else
		{
			if(m_pReciversData)
			{
				MyListItemData* pTemp = new MyListItemData[m_lReciversDataMemoryCount+DEFAULT_Recivers_List_MemoryBlock];
				if(pTemp)
				{
					for(int i = 0; i < m_lReciversDataCount; i++)
					{
						pTemp[i] = m_pReciversData[i];
					}
					delete[] m_pReciversData;
					m_pReciversData = pTemp;
					m_lReciversDataMemoryCount += DEFAULT_Recivers_List_MemoryBlock;
					m_pReciversData[m_lReciversDataCount] = *pMyListItemData;
					m_lReciversDataCount++;
				}
			}
			else
			{
				m_pReciversData = new MyListItemData[DEFAULT_Recivers_List_MemoryBlock];
				if(m_pReciversData)
				{
					m_lReciversDataMemoryCount = DEFAULT_Recivers_List_MemoryBlock;
					m_pReciversData[m_lReciversDataCount] = *pMyListItemData;
					m_lReciversDataCount++;
				}
			}
		}
	}
	void Clear()
	{
		//delete[] m_pReciversData;
		m_lReciversDataCount = 0;
		//m_lReciversDataMemoryCount = 0;
	}
private:
	MyListItemData* m_pReciversData;
	long m_lReciversDataCount;
	long m_lReciversDataMemoryCount;

};
extern CReciversList g_ReciversList;

bool	LicenseProtect();
// TODO: �ڴ˴����ó�����Ҫ������ͷ�ļ�
