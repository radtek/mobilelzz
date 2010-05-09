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
// 	<data type = "contactors">
//	 	<operation type = "list">
//	 	</operation>
//	<data type>
// </request>

HRESULT		CEasySmsUiCtrl::MakeCtorRltListReq (  wchar_t **ppBuf, long *lSize )
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

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		wcscpy( stNodeAttr.wcsName, L"type" );
		wcscpy( stNodeAttr.wcsValue, L"contactors" );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		wcscpy( stNodeAttr.wcsName , L"type" );
		wcscpy( stNodeAttr.wcsValue, L"list" );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////

		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED( hr ) )													break;

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
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

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		wcscpy( stNodeAttr.wcsName , L"type" );
		wcscpy( stNodeAttr.wcsValue, L"list" );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		wcscpy( stNodeAttr.wcsName , L"name" );
		wcscpy( stNodeAttr.wcsValue, L"unread" );
		hr	=	MakeNode( pCXmlStream, L"request/data/operation/condition/", &stNodeAttr, 1 );
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
	
	HRESULT	hr	=	pCXmlStream->SelectNode( pNodePath, &pCXmlNode );
	if ( FAILED( hr ) )												return	E_FAIL;

	auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

	hr	=	CXmlNodePtr->SetNodeContent( NULL, (wchar_t*)NULL, pNodeAttribute_t, lCnt );
	if ( FAILED( hr ) )												return	E_FAIL;

	return	S_OK;
}

HRESULT		CEasySmsUiCtrl::MakeUnReadListRlt ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream )
{
	if ( NULL == pwcRltStream )
	{
		return	E_FAIL;
	}
	
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

		hr	=	pCXmlStream->Load( pwcRltStream, wcslen( pwcRltStream ) );
		if ( FAILED( hr ) )													break;

		long	lCnt	=	0;
		hr	=	GetListCnt( pCXmlStream, lCnt );
		if ( FAILED( hr ) )													break;


		CXmlNode	*	pCXmlNode	=	NULL;
		hr	=	pCXmlStream->SelectNode( L"result/data/data/rec/", &pCXmlNode );
		if ( FAILED( hr ) )													break;

		auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

		for ( int i = 0; i < lCnt; ++i )
		{

			hr	=	AppendList ( CXmlNodePtr.get(), clCEasySmsListBase );
			if ( FAILED ( hr ) )
			{
				break;
			}

		}


	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}
	
	return	hr;
}

HRESULT		CEasySmsUiCtrl::GetListCnt( CXmlStream	*pCXmlStream, long	&lCnt )
{
	CXmlNode	*	pCXmlNode	=	NULL;
	pCXmlStream->SelectNode( L"result/data/data/", &pCXmlNode );

	auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

	NodeAttribute_t		*pAttributes	=	NULL;

	HRESULT	hr	=	CXmlNodePtr->GetNodeContent( NULL, NULL, &pAttributes, &lCnt );
	if ( FAILED( hr ) )												return	E_FAIL;
	
	return	S_OK;
}

HRESULT		CEasySmsUiCtrl::AppendList( CXmlNode *pCXmlNode, CEasySmsListBase &clCEasySmsListBase )
{
	ListItemEx*		item		=	new		ListItemEx;
	if ( NULL == item )									return	E_FAIL;
	wchar_t DetailInfo[512]		=	L"";	
	wchar_t	*		pValue		=	DetailInfo;
	HRESULT			hr			=	S_OK;

	//Insert MessageInfo
	hr	=	pCXmlNode->GetNodeContent( L"content/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}
	item->m_textDescription	=	pValue;

	//Insert Tel
	memset( DetailInfo, 0x0, sizeof( DetailInfo ) );
	hr	=	pCXmlNode->GetNodeContent( L"address/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}
	item->m_textTitle		=	pValue;

	//Set message type
	memset( DetailInfo, 0x0, sizeof( DetailInfo ) );
	hr	=	pCXmlNode->GetNodeContent( L"type/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}

	if ( L'1' == DetailInfo[0] )
	{
		item->m_itemBgType	=	UILIST_ITEM_BGTYPE_YELLOW;
	}
	else if ( L'0' == DetailInfo[0] )
	{
		item->m_itemBgType	=	UILIST_ITEM_BGTYPE_NONE ;
	}
	else
	{
		_ASSERT( 0 );
	}


	item->m_pImgFirst = m_imgContainer.LoadImage(MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true);
	clCEasySmsListBase.AddItem( item );

	pCXmlNode->MoveNext();

	return	S_OK;
}

HRESULT		CEasySmsUiCtrl::MakeCtorRltList    ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream )
{
	if ( NULL == pwcRltStream )
	{
		return	E_FAIL;
	}

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

		hr	=	pCXmlStream->Load( pwcRltStream, wcslen( pwcRltStream ) );
		if ( FAILED( hr ) )													break;

		long	lCnt	=	0;
		hr	=	GetListCnt( pCXmlStream, lCnt );
		if ( FAILED( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;
		hr	=	pCXmlStream->SelectNode( L"result/data/data/rec/", &pCXmlNode );
		if ( FAILED( hr ) )													break;

	} while ( FALSE );
}