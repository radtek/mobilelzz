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
CSmsLookMsgWnd::CSmsLookMsgWnd(void)	:	m_pListInfo( NULL )
{

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
	AddUiWin( &m_list_base );

	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_SMSLOOKMSG_TOOLBAR );
	AddUiWin( &m_toolBar_base );

	//test
	wchar_t	*pBuf		=	NULL;
	long	lSize		=	0;
	wchar_t	*pwcResult	=	NULL;


	HRESULT	hr	=	m_clCEasySmsUiCtrl.MakeMsgRltListReq( &pBuf, &lSize, ((stCoreItemData*)(m_pListInfo->m_pData))->lPid );
	if ( FAILED ( hr ) )										return	FALSE;

	CCoreService	*pCCoreService	=	CCoreService::GetInstance();
	if ( NULL == pCCoreService )							return	FALSE;

	hr	=	pCCoreService->Request( pBuf, &pwcResult );
	if ( FAILED ( hr ) )									return	FALSE;

	hr	=	m_clCEasySmsUiCtrl.MakeMsgRltList( m_list_base, pwcResult );
#if 0
	CMzString content = L"短信内容 SmsContent%d:";
	CMzString stime = L"12:20";

	CMzString content1(30);
	for (int i = 0; i < 100; i++)
	{
		swprintf(content1.C_Str(),content.C_Str(),i);

		ListItemEx* item = new ListItemEx;
		item->m_pData = (void*)i;

		item->m_textPostscript1 = stime.C_Str();

		if (i == 2)
		{
			item->m_textDescription = L"content: I&apos;m dying to see u! what? I couldn&apos;t get it";
		}
		else if(i == 6)
		{
			item->m_textDescription = L"我这里天快要黑了，那里呢？我这里天气凉凉的那里呢？ 我这里一切都变了，而那你呢？";
		}
		else
		{
			item->m_textDescription = content1.C_Str();
		}

		item->m_pImgFirst = m_imgContainer_base.LoadImage(MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true);

		item->m_itemBgType	=	UILIST_ITEM_BGTYPE_YELLOW;
		m_list_base.AddItem(item);
	}
#endif
	//
	return	TRUE;
}

void	CSmsLookMsgWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	CSmsLookMsgDetailWnd	clCSmsLookMsgDetailWnd;
	clCSmsLookMsgDetailWnd.SetListInfo( pItem );
	//
	wchar_t	*pBuf		=	NULL;
	long	lSize		=	0;
	wchar_t	*pwcResult	=	NULL;


	stCoreItemData*	pstItemData	=	(stCoreItemData*)(pItem->m_pData);
	HRESULT	hr	=	m_clCEasySmsUiCtrl.MakeDetailRltListReq( &pBuf, &lSize, pstItemData->lSid );

	CCoreService	*pCCoreService	=	CCoreService::GetInstance();

	hr	=	pCCoreService->Request( pBuf, &pwcResult );

	//Test
	wchar_t chTemp[ 256 ]	=	L"我对你的崇拜犹如滔滔江水连绵不绝！！！！";
	//
	clCSmsLookMsgDetailWnd.SetText( chTemp );
	clCSmsLookMsgDetailWnd.SetListInfo( pItem );
	int iRlt	=	DoModalBase( &clCSmsLookMsgDetailWnd );
	if ( ID_CASCADE_EXIT == iRlt )
	{
		ReturnToMainWnd();
	}
}

void	CSmsLookMsgWnd::SetListInfo( ListItemEx* pListInfo )
{
	m_pListInfo		=	pListInfo;
}