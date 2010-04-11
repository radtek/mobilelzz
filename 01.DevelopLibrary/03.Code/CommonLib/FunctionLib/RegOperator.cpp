#include "stdafx.h"
#include "RegOperator.h"


APP_Result CRegOperator::CreateKey(HKEY hKey,
	wchar_t* pwcsSubKey, long* plKeyStatus)
{
	APP_Result hr = APP_Result_E_Fail;
	if ( NULL != m_hKey )
	{
		RegCloseKey(m_hKey);
	}
	hr = (APP_Result)RegCreateKeyEx(hKey, pwcsSubKey, 0, NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &m_hKey, (LPDWORD)plKeyStatus);
	if ( ERROR_SUCCESS != hr )
	{
		return APP_Result_E_Fail;
	}
	return APP_Result_S_OK;
}
APP_Result CRegOperator::OpenKey(HKEY hKey,
	wchar_t* pwcsSubKey)
{
	APP_Result hr = APP_Result_E_Fail;
	if ( NULL != m_hKey )
	{
		RegCloseKey(m_hKey);
	}
	hr = (APP_Result)RegOpenKeyEx(hKey, pwcsSubKey, 0, KEY_ALL_ACCESS, &m_hKey);
	if ( ERROR_SUCCESS != hr )
	{
		return APP_Result_E_Fail;
	}
	return APP_Result_S_OK;
}
APP_Result CRegOperator::GetValue(wchar_t* pwcsItemName, long lItemType, char* pcItemValue, long ItemValueSize )
{
	APP_Result hr = APP_Result_E_Fail;
	if ( NULL == m_hKey )
	{
		return APP_Result_E_Fail;
	}
	hr = (APP_Result)RegQueryValueEx(m_hKey, pwcsItemName, 0, (LPDWORD)&lItemType, (LPBYTE)pcItemValue, (LPDWORD)&ItemValueSize);
	if ( ERROR_SUCCESS != hr )
	{
		return APP_Result_E_Fail;
	}
	return APP_Result_S_OK;	
}
APP_Result CRegOperator::SetValue(wchar_t* pwcsItemName, long lItemType, char* pcItemValue, long ItemValueSize)
{
	APP_Result hr = APP_Result_E_Fail;
	if ( NULL == m_hKey )
	{
		return APP_Result_E_Fail;
	}
	hr = (APP_Result)RegSetValueEx(m_hKey, pwcsItemName, 0, lItemType, (LPBYTE)pcItemValue, ItemValueSize);
	if ( ERROR_SUCCESS != hr )
	{
		return APP_Result_E_Fail;
	}
	return APP_Result_S_OK;	
}
APP_Result CRegOperator::DeleteValue( wchar_t* pwcsItemName)
{
	APP_Result hr = APP_Result_E_Fail;
	if ( NULL == m_hKey )
	{
		return APP_Result_E_Fail;
	}
	hr = (APP_Result)RegDeleteValue( m_hKey, pwcsItemName );
	if ( ERROR_SUCCESS != hr )
	{
		return APP_Result_E_Fail;
	}
	return APP_Result_S_OK;	
}
