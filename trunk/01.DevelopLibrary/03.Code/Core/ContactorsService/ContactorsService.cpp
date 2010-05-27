#include"stdafx.h"
#include"BasicService.h"
#include "XmlStream.h"
#include "RequestXmlOperator.h"
#include"ContactorsService.h"
#include <algorithm>

class CCompareContactorNode
{
public:
	BOOL operator()( CXmlNode* p1, CXmlNode* p2 )
	{
		BOOL b = FALSE;
		wchar_t* pName1 = NULL;
		p1->GetNodeContent(L"reading/", &pName1, NULL, NULL);
		wchar_t* pName2 = NULL;
		p2->GetNodeContent(L"reading/", &pName2, NULL, NULL);
		if ( pName2 && pName1 ){
			if ( (L'\0'!=pName1[0]) && (L'\0'!=pName2[0]) ){
				if ( 0 > wcscmp(pName1, pName2) ){
					b = TRUE;
				}
			}else if ( L'\0'!=pName1[0] ){
				b = TRUE;
			}
			
			delete pName2;
			delete pName1;
		}
		return b;
	}
};

CContactorsService::CContactorsService()
{
	m_pQReading = NULL;
	m_pQContactorsList = NULL;
	m_pclSqlDBSession = NULL;
}

CContactorsService::~CContactorsService()
{
	m_pQReading->Finalize();
	m_pQContactorsList->Finalize();

	m_pQSmsGroupInfo->Finalize();

	m_pclSqlDBSession->Query_Delete(m_lID_QReading);
	m_pclSqlDBSession->Query_Delete(m_lID_QContactorsList);
	m_pclSqlDBSession->Query_Delete(m_pQSmsGroupInfo->GetQh());

	BOOL bIsDBClosed = FALSE;
	m_pclSqlSessionManager->Session_DisConnect( L"contacts", &bIsDBClosed );
	m_pclSqlSessionManager->Session_DisConnect( L"contacts_sms", &bIsDBClosed );
	
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
	hr = pNode->SetNodeContent( NULL, (wchar_t*)NULL, &stTemp, 1 );
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

	hr = m_pclSqlSessionManager->Session_Connect(L"contacts", L".\\Documents and Settings\\", L"contacts.db", &m_pclSqlDBSession );
	if(FAILED(hr) || m_pclSqlDBSession == NULL)	
		return APP_Result_E_Fail;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QReading, &m_pQReading );
	if( FAILED(hr) || m_pQReading == NULL ) 
		return hr;
	hr = m_pQReading->Prepare(Contactors_SQL_GET_FIRSTLETER);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QContactorsList, &m_pQContactorsList );
	if( FAILED(hr) || m_pQContactorsList== NULL ) 
		return hr;
	hr = m_pQContactorsList->Prepare(Contactors_SQL_GET_CONTACTS);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlSessionManager->Session_Connect(L"sms", L".\\Documents and Settings", L"easysms.db", &m_pclSqlSmsDBSession );
	if(FAILED(hr) || m_pclSqlSmsDBSession == NULL)	
		return APP_Result_E_Fail;

	hr = CreateQuery(m_pclSqlSmsDBSession, Sms_SQL_GET_SmsGroupInfo, m_pQSmsGroupInfo);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlSmsDBSession, Sms_SQL_GET_SmsCode, m_pQSmsCode);
	if ( FAILED_App(hr) ){
		return hr;
	}

	return APP_Result_S_OK;
}

void CContactorsService::MakeReading(wchar_t* pwcsReading, long lPID )
{
	m_pQReading->Reset();
	m_pQReading->Bind(1, lPID);
	m_pQReading->Step();
	m_pQReading->Step();

	wchar_t* pTemp = NULL;
	m_pQReading->GetField(0,&pTemp);
	F_wcscpyn(pwcsReading, pTemp, 19);

}

void CContactorsService::MakeSmsCount(wchar_t* pwcsSmsCount, long lPID )
{
	m_pQSmsGroupInfo->Reset();
	m_pQSmsGroupInfo->Bind(1, lPID);
	m_pQSmsGroupInfo->Step();

	long lCount = 0;
	m_pQSmsGroupInfo->GetField(1,&lCount);
	_ltow(lCount, pwcsSmsCount, 10);

}

APP_Result CContactorsService::ExcuteForList(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{
	APP_Result hr = APP_Result_E_Fail;
	OperationCondition* pConditions = NULL;
	long lConditionCount = 0;
	hr = clXmlOpe.GetOperationConditions(&pConditions, &lConditionCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<OperationCondition> sp(pConditions, lConditionCount);

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
	hr = pNode->SetNodeContent( NULL, (wchar_t*)NULL, stTemp, 3 );
	if ( FAILED_App(hr) ){
		return hr;
	}
	m_pQContactorsList->Reset();
	hr = m_pQContactorsList->Step();
	long lListCount = 0;
	vector<CXmlNode*> vecTempNodeListForSort;
	while ( hr != APP_Result_E_Fail && hr != APP_Result_S_OK )
	{
		lListCount++;
		long lPID = 0;
		m_pQContactorsList->GetField(0, &lPID);
		wchar_t awcsID[5] = L"";
		wsprintf(awcsID, L"%d", lPID);
		CXmlNode clNodeID(L"pid");
		clNodeID.SetNodeContent(NULL, awcsID, NULL, 0);

		wchar_t* PName = NULL;
		m_pQContactorsList->GetField(1, &PName);
		CXmlNode clNodeName(L"name");
		clNodeName.SetNodeContent(NULL, PName, NULL, 0);

		wchar_t awReading[20] = L"";
		wchar_t awcsFirstLetter[2] = L"";
		MakeReading(awReading, lPID);
		awcsFirstLetter[0] = awReading[0];
		//wprintf( L"No.%d Contactor Reading:%s\n", lListCount, awReading );
		CXmlNode clNodeReading(L"reading");
		clNodeReading.SetNodeContent(NULL, awReading, NULL, 0);

		CXmlNode clNodeFirstLetter(L"firstletter");
		clNodeFirstLetter.SetNodeContent(NULL, awcsFirstLetter, NULL, 0);

		wchar_t* pNumber = NULL;
		m_pQContactorsList->GetField(4, &pNumber);		
		CXmlNode clNodeNumber(L"defaultno");
		clNodeNumber.SetNodeContent(NULL, pNumber, NULL, 0);			
		
		wchar_t awcsSmsCount[20] = L"";
		MakeSmsCount(awcsSmsCount, lPID);
		CXmlNode clNodeSmsCount(L"smscount");
		clNodeSmsCount.SetNodeContent(NULL, awcsSmsCount, NULL, 0);

		CXmlNode* pclNodeRec = new CXmlNode(L"rec");
		if ( pclNodeRec ){
			// Get is Encode
			long lEncodeStatus = 0;
			MakeEncodeStatus(lPID, lEncodeStatus);
			NodeAttribute_t stAttr;
			F_wcscpyn( stAttr.wcsName, L"encode", sizeof(stAttr.wcsName)/sizeof(stAttr.wcsName[0]) );
			if ( 0 == lEncodeStatus ){
				wsprintf(stAttr.wcsValue, L"false");
			}else{
				wsprintf(stAttr.wcsValue, L"true");
			}
			pclNodeRec->SetNodeContent( NULL, (wchar_t*)NULL, &stAttr, 1 );

			pclNodeRec->AppendNode(&clNodeID);
			pclNodeRec->AppendNode(&clNodeName);
			pclNodeRec->AppendNode(&clNodeFirstLetter);
			pclNodeRec->AppendNode(&clNodeReading);
			pclNodeRec->AppendNode(&clNodeNumber);
			pclNodeRec->AppendNode(&clNodeSmsCount);
		}else{
			return APP_Result_Memory_Full;
		}		
		vecTempNodeListForSort.push_back(pclNodeRec);
		//pNode->AppendNode(&pclNodeRec);

		hr = m_pQContactorsList->Step();
	}
	
	AppendStranger(vecTempNodeListForSort);
	CCompareContactorNode clCompare;
	::sort(vecTempNodeListForSort.begin(),vecTempNodeListForSort.end(),clCompare);
	long lCount = vecTempNodeListForSort.size();
	for ( int i = 0; i < lCount; i++ )
	{
		pNode->AppendNode(vecTempNodeListForSort.at(i));
		//wchar_t* pName1 = NULL;
		//vecTempNodeListForSort.at(i)->GetNodeContent(L"name/", &pName1, NULL, NULL);
		//wprintf( L"No.%d Contactor Name:%s\n", i, pName1 );
		//delete pName1;
		delete vecTempNodeListForSort.at(i);
	}
	F_wcscpyn(stTemp[0].wcsName, L"count", sizeof(stTemp[0].wcsName)/sizeof(stTemp[0].wcsName[0]));
	wchar_t awcsCount[5] = L"";
	wsprintf(awcsCount, L"%d", lListCount);
	F_wcscpyn(stTemp[0].wcsValue, awcsCount, sizeof(stTemp[0].wcsValue)/sizeof(stTemp[0].wcsValue[0]));
	hr = pNode->SetNodeContent( NULL, (wchar_t*)NULL, stTemp, 1 );
	if ( FAILED_App(hr) ){
		return hr;
	}

	return APP_Result_S_OK;
}

void CContactorsService::AppendStranger(vector<CXmlNode*>& vecList)
{
	CXmlNode* pclNodeRec = new CXmlNode(L"rec");
	if ( pclNodeRec ){
		CXmlNode clNodeID(L"pid");
		clNodeID.SetNodeContent(NULL, L"0", NULL, 0);

		CXmlNode clNodeName(L"name");
		clNodeName.SetNodeContent(NULL, L"Ä°ÉúÈË", NULL, 0);

		CXmlNode clNodeReading(L"reading");
		clNodeReading.SetNodeContent(NULL, L"MSR", NULL, 0);

		CXmlNode clNodeFirstLetter(L"firstletter");
		clNodeFirstLetter.SetNodeContent(NULL, L"M", NULL, 0);

		CXmlNode clNodeNumber(L"defaultno");
		clNodeNumber.SetNodeContent(NULL, L"", NULL, 0);

		wchar_t awcsSmsCount[20] = L"";
		MakeSmsCount(awcsSmsCount, 0);
		CXmlNode clNodeSmsCount(L"smscount");
		clNodeSmsCount.SetNodeContent(NULL, awcsSmsCount, NULL, 0);

		long lEncodeStatus = 0;
		MakeEncodeStatus(0, lEncodeStatus);
		NodeAttribute_t stAttr;
		F_wcscpyn( stAttr.wcsName, L"encode", sizeof(stAttr.wcsName)/sizeof(stAttr.wcsName[0]) );
		if ( 0 == lEncodeStatus ){
			wsprintf(stAttr.wcsValue, L"false");
		}else{
			wsprintf(stAttr.wcsValue, L"true");
		}
		pclNodeRec->SetNodeContent( NULL, (wchar_t*)NULL, &stAttr, 1 );	

		pclNodeRec->AppendNode(&clNodeID);
		pclNodeRec->AppendNode(&clNodeName);
		pclNodeRec->AppendNode(&clNodeFirstLetter);
		pclNodeRec->AppendNode(&clNodeReading);
		pclNodeRec->AppendNode(&clNodeNumber);
		pclNodeRec->AppendNode(&clNodeSmsCount);

		vecList.push_back(pclNodeRec);
	}
}

APP_Result CContactorsService::MakeEncodeStatus(long lPID, long& lEncodeStatus)
{
	APP_Result hr = APP_Result_E_Fail;
	m_pQSmsCode->Reset();
	m_pQSmsCode->Bind(1, lPID);
	hr = m_pQSmsCode->Step();
	if ( S_ROW == hr ){
		m_pQSmsCode->GetField(1,&lEncodeStatus);
	}
	
	return APP_Result_S_OK;
}