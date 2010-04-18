#ifndef __EASYSMSUICTRL_h__
#define __EASYSMSUICTRL_h__

//#include "../Core/ServiceControl/CoreService.h"
#include "EasySmsWndBase.h"
#include "../CommonLib/Xml/XmlStream.h"
#include "../Core/ServiceControl/BasicService.h"
#include "../CommonLib/FunctionLib/RequestXmlOperator.h"
#include "../Core/SmsService/SmsService.h"


class CEasySmsUiCtrl
{
	public:

		CEasySmsUiCtrl( void );

		virtual ~CEasySmsUiCtrl( void );

	public:
	
		HRESULT		MakeUnReadRltListReq (  wchar_t **ppBuf, long *lSize );

		HRESULT		MakeUnReadListRlt ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream );

	public:

	private:
		
		HRESULT		MakeNode( CXmlStream	*pCXmlStream, wchar_t *pNodePath, NodeAttribute_t *pNodeAttribute_t, long lCnt );

		HRESULT		GetListCnt( CXmlStream	*pCXmlStream, long	&lCnt );

		HRESULT		AppendList( CXmlNode *pCXmlNode, CEasySmsListBase &clCEasySmsListBase );

	private:

		ImageContainer		m_imgContainer;

};

#endif
