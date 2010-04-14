#include"stdafx.h"
#include"BasicService.h"
#include "XmlStream.h"
#include "RequestXmlOperator.h"
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
	APP_Result hr = APP_Result_E_Fail;
	CRequestXmlOperator clReqXmlOpe;
	clReqXmlOpe.Initialize(pwcsRequestXML);
	//Get operation type
	wchar_t awcsOpeType[20] = L"";
	hr = clReqXmlOpe.GetOperationType(awcsOpeType, sizeof(awcsOpeType)/sizeof(awcsOpeType[0]));
	if ( FAILED_App(hr) ){
		return hr;
	}

	CXmlStream clResultXml;
	clResultXml.Initialize();
	CXmlNode* pNode = NULL;
	hr = clResultXml.SelectNode(L"result/data/", &pNode);
	if ( FAILED_App(hr) ){
		return hr;
	}
	auto_ptr<CXmlNode> sp(pNode);
	NodeAttribute_t stTemp;
	F_wcscpyn(stTemp.wcsName, L"type", sizeof(stTemp.wcsName)/sizeof(stTemp.wcsName[0]));
	F_wcscpyn(stTemp.wcsValue, L"contactors", sizeof(stTemp.wcsValue)/sizeof(stTemp.wcsValue[0]));
	hr = pNode->SetNodeContent( NULL, NULL, &stTemp, 1 );
	if ( FAILED_App(hr) ){
		return hr;
	}
	if ( 0 == wcscmp(L"list", awcsOpeType) ){
		hr = ExcuteForList(clReqXmlOpe, clResultXml);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}

	wchar_t* pResult = NULL;
	long lSize = 0;
	hr = clResultXml.GetXmlStream(&pResult, &lSize);
	if ( SUCCEEDED_App(hr) ){
		*ppwcsResultXML = pResult;
	}
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

void CContactorsService::MakeFirstLetter(wchar_t* pwcsFirstLetter, long lPID )
{
	m_pQFirstLetter->Reset();
	m_pQFirstLetter->Bind(1, lPID);
	m_pQFirstLetter->Step();
	m_pQFirstLetter->Step();

	wchar_t* pTemp = NULL;
	m_pQFirstLetter->GetField(0,&pTemp);
	pwcsFirstLetter[0] = pTemp[0];

}

APP_Result CContactorsService::ExcuteForList(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{
	APP_Result hr = APP_Result_E_Fail;
	ListCondition* pConditions = NULL;
	long lConditionCount = 0;
	hr = clXmlOpe.GetListConditions(&pConditions, &lConditionCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<ListCondition> sp(pConditions, lConditionCount);

	CXmlNode* pNode = NULL;
	hr = clResultXml.SelectNode(L"result/data/data/", &pNode);
	if ( FAILED_App(hr) ){
		return hr;
	}
	auto_ptr<CXmlNode> spNode(pNode);
	NodeAttribute_t stTemp[3];
	memset(stTemp, 0x0, sizeof(stTemp));
	F_wcscpyn(stTemp[0].wcsName, L"type", sizeof(stTemp[0].wcsName)/sizeof(stTemp[0].wcsName[0]));
	F_wcscpyn(stTemp[0].wcsValue, L"list", sizeof(stTemp[0].wcsValue)/sizeof(stTemp[0].wcsValue[0]));
	F_wcscpyn(stTemp[1].wcsName, L"count", sizeof(stTemp[1].wcsName)/sizeof(stTemp[1].wcsName[0]));
	F_wcscpyn(stTemp[1].wcsValue, L"0", sizeof(stTemp[1].wcsValue)/sizeof(stTemp[1].wcsValue[0]));
	F_wcscpyn(stTemp[2].wcsName, L"kind", sizeof(stTemp[2].wcsName)/sizeof(stTemp[2].wcsName[0]));
	F_wcscpyn(stTemp[2].wcsValue, L"contactors", sizeof(stTemp[2].wcsValue)/sizeof(stTemp[2].wcsValue[0]));
	hr = pNode->SetNodeContent( NULL, NULL, stTemp, 3 );
	if ( FAILED_App(hr) ){
		return hr;
	}

	hr = m_pQContactorsList->Step();
	long lListCount = 0;
	while ( hr != APP_Result_E_Fail && hr != APP_Result_S_OK )
	{
		lListCount++;
		long lPID = 0;
		m_pQContactorsList->GetField(0, &lPID);
		wchar_t awcsID[5] = L"";
		wsprintf(awcsID, L"%d", lPID);
		CXmlNode clNodeID(L"id");
		clNodeID.SetNodeContent(NULL, awcsID, NULL, 0);

		wchar_t* PName = NULL;
		m_pQContactorsList->GetField(1, &PName);
		CXmlNode clNodeName(L"name");
		clNodeName.SetNodeContent(NULL, PName, NULL, 0);

		wchar_t awcsFirstLetter[2] = L"";
		MakeFirstLetter(awcsFirstLetter, lPID);
		CXmlNode clNodeFirstLetter(L"firstletter");
		clNodeFirstLetter.SetNodeContent(NULL, awcsFirstLetter, NULL, 0);

		wchar_t* pNumber = NULL;
		m_pQContactorsList->GetField(4, &pNumber);		
		CXmlNode clNodeNumber(L"defaultno");
		clNodeNumber.SetNodeContent(NULL, pNumber, NULL, 0);			

		CXmlNode clNodeRec(L"rec");
		clNodeRec.AppendNode(&clNodeID);
		clNodeRec.AppendNode(&clNodeName);
		clNodeRec.AppendNode(&clNodeFirstLetter);
		clNodeRec.AppendNode(&clNodeNumber);

		pNode->AppendNode(&clNodeRec);

		hr = m_pQContactorsList->Step();
	}
	F_wcscpyn(stTemp[0].wcsName, L"count", sizeof(stTemp[0].wcsName)/sizeof(stTemp[0].wcsName[0]));
	wchar_t awcsCount[5] = L"";
	wsprintf(awcsCount, L"%d", lListCount);
	F_wcscpyn(stTemp[0].wcsValue, awcsCount, sizeof(stTemp[0].wcsValue)/sizeof(stTemp[0].wcsValue[0]));
	hr = pNode->SetNodeContent( NULL, NULL, stTemp, 1 );
	if ( FAILED_App(hr) ){
		return hr;
	}

	return APP_Result_S_OK;
}