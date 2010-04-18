#include"stdafx.h"
#include "resource.h"

#include "SmsLookCtorWnd.h"
#include "SmsLookMsgWnd.h"


class ListItemData
{
	public:
		CMzString	StringTitle;			// ������ı�
		CMzString	StringDescription;		// ��������ı�
		BOOL		Selected;				// ���Ƿ�ѡ��

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

	SetWindowText( L"���Ų鿴" );

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

void	CSmsLookCtorWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	CSmsLookMsgWnd	clCSmsLookMsgWnd;
	int iRlt	=	DoModalBase( &clCSmsLookMsgWnd );
	if ( ID_CASCADE_EXIT == iRlt )
	{
		ReturnToMainWnd();
	}
}