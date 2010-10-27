#include"stdafx.h"
#include "resource.h"

#include "SmsFindWnd.h"
#include "SmsPassConfirmWnd.h"

///////////////////CSmsLookCtorWnd///////////////////////////////////////////////////////
CSmsPassConfirmWnd::CSmsPassConfirmWnd(void)	:	m_pwPassWord ( NULL ) 
{

}

CSmsPassConfirmWnd::~CSmsPassConfirmWnd(void)
{
// 	if ( NULL != m_pwPassWord )
// 	{
// 		delete	m_pwPassWord;
// 		m_pwPassWord	=	NULL;
// 	}
}

BOOL CSmsPassConfirmWnd::OnInitDialog()
{
	if ( !CMzWndEx::OnInitDialog() )
	{
		return FALSE;
	}

	SetWindowText( L"��������" );

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
			if ( nIndex == 2 )				//ȡ��
			{
				this->EndModal( ID_CANCEL );
			}
			else if ( nIndex == 0 )			//ȷ��
			{
				//ȡ�����벢�ύ
				wchar_t	*pBuf		=	NULL;
				long	lSize		=	0;
				wchar_t	*pwcResult	=	NULL;
// 				if ( NULL != m_pwPassWord )
// 				{
// 					delete	m_pwPassWord;
// 					m_pwPassWord	=	NULL;
// 				}

				m_pwPassWord	=	(wchar_t *)( m_PassInput.GetPassWord() );
				if ( NULL == m_pwPassWord )
				{
					MzMessageBoxEx( NULL,L"����������Ч������������!",MB_OK);
				}
				else
				{
					stCoreItemData* pstCoreItemData	=	( stCoreItemData* )( m_pItem->m_pData );
					
					HRESULT	hr	=	m_clCEasySmsUiCtrl.MakeMsgRltListReq( &pBuf, &lSize, pstCoreItemData->lPid, m_pwPassWord );
					if ( FAILED ( hr ) )										return;

					CCoreService	*pCCoreService	=	CCoreService::GetInstance();
					if ( NULL == pCCoreService )						return;
					hr	=	pCCoreService->Request( pBuf, &pwcResult );
					if ( SUCCEEDED(hr) )
					{
						this->EndModal( ID_OK );
					}
					else
					{
						MzMessageBoxEx( NULL,L"�����������!",MB_OK);
					}
				}	
			}

			m_PassInput.SetText( L"" );

			break;
		}

		default:
			break;
	}
}


BOOL	CSmsPassConfirmWnd::SubInitialize()
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
	m_PassInput.SetPos( 80, 90, GetWidth() - MZM_MARGIN_MAX * 4, 40 );
	m_PassInput.SetTip( L"����������" );
	m_PassInput.EnablePassWord(); 
	m_PassInput.SetSipMode( IM_SIP_MODE_ADDRESSEE_123, MZM_HEIGHT_TEXT_TOOLBAR );
	AddUiWin( &m_PassInput ); 

	return	TRUE;
}

wchar_t*	CSmsPassConfirmWnd::GetPassWord()
{
	return	m_pwPassWord;
}