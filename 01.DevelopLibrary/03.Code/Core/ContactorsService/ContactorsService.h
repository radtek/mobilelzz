#ifndef __ContactorsService_h__
#define __ContactorsService_h__

enum Contactors_OperationType{
	Contactors_OperationType_List,
	Contactors_OperationType_Add,
	Contactors_OperationType_Delete,
	Contactors_OperationType_Edit,
	Contactors_OperationType_Detail
};

enum Contactors_ListKind{
	Contactors_ListKind_Contactors,
	Contactors_ListKind_PhoneNo
};

enum Contactors_ListSortType{
	Contactors_ListSortType_Name
};

struct Contactors_OperationListData
{
	Contactors_ListKind			enListKind;
	Contactors_ListSortType		enListSortType;

};

union Contactors_OperationData{
	Contactors_OperationListData		stOperationListData;
	
};

struct Contactors_RequestData{
	Contactors_OperationType		enOperationType;
	Contactors_OperationData		stOperationData;
};

class CContactorsService : public CBasicService
{
public:
	CContactorsService();
	~CContactorsService();
	virtual APP_Result Initialize();
protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);
	virtual APP_Result MakeResult(wchar_t** ppwcsResultXML);	
	
private:
	void				MakeFirstLetter(wchar_t* pwcsFirstLetter, long lPID );
	void				MakeSmsCount(wchar_t* pwcsSmsCount, long lPID );
	APP_Result			ExcuteForList(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml);
private:
	CSQL_query*			m_pQFirstLetter;
	long				m_lID_QFirstLetter;
	CSQL_query*			m_pQContactorsList;
	long				m_lID_QContactorsList;

	CSQL_SmartQuery			m_pQSmsGroupInfo;

	Contactors_RequestData		m_stRequestData;

	CSQL_session*		m_pclSqlDBSession;

	CSQL_session*		m_pclSqlSmsDBSession;
};

#endif __ContactorsService_h__