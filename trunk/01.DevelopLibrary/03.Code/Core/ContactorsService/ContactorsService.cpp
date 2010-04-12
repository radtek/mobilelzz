#include"stdafx.h"
#include"BasicService.h"
#include"ContactorsService.h"

#define   SQL_GET_CONTACTS  L"select ABPerson.ROWID, ABPerson.Name , ABPhones.*  from ABPerson, ABPhones where ABPerson.ROWID = ABPhones.record_id "
#define   SQL_GET_FIRSTLETER  L"select token  from ABLookupFirstLetter where source = ?"


CContactorsService::CContactorsService()
{
	m_pQFirstLetter = NULL;
	m_pQContactorsList = NULL;
}

CContactorsService::~CContactorsService()
{
}

APP_Result CContactorsService::MakeParam(wchar_t* pwcsRequestXML)
{

	return APP_Result_S_OK;
}
APP_Result CContactorsService::ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
{
	//Get operation type
	
	//Get Info

	//excute

	//enum result

	return APP_Result_S_OK;
}
APP_Result CContactorsService::MakeResult(wchar_t** ppwcsResultXML)
{
	
	return APP_Result_S_OK;
}

APP_Result CContactorsService::Initialize()
{
	APP_Result hr = APP_Result_E_Fail;
	hr = CBasicService::Initialize();
	if(FAILED(hr))	
		return hr;

	hr = m_pclSqlDBSession->Query_Create(&m_lID_QFirstLetter, &m_pQFirstLetter );
	if( FAILED(hr) || m_pQFirstLetter == NULL ) 
		return hr;
	hr = m_pQFirstLetter->Prepare(SQL_GET_CONTACTS);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create(&m_lID_QContactorsList, &m_pQContactorsList );
	if( FAILED(hr) || m_pQContactorsList== NULL ) 
		return hr;
	hr = m_pQContactorsList->Prepare(SQL_GET_FIRSTLETER);
	if( FAILED(hr) ) 
		return hr;

	return APP_Result_S_OK;
}