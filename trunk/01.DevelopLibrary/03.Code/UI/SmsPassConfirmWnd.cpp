#include"stdafx.h"
#include "resource.h"

#include "SmsFindWnd.h"
#include "SmsPassConfirmWnd.h"

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsPassConfirmWnd::CSmsPassConfirmWnd(void)
{

}

CSmsPassConfirmWnd::~CSmsPassConfirmWnd(void)
{

}

BOOL CSmsPassConfirmWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"输入密码" );

	return	SubInitialize();
	
}

void CSmsPassConfirmWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id = LOWORD(wParam);

	switch(id)
	{
		case MZ_IDC_PASS_TOOLBAR:
		{
			int nIndex	=	lParam;
			if ( nIndex == 2 )				//取消
			{
				this->EndModal( ID_CANCEL );
			}
			else if ( nIndex == 0 )			//确认
			{
				//取得密码并提交

//				HRESULT	hr	=	CommitPassWord();
// 				if ( FAILED ( hr ) )
// 				{
// 
// 				}
// 				else
// 				{
// 					this->EndModal( ID_OK  );
// 				}
				
			}

			break;
		}

		default:
			break;
	}
}


BOOL	CSmsPassConfirmWnd::SubInitialize()
{
	
	/*设置背景图片*/
	m_modeIndex	=	0;
	m_Picture.SetID( MZ_IDC_PASS_PICTURE );
	m_Picture.SetPos( 0, 0, 350, 250 );
	m_Picture.SetPaintMode( modeId[ m_modeIndex ] );
	m_Picture.LoadImage( MzGetInstanceHandle(), RT_RCDATA, MAKEINTRESOURCE( IDR_ENCRYPT_INPUT ) );
	AddUiWin( &m_Picture );
	
	
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
	m_PassInput.SetPos( 80, 90, GetWidth() - MZM_MARGIN_MAX * 4, 40 );
	m_PassInput.SetTip( L"请输入密码" );
	m_PassInput.EnablePassWord(); 
	m_PassInput.SetSipMode( IM_SIP_MODE_ADDRESSEE_123, MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_PassInput ); 

	return	TRUE;
}


void	CSmsPassConfirmWnd::SetID ( long id )
{
	m_id	=	id;
}