// contect_sqlite.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "CSearch_SQL_base_handler.h"

#define   RLH_FAIL   -1
#define   RLH_OK	 0

#define   SQL_GET_CONTACTS  L"select ABPerson.Name , ABPhones.*  from ABPerson, ABPhones where ABPerson.ROWID = ABPhones.record_id "



int main(int argc, char* argv[])
{
	CSQL_sessionManager*  pm =CSQL_sessionManager::GetInstance();
	if( NULL == pm ) return RLH_FAIL;

	CSQL_session*  pSession = NULL;

	HRESULT hr = pm->Session_Connect(L"contact", L".\\", L"contacts.db", &pSession );
	if(FAILED(hr) || pSession == NULL)	return RLH_FAIL;

	CSQL_query * pq = NULL;
	int          q_id = 0;

	hr = pSession->Query_Create(&q_id, &pq );
	if( FAILED(hr) || pq == NULL ) return RLH_FAIL;

	
	
 	hr = pq->Prepare(SQL_GET_CONTACTS);
 	if( FAILED(hr) ) return RLH_FAIL;


 	hr = pq->Step();

	while ( hr != E_FAIL && hr != S_OK )
	{
		wchar_t* PName = NULL;
		pq->GetField(0, &PName);


		wchar_t* pNumber = NULL;
		pq->GetField(3, &pNumber);

		
		hr = pq->Step();
	}




	










	

	printf("Hello World!\n");
	return 0;
}

