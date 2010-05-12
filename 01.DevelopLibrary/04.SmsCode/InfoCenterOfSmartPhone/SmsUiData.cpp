#include "SmsUiData.h"
#include <string>
#include <mzfc/CReg.h>
#include "CSmsUi_inc.h"
//#include "CSmsUiApp.h"

using namespace std;

//////////////////////////////////////////////////////////////////////////

#define SMS_FONTSIZE_DESCTEST_NORMAL			23  ///< 未读短信时间字体

static	int si_currentMessageId = 0;
static	UINT s_ui_CntOfSendingSms = 0;
static	HANDLE h_SmsSendOver = NULL;
static	CRITICAL_SECTION	CSChangeCntOfSendSms;
//static	UINT s_uiDescTextFontSize = SMS_FONTSIZE_DESCTEST_NORMAL;
static std::wstring s_ReceiverNum;
static std::wstring s_Sms_UserLastContent = L"";
//static std::wstring s_Sms_MyNickname  = L"Me";
static CSMSConfigInfo sUserConfigInfo;

static int g_Args_Type = E_SMS_AT_None;
static std::wstring g_Args_PhoneNum;
static std::wstring g_Args_Title;
static std::wstring g_Args_Content;
static std::wstring g_Args_FilePath;
static std::wstring g_Args_BevyID;

CSMSConfigInfo::CSMSConfigInfo(void)
{
	m_bFilterSpamMSG = false;			
	m_bFilterSMSInBlacklist = false;	
	m_bSMSStatusReport = false;		
	m_bLockSMS = false;				
	m_dContentFontSize = SMS_FONTSIZE_DESCTEST_NORMAL;
	strMyNickName = L"Me";		
	m_bMMSRecv = true;				
	m_nMMSAutoDownLoad = true;			
}

//返回分割的个数
int SplitCmdArgs(const wchar_t* pstrCmd, std::wstring **Strs, int nCount)
{
//	RETAILMSG(ZONE_FUNCTION, (TEXT("*******ZONE_FUNCTION******* SplitCmdArgs: %s\n"), pstrCmd));

	if (Strs==0 || nCount<=0)
		return 0;

	int nLen = wcslen(pstrCmd);
	if (nLen<=0)
		return 0;

	std::wstring strCmd = pstrCmd;
	int nPos = 0;
	int nRet = 0;
	std::wstring **pStr = Strs;
	int n = 0;
	for (n = 0; n<nCount; n++)
	{
		int p = strCmd.find(L"▓", nPos);
		if (p == std::wstring::npos)
			break;
		
		*(*pStr) = strCmd.substr(nPos, p-nPos);
		pStr++;
		nPos = p+1;
		nRet++;
	}
	if (nPos<=nLen && n<nCount)
	{
		*(*pStr) = strCmd.substr(nPos, nLen-nPos);
		nRet++;
	}
	return nRet;
}

int ParseSmsCommandLineArgs_Type(const wchar_t* pstrCmd)
{
//	RETAILMSG(ZONE_FUNCTION, (TEXT("*******ZONE_FUNCTION******* ParseSmsCommandLineArgs_Type: %s\n"), pstrCmd));

	int nRet = E_SMS_AT_None;

	std::wstring strCmd;
	int nLen = wcslen(pstrCmd);
	if (nLen<=1)
	{
		nRet = E_SMS_AT_None;
		goto lbReturn;
	}

	strCmd = pstrCmd;
	const wchar_t *pch = strCmd.c_str();
	if (*pch!=L'-')
	{
		nRet = E_SMS_AT_None;
		goto lbReturn;
	}

	pch++;
	switch(*pch)
	{
	case L'n':
		{
			nRet = E_SMS_AT_NewSMS;
			goto lbReturn;
		}
		break;
	case L'r':
		{
			nRet = E_SMS_AT_Reply;
			goto lbReturn;
		}
		break;
	case L'c':
		{
			nRet = E_SMS_AT_Conversation;
			goto lbReturn;
		}
		break;
	case L'v':
		{
			nRet = E_SMS_AT_VCARD;
			goto lbReturn;
		}
		break;

	case L'b':
		{
			nRet = E_SMS_AT_BEVY;
			goto lbReturn;
		}
		break;
	}

lbReturn:
//	RETAILMSG(ZONE_FUNCTION, (TEXT("*******ZONE_FUNCTION******* ParseSmsCommandLineArgs_Type: return ArgType: %d\n"), nRet));
	return nRet;
}


void ParseSmsCommandLineArgs(const wchar_t* pstrCmd)
{
	CMzString strCmd = pstrCmd;
	int nLen = strCmd.Length();
	if (nLen<=1)
	{
		g_Args_Type = E_SMS_AT_None;
		return;
	}
	ClearCommandLineArgs();

	g_Args_Type = ParseSmsCommandLineArgs_Type(strCmd.C_Str());
	
	switch(g_Args_Type)
	{
	case E_SMS_AT_None:
		return;
		break;
	case E_SMS_AT_VCARD:
		{
			if(nLen<=3)
				return;
			wchar_t *pch = strCmd.C_Str();
			pch += 3;
			std::wstring strParam = pch;
			std::wstring *Strs[3] = {&g_Args_FilePath, &g_Args_Content, &g_Args_PhoneNum};
			SplitCmdArgs(strParam.c_str(), Strs, 3);
		}
		return;
		break;
	case E_SMS_AT_NewSMS:
	case E_SMS_AT_Reply:
		{
			if(nLen<=3)
				return;
			const wchar_t *pch = strCmd.C_Str();
			pch += 3;
			std::wstring strParam = pch;
			std::wstring *Strs[4] = {&g_Args_PhoneNum, &g_Args_Content, &g_Args_Title, &g_Args_FilePath};
			SplitCmdArgs(strParam.c_str(), Strs, 4);
		}
		break;
	case E_SMS_AT_BEVY:
		{
			if(nLen<=3)
				return;
			const wchar_t *pch = strCmd.C_Str();
			pch += 3;
			std::wstring strParam = pch;
			std::wstring *Strs[4] = {&g_Args_BevyID, &g_Args_Content, &g_Args_Title, &g_Args_FilePath};
			SplitCmdArgs(strParam.c_str(), Strs, 4);
		}
		break;
	case E_SMS_AT_Conversation:
		{
			if(nLen<=3)
				return;
			const wchar_t *pch = strCmd.C_Str();
			pch += 3;
			std::wstring strParam = pch;
			std::wstring *Strs[1] = {&g_Args_PhoneNum};
			SplitCmdArgs(strParam.c_str(), Strs, 1);
		}
		break;
	}
}

const wchar_t* GetSmsCommandLineArgs_PhoneNum(void)
{
	return g_Args_PhoneNum.c_str();
}

const wchar_t* GetSmsCommandLineArgs_Title(void)
{
	return g_Args_Title.c_str();
}

const wchar_t* GetSmsCommandLineArgs_Content(void)
{
	return g_Args_Content.c_str();
}
const wchar_t* GetSmsCommandLineArgs_FilePath(void)
{
	return g_Args_FilePath.c_str();
}

const wchar_t* GetSmsCommandLineArgs_BevyID(void)
{
	return g_Args_BevyID.c_str();
}

int GetSmsCommandLineArgs_Type()
{
	return g_Args_Type;
}




DWORD WINAPI LoadComponentThreadProc(LPVOID lpParameter)
{
	
	return 0;
}

void LoadComponent()
{
	
}


int SplitPhoneNumbers( const wchar_t *strNum, __out std::vector<std::wstring> &vphonenumers )
{
return 0;
}
int SplitStrings(const wchar_t *strValue, const wchar_t *strSpliter, __out std::vector<std::wstring> &vStrings)
{
	return 0;
}

CEOID GetNamesOfPhoneNumbers(__in std::vector<std::wstring> &vphonenumers, __in std::vector<std::wstring> &vnames, __out std::wstring &names)
{


	return 0;
}



static wchar_t *g_RegkeyPath_SaveLastEditText = L"SOFTWARE\\Meizu\\Sms";
static wchar_t *g_RegkeyName_SaveLastEditText = L"LastEditText";
static wchar_t *g_RegkeyName_NewSmsPhoneNumber = L"NewSmsPhoneNumber";

void RegSms_SaveLastEditText( wchar_t *pStr )
{

}

void RegSms_GetLastEditText( std::wstring &strValue )
{

}

void RegSms_GetNewSmsPhoneNumber( std::wstring &strValue )
{

}



CMzString GetVCardName( const wchar_t* pFileName )
{
	CMzString ret;
	
	return ret;
}

BOOL InsertRecvMsgToDB(mzu::array<mzu::stringw>& vAddressInfo,LPCWSTR lpDecBuf,
				   int iMsgDate, DWORD dwSegIdx, DWORD dwSegCnt,SMS_MSG_TYPE msgType)
{
	int iMessageId = 0;
	MessageDbIf* pMessageIf = MessageDbIf::GetMessageDbIf();
	if (!pMessageIf)
	{
		return FALSE;
	}
	int currentMessageId = 0;
	CMessageInfo info;

	info.threadId = 0;				
	info.msg_type = msgType;//SMS_MSG_TYPE_NORMAL;
	info.read = 0;
	info.type = SMS_TYPE_RECV;
	info.date = iMsgDate;

	if(msgType == SMS_MSG_TYPE_NORMAL)
	{
		info.strSubject = L"";
		info.strBody = lpDecBuf;
	}
	else if(msgType == SMS_MSG_TYPE_MMS)
	{
		//拆分彩信内容,标题存body,内容存subject
		wstring mmsContent = lpDecBuf ;
		info.strSubject = mmsContent.substr(0,14).c_str();
		info.strBody = mmsContent.substr(14).c_str();/**/
	}

	info.status = SMS_SEND_STATUS_DEFAULT;
	info.partNumber = dwSegCnt;
	if (dwSegIdx > 0)
	{
		info.partNumber = dwSegIdx;
	}
	
	int threadId = pMessageIf->PreInsertMessage(vAddressInfo, info);
	if (vAddressInfo.size()>0)
	{
		info.strNumber = vAddressInfo[0];
	}
	printf("info.strNumber 1 :%s\n",info.strNumber.c_str());
	info.threadId = threadId;
	iMessageId = pMessageIf->InsertMessage(info);
	printf("info.strNumber 2:%s\n",info.strNumber.c_str());
	pMessageIf->UpdateMessageAssociateID(iMessageId,iMessageId);

	return TRUE;
	
}

CMessageInfo InsertSendMsgToDB(int status, mzu::array<mzu::stringw>& vAddressInfo,DWORD dAddrIndex,LPCWSTR lpDecBuf ,int & iassociateId,SMS_MSG_TYPE msgType,DWORD dwSegCnt)
{
	int iMessageId = 0;
	CMessageInfo info;
	MessageDbIf* pIf = MessageDbIf::GetMessageDbIf();
	if (pIf)
	{
		info.threadId = 0;			
		info.msg_type = msgType;//SMS_MSG_TYPE_NORMAL;
		info.read = 1;
		info.status = status;
		info.type = SMS_TYPE_SEND;
		info.date = -1;

		if(msgType == SMS_MSG_TYPE_NORMAL)
		{
			info.strSubject = L"Me";
			info.strBody = lpDecBuf;
		}
		else if(msgType == SMS_MSG_TYPE_MMS)
		{
			//拆分彩信内容
			wstring mmsContent = lpDecBuf ;
			info.strSubject = mmsContent.substr(0,14).c_str();
			info.strBody = mmsContent.substr(14).c_str();/**/
		}
	
		info.partNumber = dwSegCnt; // how to get? get from howell's interface

		info.threadId = pIf->PreInsertMessage(vAddressInfo, info);
		//for (int nIndex = 0;nIndex < vAddressInfo.size();nIndex ++)
		//{
//		info.associateId = iassociateId;
		info.strNumber = vAddressInfo[dAddrIndex];
		info.messageid = pIf->InsertMessage(info);
	}
	
	return info;
}

//void SetDescTextFontSize(UINT uiFontSize)
//{
//	s_uiDescTextFontSize = uiFontSize;
//}

int GetDescTextFontSize(void)
{
	 return sUserConfigInfo.m_dContentFontSize;
}

void SetUserInputLastContent(wchar_t* p_strSmsContent)
{
	s_Sms_UserLastContent = p_strSmsContent;
}

const wchar_t* GetUserInputLastContent(void)
{
	return s_Sms_UserLastContent.c_str();
}

//void SetMyNickname(wchar_t* pstrName)
//{
//	sUserConfigInfo.strMyNickName = pstrName;
//}

const wchar_t* GetMyNickname(void)
{
	return sUserConfigInfo.strMyNickName.c_str();
}


bool GetSMSStatusReportStatus(void)
{
	return sUserConfigInfo.m_bSMSStatusReport;
}


void ClearCommandLineArgs(void)
{

}

void ImplementUserConfigInfo(void)
{

}
