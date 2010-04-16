#include "stdafx.h"
#include "BasicService.h"
#include "XmlStream.h"
#include "RequestXmlOperator.h"
#include "SmsService.h"
#include "ContactorsService.h"
#include "CoreService.h"

#include "XmlStream.h"

#define DataTypeSms				L"sms"	
#define DataTypeContactors		L"contactors"	

CCoreService* CCoreService::m_Instance = NULL;

CCoreService::CCoreService()
{
	m_clSmsService.Initialize();
	m_clContactorsService.Initialize();
}

CCoreService::~CCoreService()
{

}

CCoreService& CCoreService::operator = (CCoreService& clCoreService)
{

	return *this;
}

CCoreService* CCoreService::GetInstance()
{
	if ( !m_Instance ){
		m_Instance = new CCoreService;
	}

	return m_Instance;
}

void CCoreService::DeleteInstance()
{
	if ( m_Instance ){
		delete m_Instance;
	}
	
}

APP_Result CCoreService::Request(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
{
	APP_Result appR = APP_Result_E_Fail;
	wchar_t awcsDataType[64] = L"";
	appR = GetDataType(pwcsRequestXML, awcsDataType, (sizeof(awcsDataType)/sizeof(awcsDataType[0])));
	if (FAILED_App(appR)){
		return appR;
	}
	
	if ( 0 == wcscmp(awcsDataType, DataTypeSms) )
	{
		m_clSmsService.Excute(pwcsRequestXML, ppwcsResultXML);
	} 
	else if( 0 == wcscmp(awcsDataType, DataTypeContactors) )
	{
		m_clContactorsService.Excute(pwcsRequestXML, ppwcsResultXML);
	}
	else{
		
	}

	return APP_Result_S_OK;
}

APP_Result CCoreService::GetDataType(wchar_t* pwcsRequestXML, wchar_t* pwcsDataType, long lSize)
{
	APP_Result appR = APP_Result_E_Fail;
	CXmlStream clXmlStream;
	long lXmlBufSize = wcslen(pwcsRequestXML);
	appR = clXmlStream.Load(pwcsRequestXML, lXmlBufSize);
	if ( FAILED_App(appR) ){
		return appR;
	}
	CXmlNode* pTypeNode = NULL;
	appR = clXmlStream.SelectNode(L"request/data/", &pTypeNode);
	if ( FAILED_App(appR) ){
		return appR;
	}
	NodeAttribute_t* pTypeAttribute = NULL;
	long lAttributeCount = 0;
	appR = pTypeNode->GetNodeContent( NULL, NULL, &pTypeAttribute, &lAttributeCount );
	if ( FAILED_App(appR) ){
		return appR;
	}
	F_wcscpyn(pwcsDataType, pTypeAttribute->wcsValue, lSize);

	delete[] pTypeAttribute;
	delete pTypeNode;

	return APP_Result_S_OK;
}