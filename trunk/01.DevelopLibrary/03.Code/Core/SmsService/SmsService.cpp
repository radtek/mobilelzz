#include "stdafx.h"
#include "BasicService.h"
#include "SmsService.h"

CSmsService::CSmsService()
{
	m_pclSqlDBSession = NULL;
	m_pQUnReadSms = NULL;
	m_pQSmsList = NULL;
	m_pQSmsGroupInfo = NULL;
	m_pQUpdateReadStatus = NULL;
	m_pQUpdateLockStatus = NULL;
}

CSmsService::~CSmsService()
{
	m_pQUnReadSms->Finalize();
	m_pQSmsList->Finalize();
	m_pQSmsListByContactor->Finalize();
	m_pQSmsGroupInfo->Finalize();
	m_pQUpdateReadStatus->Finalize();
	m_pQUpdateLockStatus->Finalize();

	m_pclSqlDBSession->Query_Delete(m_lID_QUnReadSms);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsList);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsListByContactor);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsGroupInfo);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsReadStatus);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsReadStatus);

	BOOL bIsDBClosed = FALSE;
	m_pclSqlSessionManager->Session_DisConnect( L"sms", &bIsDBClosed );
}

APP_Result CSmsService::MakeParam(wchar_t* pwcsRequestXML)
{


	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
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
	F_wcscpyn(stTemp.wcsValue, L"sms", sizeof(stTemp.wcsValue)/sizeof(stTemp.wcsValue[0]));
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

APP_Result CSmsService::MakeResult(wchar_t** ppwcsRequestXML)
{


	return APP_Result_S_OK;
}

APP_Result CSmsService::Initialize()
{
	APP_Result hr = APP_Result_E_Fail;
	hr = CBasicService::Initialize();
	if(FAILED(hr))	
		return hr;
	APP_Result hr = m_pclSqlSessionManager->Session_Connect(L"sms", L".\\Documents and Settings\\", L"sms.db", &m_pclSqlDBSession );
	if(FAILED(hr) || m_pclSqlDBSession == NULL)	
		return APP_Result_E_Fail;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QUnReadSms, &m_pQUnReadSms );
	if( FAILED(hr) || m_pQUnReadSms == NULL ) 
		return hr;
	hr = m_pQUnReadSms->Prepare(Sms_SQL_GET_UnReadSms);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QSmsList, &m_pQSmsList );
	if( FAILED(hr) || m_pQSmsList== NULL ) 
		return hr;
	hr = m_pQSmsList->Prepare(Sms_SQL_GET_SmsList);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QSmsListByContactor, &m_pQSmsListByContactor );
	if( FAILED(hr) || m_pQSmsListByContactor== NULL ) 
		return hr;
	hr = m_pQSmsListByContactor->Prepare(Sms_SQL_GET_SmsList);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QSmsGroupInfo, &m_pQSmsGroupInfo );
	if( FAILED(hr) || m_pQSmsGroupInfo== NULL ) 
		return hr;
	hr = m_pQSmsGroupInfo->Prepare(Sms_SQL_GET_SmsGroupInfo);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QSmsReadStatus, &m_pQUpdateReadStatus );
	if( FAILED(hr) || m_pQUpdateReadStatus== NULL ) 
		return hr;
	hr = m_pQUpdateReadStatus->Prepare(Sms_SQL_SET_ReadStatus);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QSmsReadStatus, &m_pQUpdateLockStatus );
	if( FAILED(hr) || m_pQUpdateLockStatus== NULL ) 
		return hr;
	hr = m_pQUpdateLockStatus->Prepare(Sms_SQL_SET_LockStatus);
	if( FAILED(hr) ) 
		return hr;

	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteForList(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{	
	APP_Result hr = APP_Result_E_Fail;
	ListCondition* pConditions = NULL;
	long lConditionCount = 0;
	hr = clXmlOpe.GetListConditions(&pConditions, &lConditionCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<ListCondition> sp(pConditions, lConditionCount);
	CSQL_query* pQNeeded = NULL;
	BOOL bIsPermitDecode = FALSE;
	hr = DecideQuery(pConditions, lConditionCount, &pQNeeded, bIsPermitDecode);
	if ( FAILED_App(hr) ){
		return hr;
	}	
	if ( pQNeeded ){
		hr = MakeSmsList(clXmlOpe, pQNeeded, clResultXml, bIsPermitDecode);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else{

	}	

	return APP_Result_S_OK;
}

APP_Result CSmsService::DecideQuery(ListCondition* pConditions, long lConditionCount, 
									CSQL_query** ppQueryNeeded, BOOL& bIsPermitDecode)
{
	// find condition
		//unread
			//supply query, do not permit decode
		//contactor
			//decode
				//succeed
					//supply query, permit decode
				//failed
					//do not supply query, do not permit decode

	APP_Result hr = APP_Result_E_Fail;
	long lPID = Invalid_ID;
	wchar_t wcsCode[20] = L"";
	for ( int i = 0; i < lConditionCount; i++ )
	{
		if ( 0 == wcscmp(pConditions[i].wcsConditionName, L"unread") ){
			*ppQueryNeeded = m_pQUnReadSms;
			bIsPermitDecode = FALSE;
		}else if ( 0 == wcscmp(pConditions[i].wcsConditionName, L"contactor") ){
			lPID = _wtol(pConditions[i].wcsConditionValue);
		}else if( 0 == wcscmp(pConditions[i].wcsConditionName, L"decode" ){
			F_wcscpyn( wcsCode, pConditions[i].wcsConditionValue, sizeof(wcsCode)/sizeof(wcsCode[0]) );
		}
	}
	if ( lPID != Invalid_ID ){
		BOOL bNeedDecode = FALSE;
		wchar_t wcsDBCode[20] = L"";
		// check is need decode
		
		if ( bNeedDecode ){
			//process code

			if ( 0 == wcscmp( wcsDBCode, wcsCode ) ){
				m_pQSmsListByContactor->Bind(1,lPID);
				*ppQueryNeeded = m_pQSmsListByContactor;
				bIsPermitDecode = TRUE;
			}else{
				*ppQueryNeeded = NULL;
				bIsPermitDecode = FALSE;
			}				
		}else{
			m_pQSmsListByContactor->Bind(1,lPID);
			*ppQueryNeeded = m_pQSmsListByContactor;
			bIsPermitDecode = TRUE;
		}		
	}
	
	return APP_Result_S_OK;
}

APP_Result CSmsService::MakeSmsList(CRequestXmlOperator& clXmlOpe, CSQL_query* pQHandle, 
									CXmlStream& clResultXml, BOOL bIsPermitDecode)
{
	//check is sms encode(need cache)
		//is 
			//check is permit decode
				//is
					//decode
				//not
					//deplay ***
		//not
			//supply sms
	APP_Result hr = APP_Result_E_Fail;
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