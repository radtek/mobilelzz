#include"stdafx.h"
#include <mzfc_inc.h>

#include"UiEditControl.h"
//#include"NewSmsWnd.h"
#include"ContactorsWnd.h"
#include "RecieversStringParser.h"

UiEditControl::UiEditControl()
{
}

UiEditControl::~UiEditControl()
{
	//if(NULL != m_pclContactorsWnd )
	//{
	//	delete m_pclContactorsWnd ;
	//	m_pclContactorsWnd  = NULL;	
	//}

}

//void UiEditControl::OnFocused (UiWin *pWinPrev) 
//{
//
//}
// 

//zds 2010/03/21 19:39
int UiEditControl::OnLButtonUp123  ( UINT  fwKeys,  int  xPos,  int  yPos )
{
	//RECT EditRc = GetClientRect();
	//if( (yPos <= EditRc.bottom && yPos >= EditRc.top) &&( xPos <= EditRc.right && xPos >=EditRc.left) )	
	//{
	//	int i = MZ_ANIMTYPE_SCROLL_BOTTOM_TO_TOP_2;
	//	m_lFlag = 1;
	//	
	//	if(NULL != m_pclContactorsWnd )
	//	{
	//		delete m_pclContactorsWnd ;
	//		m_pclContactorsWnd  = NULL;
	//	}

	//	m_pclContactorsWnd = new CContactorsWnd;
	//	m_pclContactorsWnd->SetParent(this);
	//	RECT rcWork = MzGetWorkArea();
	//	m_pclContactorsWnd->Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0, 0);

	//	m_pclContactorsWnd->Show();		
	//	g_bContactShow = TRUE;

	//}

	return 0;

}
//zds 2010/03/21 19:39  

int UiEditControl::OnLButtonUp  ( UINT  fwKeys,  
				  int  xPos,  
				  int  yPos   
				  )
{
	SetSipMode(IM_SIP_MODE_DIGIT,0);
	long lRowCount = GetRowCount();
	UpdateFontColor(RGB(0,0,0),0,0,lRowCount,0);
	int r = UiEdit::OnLButtonUp(fwKeys, xPos, yPos);
	long lCurRow = Invalid_4Byte;
	long lCurCol = Invalid_4Byte;
	GetCaretPos((size_t*)&lCurRow, (size_t*)&lCurCol);
	if ( (Invalid_4Byte!=lCurRow)&&(Invalid_4Byte!=lCurCol) ){
		long lPos = Invalid_4Byte;
		ConvertRowCol2LinePos(lCurRow, lCurCol, lPos);
		CRecieversStringParser clParser;
		CMzString& clText = GetText();
		long lWSize = 0;
		wchar_t* pwcsTextBuf = clParser.GetWStringBuf(lWSize);
		F_wcscpyn(pwcsTextBuf, clText.C_Str(), lWSize);
		long lBeginPos = Invalid_4Byte;
		long lEndPos = Invalid_4Byte;
		
		clParser.GetContactorRangeByPos(lPos, lBeginPos, lEndPos);
		BOOL b = clParser.IsContactor(lBeginPos, lEndPos);
		if ( !b ){
			lBeginPos = Invalid_4Byte;
			lEndPos = Invalid_4Byte;
		}
		if( (lBeginPos!=Invalid_4Byte)&&(lEndPos!=Invalid_4Byte) ){
			long lRowBegin = 0;
			long lRowEnd = 0;
			long lColBegin = 0;
			long lColEnd = 0;
			ConvertLinePos2RowCol(lBeginPos, lRowBegin, lColBegin);
			ConvertLinePos2RowCol(lEndPos, lRowEnd, lColEnd);

			UpdateFontColor(RGB(167,137,63), lRowBegin, lColBegin, lRowEnd, lColEnd);
			SetCaretPos(lRowEnd, (lColEnd+1));
			Invalidate();
			Update();
		}
	}

	return r;
}

void UiEditControl::UpdateData( long lFlag )
{
	CRecieversStringParser clParser;
	CMzString& clText = GetText();
	long lWSize = 0;
	wchar_t* pwcsTextBuf = clParser.GetWStringBuf(lWSize);
	F_wcscpyn(pwcsTextBuf, clText.C_Str(), lWSize);	
	clParser.UpdateStringByContactors();
	SetText(clParser.GetWStringBuf(lWSize));
	Invalidate();
	Update();
}

void UiEditControl::UpdateTextByRecievers(BOOL bIsAddChar, long lWillPos)
{
	long lReciversCount = g_ReciversList.GetItemCount();
	wchar_t wcsReciversName[512] = L"";
	//wcscat(wcsReciversName, L" ’º˛»À:" );
	for(int i  = 0; i < lReciversCount; i++)
	{
		wcscat(wcsReciversName, g_ReciversList.GetItem(i)->StringTitle );
		if ( bIsAddChar ){
			if ( lWillPos == (g_ReciversList.GetItem(i)->lEndPos+1) ){
				wcscat(wcsReciversName, L";" );
			}			
		}
	}
 	
	SetText(wcsReciversName);
	Invalidate();
	Update();
}

//void UiEditControl::OnClick( size_t  nIndex )
//{
//	UiSingleLineEdit::OnClick(nIndex);
//	long lCursorPos = GetCursePos();
//	int b  =0;
//	return;
//}

//int UiEditControl::OnChar( TCHAR  chCharCode, LPARAM  lKeyData )
//{
//	CMzString& wcsControlText = GetText();
//	int b  =0;
//	return 0;
//}

int UiEditControl::OnKeyDown(int nVirtKey, DWORD lKeyData)
{
	int r = UiEdit::OnKeyDown(nVirtKey, lKeyData);
	long lCurRow = Invalid_4Byte;
	long lCurCol = Invalid_4Byte;
	long lPos = Invalid_4Byte;
	GetCaretPos((size_t*)&lCurRow, (size_t*)&lCurCol);
	if ( (Invalid_4Byte!=lCurRow)&&(Invalid_4Byte!=lCurCol) ){	
		ConvertRowCol2LinePos(lCurRow, lCurCol, lPos);
	}
	CRecieversStringParser clParser;
	CMzString& clText = GetText();
	long lWSize = 0;
	wchar_t* pwcsTextBuf = clParser.GetWStringBuf(lWSize);
	F_wcscpyn(pwcsTextBuf, clText.C_Str(), lWSize);	
	long lWillPos = 0;
	if ( (8 ==  nVirtKey) && ( lKeyData == 0 )){//delete button down
		lWillPos = clParser.DeleteContentByPos(lPos);
		ConvertLinePos2RowCol(lWillPos, lCurRow, lCurCol);
	}else if ( (48 <= nVirtKey)&&(57>=nVirtKey)){//numbers button down
		clParser.AddSeparator(lPos);
		
	}else{
		//cancel input
	}
	SetText(clParser.GetWStringBuf(lWSize));
	SetCaretPos(lCurRow, lCurCol);
	Invalidate();
	Update();

	return r;
}

void UiEditControl::ConvertLinePos2RowCol(long lLinePos, long& lRow, long& lCol)
{
	long lRowCount = GetRowCount();
	long lTotalCharCount = 0;
	for ( int i  = 0; i < lRowCount; i++ )
	{
		if ( (i+1) < (lRowCount-1) ){
			long lCurRowCharCount = GetCharCount(i,0,(i+1),0);
			long lTemp = lTotalCharCount+lCurRowCharCount;
			if ( lLinePos < (lTemp) ){
				lRow = i;
				lCol = lLinePos-lTotalCharCount;
				break;
			}
			lTotalCharCount=lTemp;
		}
		else{
			lRow = i;
			lCol = lLinePos-lTotalCharCount;
			break;
		}
	}

}

void UiEditControl::ConvertRowCol2LinePos(long lRow, long lCol, long& lLinePos)
{
	lLinePos = GetCharCount(0,0,lRow,lCol);
}

void UiEditControl::UpdateRecievers()
{
	CRecieversStringParser clParser;
	CMzString& clText = GetText();
	long lWSize = 0;
	wchar_t* pwcsTextBuf = clParser.GetWStringBuf(lWSize);
	F_wcscpyn(pwcsTextBuf, clText.C_Str(), lWSize);	
	clParser.UpdateRecievers();
}

void UiEditControl::UpdateContactors()
{
	CRecieversStringParser clParser;
	CMzString& clText = GetText();
	long lWSize = 0;
	wchar_t* pwcsTextBuf = clParser.GetWStringBuf(lWSize);
	F_wcscpyn(pwcsTextBuf, clText.C_Str(), lWSize);	
	clParser.UpdateContactors();
}