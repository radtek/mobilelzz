#include"stdafx.h"
#include "resource.h"
#include "EasySmsUiCtrl.h"

CEasySmsUiCtrl::CEasySmsUiCtrl( void )
{

}

CEasySmsUiCtrl::~CEasySmsUiCtrl( void )
{

}


// <request>
// 	<data type = "sms">
//	 	<operation type = "list">
//	 		<condition name = "unread"></condition>
//	 	</operation>
//	<data type>
// </request>


HRESULT		CEasySmsUiCtrl::MakeUnReadRltListReq( wchar_t **ppBuf, long *lSize )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	E_FAIL;
	}

	HRESULT	hr		=		S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		wcscpy( stNodeAttr.wcsName, L"type" );
		wcscpy( stNodeAttr.wcsValue, L"sms" );

		hr	=	MakeNode( pCXmlStream, L"result/data/", &stNodeAttr, 1 );

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		wcscpy( stNodeAttr.wcsName , L"type" );
		wcscpy( stNodeAttr.wcsValue, L"list" );

		hr	=	MakeNode( pCXmlStream, L"result/data/operation/", &stNodeAttr, 1 );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		wcscpy( stNodeAttr.wcsName , L"name" );
		wcscpy( stNodeAttr.wcsValue, L"unread" );
		hr	=	MakeNode( pCXmlStream, L"result/data/operation/condition/", &stNodeAttr, 1 );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED( hr ) )													break;

	} while ( false );


	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}
	
	return	hr;
}

HRESULT		CEasySmsUiCtrl::MakeNode( CXmlStream	*pCXmlStream, wchar_t *pNodePath, NodeAttribute_t *pNodeAttribute_t, long lCnt )
{
	CXmlNode	*	pCXmlNode	=	NULL;
	
	HRESULT	hr	=	pCXmlStream->SelectNode( L"result/data/", &pCXmlNode );
	if ( FAILED( hr ) )												return	E_FAIL;

	auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

	hr	=	CXmlNodePtr->SetNodeContent( NULL, (wchar_t*)NULL, pNodeAttribute_t, lCnt );
	if ( FAILED( hr ) )												return	E_FAIL;

	return	S_OK;
}