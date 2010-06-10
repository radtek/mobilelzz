#include "stdafx.h"
#include "resource.h"


#include "SmsUnReadWnd.h"
#include "SmsLookMsgDetailWnd.h"
#include "SmsPassConfirmWnd.h"


class ListItemData
{
public:
	CMzString	StringTitle;			// 项的主文本
	CMzString	StringDescription;		// 项的描述文本
	BOOL		Selected;				// 项是否被选中

};

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsUnReadWnd::CSmsUnReadWnd(void)
{

}

CSmsUnReadWnd::~CSmsUnReadWnd(void)
{

}

BOOL CSmsUnReadWnd::OnInitDialog()
{
	if ( !CEasySmsWndBase::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"未读短信" );
	
	return	SubInitialize();
}

void CSmsUnReadWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD( wParam );

	switch(id)
	{
	case MZ_IDC_SMS_UNREAD_TOOLBAR:
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

BOOL	CSmsUnReadWnd::SubInitialize()
{
	//ini list
	m_list_base.SetID( MZ_IDC_SMS_UNREAD_LIST );
	m_list_base.EnableDragModeH(true);


	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_SMS_UNREAD_TOOLBAR );
	AddUiWin( &m_toolBar_base );


/////////////test/////////////////////////////////////////////////////////////
	wchar_t	*pBuf		=	NULL;
	long	lSize		=	0;
	wchar_t	*pwcResult	=	NULL;


	HRESULT	hr	=	m_clCEasySmsUiCtrl.MakeUnReadRltListReq( &pBuf, &lSize );
	if ( FAILED ( hr ) )										return	FALSE;

	

#ifdef	UI_TEST
		 pwcResult =	L"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
							L"<result>"
								L"<data type = \"sms\">"
									L"<data type = \"list\" count = \"2\">"
										L"<rec encode = \"true\">"
											L"<sid>1</sid>"
											L"<pid>2</pid>"
											L"<type>1</type>"
											L"<name>妞妞</name>"
											L"<content>我是第一条短信，来点我吧！！！！！！</content>"
											L"<address>13609836338</address>"
											L"<time></time>"
											L"<lockstatus>0</lockstatus>"
											L"<readstatus>0</readstatus>"
										L"</rec>"
										L"<rec encode = \"true\">"
											L"<sid>2</sid>"
											L"<pid>3</pid>"
											L"<type>0</type>"
											L"<name>妞妞</name>"
											L"<content>我是第二条短信，来点我吧！！！！！！</content>"
											L"<address>13804026490</address>"
											L"<time></time>"
											L"<lockstatus>0</lockstatus>"
											L"<readstatus>0</readstatus>"
										L"</rec>"
									L"</data>"
								L"</data>"
							L"</result>";
#else
	
	CCoreService	*pCCoreService	=	CCoreService::GetInstance();
	if ( NULL == pCCoreService )								return	FALSE;
	
	hr	=	pCCoreService->Request( pBuf, &pwcResult );
	if ( FAILED ( hr ) )										return	FALSE;
#endif
	hr	=	m_clCEasySmsUiCtrl.MakeUnReadListRlt( m_list_base, pwcResult );
	if ( E_ACCESSDENIED == hr )
	{
		/*设置背景图片*/
		m_modeIndex	=	0;
		m_Picture.SetID( MZ_IDC_MAIN_PICTURE );
		m_Picture.SetPos( 0, 0, GetWidth(), GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR );
		m_Picture.SetPaintMode( modeId[ m_modeIndex ] );
		m_Picture.LoadImage( MzGetInstanceHandle(), RT_RCDATA, MAKEINTRESOURCE( IDR_PNG_MAIN_WND_BACKGROUND ) );
		AddUiWin( &m_Picture );	
	}
	else if ( FAILED ( hr ) )
	{
		return	FALSE;
	}
	else
	{
		AddUiWin( &m_list_base );
	}

	if ( NULL != pBuf )
	{
		delete	pBuf, pBuf	=	NULL;
	}

	return	TRUE;
}

void	CSmsUnReadWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	int iRlt	=	-1;
//	bool	bIsLock		=	( ( stCoreItemData* )( pItem->m_pData ) )->bIsLock;

// 	if ( bIsLock )					//该联系人是被加锁的
// 	{
// 		CSmsPassConfirmWnd	clCSmsPassConfirmWnd;
// 		clCSmsPassConfirmWnd.CreateModalDialog( 50, 100, 350, 250, this->m_hWnd );
// 		iRlt	=	DoModalBase( &clCSmsPassConfirmWnd );
// 		if ( ID_OK == iRlt )		//密码正确返回
// 		{
// 			//取得短信信息并进入DetailWnd
// 		}
// 
// 	}
// 	else
// 	{
		stCoreItemData* pstCoreItemData	=	( stCoreItemData* )( pItem->m_pData );

		CCoreService	*pCCoreService	=	CCoreService::GetInstance();
		if ( NULL == pCCoreService )						return;

		//GetDtail
		wchar_t	*pBuf		=	NULL;
		long	lSize		=	0;
		wchar_t	*pwcResult	=	NULL;
		m_clCEasySmsUiCtrl.MakeDetailReq( &pBuf, &lSize, pstCoreItemData->lSid, NULL );
		pCCoreService->Request( pBuf, &pwcResult );

		wchar_t	*pDetail	=	NULL;

		CCoreSmsUiCtrl	clCCoreSmsUiCtrl;

		clCCoreSmsUiCtrl.MakeDetailRlt( pwcResult, &pDetail );

		//Update ReadStatus
		
		if ( 0 == pstCoreItemData->bIsRead && false	== pstCoreItemData->bIsEncode )	//未读为0
		{
			wchar_t	*pBuf		=	NULL;
			long	lSize		=	0;
			wchar_t	*pwcResult	=	NULL;

			m_clCEasySmsUiCtrl.MakeUpdateSmsStatusReq( &pBuf, &lSize, pstCoreItemData->lSid, (long)(-1), (long)(1) );

			pCCoreService->Request( pBuf, &pwcResult );
		}
		
		CSmsLookMsgDetailWnd	clCSmsLookMsgDetailWnd;

		clCSmsLookMsgDetailWnd.SetListItem( pItem );
		iRlt	=	DoModalBase( &clCSmsLookMsgDetailWnd );

//	}

	if ( ID_CASCADE_EXIT == iRlt )
	{
		ReturnToMainWnd();
	}
	

}

void	CSmsUnReadWnd::DoSthForItemRemove( ListItemEx* pItem )
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