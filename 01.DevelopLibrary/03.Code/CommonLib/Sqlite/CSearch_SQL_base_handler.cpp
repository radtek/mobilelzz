#include	"stdafx.h"
//#include	"MarkCommon.h"
#include	"CSearch_SQL_base_handler.h"
/*
#define SQLITE_OK           0    Successful result 
#define SQLITE_ERROR        1    SQL error or missing database 
#define SQLITE_INTERNAL     2    An internal logic error in SQLite 
#define SQLITE_PERM         3    Access permission denied 
#define SQLITE_ABORT        4    Callback routine requested an abort 
#define SQLITE_BUSY         5    The database file is locked 
#define SQLITE_LOCKED       6    A table in the database is locked 
#define SQLITE_NOMEM        7    A malloc() failed 
#define SQLITE_READONLY     8    Attempt to write a readonly database 
#define SQLITE_INTERRUPT    9    Operation terminated by sqlite_interrupt() 
#define SQLITE_IOERR       10    Some kind of disk I/O error occurred 
#define SQLITE_CORRUPT     11    The database disk image is malformed 
#define SQLITE_NOTFOUND    12    (Internal Only) Table or record not found 
#define SQLITE_FULL        13    Insertion failed because database is full 
#define SQLITE_CANTOPEN    14    Unable to open the database file 
#define SQLITE_PROTOCOL    15    Database lock protocol error 
#define SQLITE_EMPTY       16    (Internal Only) Database table is empty 
#define SQLITE_SCHEMA      17    The database schema changed 
#define SQLITE_TOOBIG      18    Too much data for one row of a table 
#define SQLITE_CONSTRAINT  19    Abort due to contraint violation 
#define SQLITE_MISMATCH    20    Data type mismatch 
#define SQLITE_MISUSE      21    Library used incorrectly 
#define SQLITE_NOLFS       22    Uses OS features not supported on host 
#define SQLITE_AUTH        23    Authorization denied 
#define SQLITE_ROW         100   sqlite_step() has another row ready 
#define SQLITE_DONE        101   sqlite_step() has finished executing 
*/


/*#include	"resource.h"*/
#include <assert.h>
#include "CSearch_SQL_base_common.h"

#define		 S_ROW			((HRESULT)0x00000002L)

CSQL_sessionManager*	CSQL_sessionManager::m_this	= 0;




//====================================================================================
//____________________________________________________________________________________
//	CSQL_query.

//____________________________________________________________________________________
//	Public methods.


CSQL_query::~CSQL_query()
{
	if( NULL != m_pstmt )
	{	
		Finalize();
	}
	
}



HRESULT CSQL_query::Prepare(const wchar_t* _pwcsQuery)
{
//	m_lock.lock();
	
	int					hr	= E_FAIL;

	do {
		if( ( NULL == _pwcsQuery ) || ( NULL != m_pstmt ) || ( NULL == m_pdb ) ){
			assert(0);
			break;
		}

		long	lr = 0;
		lr	= sqlite3_prepare16_v2(m_pdb, _pwcsQuery, -1, &m_pstmt, 0);
		if (lr != SQLITE_OK){
			assert(0);
			break;
		}
		
		hr	= S_OK;
		
	}	while (false);
	
//	m_lock.unlock();
	
	return				hr;
}




HRESULT CSQL_query::Reset()
{	
//	m_lock.lock();

	int					hr	= E_FAIL;
	
	do {
		if( NULL == m_pstmt )
		{
			assert(0);
			break;
		}
		
		long	lr = 0;

		lr	= sqlite3_reset( m_pstmt );
		if (lr != SQLITE_OK)
		{
			assert(0);
			break;
		}	
		hr	= S_OK;

	}	while (false);
	
//	m_lock.unlock();

	return	hr;
}


HRESULT CSQL_query::Finalize()
{
//	m_lock.lock();

	int					hr	= E_FAIL;
	
	do {
		long	lr = 0;
		lr	= sqlite3_finalize( m_pstmt );
		if (lr != SQLITE_OK)
		{
			assert(0);
			break;
		}
		m_pstmt = NULL;
		hr	= S_OK;
		
	}	while (false);
	
//	m_lock.unlock();


	return	hr;
}


HRESULT	CSQL_query::Step()
{
//	m_lock.lock();

	int			hr	= E_FAIL;
		
	if( m_pstmt == NULL )
		return E_FAIL;
	
	long		lr  = 0;
	lr	= sqlite3_step( m_pstmt );
	if( SQLITE_DONE == lr ){
		hr = S_OK;	
	}
	else if( SQLITE_ROW == lr ){
		hr = S_ROW;
	}
	else{
		sqlite3_reset( m_pstmt );
		assert(0);
	}

 // m_lock.unlock();
	
	return		hr;
}


HRESULT		CSQL_query::GetField(long _lIndex, long* _plvalue )
{
//	m_lock.lock();

	if( NULL == m_pstmt || NULL == _plvalue ){
		assert( 0 );
		return E_FAIL;
	}
	
	*_plvalue	=	sqlite3_column_int(	m_pstmt, _lIndex );
	
//	m_lock.unlock();

	return	S_OK;
}


HRESULT		CSQL_query::GetField(long _lIndex, double* _pdvalue )
{
//	m_lock.lock();
	if( NULL == m_pstmt || NULL == _pdvalue ){
		assert( 0 );
		return E_FAIL;
	}

	*_pdvalue	=	sqlite3_column_double(	m_pstmt, _lIndex );
	
//	m_lock.unlock();

	return	S_OK;
}


HRESULT		CSQL_query::GetField(long _lIndex, wchar_t** _pwcvalue )
{
//	m_lock.lock();

	if( NULL == m_pstmt || NULL == _pwcvalue ){
		assert( 0 );
		return E_FAIL;
	}

	*_pwcvalue	= (wchar_t*)sqlite3_column_text16( m_pstmt, _lIndex );
	
//	m_lock.unlock();

	return	S_OK;
}

HRESULT		CSQL_query::Getblob(long _lIndex, const void** _pwcvalue )
{
//	m_lock.lock();

	if( NULL == m_pstmt || NULL == _pwcvalue ){
		assert( 0 );
		return E_FAIL;
	}
	
	*_pwcvalue	= sqlite3_column_blob( m_pstmt, _lIndex );

//	m_lock.unlock();
	return	S_OK;
}


HRESULT		CSQL_query::Bind(long _lIndex, wchar_t* _pwcvalue , int isize)
{
//	m_lock.lock();
	
	int					hr = E_FAIL;

	do {
		if( NULL == m_pstmt /*|| NULL == _pwcvalue*/  ){
			assert(0);
			break;
		}

		long		lr = 0;
		lr	= sqlite3_bind_text16( m_pstmt,  _lIndex,  _pwcvalue, isize, NULL  );
		if(lr != SQLITE_OK){
//			CMark_Error::set( lr, EN_ERR_SQLITE );
			assert(0);
			break;
		}

		hr = S_OK;

	} while( false );

//	m_lock.unlock();

	return	hr;
}

HRESULT		CSQL_query::Bindblob(long _lIndex, void* _pwcvalue , int isize)
{
//	m_lock.lock();

	int					hr = E_FAIL;


	do {

		if( NULL == m_pstmt /*|| NULL == _pwcvalue*/  ){
			assert(0);
			break;
		}
		long		lr = 0;

		lr	= sqlite3_bind_blob( m_pstmt,  _lIndex,  _pwcvalue, isize, NULL  );
		if(lr != SQLITE_OK){
//			CMark_Error::set( lr, EN_ERR_SQLITE );
			assert(0);
			break;
		}

		hr = S_OK;

	} while( false );

//	m_lock.unlock();

	return	hr;
}

HRESULT		CSQL_query::Bind(long _lIndex, double _dvalue )
{
//	m_lock.lock();
	int					hr = E_FAIL;

	
	do {
		if( NULL == m_pstmt ){
			assert(0);
			break;
		}

		long		lr = 0;
		lr	= sqlite3_bind_double( m_pstmt,  _lIndex,  _dvalue  );
		if(lr != SQLITE_OK)
		{
//			CMark_Error::set( lr, EN_ERR_SQLITE );
			assert(0);
			break;
		}
		

		hr = S_OK;

	} while( false );

//	m_lock.unlock();

	return	hr;

}

HRESULT		CSQL_query::Bind(long _lIndex )
{
//	m_lock.lock();
	int					hr = E_FAIL;

	
	do {
		if( NULL == m_pstmt ){
			assert(0);
			break;
		}

		long		lr = 0;
		lr	= sqlite3_bind_null( m_pstmt,  _lIndex );
		if(lr != SQLITE_OK)
		{
//			CMark_Error::set( lr, EN_ERR_SQLITE );
			assert(0);
			break;
		}
		

		hr = S_OK;

	} while( false );

//	m_lock.unlock();

	return	hr;

}

HRESULT		CSQL_query::Bind(long _lIndex, long _lvalue )
{
//	m_lock.lock();

	int					hr = E_FAIL;


	do {
		if( NULL ==  m_pstmt ){
			assert(0);
			break;
		}
		long	lr = 0;
		lr	= sqlite3_bind_int( m_pstmt,  _lIndex,  _lvalue  );
		if(lr != SQLITE_OK)
		{
//			CMark_Error::set( lr, EN_ERR_SQLITE );
			assert(0);
			break;
		}
		

		hr = S_OK;

	} while( false );

//	m_lock.unlock();

	return	hr;

}




HRESULT		CSQL_query::GetData()
{
	return	S_OK;
}










//====================================================================================
//____________________________________________________________________________________
//	CSQL_session_base.

//____________________________________________________________________________________
//	Public methods.


CSQL_session_base::~CSQL_session_base()
{
	Disconnect();
}




HRESULT CSQL_session_base::Connect(const wchar_t* _pwcs_db_folder,  const wchar_t* _pwcs_db_file,  int _buffSize)
{
	HRESULT				hr			= E_FAIL;
	
	const size_t		pw_len		= 128;
	wchar_t*			pw			= new wchar_t[pw_len];
	
	
	do {
		if( NULL != m_pdb){
			assert(0);
			break;
		}

		wcscpy_as(pw, pw_len, _pwcs_db_folder);
		wcscat_as(pw, pw_len, L"\\");
		wcscat_as(pw, pw_len, _pwcs_db_file);
		
		hr	= sqlite3_open16(pw, &m_pdb);
		if( SQLITE_OK != hr ){
//			CMark_Error::set( hr, EN_ERR_SQLITE );
			assert(0);
			hr = E_FAIL;
			break;
		}
				
		hr = S_OK;
	}	while (false);
	
	delete[]	pw;
		
	if (FAILED(hr))		Disconnect();
		
	return	hr;
}




HRESULT CSQL_session_base::Disconnect()
{
	int					hr	= E_FAIL;;
	long				lr  = 0;	
	
	do {
		
		if( NULL == m_pdb ){
			assert(0);
			break;
		}

		lr	= sqlite3_close(m_pdb);
		if (lr != SQLITE_OK){
//			CMark_Error::set( lr, EN_ERR_SQLITE );
			assert(0);
			break;
		}
		
		hr	= S_OK;		
	}	while (false);
	
	
	return	hr;
}








//====================================================================================
//____________________________________________________________________________________
//	CSQL_session.

//____________________________________________________________________________________
//	Public methods.


CSQL_session::~CSQL_session()
{

	long lSize = m_vec_qry.size();
	vector<CSQL_query*>::iterator it = m_vec_qry.begin();
//	for (vector<CSQL_query*>::iterator i = m_vec_qry.begin(); i != m_vec_qry.end(); i++) 
	for( long i = 0; i < lSize; i++ )
	{
			delete	(*it);
			it++;
//			m_vec_qry.erase(it);
	}

// 	while (m_vec_qry.size() > 0) {
// 		delete	m_vec_qry.back();
// 		m_vec_qry.pop_back();
// 	}
	m_vec_qry.clear();

	delete[]	m_name;
}




wchar_t*	CSQL_session::GetName()
{
	return	m_name;
}




HRESULT		CSQL_session::Connect(const wchar_t* _pname,  const wchar_t* _pfolder,  const wchar_t* _pfile,  int _buffSize)
{
//	MarkCSLocker cslock( m_cs );
	HRESULT				hr			= E_FAIL;
	
	
	do {
		if (m_refCnt == 0) {
			hr	= CSQL_session_base::Connect(_pfolder, _pfile, _buffSize);
			if (FAILED(hr))		break;
			
			int			l			= wcslen(_pname);
			
			if ( NULL != m_name )
			{
				delete [] m_name;
				m_name	=	NULL;
			}
			m_name	= new wchar_t[l + 2];
			wcscpy_as(m_name, l, _pname);
			m_name[l]	= 0;			
		}
		
		AddRef();

		hr = S_OK;

	}	while (false);
	
	
	return	hr;
}




void		CSQL_session::DisConnect(bool* _pb)
{
//	MarkCSLocker cslock( m_cs );
	if (--m_refCnt == 0) {	*_pb	= true;		delete this;	}
	else					*_pb	= false;
}




HRESULT		CSQL_session::Query_Create	(int* _pqh,  CSQL_query** _ppq)
{
//	MarkCSLocker cslock( m_cs );
	HRESULT				hr		= E_FAIL;
	
	
	do {
 		if( NULL == m_pdb && NULL != _ppq  ){
			assert(0);
			break;
		}

		*_ppq	= new CSQL_query(m_pdb, ++m_maxcount);
		if (*_ppq == 0)	break;
		
		m_vec_qry.push_back(*_ppq);
		
		
		if (_pqh)		*_pqh	= m_maxcount;
		
		hr	= S_OK;
		
		
	}	while (false);
	
	
	return	hr;
}




HRESULT		CSQL_session::Query_Delete	(int _qh)
{
//	MarkCSLocker cslock( m_cs );
	HRESULT				hr			= E_FAIL;
	
	
	for (vector<CSQL_query*>::iterator i = m_vec_qry.begin(); i != m_vec_qry.end(); i++) {
		if ((*i)->GetQh() == _qh) {
			delete	(*i);
			m_vec_qry.erase(i);
//			(*i)->Finalize();
			hr	= S_OK;
			
			break;
		}
	}
	
	
	return	hr;
}




HRESULT		CSQL_session::Query_Get		(int _qh,  CSQL_query** _ppq)
{
	HRESULT				hr			= E_FAIL;
	
	
	*_ppq	= 0;
	
	
	for (vector<CSQL_query*>::iterator i = m_vec_qry.begin(); i != m_vec_qry.end(); i++) {
		if ((*i)->GetQh() == _qh) {
			*_ppq	= (*i);
			
			
			hr	= S_OK;
			
			
			break;
		}
	}
	
	
	return	hr;
}








//====================================================================================
//____________________________________________________________________________________
//	CSQL_sessionManager.

//____________________________________________________________________________________
//	Public methods.


CSQL_sessionManager::~CSQL_sessionManager()
{
	while (m_vec_session.size() > 0) {
		delete	m_vec_session.back();
		m_vec_session.pop_back();
	}
	m_vec_session.clear();
	
	CSQL_sessionManager::m_this		= 0;
}




HRESULT		CSQL_sessionManager::Session_Connect		(const wchar_t* _pname,  const wchar_t* _pfolder,  const wchar_t* _pfile,  CSQL_session** _ppsession)
{
	return	Session_Connect_base (_pname, _pfolder, _pfile, 256,  _ppsession);
}

HRESULT		CSQL_sessionManager::Session_Connect		(const wchar_t* _pname,  const wchar_t* _pfolder,  const wchar_t* _pfile,  int _buffSize,  CSQL_session** _ppsession)
{
	return	Session_Connect_base (_pname, _pfolder, _pfile, _buffSize,  _ppsession);
}

HRESULT		CSQL_sessionManager::Session_Connect_base	(const wchar_t* _pname,  const wchar_t* _pfolder,  const wchar_t* _pfile,  int _buffSize,  CSQL_session** _ppsession)
{
	HRESULT				hr			= E_FAIL;
//	MarkCSLocker cslock( m_cs );
	
	do {
		*_ppsession	= 0;
		
		for (size_t i = 0; i < m_vec_session.size(); i++) {
			if (wcscmp(m_vec_session[i]->GetName(), _pname) == 0) {
				m_vec_session[i]->AddRef();
				*_ppsession	= m_vec_session[i];
				break;
			}
		}
		
		if (!*_ppsession) {
			*_ppsession	= new CSQL_session();
			if (*_ppsession == 0)	break;
			
			hr	= (*_ppsession)->Connect(_pname, _pfolder, _pfile, _buffSize);
			if (FAILED(hr))			break;
			
			
			m_vec_session.push_back(*_ppsession);
		}
		
		
		hr	= S_OK;
		
		
	}	while (false);
	
	
	if (FAILED(hr))	
	{
		delete	*_ppsession;
		*_ppsession	=	NULL;
	}
	
	
	return	hr;
}




HRESULT		CSQL_sessionManager::Session_DisConnect(const wchar_t* _pname, BOOL* pbIsDBClosed)
{	
//	MarkCSLocker cslock( m_cs );
	*pbIsDBClosed		=	 FALSE;

	for (vector<CSQL_session*>::iterator i = m_vec_session.begin(); i != m_vec_session.end(); i++) {
		if (wcscmp(	(*i)->GetName(), _pname) == 0) {
			bool		b;
			(*i)->DisConnect(&b);
			
			if (b)	
			{
				m_vec_session.erase(i);
				*pbIsDBClosed = b;
			}
			break;
		}
	}
	
	
	return	S_OK;
}




