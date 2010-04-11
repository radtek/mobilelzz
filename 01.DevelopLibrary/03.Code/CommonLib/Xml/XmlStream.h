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
		// pwcsNodePath�ӵ�ǰ�ڵ㿪ʼ���㣬���һ����ǩ����ҲҪ���'/'
		APP_Result GetNodeContent( wchar_t* pwcsNodePath, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount );
		
		//�Ӹ�Ŀ¼��ʼָ��Path
		APP_Result SetNodeContent( wchar_t* pwcsNodePath, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount );
		
		//Move�������ֵܽڵ�
		EN_MOVE	MoveNext();
		
		//��һ���ڵ�׷�ӵ��Ѵ��ڵĽڵ���,׷�Ӻ�pCXmlNode���Ա��ͷ�
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
		//����XML�ļ�����Ҫ����Load������XML�ļ�����Ҫ����Load
		APP_Result Load( wchar_t* pwcsXmlBuf, long lSize );
		
		//alloc xml buf,and set the header content
		APP_Result Initialize( long lSize = DefaultBufSize );
		
		//if the nodepath dosen't exist,then create it first
		//Path ʹ��'/'���зָ�,���һ����ǩ����ҲҪ���'/',����: A/B/C/
		//�����·����XML��Ӧ����Ψһ�ģ������Ψһ����ֻ�ᰴ��XML�ļ��е�˳��
		//�õ���һ������������Node
		//�ڴ���XML�ļ�ʱ���÷������ݴ����Path������ָ����Node
		//�ⲿʹ����pclXmlNode֮����Ҫ�ͷ�
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
