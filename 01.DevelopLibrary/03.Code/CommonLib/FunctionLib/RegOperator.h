
#ifndef __RegOperator_h__
#define __RegOperator_h__

class COMMONLIB_API CRegOperator
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
	APP_Result CreateKey(HKEY hKey,
		wchar_t* pwcsSubKey, long* plKeyStatus);

	APP_Result OpenKey(HKEY hKey,
		wchar_t* pwcsSubKey);

	APP_Result GetValue(wchar_t* pwcsItemName, long lItemType, char* pcItemValue, long ItemValueSize );

	APP_Result SetValue(wchar_t* pwcsItemName, long lItemType, char* pcItemValue, long ItemValueSize);

	APP_Result DeleteValue( wchar_t* pwcsItemName);

private:
	HKEY m_hKey;
};

#endif //__RegOperator_h__