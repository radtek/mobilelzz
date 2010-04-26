#ifndef __BasicService_h__
#define __BasicService_h__

#include "CSearch_SQL_base_handler.h"

class CBasicService
{
public:
	CBasicService();
	~CBasicService();
	
	APP_Result Excute(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);
	virtual APP_Result Initialize();
protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);
	virtual APP_Result MakeResult(wchar_t** ppwcsResultXML);	
	APP_Result CreateQuery(CSQL_session* pclSqlDBSession, wchar_t* pSqlCommand, CSQL_SmartQuery& spQuery);
protected:
	
	static CSQL_sessionManager*	m_pclSqlSessionManager;
	
private:
	
};

#endif __BasicService_h__