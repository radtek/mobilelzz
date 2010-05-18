#include"stdafx.h"
#include "resource.h"

#include "SmsEncrytpCtorWnd.h"
#include "SmsPassInputWnd.h"
#include "CoreService.h"


class ListItemData
{
	public:
		CMzString	StringTitle;			// 项的主文本
		CMzString	StringDescription;		// 项的描述文本
		BOOL		Selected;				// 项是否被选中

};

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsEncrytpCtorWnd::CSmsEncrytpCtorWnd(void)
{

}

CSmsEncrytpCtorWnd::~CSmsEncrytpCtorWnd(void)
{

}

BOOL CSmsEncrytpCtorWnd::OnInitDialog()
{
	if ( !CEasySmsWndBase::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"联系人加密" );

	return	SubInitialize();
	
}

void CSmsEncrytpCtorWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD(wParam);

	switch(id)
	{
		case MZ_IDC_ENCRYPT_TOOLBAR:
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


BOOL	CSmsEncrytpCtorWnd::SubInitialize()
{
	//ini list
	
	m_list_base.SetID( MZ_IDC_ENCRYPT_LIST );
	AddUiWin( &m_list_base );

	//ini alp
	m_AlpBar.SetID( MZ_IDC_ENCRYPT_ALPBAR );
	m_AlpBar.EnableZoomAlphabet( true );
	m_AlpBar.EnableNotifyMessage( true );
	m_AlpBar.SetPos( 350, 0, 50, GetHeight() );
	AddUiWin( &m_AlpBar );

	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_ENCRYPT_TOOLBAR );
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
		L"<pid>2</pid>"
		L"<name>张三</name>"
		L"<defaultno>13600000001</defaultno>"
		L"<smscount>9<smscount>"
		L"</rec>"
		L"<rec encode = \"true\">"
		L"<pid>3</pid>"
		L"<name>李四</name>"
		L"<defaultno>13600000002</defaultno>"
		L"<smscount>88<smscount>"
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

void	CSmsEncrytpCtorWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	stItemData	*pstItemData	=	( stItemData* )( pItem->m_pData );
	if ( NULL != pstItemData )
	{
		CSmsPassInputWnd	clCSmsPassInputWnd;
		clCSmsPassInputWnd.SetID( pstItemData->lSid );
		clCSmsPassInputWnd.CreateModalDialog( 50, 100, 350, 250, this->m_hWnd );
		//	clCSmsPassInputWnd.SetBgColor ( RGB( 228, 240, 0 ) );
		int iRlt	=	DoModalBase( &clCSmsPassInputWnd );
		if ( ID_CASCADE_EXIT == iRlt )
		{
			ReturnToMainWnd();
		}
	}

}