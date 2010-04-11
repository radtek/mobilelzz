#ifndef __SmsService_h__
#define __SmsService_h__

class CSmsService : public CBasicService
{
public:
	CSmsService();
	~CSmsService();

protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);
	virtual APP_Result MakeResult(wchar_t** ppwcsResultXML);	

private:

};

#endif __SmsService_h__