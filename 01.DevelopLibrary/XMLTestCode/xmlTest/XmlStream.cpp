#include "stdafx.h"
#include "XmlStream.h"

//class

class CFindChar
{
public:
	
	CFindChar( char* strInput ) :Pos( -1 )
	{
		strValue	=	strInput ;
		memset( pBuf, 0x0, sizeof(pBuf) );
		memcpy( pSave, strInput, min(CHAR_MAX_LENGTH, strlen( strInput) + 1 ) );
		Pointer	=	pSave;
	}
	
	char* GetNextChar( )
	{
		memset( pBuf, 0x0, sizeof(pBuf) );
		Pos		=	strValue.find( '//', Pos + 1 );
		if ( -1	== Pos )
		{
			return	NULL;
		}
		
		pSave[Pos]	=	'\0';
		memcpy( pBuf, Pointer, min(CHAR_MAX_LENGTH, strlen( Pointer) + 1 ) );
		
		Pointer	=	pSave + Pos + 1;
		
		return	pBuf;
	}
protected:
private:
	string			strValue;
	char			pBuf[CHAR_MAX_LENGTH];
	char			pSave[CHAR_MAX_LENGTH];
	char			*Pointer;
	long			Pos;
	
	
};

//Function

int		MB2WC( wchar_t* _pwc,  const char* _pch )
{
	int			buf_ln	=	MultiByteToWideChar(CP_ACP, 0, _pch, -1, _pwc, 0);
	MultiByteToWideChar(CP_ACP, 0, _pch, -1, _pwc, buf_ln);
	
	return	buf_ln;
}

int		WC2MB( char* _pch,  wchar_t* _pwc )
{
	int			buf_ln	=	WideCharToMultiByte(CP_ACP, 0, _pwc, -1, _pch, 0,		0, 0);
	WideCharToMultiByte(CP_ACP, 0, _pwc, -1, _pch, buf_ln,	0, 0);
	
	return	buf_ln;
}

//class CXmlNode

CXmlNode::CXmlNode()
: m_CurElement( NULL ), m_pCXmlStream( NULL )
{

}

CXmlNode::~CXmlNode()
{
	
}

//public:

EN_MOVE	CXmlNode::MoveNext()
{
	if ( NULL == m_CurElement )
	{
		_ASSERT(0);
		return	MOVE_END;
	}
	m_CurElement	=	(TiXmlElement*)(m_CurElement->NextSibling());
	if ( NULL == m_CurElement )
	{
		return	MOVE_END;
	}

	return	MOVE_OK;
}

HRESULT CXmlNode::GetNodeContent( wchar_t* pwcsNodePath, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount )
{
	HRESULT	hr			=	S_OK;

	BOOL	bIsFind		=	TRUE;
	*lAttributesCount	=	0;
	//从当前位置取得信息
	if ( NULL == pwcsNodePath )
	{
		hr	=	SubGetNodeContent( m_CurElement, ppwcsNodeValue, ppAttributes, lAttributesCount );
		if (FAILED(hr))
		{
			hr	=	E_FAIL;
		}

		return	hr;
	}
	//从pwcsNodePath指定的位置取得信息
	if ( NULL == m_CurElement )
	{
		return	E_FAIL;
	}

	do 
	{
		long	lSize	=	wcslen( pwcsNodePath) + 1;
		char	*pTemp	=	new	char [ lSize ];
		if ( NULL == *pTemp )
		{
			hr	=	E_FAIL;		
			break;
		}
		memset( pTemp, 0x0, lSize * sizeof( char ) );
		
		WC2MB( pTemp, pwcsNodePath );
		
		CFindChar	clCFindChar( pTemp );

		TiXmlElement*	pPreNode	=	NULL;
		TiXmlElement*	pCurNode	=	NULL;
		//fisrt time

		char	*pBuf		=	clCFindChar.GetNextChar();
		if ( NULL == pBuf )
		{
			hr	=	E_FAIL;
			break;
		}
		
		pPreNode = m_CurElement->FirstChildElement( pBuf );
		if ( NULL == pPreNode )
		{
			hr	=	E_FAIL;
			break;
		}

		//loop
		while ( TRUE )
		{
			pBuf		=	clCFindChar.GetNextChar();
			if ( NULL == pBuf )
			{
				break;
			}
			if ( NULL == pPreNode )
			{
				bIsFind	=	FALSE;
				break;
			}
			
			pCurNode	=	pPreNode->FirstChildElement( pBuf );
			
			pPreNode	=	pCurNode;
			pCurNode	=	NULL;
		}
		delete	[] pTemp;

		if (  !bIsFind )
		{
			hr	=	E_FAIL;
			break;
		}
		else
		{
			hr	=	SubGetNodeContent( pPreNode, ppwcsNodeValue, ppAttributes, lAttributesCount );
			if (FAILED(hr))
			{
				hr	=	E_FAIL;
				break;
			}
		}

	} while ( FALSE );

	return	hr;
}

HRESULT CXmlNode::SetNodeContent( wchar_t* pwcsNodePath, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount )
{
	return	S_OK;
}

HRESULT	CXmlNode::AppendNode( CXmlNode* pCXmlNode )
{
	m_CurElement->InsertEndChild( *( pCXmlNode->GetElement() ) );

	return	S_OK;
}

//private:

HRESULT	CXmlNode::SubGetNodeContent( TiXmlElement* pNode, wchar_t** ppwcsNodeValue, NodeAttribute_t** ppAttributes, long* lAttributesCount )
{
	const char *pText	=	pNode->GetText();
	if ( NULL == pText )
	{
		*ppwcsNodeValue	=	NULL;
	}
	else
	{
		MB2WC( *ppwcsNodeValue, pText );
	}
	TiXmlAttribute*	pAttr	=	pNode->FirstAttribute();
	
	while ( NULL != pAttr )
	{
		pAttr	=	pAttr->Next();
		++(*lAttributesCount);
	}
	
	if ( 0 < (*lAttributesCount) )
	{
		*ppAttributes	=	new	NodeAttribute_t[ *lAttributesCount ];
		pAttr			=	pNode->FirstAttribute();
		
		for (int i = 0; i < *lAttributesCount; ++i )
		{
			MB2WC( (*ppAttributes)[i].wcsName , pAttr->Name()  );
			MB2WC( (*ppAttributes)[i].wcsValue, pAttr->Value() );
			pAttr	=	pAttr->Next();
		}
	}

	return	S_OK;
}

HRESULT CXmlNode::SetNodePtr( TiXmlElement* pNode, CXmlStream* pCXmlStream )
{
	if ( NULL == pNode || NULL == pCXmlStream )
	{
		return	E_FAIL;
	}
	
	m_pCXmlStream	=	pCXmlStream;
	m_CurElement	=	pNode;
	
	return	S_OK;
}

TiXmlElement*	CXmlNode::GetElement()
{
	if ( NULL == m_CurElement )
	{
		_ASSERT(0);
	}
	
	return	m_CurElement;
}

//class CXmlStream 

CXmlStream::CXmlStream()
:m_pTiXmlDocument( NULL ), m_EnType( EnCreateXml )
{
	
}

CXmlStream::~CXmlStream()
{
	if ( NULL != m_pTiXmlDocument )
	{
		delete	m_pTiXmlDocument;
		m_pTiXmlDocument	=	NULL;
	}
}

//public:

HRESULT	CXmlStream::Initialize( long lSize )
{
	HRESULT	hr	=	S_OK;

	do 
	{
		if ( lSize <= 0 )
		{
			_ASSERT( 0 );
			hr	=	E_FAIL;
			break;
		}
		
		if ( NULL != m_pTiXmlDocument )
		{
			delete	m_pTiXmlDocument;
			m_pTiXmlDocument	=	NULL;
		}

		m_pTiXmlDocument	=	new	TiXmlDocument();
		if ( NULL == m_pTiXmlDocument )
		{
			hr	=	E_FAIL;
			break;
		}

	} while (FALSE);

	m_EnType	=	EnCreateXml;

	return	hr;
}

HRESULT CXmlStream::Load( wchar_t* pwcsXmlBuf, long lSize )
{
	if ( NULL == pwcsXmlBuf )	// need ReAlloc
	{
		return	E_FAIL;
	}
	HRESULT	hr	=	S_OK;
	
	do 
	{
		if ( NULL == m_pTiXmlDocument  )
		{
			hr	=	E_FAIL;
			break;
		}
		char	*pTemp	=	new	char[ lSize ];
		if ( NULL == pTemp )
		{
			hr	=	E_FAIL;
			break;
		}
		WC2MB( pTemp, pwcsXmlBuf);
		m_strBuf	=	pTemp;
		delete	[] pTemp;

		m_pTiXmlDocument->Parse( m_strBuf.c_str() );
		if ( m_pTiXmlDocument->Error() )
		{
			hr	=	E_FAIL;
			break;
		}

	} while (FALSE);

	m_EnType	=	EnLoadXml;

	return	hr;
}

HRESULT CXmlStream::SelectNode( wchar_t* pwcsNodePath, CXmlNode** pclXmlNode )
{
	if ( NULL == pwcsNodePath )
	{
		return	E_FAIL;
	}
	
	HRESULT	hr	=	S_OK;

	do 
	{
		long	lSize	=	wcslen( pwcsNodePath) + 1;
		char *pTemp	=	new	char [ lSize ];
		if ( NULL == *pTemp )
		{
			hr	=	E_FAIL, *pclXmlNode	=	NULL;		
			break;
		}
		memset( pTemp, 0x0, lSize * sizeof( char ) );

		WC2MB( pTemp, pwcsNodePath );

		hr	=	SubSelectNode( pTemp, pclXmlNode );
		delete	[] pTemp;
		if ( FAILED( hr ) )
		{
			*pclXmlNode	=	NULL;
			break;
		}

	} while ( FALSE );

	return	hr;
}

//private:

HRESULT	CXmlStream::SubSelectNode( char *pcsNodePath, CXmlNode** pclXmlNode )
{
	HRESULT	hr	=	S_OK;

	do 
	{
		if ( EnLoadXml	==	m_EnType )
		{
			hr	=	ParseXml( pcsNodePath, pclXmlNode );
		}
		else
		{
			hr	=	MakeXml( pcsNodePath, pclXmlNode );
		}

	} while ( FALSE );

	return	hr;
}

HRESULT	CXmlStream::MakeXml( char *pcsNodePath, CXmlNode** pclXmlNode )
{
	HRESULT	hr	=	S_OK;
	
	TiXmlDeclaration *pDeclaration = new TiXmlDeclaration( "1.0", "UTF-8", "" );
	if ( NULL == pDeclaration || NULL == m_pTiXmlDocument )
	{
		return	E_FAIL;
	}

	m_pTiXmlDocument->LinkEndChild(pDeclaration);
	if ( m_pTiXmlDocument->Error() )
	{
		return	E_FAIL;
	}

	do 
	{	
		CFindChar	clCFindChar( pcsNodePath );

		TiXmlElement *pCurElement	=	NULL;
		TiXmlElement *pPreElement	=	NULL;

		vector< TiXmlElement * >	vecElement;
		do 
		{
			char	*pTemp	=	clCFindChar.GetNextChar();
			if ( NULL == pTemp )
			{
				break;
			}
		
			TiXmlElement * pElement	=	new TiXmlElement( pTemp );
			if ( NULL == pElement )
			{
				hr	=	E_FAIL;
				break;
			}
			vecElement.push_back( pElement );

		} while ( TRUE );
		
//		m_pTiXmlDocument->LinkEndChild( pFirstElement );


	} while ( FALSE );

	if ( SUCCEEDED(hr) )
	{
//		m_strBuf.clear();
		m_pTiXmlDocument->SaveFile("./FileName.xml");

		m_strBuf << *m_pTiXmlDocument;

// 		*pclXmlNode	=	new	CXmlNode;
// 		if ( NULL == *pclXmlNode )
// 		{
// 			return	E_FAIL;
// 		}
// 		(*pclXmlNode)->SetNodePtr( pPreNode, this );

	}

	return	hr;
}

HRESULT	CXmlStream::ParseXml( char *pcsNodePath, CXmlNode** pclXmlNode )
{
	HRESULT	hr	=	S_OK;

	string strTemp	=	pcsNodePath;

	TiXmlElement*	pPreNode	=	NULL;
	TiXmlElement*	pCurNode	=	NULL;
	
	BOOL			bIsFind		=	TRUE;

	do 
	{	
		CFindChar	clCFindChar( pcsNodePath );
		//first time
		char	*pTemp		=	clCFindChar.GetNextChar();
		if ( NULL == pTemp )
		{
			break;
		}
		pPreNode = m_pTiXmlDocument->FirstChildElement( pTemp );

		if ( NULL == pPreNode )
		{
			hr	=	E_FAIL;
			break;
		}
				
		//loop
		while ( TRUE )
		{
			pTemp	=	clCFindChar.GetNextChar();
			if ( NULL == pTemp )
			{
				break;
			}
			if ( NULL == pPreNode )
			{
				bIsFind	=	FALSE;
				break;
			}			
			pCurNode	=	pPreNode->FirstChildElement( pTemp );

			pPreNode	=	pCurNode;
			pCurNode	=	NULL;
		}		

	} while ( FALSE );

	if ( FAILED(hr) || !bIsFind )
	{
		*pclXmlNode		=	NULL;
	}
	else
	{
		//create node
		do 
		{
			*pclXmlNode	=	new	CXmlNode;
			if ( NULL == *pclXmlNode )
			{
				hr	=	E_FAIL;
				break;
			}
			(*pclXmlNode)->SetNodePtr( pPreNode, this );

		} while ( FALSE );

	}
	
	return	hr;
}

TiXmlDocument*	CXmlStream::GetDocument()
{
	return	m_pTiXmlDocument;
}