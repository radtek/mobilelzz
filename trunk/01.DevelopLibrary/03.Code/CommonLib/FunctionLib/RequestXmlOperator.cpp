#include "stdafx.h"
#include "Errors.h"
#include "XmlStream.h"
#include "RequestXmlOperator.h"

#include "FuncLib.h"
#include "DynamicArray.h"

CRequestXmlOperator::CRequestXmlOperator()
{

}

CRequestXmlOperator::~CRequestXmlOperator()
{

}

APP_Result CRequestXmlOperator::Initialize(wchar_t* pwcsRequestXML)
{
	APP_Result appR = APP_Result_E_Fail;
	long lXmlBufSize = wcslen(pwcsRequestXML);
	appR = m_clXmlStream.Load(pwcsRequestXML, lXmlBufSize);
	if ( FAILED_App(appR) ){
		return appR;
	}
	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::GetOperationType( wchar_t* pwcsTypeBuf, long lBufCount )
{
	APP_Result appR = APP_Result_E_Fail;
	CXmlNode* pOperationNode = NULL;
	appR = m_clXmlStream.SelectNode(L"request/data/operation/", &pOperationNode);
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
		F_wcscpyn( pwcsTypeBuf, pOperationAttribute->wcsValue, lBufCount );
	}else{
		return APP_Result_E_Fail;
	}

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::GetListKind( wchar_t* pwcsKindBuf, long lBufCount )
{
	APP_Result appR = APP_Result_E_Fail;
	CXmlNode* pOperationNode = NULL;
	appR = m_clXmlStream.SelectNode(L"request/data/operation/", &pOperationNode);
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

	if ( 0 == wcscmp(L"kind", pOperationAttribute[1].wcsName) ){
		F_wcscpyn( pwcsKindBuf, pOperationAttribute[1].wcsValue, lBufCount );
	}else{
		return APP_Result_E_Fail;
	}

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::GetListConditions( ListCondition** ppwcsConditionBuf, long* plBufCount )
{
	APP_Result appR = APP_Result_E_Fail;
	CXmlNode* pNode = NULL;
	appR = m_clXmlStream.SelectNode(L"request/data/operation/condition/", &pNode);
	if ( FAILED_App(appR) ){
		return APP_Result_S_OK;
	}
	auto_ptr<CXmlNode> sp(pNode);
	CDynamicArray<ListCondition>	spConditions;
	EN_MOVE enR = MOVE_OK;
	while(enR!= MOVE_END)
	{
		NodeAttribute_t* pAttribute = NULL;
		long lAttributeCount = 0;
		appR = pNode->GetNodeContent( NULL, NULL, &pAttribute, &lAttributeCount );
		if ( FAILED_App(appR) ){
			return appR;
		}
		CDynamicArray<NodeAttribute_t> spTempMem(pAttribute, lAttributeCount);
		ListCondition stCondition;
		memset( &stCondition, 0x0, sizeof(stCondition) );
		if ( 0 == wcscmp(L"name", pAttribute->wcsName) ){
			F_wcscpyn( stCondition.wcsConditionName, pAttribute->wcsValue, 
				sizeof(stCondition.wcsConditionName)/sizeof(stCondition.wcsConditionName[0]) );
		}else{
			return APP_Result_E_Fail;
		}
		if ( 0 == wcscmp(L"value", pAttribute[1].wcsName) ){
			F_wcscpyn( stCondition.wcsConditionValue, pAttribute[1].wcsValue, 
				sizeof(stCondition.wcsConditionValue)/sizeof(stCondition.wcsConditionValue[0]) );
		}else{
			return APP_Result_E_Fail;
		}
		spConditions.AppendItem(&stCondition);
		enR = pNode->MoveNext();
	}
	*ppwcsConditionBuf = spConditions.Detatch();
	*plBufCount = spConditions.GetItemCount();

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::MakeParam(wchar_t* pwcsRequestXML)
{


	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
{


	return APP_Result_S_OK;
}