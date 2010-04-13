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
	auto_ptr<CXmlNode> sp(pOperationNode);
	NodeAttribute_t* pOperationAttribute = NULL;
	long lAttributeCount = 0;
	appR = pOperationNode->GetNodeContent( NULL, NULL, &pOperationAttribute, &lAttributeCount );
	if ( FAILED_App(appR) ){
		return appR;
	}
	CDynamicArray<NodeAttribute_t> spTempMem(pOperationAttribute, lAttributeCount);
	
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
			m_stRequestData.stOperationData.stOperationListData.enListSortType =  Contactors_ListSortType_Name;
			CXmlNode* pConditionNode = NULL;
			appR = clXmlStream.SelectNode(L"request/data/operation/condition/", &pConditionNode);
			if ( SUCCEEDED_App(appR) ){
				auto_ptr<CXmlNode> sp(pConditionNode);
				NodeAttribute_t* pConditionAttribute = NULL;
				long lAttributeCount = 0;
				appR = pOperationNode->GetNodeContent( NULL, NULL, &pConditionAttribute, &lAttributeCount );
				if ( SUCCEEDED_App(appR) ){
					CDynamicArray<NodeAttribute_t> spTempMem(pConditionAttribute, lAttributeCount);
					if ( 0 == wcscmp(L"sort", pOperationAttribute->wcsName) ){
						if ( 0 == wcscmp(L"name", pOperationAttribute->wcsValue) ){
							m_stRequestData.stOperationData.stOperationListData.enListSortType =  Contactors_ListSortType_Name;
						}
					}
				}
			}
			
		}else if (0 == wcscmp(L"add", pOperationAttribute->wcsValue)){

		}else if (0 == wcscmp(L"delete", pOperationAttribute->wcsValue)){

		}else if (0 == wcscmp(L"edit", pOperationAttribute->wcsValue)){

		}else if (0 == wcscmp(L"detail", pOperationAttribute->wcsValue)){

		}
	}

	return APP_Result_S_OK;
}
APP_Result CContactorsService::ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
{
	//Get operation type
	
	//Get Info

	//excute
	APP_Result hr = APP_Result_E_Fail;
	CXmlStream clResultXml;
	clResultXml.Initialize();
	CXmlNode* pNode = NULL;
	appR = clXmlStream.SelectNode(L"result/data/", &pNode);
	if ( FAILED_App(appR) ){
		return appR;
	}
	auto_ptr<CXmlNode> sp(pNode);
	NodeAttribute_t stTemp;
	F_wcscpyn(stTemp.wcsName, L"type", sizeof(stTemp.wcsName)/sizeof(stTemp.wcsName[0]));
	F_wcscpyn(stTemp.wcsValue, L"contactors", sizeof(stTemp.wcsValue)/sizeof(stTemp.wcsValue[0]));
	hr = pNode->SetNodeContent( NULL, NULL, &stTemp, 1 );
	if ( FAILED_App(hr) ){
		return hr;
	}
	
	if ( m_stRequestData.enOperationType == Contactors_OperationType_List ){
		CXmlNode* pNode = NULL;
		appR = clXmlStream.SelectNode(L"result/data/data/", &pNode);
		if ( FAILED_App(appR) ){
			return appR;
		}
		auto_ptr<CXmlNode> sp(pNode);
		NodeAttribute_t stTemp[3];
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
		
		while ( hr != APP_Result_E_Fail && hr != APP_Result_S_OK )
		{
			
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
	}
	wchar_t* pResult = NULL;
	long lSize = 0;
	hr = clXmlStream.GetXmlStream(&pResult, &lSize);
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