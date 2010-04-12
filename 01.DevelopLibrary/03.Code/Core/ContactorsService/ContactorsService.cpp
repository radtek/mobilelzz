#include"stdafx.h"
#include"BasicService.h"
#include"ContactorsService.h"

#include "XmlStream.h"

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
	APP_Result appR = APP_Result_E_Fail;
	CXmlStream clXmlStream;
	long lXmlBufSize = wcslen(pwcsRequestXML);
	appR = clXmlStream.Load(pwcsRequestXML, lXmlBufSize);
	if ( FAILED_App(appR) ){
		return appR;
	}
	CXmlNode* pOperationNode = NULL;
	appR = clXmlStream.SelectNode(L"request/data/operation/", &pOperationNode);
	if ( FAILED_App(appR) ){
		return appR;
	}
	NodeAttribute_t* pOperationAttribute = NULL;
	long lAttributeCount = 0;
	appR = pOperationNode->GetNodeContent( NULL, NULL, &pOperationAttribute, &lAttributeCount );
	if ( FAILED_App(appR) ){
		return appR;
	}
	if ( 0 == wcscmp(L"type", pOperationAttribute->wcsName) ){
		if ( 0 == wcscmp(L"list", pOperationAttribute->wcsValue) ){
			m_stRequestData.enOperationType = Contactors_OperationType_List;
			if ( 0 == wcscmp(L"kind", pOperationAttribute[1].wcsName) ){
				if ( 0 == wcscmp(L"phoneno", pOperationAttribute[1].wcsValue) ){
					m_stRequestData.stOperationData.stOperationListData.enListKind = Contactors_ListKind_PhoneNo;
				}else if ( 0 == wcscmp(L"contactors", pOperationAttribute[1].wcsValue) ){
					m_stRequestData.stOperationData.stOperationListData.enListKind = Contactors_ListKind_Contactors;
				}				
			}
		}else if (0 == wcscmp(L"add", pOperationAttribute->wcsValue)){

		}else if (0 == wcscmp(L"delete", pOperationAttribute->wcsValue)){

		}else if (0 == wcscmp(L"edit", pOperationAttribute->wcsValue)){

		}else if (0 == wcscmp(L"detail", pOperationAttribute->wcsValue)){

		}
	}
	
	delete[] pOperationAttribute;
	delete pOperationNode;

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

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QFirstLetter, &m_pQFirstLetter );
	if( FAILED(hr) || m_pQFirstLetter == NULL ) 
		return hr;
	hr = m_pQFirstLetter->Prepare(SQL_GET_CONTACTS);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QContactorsList, &m_pQContactorsList );
	if( FAILED(hr) || m_pQContactorsList== NULL ) 
		return hr;
	hr = m_pQContactorsList->Prepare(SQL_GET_FIRSTLETER);
	if( FAILED(hr) ) 
		return hr;

	return APP_Result_S_OK;
}