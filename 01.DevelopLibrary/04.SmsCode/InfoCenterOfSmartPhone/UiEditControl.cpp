#include"stdafx.h"
#include <mzfc_inc.h>
#include"UiEditControl.h"
#include"NewSmsWnd.h"
#include"ContactorsWnd.h"


void UiEditControl::OnFocused (UiWin *pWinPrev) 
{

}
 

//zds 2010/03/21 19:39
int UiEditControl::OnLButtonUp  ( UINT  fwKeys,  int  xPos,  int  yPos )
{
//	MzCloseSip();

	RECT rc = {0};
	int height = 0;
	int width = 0;
	HWND hWnd = FindWindow(L"CTaskBar", 0);
	if(hWnd != 0)
	{
		::GetWindowRect(hWnd, &rc);
		height = rc.bottom - rc.top;
		width = rc.right - rc.left;
	}

	//横屏
	if(width>480)
	{
		if( (xPos <= (GetWidth() -150) && xPos >= 2) &&( yPos <= 65 && yPos >=0) )	
		{
			int x =0;

		}
		else
		{
			return 0;
		}
	}

	else
	{
		if((xPos <= (GetWidth() -150) && xPos >= 2) &&( yPos <= 65 && yPos >=0))
		{
			int x =0;
		}
		else
		{
			return 0;
		}
	}
	

	int i = MZ_ANIMTYPE_SCROLL_BOTTOM_TO_TOP_2;
	m_lFlag = 1;
	CContactorsWnd clContactorsWnd;
	clContactorsWnd.SetParent(this);
	RECT rcWork = MzGetWorkArea();
	clContactorsWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, WS_POPUP);
	// 设置窗口切换动画（弹出时的动画）
	//clContactorsWnd.SetAnimateType_Show(i);
	// 设置窗口切换动画（结束时的动画）
	//clContactorsWnd.SetAnimateType_Hide(i+1);
	int nRet = clContactorsWnd.DoModal();
	int b = 2;

	return 0;

}
//zds 2010/03/21 19:39  


void UiEditControl::SetParent(void* pParent)
{
	m_pParent = pParent;
}

void UiEditControl::UpdateData( long lFlag )
{
	if(lFlag == 0)
	{
		//m_lFlag = 0;
	}
	else
	{
		long lReciversCount = g_ReciversList.GetItemCount();
		wchar_t wcsReciversName[512] = L"";
		wcscat(wcsReciversName, L"收件人:" );
		for(int i  = 0; i < lReciversCount; i++)
		{
			wcscat(wcsReciversName, g_ReciversList.GetItem(i)->StringTitle );
			wcscat(wcsReciversName, L";" );
		}
		SetText(wcsReciversName);
		//Update();
	}
//	ReleaseCapture();
	
	
	//((CNewSmsWnd*)m_pParent)->UpdateData(pRecivers, lReciversCount);
}
