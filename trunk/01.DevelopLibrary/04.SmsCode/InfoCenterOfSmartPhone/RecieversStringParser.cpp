#include "stdafx.h"
#include "RecieversStringParser.h"

#define UI_RecieversString_Separator		(L';')
#define UI_RecieversString_SeparatorStr		(L";")

CRecieversStringParser::CRecieversStringParser()
{
	memset(m_wcsStringNeedParsed,0x0,sizeof(m_wcsStringNeedParsed));
	m_lWSize = sizeof(m_wcsStringNeedParsed)/sizeof(m_wcsStringNeedParsed[0]);
}

CRecieversStringParser::~CRecieversStringParser()
{


}

void CRecieversStringParser::UpdateStringByContactors()
{ 
	wchar_t* pCur = m_wcsStringNeedParsed;
	long lWCount = wcslen(m_wcsStringNeedParsed);
	wchar_t* pEnd = &m_wcsStringNeedParsed[lWCount];
	long lLoop = 0;
	wchar_t* pBegin = pCur;
	vector<long> vecExistIndex;
	while(pCur!=pEnd)
	{
		if ( UI_RecieversString_Separator == *pCur ){
			wchar_t wcsContactor[50] = L"";
			long lCount = pCur-pBegin;
			if ( lCount > 0 ){
				wcsncpy(wcsContactor, pBegin, lCount);
				long lMatchIndex = Invalid_4Byte;
				BOOL b = IsContactor(wcsContactor, lMatchIndex);
				if ( !b ){
					b = IsNumbers(wcsContactor);
					if ( !b ){
						long lCurPos = pCur-m_wcsStringNeedParsed+1;
						MoveChars(lCurPos, Move_Direction_Forward, (lCount+1));
						long lWCount = wcslen(m_wcsStringNeedParsed);
						m_wcsStringNeedParsed[lWCount-(lCount+1)] = L'\0';
						pEnd = &m_wcsStringNeedParsed[lWCount-(lCount+1)];
						pCur = pBegin-1;
					}else{
						pBegin = pCur+1;
					}
				}else{
					pBegin = pCur+1;
					vecExistIndex.push_back(lMatchIndex);
				}		
			}else{
				pBegin = pCur+1;
			}			
		}
		pCur++;
	}
	long lCount = g_ReciversList.GetItemCount();
	for ( int j = 0; j < lCount; j++ )
	{
		int i = 0;
		long lvecSize = vecExistIndex.size();
		for ( i = 0; i < lvecSize; i++ )
		{
			if ( j == vecExistIndex.at(i) ){
				break;
			}
		}
		if ( i == lvecSize ){
			F_wcscatn( m_wcsStringNeedParsed, g_ReciversList.GetItem(j)->StringTitle, m_lWSize );
			F_wcscatn( m_wcsStringNeedParsed, UI_RecieversString_SeparatorStr, m_lWSize );
		}
	}
	
}

void CRecieversStringParser::UpdateRecievers()
{ 
	wchar_t* pCur = m_wcsStringNeedParsed;
	long lWCount = wcslen(m_wcsStringNeedParsed);
	long lLoop = 0;
	wchar_t* pBegin = pCur;
	while(lLoop<lWCount)
	{
		if ( UI_RecieversString_Separator == *pCur ){
			wchar_t wcsContactor[50] = L"";
			long lCount = pCur-pBegin;
			if ( lCount > 0 ){
				wcsncpy(wcsContactor, pBegin, lCount);
				BOOL b = IsNumbers(wcsContactor);
				if ( b ){
					long lMatchIndex = Invalid_4Byte;
					b = IsContactor(wcsContactor, lMatchIndex);
					if ( !b ){
						MyListItemData Temp;
						Temp.StringTitle = wcsContactor;
						Temp.StringDescription = wcsContactor;
						g_ReciversList.AppendItem(&Temp);
					}					
				}	
			}
			pBegin = pCur+1;
		}
		pCur++;
		lLoop++;
	}
}

void CRecieversStringParser::UpdateContactors()
{ 
	wchar_t* pCur = m_wcsStringNeedParsed;
	long lWCount = wcslen(m_wcsStringNeedParsed);
	vector<long> vecExistIndex;
	long lLoop = 0;
	wchar_t* pBegin = pCur;
	while(lLoop<lWCount)
	{
		if ( UI_RecieversString_Separator == *pCur ){
			wchar_t wcsContactor[50] = L"";
			long lCount = pCur-pBegin;
			if ( lCount > 0 ){
				wcsncpy(wcsContactor, pBegin, lCount);
				long lMatchIndex = Invalid_4Byte;
				BOOL b = IsContactor(wcsContactor, lMatchIndex);
				if ( b ){
					vecExistIndex.push_back(lMatchIndex);
				}	
			}
			pBegin = pCur+1;
		}
		pCur++;
		lLoop++;
	}
	long lCount = g_ReciversList.GetItemCount();
	for ( int j = 0; j < lCount; j++ )
	{
		int i = 0;
		long lvecSize = vecExistIndex.size();
		for ( i = 0; i < lvecSize; i++ )
		{
			if ( j == vecExistIndex.at(i) ){
				break;
			}
		}
		if ( i == lvecSize ){
			g_ReciversList.DeleteItem(j);
		}
	}
}

void CRecieversStringParser::AddSeparator(long lCurcorPos)
{
	if ( lCurcorPos > 0 ){
		if ( UI_RecieversString_Separator == m_wcsStringNeedParsed[lCurcorPos-1] ){
			MoveChars(lCurcorPos, Move_Direction_Backward, 1);
			m_wcsStringNeedParsed[lCurcorPos] = UI_RecieversString_Separator;
		}
	}else{
		MoveChars(lCurcorPos, Move_Direction_Backward, 1);
		m_wcsStringNeedParsed[lCurcorPos] = UI_RecieversString_Separator;
	}
}

void CRecieversStringParser::GetContactorRangeByPos(long lCurcorPos, 
							long& lBeginPos, long& lEndPos)
{
	if (( L'\0' != m_wcsStringNeedParsed[0] ) && ( lCurcorPos <= (m_lWSize-1) )){
		long lWCount = wcslen(m_wcsStringNeedParsed);
		if ( lCurcorPos <= (lWCount-1) ){
			lBeginPos = FindBoundaryByPos(lCurcorPos, Move_Direction_Forward);
			lEndPos = FindBoundaryByPos(lCurcorPos, Move_Direction_Backward);
		}
	}
}

long CRecieversStringParser::DeleteContentByPos(long lCurcorPos)
{
	long lRtV = lCurcorPos;
	if ( lCurcorPos > 0 ){
		if ( UI_RecieversString_Separator == m_wcsStringNeedParsed[lCurcorPos-1] ){
			long lBeginPos = Invalid_4Byte;
			long lEndPos = Invalid_4Byte;
			GetContactorRangeByPos((lCurcorPos-1), lBeginPos, lEndPos);
			MoveChars((lEndPos+1), Move_Direction_Forward, (lEndPos-lBeginPos));
			long lLen = wcslen(m_wcsStringNeedParsed);
			m_wcsStringNeedParsed[lLen-(lEndPos-lBeginPos+1)+1] = L'\0';
			m_wcsStringNeedParsed[lBeginPos] = UI_RecieversString_Separator;
			lRtV = lBeginPos+1;
		}		
	}
	return lRtV;
}

wchar_t* CRecieversStringParser::GetWStringBuf(long& lWSize)
{
	lWSize = m_lWSize;
	return m_wcsStringNeedParsed;
}

long CRecieversStringParser::FindBoundaryByPos(long lCurcorPos, Move_Direction enDirection)
{
	long lRetVal = Invalid_4Byte;
	long lStepLength = 0;
	long lMaxStepCount = 0;
	
	if ( Move_Direction_Forward == enDirection ){
		lStepLength = -1;
		lMaxStepCount = (lCurcorPos+1)/abs(lStepLength);
		lCurcorPos -= 1;
	}else{
		lStepLength = 1;
		long lWCount = wcslen(m_wcsStringNeedParsed);
		lMaxStepCount = (lWCount - lCurcorPos)/lStepLength;
	}
	int i = 0;
	for ( i = 0; i < lMaxStepCount; i++ )
	{
		if ( UI_RecieversString_Separator == m_wcsStringNeedParsed[lCurcorPos + i*lStepLength] ){
			break;
		}		
	}
	if ( i == lMaxStepCount ){
		if (Move_Direction_Forward == enDirection){
			lRetVal = 0;
		}else{
			ASSERT(0);
		}
	}else{
		if (Move_Direction_Forward == enDirection){
			lRetVal = lCurcorPos + i*lStepLength+1;
		}else{
			lRetVal = lCurcorPos + i*lStepLength;
		}
	}

	return lRetVal;
}

void CRecieversStringParser::MoveChars(long lCurcorPos, Move_Direction enDirection, long lStepLength)
{
	if ( L'\0' != m_wcsStringNeedParsed[0] ){
		long lWCount = wcslen(m_wcsStringNeedParsed);		
		if ( lWCount > (lCurcorPos+1) ){
			long lCurPos = 0;
			long lNextPos = 0;
			long lDir = 0;
			if ( Move_Direction_Forward == enDirection ){
				lCurPos = lCurcorPos-lStepLength;
				lNextPos = lCurPos+lStepLength;
				lDir = 1;
			}else{
				lCurPos = lWCount-1+lStepLength;
				lNextPos = lCurPos-lStepLength;
				lDir = -1;
			}
			if ( (lCurPos>=0)&&(lCurPos<m_lWSize) ){
				for ( int i = 0; i < (lWCount-lCurcorPos); i++ )
				{
					m_wcsStringNeedParsed[lCurPos] = m_wcsStringNeedParsed[lNextPos];
					lCurPos+=lDir;
					lNextPos+=lDir;
				}
			}			
		}
	}
}

BOOL CRecieversStringParser::IsNumbers(long lBeginPos, long lEndPos)
{
	BOOL bRet = FALSE;
	if ( L'\0' != m_wcsStringNeedParsed[0] ){
		long lWCount = wcslen(m_wcsStringNeedParsed);
		if (( lBeginPos >= 0 )&&(lBeginPos<lWCount)&&
			( lEndPos >= 0 )&&(lEndPos<lWCount)){
			int i = 0;
			for ( i = 0; i < (lEndPos-lBeginPos+1); i++ )
			{
				if ( (L'0' >= m_wcsStringNeedParsed[lBeginPos+i]) || (L'9' <= m_wcsStringNeedParsed[lBeginPos+i]) ){
					break;
				}
			}		
			if ( i == (lEndPos-lBeginPos+1) ){
				bRet = TRUE;
			}
		}
	}
	
	return bRet;
}

BOOL CRecieversStringParser::IsNumbers(wchar_t* pwcsWChars)
{
	BOOL bRet = FALSE;
	int i = 0;
	long lCount = wcslen(pwcsWChars);
	for ( i = 0; i < lCount; i++ )
	{
		if ( (L'0' >= pwcsWChars[i]) || (L'9' <= pwcsWChars[i]) ){
			break;
		}
	}		
	if ( i == lCount ){
		bRet = TRUE;
	}

	return bRet;
}

BOOL CRecieversStringParser::IsContactor(long lBeginPos, long lEndPos)
{
	BOOL bRet = FALSE;
	if ( L'\0' != m_wcsStringNeedParsed[0] ){
		long lWCount = wcslen(m_wcsStringNeedParsed);
		if (( lBeginPos >= 0 )&&(lBeginPos<lWCount)&&
			( lEndPos >= 0 )&&(lEndPos<lWCount)){
			wchar_t wcsContactor[50] = L"";
			wcsncpy(wcsContactor, &m_wcsStringNeedParsed[lBeginPos], (lEndPos-lBeginPos));
			long lCount = g_ReciversList.GetItemCount();
			for ( int i = 0; i < lCount; i++ )
			{
				if ( 0 == wcscmp(wcsContactor, g_ReciversList.GetItem(i)->StringTitle.C_Str()) ){
					bRet = TRUE;
				}
			}
		}
	}

	return bRet;
}

BOOL CRecieversStringParser::IsContactor(wchar_t* pwcsContactor, long& lMatchIndex)
{
	BOOL bRet = FALSE;
	lMatchIndex = Invalid_4Byte;
	long lCount = g_ReciversList.GetItemCount();
	for ( int i = 0; i < lCount; i++ )
	{
		if ( 0 == wcscmp(pwcsContactor, g_ReciversList.GetItem(i)->StringTitle.C_Str()) ){
			lMatchIndex = i;
			bRet = TRUE;
		}
	}

	return bRet;
}