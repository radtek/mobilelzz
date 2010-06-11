#include"stdafx.h"
#include "resource.h"

#include "SmsFindWnd.h"
#include "SmsPassDeleteWnd.h"

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsPassDeleteWnd::CSmsPassDeleteWnd(void)
{

}

CSmsPassDeleteWnd::~CSmsPassDeleteWnd(void)
{

}

BOOL CSmsPassDeleteWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"…æ≥˝√‹¬Î" );

	return	SubInitialize();
	
}

void CSmsPassDeleteWnd::OnMzCommand( WPARAM wParam, LPARAM lParam )
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
				//±£¥Ê√‹¬Î
				wchar_t	*pBuf		=	NULL;
				long	lSize		=	0;
				wchar_t	*pwcResult	=	NULL;

				stCoreItemData* pstCoreItemData	=	( stCoreItemData* )( m_pItem->m_pData );

				m_clCEasySmsUiCtrl.MakePassWordStatusReq ( &pBuf, &lSize, pstCoreItemData->lPid, L"delete", (wchar_t *)pwcInput, NULL );

				CCoreService	*pCCoreService	=	CCoreService::GetInstance();
				if ( NULL == pCCoreService )						return;

				HRESULT	hr	=	pCCoreService->Request( pBuf, &pwcResult );
				if ( SUCCEEDED(hr) )
				{
					this->EndModal( ID_OK );
				}
				else
				{
					MzMessageBoxEx( NULL,L" ‰»Îµƒ√‹¬Î¥ÌŒÛ£¨«Î÷ÿ–¬ ‰»Î!",MB_OK);
				}
			}

			m_PassInput.SetText( L"" );

			break;
		}

		default:
			break;
	}
}


BOOL	CSmsPassDeleteWnd::SubInitialize()
{
	
	/*…Ë÷√±≥æ∞Õº∆¨*/
	m_modeIndex	=	0;
	m_Picture.SetID( MZ_IDC_PASS_PICTURE );
	m_Picture.SetPos( 0, 0, 350, 250 );
	m_Picture.SetPaintMode( modeId[ m_modeIndex ] );
	m_Picture.LoadImage( MzGetInstanceHandle(), RT_RCDATA, MAKEINTRESOURCE( IDR_ENCRYPT_INPUT ) );
	AddUiWin( &m_Picture );
	
	
	//ini toolbar			
	m_toolBar_base.SetPos( 0, GetHeight() - MZM_HEIGHT_TEXT_TOOLBAR , GetWidth() , MZM_HEIGHT_TEXT_TOOLBAR );
	m_toolBar_base.SetButton( 0, true, true, L"»∑∂®" );
	m_toolBar_base.SetButton( 2, true, true, L"∑µªÿ" );
	m_toolBar_base.EnablePressedHoldSupport( true );
	m_toolBar_base.SetPressedHoldTime( 1000 );
	m_toolBar_base.EnableNotifyMessage( true );
	m_toolBar_base.SetID( MZ_IDC_PASS_TOOLBAR );
	AddUiWin( &m_toolBar_base );

	//ini	InfoEdit
	m_PassInput.SetID( MZ_IDC_PASS_DELETE );
	m_PassInput.SetTextColor( RGB( 255, 0, 0 ) );
	m_PassInput.SetPos( 80, 90, GetWidth() - MZM_MARGIN_MAX * 4, 40 );
	m_PassInput.SetTip( L"«Î ‰»Î√‹¬Î" );
	m_PassInput.EnablePassWord(); 
	m_PassInput.SetSipMode( IM_SIP_MODE_ADDRESSEE_123, MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_PassInput ); 

	return	TRUE;
}
