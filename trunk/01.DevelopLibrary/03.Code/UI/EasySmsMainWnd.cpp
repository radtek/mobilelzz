#include"stdafx.h"
#include "resource.h"

#include "EasySmsMainWnd.h"
#include "SmsLookCtorWnd.h"


CEasySmsMainWnd::CEasySmsMainWnd(void)
{

}

CEasySmsMainWnd::~CEasySmsMainWnd(void)
{
	m_imgContainer.RemoveAll();
}

BOOL CEasySmsMainWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}
	
	return	Initialize_Btn_Img();
}

void CEasySmsMainWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
{
	UINT_PTR id	=	LOWORD( wParam );

	switch( id )
	{
		case MZ_IDC_LOOK_SMS:
		{
			CSmsLookCtorWnd		clCSmsLookCtorWnd;
			RECT rcWork	=	MzGetWorkArea();
			clCSmsLookCtorWnd.Create(	rcWork.left, rcWork.top, RECT_WIDTH( rcWork ),
										RECT_HEIGHT( rcWork ), 0, 0, 0 );
			clCSmsLookCtorWnd.DoModal();
			break;
		}
			

		case MZ_IDC_SEND_SMS:
		{
			//CNewSmsWnd *pclSendSms	=	new	CNewSmsWnd;
			CNewSmsWnd		clSendSms;
			RECT rcWork	=	MzGetWorkArea();
			clSendSms.Create( rcWork.left, rcWork.top, RECT_WIDTH(rcWork), RECT_HEIGHT(rcWork), 0, 0, 0 );
			//pclSendSms->Show();
			clSendSms.DoModal();
			break;
		}

		default:
			break;

	}

}


/////private/////////////////////////////////////////////////////////////////////
BOOL	CEasySmsMainWnd::Initialize_Btn_Img()
{
	// sms look 
	ImagingHelper* imgNormal_look = m_imgContainer.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_MainWndSmsLookBtnDown, 
																true );
	ImagingHelper* imgPress_look = m_imgContainer.LoadImage(	MzGetInstanceHandle(), 
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
	ImagingHelper* imgNormal_send = m_imgContainer.LoadImage(	MzGetInstanceHandle(), 
																IDR_PNG_MainWndSmsSendBtnDown, 
																true );
	ImagingHelper* imgPress_send = m_imgContainer.LoadImage(	MzGetInstanceHandle(), 
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

	return	TRUE;
}