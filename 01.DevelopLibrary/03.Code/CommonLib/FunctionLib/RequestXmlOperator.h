#ifndef __RequestXmlOperator_h__
#define __RequestXmlOperator_h__

#define Path_RequestDataType	L"request/data/"


class COMMONLIB_API CRequestXmlOperator
{
public:
	CRequestXmlOperator();
	~CRequestXmlOperator();
	
	//APP_Result GetNode(wchar_t* pwcsNodePath );
protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);

private:

};

#endif __RequestXmlOperator_h__