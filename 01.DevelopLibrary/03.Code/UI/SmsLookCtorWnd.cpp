#include"stdafx.h"
#include "resource.h"

#include "SmsLookCtorWnd.h"
#include "SmsLookMsgWnd.h"
#include "CoreService.h"
#include "SmsPassConfirmWnd.h"

class ListItemData
{
	public:
		CMzString	StringTitle;			// 项的主文本
		CMzString	StringDescription;		// 项的描述文本
		BOOL		Selected;				// 项是否被选中

};

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsLookCtorWnd::CSmsLookCtorWnd(void)
{

}

CSmsLookCtorWnd::~CSmsLookCtorWnd(void)
{

}

BOOL CSmsLookCtorWnd::OnInitDialog()
{
	if ( !CEasySmsWndBase::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"短信查看" );

	return	SubInitialize();
	
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


BOOL	CSmsLookCtorWnd::SubInitialize()
{
	//ini list
	
	m_list_base.SetID( MZ_IDC_SMSLOOKCTOR_LIST );
	AddUiWin( &m_list_base );
/////////////////////////////////////////////////////////////////	
//	ItemAttribute	clItemAttribute;
//	clItemAttribute.SetPsText1Param( 100, RGB(100, 100, 100), FW_MEDIUM, false, false, 
//							DT_TOP|DT_LEFT|DT_END_ELLIPSIS|DT_SINGLELINE|DT_NOPREFIX ); 

//	m_list_base.SetItemAttributeH( clItemAttribute );
///////////////////////////////////////////////////////////////////
	//ini alp
	m_AlpBar.SetID( MZ_IDC_SMSLOOKCTOR_ALPBAR );
	m_AlpBar.EnableZoomAlphabet( true );
	m_AlpBar.EnableNotifyMessage( true );
	m_AlpBar.SetPos( 350, 0, 50, GetHeight() );
	AddUiWin( &m_AlpBar );

	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_SMSLOOKCTOR_TOOLBAR );
	AddUiWin( &m_toolBar_base );


	/////////////test/////////////////////////////////////////////////////////////
	wchar_t	*pBuf		=	NULL;
	long	lSize		=	0;
	wchar_t	*pwcResult	=	NULL;

	HRESULT	hr	=	m_clCEasySmsUiCtrl.MakeCtorRltListReq( &pBuf, &lSize );
	if ( FAILED ( hr ) )									return	FALSE;

	CCoreService	*pCCoreService	=	CCoreService::GetInstance();
	if ( NULL == pCCoreService )							return	FALSE;

#ifdef	UI_TEST
		 pwcResult =	L"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
							L"<result>"
								L"<data type = \"contactors\">"
									L"<data type = \"list\" count = \"2\">"
										L"<rec encode = \"true\">"
											L"<name>张三</name>"
											L"<pid>2</pid>"										
											L"<address>13600000001</address>"
											L"<smscount>9</smscount>"
										L"</rec>"
										L"<rec encode = \"true\">"
											L"<name>李四</name>"
											L"<pid>3</pid>"
											L"<address>13600000002</address>"
											L"<smscount>88</smscount>"
										L"</rec>"
									L"</data>"
								L"</data>"
							L"</result>";
#else
	hr	=	pCCoreService->Request( pBuf, &pwcResult );
	if ( FAILED ( hr ) )									return	FALSE;
#endif

	hr	=	m_clCEasySmsUiCtrl.MakeCtorRltList( m_list_base, pwcResult );
#if 0	
	CMzString name = L"姓名%d";
//	CMzString content = L"短信内容 SmsContent%d:";
	CMzString stime = L"Count: 100";

	CMzString name1(10);
	CMzString content1(30);
	for (int i = 0; i < 100; i++)
	{
		swprintf(name1.C_Str(),name.C_Str(),i);
//		swprintf(content1.C_Str(),content.C_Str(),i);
		ListItemEx* item = new ListItemEx;
		
//		ItemAttribute	clItemAttribute;
//		clItemAttribute.SetPsText1Param( 100, RGB(100, 100, 100), FW_MEDIUM, false, false, 
//									DT_TOP|DT_END_ELLIPSIS|DT_SINGLELINE|DT_NOPREFIX ); 
//		item->m_pSpecialItemAttr	=	&clItemAttribute;

		
		item->m_pData = (void*)i;
		item->m_textTitle = name1.C_Str();

		item->m_textPostscript1 = stime.C_Str();


 		item->m_pImgFirst = m_imgContainer_base.LoadImage(MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true);

		m_list_base.AddItem(item);
	}

#endif

	if ( NULL != pBuf )
	{
		delete	pBuf, pBuf	=	NULL;
	}
/////////////test/////////////////////////////////////////////////////////////

	return	TRUE;
}

void	CSmsLookCtorWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	bool	bIsEncode		=	( ( stCoreItemData* )( pItem->m_pData ) )->bIsEncode;
	int		iRlt		=	-100;
	if ( bIsEncode )					//该联系人是被加锁的
	{
		CSmsPassConfirmWnd	clCSmsPassConfirmWnd;
		clCSmsPassConfirmWnd.SetListItem( pItem );
		clCSmsPassConfirmWnd.CreateModalDialog( 50, 100, 350, 250, this->m_hWnd );
		int iRlt	=	DoModalBase( &clCSmsPassConfirmWnd );
		if ( ID_OK == iRlt )		//密码正确返回
		{
			//取得短信信息并进入DetailWnd
			CSmsLookMsgWnd	clCSmsLookMsgWnd;
			clCSmsLookMsgWnd.SetPassWord( clCSmsPassConfirmWnd.GetPassWord() );
			clCSmsLookMsgWnd.SetListItem( pItem );
			int iRlt	=	DoModalBase( &clCSmsLookMsgWnd );
		}
	}
	else
	{
		CSmsLookMsgWnd	clCSmsLookMsgWnd;
		clCSmsLookMsgWnd.SetListItem( pItem );
		int iRlt	=	DoModalBase( &clCSmsLookMsgWnd );
	}
	

	if ( ID_CASCADE_EXIT == iRlt )
	{
		ReturnToMainWnd();
	}
}