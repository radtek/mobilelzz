// XMLOperator.h: interface for the CXMLOperator class.
//
//////////////////////////////////////////////////////////////////////
#ifndef __XMLStream_h__
#define __XMLStream_h__

#include "tinyxml.h"
#include <string>
#include <vector>
using namespace std;

#define		DefaultBufSize		( 1024*4 )
#define		CHAR_MAX_LENGTH			( 50 )

struct NodeAttribute_t
{
	wchar_t	wcsName[CHAR_MAX_LENGTH];
	wchar_t	wcsValue[CHAR_MAX_LENGTH];
};

enum	EN_MOVE
{
	MOVE_END	=	0,
	MOVE_OK
};

class	CXmlStream;

class CXmlNode
{
	public:

		CXmlNode();

		virtual ~CXmlNode();
		
		friend	class	CXmlStream;
	
	public:
		
		// client needs to release the memory
		// pwcsNodePath从当前节点开始计算，最后一个标签后面也要添加'/'
		HRESULT GetNodeContent( wchar_t* pwcsNodePath, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount );
		
		HRESULT SetNodeContent( wchar_t* pwcsNodePath, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount );
		
		//Move到它的兄弟节点
		EN_MOVE	MoveNext();
		
		//将一个节点追加到已存在的节点上,追加后，pCXmlNode可以被释放
		HRESULT	AppendNode( CXmlNode* pCXmlNode );

	private:
		HRESULT SetNodePtr( TiXmlElement* pNode,  CXmlStream* pCXmlStream );

		TiXmlElement*	GetElement();

		HRESULT	SubGetNodeContent( TiXmlElement* pNode, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount );

	private:
		//node ptr of tiny lib 
		TiXmlElement*			m_CurElement;

		CXmlStream	*			m_pCXmlStream;
}; 

class CXmlStream  
{
	public:

		CXmlStream();

		virtual ~CXmlStream();

		friend class CXmlNode;

	public:
		//accept buf external, but internal also needs alloc and copy the memory
		//解析XML文件，需要调用Load，生成XML文件不需要调用Load
		HRESULT Load( wchar_t* pwcsXmlBuf, long lSize );
		
		//alloc xml buf,and set the header content
		HRESULT Initialize( long lSize = DefaultBufSize );
		
		//if the nodepath dosen't exist,then create it first
		//Path 使用'/'进行分割,最后一个标签后面也要添加'/',比如: A/B/C/
		//传入的路径在XML中应该是唯一的，如果不唯一，则只会按照XML文件中的顺序，
		//得到第一个满足条件的Node
		//在创建XML文件时，该方法根据传入的Path，创建指定的Node
		//外部使用完pclXmlNode之后，需要释放
		HRESULT SelectNode( wchar_t* pwcsNodePath, CXmlNode** pclXmlNode );

	private:
	
		HRESULT	SubSelectNode( char *pcsNodePath, CXmlNode** pclXmlNode );

		HRESULT	MakeXml( char *pcsNodePath, CXmlNode** pclXmlNode );

		HRESULT	ParseXml( char *pcsNodePath, CXmlNode** pclXmlNode );

		TiXmlDocument*	GetDocument();

		HRESULT	MakeXmlFirst( char *pcsNodePath, CXmlNode** pclXmlNode );

		HRESULT	SubMakeXml( vector<TiXmlElement*> & vecElement);	

		enum	EnOperatorType
		{
			EnLoadXml	=	0,
			EnCreateXml
		};
		
	private:
		//memory
		
//		char				*	m_pBuf;
		string					m_strBuf;
//		long					m_bufSize;

		//tiny xml object
		TiXmlDocument		*	m_pTiXmlDocument;

		EnOperatorType			m_EnType;

		BOOL					bIsFirst;
		
};

#endif // __XMLStream_h__
