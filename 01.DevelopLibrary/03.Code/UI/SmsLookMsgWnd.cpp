#include"stdafx.h"
#include "resource.h"

#include "SmsLookMsgWnd.h"
#include "SmsLookMsgDetailWnd.h"
#include "CoreService.h"

class ListItemData
{
	public:
		CMzString	StringTitle;			// 项的主文本
		CMzString	StringDescription;		// 项的描述文本
		BOOL		Selected;				// 项是否被选中

};

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsLookMsgWnd::CSmsLookMsgWnd(void)
{
	memset ( m_pwcPassWord, 0x0, sizeof( m_pwcPassWord ) );
}

CSmsLookMsgWnd::~CSmsLookMsgWnd(void)
{

}

BOOL CSmsLookMsgWnd::OnInitDialog()
{
	if ( !CEasySmsWndBase::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"昵称/号码" );

	return	SubInitialize();
}

void CSmsLookMsgWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD( wParam );

	switch(id)
	{
		case MZ_IDC_SMSLOOKMSG_TOOLBAR:
		{
			int nIndex	=	lParam;
			if ( 2 == nIndex )
			{
				this->EndModal( ID_CANCEL );
			}
			break;
		}

		default:
			break;
	}
}


BOOL	CSmsLookMsgWnd::SubInitialize()
{
	//ini list
	m_list_base.EnableDragModeH(true);
	m_list_base.SetID( MZ_IDC_FIND_RESULT_LIST );
//	m_list_base.SetSelectMode( UILISTEX_SPLITLINE_LEFT );
	AddUiWin( &m_list_base );

	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_SMSLOOKMSG_TOOLBAR );
	AddUiWin( &m_toolBar_base );

	//test
	wchar_t	*pBuf		=	NULL;
	long	lSize		=	0;
	wchar_t	*pwcResult	=	NULL;

	wchar_t	*pTemp	=	NULL;
	if ( L'\0' != *m_pwcPassWord )
	{
		pTemp	=	m_pwcPassWord;
	}

	HRESULT	hr	=	m_clCEasySmsUiCtrl.MakeMsgRltListReq( &pBuf, &lSize, ((stCoreItemData*)(m_pItem->m_pData))->lPid, pTemp );
	if ( FAILED ( hr ) )										return	FALSE;

	CCoreService	*pCCoreService	=	CCoreService::GetInstance();
	if ( NULL == pCCoreService )							return	FALSE;

	hr	=	pCCoreService->Request( pBuf, &pwcResult );
	if ( FAILED ( hr ) )									return	FALSE;

	hr	=	m_clCEasySmsUiCtrl.MakeMsgRltList( m_list_base, pwcResult );

	return	TRUE;
}

void	CSmsLookMsgWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	//
	wchar_t	*pBuf		=	NULL;
	long	lSize		=	0;
	wchar_t	*pwcResult	=	NULL;


	stCoreItemData*	pstItemData	=	(stCoreItemData*)(pItem->m_pData);
	wchar_t	*pTemp	=	NULL;
	if ( L'\0' != *m_pwcPassWord )
	{
		pTemp	=	m_pwcPassWord;
	}

	m_clCEasySmsUiCtrl.MakeDetailReq( &pBuf, &lSize, pstItemData->lSid, pTemp );

	CCoreService	*pCCoreService	=	CCoreService::GetInstance();

	HRESULT	hr	=	pCCoreService->Request( pBuf, &pwcResult );
	wchar_t	*pDetail	=	NULL;

	CCoreSmsUiCtrl	clCCoreSmsUiCtrl;

	clCCoreSmsUiCtrl.MakeDetailRlt( pwcResult, &pDetail );
	if ( NULL != pDetail )
	{
		pstItemData->bstrDetail	=	pDetail;
		delete	pDetail;
		pDetail	=	NULL;
	}

	CSmsLookMsgDetailWnd	clCSmsLookMsgDetailWnd;
	clCSmsLookMsgDetailWnd.SetListItem( pItem );


	int iRlt	=	DoModalBase( &clCSmsLookMsgDetailWnd );
	pstItemData->bstrDetail.Empty();
	if ( ID_CASCADE_EXIT == iRlt )
	{
		ReturnToMainWnd();
	}
}

void	CSmsLookMsgWnd::DoSthForItemRemove( ListItemEx* pItem )
{
	if ( NULL != pItem )
	{
		HRESULT	hr	=	RemoveSmsInDb( pItem );
		if ( FAILED ( hr ) )						return;

		if ( NULL != pItem->m_pData )
		{
			delete	pItem->m_pData;
			pItem->m_pData	=	NULL;
		}

		delete	pItem;
		pItem	=	NULL;
	}
}