#ifndef __SmsService_h__
#define __SmsService_h__

class CSmsService : public CBasicService
{
public:
	CSmsService();
	~CSmsService();
	virtual APP_Result Initialize();
protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);
	virtual APP_Result MakeResult(wchar_t** ppwcsResultXML);	
private:
	APP_Result ExcuteForSearch(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml);
	APP_Result ExcuteForDelete(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml);
	APP_Result ExcuteForAdd(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml);
	APP_Result ExcuteForEdit(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml);
	APP_Result ExcuteForList(CRequestXmlOperator& clXmlOpe, CXmlStream& clResultXml);

	APP_Result AddProtectDatta(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml);
	APP_Result EditProtectDatta(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml);
	APP_Result DeleteProtectDatta(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml);


	APP_Result DecideQuery(OperationCondition* pConditions, long lConditionCount, 
					CSQL_query** ppQueryNeeded, BOOL& bIsPermitDecode);
	APP_Result MakeSmsList(CRequestXmlOperator& clXmlOpe, CSQL_query* pQHandle, 
					CXmlStream& clResultXml, BOOL bIsPermitDecode);
	APP_Result MakeSmsListRecs( CSQL_query* pQHandle, CXmlNode* pNodeList, BOOL bIsPermitDecode,
					long& lListCount, long& lEncodeCount );
	APP_Result CheckCode(long lPID, BOOL& bNeedDecode, 
					wchar_t* pwcsDBCode, long lCodeSize);
	APP_Result ConvertDisplayCode2DBCode(wchar_t* pwcsCode, wchar_t* pwcsDBCode, 
		long lDBCodeCount );
	APP_Result ConvertDBCode2DisplayCode(wchar_t* pDBCode, wchar_t* pwcsCode, 
					long lCodeCount);

	APP_Result EncodeSmsContentByContactor(long lPID);

	APP_Result CheckInputCode(long lPID, wchar_t* pwcsInputCode);
	APP_Result UpdateCode(long lPID, wchar_t* pwcsInputCode);
	APP_Result DeleteCode(long lPID);

	APP_Result UpdateSmsInfo(CRequestXmlOperator& clReqXmlOpe, CXmlStream& clResultXml);
	APP_Result UpdateSmsRecInfo(long lSID, CSQL_query* pQHandle, wchar_t* pwcsValue);

	APP_Result GetPIDByAddress(wchar_t* pwcsAddress, long& lPID);

	APP_Result CreateTable(wchar_t* pSqlCommand);
private:
	CSQL_query*			m_pQUnReadSms;
	long				m_lID_QUnReadSms;

	CSQL_query*			m_pQSmsList;
	long				m_lID_QSmsList;

	CSQL_query*			m_pQSmsListByContactor;
	long				m_lID_QSmsListByContactor;

	CSQL_query*			m_pQSmsGroupInfo;
	long				m_lID_QSmsGroupInfo;

	CSQL_query*			m_pQUpdateReadStatus;
	long				m_lID_QSmsReadStatus;

	CSQL_query*			m_pQUpdateLockStatus;
	long				m_lID_QUpdateLockStatus;

	CSQL_query*			m_pQCheckCode;
	long				m_lID_QCheckCode;

	CSQL_query*			m_pQInsertCode;
	long				m_lID_QInsertCode;

	CSQL_query*			m_pQUpdateSmsContent;
	long				m_lID_QUpdateSmsContent;

	CSQL_session*		m_pclSqlDBSession;
	
};

#endif __SmsService_h__