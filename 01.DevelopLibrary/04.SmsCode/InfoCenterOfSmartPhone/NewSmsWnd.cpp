#include"stdafx.h"

#include"UiEditControl.h"
#include"NewSmsWnd.h"

#include <sms.h>
#pragma comment(lib,"sms.lib")

CNewSmsWnd::~CNewSmsWnd()
{
	if(NULL != m_pRecivers)
	{
		delete[] m_pRecivers;
		m_pRecivers = NULL;
	}
	m_lReciversCount = 0;
}

BOOL CNewSmsWnd::OnInitDialog()
{
	// �����ȵ��û���ĳ�ʼ��
	if (!CMzWndEx::OnInitDialog())
	{
	  return FALSE;
	}

	//��������Ӧ�豸
	MzAccOpen();

	//���������Ӧ�ı����Ϣ
	m_accMsg = MzAccGetMessage();
	//m_Recievers.SetParentWnd(m_hWnd);
	// ��ʼ���ռ��˿ؼ�������ӵ�������
	
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
		m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH_H, BUTTON_HEIGHT_VH);
		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()- m_Recievers.GetHeight() ));
		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_H),0,BUTTON_WIDTH_H,BUTTON_HEIGHT_VH);
	}
	else
	{
		long lWidth = GetWidth();
		long lHeight = GetHeight();
		RECT rc = MzGetWindowRect();
		RECT rc2 = MzGetClientRect();
		RECT rc3 = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc3.left, rc3.top,RECT_WIDTH(rc3), RECT_HEIGHT(rc3) );
		m_Recievers.SetPos(0, 0, lWidth-BUTTON_WIDTH_V, BUTTON_HEIGHT_VH);
		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (lHeight - m_Recievers.GetHeight()));
		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_V),0,BUTTON_WIDTH_V,BUTTON_HEIGHT_VH);
	}
	
	m_Recievers.SetText(L"�ռ���:");  // set the tips text
	m_Recievers.SetID(MZ_IDC_RECIEVERS_EDIT);
	m_Recievers.SetParent((void*)this);
	//m_Recievers.SetTextColor(RGB(0,0,0)); // you could also set the color of text
	//m_Recievers.SetEnable(0);
	//m_Recievers.SetColorBg(RGB(0,0,0));
	AddUiWin(&m_Recievers); // don't forget to add the control to the window
	// ��ʼ�������ı��ؼ�������ӵ�������
	//MzOpenSip(IM_SIP_MODE_KEEP,0);
	m_SmsMsgEdit.SetSipMode(IM_SIP_MODE_KEEP,0);
//	m_SmsMsgEdit.SetFocus(true);
	
	
	m_SmsMsgEdit.SetTextColor(RGB(94,94,94)); // you could also set the color of text

	m_SmsMsgEdit.SetEditBgType(UI_EDIT_BGTYPE_FILL_WHITE_AND_TOPSHADOW);
	m_SmsMsgEdit.SetColorBg(RGB(243,241,207)); 
	m_SmsMsgEdit.EnableInsideScroll(true);
	m_SmsMsgEdit.EnableZoomIn(true);   
	AddUiWin(&m_SmsMsgEdit); // don't forget to add the control to the window

	m_SendSmsBtn.SetButtonType(MZC_BUTTON_DELETE_ORANGE);
	
	m_SendSmsBtn.SetID(MZ_IDC_SEND_SMS_BTN);
	m_SendSmsBtn.SetText(L"����");
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
			
			int n = g_ReciversList.GetItemCount();
			bool SendFlag = FALSE;
			int  SendFail = 0;
			for(int i = 0; i<n;i++ )
			{
				MyListItemData* pMyListItemData = NULL;
				g_ReciversList.GetItem(i, &pMyListItemData );
				CMzString  Number = pMyListItemData->StringDescription;
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


				SendFlag = SendSMS(NewNumber.C_Str(), m_SmsMsgEdit.GetText().C_Str());
				if(!SendFlag)
				{
					SendFail++;
				}

			}

			if(SendFail ==0 )
			{
				MzMessageBoxEx(NULL,L"�����ѷ������",NULL);
			}
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
			// ת��
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
	    
	  }
	  break;
	}
	  return CMzWndEx::MzDefWndProc(message,wParam,lParam);
}

// ת���������Ҫ�������ڵ�λ�ã����ش˺�����Ӧ WM_SETTINGCHANGE ��Ϣ
void CNewSmsWnd::OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName)
{
  //�����µ���Ļ����Ĵ��ڴ�С���ؼ�λ��
  DEVMODE  devMode;
  memset(&devMode, 0, sizeof(DEVMODE));
  devMode.dmSize = sizeof(DEVMODE);
  devMode.dmFields = DM_DISPLAYORIENTATION;
  ChangeDisplaySettingsEx(NULL, &devMode, NULL, CDS_TEST, NULL);

 //����
  if (devMode.dmDisplayOrientation == DMDO_90 || devMode.dmDisplayOrientation == DMDO_270)
  {
		g_bH = TRUE;
		RECT rc = MzGetWorkArea();
		//modify begin by zhaodsh  2010/03/21 12:27
		//SetWindowPos(m_hWnd, rc.left, rc.top,RECT_HEIGHT(rc)+rc.top, RECT_WIDTH(rc)  );
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		// modify end 2010/03/21 12:27
		
		m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH_V, BUTTON_HEIGHT_VH);

		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()-m_Recievers.GetHeight()));

		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_V),0,BUTTON_WIDTH_V,BUTTON_HEIGHT_VH);
  }

  //����
	if (devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0)
	{
		g_bH = FALSE;
		RECT rc = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		m_Recievers.SetPos(0, 0, GetWidth()-BUTTON_WIDTH_H, BUTTON_HEIGHT_VH);

		m_SmsMsgEdit.SetPos(0, m_Recievers.GetHeight(), GetWidth(), (GetHeight()-m_Recievers.GetHeight()));

		m_SendSmsBtn.SetPos((GetWidth()-BUTTON_WIDTH_H),0,BUTTON_WIDTH_H,BUTTON_HEIGHT_VH);
	}
}

void CNewSmsWnd::UpdateData( MyListItemData* pRecivers,long lReciversCount )
{
	m_pRecivers = pRecivers;
	m_lReciversCount = lReciversCount;
}

/************************************
* ���÷���:
* SendSMS(_T("+8613xxxxxxxxx"),_T("����~"));
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

        //��ַ��ʽע����ں���ǰ��"+86"
        smsaDestination.smsatAddressType = SMSAT_INTERNATIONAL; 
        _tcsncpy(smsaDestination.ptsAddress, lpNumber,SMS_MAX_ADDRESS_LENGTH); 

        tpsd.dwMessageOptions  = PS_MESSAGE_OPTION_NONE; 
        //tpsd.dwMessageOptions =  PS_MESSAGE_OPTION_STATUSREPORT;//��ʾ��Ҫ״̬����
        tpsd.psMessageClass  = PS_MESSAGE_CLASS1; 
        //PS_MESSAGE_CLASS0��ʾ�����ڱ����պ�������ʾ�Ҳ��洢���ռ���(��Ϊ����) 
        //PS_MESSAGE_CLASS1��ʾһ�������������պ�洢���ռ��䲢����һ��ȷ�ϻض������ģ����ͷ��յ�һ���ѱ����յ�״̬���档

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