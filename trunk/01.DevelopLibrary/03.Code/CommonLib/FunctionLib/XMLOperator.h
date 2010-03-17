// XMLOperator.h: interface for the CXMLOperator class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_XMLOPERATOR_H__D603B44E_F990_434C_8D69_425DE9669323__INCLUDED_)
#define AFX_XMLOPERATOR_H__D603B44E_F990_434C_8D69_425DE9669323__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "tinyxml.h"

class CXMLOperator  
{
	public:

		CXMLOperator();

		virtual ~CXMLOperator();

	public:
		//父子关系:'/';兄弟关系:'#';属性:'@';Text:%
		//Example:
		//<request>
		//	<list>	bbbbb	</list>
		//	<data type = "group" kind = "sms"> aaaaaa	</data>
		//</request>
		//
		// request/list%bbbbb#data@type=group@kind=sms%aaaaaa
		HRESULT		makeXML( string	&strPath, string &dataStream  );		


	
	private:
		
		void		clear( vector<TiXmlElement*> &vecTiXml );

	private:



};

#endif // !defined(AFX_XMLOPERATOR_H__D603B44E_F990_434C_8D69_425DE9669323__INCLUDED_)
