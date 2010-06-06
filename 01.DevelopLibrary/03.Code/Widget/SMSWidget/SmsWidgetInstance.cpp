#include "SmsWidgetInstance.h"
#include "resource.h"

//#define MZ_IDC_NEW_SMS  101
//#define MZ_IDC_SEND		102
//#define MZ_IDC_ENTER	103

//#define	MZ_IDC_ARROW_LEFT	104
//#define	MZ_IDC_ARROW_RIGHT	105

//#define MZ_IDC_WIDGET_UIEDIT	106


typedef CCoreService* (*pCoreFun)();
typedef HRESULT (*Request)( wchar_t* , wchar_t**  );

void UiWidget_Sms::PaintWin( HDC hdcDst, RECT* prcWin, RECT* prcUpdate )
{
    COLORREF clr = RGB(255,0,0);
    RECT rcWin = *prcWin;
    RECT rcBg = *prcWin;


	/*描画插件背景*/	
	RECT rcImg;
	rcImg.left	= rcBg.left;
	rcImg.top	= rcBg.top;
	rcImg.right	= rcImg.left + /*400*/400;
	rcImg.bottom= rcImg.top + /*240*/300;

    ImagingHelper* pImgBg = m_imgContainer.LoadImage(GetModuleHandle(L"SmsWidget.dll"), IDR_WIDGET_ALL_BACK, true);
    pImgBg->Draw(hdcDst, &rcImg, true);

	::SetTextColor(hdcDst, RGB(255,255,255));
	::SetBkMode(hdcDst, TRANSPARENT);

}



int UiWidget_Sms::OnLButtonDown( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnLButtonDown(fwKeys, xPos, yPos);
    return iRet;
}

int UiWidget_Sms::OnLButtonUp( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnLButtonUp(fwKeys, xPos, yPos);

// 	m_CurIndex =  (m_CurIndex == 0) ? 1:0;
// 
// 	Invalidate();
// 	Update();

    return iRet;
}


int UiWidget_Sms::OnMouseMove( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnMouseMove(fwKeys, xPos, yPos);

    return iRet;
}

UiWidget_Sms::UiWidget_Sms()	
{
	m_CurIndex		= 0;
	m_TotalCount	= 0;
	m_iCurPos		=	0;

	ImagingHelper* NewSmsUp		= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_NEW_SMS_UP, true);
	ImagingHelper* NewSmsDown	= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_NEW_SMS_DOWN, true);

	ImagingHelper* EnterUp		= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_ENTER_EXE_UP, true);
	ImagingHelper* EnterDown	= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_ENTER_EXE_DOWN, true);

	ImagingHelper* FastUp		= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_FAST_SEND_UP, true);
	ImagingHelper* FastDown		= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_FAST_SEND_DOWN, true);

	ImagingHelper* ArrowLeftUp			= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_ARROW_LEFT_UP, true);
	ImagingHelper* ArrowLeftDown		= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_ARROW_LEFT_DOWN, true);

	ImagingHelper* ArrowRightUp			= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_ARROW_RIGHT_UP, true);
	ImagingHelper* ArrowRightDown		= m_imgContainer.LoadImage( GetModuleHandle(L"SmsWidget.dll"), IDR_ARROW_RIGHT_DOWN, true);

	//Arrow
	m_ArrowLeft.SetID( /*MZ_IDC_ARROW_LEFT*/SW_GetFreeID() );
	m_ArrowLeft.SetButtonType(MZC_BUTTON_NONE);
	m_ArrowLeft.SetPos( 0, 116, 40, 95 );
	m_ArrowLeft.SetImage_Normal( ArrowLeftUp );
	m_ArrowLeft.SetImage_Pressed( ArrowLeftDown );

	m_ArrowRight.SetID( /*MZ_IDC_ARROW_RIGHT*/SW_GetFreeID() );
	m_ArrowRight.SetButtonType(MZC_BUTTON_NONE);
	m_ArrowRight.SetPos( 360, 116, 40, 95 );
	m_ArrowRight.SetImage_Normal( ArrowRightUp );
	m_ArrowRight.SetImage_Pressed( ArrowRightDown );

	// 初始化窗口中的UiButton_Image按钮控件
	m_NewSms_btn.SetID( /*MZ_IDC_NEW_SMS*/SW_GetFreeID() );
	m_NewSms_btn.SetButtonType(MZC_BUTTON_NONE);
	m_NewSms_btn.SetPos( 202,230,61,58 );
	m_NewSms_btn.SetImage_Normal( NewSmsUp );
	m_NewSms_btn.SetImage_Pressed( NewSmsDown );

	m_NewSms_btn.SetMode(/*UI_BUTTON_IMAGE_MODE_ALWAYS_SHOW_NORMAL*/UI_BUTTON_IMAGE_MODE_NORMAL);
	m_NewSms_btn.SwapImageZOrder(true);
	m_NewSms_btn.SetTextColor(RGB(255,255,255));

	m_Enter_btn.SetID( /*MZ_IDC_ENTER*/SW_GetFreeID() );
	m_Enter_btn.SetButtonType(MZC_BUTTON_NONE);
	m_Enter_btn.SetPos( 262,230,61,58 );
	m_Enter_btn.SetImage_Normal( EnterUp );
	m_Enter_btn.SetImage_Pressed( EnterDown );

	m_Enter_btn.SetMode(/*UI_BUTTON_IMAGE_MODE_ALWAYS_SHOW_NORMAL*/UI_BUTTON_IMAGE_MODE_NORMAL);
	m_Enter_btn.SwapImageZOrder(true);
	m_Enter_btn.SetTextColor(RGB(255,255,255));

	m_Send_btn.SetID( /*MZ_IDC_SEND*/SW_GetFreeID() );
	m_Send_btn.SetButtonType(MZC_BUTTON_NONE);
	m_Send_btn.SetPos( 322,230,61,58 );
	m_Send_btn.SetImage_Normal( FastUp );
	m_Send_btn.SetImage_Pressed( FastDown );

	m_Send_btn.SetMode(/*UI_BUTTON_IMAGE_MODE_ALWAYS_SHOW_NORMAL*/UI_BUTTON_IMAGE_MODE_NORMAL);
	m_Send_btn.SwapImageZOrder(true);
	m_Send_btn.SetTextColor(RGB(255,255,255));


	UiWin::AddChild(&m_Send_btn);
	UiWin::AddChild(&m_Enter_btn);
	UiWin::AddChild(&m_NewSms_btn);
	UiWin::AddChild(&m_ArrowLeft);
	UiWin::AddChild(&m_ArrowRight);

	m_clCWidgetUiEdit.SetID( /*MZ_IDC_WIDGET_UIEDIT*/SW_GetFreeID() );
	m_clCWidgetUiEdit.EnableZoomIn( false );				/*设置编辑器是否支持放大镜*/
	m_clCWidgetUiEdit.OpenFace(true);						/*设置编辑器是否支持表情识别*/ 
	m_clCWidgetUiEdit.SetReadOnly( true );					/*控件文本可以读写*/
	m_clCWidgetUiEdit.EnableCopy ( true );
	m_clCWidgetUiEdit.SetAlignMode ( UIEDIT_ALIGN_LEFT );
	m_clCWidgetUiEdit.SetEditBgType( UI_EDIT_BGTYPE_NONE/*UI_EDIT_BGTYPE_FILL_WHITE*/ ); 
	m_clCWidgetUiEdit.SetFontColor( RGB(255, 255, 255 )); 
	m_clCWidgetUiEdit.SetFontSize( MZFS_MIN ); 
	m_clCWidgetUiEdit.EnableInsideScroll( true );

	m_clCWidgetUiEdit.SetPos( 40, 90, 320, 110 );
	m_clCWidgetUiEdit.SetRightInvalid(0);
	UiWin::AddChild( &m_clCWidgetUiEdit );

//time
	m_TimeEdit.SetID( /*MZ_IDC_WIDGET_UIEDIT*/SW_GetFreeID() );
	m_TimeEdit.EnableZoomIn( false );				/*设置编辑器是否支持放大镜*/
	m_TimeEdit.OpenFace(false);						/*设置编辑器是否支持表情识别*/ 
	m_TimeEdit.SetReadOnly( true );					/*控件文本可以读写*/
	m_TimeEdit.EnableCopy ( true );
	m_TimeEdit.SetAlignMode ( UIEDIT_ALIGN_LEFT );
	m_TimeEdit.SetEditBgType( UI_EDIT_BGTYPE_NONE/*UI_EDIT_BGTYPE_FILL_WHITE*/ ); 
	m_TimeEdit.SetFontColor( RGB(255, 255, 255 )); 
	m_TimeEdit.SetFontSize( MZFS_MIN ); 
	m_TimeEdit.EnableInsideScroll( false );

	m_TimeEdit.SetPos( 150, 201, 205, 35 );
	m_TimeEdit.SetRightInvalid(0);
	UiWin::AddChild( &m_TimeEdit );

//TelNo
	m_TelEdit.SetID( /*MZ_IDC_WIDGET_UIEDIT*/SW_GetFreeID() );
	m_TelEdit.EnableZoomIn( false );				/*设置编辑器是否支持放大镜*/
	m_TelEdit.OpenFace(false);						/*设置编辑器是否支持表情识别*/ 
	m_TelEdit.SetReadOnly( true );					/*控件文本可以读写*/
	m_TelEdit.EnableCopy ( true );
	m_TelEdit.SetAlignMode ( UIEDIT_ALIGN_LEFT );
	m_TelEdit.SetEditBgType( UI_EDIT_BGTYPE_NONE/*UI_EDIT_BGTYPE_FILL_WHITE*/ ); 
	m_TelEdit.SetFontColor( RGB(255, 255, 255 )); 
	m_TelEdit.SetFontSize( MZFS_MIN ); 
	m_TelEdit.EnableInsideScroll( false );

	m_TelEdit.SetPos( 170, 23, 180, 35 );
	m_TelEdit.SetRightInvalid(0);
	UiWin::AddChild( &m_TelEdit );

}

 void UiWidget_Sms::OnWmNotify( HWND hWnd, UINT nMessage, WPARAM wParam, LPARAM lParam )
 {
	 if( nMessage == MZ_WM_COMMAND )
	 {
		 int iID	=	LOWORD ( wParam );
		 
		 if( nMessage == MZ_WM_COMMAND )
		 {
			 if( m_NewSms_btn.GetID() == iID )
			 {
				////暂时这个Btn为取得未读短信////////////////////////////////////////////////
				HRESULT	hr	=	GetUnReadMsg();
				if ( FAILED ( hr ) )				return;
				hr	=	ShowMsg();
				//////////////////////////////////////////////////////////////////////////
			 }
			 else if( m_Enter_btn.GetID() == iID )
			 {
				 SHELLEXECUTEINFO ShellInfo;
				 memset(&ShellInfo, 0, sizeof(ShellInfo));
				 ShellInfo.cbSize = sizeof(ShellInfo);
				 ShellInfo.hwnd = NULL;
				 ShellInfo.lpVerb = _T("open");
				 ShellInfo.lpFile = _T("\\Program Files\\InfoCenterOfSmartPhone\\InfoCenterOfSmartPhone.exe");//文件全名（包含了整个路径）
				 ShellInfo.nShow = SW_SHOWNORMAL;
				 ShellInfo.fMask = SEE_MASK_NOCLOSEPROCESS; 
				 ShellExecuteEx(&ShellInfo);

			 }
			 else if( m_Send_btn.GetID() == iID )
			 {
				 SHELLEXECUTEINFO ShellInfo;
				 memset(&ShellInfo, 0, sizeof(ShellInfo));
				 ShellInfo.cbSize = sizeof(ShellInfo);
				 ShellInfo.hwnd = NULL;
				 ShellInfo.lpVerb = _T("open");
				 ShellInfo.lpFile = _T("\\Program Files\\InfoCenterOfSmartPhone\\InfoCenterOfSmartPhone.exe");
				 ShellInfo.lpParameters = _T( "NewSms" );
				 ShellInfo.nShow = SW_SHOWNORMAL;
				 ShellInfo.fMask = SEE_MASK_NOCLOSEPROCESS; 
				 ShellExecuteEx(&ShellInfo);
			 }
			 else if( m_ArrowLeft.GetID() == iID )
			 {
				-- m_iCurPos;
				if ( 0 > m_iCurPos )
				{
					m_iCurPos	=	m_vecstCoreItemData.size() - 1;
				}

				ShowMsg();
			 }
			 else if( m_ArrowRight.GetID() == iID )
			 {
				++ m_iCurPos;
				if ( m_iCurPos >= m_vecstCoreItemData.size() )
				{
					m_iCurPos	=	0;
				}

				ShowMsg();
			 }
			 else
			 {
				_ASSERT( 0 );
			 }
		 }	 
	 }

 }

 HRESULT	UiWidget_Sms::ShowMsg()
 {
	 if ( m_iCurPos <= m_vecstCoreItemData.size() )
	 {
		 m_clCWidgetUiEdit.SetText( (m_vecstCoreItemData[m_iCurPos])->bstrContent.m_str );
		 m_TimeEdit.SetText( (m_vecstCoreItemData[m_iCurPos])->bstrTime.m_str );
		 if ( NULL != (m_vecstCoreItemData[m_iCurPos])->bstrName.m_str )
		 {
			 m_TelEdit.SetText( (m_vecstCoreItemData[m_iCurPos])->bstrName.m_str );
		 }
		 else if ( NULL != (m_vecstCoreItemData[m_iCurPos])->bstrTelNo.m_str )
		 {
			 m_TelEdit.SetText( (m_vecstCoreItemData[m_iCurPos])->bstrTelNo.m_str );
		 }
		 else
		 {

		 }		
	 }

	 Invalidate();
	 Update();

	return	S_OK;
 }

 HRESULT	UiWidget_Sms::GetUnReadMsg()
 {
	 wchar_t	*pBuf		=	NULL;
	 long	lSize		=	0;
	 wchar_t	*pwcResult	=	NULL;

	 CCoreSmsUiCtrl		m_clCCoreSmsUiCtrl;
	 HRESULT	hr	=	m_clCCoreSmsUiCtrl.MakeUnReadRltListReq( &pBuf, &lSize );
	 if ( FAILED( hr ) )										return	E_FAIL;

	 CCoreService	*pCCoreService	=	CCoreService::GetInstance();
	 if ( NULL == pCCoreService )								return	E_FAIL;

	 hr	=	pCCoreService->Request( pBuf, &pwcResult );
	 if ( FAILED ( hr ) )										return	E_FAIL;

	 stCoreItemData	*pstCoreItemData	=	NULL;
	 long			lCnt				=	0;

	 hr	=	m_clCCoreSmsUiCtrl.MakeListRlt( pwcResult, &pstCoreItemData, &lCnt );
	 if ( FAILED( hr ) )							return	E_FAIL;

	 for ( int i = 0; i < lCnt; ++i )
	 {
		 m_vecstCoreItemData.push_back( &( pstCoreItemData[i] ) );
	 }

	 return	S_OK;
 }

UiWidget_Sms::~UiWidget_Sms()
{
	SW_ReleaseID( m_Send_btn.GetID() );
	SW_ReleaseID( m_Enter_btn.GetID() );
	SW_ReleaseID( m_NewSms_btn.GetID() );
	SW_ReleaseID( m_ArrowLeft.GetID() );
	SW_ReleaseID( m_ArrowRight.GetID() );
	SW_ReleaseID( m_clCWidgetUiEdit.GetID() );
	SW_ReleaseID( m_TimeEdit.GetID() );
	SW_ReleaseID( m_TelEdit.GetID() );

	int iSize	=	m_vecstCoreItemData.size();
	for ( int i = 0; i < iSize; ++i )
	{
		if ( NULL != m_vecstCoreItemData[i] )
		{
			delete	m_vecstCoreItemData[i];
			m_vecstCoreItemData[i]	=	NULL;
		}
	}

	m_vecstCoreItemData.clear();
}

bool UiWidget_Sms::StartWidget()
{
    UINT id = SW_GetFreeID();
    if(id==0)
        return false;

	Invalidate();
	Update();

//    SetID(id);
//    SetTimer(GetParentWnd(), GetID(),  1000, 0);
    return true;
}

void UiWidget_Sms::EndWidget()
{
    SW_ReleaseID(GetID());
}

int UiWidget_Sms::OnTimer( UINT_PTR nIDEvent )
{
    if (nIDEvent==GetID())
    {
    }

    return 0;
}

unsigned int UiWidget_Sms::Rand()
{
    static bool b = true;
    if(b)
    {
        m_rand = GetTickCount();
        b = false;
    }
    srand( m_rand);
    unsigned int r = rand();
    srand( (unsigned)r);
    r = rand();
    m_rand = r;
    return r;
}

void UiWidget_Sms::OnCalcItemSize( __out int &xSize, __out int &ySize )
{
    xSize = 4;
    ySize = 4;
}

void UiWidget_Sms::AddMessage( wchar_t* _phone, wchar_t* _content )
{
	m_PhoneNum.push_back( CMzString(_phone));
	m_Message.push_back( CMzString(_content));

	m_TotalCount++;

}




