#include "stdafx.h"
#include "BasicService.h"
#include "SmsService.h"

CSmsService::CSmsService()
{

}

CSmsService::~CSmsService()
{

}

APP_Result CSmsService::MakeParam(wchar_t* pwcsRequestXML)
{


	return APP_Result_S_OK;
}

APP_Result CSmsService::ExcuteParam(wchar_t* pwcsRequestXML, wchar_t** ppwcsResultXML)
{


	return APP_Result_S_OK;
}

APP_Result CSmsService::MakeResult(wchar_t** ppwcsRequestXML)
{


	return APP_Result_S_OK;
}

APP_Result CSmsService::Initialize()
{
	APP_Result hr = APP_Result_E_Fail;
	hr = CBasicService::Initialize();
	if(FAILED(hr))	
		return hr;
	

	return APP_Result_S_OK;
}