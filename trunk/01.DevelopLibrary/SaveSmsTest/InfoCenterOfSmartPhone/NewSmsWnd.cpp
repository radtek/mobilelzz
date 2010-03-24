#include"stdafx.h"

#include"UiEditControl.h"
#include"NewSmsWnd.h"

#include <sms.h>

#pragma comment(lib,"sms.lib")

//#define		SMS_MSGTYPE_TEXT	L"Meizu Text SMS Protocol"
BOOL CNewSmsWnd::OnInitDialog()
{
//zhu.t add for License	at 2010-3-22
#ifdef	LICENSE
	if ( !LicenseProtect())
	{
		PostQuitMessage(0);
		return	FALSE;
	}
#endif
//test Meizu
	wchar_t protocolName[260];  
	HRESULT hr = S_OK;  
	wcscpy(protocolName,  SMS_MSGTYPE_NOTIFICATION/**/); 

	hr = SmsClearMessageNotification(SMS_MSGTYPE_TEXT); 
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	hr = SmsClearMessageNotification(SMS_MSGTYPE_NOTIFICATION); 
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	hr = SmsClearMessageNotification(SMS_MSGTYPE_WDP); 
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	hr = SmsClearMessageNotification(SMS_MSGTYPE_WCMP); 
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	hr = SmsClearMessageNotification(SMS_MSGTYPE_STATUS); 
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	hr = SmsClearMessageNotification(SMS_MSGTYPE_BROADCAST); 
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	hr = SmsClearMessageNotification(SMS_MSGTYPE_RAW); 
	if (FAILED(hr))
	{
		return	E_FAIL;
	}


	SMSREGISTRATIONDATA smsrd = SMSREGISTRATIONDATA();  

	memset(&smsrd, 0x00, sizeof(SMSREGISTRATIONDATA));  

	smsrd.cbSize = sizeof(SMSREGISTRATIONDATA); 

	wchar_t pszFileName[MAX_PATH] = {0};
	GetModuleFileName(0, pszFileName, MAX_PATH); 

	wcscpy(smsrd.tszAppName, pszFileName);  
	wcscpy(smsrd.tszParams, _T(""));  

	wcscpy(smsrd.tszProtocolName, SMS_MSGTYPE_TEXT);  

	hr = SmsSetMessageNotification (&smsrd);  
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
//////////////////////////////////////////////////////////////////////////
	memset( smsrd.tszProtocolName, 0x0, SMS_MAX_PROTOCOLNAME_LENGTH *sizeof(TCHAR));
	wcscpy(smsrd.tszProtocolName, SMS_MSGTYPE_NOTIFICATION); 
	hr = SmsSetMessageNotification (&smsrd);  
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
//////////////////////////////////////////////////////////////////////////
	memset( smsrd.tszProtocolName, 0x0, SMS_MAX_PROTOCOLNAME_LENGTH *sizeof(TCHAR));
	wcscpy(smsrd.tszProtocolName, SMS_MSGTYPE_WDP); 
	hr = SmsSetMessageNotification (&smsrd);  
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	memset( smsrd.tszProtocolName, 0x0, SMS_MAX_PROTOCOLNAME_LENGTH *sizeof(TCHAR));
	wcscpy(smsrd.tszProtocolName, SMS_MSGTYPE_WCMP); 
	hr = SmsSetMessageNotification (&smsrd);  
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	//////////////////////////////////////////////////////////////////////////
	memset( smsrd.tszProtocolName, 0x0, SMS_MAX_PROTOCOLNAME_LENGTH *sizeof(TCHAR));
	wcscpy(smsrd.tszProtocolName, SMS_MSGTYPE_STATUS); 
	hr = SmsSetMessageNotification (&smsrd);  
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	//////////////////////////////////////////////////////////////////////////
	memset( smsrd.tszProtocolName, 0x0, SMS_MAX_PROTOCOLNAME_LENGTH *sizeof(TCHAR));
	wcscpy(smsrd.tszProtocolName, SMS_MSGTYPE_BROADCAST); 
	hr = SmsSetMessageNotification (&smsrd);  
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
	//////////////////////////////////////////////////////////////////////////
	memset( smsrd.tszProtocolName, 0x0, SMS_MAX_PROTOCOLNAME_LENGTH *sizeof(TCHAR));
	wcscpy(smsrd.tszProtocolName, SMS_MSGTYPE_RAW); 
	hr = SmsSetMessageNotification (&smsrd);  
	if (FAILED(hr))
	{
		return	E_FAIL;
	}
//end	
	// 必须先调用基类的初始化
	if (!CMzWndEx::OnInitDialog())
	{
	  return FALSE;
	}

	//打开重力感应设备
	MzAccOpen();

	//获得重力感应改变的消息
	m_accMsg = MzAccGetMessage();
	//m_Recievers.SetParentWnd(m_hWnd);
	// 初始化收件人控件，并添加到窗口中
	
	RECT rect = MzGetVisibleDesktopRect();
	RECT rc = {0};
	int height = 0;
	int width = 0;
	HWND hWnd = FindWindow(L"CTaskBar", 0);
	if(hWnd != 0)
	{
		::GetWindowRect(hWnd, &rc);
		height = rc.bottom - rc.top;
		width = rc.right - rc.left;
	}

	if(width>480)
	{
		g_bH = TRUE;
		long lWidth = GetWidth();
		long lHeight = GetHeight();
		RECT rc = MzGetWindowRect();
		RECT rc2 = MzGetClientRect();
		RECT rc3 = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_HEIGHT(rc)+rc.top,RECT_WIDTH(rc)  );
		lWidth = GetWidth();
		lHeight = GetHeight();
		m_Recievers.SetPos(2, 0, GetWidth()-BUTTON_WIDTH_H-2, BUTTON_HEIGHT_VH);
		m_SmsMsgEdit->SetPos(2, m_Recievers.GetHeight()+3, GetWidth()-4, (GetHeight()- m_Recievers.GetHeight() ));
		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_H),0,BUTTON_WIDTH_H-2,BUTTON_HEIGHT_VH);
	}
	else
	{
		long lWidth = GetWidth();
		long lHeight = GetHeight();
		RECT rc = MzGetWindowRect();
		RECT rc2 = MzGetClientRect();
		RECT rc3 = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc3.left, rc3.top,RECT_WIDTH(rc3), RECT_HEIGHT(rc3) );
		m_Recievers.SetPos(2, 0, lWidth-BUTTON_WIDTH_V-2, BUTTON_HEIGHT_VH);
		m_SmsMsgEdit->SetPos(2, m_Recievers.GetHeight()+3, GetWidth()-4, (lHeight - m_Recievers.GetHeight()));
		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_V),0,BUTTON_WIDTH_V-2,BUTTON_HEIGHT_VH);
	}

	m_Recievers.SetText(L"点击选择联系人:");  // set the tips text
	m_Recievers.SetID(MZ_IDC_RECIEVERS_EDIT);
	m_Recievers.SetParent((void*)this);
	//m_Recievers.SetTextColor(RGB(0,0,0)); // you could also set the color of text
	//m_Recievers.SetEnable(0);
	//m_Recievers.SetColorBg(RGB(0,0,0));
	AddUiWin(&m_Recievers); // don't forget to add the control to the window
	// 初始化短信文本控件，并添加到窗口中
	//MzOpenSip(IM_SIP_MODE_KEEP,0);
	m_SmsMsgEdit->SetSipMode(IM_SIP_MODE_GEL_PY,0);
	//m_SmsMsgEdit->SetFocus(true);
	
	
	m_SmsMsgEdit->SetTextColor(RGB(94,94,94)); // you could also set the color of text

	m_SmsMsgEdit->SetEditBgType(UI_EDIT_BGTYPE_ROUND_RECT);
	m_SmsMsgEdit->SetColorBg(RGB(243,241,207)); 
	m_SmsMsgEdit->EnableInsideScroll(true);
	m_SmsMsgEdit->EnableZoomIn(true);   
	m_SmsMsgEdit->SetTip(L"在这里输入短信内容");
	//m_SmsMsgEdit->EnableAutoOpenSip(true);
	AddUiWin(m_SmsMsgEdit); // don't forget to add the control to the window

	m_SendSmsBtn.SetButtonType(MZC_BUTTON_DELETE_ORANGE);
	m_SendSmsBtn.EnableTextSinkOnPressed(TRUE);
	m_SendSmsBtn.SetTextColor_Pressed(RGB(94,94,94));
	
	m_SendSmsBtn.SetID(MZ_IDC_SEND_SMS_BTN);
	m_SendSmsBtn.SetText(L"发送");
	//m_ContactorsBtn.SetTextColor(RGB(255,255,255));
	AddUiWin(&m_SendSmsBtn);
//	MzOpenSip();

	return TRUE;
}

void CNewSmsWnd::OnMzCommand(WPARAM wParam, LPARAM lParam)
{
	UINT_PTR id = LOWORD(wParam);
	switch(id)
	{
		case MZ_IDC_SEND_SMS_BTN:
		{		
			RECT rect = MzGetVisibleDesktopRect();
			RECT rc = {0};
			int height = 0;
			int width = 0;
			HWND hWnd = FindWindow(L"CTaskBar", 0);
			if(hWnd != 0)
			{
				::GetWindowRect(hWnd, &rc);
				height = rc.bottom - rc.top;
				width = rc.right - rc.left;
			}

			if(width>480)
			{
				m_progress.SetPos(200, 100, 300, 150);
			}
			else
			{
				m_progress.SetPos(100, 200, 150, 300);
			}
			 m_lCurProgress = 0;
            // 初始化 MzPopupProgress 控件
            m_progress.SetRange(0, 200);
            m_progress.SetCurrentValue(m_lCurProgress);
            //m_progress.SetTitleText(L"剩余时间 10 秒");
            m_progress.SetNoteText(L"短信发送中");
            m_progress.StartProgress(m_hWnd, TRUE, FALSE);
            // 开启定时器，每100ms刷新一次进度条
            SetTimer(m_hWnd, PROGRESS_TIMER_ID, 100, NULL);

	//		MzAutoMsgBoxEx(NULL, L"短信发送中.....", 3000);



			DWORD lThreadId = 0;
			CreateThread( NULL, 0, ProxyRun, (void*)this, 0, &lThreadId);
			//m_SmsMsgEdit->SetText(L"");
			


			break;
		}

	  break;
	}

}




LRESULT CNewSmsWnd::MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam)
{
	switch(message)
	{
	default:
	  {
		if (message == m_accMsg)
		{
			// 转屏
		  switch(wParam)
		  {
		  case SCREEN_PORTRAIT_P:
			{
				MzChangeDisplaySettingsEx(DMDO_90);
				g_bH = TRUE;
			}
			break;
		  case SCREEN_PORTRAIT_N:
			{
				MzChangeDisplaySettingsEx(DMDO_270);
				g_bH = FALSE;
			}
			break;
		  case SCREEN_LANDSCAPE_N:
			{
				MzChangeDisplaySettingsEx(DMDO_180);
				g_bH = TRUE;
			}
			break;
		  case SCREEN_LANDSCAPE_P:
			{
				MzChangeDisplaySettingsEx(DMDO_0);
				g_bH = FALSE;
			}
			break;
		  }
		}
		else if( message == 641 )
		{
			WinManager* pMng = GetWinManager();
			pMng->SetFocusedWinBeforeDeactivate(m_SmsMsgEdit);
		}
	    
	  }
	  break;
	}
	  return CMzWndEx::MzDefWndProc(message,wParam,lParam);
}

// 转屏后如果需要调整窗口的位置，重载此函数响应 WM_SETTINGCHANGE 消息
void CNewSmsWnd::OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName)
{
  //设置新的屏幕方向的窗口大小及控件位置
  DEVMODE  devMode;
  memset(&devMode, 0, sizeof(DEVMODE));
  devMode.dmSize = sizeof(DEVMODE);
  devMode.dmFields = DM_DISPLAYORIENTATION;
  ChangeDisplaySettingsEx(NULL, &devMode, NULL, CDS_TEST, NULL);

 //竖屏
  if (devMode.dmDisplayOrientation == DMDO_90 || devMode.dmDisplayOrientation == DMDO_270)
  {
		g_bH = TRUE;
		RECT rc = MzGetWorkArea();
		//modify begin by zhaodsh  2010/03/21 12:27
		//SetWindowPos(m_hWnd, rc.left, rc.top,RECT_HEIGHT(rc)+rc.top, RECT_WIDTH(rc)  );
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		// modify end 2010/03/21 12:27
		
		m_Recievers.SetPos(2, 0, GetWidth()-BUTTON_WIDTH_V-2, BUTTON_HEIGHT_VH);

		m_SmsMsgEdit->SetPos(2, m_Recievers.GetHeight()+3, GetWidth()-4, (GetHeight()-m_Recievers.GetHeight()));

		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_V),0,BUTTON_WIDTH_V-2,BUTTON_HEIGHT_VH);
  }

  //横屏
	if (devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0)
	{
		g_bH = FALSE;
		RECT rc = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		m_Recievers.SetPos(2, 0, GetWidth()-BUTTON_WIDTH_H-2, BUTTON_HEIGHT_VH);

		m_SmsMsgEdit->SetPos(2, m_Recievers.GetHeight()+3, GetWidth()-4, (GetHeight()-m_Recievers.GetHeight()));

		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_H),0,BUTTON_WIDTH_H-2,BUTTON_HEIGHT_VH);
	}
}

void CNewSmsWnd::UpdateData( MyListItemData* pRecivers,long lReciversCount )
{
}

/************************************
* 调用范例:
* SendSMS(_T("+8613xxxxxxxxx"),_T("测试~"));
*
* Author:LOJA
* Version: 1.0.1.0
* Date: 2009/07/30
*
* Smartphone Platforms: Smartphone 2002 and later
* OS Versions: Windows CE 3.0 and later
*
*************************************/
bool CNewSmsWnd::SendSMS(IN LPCTSTR lpNumber,IN LPCTSTR lpszMessage)
{

        HRESULT hRes; 
        SMS_HANDLE   smsHandle=NULL; 
        SMS_ADDRESS   smsaDestination; 
        SMS_MESSAGE_ID   smsmidMessageID=0; 
        TEXT_PROVIDER_SPECIFIC_DATA   tpsd; 

        hRes=SmsOpen(SMS_MSGTYPE_TEXT,SMS_MODE_SEND,&smsHandle,NULL); 
        if   (FAILED(hRes)) 
        { 
                return   false; 
        } 

        //地址方式注意国内号码前加"+86"
        smsaDestination.smsatAddressType = SMSAT_INTERNATIONAL; 
        _tcsncpy(smsaDestination.ptsAddress, lpNumber,SMS_MAX_ADDRESS_LENGTH); 

        tpsd.dwMessageOptions  = PS_MESSAGE_OPTION_NONE; 
        //tpsd.dwMessageOptions =  PS_MESSAGE_OPTION_STATUSREPORT;//表示需要状态报告
        tpsd.psMessageClass  = PS_MESSAGE_CLASS1; 
        //PS_MESSAGE_CLASS0表示短信在被接收后立即显示且不存储在收件箱(称为闪信) 
        //PS_MESSAGE_CLASS1表示一般的情况，被接收后存储到收件箱并发送一个确认回短信中心，发送方收到一个已被接收的状态报告。

        ZeroMemory(tpsd.pbHeaderData, sizeof(tpsd.pbHeaderData));         
        tpsd.dwHeaderDataSize = 0; 
        tpsd.fMessageContainsEMSHeaders = FALSE; 
        tpsd.dwProtocolID = SMS_MSGPROTOCOL_UNKNOWN; 
        tpsd.psReplaceOption = PSRO_NONE; 


        hRes= SmsSendMessage(smsHandle,
                NULL,   
                &smsaDestination,   
                NULL, 
                (PBYTE)lpszMessage,   
                _tcslen(lpszMessage) *  sizeof(TCHAR),   
                (PBYTE)&tpsd, 
                sizeof(TEXT_PROVIDER_SPECIFIC_DATA),   
                SMSDE_OPTIMAL,   
                SMS_OPTION_DELIVERY_NONE, 
                &smsmidMessageID); 

        SmsClose(smsHandle);

        if   (SUCCEEDED(hRes)) 
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
bool CNewSmsWnd::SendSMS_Wrapper(IN CMzString&  Number)
{
	bool SendFlag = false;
	CMzString  NewNumber ;
	if(Number.C_Str()[0] ==L'1')
	{
		NewNumber = L"+86";
		NewNumber += Number;
	}
	else
	{
		NewNumber = Number;
	}


	CMzString  SMS_Content = m_SmsMsgEdit->GetText().C_Str();

	
	int length = SMS_Content.Length();
	if( length <= 69 )
	{
	 	SendFlag = SendSMS(NewNumber.C_Str(), m_SmsMsgEdit->GetText().C_Str());
		HRESULT	hr	=	smsInsert( NewNumber.C_Str(), m_SmsMsgEdit->GetText().C_Str() );
		if (FAILED(hr))
		{
			return	false;
		}
	}
	else
	{
		for(int i = 0; i < length; i+=69)
		{
			int count = ((length - i) < 69 ? (length-i): 69); 
			CMzString Single_Content =SMS_Content.SubStr(i,count);

			SendFlag = SendSMS(NewNumber.C_Str(), Single_Content.C_Str() );
			

		}
		HRESULT	hr	=	smsInsert( NewNumber.C_Str(), SMS_Content.C_Str() );
		if (FAILED(hr))
		{
			return	false;
		}
	}


	return SendFlag;

}

// 定时器触发的消息函数
void CNewSmsWnd::OnTimer(UINT_PTR nIDEvent)
{
	if (PROGRESS_TIMER_ID == nIDEvent)
	{
		// 刷新进度条
		m_progress.SetCurrentValue(++m_lCurProgress);
		//if (m_lCurProgress % 10 == 0)
		//{
		//	WCHAR wstr[10];
		//	wsprintf(wstr, L"剩余时间 %d 秒", (100 - m_lCurProgress) / 10);
		//	m_progress.SetTitleText(wstr);
		//}
		m_progress.UpdateProgress(FALSE);

		if (200 == m_lCurProgress)
		{
			KillTimer(m_hWnd, PROGRESS_TIMER_ID);
			m_progress.KillProgress();
//			MzMessageBoxEx(NULL,L"发送超时",MB_OK);
		}
	}
}

DWORD CNewSmsWnd::ProxyRun(LPVOID lp)
{
	CNewSmsWnd*  Wnd = (CNewSmsWnd*)lp;
	Wnd->Run();

	return 0;
}

void
CNewSmsWnd::Run()
{
	int n = g_ReciversList.GetItemCount();
	bool SendFlag = FALSE;
	for(int i = 0; i<n;i++ )
	{
		MyListItemData* pMyListItemData = NULL;
		g_ReciversList.GetItem(i, &pMyListItemData );
		CMzString  Number = pMyListItemData->StringDescription;
		
		SendFlag = SendSMS_Wrapper(Number);
	}

	KillTimer(m_hWnd, PROGRESS_TIMER_ID);
	m_progress.KillProgress();
	MzMessageBoxEx(NULL,L"短信已发送完毕",MB_OK);

	g_ReciversList.Clear();
	m_Recievers.UpdateData(1);
}

//HRESULT	CNewSmsWnd::smsInsert( wchar_t **pwcNo, long lCnt, wchar_t *pContent )
HRESULT	CNewSmsWnd::smsInsert( wchar_t *pwcNo, wchar_t *pContent )
{
	if ( NULL == pwcNo /*|| 0 == lCnt */|| NULL == pContent )
	{
		return	E_FAIL;
	}

	CSQL_sessionManager*  pm =CSQL_sessionManager::GetInstance();
	if( NULL == pm )						return E_FAIL;
	CSQL_session*  pSession = NULL;

	//sms 的路径/////////////////////////////////////////////////////////////////////////////
//	HRESULT hr = pm->Session_Connect(L"contact", L".\\", L"contacts.db", &pSession );
	HRESULT hr = pm->Session_Connect(L"sms.db", L".\\"/*L".\\docment and setting"*/, L"sms.db", &pSession );
	if(FAILED(hr) || pSession == NULL)				return E_FAIL;

	//sms insert
	CSQL_query * pSmsInsert = NULL;
	int			SmsInsertid	=	0;
	hr	=	pSession->Query_Create(&SmsInsertid, &pSmsInsert );
	hr	=	pSmsInsert->Prepare( L"insert into sms values( ?,?,?,?,?,?,?,?,?,?,?,?,?)" );
	if(FAILED(hr))									return E_FAIL;

	//thread select query
	CSQL_query * pThreadSelect = NULL;
	int          ThreadSelectid = 0;
	hr	=	pSession->Query_Create(&ThreadSelectid, &pThreadSelect );
	if(FAILED(hr))									return E_FAIL;
	hr	=	pThreadSelect->Prepare( L"select _id  from threads where recipient_address = ?" );
	if(FAILED(hr))									return E_FAIL;

	//thread insert query
	CSQL_query * pThreadInsert = NULL;
	int          ThreadInsertid = 0;
	hr	=	pSession->Query_Create(&ThreadInsertid, &pThreadInsert );
	if(FAILED(hr))									return E_FAIL;
	hr	=	pThreadInsert->Prepare( L"insert into threads values( ?,?,?,?,?,?,?,?,?,?,?,?)" );
	if(FAILED(hr))									return E_FAIL;

	//canonical_addresses select query
	CSQL_query * pCononialSelect = NULL;
	int          CononialSelectid = 0;
	hr	=	pSession->Query_Create(&CononialSelectid, &pCononialSelect );
	if(FAILED(hr))									return E_FAIL;
	hr	=	pCononialSelect->Prepare( L"select *  from canonical_addresses where address = ?" );
	if(FAILED(hr))									return E_FAIL;
	long	lTime	=	GetTickCount();

	//canonical_addresses Insert query
	CSQL_query * pCononiaInsert = NULL;
	int          CononiaInsertid = 0;
	hr	=	pSession->Query_Create(&CononiaInsertid, &pCononiaInsert );
	if(FAILED(hr))									return E_FAIL;
	hr	=	pCononiaInsert->Prepare( L"insert into canonical_addresses values( ?,? )" );
	if(FAILED(hr))									return E_FAIL;
	
	//sms_part Insert query
	CSQL_query * pSmsPart = NULL;
	int          pSmsPartid = 0;
	hr	=	pSession->Query_Create(&pSmsPartid, &pSmsPart );
	if(FAILED(hr))									return E_FAIL;

	hr	=	pSmsPart->Prepare( L"insert into sms_part values( ?,?,?,?,?,? )" );
	if(FAILED(hr))									return E_FAIL;

	execute_sql( pSession, L"Begin",0 , (long*)0 );/*begin*/

//	for ( int i = 0; i < lCnt; ++i )
//	{
		//insert into sms
		//get id
		long	MaxID	=	-1;
		hr = execute_sql( pSession, L"Select Max(_id) from sms", 0, &MaxID);
		if(FAILED(hr))									return E_FAIL;
		++MaxID;

		//find if need insert into canonical_addresses
		long	lCononialID	=	0;
		pCononialSelect->Reset();
		pCononialSelect->Bind( 1, pwcNo/*[i]*/, wcslen(pwcNo/*[i]*/) *  sizeof(wchar_t) );
		if ( S_OK == pCononialSelect->Step() )
		{
			//not find,need insert
			hr = execute_sql( pSession, L"Select Max(_id) from canonical_addresses", 0, &lCononialID);
			if(FAILED(hr))									return E_FAIL;
			++lCononialID;
			pCononiaInsert->Reset();
			pCononiaInsert->Bind( 1, lCononialID );
			pCononiaInsert->Bind( 2, pwcNo/*[i]*/, wcslen(pwcNo/*[i]*/) *  sizeof(wchar_t) );
			hr	=	pCononiaInsert->Step();
			if ( FAILED(hr) )
			{
				return	E_FAIL;
			}

		}
		else
		{
			pCononialSelect->GetField( 0, &lCononialID );
		}

		//find if need insert into Threads
		long	lThread_ID	=	0;
		pThreadSelect->Reset();
		pThreadSelect->Bind( 1, pwcNo/*[i]*/, wcslen(pwcNo/*[i]*/) *  sizeof(wchar_t) );
		if ( S_OK == pThreadSelect->Step() )
		{
			//not find,need insert
			hr = execute_sql( pSession, L"Select Max(_id) from threads", 0, &lThread_ID);
			if(FAILED(hr))									return E_FAIL;
			++lThread_ID;
			pThreadInsert->Reset();
			pThreadInsert->Bind( 1, lThread_ID );
			pThreadInsert->Bind( 2, lTime );
			pThreadInsert->Bind( 3, (long)1);
			pThreadInsert->Bind( 4, lCononialID);
			pThreadInsert->Bind( 5, pwcNo/*[i]*/, wcslen(pwcNo/*[i]*/) *  sizeof(wchar_t) );
			pThreadInsert->Bind( 6, pContent, wcslen( pContent )*sizeof(wchar_t) );
			pThreadInsert->Bind( 7, (long)1 );
			pThreadInsert->Bind( 8, (long)0 );
			pThreadInsert->Bind( 9, (long)0 );
			pThreadInsert->Bind( 10, (long)0 );
			pThreadInsert->Bind( 11, (long)0);
			pThreadInsert->Bind( 12);
			hr	=	pThreadInsert->Step();
			if ( FAILED(hr) )
			{
				return	E_FAIL;
			}
		}
		else
		{
			pThreadSelect->GetField( 0, &lThread_ID );
		}

		//insert into sms
		pSmsInsert->Reset();
		pSmsInsert->Bind( 1, MaxID );
		pSmsInsert->Bind( 2, lThread_ID );
		pSmsInsert->Bind( 3, lCononialID );
		pSmsInsert->Bind( 4, (long)1 );
		pSmsInsert->Bind( 5, pwcNo/*[i]*/, wcslen(pwcNo/*[i]*/) *  sizeof(wchar_t) );
		pSmsInsert->Bind( 6, lTime );
		pSmsInsert->Bind( 7, (long)0 );
		pSmsInsert->Bind( 8, (long)1 );
		pSmsInsert->Bind( 9, (long)0 );
		pSmsInsert->Bind(10, (long)2);
		pSmsInsert->Bind(11, L"我", wcslen(L"我") * sizeof( wchar_t ) );
		pSmsInsert->Bind(12, pContent, wcslen( pContent )*sizeof(wchar_t) );
		pSmsInsert->Bind(13);
		hr	=	pSmsInsert->Step();
		if ( FAILED(hr) )
		{
			return	E_FAIL;
		}

		//insert into part
		long lPartMax	=	0;
		hr = execute_sql( pSession, L"Select Max(_id) from sms_part", 0, &lPartMax);
		++lPartMax;
		pSmsPart->Reset();
		pSmsPart->Bind( 1, lPartMax );
		pSmsPart->Bind( 2, MaxID );
		pSmsPart->Bind( 3, lPartMax );
		pSmsPart->Bind( 4, lTime );
		pSmsPart->Bind( 5, (long)0 );
		pSmsPart->Bind( 6, (long)1 );
		hr	=	pSmsPart->Step();
		if ( FAILED(hr) )
		{
			return	E_FAIL;
		}

//	}

	execute_sql( pSession, L"Commit",0 , (long*)0 );	/*commit*/

	pSession->Query_Delete( pSmsPartid );
	pSession->Query_Delete( SmsInsertid );
	pSession->Query_Delete( ThreadSelectid );
	pSession->Query_Delete( ThreadInsertid );
	pSession->Query_Delete( CononialSelectid );
	pSession->Query_Delete( CononiaInsertid );

	BOOL	bDBClosed	=	FALSE;
	pm->Session_DisConnect( L"contacts.db", &bDBClosed );


	return	S_OK;
}