#ifndef _WIDGET_UIEDIT_H_
#define _WIDGET_UIEDIT_H_

#include "SmsWidget.h"

class CWidgetUiEdit	:	public	UiEdit
{
	public:

		CWidgetUiEdit();

		virtual ~CWidgetUiEdit();

	public:

		virtual void  PaintWin ( HDC hdcDst, RECT *prcWin, RECT *prcUpdate );

	private:

		ImageContainer m_imgContainer;
};

class	CSmsBtnImage	:	public	UiButton_Image
{
	public:

		CSmsBtnImage();

		virtual ~CSmsBtnImage();

	protected:

		virtual int  OnLButtonUp  (UINT fwKeys, int xPos, int yPos);

	private:
};










#endif