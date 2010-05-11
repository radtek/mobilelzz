#include"stdafx.h"
#include "resource.h"

#include "EasySmsMainWnd.h"
#include "SmsLookCtorWnd.h"
#include "SmsUnReadWnd.h"
#include "SmsFindWnd.h"
#include "SmsEncrytpCtorWnd.h"

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
	
	SetWindowText( L"易短信2.000" );

	return	SubInitialize();
}

void CEasySmsMainWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id	=	LOWORD( wParam );

	int iRlt	=	-1;

	switch( id )
	{
		
		case MZ_IDC_UNREAD_SMS:
		{
			CSmsUnReadWnd	clCSmsUnReadWnd;
			iRlt	=	DoModalBase( &clCSmsUnReadWnd );
			
			break;
		}

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

		case MZ_IDC_FIND_SMS:
		{
			CSmsFindWnd		clCSmsFindWnd;
			iRlt	=	DoModalBase( &clCSmsFindWnd );

			break;
		}

		case MZ_IDC_ENCRYTP_SMS:
		{
			CSmsEncrytpCtorWnd	clCSmsEncrytpCtorWnd;
			iRlt	=	DoModalBase( &clCSmsEncrytpCtorWnd );

			break;
		}
			
		case MZ_IDC_SYNC_SMS:
		{
			break;
		}

		case MZ_IDC_SETUP_SMS:
		{
			break;
		}

		case IDR_TOOLBAR_MAIN_WND:
		{
			int index = lParam;
			if ( 2 == index )
			{
				exit( 0 );
			}

			break;
		}

		default:
			break;

	}

}


/////private/////////////////////////////////////////////////////////////////////
BOOL	CEasySmsMainWnd::SubInitialize()
{

	/*设置背景图片*/
	m_modeIndex	=	0;
	m_Picture.SetID( MZ_IDC_MAIN_PICTURE );
	m_Picture.SetPos( 0, 0, GetWidth(), GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR );
	m_Picture.SetPaintMode( modeId[ m_modeIndex ] );
	m_Picture.LoadImage( MzGetInstanceHandle(), RT_RCDATA, MAKEINTRESOURCE( IDR_PNG_MAIN_WND_BACKGROUND ) );
	AddUiWin( &m_Picture );

	//SMS UnRead
	ImagingHelper* UnReadUp = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_UNREAD_UP, 
																true );
	ImagingHelper* UnReadDown = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_UNREAD_DOWN, 
																true );

	if ( NULL == UnReadUp || NULL == UnReadDown )
	{
		return	FALSE;
	}
	
	m_UnReadSmsBtnImg.SetID( MZ_IDC_UNREAD_SMS );
	m_UnReadSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnReadSmsBtnImg.SetImage_Normal( UnReadUp );
	m_UnReadSmsBtnImg.SetImage_Pressed( UnReadDown );
	m_UnReadSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnReadSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnReadSmsBtnImg.SwapImageZOrder( true );
	m_UnReadSmsBtnImg.SetPos( 30, 80, 140, 140 );
	AddUiWin( &m_UnReadSmsBtnImg );

	// sms look 
	ImagingHelper* UnSmsUp	 = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_SMS_UP, 
																true );
	ImagingHelper* UnSmsDown = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_SMS_DOWN, 
																true );
	m_LookSmsBtnImg.SetID( MZ_IDC_LOOK_SMS );
	m_LookSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_LookSmsBtnImg.SetImage_Normal( UnSmsUp );
	m_LookSmsBtnImg.SetImage_Pressed( UnSmsDown );
	m_LookSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_LookSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_LookSmsBtnImg.SwapImageZOrder( true );
	m_LookSmsBtnImg.SetPos( 170, 80, 140, 140 );

	AddUiWin( &m_LookSmsBtnImg );

	//sms new

	ImagingHelper* UnNewUp	 = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_NEW_UP, 
																true );

	ImagingHelper* UnNewDown = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_NEW_DOWN, 
																true );

	m_SendSmsBtnImg.SetID( MZ_IDC_SEND_SMS );
	m_SendSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_SendSmsBtnImg.SetImage_Normal( UnNewUp );
	m_SendSmsBtnImg.SetImage_Pressed( UnNewDown );
	m_SendSmsBtnImg.EnableTextSinkOnPressed(TRUE);
	m_SendSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_SendSmsBtnImg.SwapImageZOrder( true );
	m_SendSmsBtnImg.SetPos( 310, 80, 140, 140 );

	AddUiWin( &m_SendSmsBtnImg );

	//find

	ImagingHelper* UnFindUp	 = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_FIND_UP, 
																true );

	ImagingHelper* UnFindDown = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_FIND_DOWN, 
																true );

	m_UnFindSmsBtnImg.SetID( MZ_IDC_FIND_SMS );
	m_UnFindSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnFindSmsBtnImg.SetImage_Normal( UnFindUp );
	m_UnFindSmsBtnImg.SetImage_Pressed( UnFindDown );
	m_UnFindSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnFindSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnFindSmsBtnImg.SwapImageZOrder( true );
	m_UnFindSmsBtnImg.SetPos( 30, 260, 140, 140 );

	AddUiWin( &m_UnFindSmsBtnImg );

	//加密
	ImagingHelper* UnEncryptUp	 = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																	IDR_PNG_ENCRYPT_UP, 
																	true );

	ImagingHelper* UnEncryptDown = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																	IDR_PNG_ENCRYPT_DOWN, 
																	true );
	m_UnEncryptSmsBtnImg.SetID( MZ_IDC_ENCRYTP_SMS );
	m_UnEncryptSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnEncryptSmsBtnImg.SetImage_Normal( UnEncryptUp );
	m_UnEncryptSmsBtnImg.SetImage_Pressed( UnEncryptDown );
	m_UnEncryptSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnEncryptSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnEncryptSmsBtnImg.SwapImageZOrder( true );
	m_UnEncryptSmsBtnImg.SetPos( 30, 440, 140, 140 );

	AddUiWin( &m_UnEncryptSmsBtnImg );

	//synchronization

	ImagingHelper* UnSyncUp	 = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_SYNC_UP, 
																true );

	ImagingHelper* UnSyncDown = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_SYNC_DOWN, 
																true );

	m_UnSyncSmsBtnImg.SetID( MZ_IDC_SYNC_SMS );
	m_UnSyncSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnSyncSmsBtnImg.SetImage_Normal( UnSyncUp );
	m_UnSyncSmsBtnImg.SetImage_Pressed( UnSyncDown );
	m_UnSyncSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnSyncSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnSyncSmsBtnImg.SwapImageZOrder( true );
	m_UnSyncSmsBtnImg.SetPos( 170, 260, 140, 140 );

	AddUiWin( &m_UnSyncSmsBtnImg );
	
	//设置

	ImagingHelper* UnSetUpUp	 = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																	IDR_PNG_SETUP_UP, 
																	true );

	ImagingHelper* UnSetUpDown = m_imgContainer_base.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_SETUP_DOWN, 
																true );

	m_UnSetUpSmsBtnImg.SetID( MZ_IDC_SETUP_SMS );
	m_UnSetUpSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnSetUpSmsBtnImg.SetImage_Normal( UnSetUpUp );
	m_UnSetUpSmsBtnImg.SetImage_Pressed( UnSetUpDown );
	m_UnSetUpSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnSetUpSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnSetUpSmsBtnImg.SwapImageZOrder( true );
//	m_UnSetUpSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );

	m_UnSetUpSmsBtnImg.SetPos( 310, 260, 140, 140 );

	AddUiWin( &m_UnSetUpSmsBtnImg );

	//ini toolbar
	m_toolBar_base.SetID( IDR_TOOLBAR_MAIN_WND );	
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 2, true, true, L"退出" );
	AddUiWin( &m_toolBar_base );

	return	TRUE;
}