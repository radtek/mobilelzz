#include "stdafx.h"
#include "BasicService.h"
#include "XmlStream.h"
#include "RequestXmlOperator.h"
#include "SmsService.h"

CSmsService::CSmsService()
{
	m_pclSqlDBSession = NULL;
	m_pQUnReadSms = NULL;
	m_pQSmsList = NULL;
	m_pQSmsGroupInfo = NULL;
	m_pQUpdateReadStatus = NULL;
	m_pQUpdateLockStatus = NULL;
	m_pQCheckCode = NULL;
	m_pQInsertCode = NULL;
	m_pQUpdateSmsContent = NULL;

}

CSmsService::~CSmsService()
{
	m_pQUnReadSms->Finalize();
	m_pQSmsList->Finalize();
	m_pQSmsListByContactor->Finalize();
	m_pQSmsGroupInfo->Finalize();
	m_pQUpdateReadStatus->Finalize();
	m_pQUpdateLockStatus->Finalize();
	m_pQCheckCode->Finalize();
	m_pQInsertCode->Finalize();
	m_pQUpdateSmsContent->Finalize();

	m_pclSqlDBSession->Query_Delete(m_lID_QUnReadSms);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsList);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsListByContactor);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsGroupInfo);
	m_pclSqlDBSession->Query_Delete(m_lID_QSmsReadStatus);
	m_pclSqlDBSession->Query_Delete(m_lID_QUpdateLockStatus);
	m_pclSqlDBSession->Query_Delete(m_lID_QCheckCode);
	m_pclSqlDBSession->Query_Delete(m_lID_QInsertCode);
	m_pclSqlDBSession->Query_Delete(m_lID_QUpdateSmsContent);

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
	hr = pNode->SetNodeContent( NULL, (wchar_t*)NULL, &stTemp, 1 );
	if ( FAILED_App(hr) ){
		return hr;
	}
	if ( 0 == wcscmp(L"list", awcsOpeType) ){
		hr = ExcuteForList(clReqXmlOpe, clResultXml);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else if ( 0 == wcscmp(L"encode", awcsOpeType) ){
		hr = ExcuteForEncode(clReqXmlOpe, clResultXml);
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
	hr = m_pclSqlSessionManager->Session_Connect(L"sms", L".\\Documents and Settings\\", L"sms.db", &m_pclSqlDBSession );
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

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QCheckCode, &m_pQCheckCode );
	if( FAILED(hr) || m_pQCheckCode== NULL ) 
		return hr;
	hr = m_pQCheckCode->Prepare(Sms_SQL_GET_SmsCode);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QInsertCode, &m_pQInsertCode );
	if( FAILED(hr) || m_pQInsertCode== NULL ) 
		return hr;
	hr = m_pQInsertCode->Prepare(Sms_SQL_INSERT_SmsCode);
	if( FAILED(hr) ) 
		return hr;

	hr = m_pclSqlDBSession->Query_Create((int*)&m_lID_QUpdateSmsContent, &m_pQUpdateSmsContent );
	if( FAILED(hr) || m_pQUpdateSmsContent== NULL ) 
		return hr;
	hr = m_pQUpdateSmsContent->Prepare(Sms_SQL_SET_SmsContent);
	if( FAILED(hr) ) 
		return hr;

	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteForList(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{	
	APP_Result hr = APP_Result_E_Fail;
	OperationCondition* pConditions = NULL;
	long lConditionCount = 0;
	hr = clXmlOpe.GetOperationConditions(&pConditions, &lConditionCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<OperationCondition> sp(pConditions, lConditionCount);
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

APP_Result CSmsService::DecideQuery(OperationCondition* pConditions, long lConditionCount, 
									CSQL_query** ppQueryNeeded, BOOL& bIsPermitDecode)
{
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
		}else if( 0 == wcscmp(pConditions[i].wcsConditionName, L"decode") ){
			F_wcscpyn( wcsCode, pConditions[i].wcsConditionValue, sizeof(wcsCode)/sizeof(wcsCode[0]) );
		}
	}
	if ( lPID != Invalid_ID ){
		BOOL bNeedDecode = FALSE;
		wchar_t wcsDBCode[20] = L"";
		// check is need decode
		hr = CheckCode(lPID, bNeedDecode, wcsDBCode, sizeof(wcsDBCode)/sizeof(wcsDBCode[0]));
		if ( FAILED_App(hr) ){
			return APP_Result_E_Fail;
		}
		if ( bNeedDecode ){
			//process code
			if ( L'\0' != wcsCode[0] ){
				wchar_t wcsInputCodeCompared[20] = L"";
				hr = ConvertDisplayCode2DBCode(wcsCode, wcsInputCodeCompared, 
					sizeof(wcsInputCodeCompared)/sizeof(wcsInputCodeCompared[0]));
				if ( FAILED_App(hr) ){
					return APP_Result_E_Fail;
				}
				if ( 0 == wcscmp( wcsDBCode, wcsInputCodeCompared ) ){
					m_pQSmsListByContactor->Bind(1,lPID);
					*ppQueryNeeded = m_pQSmsListByContactor;
					bIsPermitDecode = TRUE;
				}else{
					*ppQueryNeeded = NULL;
					bIsPermitDecode = FALSE;
				}		
			}
			else{
				return APP_Result_ProtectData_NeedCode;
			}
		}else{
			m_pQSmsListByContactor->Bind(1,lPID);
			*ppQueryNeeded = m_pQSmsListByContactor;
			bIsPermitDecode = TRUE;
		}		
	}
	
	return APP_Result_S_OK;
}

APP_Result CSmsService::CheckCode(long lPID, BOOL& bNeedDecode, 
								  wchar_t* pwcsDBCode, long lCodeSize)
{
	APP_Result hr = APP_Result_E_Fail;
	
	m_pQCheckCode->Reset();
	m_pQCheckCode->Bind(1, lPID);
	hr = m_pQCheckCode->Step();
	if ( FAILED_App(hr) ){
		return APP_Result_E_Fail;
	}
	wchar_t* pCode = NULL;
	m_pQCheckCode->GetField(1, &pCode);
	if ( NULL != pCode && L'\0' == pCode[0] ){
		F_wcscpyn(pwcsDBCode, pCode, lCodeSize);
		bNeedDecode = TRUE;
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::ConvertDisplayCode2DBCode(wchar_t* pwcsCode, wchar_t* pwcsDBCode, 
						long lDBCodeCount )
{
	unsigned short usMask = 0x0007;
	long lCount = wcslen(pwcsCode);
	if ( lCount > lDBCodeCount ){
		return APP_Result_E_Fail;
	}
	for ( int i = 0; i < lCount; i++ )
	{
		unsigned short usTemp = (pwcsCode[i] & usMask)<<13;
		pwcsDBCode[i] = (pwcsCode[i]>>3)|usTemp;
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::ConvertDBCode2DisplayCode(wchar_t* pDBCode, wchar_t* pwcsCode, 
												  long lCodeCount)
{
	unsigned short usMask = 0xE000;
	long lCount = wcslen(pwcsCode);
	if ( lCount > lCodeCount ){
		return APP_Result_E_Fail;
	}
	for ( int i = 0; i < lCount; i++ )
	{
		unsigned short usTemp = (pDBCode[i] & usMask)>>13;
		pwcsCode[i] = (pDBCode[i]<<3)|usTemp;
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::MakeSmsList(CRequestXmlOperator& clXmlOpe, CSQL_query* pQHandle, 
									CXmlStream& clResultXml, BOOL bIsPermitDecode)
{
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
	F_wcscpyn(stTemp[2].wcsName, L"encode", sizeof(stTemp[2].wcsName)/sizeof(stTemp[2].wcsName[0]));
	F_wcscpyn(stTemp[2].wcsValue, L"none", sizeof(stTemp[2].wcsValue)/sizeof(stTemp[2].wcsValue[0]));
	hr = pNode->SetNodeContent( NULL, (wchar_t*)NULL, stTemp, 3 );
	if ( FAILED_App(hr) ){
		return hr;
	}
	long lListCount = 0;
	long lEncodeCount = 0;
	hr = MakeSmsListRecs( pQHandle, pNode, bIsPermitDecode, lListCount, lEncodeCount );
	if ( FAILED_App(hr) ){
		return hr;
	}
	
	F_wcscpyn(stTemp[0].wcsName, L"count", sizeof(stTemp[0].wcsName)/sizeof(stTemp[0].wcsName[0]));
	wchar_t awcsCount[5] = L"";
	wsprintf(awcsCount, L"%d", lListCount);
	F_wcscpyn(stTemp[0].wcsValue, awcsCount, sizeof(stTemp[0].wcsValue)/sizeof(stTemp[0].wcsValue[0]));
	hr = pNode->SetNodeContent( NULL, (wchar_t*)NULL, stTemp, 1 );
	if ( FAILED_App(hr) ){
		return hr;
	}

	wchar_t wcsEncodeType[20] = L"";
	if ( 0 == lEncodeCount ){
		F_wcscpyn(wcsEncodeType, L"none", sizeof(wcsEncodeType)/sizeof(wcsEncodeType));
	}else if ( lListCount == lEncodeCount ){
		F_wcscpyn(wcsEncodeType, L"all", sizeof(wcsEncodeType)/sizeof(wcsEncodeType));
	}else if ( lListCount > lEncodeCount ){
		F_wcscpyn(wcsEncodeType, L"part", sizeof(wcsEncodeType)/sizeof(wcsEncodeType));
	}

	NodeAttribute_t stAttribute;
	F_wcscpyn(stAttribute.wcsName, L"encode", sizeof(stAttribute.wcsName)/sizeof(stAttribute.wcsName[0]));
	F_wcscpyn(stAttribute.wcsValue, wcsEncodeType, sizeof(stAttribute.wcsValue)/sizeof(stAttribute.wcsValue[0]));
	pNode->SetNodeContent(NULL, (wchar_t*)NULL, &stAttribute, 1);

	return APP_Result_S_OK;
}

APP_Result CSmsService::MakeSmsListRecs( CSQL_query* pQHandle, CXmlNode* pNodeList, BOOL bIsPermitDecode,
										long& lListCount, long& lEncodeCount )
{
	APP_Result hr = APP_Result_E_Fail;
	pQHandle->Reset();
	hr = pQHandle->Step();

	while ( hr != APP_Result_E_Fail && hr != APP_Result_S_OK )
	{
		lListCount++;
		long lSID = 0;
		pQHandle->GetField(0, &lSID);
		CXmlNode clNodeSID(L"sid");
		clNodeSID.SetNodeContent(NULL, lSID, NULL, 0);

		long lPID = 0;
		pQHandle->GetField(1, &lPID);
		CXmlNode clNodePID(L"pid");
		clNodePID.SetNodeContent(NULL, lPID, NULL, 0);

		long lType = 0;
		pQHandle->GetField(2, &lType);
		CXmlNode clNodeType(L"type");
		clNodeType.SetNodeContent(NULL, lType, NULL, 0);

		BOOL bIsEnCode = FALSE;
		CXmlNode clNodeContent(L"content");
		NodeAttribute_t stAttribute;
		F_wcscpyn(stAttribute.wcsName, L"encode", sizeof(stAttribute.wcsName)/sizeof(stAttribute.wcsName[0]));
		F_wcscpyn(stAttribute.wcsValue, L"false", sizeof(stAttribute.wcsValue)/sizeof(stAttribute.wcsValue[0]));
		clNodeContent.SetNodeContent(NULL, (wchar_t*)NULL, &stAttribute, 1);
		CheckCode(lPID, bIsEnCode, NULL, 0);
		if ( bIsEnCode ){
			if ( bIsPermitDecode ){
				wchar_t* pDBContent = NULL;
				pQHandle->GetField(3, &pDBContent);	
				wchar_t wcsContent[256] = L"";
				ConvertDBCode2DisplayCode(pDBContent, wcsContent, sizeof(wcsContent)/sizeof(wcsContent[0]));
				clNodeContent.SetNodeContent(NULL, wcsContent, NULL, 0);
			}else{
				lEncodeCount++;
				NodeAttribute_t stAttribute;
				F_wcscpyn(stAttribute.wcsName, L"encode", sizeof(stAttribute.wcsName)/sizeof(stAttribute.wcsName[0]));
				F_wcscpyn(stAttribute.wcsValue, L"true", sizeof(stAttribute.wcsValue)/sizeof(stAttribute.wcsValue[0]));
				clNodeContent.SetNodeContent(NULL, (wchar_t*)NULL, &stAttribute, 1);
				clNodeContent.SetNodeContent(NULL, L"*** ***", NULL, 0);
			}
		}else{
			wchar_t* pContent = NULL;
			pQHandle->GetField(3, &pContent);			
			clNodeContent.SetNodeContent(NULL, pContent, NULL, 0);
		}

		wchar_t* pNumber = NULL;
		pQHandle->GetField(4, &pNumber);		
		CXmlNode clNodeNumber(L"address");
		clNodeNumber.SetNodeContent(NULL, pNumber, NULL, 0);

		double dTime = 0;
		pQHandle->GetField(5, &dTime);
		CXmlNode clNodeTime(L"time");
		clNodeTime.SetNodeContent(NULL, dTime, NULL, 0);

		long lLockStatus = 0;
		pQHandle->GetField(6, &lLockStatus);
		CXmlNode clNodeLockStatus(L"lockstatus");
		clNodeLockStatus.SetNodeContent(NULL, lLockStatus, NULL, 0);

		long lReadStatus = 0;
		pQHandle->GetField(7, &lReadStatus);
		CXmlNode clNodeReadStatus(L"readstatus");
		clNodeReadStatus.SetNodeContent(NULL, lReadStatus, NULL, 0);

		CXmlNode clNodeRec(L"rec");
		clNodeRec.AppendNode(&clNodeSID);
		clNodeRec.AppendNode(&clNodePID);
		clNodeRec.AppendNode(&clNodeType);
		clNodeRec.AppendNode(&clNodeContent);
		clNodeRec.AppendNode(&clNodeNumber);
		clNodeRec.AppendNode(&clNodeTime);
		clNodeRec.AppendNode(&clNodeLockStatus);
		clNodeRec.AppendNode(&clNodeReadStatus);

		pNodeList->AppendNode(&clNodeRec);

		hr = pQHandle->Step();
	}
	
	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteForEncode(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml)
{
	APP_Result hr = APP_Result_E_Fail;
	// get contactor's id,and code
	OperationCondition* pOperationCondition = NULL;
	long lConditionBufCount = 0;
	hr = clReqXmlOpe.GetOperationConditions(&pOperationCondition, &lConditionBufCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	//save code
	long lPID = Invalid_ID;
	if ( 0 == wcscmp(L"contactor", pOperationCondition[0].wcsConditionName) ){
		lPID = _wtol(pOperationCondition[0].wcsConditionValue);
	}
	if ( Invalid_ID != lPID ){
		if ( 0 == wcscmp(L"code", pOperationCondition[1].wcsConditionName) ){
			m_pQInsertCode->Reset();
			m_pQInsertCode->Bind( 1, lPID );
			m_pQInsertCode->Bind( 2, pOperationCondition[1].wcsConditionValue, 
								sizeof(pOperationCondition[1].wcsConditionValue)/sizeof(pOperationCondition[1].wcsConditionValue[0]) );
			hr = m_pQInsertCode->Step();
			if ( FAILED_App(hr) ){
				return hr;
			}
		}		
		else{
			return APP_Result_E_Fail;
		}
	}else{
		return APP_Result_E_Fail;
	}
	
	//loop to encode sms content
	hr = EncodeSmsContentByContactor(lPID);
	if ( FAILED_App(hr) ){
		return hr;
	}
	
	return APP_Result_S_OK;
}

APP_Result CSmsService::EncodeSmsContentByContactor(long lPID)
{
	APP_Result hr = APP_Result_E_Fail;
	m_pQSmsListByContactor->Reset();
	m_pQSmsListByContactor->Bind(1, lPID);
	hr = m_pQSmsListByContactor->Step();
	while ( hr != APP_Result_E_Fail && hr != APP_Result_S_OK )
	{	
		long lSID = 0;
		m_pQSmsListByContactor->GetField(0, &lSID);
		wchar_t* pContent = NULL;
		m_pQSmsListByContactor->GetField(3, &pContent);	
		wchar_t wcsDBContent[256] = L"";
		hr = ConvertDisplayCode2DBCode(pContent, wcsDBContent, sizeof(wcsDBContent)/sizeof(wcsDBContent[0]));
		if ( FAILED_App(hr) ){
			return APP_Result_E_Fail;
		}
		m_pQUpdateSmsContent->Reset();
		m_pQUpdateSmsContent->Bind(2, lSID);
		m_pQUpdateSmsContent->Bind(1, wcsDBContent, sizeof(wcsDBContent)/sizeof(wcsDBContent[0]));
		hr = m_pQUpdateSmsContent->Step();
		if ( FAILED_App(hr) ){
			return APP_Result_E_Fail;
		}
		hr = m_pQSmsListByContactor->Step();
	}

	return APP_Result_S_OK;
}