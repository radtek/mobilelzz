#ifndef __CSEARCH_SQL_BASE_HANDLER_H__
#define __CSEARCH_SQL_BASE_HANDLER_H__

#include "sqlite3.h"
#include <windows.h>
#include <vector>
using namespace std;
#define   Sms_SQL_CREATETABLE_SmsDetail	L"create table IF NOT EXISTS SmsDetail(sid integer PRIMARY KEY ASC, \
											pid integer, type integer, content text, address text not null, time integer,\
											lockstatus integer, readstatus integer)"
#define   Sms_SQL_CREATETABLE_SmsCode	L"create table IF NOT EXISTS SmsCode(pid integer PRIMARY KEY ASC, \
											code text)"
#define   Sms_SQL_CREATETABLE_SmsGroup	L"create table IF NOT EXISTS SmsGroup(pid integer PRIMARY KEY ASC, \
											msgcount integer)"

#define   Contactors_SQL_GET_CONTACTS  L"select ABPerson.ROWID, ABPerson.Name , ABPhones.*  from ABPerson, ABPhones where ABPerson.ROWID = ABPhones.record_id "
#define   Contactors_SQL_GET_FIRSTLETER  L"select token  from ABLookupFirstLetter where source = ?"
#define   Sms_SQL_GET_PID_ByAddress				L"select record_id  from ABPhones where number = ? collate binary"
#define		Sms_SQL_GET_Name_ByPID			L"select Name from ABPerson where ROWID = ?"

#define   Sms_SQL_GET_UnReadSms					L"select *  from SmsDetail where readstatus = 0"
#define   Sms_SQL_GET_SmsList					L"select *  from SmsDetail"
#define   Sms_SQL_GET_SmsListByContactor		L"select *  from SmsDetail where pid = ?"

#define   Sms_SQL_GET_SmsGroupInfo			L"select *  from SmsGroup where pid = ?"
#define   Sms_SQL_SET_ReadStatus			L"update SmsDetail set readstatus = ? where sid = ?"
#define   Sms_SQL_SET_LockStatus			L"update SmsDetail set lockstatus = ? where sid = ?"

#define   Sms_SQL_GET_SmsCode				L"select * from SmsCode where pid = ?"
#define   Sms_SQL_INSERT_SmsCode			L"insert into SmsCode values(?,?)"
#define   Sms_SQL_SET_SmsContent			L"update SmsDetail set content = ? where sid = ?"
#define   Sms_SQL_SET_SmsCode				L"update SmsCode set code = ? where pid = ?"
#define   Sms_SQL_DELETE_SmsCode			L"delete from SmsCode where pid = ?"
#define   Sms_SQL_DELETE_SmsDetail			L"delete from SmsDetail where sid = ?"
#define   Sms_SQL_DELETE_ALLSmsDetail		L"delete from SmsDetail where lockstatus != 1"

#define   Sms_SQL_INSERT_SmsDetail			L"insert into SmsDetail(sid,pid,type,content,address,time,\
												lockstatus,readstatus) values(?,?,?,?,?,?,?,?)"

#define		Sms_SQL_Search_SmsDetail_AfterDate		L"select * from SmsDetail where pid = ? and content like ?\
														and type = ? and time > ?"
#define		Sms_SQL_Search_SmsDetail_BeforeDate		L"select * from SmsDetail where pid = ? and content like ?\
														and type = ? and time < ?"
#define		Sms_SQL_Search_SmsDetail_BetweenDate	L"select * from SmsDetail where pid = ? and content like ?\
														and type = ? and time < ? and time > ?"
														
#define	Sms_SQL_Insert_SmsGroupInfo		L"insert to SmsGroup values(?,?)"
#define	Sms_SQL_Update_MsgCount			L"update SmsGroup set msgcount = ? where pid = ?"

#define		 S_ROW			((APP_Result)0x00000002L)

//====================================================================================
//____________________________________________________________________________________
//	CSQL_query.

// class  sqllock{
// 
// 	public:
// 		sqllock			(){::InitializeCriticalSection( &m_sec ); }
// 		~sqllock			(){::DeleteCriticalSection( &m_sec );	  }
// 		void lock		(){::EnterCriticalSection( &m_sec );	  }
// 		void unlock		(){::LeaveCriticalSection( &m_sec );      }
// 
// 	private:
// 		CRITICAL_SECTION		m_sec;
// };





class COMMONLIB_API CSQL_query 
{
	public:	
		virtual	~CSQL_query();
		
		CSQL_query(sqlite3* _pdb, int _qh) : 
			m_pdb(0),	m_pstmt(0), 
			m_qh(_qh) 
		{
			m_pdb				= _pdb;
		}
		
		
		
		
	private:
		int						m_qh;
		
	
		
	public:
		sqlite3*				m_pdb;
		sqlite3_stmt*			m_pstmt;

//		sqllock					m_lock;
		
		
		
		
		
		
	public:
		int						GetQh()	{	return	m_qh;	};
		
		
		APP_Result					Prepare(const wchar_t* _pwcsQuery);	
		APP_Result					Finalize();
		APP_Result					Reset();	
		APP_Result					Step();
		APP_Result					GetData();


		APP_Result					GetField( long _lIndex,  long*		_plValue  );
		APP_Result					GetField( long _lIndex,  double*	_pdValue );
		APP_Result					GetField( long _lIndex,  wchar_t**	_pwcvalue );
		APP_Result					Getblob(long _lIndex,   const void** _pwcvalue );
		
		APP_Result					Bind( long	_lIndex );
		APP_Result				    Bind( long	_lindex,	long 		_lvalue );
		APP_Result					Bind( long	_lindex,	double		_dvalue );
		APP_Result					Bind( long	_lindex,	wchar_t * 	_wcvalue, int iSize );
		APP_Result					Bindblob( long _lIndex,   void* _pwcvalue , int isize);
		
		
};








//====================================================================================
//____________________________________________________________________________________
//	CSQL_session_base.

class COMMONLIB_API CSQL_session_base 
{
	public:
		virtual	~CSQL_session_base();
		
		CSQL_session_base() : 
			m_pdb(0)
		{
		}
		
		
		
		
	protected:
		sqlite3*				m_pdb;
		
		
		
		
	public:
		APP_Result					Connect(const wchar_t* _pwcs_db_Folder,  const wchar_t* _pwcs_db_File,  int _buffSize);
		APP_Result					Disconnect();
		
		sqlite3*				GetSession()	{	return	m_pdb;		};
		
		
		
		
};








class	CSQL_sessionManager;


//====================================================================================
//____________________________________________________________________________________
//	CSQL_session.


class COMMONLIB_API	CSQL_session : public CSQL_session_base
{
	friend		CSQL_sessionManager;
	
	
	public:
		virtual ~CSQL_session();
		
		CSQL_session() :
			m_name(0), 
			m_refCnt(0), m_hdlCnt(0)
		{
			m_vec_qry.clear();
			m_maxcount = 0;
		};
		
		
		
		
	private:
		wchar_t*				m_name;
		vector<CSQL_query*>		m_vec_qry;
		
		int						m_refCnt;
		int						m_hdlCnt;

		int						m_maxcount;
		
//		CMark_CriticalSectionObject				m_cs;		
		
		
	private:
		int						GetRef()	{	return	m_refCnt;		}
		int						AddRef()	{	return	++m_refCnt;		}
		
		
		
		
	public:
		wchar_t*				GetName();
		APP_Result					Connect		(const wchar_t* _pn,  const wchar_t* _pfolder,  const wchar_t* _pfile,  int _buffSize);
		void					DisConnect	(bool* _pb);
		
		
		APP_Result					Query_Create	(int* _pqh,		CSQL_query** _ppq);
		APP_Result					Query_Delete	(int _qh);
		APP_Result					Query_Get		(int _qh,		CSQL_query** _ppq);
		
		
		
		
};








//====================================================================================
//____________________________________________________________________________________
//	CSQL_sessionManager.

class COMMONLIB_API	CSQL_sessionManager 
{
	private:
		CSQL_sessionManager() : m_ref_count(0)
		{
			m_vec_session.clear();
		}
		
		virtual	~CSQL_sessionManager();
		
		
		static	CSQL_sessionManager*	m_this;
		
		
	public:
		static	CSQL_sessionManager*	GetInstance()
		{
			if (!m_this)	m_this	= new CSQL_sessionManager();
			m_this->m_ref_count++;
			return	m_this;
		}
		
		void					ReleaseInstance()
		{
			m_this->m_ref_count--;		if (m_this->m_ref_count <= 0)	delete m_this;
		}
		
		
		
		
	private:
		vector<CSQL_session*>	m_vec_session;
		int						m_ref_count;
		
//		CMark_CriticalSectionObject				m_cs;		
		
		
	private:
		APP_Result					Session_Connect_base(const wchar_t* _name,  const wchar_t* _folder,  const wchar_t* _file,  int _buffSize,  CSQL_session** _ppsession);
		
		
		
		
	public:
		APP_Result					Session_Connect		(const wchar_t* _name,  const wchar_t* _folder,  const wchar_t* _file,  CSQL_session** _ppsession);
		APP_Result					Session_Connect		(const wchar_t* _name,  const wchar_t* _folder,  const wchar_t* _file,  int _buffSize,  CSQL_session** _ppsession);
		APP_Result					Session_DisConnect	(const wchar_t* _name, BOOL* pbIsDBClosed);
		
		
		
		
};

class COMMONLIB_API	CSQL_SmartQuery
{
public:
	CSQL_SmartQuery();
	CSQL_SmartQuery(CSQL_session* pSession);
	CSQL_SmartQuery(CSQL_query* pQuery, CSQL_session* pSession);
	virtual~ CSQL_SmartQuery();
public:
	CSQL_query* Get();
	CSQL_query* Detach();
	void Release();
	void Initialize(CSQL_session* pSession);
	CSQL_query** operator&();
	CSQL_query* operator->();
private:
	BOOL				m_bOwn;
	CSQL_query*			m_pQuery;
	CSQL_session*		m_pSession;
};

#endif //__CSEARCH_SQL_BASE_HANDLER_H__


