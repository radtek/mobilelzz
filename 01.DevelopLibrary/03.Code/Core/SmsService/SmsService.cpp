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
	m_pQUnReadSms.Release();
	m_pQSmsList.Release();
	m_pQSmsListByContactor.Release();
	m_pQSmsGroupInfo.Release();
	m_pQUpdateReadStatus.Release();
	m_pQUpdateLockStatus.Release();
	m_pQCheckCode.Release();
	m_pQInsertCode.Release();
	m_pQUpdateSmsContent.Release();
	m_pQGetNameByPID.Release();

	BOOL bIsDBClosed = FALSE;
	m_pclSqlSessionManager->Session_DisConnect( L"sms", &bIsDBClosed );
	m_pclSqlSessionManager->Session_DisConnect( L"sms_contacts", &bIsDBClosed );
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
	}else if ( 0 == wcscmp(L"edit", awcsOpeType) ){
		hr = ExcuteForEdit(clReqXmlOpe, clResultXml);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else if ( 0 == wcscmp(L"add", awcsOpeType) ){
		hr = ExcuteForAdd(clReqXmlOpe, clResultXml);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else if ( 0 == wcscmp(L"delete", awcsOpeType) ){
		hr = ExcuteForDelete(clReqXmlOpe, clResultXml);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else if ( 0 == wcscmp(L"search", awcsOpeType) ){
		hr = ExcuteForSearch(clReqXmlOpe, clResultXml);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else{

	}

	wchar_t* pResult = NULL;
	long lSize = 0;
	hr = clResultXml.GetXmlStream(&pResult, &lSize);
	if ( SUCCEEDED_App(hr) ){
		if ( *ppwcsResultXML ){
			*ppwcsResultXML = pResult;
		}		
	}
	return APP_Result_S_OK;
}

APP_Result CSmsService::MakeResult(wchar_t** ppwcsRequestXML)
{


	return APP_Result_S_OK;
}

APP_Result CSmsService::CreateTable(wchar_t* pSqlCommand)
{
	//SELECT name FROM sqlite_master WHERE name='" + tableName + "'";
	APP_Result hr = APP_Result_E_Fail;
	CSQL_SmartQuery pQCreate(m_pclSqlDBSession);
	hr = m_pclSqlDBSession->Query_Create(NULL, &pQCreate );
	if( FAILED(hr) || pQCreate.Get() == NULL ) 
		return hr;
	hr = pQCreate->Prepare(pSqlCommand);
	if( FAILED(hr) ) 
		return hr;
	hr = pQCreate->Step();
	if( FAILED(hr) ) 
		return hr;

	return APP_Result_S_OK;
}

APP_Result CSmsService::Initialize()
{
	APP_Result hr = APP_Result_E_Fail;
	hr = CBasicService::Initialize();
	if(FAILED(hr))	
		return hr;
	hr = m_pclSqlSessionManager->Session_Connect(L"sms", L".\\Documents and Settings", L"easysms.db", &m_pclSqlDBSession );
	if(FAILED(hr) || m_pclSqlDBSession == NULL)	
		return APP_Result_E_Fail;
	hr = CreateTable(Sms_SQL_CREATETABLE_SmsDetail);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateTable(Sms_SQL_CREATETABLE_SmsCode);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateTable(Sms_SQL_CREATETABLE_SmsGroup);
	if ( FAILED_App(hr) ){
		return hr;
	}

	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_GET_UnReadSms, m_pQUnReadSms);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_GET_SmsList, m_pQSmsList);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_GET_SmsListByContactor, m_pQSmsListByContactor);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_SET_ReadStatus, m_pQUpdateReadStatus);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_SET_LockStatus, m_pQUpdateLockStatus);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_GET_SmsCode, m_pQCheckCode);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_INSERT_SmsCode, m_pQInsertCode);
	if ( FAILED_App(hr) ){
		return hr;
	}
	hr = CreateQuery(m_pclSqlDBSession, Sms_SQL_SET_SmsContent, m_pQUpdateSmsContent);
	if ( FAILED_App(hr) ){
		return hr;
	}

	hr = m_pclSqlSessionManager->Session_Connect(L"contacts", L".\\Documents and Settings\\", L"contacts.db", &m_pclSqlContactsDBSession );
	if(FAILED(hr) || m_pclSqlContactsDBSession == NULL)	
		return APP_Result_E_Fail;
	hr = CreateQuery(m_pclSqlContactsDBSession, Sms_SQL_GET_Name_ByPID, m_pQGetNameByPID);
	if ( FAILED_App(hr) ){
		return hr;
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteForSearch(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{	
	APP_Result hr = APP_Result_E_Fail;
	OperationCondition* pstConditions = NULL;
	long lConditionCount = 0;
	hr = clXmlOpe.GetOperationConditions(&pstConditions, &lConditionCount);
	if ( FAILED_App(hr) ){
		return APP_Result_Param_Invalid;
	}
	CDynamicArray<OperationCondition> spConditions(pstConditions, lConditionCount);
	//
	Search_DateKind enDateKind = Search_DateKind_Before;
	CSQL_SmartQuery pQHandle(m_pclSqlDBSession);
	DecideSearchCommond(spConditions, enDateKind, pQHandle);
	//ini bind param to null
	InitSearchParam(enDateKind, pQHandle);
	//bind param by conditions
	SetSearchParamByConditions(spConditions, enDateKind, pQHandle);
	//make recs
	hr = MakeSmsList(clXmlOpe, pQHandle.Get(), clResultXml, FALSE);
	if ( FAILED_App(hr) ){
		return hr;
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::DecideSearchCommond(CDynamicArray<OperationCondition>&spConditions, 
											Search_DateKind& enDateKind,CSQL_SmartQuery& pQHandle)
{
	APP_Result hr = APP_Result_E_Fail;
	long lC = spConditions.GetItemCount();
	for ( int i = 0; i < lC; i++ )
	{
		if ( 0 == wcscmp( L"beforetime", spConditions.GetItem(i)->wcsConditionName ) ){
			enDateKind = Search_DateKind_Before;
		}else if ( 0 == wcscmp( L"aftertime", spConditions.GetItem(i)->wcsConditionName ) ){
			enDateKind = Search_DateKind_After;
		}
		else if ( 0 == wcscmp( L"betweentime", spConditions.GetItem(i)->wcsConditionName ) ){
			enDateKind = Search_DateKind_Between;
		}else{

		}
	}
	if ( Search_DateKind_Before == enDateKind ){
		hr = pQHandle->Prepare(Sms_SQL_Search_SmsDetail_BeforeDate);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else if ( Search_DateKind_After == enDateKind ){
		hr = pQHandle->Prepare(Sms_SQL_Search_SmsDetail_AfterDate);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else if ( Search_DateKind_Between == enDateKind ){
		hr = pQHandle->Prepare(Sms_SQL_Search_SmsDetail_BetweenDate);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else{

	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::InitSearchParam(Search_DateKind& enDateKind,CSQL_SmartQuery& pQHandle)
{
	APP_Result hr = APP_Result_E_Fail;
	pQHandle->Bind(1);
	pQHandle->Bind(2);
	pQHandle->Bind(3);
	pQHandle->Bind(4);
	if(enDateKind == Search_DateKind_Between){
		pQHandle->Bind(5);
	}
	return APP_Result_S_OK;
}

APP_Result CSmsService::SetSearchParamByConditions(CDynamicArray<OperationCondition>&spConditions, 
											Search_DateKind& enDateKind,CSQL_SmartQuery& pQHandle)
{
	APP_Result hr = APP_Result_E_Fail;
	long lC = spConditions.GetItemCount();
	for ( int i = 0; i < lC; i++ )
	{
		if ( 0 == wcscmp( L"pid", spConditions.GetItem(i)->wcsConditionName ) ){
			long lPID = _wtol(spConditions.GetItem(i)->wcsConditionValue);
			pQHandle->Bind(1, lPID);
		}else if ( 0 == wcscmp( L"keword", spConditions.GetItem(i)->wcsConditionName ) ){
			wchar_t wcsSearchString[256] = L"";
			F_wcscpyn(wcsSearchString, L"%", sizeof(wcsSearchString)/sizeof(wcsSearchString[0]));
			F_wcscatn(wcsSearchString, spConditions.GetItem(i)->wcsConditionValue, sizeof(wcsSearchString)/sizeof(wcsSearchString[0]));
			F_wcscatn(wcsSearchString, L"%", sizeof(wcsSearchString)/sizeof(wcsSearchString[0]));
			pQHandle->Bind(2, wcsSearchString, sizeof(wcsSearchString)/sizeof(wcsSearchString[0]));
		}else if ( 0 == wcscmp( L"type", spConditions.GetItem(i)->wcsConditionName ) ){
			long lType = _wtol(spConditions.GetItem(i)->wcsConditionValue);
			pQHandle->Bind(3, lType);
		}else if ( 0 == wcscmp( L"beforetime", spConditions.GetItem(i)->wcsConditionName ) ){
			double lTime = _wtoll(spConditions.GetItem(i)->wcsConditionValue);
			pQHandle->Bind(4, lTime);
		}else if ( 0 == wcscmp( L"aftertime", spConditions.GetItem(i)->wcsConditionName ) ){
			double lTime = _wtoll(spConditions.GetItem(i)->wcsConditionValue);
			if ( enDateKind = Search_DateKind_Between ){				
				pQHandle->Bind(5, lTime);
			}else{
				pQHandle->Bind(4, lTime);
			}		
		}else{

		}
	}
	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteForDelete(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{	
	APP_Result hr = APP_Result_E_Fail;
	wchar_t wcsKindBuf[20] = L"";
	clXmlOpe.GetOperationKind(wcsKindBuf, sizeof(wcsKindBuf)/sizeof(wcsKindBuf[0]));
	if ( 0 == wcscmp(L"all", wcsKindBuf) ){
		CSQL_SmartQuery pQHandle(m_pclSqlDBSession);
		long lQID = 0;
		hr = m_pclSqlDBSession->Query_Create((int*)&lQID, &pQHandle );
		if( FAILED(hr) || pQHandle.Get() == NULL ) 
			return hr;
		hr = pQHandle->Prepare(Sms_SQL_DELETE_ALLSmsDetail);
		if( FAILED(hr) ) 
			return hr;
		hr = pQHandle->Step();
		if( FAILED(hr) ) 
			return hr;
	}else if ( 0 == wcscmp(L"sids", wcsKindBuf) ){
		NodeDataInfo* pstNodeDataInfos = NULL;
		long lNodeDataInfoCount = 0;
		hr = clXmlOpe.GetDeleteSIDs(&pstNodeDataInfos, &lNodeDataInfoCount);
		if ( FAILED_App(hr) ){
			return APP_Result_Param_Invalid;
		}
		CDynamicArray<NodeDataInfo> spNodes(pstNodeDataInfos, lNodeDataInfoCount);
		CSQL_SmartQuery pQHandle(m_pclSqlDBSession);
		long lQID = 0;
		hr = m_pclSqlDBSession->Query_Create((int*)&lQID, &pQHandle );
		if( FAILED(hr) || pQHandle.Get() == NULL ) 
			return hr;
		hr = pQHandle->Prepare(Sms_SQL_DELETE_SmsDetail);
		if( FAILED(hr) ) 
			return hr;
		for ( int i =  0; i < lNodeDataInfoCount; i++ )
		{
			pQHandle->Reset();
			long lSID = _wtol(pstNodeDataInfos[i].wcsNodeValue);
			pQHandle->Bind(1, lSID);
			pQHandle->Step();
		}
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteForAdd(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{	
	APP_Result hr = APP_Result_E_Fail;
	NodeDataInfo* pstNodeDataInfos = NULL;
	long lNodeDataInfoCount = 0;
	hr = clXmlOpe.GetAddSmsInfos(&pstNodeDataInfos, &lNodeDataInfoCount);
	if ( FAILED_App(hr) ){
		return APP_Result_Param_Invalid;
	}
	CDynamicArray<NodeDataInfo> spNodes(pstNodeDataInfos, lNodeDataInfoCount);
	
	CSQL_SmartQuery pQHandle(m_pclSqlDBSession);
	long lQID = 0;
	hr = m_pclSqlDBSession->Query_Create((int*)&lQID, &pQHandle );
	if( FAILED(hr) || pQHandle.Get() == NULL ) 
		return hr;
	hr = pQHandle->Prepare(Sms_SQL_INSERT_SmsDetail);
	if( FAILED(hr) ) 
		return hr;

	/*long lSID = _wtol(pstNodeDataInfos[0].wcsNodeValue);
	pQHandle->Bind(1, lSID);*/
	long lType = _wtol(pstNodeDataInfos[2].wcsNodeValue);
	pQHandle->Bind(3, lType);
	long lSize = wcslen(pstNodeDataInfos[3].wcsNodeValue);
	pQHandle->Bind(4, pstNodeDataInfos[3].wcsNodeValue, lSize);//content
	lSize = wcslen(pstNodeDataInfos[4].wcsNodeValue);
	pQHandle->Bind(5, pstNodeDataInfos[4].wcsNodeValue, lSize);//address
	double dTime = _wtoll(pstNodeDataInfos[5].wcsNodeValue);
	pQHandle->Bind(6, dTime);
	long lLockStatus = _wtol(pstNodeDataInfos[6].wcsNodeValue);
	pQHandle->Bind(7, lLockStatus);
	long lReadStatus = _wtol(pstNodeDataInfos[7].wcsNodeValue);
	pQHandle->Bind(8, lReadStatus);
	long lPID = 0;
	hr = GetPIDByAddress(pstNodeDataInfos[4].wcsNodeValue, lPID);
	if ( FAILED_App(hr) ){
		return hr;
	}
	pQHandle->Bind(2, lPID);
	hr = pQHandle->Step();
	if ( FAILED_App(hr) ){
		return hr;
	}
	
	return APP_Result_S_OK;
}

APP_Result CSmsService::GetPIDByAddress(wchar_t* pwcsAddress, long& lPID)
{
	APP_Result hr = APP_Result_E_Fail;
	//CSQL_query*			pQHandle;
	CSQL_SmartQuery pQHandle(m_pclSqlContactsDBSession);
	long lQID = 0;
	hr = m_pclSqlContactsDBSession->Query_Create((int*)&lQID, &pQHandle );
	if( FAILED(hr) || pQHandle.Get() == NULL ) 
		return hr;
	//CSQL_SmartQuery spQ(pQHandle, m_pclSqlDBSession);
	hr = pQHandle->Prepare(Sms_SQL_GET_PID_ByAddress);
	if( FAILED(hr) ) 
		return hr;
	pQHandle->Bind(1, pwcsAddress, wcslen(pwcsAddress));

	hr = pQHandle->Step();
	if ( FAILED_App(hr) ){
		return hr;
	}
	pQHandle->GetField(0, &lPID);

	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteForEdit(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml)
{	
	APP_Result hr = APP_Result_E_Fail;
	wchar_t wcsOpeKind[20] = L""; 
	hr = clXmlOpe.GetOperationKind(wcsOpeKind, sizeof(wcsOpeKind)/sizeof(wcsOpeKind[0]));
	if ( FAILED_App(hr) ){
		return hr;
	}
	if ( 0 == wcscmp(wcsOpeKind, L"sms") ){
		hr = UpdateSmsInfo(clXmlOpe, clResultXml);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else if ( 0 == wcscmp(wcsOpeKind, L"protectdata") ){
		wchar_t wcsProtectDataKind[20] = L""; 
		hr = clXmlOpe.GetProtectDataKind(wcsProtectDataKind, sizeof(wcsProtectDataKind)/sizeof(wcsProtectDataKind[0]));
		if ( FAILED_App(hr) ){
			return hr;
		}
		if ( 0 == wcscmp(wcsProtectDataKind, L"add") ){
			hr = AddProtectDatta(clXmlOpe, clResultXml);
			if ( FAILED_App(hr) ){
				return hr;
			}
		}else if ( 0 == wcscmp(wcsProtectDataKind, L"edit") ){
			hr = EditProtectDatta(clXmlOpe, clResultXml);
			if ( FAILED_App(hr) ){
				return hr;
			}
		}else if ( 0 == wcscmp(wcsProtectDataKind, L"delete") ){
			hr = DeleteProtectDatta(clXmlOpe, clResultXml);
			if ( FAILED_App(hr) ){
				return hr;
			}
		}else{
			
		}
	}

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
	m_pQSmsListByContactor->Reset();
	m_pQUnReadSms->Reset();
	APP_Result hr = APP_Result_E_Fail;
	long lPID = Invalid_ID;
	wchar_t wcsCode[20] = L"";
	for ( int i = 0; i < lConditionCount; i++ )
	{
		if ( 0 == wcscmp(pConditions[i].wcsConditionName, L"unread") ){
			*ppQueryNeeded = m_pQUnReadSms.Get();
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
					*ppQueryNeeded = m_pQSmsListByContactor.Get();
					bIsPermitDecode = TRUE;
				}else{
					*ppQueryNeeded = NULL;
					bIsPermitDecode = FALSE;
					return APP_Result_ProtectData_CodeWrong;
				}		
			}
			else{
				*ppQueryNeeded = NULL;
				bIsPermitDecode = FALSE;
				return APP_Result_ProtectData_NeedCode;
			}
		}else{
			m_pQSmsListByContactor->Bind(1,lPID);
			*ppQueryNeeded = m_pQSmsListByContactor.Get();
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
	if ( APP_Result_S_OK == hr ){
		bNeedDecode = FALSE;
	}else{
		wchar_t* pCode = NULL;
		m_pQCheckCode->GetField(1, &pCode);
		if ( NULL != pCode && L'\0' == pCode[0] ){
			F_wcscpyn(pwcsDBCode, pCode, lCodeSize);
			bNeedDecode = TRUE;
		}
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
		F_wcscpyn(wcsEncodeType, L"none", sizeof(wcsEncodeType)/sizeof(wcsEncodeType[0]));
	}else if ( lListCount == lEncodeCount ){
		F_wcscpyn(wcsEncodeType, L"all", sizeof(wcsEncodeType)/sizeof(wcsEncodeType[0]));
	}else if ( lListCount > lEncodeCount ){
		F_wcscpyn(wcsEncodeType, L"part", sizeof(wcsEncodeType)/sizeof(wcsEncodeType[0]));
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
	//pQHandle->Reset();
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
			
		wchar_t* pName = NULL;
		m_pQGetNameByPID->Reset();
		m_pQGetNameByPID->Bind(lPID);
		m_pQGetNameByPID->Step();
		m_pQGetNameByPID->GetField(0, &pName);
		CXmlNode clNodeName(L"name");
		clNodePID.SetNodeContent(NULL, pName, NULL, 0);

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

APP_Result CSmsService::AddProtectDatta(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml)
{
	APP_Result hr = APP_Result_E_Fail;
	// get contactor's id,and code
	NodeDataInfo* pProtectDatas = NULL;
	long lProtectDataBufCount = 0;
	hr = clReqXmlOpe.GetProtectDatas(&pProtectDatas, &lProtectDataBufCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<NodeDataInfo> spNodes(pProtectDatas, lProtectDataBufCount);
	//insert code
	long lPID = Invalid_ID;
	if ( 0 == wcscmp(L"pid", pProtectDatas[0].wcsNodeName) ){
		lPID = _wtol(pProtectDatas[0].wcsNodeValue);
	}
	if ( Invalid_ID != lPID ){
		if ( 0 == wcscmp(L"code", pProtectDatas[1].wcsNodeName) ){
			m_pQInsertCode->Reset();
			m_pQInsertCode->Bind( 1, lPID );
			m_pQInsertCode->Bind( 2, pProtectDatas[1].wcsNodeValue, 
								sizeof(pProtectDatas[1].wcsNodeValue)/sizeof(pProtectDatas[1].wcsNodeValue[0]) );
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

APP_Result CSmsService::EditProtectDatta(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml)
{
	APP_Result hr = APP_Result_E_Fail;
	
	NodeDataInfo* pProtectDatas = NULL;
	long lProtectDataBufCount = 0;
	hr = clReqXmlOpe.GetProtectDatas(&pProtectDatas, &lProtectDataBufCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<NodeDataInfo> spNodes(pProtectDatas, lProtectDataBufCount);
	//save code
	long lPID = Invalid_ID;
	if ( 0 == wcscmp(L"pid", pProtectDatas[0].wcsNodeName) ){
		lPID = _wtol(pProtectDatas[0].wcsNodeValue);
	}
	if ( Invalid_ID != lPID ){
		//check input code
			hr = CheckInputCode(lPID, pProtectDatas[1].wcsNodeValue);
			if ( FAILED_App(hr) ){
				return hr;
			}
			//success,save new code
			hr = UpdateCode(lPID, pProtectDatas[2].wcsNodeValue);
			if ( FAILED_App(hr) ){
				return hr;
			}
	}else{
		return APP_Result_Param_Invalid;
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::CheckInputCode(long lPID, wchar_t* pwcsInputCode)
{
	APP_Result hr = APP_Result_E_Fail;
	m_pQCheckCode->Reset();
	m_pQCheckCode->Bind(1, lPID);
	hr = m_pQCheckCode->Step();
	if ( FAILED_App(hr) ){
		return hr;
	}
	if ( APP_Result_S_OK  == hr ){
		return APP_Result_Param_Invalid;
	}
	
	wchar_t wcsDBCode[256] = L"";
	ConvertDisplayCode2DBCode( pwcsInputCode, wcsDBCode, sizeof(wcsDBCode)/sizeof(wcsDBCode[0]) );
	wchar_t* pComparedDBCode = NULL;
	m_pQCheckCode->GetField(1, &pComparedDBCode);
	if ( NULL != pComparedDBCode && L'\0' == pComparedDBCode[0] ){
		if ( 0 != wcscmp(wcsDBCode, pComparedDBCode) ){
			return APP_Result_ProtectData_CodeWrong;
		}
	}else{
		return APP_Result_E_Fail;
	}

	return APP_Result_S_OK;
}

APP_Result CSmsService::UpdateCode(long lPID, wchar_t* pwcsInputCode)
{
	APP_Result hr = APP_Result_E_Fail;
	CSQL_query*			pQUpdateCode;
	long lID_UpdateCode = 0;
	hr = m_pclSqlDBSession->Query_Create((int*)&lID_UpdateCode, &pQUpdateCode );
	if( FAILED(hr) || pQUpdateCode == NULL ) 
		return hr;
	hr = pQUpdateCode->Prepare(Sms_SQL_SET_SmsCode);
	if( FAILED(hr) ) 
		return hr;
	wchar_t wcsDBCode[256] = L"";
	ConvertDisplayCode2DBCode(pwcsInputCode, wcsDBCode, sizeof(wcsDBCode)/sizeof(wcsDBCode[0]));
	pQUpdateCode->Bind(1, wcsDBCode, wcslen(wcsDBCode));
	pQUpdateCode->Bind(2, lPID);
	hr = pQUpdateCode->Step();
	if ( FAILED_App(hr) ){
		return hr;
	}
	pQUpdateCode->Finalize();
	m_pclSqlDBSession->Query_Delete(lID_UpdateCode);

	return APP_Result_S_OK;
}

APP_Result CSmsService::DeleteCode(long lPID)
{
	APP_Result hr = APP_Result_E_Fail;
	CSQL_query*			pQUpdateCode;
	long lID_UpdateCode = 0;
	hr = m_pclSqlDBSession->Query_Create((int*)&lID_UpdateCode, &pQUpdateCode );
	if( FAILED(hr) || pQUpdateCode == NULL ) 
		return hr;
	hr = pQUpdateCode->Prepare(Sms_SQL_DELETE_SmsCode);
	if( FAILED(hr) ) 
		return hr;
	pQUpdateCode->Bind(1, lPID);
	hr = pQUpdateCode->Step();
	if ( FAILED_App(hr) ){
		return hr;
	}
	pQUpdateCode->Finalize();
	m_pclSqlDBSession->Query_Delete(lID_UpdateCode);

	return APP_Result_S_OK;
}

APP_Result CSmsService::DeleteProtectDatta(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml)
{
	APP_Result hr = APP_Result_E_Fail;
	NodeDataInfo* pProtectDatas = NULL;
	long lProtectDataBufCount = 0;
	hr = clReqXmlOpe.GetProtectDatas(&pProtectDatas, &lProtectDataBufCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<NodeDataInfo> spNodes(pProtectDatas, lProtectDataBufCount);
	//delete code
	long lPID = Invalid_ID;
	if ( 0 == wcscmp(L"pid", pProtectDatas[0].wcsNodeName) ){
		lPID = _wtol(pProtectDatas[0].wcsNodeValue);
	}
	if ( Invalid_ID != lPID ){
		//check input code
		hr = CheckInputCode(lPID, pProtectDatas[1].wcsNodeValue);
		if ( FAILED_App(hr) ){
			return hr;
		}
		//success,delete code
		hr = DeleteCode(lPID);
		if ( FAILED_App(hr) ){
			return hr;
		}
	}else{
		return APP_Result_Param_Invalid;
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

APP_Result CSmsService::UpdateSmsInfo(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml)
{
	APP_Result hr = APP_Result_E_Fail;
	// get contactor's id,and code
	NodeDataInfo* pEditSmsInfos = NULL;
	long lEditSmsInfoBufCount = 0;
	hr = clReqXmlOpe.GetEditSmsInfos(&pEditSmsInfos, &lEditSmsInfoBufCount);
	if ( FAILED_App(hr) ){
		return hr;
	}
	CDynamicArray<NodeDataInfo> spNodes(pEditSmsInfos, lEditSmsInfoBufCount);
	//insert code
	long lSID = Invalid_ID;
	if ( 0 == wcscmp(L"sid", pEditSmsInfos[0].wcsNodeName) ){
		lSID = _wtol(pEditSmsInfos[0].wcsNodeValue);
	}
	if ( Invalid_ID != lSID ){
		if ( 0 == wcscmp(L"readstatus", pEditSmsInfos[1].wcsNodeName) ){
			hr = UpdateSmsRecInfo(lSID, m_pQUpdateReadStatus.Get(), pEditSmsInfos[1].wcsNodeValue);
			if ( FAILED_App(hr) ){
				return hr;
			}
		}else if ( 0 == wcscmp(L"lockstatus", pEditSmsInfos[1].wcsNodeName) ){
			hr = UpdateSmsRecInfo(lSID, m_pQUpdateLockStatus.Get(), pEditSmsInfos[1].wcsNodeValue);
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

	return APP_Result_S_OK;
}

APP_Result CSmsService::UpdateSmsRecInfo(long lSID, CSQL_query* pQHandle, wchar_t* pwcsValue)
{
	APP_Result hr = APP_Result_E_Fail;
	long lValue = _wtol(pwcsValue);
	pQHandle->Reset();
	pQHandle->Bind(1, lValue);
	pQHandle->Bind(2, lSID);
	hr = pQHandle->Step();
	if ( FAILED_App(hr) ){
		return hr;
	}

	return APP_Result_S_OK;
}