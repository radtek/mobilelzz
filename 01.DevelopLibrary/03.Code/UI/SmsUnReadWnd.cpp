#include "stdafx.h"
#include "resource.h"


#include "SmsUnReadWnd.h"
#include "SmsLookMsgDetailWnd.h"
#include "SmsPassConfirmWnd.h"
#include "CoreService.h"

class ListItemData
{
public:
	CMzString	StringTitle;			// ������ı�
	CMzString	StringDescription;		// ��������ı�
	BOOL		Selected;				// ���Ƿ�ѡ��

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

	SetWindowText( L"δ������" );
	
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
											L"<name>��</name>"
											L"<content>���ǵ�һ�����ţ������Ұɣ�����������</content>"
											L"<address>13609836338</address>"
											L"<time></time>"
											L"<lockstatus>0</lockstatus>"
											L"<readstatus>0</readstatus>"
										L"</rec>"
										L"<rec encode = \"true\">"
											L"<sid>2</sid>"
											L"<pid>3</pid>"
											L"<type>0</type>"
											L"<name>��</name>"
											L"<content>���ǵڶ������ţ������Ұɣ�����������</content>"
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
		/*���ñ���ͼƬ*/
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

#if 0
	CMzString content = L"�������� SmsContent%d:";
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
			item->m_textDescription = L"���������Ҫ���ˣ������أ����������������������أ� ������һ�ж����ˣ��������أ�";
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
	/////////////test/////////////////////////////////////////////////////////////

	if ( NULL != pBuf )
	{
		delete	pBuf, pBuf	=	NULL;
	}

	return	TRUE;
}

void	CSmsUnReadWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	int iRlt	=	-1;
	bool	bIsLock		=	( ( stItemData* )( pItem->m_pData ) )->bIsLock;

// 	if ( bIsLock )					//����ϵ���Ǳ�������
// 	{
// 		CSmsPassConfirmWnd	clCSmsPassConfirmWnd;
// 		clCSmsPassConfirmWnd.CreateModalDialog( 50, 100, 350, 250, this->m_hWnd );
// 		iRlt	=	DoModalBase( &clCSmsPassConfirmWnd );
// 		if ( ID_OK == iRlt )		//������ȷ����
// 		{
// 			//ȡ�ö�����Ϣ������DetailWnd
// 		}
// 
// 	}
// 	else
// 	{
		CSmsLookMsgDetailWnd	clCSmsLookMsgDetailWnd( pItem->m_textTitle );

		clCSmsLookMsgDetailWnd.SetText( pItem->m_textDescription );
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
		if ( NULL != pItem->m_pData )
		{
			delete	pItem->m_pData;
			pItem->m_pData	=	NULL;
		}

		delete	pItem;
		pItem	=	NULL;
	}
}