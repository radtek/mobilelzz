#include"stdafx.h"
#include "resource.h"

#include "SmsLookCtorWnd.h"
#include "SmsLookMsgWnd.h"

class ListItemData
{
	public:
		CMzString	StringTitle;			// 项的主文本
		CMzString	StringDescription;		// 项的描述文本
		BOOL		Selected;				// 项是否被选中

};


/////////////////////CSmsLookCtorList/////////////////////////////////////////////////////

CSmsLookCtorList::CSmsLookCtorList()
{

}

CSmsLookCtorList::~CSmsLookCtorList()
{
	RemoveAll();
}

void CSmsLookCtorList::OnRemoveItem(int nIndex)
{
	ListItem*	pItem	=	GetItem(nIndex);
	if( NULL != pItem )
	{
		MyListItemData*	mlid	=	(MyListItemData*)pItem->Data;
		if( NULL != mlid )
		{
			delete mlid; 
		}	
	}
}

int CSmsLookCtorList::OnLButtonUp( UINT fwKeys, int xPos, int yPos )
{
	int		iRlt	=	UiList::OnLButtonUp( fwKeys, xPos, yPos );

	bool	b1		=	IsMouseDownAtScrolling();
	bool	b2		=	IsMouseMoved();

	if( ( !b1 ) && ( !b2 ) )
	{
		// 计算鼠标所在位置的项的索引
		int	nIndex	=	CalcIndexOfPos( xPos, yPos );
		if( 1 == nIndex )
		{
			CSmsLookMsgWnd	clCSmsLookMsgWnd;
			RECT rcWork	=	MzGetWorkArea();
			clCSmsLookMsgWnd.Create(	rcWork.left, rcWork.top, RECT_WIDTH(rcWork), 
										RECT_HEIGHT(rcWork), 0, 0, 0 );
			clCSmsLookMsgWnd.DoModal();
		}
	}

	return	iRlt;
}

void CSmsLookCtorList::DrawItem(HDC hdcDst, int nIndex, RECT* prcItem, RECT *prcWin, RECT *prcUpdate)
{
	ListItem*	pItem	=	GetItem( nIndex );
	if ( 0 == pItem )
	{
		return;
	}
		
	if ( 0 == pItem->Data )
	{
		return;
	}
	
	ListItemData	*	pmlid	=	(ListItemData*)pItem->Data;

	// 绘制左边的小图像
	ImagingHelper* pimg	=	m_ImageContainer.LoadImage( MzGetInstanceHandle(), IDR_RCDATA_N_V, true );
	RECT rcImg			=	*prcItem;
	rcImg.right			=	rcImg.left + MZM_MARGIN_MAX * 2;
	if ( NULL != pimg )
	{
		pimg->Draw( hdcDst, &rcImg, false, false );
	}
	// 绘制主文本
	RECT rcText		=	*prcItem;
	rcText.left		=	rcImg.right + 20;
	rcText.bottom	=	rcText.top + RECT_HEIGHT( rcText ) / 2;
	//::SetTextColor(hdcDst, RGB(0,200,0));
	::SetTextColor( hdcDst, RGB( 0, 0, 0 ) );

	MzDrawText( hdcDst, pmlid->StringTitle.C_Str(), &rcText, 
				DT_LEFT|DT_BOTTOM|DT_SINGLELINE|DT_END_ELLIPSIS );

	// 绘制描述文本
	rcText.top		=	rcText.bottom;
	rcText.bottom	=	prcItem->bottom;
	::SetTextColor( hdcDst, RGB( 200, 200, 200 ) );
	MzDrawText( hdcDst, pmlid->StringDescription.C_Str(), &rcText, 
				DT_LEFT|DT_TOP|DT_SINGLELINE|DT_END_ELLIPSIS );
}

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsLookCtorWnd::CSmsLookCtorWnd(void)
{

}

CSmsLookCtorWnd::~CSmsLookCtorWnd(void)
{

}

BOOL CSmsLookCtorWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}
	//ini list
	m_List.SetID( MZ_IDC_SMSLOOKCTOR_LIST );
	m_List.EnableScrollBarV( true );
	m_List.EnableNotifyMessage( true );
	m_List.SetTextColor( RGB( 255,0,0 ) );
	m_List.SetPos(0, 0, GetWidth(), GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR );
	m_List.SetItemHeight( 90 );
	AddUiWin( &m_List );



	//ini alp
	m_AlpBar.SetID( MZ_IDC_SMSLOOKCTOR_ALPBAR );
	m_AlpBar.EnableZoomAlphabet( true );
	m_AlpBar.EnableNotifyMessage( true );
	m_AlpBar.SetPos( 350, 0, 50, GetHeight() );
	AddUiWin( &m_AlpBar );

	//ini toolbar
	m_Toolbar.SetButton( 2, true, true, L"取消" );
	m_Toolbar.SetID( MZ_IDC_SMSLOOKCTOR_TOOLBAR );
	m_Toolbar.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR, GetWidth(), MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_Toolbar );


//test
	{
		ListItem li;
		ListItemData *pmlid = new ListItemData;
		pmlid->StringTitle			= L"Title1";
		pmlid->StringDescription	= L"Description1";
		pmlid->Selected = false;

		li.Data = pmlid;
		m_List.AddItem(li);
	}
	
	{
		ListItem li;
		ListItemData *pmlid = new ListItemData;
		pmlid->StringTitle			= L"Title2";
		pmlid->StringDescription	= L"Description2";
		pmlid->Selected = false;

		li.Data = pmlid;
		m_List.AddItem(li);
	}


//

	return	TRUE;
}

void CSmsLookCtorWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD(wParam);

	switch(id)
	{
		case MZ_IDC_SMSLOOKCTOR_TOOLBAR:
		{
			int nIndex	=	lParam;
			if ( nIndex == 2 )
			{
				this->EndModal( ID_CANCEL );
			}
			break;
		}

		default:
			break;
	}
}
