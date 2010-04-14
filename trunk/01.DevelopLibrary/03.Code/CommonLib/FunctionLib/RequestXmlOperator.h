#ifndef __RequestXmlOperator_h__
#define __RequestXmlOperator_h__

#define Path_RequestDataType	L"request/data/"

struct ListCondition{
	wchar_t			wcsConditionName[20];
	wchar_t			wcsConditionValue[20];
};

class COMMONLIB_API CRequestXmlOperator
{
public:
	CRequestXmlOperator();
	~CRequestXmlOperator();
	APP_Result Initialize(wchar_t* pwcsRequestXML);
	APP_Result GetOperationType( wchar_t* pwcsTypeBuf, long lBufCount );
	APP_Result GetListKind( wchar_t* pwcsKindBuf, long lBufCount );
	APP_Result GetListConditions( ListCondition** ppwcsConditionBuf, long* plBufCount );
protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);

private:
	CXmlStream m_clXmlStream;
};

#endif __RequestXmlOperator_h__