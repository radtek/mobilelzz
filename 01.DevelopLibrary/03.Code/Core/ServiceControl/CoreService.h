#ifndef __CoreService_h__
#define __CoreService_h__

class CSmsService;
class CContactorsService;

class CORE_API CCoreService
{
public:
	static CCoreService* GetInstance();
	static void DeleteInstance();

	APP_Result Request(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);

protected:

private:
	CCoreService();
	~CCoreService();
	CCoreService& operator = (CCoreService& clCoreService);

	APP_Result GetDataType(wchar_t* pwcsRequestXML, wchar_t* pwcsDataType, long lSize);
private:
	static CCoreService*	m_Instance;

	CSmsService*				m_pclSmsService;
	CContactorsService*			m_pclContactorsService;

};

#endif __CoreService_h__