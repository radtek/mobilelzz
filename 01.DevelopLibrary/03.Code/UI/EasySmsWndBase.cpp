#include"stdafx.h"
#include "resource.h"
#include "EasySmsWndBase.h"
#include "EasySmsUiCtrl.h"


/////////////////////CSmsLookCtorList/////////////////////////////////////////////////////

CEasySmsListBase::CEasySmsListBase()
{

}

CEasySmsListBase::~CEasySmsListBase()
{
	long	lCnt	=	GetItemCount();
	for ( int i = 0; i < lCnt; ++i )
	{
		ListItemEx	*pListItemEx	=	GetItem ( i );
		if ( NULL != pListItemEx )
		{
			if ( NULL != pListItemEx->m_pData )
			{
				delete	pListItemEx->m_pData;
				pListItemEx->m_pData	=	NULL;
			}

			delete	pListItemEx;
			pListItemEx	=	NULL;
		}
	}
	
	RemoveAll();
}

// void CEasySmsListBase::OnRemoveItem(int nIndex)
// {
// 	ListItemEx*		pItem	=	GetItem( nIndex );
// 	if( NULL != pItem )
// 	{
// 		MyListItemData*	mlid	=	(MyListItemData*)pItem->Data;
// 		if( NULL != mlid )
// 		{
// 			delete mlid; 
// 		}
// 	}
// }

int CEasySmsListBase::OnLButtonUp( UINT fwKeys, int xPos, int yPos )
{
	int iRlt = UiListEx::OnLButtonUp(fwKeys, xPos, yPos);

	return	iRlt;
}

void	CEasySmsListBase::setEasySmsWndBase( CEasySmsWndBase *pCEasySmsWndBase )
{
	m_pCEasySmsWndBase	=	pCEasySmsWndBase;
}

///////////////CEasySmsWndBase///////////////////////////////////////////////////////////
CEasySmsWndBase::CEasySmsWndBase(void)
{
	m_list_base.setEasySmsWndBase( this );
}


CEasySmsWndBase::~CEasySmsWndBase(void)
{
	m_imgContainer_base.RemoveAll();
}

BOOL CEasySmsWndBase::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

//ini list
	/*ID和AddUiWin在派生类中设定*/
	m_list_base.SetPos( 0, 0, GetWidth(), GetHeight( ) - MZM_HEIGHT_TEXT_TOOLBAR );
	m_list_base.EnableInsideScroll( true );
	m_list_base.EnableUltraGridlines( true );			
	m_list_base.SetItemAttribute( UILISTEX_ITEMTYPE_SMS/*UILISTEX_ITEMTYPE_BASE*/ );  
	m_list_base.EnableNotifyMessage( true );
	m_list_base.UpdateItemAttribute_Del();


//ini toolbar
	/*ID和AddUiWin在派生类中设定*/				
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 2, true, true, L"返回" );
	m_toolBar_base.EnablePressedHoldSupport( true );
	m_toolBar_base.SetPressedHoldTime( 1000 );
	m_toolBar_base.EnableNotifyMessage( true );


	return	TRUE;
}

int	CEasySmsWndBase::DoModalBase( CMzWndEx *pCMzWndEx )
{
	RECT rcWork	=	MzGetWorkArea();
	pCMzWndEx->Create( rcWork.left, rcWork.top, RECT_WIDTH(rcWork), RECT_HEIGHT(rcWork), 0, 0, 0 );
	// 设置窗口切换动画（弹出时的动画）
	pCMzWndEx->SetAnimateType_Show( getScreenRandom() );
	// 设置窗口切换动画（结束时的动画）
	pCMzWndEx->SetAnimateType_Hide( getScreenRandom() );

	return	pCMzWndEx->DoModal();
}

LRESULT CEasySmsWndBase::MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam )
{
	switch(message)
	{
		//处理删除某项时的消息
		case MZ_WM_ITEM_ONREMOVE:
		{
			int			index	=	lParam;
			ListItemEx* pItem	=	m_list_base.GetItem( index );

			DoSthForItemRemove( pItem );
		}
		break;

		/*点击某项被选中的通知消息 wparam 控件ID lparam的低位表示被选中的项,高位表示选中的区域(分割线左,右或非分割线内区域) */
		case MZ_WM_UILIST_LBUTTONUP_SELECT:
		{
			if( m_list_base.GetID() == wParam )
			{	
				int			index	=		lParam;
				index				&=   0x0000FFFF;
				ListItemEx* pItem	=	m_list_base.GetItem( index );
				DoSthForItemBtnUpSelect( pItem );
			}

		}
		break;

		case MZ_WM_ITEM_ONSELECTED:
		{
			int			index	=	wParam;
			ListItemEx* pItem	=	m_list_base.GetItem( index );

			DoSthForItemSelect( pItem );
		}
		break;

		case MZ_WM_MOUSE_NOTIFY:
		{
			if (LOWORD(wParam) == m_toolBar_base.GetID() )
			{
				int nNotify = HIWORD(wParam);
				switch(nNotify)
				{
					case MZ_MN_PRESSEDHOLD_TIMEUP:
					{
						POINT PT	=	m_toolBar_base.GetMouseDownPos ();

						int	nIndex	=	m_toolBar_base.CalcIndexOfPos( PT.x, PT.y );

						DoSthForTooBarHoldPress( nIndex );
						
					}
					break;

					default:
						break;
				}

			}
		}
			
		break;
		default:
		break;
	}


	return CMzWndEx::MzDefWndProc( message, wParam, lParam );
}

void	CEasySmsWndBase::DoSthForItemRemove( ListItemEx* pItem )
{

}

void	CEasySmsWndBase::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{

}

void	CEasySmsWndBase::DoSthForItemSelect( ListItemEx* pItem )
{

}

void	CEasySmsWndBase::DoSthForTooBarHoldPress( int	nIndex )
{
	if ( 2 == nIndex )
	{
		ReturnToMainWnd();
	}
}

void	CEasySmsWndBase::ReturnToMainWnd()
{
	this->EndModal( ID_CASCADE_EXIT );
}

HRESULT	CEasySmsWndBase::RemoveSmsInDb( ListItemEx* pItem )
{
	//get sid

	stCoreItemData *pstCoreItemData	=	(stCoreItemData *)(pItem->m_pData);
	long	lSid	=	pstCoreItemData->lSid;

	//delete
	wchar_t	*pBuf		=	NULL;
	long	lSize		=	0;
	wchar_t	*pwcResult	=	NULL;

	CEasySmsUiCtrl	clCEasySmsUiCtrl;
	clCEasySmsUiCtrl.MakeDeleteSmsInfo( &pBuf, &lSize, &lSid, 1 );


	
	return	S_OK;
}