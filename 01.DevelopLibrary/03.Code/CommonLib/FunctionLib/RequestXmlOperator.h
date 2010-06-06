#ifndef __RequestXmlOperator_h__
#define __RequestXmlOperator_h__

#define Path_RequestDataType	L"request/data/"

struct OperationCondition{
	wchar_t			wcsConditionName[20];
	wchar_t			wcsConditionValue[20];
};

struct NodeDataInfo{
	wchar_t			wcsNodeName[20];
	wchar_t			wcsNodeValue[20];
};

class COMMONLIB_API CRequestXmlOperator
{
public:
	CRequestXmlOperator();
	~CRequestXmlOperator();
	APP_Result Initialize(wchar_t* pwcsRequestXML);
	APP_Result GetOperationType( wchar_t* pwcsTypeBuf, long lBufCount );
	APP_Result GetProtectDataKind( wchar_t* pwcsKindBuf, long lBufCount );
	APP_Result GetOperationKind( wchar_t* pwcsKindBuf, long lBufCount );
	APP_Result GetOperationConditions( OperationCondition** ppwcsConditionBuf, long* plBufCount );

	APP_Result GetProtectDatas( NodeDataInfo** ppwcsProtectDataBuf, long* plBufCount );
	APP_Result GetEditSmsInfos( NodeDataInfo** ppwcsProtectDataBuf, long* plBufCount );
	APP_Result GetAddSmsInfos( NodeDataInfo** ppwcsProtectDataBuf, long* plBufCount );
	APP_Result GetDetailSmsInfos( NodeDataInfo** ppwcsProtectDataBuf, long* plBufCount );
	
	APP_Result GetDeleteSIDs( NodeDataInfo** ppwcsDataBuf, long* plBufCount );
protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);
private:
	APP_Result AppendNodeInfo(wchar_t* pPath, wchar_t* pNodeName, 
				CDynamicArray<NodeDataInfo>& spNodeInfos);

private:
	CXmlStream m_clXmlStream;
};

#endif __RequestXmlOperator_h__