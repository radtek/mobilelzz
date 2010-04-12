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
	
protected:
	CSQL_session*		m_pclSqlDBSession;

private:
	
};

#endif __BasicService_h__