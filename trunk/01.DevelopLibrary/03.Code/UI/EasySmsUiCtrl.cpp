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

HRESULT		CEasySmsUiCtrl::MakeNode( CXmlStream	*pCXmlStream, wchar_t *pNodePath, NodeAttribute_t *pNodeAttribute_t, long lCnt, void *pValue, EnType _type )
{
	CXmlNode	*	pCXmlNode	=	NULL;
	
	HRESULT	hr	=	pCXmlStream->SelectNode( pNodePath, &pCXmlNode );
	if ( FAILED( hr ) )												return	E_FAIL;

	auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );
	
	switch ( _type )
	{
		case EN_NONE:
			hr	=	CXmlNodePtr->SetNodeContent( NULL, (wchar_t*)NULL, pNodeAttribute_t, lCnt );
			if ( FAILED( hr ) )												return	E_FAIL;
			break;

		case EN_DOUBLE:
			hr	=	CXmlNodePtr->SetNodeContent( NULL, *((double*)pValue), pNodeAttribute_t, lCnt );
			if ( FAILED( hr ) )												return	E_FAIL;
			break;

		case EN_LONG:
			hr	=	CXmlNodePtr->SetNodeContent( NULL, *((long*)pValue), pNodeAttribute_t, lCnt );
			if ( FAILED( hr ) )												return	E_FAIL;
			break;

		case EN_WCHAR:
			hr	=	CXmlNodePtr->SetNodeContent( NULL, (wchar_t*)pValue, pNodeAttribute_t, lCnt );
			if ( FAILED( hr ) )												return	E_FAIL;
			break;

		default:
			_ASSERT ( 0 );
			break;
	}


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

		if ( NULL == pCXmlNode || 0 == lCnt )
		{
			hr	=	E_ACCESSDENIED;
			break;
		}

		auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

		for ( int i = 0; i < lCnt; ++i )
		{

			hr	=	AppendUnReadList ( CXmlNodePtr.get(), clCEasySmsListBase );
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
	if ( NULL == pCXmlStream )
	{
		return	E_FAIL;
	}

	lCnt	=	0;
	
	CXmlNode	*	pCXmlNode	=	NULL;
	pCXmlStream->SelectNode( L"result/data/data/", &pCXmlNode );

	auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

	NodeAttribute_t		*pAttributes	=	NULL;
	long				lAttrCnt		=	0;

	HRESULT	hr	=	CXmlNodePtr->GetNodeContent( NULL, NULL, &pAttributes, &lAttrCnt );
	if ( FAILED( hr ) )												return	E_FAIL;

	for ( int i = 0; i < lAttrCnt; ++i )
	{
		if ( 0 == wcsncmp( pAttributes[i].wcsName, L"count", wcslen( L"count" ) ) )
		{
			lCnt	=	_wtol( pAttributes[i].wcsValue );
			break;
		}
	}
	
	return	S_OK;
}

HRESULT		CEasySmsUiCtrl::AppendUnReadList( CXmlNode *pCXmlNode, CEasySmsListBase &clCEasySmsListBase )
{
	ListItemEx*		item		=	new		ListItemEx;
	if ( NULL == item )									return	E_FAIL;	
	wchar_t	*		pValue		=	NULL;
	HRESULT			hr			=	S_OK;

	/*OtherInfo*/
	stItemData	*pstItemData	=	new	stItemData;
	if ( NULL == pstItemData )
	{
		delete item;									return	E_FAIL;
	}
	memset( pstItemData, 0x0, sizeof( stItemData ) );

	hr	=	GetOtherInfo( pCXmlNode, pstItemData );
	if ( FAILED ( hr ) )
	{
		delete item, pstItemData;						return	E_FAIL;
	}

	item->m_pData	=	( void* )( pstItemData );

	/*end*/
	
// 	if ( !( pstItemData->bIsLock ) )
//  	{
		//Insert MessageInfo
		pValue	=	NULL;
		hr	=	pCXmlNode->GetNodeContent( L"content/", &pValue, NULL, NULL );
		if ( FAILED ( hr ) )
		{
			delete item;									return	E_FAIL;
		}
		item->m_textDescription	=	pValue;

		if ( NULL != pValue )								delete	pValue, pValue	=	NULL;
// 	}
// 	else
// 	{	
// 		item->m_textDescription	=	L"该信息被加密***";
// 	}


	//Time
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"time/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}
	if ( NULL != pValue && *pValue != L'\0' )
	{
		wchar_t*	stopstring	=	NULL;

		double  vtime	=	wcstod( pValue, &stopstring );
		SYSTEMTIME  stSystemTime;
		if ( VariantTimeToSystemTime( vtime, &stSystemTime) )
		{
			CComBSTR bstrTime;
			wchar_t buffer[20]	=	L"";
			_ltow( stSystemTime.wYear, buffer, 10 );
			bstrTime.Append( buffer );
			bstrTime.Append(L"/");

			memset( buffer, 0x0, sizeof( buffer ) );
			_ltow( stSystemTime.wMonth, buffer, 10 );
			bstrTime.Append( buffer );
			bstrTime.Append(L"/");
			
			memset( buffer, 0x0, sizeof( buffer ) );
			_ltow( stSystemTime.wDay, buffer, 10 );
			bstrTime.Append( buffer );
			bstrTime.Append(L" ");	

			memset( buffer, 0x0, sizeof( buffer ) );
			_ltow( stSystemTime.wHour, buffer, 10 );
			bstrTime.Append( buffer );
			bstrTime.Append(L":");

			memset( buffer, 0x0, sizeof( buffer ) );
			_ltow( stSystemTime.wMinute, buffer, 10 );
			bstrTime.Append( buffer );

			item->m_textPostscript1	=	bstrTime.m_str;

		}
		

	}
	
	//Set message type
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"type/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}

	if ( L'1' == *pValue )
	{
		item->m_itemBgType	=	UILIST_ITEM_BGTYPE_YELLOW;
	}
	else if ( L'0' == *pValue )
	{
		item->m_itemBgType	=	UILIST_ITEM_BGTYPE_NONE ;
	}
	else
	{
		_ASSERT( 0 );
	}

	/*Name*/
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"name/", &pValue, NULL, NULL );
	if ( NULL == pValue )
	{
		//Insert Tel
		hr	=	pCXmlNode->GetNodeContent( L"address/", &pValue, NULL, NULL );
		if ( FAILED ( hr ) )
		{
			delete item;								return	E_FAIL;
		}

	}

	item->m_textTitle		=	pValue;
	if ( NULL != pValue )								delete	pValue, pValue	=	NULL;

	item->m_pImgFirst = m_imgContainer.LoadImage(MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true);
	clCEasySmsListBase.AddItem( item );

	pCXmlNode->MoveNext();

	return	S_OK;
}

HRESULT		CEasySmsUiCtrl::GetOtherInfo( IN CXmlNode *pCXmlNode, OUT stItemData	*pstItemData )
{
	//sid
	wchar_t * pValue	=	NULL;

	HRESULT	hr	=	pCXmlNode->GetNodeContent( L"sid/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->lSid	=	_wtol( pValue );
		delete	pValue, pValue	=	NULL;
	}
	
	//pid
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"pid/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->lPid	=	_wtol( pValue );
		delete	pValue, pValue	=	NULL;
	}
	//lockstatus
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"lockstatus/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->bIsLock	=	( bool )( _wtol( pValue ) );
		delete	pValue, pValue	=	NULL;
	}
	
	//readstatus
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"readstatus/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->bIsRead	=	( bool )( _wtol( pValue ) );
		delete	pValue, pValue	=	NULL;
	}

	//count
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"smscount/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->lCnt		=	_wtol( pValue );
		delete	pValue, pValue	=	NULL;
	}
	//firstletter
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"firstletter/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->cFirst		=	*pValue;
		delete	pValue, pValue	=	NULL;
	}

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

		if ( NULL == pCXmlNode || 0 == lCnt )
		{
			hr	=	E_ACCESSDENIED;
			break;
		}

		auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

		for ( int i = 0; i < lCnt; ++i )
		{

			hr	=	AppendLookCtorList ( CXmlNodePtr.get(), clCEasySmsListBase );
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

HRESULT		CEasySmsUiCtrl::AppendLookCtorList( CXmlNode *pCXmlNode, CEasySmsListBase &clCEasySmsListBase )
{
	ListItemEx*		item		=	new		ListItemEx;
	if ( NULL == item )									return	E_FAIL;
	
	/*OtherInfo*/
	stItemData	*pstItemData	=	new	stItemData;
	if ( NULL == pstItemData )
	{
		delete item;									return	E_FAIL;
	}
	memset( pstItemData, 0x0, sizeof( stItemData ) );

	HRESULT	hr	=	GetOtherInfo( pCXmlNode, pstItemData );
	if ( FAILED ( hr ) )
	{
		delete item, pstItemData;						return	E_FAIL;
	}

	item->m_pData	=	( void* )( pstItemData );

	//end

	wchar_t	*		pValue		=	NULL;
	
	//Insert MessageInfo
	hr	=	pCXmlNode->GetNodeContent ( L"name/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}

	item->m_textTitle	=	pValue;
	if ( NULL != pValue )
	{
		delete	pValue,	pValue	=	NULL;
	}

	//Insert MessageInfo
	hr	=	pCXmlNode->GetNodeContent ( L"defaultno/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}

	item->m_textDescription	=	pValue;
	if ( NULL != pValue )
	{
		delete	pValue,	pValue	=	NULL;
	}

	hr	=	pCXmlNode->GetNodeContent ( L"smscount/", &pValue, NULL, NULL );
	if ( FAILED ( hr ) )
	{
		delete item;									return	E_FAIL;
	}

	CComBSTR	bstrCount ( L"Count : " );
	bstrCount.Append( pValue );

	item->m_textPostscript1		=	bstrCount.m_str;
	item->m_itemBgType	=	UILIST_ITEM_BGTYPE_YELLOW;

	item->m_pImgFirst	=	m_imgContainer.LoadImage ( MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true );
	clCEasySmsListBase.AddItem( item );

	EN_MOVE	EnFlg		=	pCXmlNode->MoveNext();

	return	S_OK;
}

HRESULT		CEasySmsUiCtrl::MakeSendSmsInfo	( wchar_t **ppBuf, long *lSize, wchar_t *pwcSmsInfo, wchar_t* pwcsNumber )
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
		wcscpy( stNodeAttr.wcsValue, L"add" );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		long	sid	=	123;
		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/sid/", NULL, 0, (void*)&sid, EN_LONG );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		long	type	=	1;
		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/type/", NULL, 0, (void*)&type, EN_LONG );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/content/", NULL, 0, pwcSmsInfo, EN_WCHAR );
		if ( FAILED( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/address/", NULL, 0, pwcsNumber, EN_WCHAR );
		if ( FAILED( hr ) )													break;

		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED( hr ) )													break;

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}