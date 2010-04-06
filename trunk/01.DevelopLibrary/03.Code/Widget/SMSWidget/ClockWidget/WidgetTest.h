#pragma once

#include <ShellWidget/ShellWidget.h>
#include <vector>
using namespace std;

// Widget�����UiWidget����
// ������Ҫ��������StartWidget()���Զ���Widget��������Ϊ

// UiWidget_Clock��һ�����׵�ʱ��Widget����ʾ��ǰʱ�䣬��������ɫ��
class UiWidget_Clock: public UiWidget
{
public:
    UiWidget_Clock();
    virtual ~UiWidget_Clock();

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

	UiButton_Image			m_Edit_btn;
	UiButton_Image			m_Setup_btn;
	UiButton_Image			m_Send_btn;

private:
};
