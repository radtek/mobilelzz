#ifndef __ResultXmlOperator_h__
#define __ResultXmlOperator_h__

typedef	wchar_t		UiCodeChar;

typedef struct CoreItemData_t
{
	long						lPid;
	long						lSid;
	long						lCnt;
	bool						bIsLock;
	bool						bIsRead;
	UiCodeChar					wcFirst;
	/*CDynamicArray<UiCodeChar>*/CComBSTR	bstrTelNo;
	/*CDynamicArray<UiCodeChar>*/CComBSTR	bstrName;
	/*CDynamicArray<UiCodeChar>*/CComBSTR	bstrTime;
	/*CDynamicArray<UiCodeChar>*/CComBSTR	bstrContent;
	/*CDynamicArray<UiCodeChar>*/CComBSTR	bstrDetail;
//	unsigned	short			usIcon;
	UiCodeChar					wcFromTo;
	bool						bIsEncode;

}stCoreItemData;

enum	EnCoreType
{
	EN_CORE_NONE		=	0,
	EN_CORE_DOUBLE,
	EN_CORE_LONG,
	EN_CORE_WCHAR
};

class  COMMONLIB_API CCoreSmsUiCtrl
{
	public:

		CCoreSmsUiCtrl( void );

		virtual ~CCoreSmsUiCtrl( void );

	public:

		APP_Result		MakeUnReadRltListReq ( UiCodeChar **ppBuf, long *lSize );
		APP_Result		MakeCtorRltListReq   ( UiCodeChar **ppBuf, long *lSize );
		APP_Result		MakeMsgRltListReq    ( UiCodeChar **ppBuf, long *lSize, long lPid );
		APP_Result		MakeDetailRltListReq ( UiCodeChar **ppBuf, long *lSize, long lSid );
		APP_Result		MakeSendSmsInfo		 ( UiCodeChar **ppBuf, long *lSize, UiCodeChar *pwcSmsInfo, UiCodeChar* pwcsNumber );
		APP_Result		MakeDeleteSmsInfo	 ( OUT UiCodeChar **ppBuf, OUT long *lSize, IN long *plSid, IN long lCnt );
		APP_Result		MakeUpdateSmsStatusReq( UiCodeChar **ppBuf, long *lSize, long lSid, long lLock, long lRead );

		APP_Result		MakePassWordStatusReq ( UiCodeChar **ppBuf, long *lSize, 
												long lPid, UiCodeChar* pwcDataKind, 
												UiCodeChar* pwcCode, UiCodeChar* pwcNewCode );
	
		APP_Result		MakeDetailReq ( UiCodeChar **ppBuf, long *lSize, long lSid, UiCodeChar* pwcCode );


		APP_Result		MakeListRlt    ( UiCodeChar *pwcRltStream, stCoreItemData **ppclstItemData, long *plCnt );
		APP_Result		MakeDetailRlt  ( UiCodeChar *pwcRltStream, wchar_t **ppwcDetail );



	private:

		APP_Result		MakeNode(	CXmlStream	*pCXmlStream, UiCodeChar *pNodePath, NodeAttribute_t *pNodeAttribute_t,
									long lCnt, void *pValue = NULL, EnCoreType _type = EN_CORE_NONE );

		APP_Result		GetListCnt( CXmlStream	*pCXmlStream, long	&lCnt );

		APP_Result		AppendList( CXmlNode *pCXmlNode, stCoreItemData *pclstItemData );

		APP_Result		GetInfo( IN CXmlNode *pCXmlNode, OUT stCoreItemData	*pstItemData );
};


#endif __RequestXmlOperator_h__