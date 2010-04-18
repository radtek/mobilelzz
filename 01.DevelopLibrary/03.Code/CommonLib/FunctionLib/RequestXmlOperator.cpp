#include "stdafx.h"
#include "Errors.h"
#include "FuncLib.h"
#include "XmlStream.h"
#include "DynamicArray.h"
#include "RequestXmlOperator.h"



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

APP_Result CRequestXmlOperator::GetProtectDataKind( wchar_t* pwcsKindBuf, long lBufCount )
{
	APP_Result appR = APP_Result_E_Fail;
	CXmlNode* pOperationNode = NULL;
	appR = m_clXmlStream.SelectNode(L"request/data/operation/protectdata/", &pOperationNode);
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

	if ( 0 == wcscmp(L"kind", pOperationAttribute[0].wcsName) ){
		F_wcscpyn( pwcsKindBuf, pOperationAttribute[0].wcsValue, lBufCount );
	}else{
		return APP_Result_E_Fail;
	}

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::GetOperationKind( wchar_t* pwcsKindBuf, long lBufCount )
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

APP_Result CRequestXmlOperator::GetOperationConditions( OperationCondition** ppwcsConditionBuf, long* plBufCount )
{
	APP_Result appR = APP_Result_E_Fail;
	CXmlNode* pNode = NULL;
	appR = m_clXmlStream.SelectNode(L"request/data/operation/condition/", &pNode);
	if ( FAILED_App(appR) ){
		return APP_Result_S_OK;
	}
	auto_ptr<CXmlNode> sp(pNode);
	CDynamicArray<OperationCondition>	spConditions;
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
		OperationCondition stCondition;
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

APP_Result CRequestXmlOperator::GetProtectDatas( NodeDataInfo** ppwcsProtectDataBuf, long* plBufCount )
{
	APP_Result appR = APP_Result_E_Fail;	
	CDynamicArray<NodeDataInfo>	spProtectDatas;
	AppendNodeInfo(L"request/data/operation/protectdata/pid/", L"pid", spProtectDatas);
	AppendNodeInfo(L"request/data/operation/protectdata/code/", L"code", spProtectDatas);
	AppendNodeInfo(L"request/data/operation/protectdata/newcode/", L"newcode", spProtectDatas);

	*ppwcsProtectDataBuf = spProtectDatas.Detatch();
	*plBufCount = spProtectDatas.GetItemCount();

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::GetEditSmsInfos( NodeDataInfo** ppwcsProtectDataBuf, long* plBufCount )
{
	APP_Result appR = APP_Result_E_Fail;	
	CDynamicArray<NodeDataInfo>	spProtectDatas;
	AppendNodeInfo(L"request/data/operation/sms/sid/", L"sid", spProtectDatas);
	AppendNodeInfo(L"request/data/operation/sms/readstatus/", L"readstatus", spProtectDatas);
	AppendNodeInfo(L"request/data/operation/sms/lockstatus/", L"lockstatus", spProtectDatas);

	*ppwcsProtectDataBuf = spProtectDatas.Detatch();
	*plBufCount = spProtectDatas.GetItemCount();

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::GetAddSmsInfos( NodeDataInfo** ppwcsProtectDataBuf, long* plBufCount )
{
	APP_Result appR = APP_Result_E_Fail;	
	CDynamicArray<NodeDataInfo>	spNodeDataInfos;
	AppendNodeInfo(L"request/data/operation/sid/", L"sid", spNodeDataInfos);
	AppendNodeInfo(L"request/data/operation/type/", L"type", spNodeDataInfos);
	AppendNodeInfo(L"request/data/operation/content/", L"content", spNodeDataInfos);
	AppendNodeInfo(L"request/data/operation/address/", L"address", spNodeDataInfos);
	AppendNodeInfo(L"request/data/operation/time/", L"time", spNodeDataInfos);
	AppendNodeInfo(L"request/data/operation/lockstatus/", L"lockstatus", spNodeDataInfos);
	AppendNodeInfo(L"request/data/operation/readstatus/", L"readstatus", spNodeDataInfos);

	*ppwcsProtectDataBuf = spNodeDataInfos.Detatch();
	*plBufCount = spNodeDataInfos.GetItemCount();

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::GetDeleteSIDs( NodeDataInfo** ppwcsDataBuf, long* plBufCount )
{
	APP_Result appR = APP_Result_E_Fail;	
	CDynamicArray<NodeDataInfo>	spNodeDataInfos;

	CXmlNode* pNode = NULL;
	appR = m_clXmlStream.SelectNode(L"request/data/operation/sid/", &pNode);
	if ( FAILED_App(appR) ){
		return APP_Result_S_OK;
	}
	auto_ptr<CXmlNode> sp(pNode);
	CDynamicArray<NodeDataInfo>	spNodeDatas;
	EN_MOVE enR = MOVE_OK;
	while(enR!= MOVE_END)
	{
		wchar_t* pValue = NULL;
		appR = pNode->GetNodeContent( NULL, &pValue, NULL, NULL );
		if ( FAILED_App(appR) ){
			return appR;
		}
		auto_ptr<wchar_t> sp(pValue);
		NodeDataInfo stNode;
		F_wcscpyn(stNode.wcsNodeName, L"sid", sizeof(stNode.wcsNodeName)/sizeof(stNode.wcsNodeName[0]));
		F_wcscpyn(stNode.wcsNodeValue, pValue, sizeof(stNode.wcsNodeValue)/sizeof(stNode.wcsNodeValue[0]));
		spNodeDatas.AppendItem(&stNode);

		enR = pNode->MoveNext();
	}

	*ppwcsDataBuf = spNodeDatas.Detatch();
	*plBufCount = spNodeDatas.GetItemCount();

	return APP_Result_S_OK;
}

APP_Result CRequestXmlOperator::AppendNodeInfo(wchar_t* pPath, wchar_t* pNodeName, CDynamicArray<NodeDataInfo>& spNodeInfos)
{
	APP_Result hr = APP_Result_E_Fail;	
	CXmlNode* pNode = NULL;
	hr = m_clXmlStream.SelectNode(pPath, &pNode);
	if ( SUCCEEDED_App(hr) ){
		auto_ptr<CXmlNode> sp(pNode);
		NodeDataInfo stTemp;
		F_wcscpyn(stTemp.wcsNodeName, pNodeName, 
			sizeof(stTemp.wcsNodeName)/sizeof(stTemp.wcsNodeName[0]));
		wchar_t* pNodeContent = NULL;
		
		hr = pNode->GetNodeContent( NULL, &pNodeContent, NULL, NULL );
		if ( FAILED_App(hr) ){
			return hr;
		}
		auto_ptr<wchar_t> spNodeContent(pNodeContent);
		F_wcscpyn(stTemp.wcsNodeValue, pNodeContent, 
			sizeof(stTemp.wcsNodeValue)/sizeof(stTemp.wcsNodeValue[0]));

		spNodeInfos.AppendItem(&stTemp);
	}

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