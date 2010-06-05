#pragma once

#include <ShellWidget/ShellWidget.h>
#include <vector>
using namespace std;
#include "atlbase.h"

// Widget必须从UiWidget派生
// 根据需要，可重载StartWidget()以自定义Widget的启动行为

// UiWidget_Sms是一个简易的时钟Widget，显示当前时间，点击会变颜色。
class UiWidget_Sms: public UiWidget
{
public:
    UiWidget_Sms();
    virtual ~UiWidget_Sms();

    virtual void PaintWin(HDC hdcDst, RECT* prcWin, RECT* prcUpdate);

    virtual int OnLButtonDown(UINT fwKeys, int xPos, int yPos);
    virtual int OnLButtonUp(UINT fwKeys, int xPos, int yPos);
    virtual int OnMouseMove(UINT fwKeys, int xPos, int yPos);

    virtual bool StartWidget();
    virtual void EndWidget();

    virtual void OnCalcItemSize(__out int &xSize, __out int &ySize);

    virtual int OnTimer(UINT_PTR nIDEvent);
protected:

    unsigned int Rand();
	void AddMessage( wchar_t* _phone, wchar_t* _content );

    unsigned int m_rand;

    ImageContainer m_imgContainer;


	vector<CMzString>	m_PhoneNum;
	vector<CMzString>	m_Message;


    COLORREF m_clrA;
    COLORREF m_clrB;
    COLORREF m_clrC;

	unsigned int		m_CurIndex;

	unsigned int		m_TotalCount;

	UiButton_Image			m_NewSms_btn;
	UiButton_Image			m_Enter_btn;
	UiButton_Image			m_Send_btn;

	UiButton_Image			m_ArrowLeft;
	UiButton_Image			m_ArrowRight;

private:
};
