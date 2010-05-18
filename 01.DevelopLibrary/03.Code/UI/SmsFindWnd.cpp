#include"stdafx.h"
#include "resource.h"

#include "SmsFindWnd.h"
#include "SmsLookMsgWnd.h"
#include "SmsFindResultWnd.h"

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsFindWnd::CSmsFindWnd(void)
{

}

CSmsFindWnd::~CSmsFindWnd(void)
{

}

BOOL CSmsFindWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"短信查找" );

	return	SubInitialize();
	
}

void CSmsFindWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD(wParam);

	switch(id)
	{
		case MZ_IDC_FIND_TOOLBAR:
		{
			int nIndex	=	lParam;
			if ( nIndex == 2 )
			{
				this->EndModal( ID_CANCEL );
			}
			else if ( nIndex == 1 )
			{
				CSmsFindResultWnd		clCSmsFindResultWnd;
				int	iRlt	=	DoModalBase( &clCSmsFindResultWnd );
				if ( ID_CASCADE_EXIT == iRlt )
				{
					ReturnToMainWnd();
				}
			}
			break;
		}

		default:
			break;
	}
}


BOOL	CSmsFindWnd::SubInitialize()
{
	/*设置背景图片*/
	m_modeIndex	=	0;
	m_Picture.SetID( MZ_IDC_MAIN_PICTURE );
	m_Picture.SetPos( 0, 0, GetWidth(), GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR );
	m_Picture.SetPaintMode( modeId[ m_modeIndex ] );
	m_Picture.LoadImage( MzGetInstanceHandle(), RT_RCDATA, MAKEINTRESOURCE( IDR_PNG_MAIN_WND_BACKGROUND ) );
	AddUiWin( &m_Picture );
	
	//ini toolbar			
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 1, true, true, L"确定" );
	m_toolBar_base.SetButton( 2, true, true, L"返回" );
	m_toolBar_base.EnablePressedHoldSupport( true );
	m_toolBar_base.SetPressedHoldTime( 1000 );
	m_toolBar_base.EnableNotifyMessage( true );
	m_toolBar_base.SetID( MZ_IDC_FIND_TOOLBAR );
	AddUiWin( &m_toolBar_base );
	
	//ini UiSingleLineEdit
	long	lW	=	200;
	m_ContactorsEdit.SetID( MZ_IDC_FIND_CONTACTORS );
	m_ContactorsEdit.SetPos( 50, MZM_MARGIN_MAX, lW, 70 );
	m_ContactorsEdit.SetTextColor( RGB( 255, 0, 0 ) );
	m_ContactorsEdit.EnableGrabFocusByMouse ( false );

	AddUiWin( &m_ContactorsEdit ); 

	//ini ContactorsBtn
	m_ContactorsBtn.SetButtonType( MZC_BUTTON_GREEN );
	m_ContactorsBtn.SetPos( lW + 70, MZM_MARGIN_MAX, 100, 100 );
	m_ContactorsBtn.SetID( MZ_IDC_FIND_CONTACTORS_BTN );
	m_ContactorsBtn.SetText( L"联系人" );
	m_ContactorsBtn.SetTextColor(RGB(255,255,255));
	AddUiWin( &m_ContactorsBtn ); 

	//ini	InfoEdit
	m_InfoEdit.SetID( MZ_IDC_FIND_INFO );
	m_InfoEdit.SetTextColor( RGB( 255, 0, 0 ) );
	m_InfoEdit.SetPos( MZM_MARGIN_MAX, MZM_MARGIN_MAX + 150, GetWidth() - MZM_MARGIN_MAX * 2, 70 );
	m_InfoEdit.SetTip( L"请输入要查找的内容:……" );
	AddUiWin( &m_InfoEdit ); 


	return	TRUE;
}
