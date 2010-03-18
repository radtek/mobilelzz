#ifndef __GroupService_h__
#define __GroupService_h__

class CGroupService : public CBasicService
{
public:
	CGroupService();
	~CGroupService();
	
protected:
	virtual HRESULT MakeParam();
	virtual HRESULT ExcuteParam();
	virtual HRESULT MakeResult();	
	
	
private:
	
		
}

#endif __GroupService_h__