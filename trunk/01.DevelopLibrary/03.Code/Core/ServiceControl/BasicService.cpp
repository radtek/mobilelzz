#include "stdafx.h"
#include "BasicService.h"

CBasicService::CBasicService()
{
	m_pclSqlDBSession = NULL;
}

CBasicService::~CBasicService()
{

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
	CSQL_sessionManager*  pm =CSQL_sessionManager::GetInstance();
	if( NULL == pm ) 
		return APP_Result_NullPointer;

	APP_Result hr = pm->Session_Connect(L"contact", L".\\Documents and Settings\\", L"sms.db", &m_pclSqlDBSession );
	if(FAILED(hr) || m_pclSqlDBSession == NULL)	
		return APP_Result_E_Fail;

	return APP_Result_S_OK;
}