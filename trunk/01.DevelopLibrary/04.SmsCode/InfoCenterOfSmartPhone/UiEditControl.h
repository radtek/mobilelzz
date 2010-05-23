#ifndef __UiEditControl_h__
#define __UiEditControl_h__

#define UiEditControl_Click		1001

class UiEditControl: public UiEdit
{
public:
	UiEditControl();

	virtual ~UiEditControl();

	void UpdateData(  long lFlag  );

	//void UpdateTextByRecievers(BOOL bIsAddChar = FALSE, long lWillPos = Invalid_4Byte);

	//virtual void  OnFocused(UiWin *pWinPrev);

	
	//zds 2010/03/21 19:39
	//int OnLButtonUp123 ( UINT  fwKeys,  int  xPos,  int  yPos );
	//zds 2010/03/21 19:39
	virtual int OnLButtonUp( UINT  fwKeys,  
		int  xPos,  
		int  yPos   
		);

	virtual int  OnKeyDown(int nVirtKey, DWORD lKeyData);
	//virtual void OnClick( size_t  nIndex );
	//virtual int OnChar( TCHAR  chCharCode, LPARAM  lKeyData );
	void UpdateRecievers();
	void UpdateContactors();
private:
	//long GetLinePos();
	void ConvertLinePos2RowCol(long lLinePos, long& lRow, long& lCol);
	void ConvertRowCol2LinePos(long lRow, long lCol, long& lLinePos);
};
#endif //__UiEditControl_h__
