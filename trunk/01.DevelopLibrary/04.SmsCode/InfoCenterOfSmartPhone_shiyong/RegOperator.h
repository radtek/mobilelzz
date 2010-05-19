
#ifndef __RegOperator_h__
#define __RegOperator_h__

class CRegOperator
{
public:
	CRegOperator()
	{
		m_hKey = NULL;
	}
	virtual ~CRegOperator()
	{
		if ( NULL != m_hKey )
		{
			RegCloseKey(m_hKey);
		}
	}
public:
	HRESULT CreateKey(HKEY hKey,
		wchar_t* pwcsSubKey, long* plKeyStatus)
	{
		HRESULT hr = E_FAIL;
		if ( NULL != m_hKey )
		{
			RegCloseKey(m_hKey);
		}
		hr = RegCreateKeyEx(hKey, pwcsSubKey, 0, NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &m_hKey, (LPDWORD)plKeyStatus);
		if ( ERROR_SUCCESS != hr )
		{
			return E_FAIL;
		}
		return S_OK;
	}
	HRESULT OpenKey(HKEY hKey,
		wchar_t* pwcsSubKey)
	{
		HRESULT hr = E_FAIL;
		if ( NULL != m_hKey )
		{
			RegCloseKey(m_hKey);
		}
		hr = RegOpenKeyEx(hKey, pwcsSubKey, 0, KEY_ALL_ACCESS, &m_hKey);
		if ( ERROR_SUCCESS != hr )
		{
			return E_FAIL;
		}
		return S_OK;
	}
	HRESULT GetValue(wchar_t* pwcsItemName, long lItemType, char* pcItemValue, long ItemValueSize )
	{
		HRESULT hr = E_FAIL;
		if ( NULL == m_hKey )
		{
			return E_FAIL;
		}
		hr = RegQueryValueEx(m_hKey, pwcsItemName, 0, (LPDWORD)&lItemType, (LPBYTE)pcItemValue, (LPDWORD)&ItemValueSize);
		if ( ERROR_SUCCESS != hr )
		{
			return E_FAIL;
		}
		return S_OK;	
	}
	HRESULT SetValue(wchar_t* pwcsItemName, long lItemType, char* pcItemValue, long ItemValueSize)
	{
		HRESULT hr = E_FAIL;
		if ( NULL == m_hKey )
		{
			return E_FAIL;
		}
		hr = RegSetValueEx(m_hKey, pwcsItemName, 0, lItemType, (LPBYTE)pcItemValue, ItemValueSize);
		if ( ERROR_SUCCESS != hr )
		{
			return E_FAIL;
		}
		return S_OK;	
	}
	HRESULT DeleteValue( wchar_t* pwcsItemName)
	{
		HRESULT hr = E_FAIL;
		if ( NULL == m_hKey )
		{
			return E_FAIL;
		}
		hr = RegDeleteValue( m_hKey, pwcsItemName );
		if ( ERROR_SUCCESS != hr )
		{
			return E_FAIL;
		}
		return S_OK;	
	}
private:
	HKEY m_hKey;
};

#endif //__RegOperator_h__