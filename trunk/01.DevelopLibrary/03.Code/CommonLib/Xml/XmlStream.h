// XMLOperator.h: interface for the CXMLOperator class.
//
//////////////////////////////////////////////////////////////////////
#ifndef __XMLStream_h__
#define __XMLStream_h__

#define		DefaultBufSize		( 1024*4 )
#define		CHAR_MAX_LENGTH			( 50 )


struct NodeAttribute_t
{
	wchar_t	wcsName [CHAR_MAX_LENGTH];
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

		CXmlNode( wchar_t *pwcNodeName );

		virtual ~CXmlNode();
		
		friend	class	CXmlStream;
	
	public:
		
		// client needs to release the memory
		// pwcsNodePath从当前节点开始计算，最后一个标签后面也要添加'/'
		APP_Result GetNodeContent( wchar_t* pwcsNodePath, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount );
		
		//从根目录开始指定Path
		APP_Result SetNodeContent( wchar_t* pwcsNodePath, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount );
		
		//Move到它的兄弟节点
		EN_MOVE	MoveNext();
		
		//将一个节点追加到已存在的节点上,追加后，pCXmlNode可以被释放
		APP_Result	AppendNode( CXmlNode* pCXmlNode );

	private:

		CXmlNode();

		APP_Result SetNodePtr( TiXmlElement* pNode,  CXmlStream* pCXmlStream );

		TiXmlElement*	GetElement();

		APP_Result	SubGetNodeContent( TiXmlElement* pNode, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount );

		APP_Result SubSetNodeContent( TiXmlElement* pNode, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount );

		int		MB2WC( wchar_t* _pwc,  const char* _pch );
		int		WC2MB( char* _pch,  wchar_t* _pwc );	

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
		APP_Result Load( wchar_t* pwcsXmlBuf, long lSize );
		
		//alloc xml buf,and set the header content
		APP_Result Initialize( long lSize = DefaultBufSize );
		
		//if the nodepath dosen't exist,then create it first
		//Path 使用'/'进行分割,最后一个标签后面也要添加'/',比如: A/B/C/
		//传入的路径在XML中应该是唯一的，如果不唯一，则只会按照XML文件中的顺序，
		//得到第一个满足条件的Node
		//在创建XML文件时，该方法根据传入的Path，创建指定的Node
		//外部使用完pclXmlNode之后，需要释放
		APP_Result SelectNode( wchar_t* pwcsNodePath, CXmlNode** pclXmlNode );

		APP_Result	GetXmlStream( wchar_t* pwcStream, long lSize );

	private:
	
		APP_Result	SubSelectNode( char *pcsNodePath, CXmlNode** pclXmlNode );

		APP_Result	MakeXml( char *pcsNodePath, CXmlNode** pclXmlNode );

		APP_Result	ParseXml( char *pcsNodePath, CXmlNode** pclXmlNode );

		TiXmlDocument*	GetDocument();

		APP_Result	MakeXmlFirst( char *pcsNodePath, CXmlNode** pclXmlNode );

		APP_Result	FindNode( char *pcsNodePath, TiXmlElement** pclXmlElement );

		int		MB2WC( wchar_t* _pwc,  const char* _pch );
		int		WC2MB( char* _pch,  wchar_t* _pwc );	

//		APP_Result	SubMakeXml( vector<TiXmlElement*> & vecElement);	

		enum	EnOperatorType
		{
			EnLoadXml	=	0,
			EnCreateXml
		};
		
	private:
		//memory
	
		string				m_strBuf;

		//tiny xml object
		TiXmlDocument		*	m_pTiXmlDocument;

		EnOperatorType			m_EnType;

		BOOL					bIsFirst;
		
};

#endif // __XMLStream_h__
