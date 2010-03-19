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
		// pwcsNodePath�ӵ�ǰ�ڵ㿪ʼ���㣬���һ����ǩ����ҲҪ���'/'
		HRESULT GetNodeContent( wchar_t* pwcsNodePath, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount );
		
		HRESULT SetNodeContent( wchar_t* pwcsNodePath, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount );
		
		//Move�������ֵܽڵ�
		EN_MOVE	MoveNext();
		
		//��һ���ڵ�׷�ӵ��Ѵ��ڵĽڵ���,׷�Ӻ�pCXmlNode���Ա��ͷ�
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
		//����XML�ļ�����Ҫ����Load������XML�ļ�����Ҫ����Load
		HRESULT Load( wchar_t* pwcsXmlBuf, long lSize );
		
		//alloc xml buf,and set the header content
		HRESULT Initialize( long lSize = DefaultBufSize );
		
		//if the nodepath dosen't exist,then create it first
		//Path ʹ��'/'���зָ�,���һ����ǩ����ҲҪ���'/',����: A/B/C/
		//�����·����XML��Ӧ����Ψһ�ģ������Ψһ����ֻ�ᰴ��XML�ļ��е�˳��
		//�õ���һ������������Node
		//�ڴ���XML�ļ�ʱ���÷������ݴ����Path������ָ����Node
		//�ⲿʹ����pclXmlNode֮����Ҫ�ͷ�
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
