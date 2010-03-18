#ifndef __BasicService_h__
#define __BasicService_h__

class CBasicService
{
public:
	CBasicService();
	~CBasicService();
	
	HRESULT Excute(wchar_t* pwcsRequestXML, wchar_t* pwcsResultXML);

protected:
	virtual HRESULT MakeParam();
	virtual HRESULT ExcuteParam();
	virtual HRESULT MakeResult();	
	
private:
	
};

#endif __BasicService_h__