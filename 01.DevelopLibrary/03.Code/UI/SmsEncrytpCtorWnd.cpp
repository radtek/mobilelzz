#include"stdafx.h"
#include "resource.h"

#include "SmsEncrytpCtorWnd.h"
#include "SmsPassInputWnd.h"



class ListItemData
{
	public:
		CMzString	StringTitle;			// ������ı�
		CMzString	StringDescription;		// ��������ı�
		BOOL		Selected;				// ���Ƿ�ѡ��

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

	SetWindowText( L"��ϵ�˼���" );

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
	
	CMzString name = L"����%d";
//	CMzString content = L"�������� SmsContent%d:";
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


/////////////test/////////////////////////////////////////////////////////////

	return	TRUE;
}

void	CSmsEncrytpCtorWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	CSmsPassInputWnd	clCSmsPassInputWnd;
	clCSmsPassInputWnd.CreateModalDialog( 50, 100, 400, 300, this->m_hWnd );
	clCSmsPassInputWnd.SetBgColor ( RGB( 228, 240, 0 ) );
	int iRlt	=	DoModalBase( &clCSmsPassInputWnd );
	if ( ID_CASCADE_EXIT == iRlt )
	{
		ReturnToMainWnd();
	}
}