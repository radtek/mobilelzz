#include"stdafx.h"
#include <mzfc_inc.h>
#include"UiEditControl.h"
#include"NewSmsWnd.h"
#include"ContactorsWnd.h"


void UiEditControl::OnFocused (UiWin *pWinPrev) 
{

}
 
CContactorsWnd clContactorsWnd;
//zds 2010/03/21 19:39
int UiEditControl::OnLButtonUp  ( UINT  fwKeys,  int  xPos,  int  yPos )
{
//	MzCloseSip();

	int i = MZ_ANIMTYPE_SCROLL_BOTTOM_TO_TOP_2;
	m_lFlag = 1;
	
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
	ReleaseCapture();
	
	
	//((CNewSmsWnd*)m_pParent)->UpdateData(pRecivers, lReciversCount);
}
