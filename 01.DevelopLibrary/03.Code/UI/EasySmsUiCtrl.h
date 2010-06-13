#ifndef __EASYSMSUICTRL_h__
#define __EASYSMSUICTRL_h__

//#include "../Core/ServiceControl/CoreService.h"
#include "EasySmsWndBase.h"
//#include "../CommonLib/Xml/TinySrc/tinyxml.h"
/*
#include "../CommonLib/Xml/XmlStream.h"
#include "../Core/ServiceControl/BasicService.h"
#include "../CommonLib/FunctionLib/RequestXmlOperator.h"
#include "../CommonLib/FunctionLib/ResultXmlOperator.h"
#include "../Core/SmsService/SmsService.h"
*/

class CEasySmsUiCtrl
{
	public:

		CEasySmsUiCtrl( void );

		virtual ~CEasySmsUiCtrl( void );

	public:
	
		HRESULT		MakeUnReadRltListReq (  wchar_t **ppBuf, long *lSize );
		HRESULT		MakeUnReadListRlt    ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream );

		HRESULT		MakeCtorRltListReq (  wchar_t **ppBuf, long *lSize );
		HRESULT		MakeCtorRltList    ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream );

		HRESULT		MakeMsgRltListReq( wchar_t **ppBuf, long *lSize, long lPid , wchar_t *pDecode = NULL );
		HRESULT		MakeMsgRltList   ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream );

		HRESULT		MakeDetailRltListReq( wchar_t **ppBuf, long *lSize, long lSid, wchar_t *pDecode = NULL );

		HRESULT		MakeSendSmsInfo		( wchar_t **ppBuf, long *lSize, wchar_t *pwcSmsInfo, wchar_t* pwcsNumber );

		HRESULT		MakeDeleteSmsInfo	 ( OUT wchar_t **ppBuf, OUT long *lSize, IN long *plSid, IN long lCnt );

		HRESULT		MakeUpdateSmsStatusReq( wchar_t **ppBuf, long *lSize, long lSid, long lLock, long lRead );

		HRESULT		MakePassWordStatusReq ( wchar_t **ppBuf, long *lSize, 
											long lPid, wchar_t* pwcDataKind, 
											wchar_t* pwcCode, wchar_t* pwcNewCode );

		HRESULT		MakeDetailReq ( wchar_t **ppBuf, long *lSize, long lSid, wchar_t* pwcCode );

	private:

		ImageContainer		m_imgContainer;

		CCoreSmsUiCtrl		m_clCCoreSmsUiCtrl;



};

#endif
