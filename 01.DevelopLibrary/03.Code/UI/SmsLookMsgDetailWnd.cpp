#include"stdafx.h"
#include "resource.h"

#include "SmsLookMsgDetailWnd.h"


///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsLookMsgDetailWnd::CSmsLookMsgDetailWnd(void)
{

}

CSmsLookMsgDetailWnd::~CSmsLookMsgDetailWnd(void)
{

}

BOOL CSmsLookMsgDetailWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

	wchar_t	*pName	=	GetNameOrTel( m_pItem );
	if ( NULL != pName )
	{
		SetWindowText( pName );
	}

	return	SubInitialize();
}

void CSmsLookMsgDetailWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD( wParam );

	switch(id)
	{
		case MZ_IDC_SMSLOOKMSG_DETAIL_TOOLBAR:
		{
			int nIndex	=	lParam;
			if ( 2 == nIndex )
			{
				this->EndModal( ID_CANCEL );	//�˳�
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
			else if ( 1 == nIndex )
			{
				//�ظ�
				CNewSmsWnd		clSendSms;
				int	iRlt	=	DoModalBase( &clSendSms );
				if ( ID_CASCADE_EXIT == iRlt )
				{
					ReturnToMainWnd();
				}
			}

			break;
		}
		// ���� GridMenu ��������Ϣ
		case MZ_IDC_SMSLOOKMSG_DETAIL_GRIDMENU_BACK:
		{
			if ( m_GridMenu.IsContinue() )
			{
				m_GridMenu.EndGridMenu();
			}
		}

		break;

		default:
			break;
	}
}


BOOL	CSmsLookMsgDetailWnd::SubInitialize()
{
	//Ini UiEdit
	m_UiEdit.SetID( MZ_IDC_SMSLOOKMSG_DETAIL_EDIT );
	m_UiEdit.EnableZoomIn( false );				/*���ñ༭���Ƿ�֧�ַŴ�*/
	m_UiEdit.OpenFace(true);					/*���ñ༭���Ƿ�֧�ֱ���ʶ��*/ 
	m_UiEdit.SetReadOnly( true );				/*�ؼ��ı����Զ�д*/
//	m_UiEdit.SetLineSpace( 10 );				/*�����о�Ϊ10*/
	m_UiEdit.EnableCopy ( true );
	m_UiEdit.SetAlignMode ( UIEDIT_ALIGN_LEFT );
	m_UiEdit.SetEditBgType( UI_EDIT_BGTYPE_ROUND_RECT ); 
	long lW = GetWidth();
	long lH = GetHeight();
	m_UiEdit.SetPos( 0, 0, lW, lH - MZM_HEIGHT_TEXT_TOOLBAR );
	m_UiEdit.SetRightInvalid(0);
	m_UiEdit.EnableInsideScroll( true );
	AddUiWin( &m_UiEdit );
	
	//ini toolbar
	m_toolBar_base.SetID( MZ_IDC_SMSLOOKMSG_DETAIL_TOOLBAR );
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 0, true, true, L"ѡ��" );
	m_toolBar_base.SetButton( 1, true, true, L"�ظ�" );
	m_toolBar_base.SetButton( 2, true, true, L"����" );
	m_toolBar_base.EnablePressedHoldSupport( true );
	m_toolBar_base.SetPressedHoldTime( 1000 );
	m_toolBar_base.EnableNotifyMessage( true );
	
	AddUiWin( &m_toolBar_base );

	//Ini	GridMenu
	m_GridMenu.AppendMenuItem(MZ_IDC_SMSLOOKMSG_DETAIL_GRIDMENU_DETAIL, L"��ȡ����" );
	m_GridMenu.AppendMenuItem(MZ_IDC_SMSLOOKMSG_DETAIL_GRIDMENU_FW, L"ת��" );
	m_GridMenu.AppendMenuItem(MZ_IDC_SMSLOOKMSG_DETAIL_GRIDMENU_LOCK, L"����" );
	m_GridMenu.AppendMenuItem(MZ_IDC_SMSLOOKMSG_DETAIL_GRIDMENU_BACK, L"����" );

	//add Text
	wchar_t	*pInfor	=	GetMsgInfoFromIterm( m_pItem );
	if ( NULL != pInfor )
	{
		SetText( pInfor );
	}

	return	TRUE;
}

void	CSmsLookMsgDetailWnd::SetText( LPCTSTR text )
{
	m_UiEdit.SetText( text );
}
