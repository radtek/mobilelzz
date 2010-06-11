#include "stdafx.h"

#include "Errors.h"
#include "FuncLib.h"
#include "DynamicArray.h"
#include "../Xml/TinySrc/tinyxml.h"
#include "XmlStream.h"
#include "ResultXmlOperator.h"

CCoreSmsUiCtrl::CCoreSmsUiCtrl( void )
{

}

CCoreSmsUiCtrl::~CCoreSmsUiCtrl( void )
{

}

APP_Result		CCoreSmsUiCtrl::MakeCtorRltListReq   ( UiCodeChar **ppBuf, long *lSize )
{
	CXmlStream	*pCXmlStream	=	new		CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App ( hr ) )												break;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		F_wcscpyn( stNodeAttr.wcsName, L"type" , CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"contactors", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type",  CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"list" , CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////

		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakeMsgRltListReq    ( UiCodeChar **ppBuf, long *lSize, long lPid )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"list", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"name", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"contactor", CHAR_MAX_LENGTH );
		hr	=	MakeNode( pCXmlStream, L"request/data/operation/condition/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"value", CHAR_MAX_LENGTH );
		wchar_t	buf[20]	=	L"";
		_ltow( lPid, buf, 10 );

		F_wcscpyn( stNodeAttr.wcsValue, buf, CHAR_MAX_LENGTH );
		hr	=	MakeNode( pCXmlStream, L"request/data/operation/condition/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;

	} while ( false );


	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakeDeleteSmsInfo	 ( OUT UiCodeChar **ppBuf, OUT long *lSize, IN long *plSid, IN long lCnt  )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"delete", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"kind", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sids", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;
		//////////////////////////////////////////////////////////////////////////
		for ( int i = 0; i < lCnt; ++i )
		{
			hr	=	MakeNode( pCXmlStream, L"request/data/operation/sid/", NULL, NULL, (void*)&(plSid[i]), EN_CORE_LONG );
			if ( FAILED_App( hr ) )												break;
		}

		//////////////////////////////////////////////////////////////////////////
		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;

	} while ( false );


	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakeSendSmsInfo		 ( UiCodeChar **ppBuf, long *lSize, UiCodeChar *pwcSmsInfo, UiCodeChar* pwcsNumber )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"add",  CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
// 		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
// 		long	sid	=	123;
// 		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/sid/", NULL, 0, (void*)&sid, EN_CORE_LONG );
// 		if ( FAILED_App( hr ) )													break;
// 
// 		//////////////////////////////////////////////////////////////////////////
// 		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
// 		long	type	=	1;
// 		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/type/", NULL, 0, (void*)&type, EN_CORE_LONG );
// 		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/content/", NULL, 0, pwcSmsInfo, EN_CORE_WCHAR );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		hr	=	MakeNode ( pCXmlStream, L"request/data/operation/address/", NULL, 0, pwcsNumber, EN_CORE_WCHAR );
		if ( FAILED_App( hr ) )													break;

		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}
//不锁定为0，锁定为1,可以不设置，默认为0
//未读为0，已读为1；如果Type为1，该值默认为1；如果Type为0，该值默认为0
APP_Result		CCoreSmsUiCtrl::MakeUpdateSmsStatusReq( UiCodeChar **ppBuf, long *lSize, long lSid, long lLock, long lRead )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"edit", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"kind", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/sms/", NULL, NULL );
		if ( FAILED_App( hr ) )													break;

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/sms/sid/", NULL, NULL, (void*)(&lSid), EN_CORE_LONG );
		if ( FAILED_App( hr ) )													break;

		if ( lRead >= 0 )
		{
			hr	=	MakeNode( pCXmlStream, L"request/data/operation/sms/readstatus/", NULL, NULL, (void*)(&lRead), EN_CORE_LONG );
			if ( FAILED_App( hr ) )													break;
		}

		if ( lLock >= 0 )
		{
			hr	=	MakeNode( pCXmlStream, L"request/data/operation/sms/lockstatus/", NULL, NULL, (void*)(&lLock), EN_CORE_LONG );
			if ( FAILED_App( hr ) )													break;
		}

		//////////////////////////////////////////////////////////////////////////
		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;


	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakePassWordStatusReq ( UiCodeChar **ppBuf, long *lSize, 
													   long lPid, UiCodeChar* pwcDataKind, 
													   UiCodeChar* pwcCode, UiCodeChar* pwcNewCode )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"edit", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"kind", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"protectdata", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"kind", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, pwcDataKind, CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/protectdata/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/protectdata/pid/", NULL, NULL, (void*)(&lPid), EN_CORE_LONG );
		if ( FAILED_App( hr ) )													break;
		
		if ( NULL != pwcCode )
		{
			hr	=	MakeNode( pCXmlStream, L"request/data/operation/protectdata/code/", NULL, NULL, (void*)(pwcCode), EN_CORE_WCHAR );
			if ( FAILED_App( hr ) )													break;
		}

		if ( NULL != pwcNewCode )
		{
			hr	=	MakeNode( pCXmlStream, L"request/data/operation/protectdata/newcode/", NULL, NULL, (void*)(pwcNewCode), EN_CORE_WCHAR );
			if ( FAILED_App( hr ) )													break;
		}

		//////////////////////////////////////////////////////////////////////////
		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakeDetailRltListReq ( UiCodeChar **ppBuf, long *lSize, long lSid )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );

		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"detail", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		hr	=	MakeNode( pCXmlStream, L"request/data/operation/sid/", NULL, 0, (void*)(&lSid), EN_CORE_LONG );
		if ( FAILED_App( hr ) )													break;

		//////////////////////////////////////////////////////////////////////////
		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;

	} while ( false );


	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakeUnReadRltListReq( wchar_t **ppBuf, long *lSize )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"list", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"name",  CHAR_MAX_LENGTH  );
		F_wcscpyn( stNodeAttr.wcsValue, L"unread", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/condition/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )													break;

	} while ( false );


	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakeDetailReq ( UiCodeChar **ppBuf, long *lSize, long lSid, UiCodeChar* pwcCode )
{
	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;

		NodeAttribute_t  stNodeAttr;
		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName, L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"sms", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )													break;

		memset( &stNodeAttr, 0x0, sizeof( NodeAttribute_t ) );
		F_wcscpyn( stNodeAttr.wcsName , L"type", CHAR_MAX_LENGTH );
		F_wcscpyn( stNodeAttr.wcsValue, L"detail", CHAR_MAX_LENGTH );

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/", &stNodeAttr, 1 );
		if ( FAILED_App( hr ) )														break;

		hr	=	MakeNode( pCXmlStream, L"request/data/operation/sid/", NULL, NULL, (void*)(&lSid), EN_CORE_LONG );
		if ( FAILED_App( hr ) )														break;

		if ( NULL != pwcCode )
		{
			hr	=	MakeNode( pCXmlStream, L"request/data/operation/decode/", NULL, NULL, (void*)(&pwcCode), EN_CORE_WCHAR );
			if ( FAILED_App( hr ) )													break;
		}

		hr	=	pCXmlStream->GetXmlStream( ppBuf, lSize );
		if ( FAILED_App( hr ) )														break;

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
	
}

APP_Result		CCoreSmsUiCtrl::MakeDetailRlt  ( UiCodeChar *pwcRltStream, wchar_t **ppwcDetail )
{
	if ( NULL == pwcRltStream || NULL == ppwcDetail )
	{
		return	APP_Result_E_Fail;
	}

	*ppwcDetail	=	NULL;

	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		hr	=	pCXmlStream->Load( pwcRltStream, wcslen( pwcRltStream ) );
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;
		hr	=	pCXmlStream->SelectNode( L"result/data/data/rec/content/", &pCXmlNode );
		if ( FAILED_App( hr ) )													break;

		auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );
		hr	=	CXmlNodePtr->GetNodeContent ( NULL, ppwcDetail, NULL, NULL );

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::MakeListRlt    ( UiCodeChar *pwcRltStream, stCoreItemData **ppclstItemData, long *plCnt )
{
	if ( NULL == pwcRltStream || NULL == ppclstItemData )
	{
		return	APP_Result_E_Fail;
	}

	*ppclstItemData	=	NULL, *plCnt	=	0;

	CXmlStream	*pCXmlStream	=	new	CXmlStream;

	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	APP_Result	hr		=		APP_Result_S_OK;

	do 
	{
		hr	=	pCXmlStream->Initialize();
		if ( FAILED_App( hr ) )													break;

		hr	=	pCXmlStream->Load( pwcRltStream, wcslen( pwcRltStream ) );
		if ( FAILED_App( hr ) )													break;

		long	lCnt	=	0;

		hr	=	GetListCnt( pCXmlStream, lCnt );
		if ( FAILED_App( hr ) )													break;

		CXmlNode	*	pCXmlNode	=	NULL;
		hr	=	pCXmlStream->SelectNode( L"result/data/data/rec/", &pCXmlNode );
		if ( FAILED_App( hr ) )													break;

		if ( NULL == pCXmlNode || 0 == lCnt )
		{
			hr	=	APP_Result_ProtectData_Error;
			break;
		}

		auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

		*ppclstItemData	=	new	stCoreItemData [ lCnt ];
		if ( *ppclstItemData == NULL )
		{
			hr	=	APP_Result_E_Fail;
		}

		for ( int i = 0; i < lCnt; ++i )
		{

			hr	=	AppendList ( CXmlNodePtr.get(), &((*ppclstItemData)[i]) );
			if ( FAILED_App ( hr ) )
			{
				break;
			}

		}

		*plCnt	=	lCnt;

	} while ( FALSE );

	if ( NULL != pCXmlStream )
	{
		delete	pCXmlStream;
	}

	return	hr;
}

APP_Result		CCoreSmsUiCtrl::AppendList( CXmlNode *pCXmlNode, stCoreItemData *pclstItemData )
{
	wchar_t	*		pValue		=	NULL;
	HRESULT			hr			=	S_OK;



	hr	=	GetInfo( pCXmlNode, pclstItemData );
	if ( FAILED ( hr ) )
	{
		return	APP_Result_E_Fail;
	}

	pCXmlNode->MoveNext();

	return	APP_Result_S_OK;
}

//////////////////////////////////////////////////////////////////////////

APP_Result		CCoreSmsUiCtrl::GetInfo( IN CXmlNode *pCXmlNode, OUT stCoreItemData	*pstItemData )
{
	//get Encode
	NodeAttribute_t* pAttributes	=	NULL;
	long			lCnt			=	0;
	HRESULT	hr	=	pCXmlNode->GetNodeContent ( NULL, NULL, &pAttributes, &lCnt );
	if ( NULL != pAttributes )
	{
		for ( int i = 0; i < lCnt; ++i )
		{
			if ( 0 == wcscmp( pAttributes[i].wcsName, L"encode" ) )
			{
				if ( 0 == wcscmp( pAttributes[i].wcsValue, L"false" ) )
				{
					pstItemData->bIsEncode	=	false;
				}
				else if ( 0 == wcscmp( pAttributes[i].wcsValue, L"true") )
				{
					pstItemData->bIsEncode	=	true;
				}
				else
				{
					_ASSERT(0);
				}
				break;
			}
		}

		delete [] pAttributes;
		pAttributes	=	NULL;

	}
	
	
	wchar_t * pValue	=	NULL;
	//name
	hr	=	pCXmlNode->GetNodeContent ( L"name/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->bstrName	=	pValue;
		delete	pValue, pValue	=	NULL;
	}

	//telno
	hr	=	pCXmlNode->GetNodeContent ( L"address/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0')
	{
		pstItemData->bstrTelNo	=	pValue;
		delete	pValue, pValue	=	NULL;
	}

	//sid
	hr	=	pCXmlNode->GetNodeContent( L"sid/", &pValue, NULL, NULL );
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
		pstItemData->wcFirst		=	*pValue;
		delete	pValue, pValue	=	NULL;
	}
	//Time
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"time/", &pValue, NULL, NULL );
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
			//_ltow( stSystemTime.wMinute, buffer, 10 );
			wsprintf(buffer, L"%02d", stSystemTime.wMinute);
			bstrTime.Append( buffer );

			pstItemData->bstrTime	=	bstrTime.m_str;
		}

		delete	pValue, pValue	=	NULL;
	}

	//Content
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"content/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0' )
	{
		pstItemData->bstrContent	=	pValue;
		delete	pValue, pValue		=	NULL;
	}
	//Type : From or To
	pValue	=	NULL;
	hr	=	pCXmlNode->GetNodeContent( L"type/", &pValue, NULL, NULL );
	if ( NULL != pValue && *pValue != L'\0' )
	{
		pstItemData->wcFromTo	=	*pValue;
		delete	pValue, pValue		=	NULL;
	}


	return	APP_Result_S_OK;
}

APP_Result		CCoreSmsUiCtrl::MakeNode( CXmlStream	*pCXmlStream, UiCodeChar *pNodePath, NodeAttribute_t *pNodeAttribute_t, long lCnt, void *pValue, EnCoreType _type )
{
	CXmlNode	*	pCXmlNode	=	NULL;

	APP_Result	hr	=	pCXmlStream->SelectNode( pNodePath, &pCXmlNode );
	if ( FAILED_App( hr ) )												return	APP_Result_E_Fail;

	auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

	switch ( _type )
	{
	case EN_CORE_NONE:
		hr	=	CXmlNodePtr->SetNodeContent( NULL, (wchar_t*)NULL, pNodeAttribute_t, lCnt );
		if ( FAILED_App( hr ) )												return	APP_Result_E_Fail;
		break;

	case EN_CORE_DOUBLE:
		hr	=	CXmlNodePtr->SetNodeContent( NULL, *((double*)pValue), pNodeAttribute_t, lCnt );
		if ( FAILED_App( hr ) )												return	APP_Result_E_Fail;
		break;

	case EN_CORE_LONG:
		hr	=	CXmlNodePtr->SetNodeContent( NULL, *((long*)pValue), pNodeAttribute_t, lCnt );
		if ( FAILED_App( hr ) )												return	APP_Result_E_Fail;
		break;

	case EN_CORE_WCHAR:
		hr	=	CXmlNodePtr->SetNodeContent( NULL, (wchar_t*)pValue, pNodeAttribute_t, lCnt );
		if ( FAILED_App( hr ) )												return	APP_Result_E_Fail;
		break;

	default:
		_ASSERT ( 0 );
		break;
	}


	return	APP_Result_S_OK;
}

APP_Result		CCoreSmsUiCtrl::GetListCnt( CXmlStream	*pCXmlStream, long	&lCnt )
{
	if ( NULL == pCXmlStream )
	{
		return	APP_Result_E_Fail;
	}

	lCnt	=	0;

	CXmlNode	*	pCXmlNode	=	NULL;
	pCXmlStream->SelectNode( L"result/data/data/", &pCXmlNode );

	auto_ptr<CXmlNode>	CXmlNodePtr( pCXmlNode );

	NodeAttribute_t		*pAttributes	=	NULL;
	long				lAttrCnt		=	0;

	APP_Result	hr	=	CXmlNodePtr->GetNodeContent( NULL, NULL, &pAttributes, &lAttrCnt );
	if ( FAILED_App ( hr ) )												return	APP_Result_E_Fail;

	for ( int i = 0; i < lAttrCnt; ++i )
	{
		if ( 0 == wcsncmp( pAttributes[i].wcsName, L"count", wcslen( L"count" ) ) )
		{
			lCnt	=	_wtol( pAttributes[i].wcsValue );
			break;
		}
	}

	return	APP_Result_S_OK;
}