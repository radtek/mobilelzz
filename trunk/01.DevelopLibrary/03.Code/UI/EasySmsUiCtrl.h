#ifndef __EASYSMSUICTRL_h__
#define __EASYSMSUICTRL_h__

#include "../CommonLib/Xml/XmlStream.h"

class CEasySmsUiCtrl
{
	public:

		CEasySmsUiCtrl( void );

		virtual ~CEasySmsUiCtrl( void );

	public:
	
		HRESULT		MakeUnReadRltListReq(  wchar_t **ppBuf, long *lSize );

	public:

	private:
		
		HRESULT		MakeNode( CXmlStream	*pCXmlStream, wchar_t *pNodePath, NodeAttribute_t *pNodeAttribute_t, long lCnt );

};

#endif
