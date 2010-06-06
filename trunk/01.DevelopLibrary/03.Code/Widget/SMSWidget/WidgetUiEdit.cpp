#include "resource.h"
#include "WidgetUiEdit.h"

CWidgetUiEdit::CWidgetUiEdit()
{

}

CWidgetUiEdit::~CWidgetUiEdit()
{

}

void  CWidgetUiEdit::PaintWin ( HDC hdcDst, RECT *prcWin, RECT *prcUpdate )
{
 	UiEdit::PaintWin( hdcDst, prcWin, prcUpdate );
	
// 	ImagingHelper* pImgBg = m_imgContainer.LoadImage(	GetModuleHandle(L"SmsWidget.dll")
// 														/*GetMzResV2ModuleHandle()*/, 
// 														IDR_UIEDIT_BACKGROUND, true	);
//  	RECT rcBg = *prcWin;
//  	InflateRect(&rcBg, 0, 0);
// 	
// 	
//  
//  	pImgBg->Draw(hdcDst, &rcBg, true);
// 
// 	this->SetText( L"尊敬的用户：因您的手机外形难看，严重影响市容，本中心将在十分钟发射强信息摧毁该手机，望见谅。" );


}


CSmsBtnImage::CSmsBtnImage()
{

}
CSmsBtnImage::~CSmsBtnImage()
{

}


int  CSmsBtnImage::OnLButtonUp  (UINT fwKeys, int xPos, int yPos)
{
	return	UiButton_Image::OnLButtonUp( fwKeys, xPos, yPos );

}