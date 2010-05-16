#ifndef __EASYSMSUICTRL_h__
#define __EASYSMSUICTRL_h__

//#include "../Core/ServiceControl/CoreService.h"
#include "EasySmsWndBase.h"
#include "../CommonLib/Xml/XmlStream.h"
#include "../Core/ServiceControl/BasicService.h"
#include "../CommonLib/FunctionLib/RequestXmlOperator.h"
#include "../Core/SmsService/SmsService.h"

enum	EnType
{
	EN_NONE		=	0,
	EN_DOUBLE,
	EN_LONG,
	EN_WCHAR
};

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

		HRESULT		MakeSendSmsInfo		( wchar_t **ppBuf, long *lSize, wchar_t *pwcSmsInfo );

	public:

	private:
		
		HRESULT		MakeNode(	CXmlStream	*pCXmlStream, wchar_t *pNodePath, NodeAttribute_t *pNodeAttribute_t,
								long lCnt, void *pValue = NULL, EnType _type = EN_NONE );

		HRESULT		GetListCnt( CXmlStream	*pCXmlStream, long	&lCnt );

		HRESULT		AppendUnReadList( CXmlNode *pCXmlNode, CEasySmsListBase &clCEasySmsListBase );

		HRESULT		AppendLookCtorList( CXmlNode *pCXmlNode, CEasySmsListBase &clCEasySmsListBase );

	private:

		ImageContainer		m_imgContainer;

};

#endif
