#pragma once

#include <ShellWidget/ShellWidget.h>
#include <vector>
using namespace std;
#include "atlbase.h"

#include "WidgetUiEdit.h"
#include "../../App/CommonLib/CommonLib.h"
#include "../../CommonLib/FunctionLib/Errors.h"
#include "../../Core/ServiceControl/CoreService.h"
#include "../../CommonLib/Xml/XmlStream.h"
#include "../../CommonLib/FunctionLib/ResultXmlOperator.h"
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

	virtual void OnWmNotify( HWND hWnd, UINT nMessage, WPARAM wParam, LPARAM lParam );

	HRESULT	GetUnReadMsg();

    unsigned int m_rand;

    ImageContainer m_imgContainer;


	vector<CMzString>	m_PhoneNum;
	vector<CMzString>	m_Message;


    COLORREF m_clrA;
    COLORREF m_clrB;
    COLORREF m_clrC;

	unsigned int		m_CurIndex;

	unsigned int		m_TotalCount;

	UiButton_Image/*CSmsBtnImage*/			m_NewSms_btn;
	UiButton_Image/*CSmsBtnImage*/			m_Enter_btn;
	UiButton_Image/*CSmsBtnImage*/			m_Send_btn;

	UiButton_Image/*CSmsBtnImage*/			m_ArrowLeft;
	UiButton_Image/*CSmsBtnImage*/			m_ArrowRight;

	/*CWidgetUiEdit*/UiEdit			m_clCWidgetUiEdit;
	UiEdit 							m_TimeEdit;
	UiEdit							m_TelEdit;

private:

	vector<  stCoreItemData	* >		m_vecstCoreItemData;

	int								m_iCurPos;
};
