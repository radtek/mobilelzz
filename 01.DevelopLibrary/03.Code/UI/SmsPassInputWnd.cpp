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

	SetWindowText( L"��������" );

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
				LPCWSTR pwcInput		=	m_PassInput.GetPassWord();
				LPCWSTR	pwcInputAgain	=	m_PassInput_Again.GetPassWord();
				if ( NULL == pwcInput || NULL == pwcInputAgain || *pwcInput == L'\0' || *pwcInputAgain == L'\0' )
				{
					MzMessageBoxEx( NULL,L"����������Ч������������!",MB_OK);
				}
				else if ( 0 != wcscmp( pwcInput,pwcInputAgain ) )
				{
					MzMessageBoxEx( NULL,L"������������벻һ�£�����������!",MB_OK);
				}
				else
				{
					//��������
					wchar_t	*pBuf		=	NULL;
					long	lSize		=	0;
					wchar_t	*pwcResult	=	NULL;

					stCoreItemData* pstCoreItemData	=	( stCoreItemData* )( m_pItem->m_pData );

					m_clCEasySmsUiCtrl.MakePassWordStatusReq ( &pBuf, &lSize, pstCoreItemData->lPid, L"add", (wchar_t *)pwcInput, NULL );
				
					CCoreService	*pCCoreService	=	CCoreService::GetInstance();
					if ( NULL == pCCoreService )						return;

					HRESULT	hr	=	pCCoreService->Request( pBuf, &pwcResult );
					if ( SUCCEEDED(hr) )
					{
						this->EndModal( ID_OK );
					}
				}

			}

			m_PassInput.SetText( L"" );
			m_PassInput_Again.SetText( L"" );

			break;
		}

		default:
			break;
	}
}


BOOL	CSmsPassInputWnd::SubInitialize()
{
	
	/*���ñ���ͼƬ*/
	m_modeIndex	=	0;
	m_Picture.SetID( MZ_IDC_PASS_PICTURE );
	m_Picture.SetPos( 0, 0, 350, 250 );
	m_Picture.SetPaintMode( modeId[ m_modeIndex ] );
	m_Picture.LoadImage( MzGetInstanceHandle(), RT_RCDATA, MAKEINTRESOURCE( IDR_ENCRYPT_INPUT ) );
	AddUiWin( &m_Picture );
	
	
	//ini toolbar			
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 0, true, true, L"ȷ��" );
	m_toolBar_base.SetButton( 2, true, true, L"����" );
	m_toolBar_base.EnablePressedHoldSupport( true );
	m_toolBar_base.SetPressedHoldTime( 1000 );
	m_toolBar_base.EnableNotifyMessage( true );
	m_toolBar_base.SetID( MZ_IDC_PASS_TOOLBAR );
	AddUiWin( &m_toolBar_base );

	//ini	InfoEdit
	m_PassInput.SetID( MZ_IDC_FIND_INFO );
	m_PassInput.SetTextColor( RGB( 255, 0, 0 ) );
	m_PassInput.SetPos( 80, 60, GetWidth() - MZM_MARGIN_MAX * 4, 40 );
	m_PassInput.SetTip( L"����������" );
	m_PassInput.EnablePassWord(); 
	m_PassInput.SetSipMode( IM_SIP_MODE_ADDRESSEE_123, MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_PassInput ); 

	//ini	InfoEdit
	m_PassInput_Again.SetID( MZ_IDC_FIND_INFO );
	m_PassInput_Again.SetTextColor( RGB( 255, 0, 0 ) );
	m_PassInput_Again.SetPos( 80, 120, GetWidth() - MZM_MARGIN_MAX * 4, 40 );
	m_PassInput_Again.SetTip( L"���ٴ���������" );
	m_PassInput_Again.EnablePassWord();
	m_PassInput_Again.SetSipMode( IM_SIP_MODE_ADDRESSEE_123, MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_PassInput_Again ); 

	return	TRUE;
}
