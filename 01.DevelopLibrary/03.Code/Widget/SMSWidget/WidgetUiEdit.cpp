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
// 	this->SetText( L"�𾴵��û����������ֻ������ѿ�������Ӱ�����ݣ������Ľ���ʮ���ӷ���ǿ��Ϣ�ݻٸ��ֻ��������¡�" );


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