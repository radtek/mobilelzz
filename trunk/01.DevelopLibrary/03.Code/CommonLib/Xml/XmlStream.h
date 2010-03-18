// XMLOperator.h: interface for the CXMLOperator class.
//
//////////////////////////////////////////////////////////////////////
#ifndef __XMLStream_h__
#define __XMLStream_h__
#define DefaultBufSize	1024*4

struct NodeAttribute_t
{
	wchar_t	wcsName[50];
	wchar_t	wcsValue[50];
};

class CXmlNode
{
public:
	CXmlNode();
	virtual ~CXmlNode();
	
public:
	HRESULT SetNodePtr();
	
	// client needs to release the memory
	HRESULT GetNodeContent(wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount);
	
	HRESULT SetNodeContent(wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount);
private:
	//node ptr of tiny lib 
} 

class CXmlStream  
{
	public:

		CXmlStream();

		virtual ~CXmlStream();

	public:
		//accept buf external, but internal also needs alloc and copy the memory
		HRESULT Load(wchar_t* pwcsXmlBuf, long lSize);
		
		//alloc xml buf,and set the header content
		HRESULT Initialize(long lSize = DefaultBufSize );
		
		//if the nodepath dosen't exist,then create it first
		HRESULT SelectNode(wchar_t* pwcsNodePath, CXmlNode* pclXmlNode);
		
	private:
		//memory
		
		//tiny xml object
		
};

#endif // __XMLStream_h__
