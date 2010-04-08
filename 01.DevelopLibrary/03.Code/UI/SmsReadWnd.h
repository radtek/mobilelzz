#ifndef __SMSREADWND_h__
#define __SMSREADWND_h__

#include"ContactorsWnd.h"
#include"NewSmsWnd.h"


class CSmsReadWnd	:	public	CMzWndEx
{
	
	MZ_DECLARE_DYNAMIC( CSmsReadWnd );

	public:

		CSmsReadWnd(void);

		virtual ~CSmsReadWnd(void);

	public:

		virtual	BOOL OnInitDialog();

		virtual void OnMzCommand( WPARAM wParam, LPARAM lParam );

	private:
		
	protected:

	private:

};

#endif
