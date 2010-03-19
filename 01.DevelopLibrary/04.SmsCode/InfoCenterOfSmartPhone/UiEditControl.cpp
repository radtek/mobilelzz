#include"stdafx.h"
#include <mzfc_inc.h>
#include"UiEditControl.h"
#include"NewSmsWnd.h"
#include"ContactorsWnd.h"

void UiEditControl::OnFocused (UiWin *pWinPrev) 
{
	if(m_lFlag == 0)
	{
		int i = MZ_ANIMTYPE_SCROLL_BOTTOM_TO_TOP_2;
	
		CContactorsWnd clContactorsWnd;
		clContactorsWnd.SetParent(this);
		RECT rcWork = MzGetWorkArea();
		clContactorsWnd.Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, WS_POPUP);
		// 设置窗口切换动画（弹出时的动画）
		clContactorsWnd.SetAnimateType_Show(i);
		// 设置窗口切换动画（结束时的动画）
		clContactorsWnd.SetAnimateType_Hide(i+1);
		int nRet = clContactorsWnd.DoModal();
		int b = 2;
		m_lFlag = 1;
	}
	else{
		m_lFlag = 0;
	}
}

void UiEditControl::SetParent(void* pParent)
{
	m_pParent = pParent;
}

void UiEditControl::UpdateData( MyListItemData* pRecivers,long lReciversCount )
{
	m_pRecivers = pRecivers;
	m_lReciversCount = lReciversCount;
	wchar_t wcsReciversName[512] = L"";
	wcscat(wcsReciversName, L"收件人:" );
	for(int i  = 0; i < lReciversCount; i++)
	{
		wcscat(wcsReciversName, m_pRecivers[i].StringTitle );
		wcscat(wcsReciversName, L";" );
	}
	SetText(wcsReciversName);
	Update();
	((CNewSmsWnd*)m_pParent)->UpdateData(pRecivers, lReciversCount);
}
