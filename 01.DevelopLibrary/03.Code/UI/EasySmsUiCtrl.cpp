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
	return	m_clCCoreSmsUiCtrl.MakeCtorRltListReq( ppBuf, lSize );
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
	return	m_clCCoreSmsUiCtrl.MakeUnReadRltListReq( ppBuf, lSize );
}

HRESULT		CEasySmsUiCtrl::MakeUnReadListRlt ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream )
{
	if ( NULL == pwcRltStream )
	{
		return	E_FAIL;
	}
	long			lCnt				=	0;
	stCoreItemData	*pstCoreItemData	=	NULL;
	HRESULT	hr	=	m_clCCoreSmsUiCtrl.MakeListRlt( pwcRltStream, &pstCoreItemData, &lCnt );
	if ( FAILED ( hr ) )
	{
		return	E_FAIL;
	}

	for ( int i = 0; i < lCnt; ++i )
	{
		ListItemEx*		item		=	new		ListItemEx;
		if ( NULL == item )									return	E_FAIL;	
		item->m_textDescription	=	pstCoreItemData[i].bstrContent.m_str;
		item->m_textPostscript1	=	pstCoreItemData[i].bstrTime.m_str;

		if ( L'1' ==  pstCoreItemData[i].wcFromTo )
		{
			item->m_itemBgType	=	UILIST_ITEM_BGTYPE_YELLOW;
		}
		else if ( L'0' == pstCoreItemData[i].wcFromTo )
		{
			item->m_itemBgType	=	UILIST_ITEM_BGTYPE_NONE ;
		}
		else
		{
			_ASSERT( 0 );
		}
		/*Name*/
		if ( NULL == pstCoreItemData[i].bstrName.m_str )
		{
			if ( NULL != pstCoreItemData[i].bstrTelNo.m_str )
			{
				item->m_textTitle	=	pstCoreItemData[i].bstrTelNo.m_str;
			}
		}
		else
		{
				item->m_textTitle	=	pstCoreItemData[i].bstrName.m_str;
		}

		item->m_pData	=	( void* )( &(pstCoreItemData[i]) );

		item->m_pImgFirst = m_imgContainer.LoadImage(MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true);
		clCEasySmsListBase.AddItem( item );
	}

	
	return	hr;
}

HRESULT		CEasySmsUiCtrl::MakeCtorRltList    ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream )
{
	if ( NULL == pwcRltStream )
	{
		return	E_FAIL;
	}
	long			lCnt				=	0;
	stCoreItemData	*pstCoreItemData	=	NULL;
	HRESULT	hr	=	m_clCCoreSmsUiCtrl.MakeListRlt( pwcRltStream, &pstCoreItemData, &lCnt );
	if ( FAILED ( hr ) )
	{
		return	E_FAIL;
	}

	for ( int i = 0; i < lCnt; ++i )
	{
		ListItemEx*		item		=	new		ListItemEx;
		if ( NULL == item )									return	E_FAIL;	

		item->m_textTitle		=	pstCoreItemData[i].bstrName.m_str;
		item->m_textDescription	=	pstCoreItemData[i].bstrTelNo.m_str;

		CComBSTR	bstrCount ( L"Count : " );
		wchar_t buffer[20]	=	L"";
		_ltow( pstCoreItemData[i].lCnt, buffer, 10 );
		bstrCount.Append( buffer );
		item->m_textPostscript1		=	bstrCount.m_str;

		item->m_pData	=	( void* )( &(pstCoreItemData[i]) );

		item->m_itemBgType	=	UILIST_ITEM_BGTYPE_YELLOW;

		item->m_pImgFirst	=	m_imgContainer.LoadImage ( MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true );
		clCEasySmsListBase.AddItem( item );

	}


	return	hr;
}

HRESULT		CEasySmsUiCtrl::MakeSendSmsInfo	( wchar_t **ppBuf, long *lSize, wchar_t *pwcSmsInfo, wchar_t* pwcsNumber )
{
	return	m_clCCoreSmsUiCtrl.MakeSendSmsInfo( ppBuf, lSize, pwcSmsInfo, pwcsNumber );
}

HRESULT		CEasySmsUiCtrl::MakeMsgRltListReq( wchar_t **ppBuf, long *lSize, long lPid )
{
	return	m_clCCoreSmsUiCtrl.MakeMsgRltListReq( ppBuf, lSize, lPid );
}

HRESULT		CEasySmsUiCtrl::MakeMsgRltList   ( CEasySmsListBase &clCEasySmsListBase, wchar_t *pwcRltStream )
{
	if ( NULL == pwcRltStream )
	{
		return	E_FAIL;
	}
	long			lCnt				=	0;
	stCoreItemData	*pstCoreItemData	=	NULL;
	HRESULT	hr	=	m_clCCoreSmsUiCtrl.MakeListRlt( pwcRltStream, &pstCoreItemData, &lCnt );
	if ( FAILED ( hr ) )
	{
		return	E_FAIL;
	}

	for ( int i = 0; i < lCnt; ++i )
	{
		ListItemEx*		item		=	new		ListItemEx;
		if ( NULL == item )									return	E_FAIL;	

		item->m_textDescription	=	pstCoreItemData[i].bstrContent.m_str;
		item->m_textPostscript1	=	pstCoreItemData[i].bstrTime.m_str;
		if ( L'1' ==  pstCoreItemData[i].wcFromTo )
		{
			item->m_itemBgType	=	UILIST_ITEM_BGTYPE_YELLOW;
		}
		else if ( L'0' == pstCoreItemData[i].wcFromTo )
		{
			item->m_itemBgType	=	UILIST_ITEM_BGTYPE_NONE ;
		}
		else
		{
			_ASSERT( 0 );
		}

		/*Name*/
		if ( NULL == pstCoreItemData[i].bstrName.m_str )
		{
			if ( NULL != pstCoreItemData[i].bstrTelNo.m_str )
			{
				item->m_textTitle	=	pstCoreItemData[i].bstrTelNo.m_str;
			}
		}
		else
		{
			item->m_textTitle	=	pstCoreItemData[i].bstrName.m_str;
		}

		item->m_pData	=	( void* )( &(pstCoreItemData[i]) );

		item->m_pImgFirst = m_imgContainer.LoadImage(MzGetInstanceHandle(), IDR_PNG_CTR_LIST_READ, true);
		clCEasySmsListBase.AddItem( item );

	}


	return	hr;
}

HRESULT		CEasySmsUiCtrl::MakeDetailRltListReq( wchar_t **ppBuf, long *lSize, long lSid )
{
	return	m_clCCoreSmsUiCtrl.MakeDetailRltListReq( ppBuf, lSize, lSid );
}