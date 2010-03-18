#ifndef __ContactorsService_h__
#define __ContactorsService_h__

class CContactorsService//:public CBasicService
{
public:
	CContactorsService();
	~CContactorsService();
	
protected:
	HRESULT MakeParam();
	HRESULT ExcuteParam();
	HRESULT MakeResult();	
	
	
private:
	
		
};

#endif __ContactorsService_h__