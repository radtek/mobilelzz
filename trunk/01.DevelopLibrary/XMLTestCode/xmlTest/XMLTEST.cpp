// XMLTEST.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "XmlStream.h"


wchar_t* demoStart =	L"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						L"<Request>"
							L"<List>"
								L"<Rec>"
									L"<Name nameAttr=\"zhu.t1\" aaaaaa=\"XXXXX1\">first</Name>"
									L"<Tel>1234567</Tel>"
								L"</Rec>"
								L"<Rec>"
								L"<Name nameAttr=\"zhu.t2\" aaaaaa=\"XXXXX2\">Second</Name>"
								L"<Tel>7654321</Tel>"
								L"</Rec>"
							L"</List>"
						L"</Request>";

//#define PARSE_XML

int main(int argc, char* argv[])
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;
	if ( NULL == pCXmlStream )
	{
		_ASSERT(0);
		return -1;
	}
	
	HRESULT	hr	=	pCXmlStream->Initialize();
	if ( FAILED(hr))
	{
		_ASSERT(0);
	}

#ifdef	PARSE_XML

	hr	=	pCXmlStream->Load( demoStart, wcslen(demoStart) + 1 );
	if ( FAILED(hr))
	{
		_ASSERT(0);
	}

	CXmlNode	*	pCXmlNode	=	NULL;
	pCXmlStream->SelectNode( L"Request/List/Rec/", &pCXmlNode );
//	hr	=	pCXmlStream->SelectNode( L"Request/Lisdfst/Rec/", &pCXmlNode );
	if ( FAILED(hr))
	{
		_ASSERT(0);
	}

	wchar_t				Value[50]		=	L"";
	NodeAttribute_t		*Attribute		=	NULL;
	long				Count			=	0;
	//first
	wchar_t	*pValue	=	Value;
	if ( NULL != pCXmlNode )
	{
		hr	=	pCXmlNode->GetNodeContent( L"Name/", &pValue, &Attribute, &Count );
		if ( FAILED(hr))
		{
			_ASSERT(0);
		}
	}
	
	if ( NULL != Attribute )
	{
		delete [] Attribute;
		Attribute	=	NULL;
	}
	memset( pValue, 0x0, sizeof(pValue) );
	if ( NULL != pCXmlNode )
	{
		hr	=	pCXmlNode->GetNodeContent( L"Tel/", &pValue, &Attribute, &Count );
		if ( FAILED(hr))
		{
			_ASSERT(0);
		}
	}
	if ( NULL != Attribute )
	{
		delete [] Attribute;
		Attribute	=	NULL;
	}
	memset( pValue, 0x0, sizeof(pValue) );
	//move
	if ( NULL != pCXmlNode )
	{
		EN_MOVE	enRlt	=	pCXmlNode->MoveNext();
		if ( MOVE_END != enRlt )
		{
			//second
			if ( NULL != pCXmlNode )
			{
				hr	=	pCXmlNode->GetNodeContent( L"Name/", &pValue, &Attribute, &Count );
				if ( FAILED(hr))
				{
					_ASSERT(0);
				}
			}
			
			if ( NULL != Attribute )
			{
				delete [] Attribute;
				Attribute	=	NULL;
			}
			memset( pValue, 0x0, sizeof(pValue) );
			if ( NULL != pCXmlNode )
			{
				hr	=	pCXmlNode->GetNodeContent( L"Tel/", &pValue, &Attribute, &Count );
				if ( FAILED(hr))
				{
					_ASSERT(0);
				}
			}
			if ( NULL != Attribute )
			{
				delete [] Attribute;
				Attribute	=	NULL;
			}
			memset( pValue, 0x0, sizeof(pValue) );
		}
	}
	//end
	if ( NULL != pCXmlNode )
	{
		delete	pCXmlNode;
		pCXmlNode	=	NULL;

	}
	pCXmlStream->SelectNode( L"Request/List/Rec/Tel/", &pCXmlNode );
	hr	=	pCXmlNode->GetNodeContent( NULL, &pValue, &Attribute, &Count );
	delete	pCXmlStream;
#else
// 	CXmlNode	*	pCXmlNode	=	NULL;
// 	pCXmlStream->SelectNode( L"request/data/operation/condition/", &pCXmlNode );
// 	pCXmlStream->SelectNode( L"request/data/content/name/", &pCXmlNode );
// //	wchar_t	name[20]	=	L"MK";
// 	wchar_t	name[20]	=	L"";
// 	NodeAttribute_t  chAttr[2]	=	{ L"Type",L"Hum",  L"Skill", L"T" };
// 	
// 	pCXmlNode->SetNodeContent( NULL, name, chAttr, 2 );
// 	pCXmlStream->SelectNode( L"request/data/content/ring/", &pCXmlNode );
// 
// 	pCXmlNode->SetNodeContent( L"request/data/", L"100", chAttr, 2 );
// 	
// 	wchar_t	pwcBuf[ DefaultBufSize ]	=	L"";
// 	pCXmlStream->GetXmlStream( pwcBuf, DefaultBufSize );
/////
//	CXmlStream	*pCXmlStream1	=	new	CXmlStream;
	CXmlNode	*	pCXmlNode	=	NULL;
	pCXmlStream->SelectNode( L"result/data/", &pCXmlNode );
	NodeAttribute_t  chAttr[2]	=	{ L"Type",L"list",  L"count", L"2" };
	pCXmlNode->SetNodeContent( NULL, NULL, chAttr, 2 );

	for ( int i=0; i<5; ++i )
	{
		CXmlNode	rec( L"rec" );
		CXmlNode	id( L"id" );
		id.SetNodeContent( NULL, L"8888", NULL, 0 );
		CXmlNode	name1( L"name" );
		name1.SetNodeContent( NULL, L"zhu.t", NULL, 0 );
		rec.AppendNode( &id );
		rec.AppendNode( &name1 );
		pCXmlNode->AppendNode( &rec );
	}

 	wchar_t	pwcBuf[ DefaultBufSize ]	=	L"";
 	pCXmlStream->GetXmlStream( pwcBuf, DefaultBufSize );

#endif
	printf("Hello World!\n");
	return 0;
}

