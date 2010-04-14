#include "stdafx.h"
#include "resource.h"

#include "SmsFindResultWnd.h"
#include "SmsLookMsgDetailWnd.h"

class ListItemData
{
public:
	CMzString	StringTitle;			// ������ı�
	CMzString	StringDescription;		// ��������ı�
	BOOL		Selected;				// ���Ƿ�ѡ��

};

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsFindResultWnd::CSmsFindResultWnd(void)
{

}

CSmsFindResultWnd::~CSmsFindResultWnd(void)
{

}

BOOL CSmsFindResultWnd::OnInitDialog()
{
	if ( !CEasySmsWndBase::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"���ҽ��" );
	
	return	SubInitialize();
}

void CSmsFindResultWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
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
		else if ( 0 == nIndex )
		{
			//ѡ��
			// ʹ�öԻ���ʽ���� GridMenu
			if ( m_GridMenu.IsContinue() )
			{
				m_GridMenu.EndGridMenu();
			}
			else
			{
				m_GridMenu.TrackGridMenuDialog( m_hWnd, MZM_HEIGHT_TOOLBARPRO );
			}
		}
		break;
	}
	// ���� GridMenu ��������Ϣ
	case MZ_IDC_FIND_RESULT_BACK:
	{
		if ( m_GridMenu.IsContinue() )
		{
			m_GridMenu.EndGridMenu();
		}
	}

	default:
		break;
	}
}

BOOL	CSmsFindResultWnd::SubInitialize()
{
	//ini list

	m_list_base.SetID( MZ_IDC_SMS_UNREAD_LIST );
	m_list_base.EnableDragModeH(true);
	m_list_base.SetMultiSelectMode( UILISTEX_MULTISELECT_LEFT );
	m_list_base.SetSplitLineMode ( UILISTEX_SPLITLINE_RIGHT ); 
	AddUiWin( &m_list_base );


	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_SMS_UNREAD_TOOLBAR );
	m_toolBar_base.SetButton( 0, true, true, L"ѡ��" );
	AddUiWin( &m_toolBar_base );

	//Ini	GridMenu
	m_GridMenu.AppendMenuItem(MZ_IDC_FIND_RESULT_ALL_SELECT, L"ȫѡ" );
	m_GridMenu.AppendMenuItem(MZ_IDC_FIND_RESULT_DELETE, L"ɾ��" );
	m_GridMenu.AppendMenuItem(MZ_IDC_FIND_RESULT_LOCK, L"����" );
	m_GridMenu.AppendMenuItem(MZ_IDC_FIND_RESULT_BACK, L"����" );

	/////////////test/////////////////////////////////////////////////////////////

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


	/////////////test/////////////////////////////////////////////////////////////

	return	TRUE;
}

void	CSmsFindResultWnd::DoSthForItemBtnUpSelect( ListItemEx* pItem )
{
	CSmsLookMsgDetailWnd	clCSmsLookMsgDetailWnd;
	//Test
	wchar_t chTemp[ 256 ]	=	L"�Ӳ��ҽ����ҳ�������ҳ��";
	//
	clCSmsLookMsgDetailWnd.SetText( chTemp );
	int iRlt	=	DoModalBase( &clCSmsLookMsgDetailWnd );
	if ( ID_CASCADE_EXIT == iRlt )
	{
		ReturnToMainWnd();
	}
}