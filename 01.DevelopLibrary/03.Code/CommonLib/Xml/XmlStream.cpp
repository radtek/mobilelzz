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
	char			pBuf [CHAR_MAX_LENGTH];
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

CXmlNode::CXmlNode( wchar_t *pwcNodeName )
: m_CurElement( NULL ), m_pCXmlStream( NULL )
{
	char	wcTemp[ CHAR_MAX_LENGTH ]	=	"";
	WC2MB( wcTemp, pwcNodeName );
	m_CurElement	=	new TiXmlElement( wcTemp );
	if ( NULL == m_CurElement )
	{
		_ASSERT(0);
	}
}

CXmlNode::~CXmlNode()
{
	if ( NULL == m_pCXmlStream && NULL != m_CurElement )
	{
		delete	m_CurElement;
		m_CurElement	=	NULL;
	}
}

//public:

EN_MOVE	CXmlNode::MoveNext()
{
	if ( NULL == m_CurElement )
	{
		_ASSERT(0);
		return	MOVE_END;
	}
	m_CurElement	=	/*(TiXmlElement*)*/(m_CurElement->NextSibling())->ToElement();
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
			_ASSERT(0);
			hr	=	E_FAIL;
		}

		return	hr;
	}
	//从pwcsNodePath指定的位置取得信息
	if ( NULL == m_CurElement )
	{
		_ASSERT(0);
		return	E_FAIL;
	}

	do 
	{
		long	lSize	=	wcslen( pwcsNodePath) + 1;
		char	*pTemp	=	new	char [ lSize ];
		if ( NULL == *pTemp )
		{
			_ASSERT(0);
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
			_ASSERT(0);
			hr	=	E_FAIL;
			break;
		}
		
		pPreNode = m_CurElement->FirstChildElement( pBuf );
		if ( NULL == pPreNode )
		{
			_ASSERT(0);
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
			_ASSERT(0);
			hr	=	E_FAIL;
			break;
		}
		else
		{
			hr	=	SubGetNodeContent( pPreNode, ppwcsNodeValue, ppAttributes, lAttributesCount );
			if (FAILED(hr))
			{
				_ASSERT(0);
				hr	=	E_FAIL;
				break;
			}
		}

	} while ( FALSE );

	return	hr;
}

HRESULT CXmlNode::SetNodeContent( wchar_t* pwcsNodePath, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount )
{
	if ( NULL == m_CurElement )
	{
		_ASSERT(0);
		return	E_FAIL;
	}
	HRESULT	hr	=	S_OK;
	if ( NULL == pwcsNodePath )
	{
		hr	=	SubSetNodeContent( m_CurElement, pwcsNodeValue, ppAttributes, lAttributesCount );
	}
	else
	{
		char	chPath[ CHAR_MAX_LENGTH ]	=	"";
		WC2MB( chPath, pwcsNodePath );

		if ( NULL == m_pCXmlStream )
		{
			_ASSERT(0);
			return	E_FAIL;
		}
		TiXmlElement*	pTiXmlElement;
		hr	=	m_pCXmlStream->FindNode( chPath, &pTiXmlElement );
		if ( FAILED(hr) )
		{
			_ASSERT(0);
			return	E_FAIL;
		}

		hr	=	SubSetNodeContent( pTiXmlElement, pwcsNodeValue, ppAttributes, lAttributesCount );
	}

#ifdef _DEBUG
	if ( NULL != m_pCXmlStream )
	{
		m_pCXmlStream->GetDocument()->SaveFile( "./FileName.xml" );
	}	
#endif

	return	hr;
}

HRESULT CXmlNode::SubSetNodeContent( TiXmlElement* pNode, wchar_t* pwcsNodeValue, NodeAttribute_t* ppAttributes, long lAttributesCount )
{
//	if ( NULL != pwcsNodeValue )
//	{
		char	chValue[ CHAR_MAX_LENGTH ]	=	"";
		WC2MB( chValue, pwcsNodeValue );
		
		TiXmlText text( chValue );
		
		for ( int i = 0; i < lAttributesCount; ++i )
		{
			char	name [ CHAR_MAX_LENGTH ]	=	"";
			char	value[ CHAR_MAX_LENGTH ]	=	"";
			
			WC2MB( name , ppAttributes[i].wcsName  );
			WC2MB( value, ppAttributes[i].wcsValue );
			pNode->SetAttribute( name, value );
		}
		
		pNode->InsertEndChild( text );
//	}
// 	else
// 	{
// 		_ASSERT(0);
// 		return	E_FAIL;
// 	}

	return	S_OK;
}

HRESULT	CXmlNode::AppendNode( CXmlNode* pCXmlNode )
{
	if ( NULL == m_CurElement )
	{
		_ASSERT(0);
		return	E_FAIL;
	}
	
	m_CurElement->InsertEndChild( *( pCXmlNode->GetElement() ) );

#ifdef _DEBUG
	if ( NULL != m_pCXmlStream )
	{
		m_pCXmlStream->GetDocument()->SaveFile( "./FileName.xml" );
	}	
#endif

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
		_ASSERT(0);
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
:m_pTiXmlDocument( NULL ), m_EnType( EnCreateXml ), bIsFirst( TRUE )
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

HRESULT	CXmlStream::GetXmlStream( wchar_t* pwcStream, long lSize )
{
	m_strBuf << *m_pTiXmlDocument;

	MB2WC( pwcStream, m_strBuf.c_str() );

	return	S_OK;
}

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
			_ASSERT(0);
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
		_ASSERT(0);
		return	E_FAIL;
	}
	HRESULT	hr	=	S_OK;
	
	do 
	{
		if ( NULL == m_pTiXmlDocument  )
		{
			_ASSERT(0);
			hr	=	E_FAIL;
			break;
		}
		char	*pTemp	=	new	char[ lSize ];
		if ( NULL == pTemp )
		{
			_ASSERT(0);
			hr	=	E_FAIL;
			break;
		}
		WC2MB( pTemp, pwcsXmlBuf);
		m_strBuf	=	pTemp;
		delete	[] pTemp;

		m_pTiXmlDocument->Parse( m_strBuf.c_str() );
		if ( m_pTiXmlDocument->Error() )
		{
			_ASSERT(0);
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
		_ASSERT(0);
		return	E_FAIL;
	}
	
	HRESULT	hr	=	S_OK;

	do 
	{
		long	lSize	=	wcslen( pwcsNodePath) + 1;
		char *pTemp	=	new	char [ lSize ];
		if ( NULL == *pTemp )
		{
			_ASSERT(0);
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

HRESULT	CXmlStream::MakeXmlFirst( char *pcsNodePath, CXmlNode** pclXmlNode )
{
	HRESULT	hr	=	S_OK;

	do 
	{
		CFindChar	clCFindChar( pcsNodePath );
		//first time
		char	*pTemp		=	clCFindChar.GetNextChar();
		if ( NULL == pTemp )
		{
			break;
		}
		TiXmlElement	*pNode	=	new TiXmlElement( pTemp );
		m_pTiXmlDocument->LinkEndChild( pNode );

		TiXmlElement	*pTempNode	=	NULL;
		do 
		{
			pTemp		=	clCFindChar.GetNextChar();
			if ( NULL == pTemp )
			{
				break;
			}

			pTempNode	=	new TiXmlElement( pTemp );
			pNode->LinkEndChild( pTempNode );
			pNode	=	pTempNode;
#ifdef _DEBUG
			if ( NULL != m_pTiXmlDocument )
			{
				m_pTiXmlDocument->SaveFile( "./FileName.xml" );
			}
#endif

		} while ( TRUE );

		if ( NULL != pNode )
		{
			* pclXmlNode	=	new	CXmlNode;
			if ( NULL == *pclXmlNode )
			{
				_ASSERT(0);
				return	E_FAIL;
			}
			(*pclXmlNode)->SetNodePtr( pNode, this );
		}

	} while ( FALSE );

	return	hr;
}

#if 0
HRESULT	CXmlStream::MakeXmlFirst( char *pcsNodePath, CXmlNode** pclXmlNode )
{
	HRESULT	hr	=	S_OK;
	
	vector< TiXmlElement * >	vecElement;

	do 
	{	
		CFindChar	clCFindChar( pcsNodePath );

		TiXmlElement *pCurElement	=	NULL;
		TiXmlElement *pPreElement	=	NULL;

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
				_ASSERT(0);
				hr	=	E_FAIL;
				break;
			}
			vecElement.push_back( pElement );

		} while ( TRUE );
		
		hr	=	SubMakeXml( vecElement );


	} while ( FALSE );

	if ( SUCCEEDED(hr) )
	{
#ifdef _DEBUG		
	if ( NULL != m_pCXmlStream )
	{
		m_pCXmlStream->GetDocument()->SaveFile( "./FileName.xml" );
	}
#endif
	}

	return	hr;
}

HRESULT	CXmlStream::SubMakeXml( vector<TiXmlElement*> & vecElement)
{
	vector< TiXmlElement * >::reverse_iterator ri, riend, save;
	riend	=	vecElement.rend();
	ri = save = vecElement.rbegin();
	++ri;
	for ( ri; ri != riend; ++ri, ++save )
	{
		(*ri)->InsertEndChild( *(*save) );
	}

	m_pTiXmlDocument->LinkEndChild( *(vecElement.begin()) );
	
	vector< TiXmlElement * >::iterator	iter	=	vecElement.begin();
	++iter;
	for ( ; iter != vecElement.end(); ++iter )
	{
		delete (*iter);
		(*iter)	=	NULL;
	}

	return	S_OK;
}

#endif

HRESULT	CXmlStream::MakeXml( char *pcsNodePath, CXmlNode** pclXmlNode )
{
	HRESULT	hr	=	S_OK;

	if ( bIsFirst )
	{
		bIsFirst	=	FALSE;

		TiXmlDeclaration *pDeclaration = new TiXmlDeclaration( "1.0", "UTF-8", "" );
		if ( NULL == pDeclaration || NULL == m_pTiXmlDocument )
		{
			_ASSERT(0);
			return	E_FAIL;
		}
		m_pTiXmlDocument->LinkEndChild(pDeclaration);

		hr	=	MakeXmlFirst( pcsNodePath, pclXmlNode );

		return	hr;
	}
	
	
	do 
	{
		CFindChar	clCFindChar( pcsNodePath );
		//first time
		char	*pTemp		=	clCFindChar.GetNextChar();
		if ( NULL == pTemp )
		{
			break;
		}
		TiXmlElement	*pNode = m_pTiXmlDocument->FirstChildElement( pTemp );
		if ( NULL == pNode )
		{
			pNode	=	new TiXmlElement( pTemp );
			m_pTiXmlDocument->LinkEndChild( pNode );
#ifdef _DEBUG
			if ( NULL != m_pTiXmlDocument )
			{
				m_pTiXmlDocument->SaveFile( "./FileName.xml" );
			}
#endif
		}
		
		TiXmlElement	*pTempNode	=	NULL;
		do 
		{
			pTemp		=	clCFindChar.GetNextChar();
			if ( NULL == pTemp )
			{
				break;
			}
			pTempNode	=	pNode->FirstChildElement( pTemp );
			if ( NULL == pTempNode )
			{
				pTempNode	=	new TiXmlElement( pTemp );
				pNode->LinkEndChild( pTempNode );
				pNode	=	pTempNode;
#ifdef _DEBUG
				if ( NULL != m_pTiXmlDocument )
				{
					m_pTiXmlDocument->SaveFile( "./FileName.xml" );
				}
#endif
			}
			else
			{
				pNode	=	pTempNode;
			}

		} while ( TRUE );

		if ( NULL != pNode )
		{
			* pclXmlNode	=	new	CXmlNode;
			if ( NULL == *pclXmlNode )
			{
				_ASSERT(0);
				return	E_FAIL;
			}
			(*pclXmlNode)->SetNodePtr( pNode, this );
		}

	} while ( FALSE );

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
			_ASSERT(0);
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
				_ASSERT(0);
				hr	=	E_FAIL;
				break;
			}
			(*pclXmlNode)->SetNodePtr( pPreNode, this );

		} while ( FALSE );

	}
	
	return	hr;
}

HRESULT	CXmlStream::FindNode( char *pcsNodePath, TiXmlElement** pclXmlElement )
{
	HRESULT	hr		=	S_OK;
	*pclXmlElement		=	NULL;

	do 
	{
		CFindChar	clCFindChar( pcsNodePath );
		//first time
		char	*pTemp		=	clCFindChar.GetNextChar();
		if ( NULL == pTemp )
		{
			break;
		}
		TiXmlElement	*pNode = m_pTiXmlDocument->FirstChildElement( pTemp );
		if ( NULL == pNode )
		{
			_ASSERT(0);
			hr	=	E_FAIL;
			break;
		}
		
		TiXmlElement	*pTempNode	=	NULL;
		do 
		{
			pTemp		=	clCFindChar.GetNextChar();
			if ( NULL == pTemp )
			{
				break;
			}
			pTempNode	=	pNode->FirstChildElement( pTemp );
			if ( NULL == pTempNode )
			{
				_ASSERT(0);
				hr	=	E_FAIL;
				break;
			}
			pNode	=	pTempNode;
			
		} while ( TRUE );
		
		if ( NULL != pTempNode )
		{
			*pclXmlElement	=	pTempNode;
		}
		
	} while ( FALSE );
	
	return	hr;
}	

TiXmlDocument*	CXmlStream::GetDocument()
{
	return	m_pTiXmlDocument;
}