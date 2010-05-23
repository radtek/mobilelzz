#ifndef __RecieversStringParser_h__
#define __RecieversStringParser_h__

class CRecieversStringParser
{
public:
	CRecieversStringParser();
	virtual~ CRecieversStringParser();

public:
	//void Initialize(UiWin* pSourceControl);

	// add delete
	void UpdateStringByContactors();
	void UpdateRecievers();
	void UpdateContactors();
	
	//if not need,do nothing
	void AddSeparator(long lCurcorPos);

	//if Contactor is user input,range is invalid
	void GetContactorRangeByPos(long lCurcorPos, 
									long& lBeginPos, long& lEndPos);
	
	//if delete user input number,do nothing,then delete contactor
	//if delete Separator,delete until before Separator
	long DeleteContentByPos(long lCurcorPos);

	wchar_t* GetWStringBuf(long& lWSize);
	//long CalcPosByRowCol(long lRow, long lCol);

	BOOL IsContactor(long lBeginPos, long lEndPos);
private:
	long FindBoundaryByPos(long lCurcorPos, Move_Direction enDirection);
	void MoveChars(long lCurcorPos, Move_Direction enDirection, long lStepLength);
	BOOL IsNumbers(long lBeginPos, long lEndPos);
	BOOL IsNumbers(wchar_t* pwcsWChars);
	BOOL IsContactor(wchar_t* pwcsContactor, long& lMatchIndex);
	
private:
	long			m_lWSize;
	wchar_t			m_wcsStringNeedParsed[UI_Recievers_String_Length];
};


#endif //__RecieversStringParser_h__