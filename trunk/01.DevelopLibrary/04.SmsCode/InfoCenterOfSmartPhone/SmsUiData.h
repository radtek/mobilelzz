#pragma once
//#include "windows.h"
//#include "CSmsUi_inc.h"
#include <mzfc_inc.h>
#include <MzUtils/MzUtils.h>
#include <mzpam\MessageDbIf.h>
#include <vector>

//////////////////////////////////////////////////////////////////////////

#ifdef MZFC_X86
typedef DWORD CEOID;
#endif

enum ArgType{
	E_SMS_AT_None = 0,
	E_SMS_AT_NewSMS,
	E_SMS_AT_Conversation,
	E_SMS_AT_Reply,
	E_SMS_AT_VCARD,
	E_SMS_AT_BEVY
};

class CSMSConfigInfo
{
public:
	CSMSConfigInfo(void);

	
	bool m_bFilterSpamMSG;			//�Ƿ����������Ź��˹���
	bool m_bFilterSMSInBlacklist;	//�Ƿ���պ���������ϵ�˶���
	bool m_bSMSStatusReport;		//�Ƿ�Ҫ�����״̬��ִ
	bool m_bLockSMS;				//���ż��ܹ����Ƿ���
	DWORD m_dContentFontSize;		//������������ֵ
	mzu::stringw strMyNickName;		//�ҵ��ǳ�
	bool m_bMMSRecv;				//�Ƿ���ղ���
	int m_nMMSAutoDownLoad;			//0 �رգ� 1 ȫ�� 2 ͨ��¼����ϵ��

protected:
private:
};

void ParseSmsCommandLineArgs(const wchar_t* pstrCmd);

int GetSmsCommandLineArgs_Type(void);

const wchar_t* GetSmsCommandLineArgs_PhoneNum(void);

const wchar_t* GetSmsCommandLineArgs_Title(void);

const wchar_t* GetSmsCommandLineArgs_Content(void);

const wchar_t* GetSmsCommandLineArgs_FilePath(void);

const wchar_t* GetSmsCommandLineArgs_BevyID(void);

void ClearCommandLineArgs(void);

//////////////////////////////////////////////////////////////////////////

void LoadComponent(void);

int SplitPhoneNumbers(const wchar_t *strNum, __out std::vector<std::wstring> &vphonenumers);
int SplitStrings(const wchar_t *strValue, const wchar_t *strSpliter, __out std::vector<std::wstring> &vStrings);

CEOID GetNamesOfPhoneNumbers(__in std::vector<std::wstring> &vphonenumers, __in std::vector<std::wstring> &vnames, __out std::wstring &names);



CMzString GetVCardName(const wchar_t* pFileName);


BOOL InsertRecvMsgToDB(mzu::array<mzu::stringw>& vAddressInfo,LPCWSTR lpDecBuf,
					   int iMsgDate, DWORD dwSegIdx, DWORD dwSegCnt,SMS_MSG_TYPE msgType);

CMessageInfo InsertSendMsgToDB(int status,mzu::array<mzu::stringw>& vAddressInfo,DWORD dAddrIndex,LPCWSTR lpDecBuf ,
											int & iassociateId,SMS_MSG_TYPE msgType,DWORD dwSegCnt = 0);


//void	SetDescTextFontSize(UINT uiFontSize);								//Set descText fontsize for Item in conversation list
int		GetDescTextFontSize(void);											//Get descText fontsize for Item in conversation list

void SetUserInputLastContent(wchar_t* p_strSmsContent);					//set user input content in edit box 

const wchar_t* GetUserInputLastContent(void);									//get user input content in edit box 

//void SetMyNickname(wchar_t* pstrName);

const wchar_t* GetMyNickname(void);

bool GetSMSStatusReportStatus(void);

void ImplementUserConfigInfo(void);

//////////////////////////////////////////////////////////////////////////

//#include <ContactServer.h>
//
//IGetFilterInfo* GetFilterInfo();
//
//IContactInfoMatch* GetContactInfoMatch();
//IContactInfoSelect* GetContactInfoSelect();
//
////////////////////////////////////////////////////////////////////////////
//
//void RegSms_SaveLastEditText(wchar_t *pStr);
//void RegSms_GetLastEditText(std::wstring &strValue);
//void RegSms_GetNewSmsPhoneNumber( std::wstring &strValue );

//void WriteSmsLog(const wchar_t *strContent);
//void WriteSmsLog_d(const wchar_t *strContent, int nValue);
//void WriteSmsLog_u(const wchar_t *strContent, DWORD dwValue);
//void WriteSmsLog_s(const wchar_t *strContent, const wchar_t *strValue);

//#define WRITE_SMS_LOG WriteSmsLog
//#define WRITE_SMS_LOG_D WriteSmsLog_d
//#define WRITE_SMS_LOG_U WriteSmsLog_u
//#define WRITE_SMS_LOG_S WriteSmsLog_s

