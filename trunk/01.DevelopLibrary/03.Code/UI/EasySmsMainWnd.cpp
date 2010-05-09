#include"stdafx.h"
#include "resource.h"

#include "EasySmsMainWnd.h"
#include "SmsLookCtorWnd.h"
#include "SmsUnReadWnd.h"
#include "SmsFindWnd.h"
#include "SmsEncrytpCtorWnd.h"

//显示图片的不同模式
static DWORD modeId[4]=
{
	MZ_PAINTMODE_NORMAL,                    //正常显示
	MZ_PAINTMODE_TILE,                      //平铺
	MZ_PAINTMODE_STRETCH_H,                 //水平方向拉伸 (不设置边距)
	MZ_PAINTMODE_STRETCH_H                  //水平方向拉伸 (设置边距)
};


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
	
	long	lW			=	GetWidth();
	long	lH			=	( GetHeight() * 2 ) / 3;
	long	lReserveX	=	20;
	long	lReserveY	=	30;

	long	lSpaceX		=	( lW - 2 * lReserveX) / 3 + 20;
	long	lSpaceY		=	( lH - 2 * lReserveY) / 3;
	int		i			=	0;
	int		j			=	0;
	/*设置背景图片*/
	m_modeIndex	=	0;
	m_Picture.SetID( MZ_IDC_MAIN_PICTURE );
	m_Picture.SetPos( 0, 0, GetWidth(), GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR );
	m_Picture.SetPaintMode( modeId[ m_modeIndex ] );
	m_Picture.LoadImage( MzGetInstanceHandle(), RT_RCDATA, MAKEINTRESOURCE( IDR_PNG_MAIN_WND_BACKGROUND ) );
	AddUiWin( &m_Picture );

	
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
	//SMS UnRead
	m_UnReadSmsBtnImg.SetID( MZ_IDC_UNREAD_SMS );
	m_UnReadSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnReadSmsBtnImg.SetImage_Normal( imgNormal_look );
	m_UnReadSmsBtnImg.SetImage_Pressed( imgPress_look );
	m_UnReadSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnReadSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnReadSmsBtnImg.SwapImageZOrder( true );
	m_UnReadSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );
	m_UnReadSmsBtnImg.SetText( L"未读" );
	m_UnReadSmsBtnImg.SetPos( lReserveX + lSpaceX * i, lReserveY + lSpaceY * j, 100, 100 );

	AddUiWin( &m_UnReadSmsBtnImg );
	++i;

	// sms look 
	m_LookSmsBtnImg.SetID( MZ_IDC_LOOK_SMS );
	m_LookSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_LookSmsBtnImg.SetImage_Normal( imgNormal_look );
	m_LookSmsBtnImg.SetImage_Pressed( imgPress_look );
	m_LookSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_LookSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_LookSmsBtnImg.SwapImageZOrder( true );
	m_LookSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );
	m_LookSmsBtnImg.SetText( L"阅读" );
	m_LookSmsBtnImg.SetPos( lReserveX + lSpaceX * i, lReserveY + lSpaceY * j, 100, 100 );

	AddUiWin( &m_LookSmsBtnImg );
	++i;

	//sms new
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
	m_SendSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );
	m_SendSmsBtnImg.SetPos( lReserveX + lSpaceX * i, lReserveY + lSpaceY * j, 100, 100 );
	m_SendSmsBtnImg.SetText( L"发送" );

	AddUiWin( &m_SendSmsBtnImg );
	i = 0;
	++j;

	//find
	m_UnFindSmsBtnImg.SetID( MZ_IDC_FIND_SMS );
	m_UnFindSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnFindSmsBtnImg.SetImage_Normal( imgNormal_look );
	m_UnFindSmsBtnImg.SetImage_Pressed( imgPress_look );
	m_UnFindSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnFindSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnFindSmsBtnImg.SwapImageZOrder( true );
	m_UnFindSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );
	m_UnFindSmsBtnImg.SetText( L"查找" );
	m_UnFindSmsBtnImg.SetPos( lReserveX + lSpaceX * i, lReserveY + lSpaceY * j, 100, 100 );

	AddUiWin( &m_UnFindSmsBtnImg );
	++i;

	//加密

	m_UnEncryptSmsBtnImg.SetID( MZ_IDC_ENCRYTP_SMS );
	m_UnEncryptSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnEncryptSmsBtnImg.SetImage_Normal( imgNormal_look );
	m_UnEncryptSmsBtnImg.SetImage_Pressed( imgPress_look );
	m_UnEncryptSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnEncryptSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnEncryptSmsBtnImg.SwapImageZOrder( true );
	m_UnEncryptSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );
	m_UnEncryptSmsBtnImg.SetText( L"加密" );
	m_UnEncryptSmsBtnImg.SetPos( lReserveX + lSpaceX * i, lReserveY + lSpaceY * j, 100, 100 );

	AddUiWin( &m_UnEncryptSmsBtnImg );
	++i;

	//synchronization

	m_UnSyncSmsBtnImg.SetID( MZ_IDC_SYNC_SMS );
	m_UnSyncSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnSyncSmsBtnImg.SetImage_Normal( imgNormal_look );
	m_UnSyncSmsBtnImg.SetImage_Pressed( imgPress_look );
	m_UnSyncSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnSyncSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnSyncSmsBtnImg.SwapImageZOrder( true );
	m_UnSyncSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );
	m_UnSyncSmsBtnImg.SetText( L"同步" );
	m_UnSyncSmsBtnImg.SetPos( lReserveX + lSpaceX * i, lReserveY + lSpaceY * j, 100, 100 );

	AddUiWin( &m_UnSyncSmsBtnImg );
	
	i = 0;
	++j;
	//设置

	m_UnSetUpSmsBtnImg.SetID( MZ_IDC_SETUP_SMS );
	m_UnSetUpSmsBtnImg.SetButtonType( MZC_BUTTON_NONE );
	m_UnSetUpSmsBtnImg.SetImage_Normal( imgNormal_look );
	m_UnSetUpSmsBtnImg.SetImage_Pressed( imgPress_look );
	m_UnSetUpSmsBtnImg.EnableTextSinkOnPressed( TRUE );
	m_UnSetUpSmsBtnImg.SetMode( UI_BUTTON_IMAGE_MODE_NORMAL );
	m_UnSetUpSmsBtnImg.SwapImageZOrder( true );
	m_UnSetUpSmsBtnImg.SetTextColor( RGB( 0, 0, 0 ) );
	m_UnSetUpSmsBtnImg.SetText( L"同步" );
	m_UnSetUpSmsBtnImg.SetPos( lReserveX + lSpaceX * i, lReserveY + lSpaceY * j, 100, 100 );

	AddUiWin( &m_UnSetUpSmsBtnImg );
	++i;

	//ini toolbar
	m_toolBar_base.SetID( IDR_TOOLBAR_MAIN_WND );	
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 2, true, true, L"退出" );
	AddUiWin( &m_toolBar_base );


	return	TRUE;
}