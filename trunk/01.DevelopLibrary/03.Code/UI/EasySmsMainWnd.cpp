#include"stdafx.h"
#include "resource.h"

#include "EasySmsMainWnd.h"
#include "SmsLookCtorWnd.h"


CEasySmsMainWnd::CEasySmsMainWnd(void)
{

}

CEasySmsMainWnd::~CEasySmsMainWnd(void)
{

}

BOOL CEasySmsMainWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}
	
	SetWindowText( L"Ò×¶ÌÐÅ2.000" );

	return	SubInitialize();
}

void CEasySmsMainWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id	=	LOWORD( wParam );

	int iRlt	=	-1;

	switch( id )
	{
		case MZ_IDC_LOOK_SMS:
		{
			CSmsLookCtorWnd		clCSmsLookCtorWnd;
			iRlt	=	DoModalBase( &clCSmsLookCtorWnd );

			break;
		}
			

		case MZ_IDC_SEND_SMS:
		{

			CNewSmsWnd		clSendSms;
			iRlt	=	DoModalBase( &clSendSms );

			break;
		}

		case IDR_TOOLBAR_MAIN_WND:
		{
			int index = lParam;
			if ( 2 == index )
			{
				exit( 0 );
			}
		}


		default:
			break;

	}

}


/////private/////////////////////////////////////////////////////////////////////
BOOL	CEasySmsMainWnd::SubInitialize()
{
	// sms look 
	ImagingHelper* imgNormal_look = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_MainWndSmsLookBtnDown, 
																true );
	ImagingHelper* imgPress_look = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_MainWndSmsLookBtnUp, 
																true );

	if ( NULL == imgNormal_look || NULL == imgPress_look )
	{
		return	FALSE;
	}

	m_LookSmsBtnImg.SetID( MZ_IDC_LOOK_SMS );
	m_LookSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_LookSmsBtnImg.SetImage_Normal( imgNormal_look );
	m_LookSmsBtnImg.SetImage_Pressed( imgPress_look );
	m_LookSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_LookSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL /*UI_BUTTON_IMAGE_MODE_ALWAYS_SHOW_NORMAL*/ );
	m_LookSmsBtnImg.SwapImageZOrder( true );
	m_LookSmsBtnImg.SetTextColor( RGB( 255, 255, 255 ) );
	m_LookSmsBtnImg.SetPos( 10, 10, 100, 100 );

	AddUiWin( &m_LookSmsBtnImg );

	//sms send
	ImagingHelper* imgNormal_send = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_MainWndSmsSendBtnDown, 
																true );
	ImagingHelper* imgPress_send = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_MainWndSmsSendBtnUp, 
																true );
	m_SendSmsBtnImg.SetID( MZ_IDC_SEND_SMS );
	m_SendSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_SendSmsBtnImg.SetImage_Normal( imgNormal_send );
	m_SendSmsBtnImg.SetImage_Pressed( imgPress_send );
	m_SendSmsBtnImg.EnableTextSinkOnPressed(TRUE);
	m_SendSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_SendSmsBtnImg.SwapImageZOrder( true );
	m_SendSmsBtnImg.SetTextColor( RGB( 255, 255, 255 ) );
	m_SendSmsBtnImg.SetPos( 110, 10, 100, 100 );

	AddUiWin( &m_SendSmsBtnImg );

	//ini toolbar
	m_toolBar_base.SetID(IDR_TOOLBAR_MAIN_WND);	
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 2, true, true, L"ÍË³ö" );
	AddUiWin( &m_toolBar_base );


	return	TRUE;
}