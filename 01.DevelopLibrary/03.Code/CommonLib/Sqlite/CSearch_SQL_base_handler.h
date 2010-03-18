#ifndef __CSEARCH_SQL_BASE_HANDLER_H__
#define __CSEARCH_SQL_BASE_HANDLER_H__

#include "sqlite/sqlite3.h"


//====================================================================================
//____________________________________________________________________________________
//	CSQL_query.

class  sqllock{

	public:
		sqllock			(){::InitializeCriticalSection( &m_sec ); }
		~sqllock			(){::DeleteCriticalSection( &m_sec );	  }
		void lock		(){::EnterCriticalSection( &m_sec );	  }
		void unlock		(){::LeaveCriticalSection( &m_sec );      }

	private:
		CRITICAL_SECTION		m_sec;
};





class CSQL_query :public CNaviSystemObject
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
		
		
		HRESULT					Prepare(const wchar_t* _pwcsQuery);	
		HRESULT					Finalize();
		HRESULT					Reset();	
		HRESULT					Step();
		HRESULT					GetData();


		HRESULT					GetField( long _lIndex,  long*		_plValue  );
		HRESULT					GetField( long _lIndex,  double*	_pdValue );
		HRESULT					GetField( long _lIndex,  wchar_t**	_pwcvalue );
		HRESULT					Getblob(long _lIndex,   const void** _pwcvalue );
		
		HRESULT					Bind( long	_lIndex );
		HRESULT				    Bind( long	_lindex,	long 		_lvalue );
		HRESULT					Bind( long	_lindex,	double		_dvalue );
		HRESULT					Bind( long	_lindex,	wchar_t * 	_wcvalue, int iSize );
		HRESULT					Bindblob( long _lIndex,   void* _pwcvalue , int isize);
		
		
};








//====================================================================================
//____________________________________________________________________________________
//	CSQL_session_base.

class CSQL_session_base :public CNaviSystemObject
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
		HRESULT					Connect(const wchar_t* _pwcs_db_Folder,  const wchar_t* _pwcs_db_File,  int _buffSize);
		HRESULT					Disconnect();
		
		sqlite3*				GetSession()	{	return	m_pdb;		};
		
		
		
		
};








class	CSQL_sessionManager;


//====================================================================================
//____________________________________________________________________________________
//	CSQL_session.


class	CSQL_session : public CSQL_session_base
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
		
		CMark_CriticalSectionObject				m_cs;		
		
		
	private:
		int						GetRef()	{	return	m_refCnt;		}
		int						AddRef()	{	return	++m_refCnt;		}
		
		
		
		
	public:
		wchar_t*				GetName();
		HRESULT					Connect		(const wchar_t* _pn,  const wchar_t* _pfolder,  const wchar_t* _pfile,  int _buffSize);
		void					DisConnect	(bool* _pb);
		
		
		HRESULT					Query_Create	(int* _pqh,		CSQL_query** _ppq);
		HRESULT					Query_Delete	(int _qh);
		HRESULT					Query_Get		(int _qh,		CSQL_query** _ppq);
		
		
		
		
};








//====================================================================================
//____________________________________________________________________________________
//	CSQL_sessionManager.

class	CSQL_sessionManager : public CNaviSystemObject
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
		
		CMark_CriticalSectionObject				m_cs;		
		
		
	private:
		HRESULT					Session_Connect_base(const wchar_t* _name,  const wchar_t* _folder,  const wchar_t* _file,  int _buffSize,  CSQL_session** _ppsession);
		
		
		
		
	public:
		HRESULT					Session_Connect		(const wchar_t* _name,  const wchar_t* _folder,  const wchar_t* _file,  CSQL_session** _ppsession);
		HRESULT					Session_Connect		(const wchar_t* _name,  const wchar_t* _folder,  const wchar_t* _file,  int _buffSize,  CSQL_session** _ppsession);
		HRESULT					Session_DisConnect	(const wchar_t* _name, BOOL* pbIsDBClosed);
		
		
		
		
};

#endif //__CSEARCH_SQL_BASE_HANDLER_H__


