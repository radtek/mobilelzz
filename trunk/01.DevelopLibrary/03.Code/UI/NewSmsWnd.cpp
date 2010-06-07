#include "stdafx.h"
#include "resource.h"
#include "UiEditControl.h"
#include "ContactorsWnd.h"
#include "NewSmsWnd.h"
#include "UsbNotifyApi.h"
#include "CallNotifyApi.h"

#include <sms.h>
#pragma comment(lib,"sms.lib")

#include <winreg.h>
#include "RegOperator.h"
#include "CoreService.h"

INT g_iUsbNotifyMsg = 0;
CNewSmsWnd::CNewSmsWnd()
{
	m_lCurProgress = 0;
	m_SmsMsgEdit = new CMyEdit;
	m_pclContactorsWnd  = NULL;	
}
CNewSmsWnd::~CNewSmsWnd()
{
	if ( NULL != m_SmsMsgEdit )
	{
		delete m_SmsMsgEdit;
		m_SmsMsgEdit	=	NULL;
	}
	if(NULL != m_pclContactorsWnd )
	{
		delete m_pclContactorsWnd ;
		m_pclContactorsWnd  = NULL;	
	}
	MzCloseSip();
	MzAccClose(); 
	m_imgContainer.RemoveAll();
}

BOOL CNewSmsWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"易短信" );
	g_iUsbNotifyMsg	=	RegisterUsbNotifyMsg();

	MzAccOpen();										//打开重力感应设备

	m_accMsg = MzAccGetMessage();						//获得重力感应改变的消息

	// 初始化收件人控件，并添加到窗口中	
	RECT	rect	=	MzGetVisibleDesktopRect();
	RECT	rc		=	{0};
	int		height	=	0;
	int		width	=	0;
	HWND	hWnd	=	FindWindow( L"CTaskBar", 0 );
	if( hWnd != 0 )
	{
		::GetWindowRect( hWnd, &rc );
		height	=	rc.bottom	-	rc.top;
		width	=	rc.right	-	rc.left;
	}

	if( width > 480 )
	{
		g_bH = TRUE;
		long	lWidth	=	GetWidth();
		long	lHeight	=	GetHeight();
		RECT	rc		=	MzGetWindowRect();
		RECT	rc2		=	MzGetClientRect();
		RECT	rc3		=	MzGetWorkArea();

		SetWindowPos( m_hWnd, rc.left, rc.top, RECT_HEIGHT(rc) + rc.top, RECT_WIDTH( rc )  );
// 		lWidth			=	GetWidth();
// 		lHeight			=	GetHeight();
		m_Recievers.SetPos( 2, 0, GetWidth() - BUTTON_WIDTH_H - 2, BUTTON_HEIGHT_VH );

		m_SmsMsgEdit->SetPos(	2, m_Recievers.GetHeight() + 3, GetWidth() - 4, 
								( GetHeight() - m_Recievers.GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR ) );	//add toolbar by zhu.t
//add by zhu.t for toobar
// 		m_toolBar_base.SetPos( 2, m_Recievers.GetHeight() + 3 + m_SmsMsgEdit->GetHeight(), 
// 								GetWidth() - 4, MZM_HEIGHT_TEXT_TOOLBAR );
		m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
//

		m_SendSmsBtn.SetPos( ( GetWidth() - BUTTON_WIDTH_H ), 0, BUTTON_WIDTH_H - 2, BUTTON_HEIGHT_VH );
	}
	else
	{
		long lWidth		=	GetWidth();
		long lHeight	=	GetHeight();
		RECT	rc		=	MzGetWindowRect();
		RECT	rc2		=	MzGetClientRect();
		RECT	rc3		=	MzGetWorkArea();
		SetWindowPos( m_hWnd, rc3.left, rc3.top, RECT_WIDTH( rc3 ), RECT_HEIGHT( rc3 ) );

		m_Recievers.SetPos( 2, 0, lWidth - BUTTON_WIDTH_V - 2, BUTTON_HEIGHT_VH );
		m_SmsMsgEdit->SetPos(	 2, m_Recievers.GetHeight() + 3, GetWidth() - 4,
								(lHeight - m_Recievers.GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR ) ); //add toolbar by zhu.t
		//add by zhu.t for toobar
// 		m_toolBar_base.SetPos( 2, m_Recievers.GetHeight() + 3 + m_SmsMsgEdit->GetHeight(), 
// 								GetWidth() - 4, MZM_HEIGHT_TEXT_TOOLBAR );
		m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
		//

		m_SendSmsBtn.SetPos( ( GetWidth() - BUTTON_WIDTH_V ), 0, BUTTON_WIDTH_V - 2, BUTTON_HEIGHT_VH );
	}

	ImagingHelper* imgNormal	=	m_imgContainer.LoadImage( MzGetInstanceHandle(), IDR_PNG_EMAILICON, true );


	// 初始化窗口中的UiButton_Image按钮控件
	m_SendSmsBtn.SetID( MZ_IDC_SEND_SMS_BTN );
	m_SendSmsBtn.SetButtonType( MZC_BUTTON_NONE );
	m_SendSmsBtn.SetImage_Normal( imgNormal );
	m_SendSmsBtn.SetMode( UI_BUTTON_IMAGE_MODE_ALWAYS_SHOW_NORMAL );
	m_SendSmsBtn.SwapImageZOrder( true );
	m_SendSmsBtn.SetTextColor( RGB( 255,255,255 ) );

	AddUiWin( &m_SendSmsBtn );

	m_Recievers.SetTip( L"联系人编辑区域:" );					// set the tips text
	m_Recievers.SetID( MZ_IDC_RECIEVERS_EDIT );
	m_Recievers.SetEditBgType( UI_EDIT_BGTYPE_ROUND_RECT ); 
	m_Recievers.EnableInsideScroll( true );
	m_Recievers.EnableZoomIn( true );
	m_Recievers.EnableRichTextFormat(true);
	m_Recievers.SetFontSize(20);
	m_Recievers.SetTopInvalid(5);
	m_Recievers.SetBottomInvalid(5);
	AddUiWin( &m_Recievers ); 
	// 初始化短信文本控件，并添加到窗口中
	
	m_SmsMsgEdit->SetSipMode( IM_SIP_MODE_GEL_PY, MZM_HEIGHT_TEXT_TOOLBAR );
	m_SmsMsgEdit->SetTextColor( RGB( 94, 94, 94 ) );			// you could also set the color of text
	m_SmsMsgEdit->SetEditBgType( UI_EDIT_BGTYPE_ROUND_RECT ); 
	m_SmsMsgEdit->SetColorBg( RGB( 250, 250, 250 ) );
	m_SmsMsgEdit->EnableInsideScroll( true );
	//m_SmsMsgEdit->EnableZoomIn( true );   
	m_SmsMsgEdit->SetTip( L"在这里输入短信内容" );

	AddUiWin( m_SmsMsgEdit );

	m_smsMsg			=	GetSmsRegisterMessage();

	WinManager* pMng	=	GetWinManager();
	pMng->SetFocusedWinBeforeDeactivate( m_SmsMsgEdit );

	DWORD lReadMessageThreadThreadID	=	0;
	m_uShowNotifyWnd	=	GetShellNotifyMsg_ShowNotifyWnd();
	RegisterShellMessage( m_hWnd, WM_MZSH_SHOW_NOTIFY_WND );

	//back
// 	ImagingHelper* imgNormalBack	=	m_imgContainer.LoadImage(MzGetInstanceHandle(), 
// 										/*IDR_PNG_EditWndBack*/IDR_PNG_MainWndSmsSendBtnDown, true );

	// zhu.t add 初始化窗口中的Toolbar控件
	m_toolBar_base.SetID( MZ_IDC_NEWSMS_TOOLBAR );
	m_toolBar_base.SetButton( 1, true, true, L"联系人" );
	m_toolBar_base.SetButton( 2, true, true, L"返回" );
	m_toolBar_base.EnablePressedHoldSupport( true );
	m_toolBar_base.SetPressedHoldTime( 1000 );
	m_toolBar_base.EnableNotifyMessage( true );

	AddUiWin( &m_toolBar_base );


	return TRUE;
}

void CNewSmsWnd::OnMzCommand(WPARAM wParam, LPARAM lParam)
{
	UINT_PTR id	=	LOWORD( wParam );
	switch( id )
	{
		case MZ_IDC_SEND_SMS_BTN:
		{		
// 			if ( !Normal() )
// 			{
// 				MzMessageBoxEx( NULL, L"试用达到最大限制,谢谢您的试用!", MB_OK );
// 				return;
// 			}

			RECT	rect	=	MzGetVisibleDesktopRect();
			RECT	rc		=	{0};
			int		height	=	0;
			int		width	=	0;
			HWND	hWnd	=	FindWindow(L"CTaskBar", 0);
			if( hWnd != 0 )
			{
				::GetWindowRect( hWnd, &rc );
				height	=	rc.bottom	-	rc.top;
				width	=	rc.right	-	rc.left;
			}

			if( width > 480 )
			{
				m_progress.SetPos( 200, 100, 300, 150 );
			}
			else
			{
				m_progress.SetPos( 100, 200, 150, 300 );
			}

			 m_lCurProgress	=	0;
			
            // 初始化 MzPopupProgress 控件
            m_progress.SetRange( 0, 200 );
            m_progress.SetCurrentValue( m_lCurProgress );
            m_progress.SetNoteText( L"短信发送中" );
            m_progress.StartProgress( m_hWnd, TRUE, FALSE );
            
            SetTimer( m_hWnd, PROGRESS_TIMER_ID, 100, NULL );			// 开启定时器，每100ms刷新一次进度条
			DWORD	lThreadId	=	0;
			CreateThread( NULL, 0, ProxyRun, (void*)this, 0, &lThreadId );
			
			break;
		}

		case MZ_IDC_NEWSMS_TOOLBAR:
		{
			int nIndex	=	lParam;
			if ( 2 == nIndex )
			{
				g_ReciversList.Clear();
				this->EndModal( ID_CANCEL );	//退出
				/*this->DestroyWindow();
				exit(1);*/
			}else if ( 1 == nIndex )
			{	//联系人
				if(NULL != m_pclContactorsWnd )
				{
					delete m_pclContactorsWnd ;
					m_pclContactorsWnd  = NULL;
				}
				m_Recievers.UpdateContactors();
				m_pclContactorsWnd = new CContactorsWnd;
				m_pclContactorsWnd->SetParent(&m_Recievers);
				RECT rcWork = MzGetWorkArea();
				m_pclContactorsWnd->Create(rcWork.left,rcWork.top,RECT_WIDTH(rcWork),RECT_HEIGHT(rcWork), 0, 0, 0, 0);

				m_pclContactorsWnd->Show();
			}

		}

	  break;
	}

}




LRESULT CNewSmsWnd::MzDefWndProc( UINT message, WPARAM wParam, LPARAM lParam )
{
	switch( message )
	{
		
		case MZ_WM_MOUSE_NOTIFY:
		{
			if (LOWORD(wParam) == m_toolBar_base.GetID() )
			{
				int nNotify = HIWORD(wParam);
				switch(nNotify)
				{
				case MZ_MN_PRESSEDHOLD_TIMEUP:
					{
						POINT PT	=	m_toolBar_base.GetMouseDownPos ();

						int	nIndex	=	m_toolBar_base.CalcIndexOfPos( PT.x, PT.y );

						DoSthForTooBarHoldPress( nIndex );

					}
					break;

				default:
					break;
				}

			}
		}
		//case WM_IM_INFO:
		//{
		//	if ( wParam == IM_WIDEIMAGE ){

		//	}
		//	BOOL b = SipRegisterNotification(m_hWnd);
		//	break;
		//}
		
		default:
		{
			if ( message == m_accMsg )
			{
			// 转屏
				switch( wParam )
				{
					case SCREEN_PORTRAIT_P:
					{
						MzChangeDisplaySettingsEx( DMDO_90 );
						g_bH	=	TRUE;
					}
					break;

					case SCREEN_PORTRAIT_N:
					{
						MzChangeDisplaySettingsEx( DMDO_270 );
						g_bH	=	FALSE;
					}
					break;

					case SCREEN_LANDSCAPE_N:
					{
						MzChangeDisplaySettingsEx( DMDO_180 );
						g_bH = TRUE;
					}
					break;

					case SCREEN_LANDSCAPE_P:
					{
						MzChangeDisplaySettingsEx( DMDO_0 );
						g_bH	=	FALSE;
					}
					break;

				}
			}
			else if( message == g_iUsbNotifyMsg )
			{
				INT iEvenType	=	( INT )wParam;
				if( wParam == USB_MASSSTORAGE_ATTACH )
				{
					exit(0);
				}
			}
			else if( message == m_smsMsg )
			{
				ReadMessage();
			}
			else if( message == m_uShowNotifyWnd )
			{
				if ( LOWORD( wParam ) == 2 )
				{
					ReadMessage();
				}    
			}
		}

		break;
	}

	return CMzWndEx::MzDefWndProc(message,wParam,lParam);
}

void CNewSmsWnd::ReadMessage()
{
	HRESULT				hRes				=	S_OK; 
	SMS_HANDLE			smsHandle			=	NULL; 
	//SMS_ADDRESS			smsaDestination; 
	SMS_MESSAGE_ID		smsmidMessageID		=	0; 

	hRes	=	SmsOpen( SMS_MSGTYPE_TEXT, SMS_MODE_RECEIVE, &smsHandle, NULL );
	if ( FAILED( hRes ) ) 
	{ 
	//	return   false; 
	}

	long	lDataSize	=	0;
	SmsGetMessageSize( smsHandle, (DWORD *const)&lDataSize );
	wchar_t		strMessage[512]	=	L"";
	SYSTEMTIME stTime;
	memset( &stTime, 0x0, sizeof(SYSTEMTIME) );
	GetSystemTime( &stTime );
	stTime.wHour			=	0;
	stTime.wMilliseconds	=	0;
	stTime.wMinute			=	0;
	stTime.wSecond			=	0;

	TEXT_PROVIDER_SPECIFIC_DATA		tpsd;
	memset( &tpsd, 0x0, sizeof( tpsd ) );

	tpsd.dwMessageOptions	=	PS_MESSAGE_OPTION_NONE; 
	tpsd.dwHeaderDataSize	=	0; 
	tpsd.fMessageContainsEMSHeaders	=	FALSE; 
	tpsd.dwProtocolID		=	SMS_MSGPROTOCOL_UNKNOWN; 
	tpsd.psReplaceOption	=	PSRO_NONE; 

	long	lReadSize	=	0;	
	hRes	=	SmsReadMessage( smsHandle, 0, 0, &stTime, (PBYTE)strMessage, 1024, (PBYTE)&tpsd,
								sizeof(TEXT_PROVIDER_SPECIFIC_DATA), (DWORD *)&lReadSize ); 
	if( FAILED( hRes ) ) 
	{ 
		//	return   false; 
	}

	m_SmsMsgEdit->SetText( strMessage );
	SmsClose( smsHandle );
}

// 转屏后如果需要调整窗口的位置，重载此函数响应 WM_SETTINGCHANGE 消息
void CNewSmsWnd::OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName)
{
  //设置新的屏幕方向的窗口大小及控件位置
  DEVMODE	devMode;
  memset( &devMode, 0, sizeof( DEVMODE ) );
  devMode.dmSize	=	sizeof( DEVMODE );
  devMode.dmFields	=	DM_DISPLAYORIENTATION;
  ChangeDisplaySettingsEx( NULL, &devMode, NULL, CDS_TEST, NULL );

 //竖屏
  if ( devMode.dmDisplayOrientation == DMDO_90 || devMode.dmDisplayOrientation == DMDO_270 )
  {
		g_bH = TRUE;
		RECT rc = MzGetWorkArea();
		//modify begin by zhaodsh  2010/03/21 12:27
		SetWindowPos( m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		// modify end 2010/03/21 12:27
		
		m_Recievers.SetPos( 2, 0, GetWidth() - BUTTON_WIDTH_V - 2, BUTTON_HEIGHT_VH );

		m_SmsMsgEdit->SetPos(	2, m_Recievers.GetHeight() + 3, GetWidth() - 4, 
								(GetHeight() - m_Recievers.GetHeight() ) );

		m_SendSmsBtn.SetPos( ( GetWidth() - BUTTON_WIDTH_V ), 0, BUTTON_WIDTH_V - 2, BUTTON_HEIGHT_VH );
  }

  //横屏
	if ( devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0 )
	{
		g_bH		=	FALSE;
		RECT	rc	=	MzGetWorkArea();

		SetWindowPos( m_hWnd, rc.left, rc.top, RECT_WIDTH(rc), RECT_HEIGHT(rc) );

		m_Recievers.SetPos( 2, 0, GetWidth() - BUTTON_WIDTH_H - 2, BUTTON_HEIGHT_VH) ;

		m_SmsMsgEdit->SetPos(	2, m_Recievers.GetHeight() + 3, GetWidth() - 4,
								( GetHeight() - m_Recievers.GetHeight() ) );

		m_SendSmsBtn.SetPos( ( GetWidth() - BUTTON_WIDTH_H ), 0, BUTTON_WIDTH_H - 2,BUTTON_HEIGHT_VH );
	}
}

void CNewSmsWnd::UpdateData( MyListItemData* pRecivers,long lReciversCount )
{
}

bool CNewSmsWnd::SendSMS(IN LPCTSTR lpNumber,IN LPCTSTR lpszMessage)
{
    HRESULT							hRes; 
    SMS_HANDLE						smsHandle		=	NULL; 
    SMS_ADDRESS						smsaDestination; 
    SMS_MESSAGE_ID					smsmidMessageID	=	0; 
    TEXT_PROVIDER_SPECIFIC_DATA		tpsd; 

    hRes	=	SmsOpen( SMS_MSGTYPE_TEXT, SMS_MODE_SEND, &smsHandle, NULL ); 
    if ( FAILED ( hRes ) ) 
    { 
		return	false; 
    } 

    //地址方式注意国内号码前加"+86"
    smsaDestination.smsatAddressType	=	SMSAT_INTERNATIONAL; 
    _tcsncpy( smsaDestination.ptsAddress, lpNumber, SMS_MAX_ADDRESS_LENGTH ); 

    tpsd.dwMessageOptions	=	PS_MESSAGE_OPTION_NONE; 
    //tpsd.dwMessageOptions =  PS_MESSAGE_OPTION_STATUSREPORT;//表示需要状态报告
    tpsd.psMessageClass		=	PS_MESSAGE_CLASS1; 
    //PS_MESSAGE_CLASS0表示短信在被接收后立即显示且不存储在收件箱(称为闪信) 
    //PS_MESSAGE_CLASS1表示一般的情况，被接收后存储到收件箱并发送一个确认回短信中心，发送方收到一个已被接收的状态报告。

    ZeroMemory( tpsd.pbHeaderData, sizeof( tpsd.pbHeaderData ) );         
    tpsd.dwHeaderDataSize			=	0; 
    tpsd.fMessageContainsEMSHeaders	=	FALSE; 
    tpsd.dwProtocolID				=	SMS_MSGPROTOCOL_UNKNOWN; 
    tpsd.psReplaceOption			=	PSRO_NONE; 


    hRes= SmsSendMessage(	smsHandle,
							NULL,   
							&smsaDestination,   
							NULL, 
							(PBYTE)lpszMessage,   
							_tcslen(lpszMessage) *  sizeof(TCHAR),   
							(PBYTE)&tpsd, 
							sizeof(TEXT_PROVIDER_SPECIFIC_DATA),   
							SMSDE_OPTIMAL,   
							SMS_OPTION_DELIVERY_NONE, 
							&smsmidMessageID ); 

    SmsClose(smsHandle);

    if ( SUCCEEDED( hRes ) ) 
    { 			
		return true;
    } 
    else 
    { 
		return false;
    } 
} 



/*
 *拆分短信发送
*/
bool CNewSmsWnd::SendSMS_Wrapper( IN CMzString& Number )
{
	bool		SendFlag	=	false;
	CMzString	NewNumber;

	if( Number.C_Str()[ 0 ] == L'1' )
	{
		NewNumber	=	L"+86";
		NewNumber	+=	Number;
	}
	else
	{
		NewNumber	=	Number;
	}

	CMzString	SMS_Content	=	m_SmsMsgEdit->GetText().C_Str();

	int length	=	SMS_Content.Length();
	if( length <= 69 )
	{
	 	SendFlag = SendSMS( NewNumber.C_Str(), m_SmsMsgEdit->GetText().C_Str() );
	}
	else
	{
		for(int i = 0; i < length; i += 69 )
		{
			int count	=	((length - i) < 69 ? (length-i): 69); 
			CMzString Single_Content =SMS_Content.SubStr(i,count);

			SendFlag	=	SendSMS(NewNumber.C_Str(), Single_Content.C_Str() );
		}	
	}
	if ( /*SendFlag*/1 ){
		///////////////////////////////SaveMessage///////////////////////////////////////////
		wchar_t	*pBuf		=	NULL;
		long		lSize		=	0;
		wchar_t	*pwcResult	=	NULL;

		HRESULT	hr	=	m_clCEasySmsUiCtrl.MakeSendSmsInfo( &pBuf, &lSize, m_SmsMsgEdit->GetText().C_Str(), Number.C_Str() );
		//if ( FAILED ( hr ) )									return	_ASSERT ( 0 );

		CCoreService	*pCCoreService	=	CCoreService::GetInstance();
		//if ( NULL == pCCoreService )							return	_ASSERT ( 0 );

		hr	=	pCCoreService->Request( pBuf, &pwcResult );
		//if ( FAILED ( hr ) )									return	_ASSERT ( 0 );

		//////////////////////////////////////////////////////////////////////////
	}

	return SendFlag;
}

// 定时器触发的消息函数
void CNewSmsWnd::OnTimer(UINT_PTR nIDEvent)
{
	if ( PROGRESS_TIMER_ID == nIDEvent )
	{
		// 刷新进度条
		m_progress.SetCurrentValue( ++m_lCurProgress );

		m_progress.UpdateProgress( FALSE );

		if ( 200 == m_lCurProgress )
		{
			KillTimer( m_hWnd, PROGRESS_TIMER_ID );
			m_progress.KillProgress();
		}
	}
}

DWORD CNewSmsWnd::ProxyRun(LPVOID lp)
{
	CNewSmsWnd*  Wnd	=	(CNewSmsWnd*)lp;
	Wnd->Run();

	return 0;
}


void	CNewSmsWnd::Run()
{
	m_Recievers.UpdateRecievers();
	int		n			=	g_ReciversList.GetItemCount();
	bool	SendFlag	=	FALSE;
	for(int i = 0; i < n; i++ )
	{
		MyListItemData*		pMyListItemData		=	NULL;
		g_ReciversList.GetItem( i, &pMyListItemData );

		CMzString	Number	=	pMyListItemData->StringDescription;
		
		SendFlag	=	SendSMS_Wrapper( Number );
	}

	KillTimer( m_hWnd, PROGRESS_TIMER_ID );

	m_progress.KillProgress();
	if ( SendFlag )
	{
		MzMessageBoxEx(NULL,L"短信已发送完毕",MB_OK);
	}

	g_ReciversList.Clear();
	m_Recievers.SetText(L"");
	m_SmsMsgEdit->SetText(L"");
	UpdateWindow();
}

//void CNewSmsWnd::OnLButtonUp ( UINT fwKeys, int  xPos, int yPos )
//{
//	m_Recievers.OnLButtonUp123( fwKeys, xPos, yPos );
//	m_SmsMsgEdit->OnLButtonUp123( fwKeys, xPos, yPos );
//	
//	return CMzWndEx::OnLButtonUp( fwKeys, xPos, yPos );
//}

DWORD CNewSmsWnd::ReadMessage( LPVOID lpParameter )
{
	CNewSmsWnd*	pNewSmsWnd	=	(CNewSmsWnd*)lpParameter;
	SMS_ADDRESS smsaDestination;

	TEXT_PROVIDER_SPECIFIC_DATA tpsd;

	SMS_HANDLE smshHandle;
	HANDLE hRead	=	CreateEvent (NULL, FALSE, FALSE, NULL);
	// Open an SMS Handle
	HRESULT hr	=	SmsOpen ( SMS_MSGTYPE_TEXT, SMS_MODE_RECEIVE, &smshHandle, &hRead );
	if ( hr != ERROR_SUCCESS)
	{
		return 0;
	}

	while ( 1 )
	{
		// Wait for message to come in.
		int		rc	=	WaitForSingleObject( hRead, INFINITE );
		if ( rc != WAIT_OBJECT_0 )
		{
			SmsClose (smshHandle );
			return 0;
		}

		memset ( &smsaDestination, 0, sizeof ( smsaDestination ) );
		DWORD dwSize, dwRead	=	0;

		hr	=	SmsGetMessageSize ( smshHandle, &dwSize );
		if (hr != ERROR_SUCCESS) 
		{
			dwSize	=	1024;
			return 0;
		}   

		char *pMessage	=	(char *)malloc ( dwSize + 1 );
		memset ( &tpsd, 0, sizeof ( tpsd ) );

		hr	=	SmsReadMessage(	smshHandle, NULL, &smsaDestination, NULL, (PBYTE)pMessage,
								dwSize,(PBYTE)&tpsd, sizeof(TEXT_PROVIDER_SPECIFIC_DATA), &dwRead );
		if (hr == ERROR_SUCCESS)
		{
			pNewSmsWnd->m_SmsMsgEdit->SetText( (wchar_t*) pMessage );
		} 

		free( pMessage );
	}

	SmsClose( smshHandle );

	return 0;
	
}

BOOL CNewSmsWnd::Normal()
{
	CRegOperator clRegTest;
	//long lKeyStatus1 = 0;
	//clRegTest.CreateKey(HKEY_LOCAL_MACHINE, L"Software\\EasySMS", &lKeyStatus1);
	//long l = 1;
	//clRegTest.SetValue(L"Identify1", REG_DWORD, (char*)&l, sizeof(l));
	//l = 2;
	//clRegTest.SetValue(L"Identify2", REG_DWORD, (char*)&l, sizeof(l));
	//l = 3;
	//clRegTest.SetValue(L"Identify3", REG_DWORD, (char*)&l, sizeof(l));
	//l = 4;
	//clRegTest.SetValue(L"Identify4", REG_DWORD, (char*)&l, sizeof(l));
	//l = 5;
	//clRegTest.SetValue(L"Identify5", REG_DWORD, (char*)&l, sizeof(l));
	clRegTest.OpenKey( HKEY_LOCAL_MACHINE, L"Software\\EasySMS" );
	clRegTest.DeleteValue( L"Identify1" );

	HRESULT	hr		=	E_FAIL;
	CRegOperator	clReg;
	long lKeyStatus	=	0;
	hr	=	clReg.OpenKey( HKEY_LOCAL_MACHINE, L"Software\\Microsoft\\WindowsPPC" );
	if ( FAILED(hr) )
	{
		return FALSE;
	}
	long lIdentify1 = 0;
	hr = clReg.GetValue( L"Identify1", REG_DWORD, (char*)&lIdentify1, sizeof( lIdentify1 ) );
	if ( FAILED(hr) )
	{
		return FALSE;
	}
	long lIdentify2 = 0;
	hr = clReg.GetValue(L"Identify2", REG_DWORD, (char*)&lIdentify2, sizeof(lIdentify2));
	if ( FAILED(hr) )
	{
		return FALSE;
	}
	long lIdentify3 = 0;
	hr = clReg.GetValue( L"Identify3", REG_DWORD, (char*)&lIdentify3, sizeof( lIdentify3 ) );
	if ( FAILED(hr) )
	{
		return FALSE;
	}
	long lIdentify4 = 0;
	hr = clReg.GetValue(L"Identify4", REG_DWORD, (char*)&lIdentify4, sizeof(lIdentify4));
	if ( FAILED(hr) )
	{
		return FALSE;
	}
	long lIdentify5 = 0;
	hr = clReg.GetValue(L"Identify5", REG_DWORD, (char*)&lIdentify5, sizeof(lIdentify5));
	if ( FAILED(hr) )
	{
		return FALSE;
	}

	long x = lIdentify4*100+lIdentify3*10+lIdentify2;
	long m = (x-154)/15;
	if ( m >= 0 && m <= 19 )
	{
		x += 15;
		lIdentify1 = x/100;
		lIdentify2 = (x- lIdentify1*100)/10;
		lIdentify3 = x - lIdentify1*100 - lIdentify2*10;
		hr = clReg.SetValue(L"Identify2", REG_DWORD, (char*)&lIdentify3, sizeof(lIdentify3));
		if ( FAILED(hr) )
		{
			return FALSE;
		}
		hr = clReg.SetValue(L"Identify3", REG_DWORD, (char*)&lIdentify2, sizeof(lIdentify2));
		if ( FAILED(hr) )
		{
			return FALSE;
		}
		hr = clReg.SetValue(L"Identify4", REG_DWORD, (char*)&lIdentify1, sizeof(lIdentify1));
		if ( FAILED(hr) )
		{
			return FALSE;
		}
		unsigned int lT = 0;
		rand_s(&lT);
		lIdentify1 = lT%10;
		hr = clReg.SetValue(L"Identify1", REG_DWORD, (char*)&lIdentify1, sizeof(lIdentify1));
		if ( FAILED(hr) )
		{
			return FALSE;
		}
		rand_s(&lT);
		lIdentify5 = lT%10;
		hr = clReg.SetValue(L"Identify5", REG_DWORD, (char*)&lIdentify5, sizeof(lIdentify5));
		if ( FAILED(hr) )
		{
			return FALSE;
		}

		return TRUE;
	}
	else
	{
		return FALSE;
	}

	return FALSE;
}