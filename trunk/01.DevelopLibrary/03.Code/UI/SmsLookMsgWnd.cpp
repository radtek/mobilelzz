#include"stdafx.h"
#include "resource.h"

#include "SmsLookMsgWnd.h"

class ListItemData
{
	public:
		CMzString	StringTitle;			// ������ı�
		CMzString	StringDescription;		// ��������ı�
		BOOL		Selected;				// ���Ƿ�ѡ��

};

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsLookMsgWnd::CSmsLookMsgWnd(void)
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

	SetWindowText( L"�ǳ�/����" );

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
	m_list_base.SetID( MZ_IDC_SMSLOOKMSG_LIST );
	AddUiWin( &m_list_base );

	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_SMSLOOKMSG_TOOLBAR );
	AddUiWin( &m_toolBar_base );

	//test
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

	//
	return	TRUE;
}