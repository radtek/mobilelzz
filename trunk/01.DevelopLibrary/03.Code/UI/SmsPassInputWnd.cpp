#include"stdafx.h"
#include "resource.h"

#include "SmsFindWnd.h"
#include "SmsPassInputWnd.h"

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsPassInputWnd::CSmsPassInputWnd(void)
{

}

CSmsPassInputWnd::~CSmsPassInputWnd(void)
{

}

BOOL CSmsPassInputWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"输入密码" );

	return	SubInitialize();
	
}

void CSmsPassInputWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD(wParam);

	switch(id)
	{
		case MZ_IDC_PASS_TOOLBAR:
		{
			int nIndex	=	lParam;
			if ( nIndex == 2 )
			{
				this->EndModal( ID_CANCEL );
			}
			else if ( nIndex == 0 )
			{

			}
			break;
		}

		default:
			break;
	}
}


BOOL	CSmsPassInputWnd::SubInitialize()
{
	//ini toolbar			
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 0, true, true, L"确定" );
	m_toolBar_base.SetButton( 2, true, true, L"返回" );
	m_toolBar_base.EnablePressedHoldSupport( true );
	m_toolBar_base.SetPressedHoldTime( 1000 );
	m_toolBar_base.EnableNotifyMessage( true );
	m_toolBar_base.SetID( MZ_IDC_PASS_TOOLBAR );
	AddUiWin( &m_toolBar_base );

	//ini	InfoEdit
	m_PassInput.SetID( MZ_IDC_FIND_INFO );
	m_PassInput.SetTextColor( RGB( 255, 0, 0 ) );
	m_PassInput.SetPos( 40, 20, GetWidth() - MZM_MARGIN_MAX * 2, 70 );
	m_PassInput.SetTip( L"请输入密码" );
	m_PassInput.EnablePassWord(); 
	m_PassInput.SetSipMode( IM_SIP_MODE_ADDRESSEE_123, MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_PassInput ); 

	//ini	InfoEdit
	m_PassInput_Again.SetID( MZ_IDC_FIND_INFO );
	m_PassInput_Again.SetTextColor( RGB( 255, 0, 0 ) );
	m_PassInput_Again.SetPos( 40, 100, GetWidth() - MZM_MARGIN_MAX * 2, 70 );
	m_PassInput_Again.SetTip( L"请再次输入密码" );
	m_PassInput_Again.EnablePassWord();
	m_PassInput_Again.SetSipMode( IM_SIP_MODE_ADDRESSEE_123, MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_PassInput_Again ); 

	return	TRUE;
}
