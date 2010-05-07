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

void UiEditControl::OnFocused (UiWin *pWinPrev) 
{

}
 

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
	return UiSingleLineEdit::OnLButtonUp(fwKeys, xPos, yPos);
}


void UiEditControl::SetParent(void* pParent)
{
	m_pParent = pParent;
}

void UiEditControl::UpdateData( long lFlag )
{
	if(lFlag == 0)
	{
	}
	else
	{
		UpdateTextByRecievers();		
	}
	ReleaseCapture();
	
	
}

void UiEditControl::UpdateTextByRecievers()
{
	long lReciversCount = g_ReciversList.GetItemCount();
	wchar_t wcsReciversName[512] = L"";
	wcscat(wcsReciversName, L" ’º˛»À:" );
	for(int i  = 0; i < lReciversCount; i++)
	{
		wcscat(wcsReciversName, g_ReciversList.GetItem(i)->StringTitle );
		//wcscat(wcsReciversName, L";" );
	}
	SetText(wcsReciversName);
}

int UiEditControl::OnKeyDown (int nVirtKey, DWORD lKeyData)
{
	if ( 8 ==  nVirtKey){//delete button down
		int b = 0;
		// get cur pos
		long lCursorPos = GetCursePos();
		// delete item from recievers by pos
		g_ReciversList.DeleteItemByCursorPos(lCursorPos);
		//update edit
		UpdateTextByRecievers();
	}else if ( 3 == nVirtKey ){//';' button down
		int b = 0;
		//get numbers until before ;
		long lPos = 0;	//make pos

		MyListItemData stTemp;	//make item to insert

		//insert reciever in the pos
		g_ReciversList.InsertItemByPos(&stTemp, lPos);
	}

	return 0;
}
