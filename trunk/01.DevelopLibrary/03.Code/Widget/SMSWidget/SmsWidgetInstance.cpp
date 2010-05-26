#include "SmsWidgetInstance.h"
#include "resource.h"

#define MZ_IDC_NEW_SMS  101
#define MZ_IDC_SEND		102
#define MZ_IDC_ENTER	103

#define	MZ_IDC_ARROW_LEFT	104
#define	MZ_IDC_ARROW_RIGHT	105

void UiWidget_Sms::PaintWin( HDC hdcDst, RECT* prcWin, RECT* prcUpdate )
{
    COLORREF clr = RGB(255,0,0);
    RECT rcWin = *prcWin;
    RECT rcBg = *prcWin;


	/*�軭�������*/	
	RECT rcImg;
	rcImg.left	= rcBg.left;
	rcImg.top	= rcBg.top;
	rcImg.right	= rcImg.left + /*400*/400;
	rcImg.bottom= rcImg.top + /*240*/300;

    ImagingHelper* pImgBg = m_imgContainer.LoadImage(GetModuleHandle(L"SmsWidget.dll"), IDR_WIDGET_ALL_BACK, true);
    pImgBg->Draw(hdcDst, &rcImg, true);

	::SetTextColor(hdcDst, RGB(27,163,228));
	::SetBkMode(hdcDst, TRANSPARENT);


	/*����Ϣ�������軭�ڲ����*/

// 	HFONT font = FontHelper::GetFont(20, FW_NORMAL,0,0,0,FONT_QUALITY_CLEARTYPE);
// 	HGDIOBJ oldfont = SelectObject(hdcDst, font);
// 
// 	RECT rcText;
// 	rcText.left		= rcBg.left +30 ;
// 	rcText.top		= rcBg.top  +70;
// 	rcText.right	= rcBg.left + 350;
// 	rcText.bottom	= rcBg.top +  150;
// 
// 	int Height = MzDrawText(hdcDst, m_Message[m_CurIndex].C_Str(), &rcText, DT_WORDBREAK |DT_CALCRECT|DT_LEFT);
// 	MzDrawText(hdcDst, m_Message[m_CurIndex].C_Str(), &rcText, DT_WORDBREAK|DT_LEFT);
// 	SelectObject(hdcDst, oldfont);


	/*���ֻ������軭�ڲ����*/
// 	font = FontHelper::GetFont(25, FW_NORMAL,0,0,0,FONT_QUALITY_DEFAULT);
// 	oldfont = SelectObject(hdcDst, font);
// 	
// 	RECT rcNum;
// 	rcNum.left	= rcBg.left +55 ;
// 	rcNum.top	= rcBg.top  ;
// 	rcNum.right	= rcBg.left + 290;
// 	rcNum.bottom= rcBg.top +  60;
// 
// 	MzDrawText(hdcDst, m_PhoneNum[m_CurIndex].C_Str(), &rcNum, DT_VCENTER|DT_LEFT);
// 	SelectObject(hdcDst, oldfont);



	/*�軭��ǰ���������ܼ���*/
// 	font = FontHelper::GetFont(25, FW_NORMAL,0,0,0,FONT_QUALITY_DEFAULT);
// 	oldfont = SelectObject(hdcDst, font);
// 
// 	RECT rcCnt;
// 	rcCnt.left	= rcBg.left +40 ;
// 	rcCnt.right	= rcBg.left + 100;
// 	rcCnt.top	= rcBg.top +200;
// 	rcCnt.bottom = rcCnt.top +40;
// 	
// 	WCHAR ToTalCount[15] ={0};
// 	wsprintf(ToTalCount,L"%d/%d", m_CurIndex+1, m_TotalCount);
// 	MzDrawText(hdcDst, ToTalCount, &rcCnt, DT_VCENTER|DT_LEFT);
// 
// 	SelectObject(hdcDst, oldfont);





}


int UiWidget_Sms::OnLButtonDown( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnLButtonDown(fwKeys, xPos, yPos);

    return iRet;
}

int UiWidget_Sms::OnLButtonUp( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnLButtonUp(fwKeys, xPos, yPos);

	m_CurIndex =  (m_CurIndex == 0) ? 1:0;

	Invalidate();
	Update();

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
	m_ArrowLeft.SetID( MZ_IDC_ARROW_LEFT );
	m_ArrowLeft.SetButtonType(MZC_BUTTON_NONE);
	m_ArrowLeft.SetPos( 0, 116, 40, 95 );
	m_ArrowLeft.SetImage_Normal( ArrowLeftUp );
	m_ArrowLeft.SetImage_Pressed( ArrowLeftDown );

	m_ArrowRight.SetID( MZ_IDC_ARROW_RIGHT );
	m_ArrowRight.SetButtonType(MZC_BUTTON_NONE);
	m_ArrowRight.SetPos( 360, 116, 40, 95 );
	m_ArrowRight.SetImage_Normal( ArrowRightUp );
	m_ArrowRight.SetImage_Pressed( ArrowRightDown );

	// ��ʼ�������е�UiButton_Image��ť�ؼ�
	m_NewSms_btn.SetID( MZ_IDC_NEW_SMS );
	m_NewSms_btn.SetButtonType(MZC_BUTTON_NONE);
	m_NewSms_btn.SetPos( 202,230,61,58 );
	m_NewSms_btn.SetImage_Normal( NewSmsUp );
	m_NewSms_btn.SetImage_Pressed( NewSmsDown );

	m_NewSms_btn.SetMode(/*UI_BUTTON_IMAGE_MODE_ALWAYS_SHOW_NORMAL*/UI_BUTTON_IMAGE_MODE_NORMAL);
	m_NewSms_btn.SwapImageZOrder(true);
	m_NewSms_btn.SetTextColor(RGB(255,255,255));

	m_Enter_btn.SetID( MZ_IDC_ENTER );
	m_Enter_btn.SetButtonType(MZC_BUTTON_NONE);
	m_Enter_btn.SetPos( 262,230,61,58 );
	m_Enter_btn.SetImage_Normal( EnterUp );
	m_Enter_btn.SetImage_Pressed( EnterDown );

	m_Enter_btn.SetMode(/*UI_BUTTON_IMAGE_MODE_ALWAYS_SHOW_NORMAL*/UI_BUTTON_IMAGE_MODE_NORMAL);
	m_Enter_btn.SwapImageZOrder(true);
	m_Enter_btn.SetTextColor(RGB(255,255,255));

	m_Send_btn.SetID( MZ_IDC_SEND );
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

}

UiWidget_Sms::~UiWidget_Sms()
{

}

bool UiWidget_Sms::StartWidget()
{
    UINT id = SW_GetFreeID();
    if(id==0)
        return false;

// 	AddMessage(L"10086", L"�𾴵��û����������ֻ������ѿ�������Ӱ�����ݣ������Ľ���ʮ���ӷ���ǿ��Ϣ�ݻٸ��ֻ��������¡�");
// 	AddMessage(L"13587458741", L"����Ĵ����룺�������˶��õ������Ϸ׷������ϣ���������������ȫ�������Żؽ̡�");
// 
// 	m_CurIndex = 0;

	Invalidate();
	Update();

    SetID(id);
    SetTimer(GetParentWnd(), GetID(),  1000, 0);
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
	//	m_CurIndex =  (m_CurIndex == 0) ? 1:0;

      //  Invalidate();
       // Update();
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




