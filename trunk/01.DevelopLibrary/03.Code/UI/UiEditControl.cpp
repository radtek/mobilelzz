#include"stdafx.h"
#include <mzfc_inc.h>

#include"UiEditControl.h"
//#include"NewSmsWnd.h"
#include"ContactorsWnd.h"

UiEditControl::UiEditControl()
{
	m_lFlag = 0;
	m_pParent = NULL;
//	m_pclContactorsWnd  = NULL;	
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
	MzOpenSip();
	int r = UiSingleLineEdit::OnLButtonUp(fwKeys, xPos, yPos);
	long lCursorPos = GetCursePos();
	long lWillPos = lCursorPos;
	g_ReciversList.FindWillPos(lCursorPos, lWillPos);
	if ( lWillPos >= 0 ){
		SetCursePos(lWillPos);
	}else{
		CMzString& clTempStr = GetText();
		long lLength = clTempStr.Length();
		SetCursePos(lLength);
	}

	Update();
	return r;
}


void UiEditControl::SetParent(void* pParent)
{
	m_pParent = pParent;
}

void UiEditControl::UpdateData( long lFlag )
{
	//if(lFlag == 0)
	//{
	//}
	//else
	//{
		UpdateTextByRecievers();		
	//}
	//ReleaseCapture();
	
	
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
	
//	if ( Invalid_4Byte != lWillPos ){
		//SetCursePos(0);
//	}
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
	int r = UiSingleLineEdit::OnKeyDown(nVirtKey, lKeyData);

	if ( (8 ==  nVirtKey) && ( lKeyData == 0 )){//delete button down
		int b = 0;
		// get cur pos
		long lCursorPos = GetCursePos();
		long lWillPos = -1;
		// delete item from recievers by pos
		if ( (-1 != lCursorPos) && (0 != lCursorPos) ){			
			g_ReciversList.DeleteItemByCursorPos((lCursorPos-1), &lWillPos);	
			UpdateTextByRecievers(TRUE, lWillPos);
		}				
		SetCursePos(lWillPos+1);
		//Update();
	}else if ( 3 == nVirtKey ){//';' button down
		int b = 0;
		//get numbers until before ;
		long lCursorPos = GetCursePos();
		if ( (-1 != lCursorPos) && (0 != lCursorPos) ){
			long lPos = 0;	//make pos
			CMzString& wcsControlText = GetText();
			wchar_t* pwcsControlText = wcsControlText.C_Str();
			long lCur = 0;
			while ( (L'\0' != (*pwcsControlText)) &&(pwcsControlText) && (lCur <= lCursorPos))
			{
				if ( L';' == (*pwcsControlText) ){
					lPos = lCur+1;
				}
				pwcsControlText++;
				lCur++;
			}
			wchar_t awcsTemp[30] = L"";
			pwcsControlText = wcsControlText.C_Str();
			F_wcscpyn(awcsTemp, pwcsControlText, sizeof(awcsTemp)/sizeof(awcsTemp[0]));
			wchar_t* pNumber = awcsTemp;
			pNumber[lCursorPos] = L'\0';
			pNumber += lPos;
			MyListItemData stTemp;	//make item to insert
			stTemp.StringDescription = pNumber;
			stTemp.StringTitle = pNumber;
			//insert reciever in the pos
			g_ReciversList.InsertItemByPos(&stTemp, lPos);
			//UpdateTextByRecievers();
		}		
	}

	return r;
}
