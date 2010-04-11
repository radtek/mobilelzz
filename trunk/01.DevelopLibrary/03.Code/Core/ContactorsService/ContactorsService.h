#ifndef __ContactorsService_h__
#define __ContactorsService_h__

class CContactorsService : public CBasicService
{
public:
	CContactorsService();
	~CContactorsService();
	
protected:
	virtual APP_Result MakeParam(wchar_t* pwcsRequestXML);
	virtual APP_Result ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML);
	virtual APP_Result MakeResult(wchar_t** ppwcsResultXML);	
	
	
private:
	
		
};

#endif __ContactorsService_h__