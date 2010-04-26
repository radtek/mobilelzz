#include "stdafx.h"
#include "BasicService.h"

CSQL_sessionManager*	CBasicService::m_pclSqlSessionManager = NULL;

CBasicService::CBasicService()
{
//	m_pclSqlSessionManager = NULL;
}

CBasicService::~CBasicService()
{
	m_pclSqlSessionManager->ReleaseInstance();
}

APP_Result CBasicService::Excute(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
{
	APP_Result appR = APP_Result_E_Fail;
	appR = MakeParam( pwcsRequestXML );
	if (FAILED_App(appR)){
		return appR;
	}
	appR = ExcuteParam( pwcsRequestXML, ppwcsResultXML );
	if (FAILED_App(appR)){
		return appR;
	}

	return APP_Result_S_OK;
}

APP_Result CBasicService::MakeParam(wchar_t* pwcsRequestXML)
{
	

	return APP_Result_S_OK;
}

APP_Result CBasicService::ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
{
	

	return APP_Result_S_OK;
}

APP_Result CBasicService::MakeResult(wchar_t** ppwcsResultXML)
{


	return APP_Result_S_OK;
}

APP_Result CBasicService::Initialize()
{
	if ( NULL == m_pclSqlSessionManager ){
		m_pclSqlSessionManager =CSQL_sessionManager::GetInstance();
		if( NULL == m_pclSqlSessionManager ) 
			return APP_Result_NullPointer;
	}	

	return APP_Result_S_OK;
}

APP_Result CBasicService::CreateQuery(CSQL_session* pclSqlDBSession, wchar_t* pSqlCommand, CSQL_SmartQuery& spQuery)
{
	//SELECT name FROM sqlite_master WHERE name='" + tableName + "'";
	APP_Result hr = APP_Result_E_Fail;
	spQuery.Initialize(pclSqlDBSession);
	hr = pclSqlDBSession->Query_Create(NULL, &spQuery );
	if( FAILED(hr) || spQuery.Get() == NULL ) 
		return hr;
	hr = spQuery->Prepare(pSqlCommand);
	if( FAILED(hr) ) 
		return hr;

	return APP_Result_S_OK;
}