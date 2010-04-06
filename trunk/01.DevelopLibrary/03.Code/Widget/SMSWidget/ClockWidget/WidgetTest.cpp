#include "WidgetTest.h"
#include "resource.h"
void UiWidget_Clock::PaintWin( HDC hdcDst, RECT* prcWin, RECT* prcUpdate )
{
    COLORREF clr = RGB(255,0,0);
    RECT rcWin = *prcWin;
    RECT rcBg = *prcWin;


	/*描画插件背景*/	
	RECT rcImg;
	rcImg.left	= rcBg.left;
	rcImg.top	= rcBg.top;
	rcImg.right	= rcImg.left + 400;
	rcImg.bottom= rcImg.top + 240;

    ImagingHelper* pImgBg = m_imgContainer.LoadImage(GetModuleHandle(L"ClockWidget.dll"), IDR_JPG_PIC1, true);
    pImgBg->Draw(hdcDst, &rcImg, true);

	::SetTextColor(hdcDst, RGB(27,163,228));
	::SetBkMode(hdcDst, TRANSPARENT);


	/*将消息的内容描画在插件上*/

	HFONT font = FontHelper::GetFont(20, FW_NORMAL,0,0,0,FONT_QUALITY_CLEARTYPE);
	HGDIOBJ oldfont = SelectObject(hdcDst, font);

	RECT rcText;
	rcText.left		= rcBg.left +30 ;
	rcText.top		= rcBg.top  +70;
	rcText.right	= rcBg.left + 350;
	rcText.bottom	= rcBg.top +  150;

	int Height = MzDrawText(hdcDst, m_Message[m_CurIndex].C_Str(), &rcText, DT_WORDBREAK |DT_CALCRECT|DT_LEFT);
	MzDrawText(hdcDst, m_Message[m_CurIndex].C_Str(), &rcText, DT_WORDBREAK|DT_LEFT);
	SelectObject(hdcDst, oldfont);


	/*将手机号码描画在插件上*/
	font = FontHelper::GetFont(25, FW_NORMAL,0,0,0,FONT_QUALITY_DEFAULT);
	oldfont = SelectObject(hdcDst, font);
	
	RECT rcNum;
	rcNum.left	= rcBg.left +55 ;
	rcNum.top	= rcBg.top  ;
	rcNum.right	= rcBg.left + 290;
	rcNum.bottom= rcBg.top +  60;

	MzDrawText(hdcDst, m_PhoneNum[m_CurIndex].C_Str(), &rcNum, DT_VCENTER|DT_LEFT);
	SelectObject(hdcDst, oldfont);



	/*描画当前的索引和总件数*/
	font = FontHelper::GetFont(25, FW_NORMAL,0,0,0,FONT_QUALITY_DEFAULT);
	oldfont = SelectObject(hdcDst, font);

	RECT rcCnt;
	rcCnt.left	= rcBg.left +40 ;
	rcCnt.right	= rcBg.left + 100;
	rcCnt.top	= rcBg.top +200;
	rcCnt.bottom = rcCnt.top +40;
	
	WCHAR ToTalCount[15] ={0};
	wsprintf(ToTalCount,L"%d/%d", m_CurIndex+1, m_TotalCount);
	MzDrawText(hdcDst, ToTalCount, &rcCnt, DT_VCENTER|DT_LEFT);

	SelectObject(hdcDst, oldfont);



}


int UiWidget_Clock::OnLButtonDown( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnLButtonDown(fwKeys, xPos, yPos);

    return iRet;
}

int UiWidget_Clock::OnLButtonUp( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnLButtonUp(fwKeys, xPos, yPos);

	m_CurIndex =  (m_CurIndex == 0) ? 1:0;

	Invalidate();
	Update();

    return iRet;
}

int UiWidget_Clock::OnMouseMove( UINT fwKeys, int xPos, int yPos )
{
    int iRet = UiWidget::OnMouseMove(fwKeys, xPos, yPos);

    return iRet;
}

UiWidget_Clock::UiWidget_Clock()
{
	m_CurIndex = 0;
	m_TotalCount = 0;
}

UiWidget_Clock::~UiWidget_Clock()
{

}

bool UiWidget_Clock::StartWidget()
{
    UINT id = SW_GetFreeID();
    if(id==0)
        return false;

	AddMessage(L"10086", L"尊敬的用户：因您的手机外形难看，严重影响市容，本中心将在十分钟发射强信息摧毁该手机，望见谅。");
	AddMessage(L"13587458741", L"猪的四大理想：四周栏杆都烂掉，天上纷纷下饲料，世上屠夫都死掉，全国人民信回教。");

	m_CurIndex = 0;

	Invalidate();
	Update();

    SetID(id);
    SetTimer(GetParentWnd(), GetID(),  1000, 0);
    return true;
}

void UiWidget_Clock::EndWidget()
{
    SW_ReleaseID(GetID());
}

int UiWidget_Clock::OnTimer( UINT_PTR nIDEvent )
{
    if (nIDEvent==GetID())
    {
	//	m_CurIndex =  (m_CurIndex == 0) ? 1:0;

      //  Invalidate();
       // Update();
    }

    return 0;
}

unsigned int UiWidget_Clock::Rand()
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

void UiWidget_Clock::OnCalcItemSize( __out int &xSize, __out int &ySize )
{
    xSize = 4;
    ySize = 4;
}

void UiWidget_Clock::AddMessage( wchar_t* _phone, wchar_t* _content )
{
	m_PhoneNum.push_back( CMzString(_phone));
	m_Message.push_back( CMzString(_content));

	m_TotalCount++;




}




