
#ifndef __ContactorsWnd_h__
#define __ContactorsWnd_h__


#include ".\\Sqlite\\CSearch_SQL_base_handler.h"

#define   RLH_FAIL   -1
#define   RLH_OK	 0

#define   SQL_GET_CONTACTS  L"select ABPerson.ROWID, ABPerson.Name , ABPhones.*  from ABPerson, ABPhones where ABPerson.ROWID = ABPhones.record_id "
#define   SQL_GET_FIRSTLETER  L"select token  from ABLookupFirstLetter where source = ?"

#define MZ_IDC_LIST      101
#define MZ_IDC_TOOLBAR1    102

#define MZ_IDC_SCROLLWIN  105

#define MZ_IDC_ALPBAR	106

#include "resource.h"

#include"UiEditControl.h"

// 从 UiList 派生的自定义列表类
class MyList:
  public UiList
{
public:
	MyList()
	{
	}
	virtual ~MyList()
	{
		m_ImageContainer.RemoveAll();
	}
  // 当某一项即将被删除之前，delete 其自定义项数据
  virtual void OnRemoveItem(int nIndex)
  {
    ListItem* pItem = GetItem(nIndex);
    if(pItem)
    {
      MyListItemData* mlid = (MyListItemData*)pItem->Data;
      if(mlid)
          delete mlid; 
    }
  }

  // 选中、不选中某一项
  BOOL MultiSelectItem(int nIndex, bool bSelect)
  {
    ListItem* pItem = GetItem(nIndex);
    if(pItem)
    {
      MyListItemData* mlid = (MyListItemData*)pItem->Data;
      if(mlid)
          mlid->Selected = bSelect; 
    }
	return FALSE;
  }

  // 判断某一项是否被选中
  BOOL IsMultiSelect(int nIndex)
  {
    ListItem* pItem = GetItem(nIndex);
    if(pItem)
    {
      MyListItemData* mlid = (MyListItemData*)pItem->Data;
      if(mlid)
          return mlid->Selected; 
    }
    return FALSE;
  }

  // 在 OnLButtonUp 中改变项的选中状态
  virtual int OnLButtonUp(UINT fwKeys, int xPos, int yPos)
  {
    // 获得控件在鼠标放开前的一些鼠标的相关状态（在调用 UiLst::OnLButtonUp()）
    bool b1 = IsMouseDownAtScrolling();
    bool b2 = IsMouseMoved();

    int Ret = UiList::OnLButtonUp(fwKeys, xPos, yPos);

    if((!b1) && (!b2))
    {
      // 计算鼠标所在位置的项的索引
      int nIndex = CalcIndexOfPos(xPos, yPos);
      if(nIndex>=0)
      {
        BOOL bSelect = IsMultiSelect(nIndex);

        // 改变项的选中状态
        MultiSelectItem(nIndex, !bSelect);

        // 刷新项
        InvalidateItem(nIndex);
        Update();
      }
    }
    return Ret;
  }

  // 重载DrawItem，自定义每一项的绘制
  virtual void DrawItem(HDC hdcDst, int nIndex, RECT* prcItem, RECT *prcWin, RECT *prcUpdate)
  {
    ListItem* pItem = GetItem(nIndex);
    if (pItem==0)
      return;
    if (pItem->Data==0)
      return;

    // 获得自定义的项数据 MyListItemData
    MyListItemData *pmlid = (MyListItemData*)pItem->Data;

    // 是否被选中
    BOOL bSelected = pmlid->Selected;

	RECT rc = {0};
	int height = 0;
	int width = 0;
	HWND hWnd = FindWindow(L"CTaskBar", 0);
	if(hWnd != 0)
	{
		::GetWindowRect(hWnd, &rc);
		height = rc.bottom - rc.top;
		width = rc.right - rc.left;
	}

	if(width>480)
	{
		// 绘制左边的小图像
		ImagingHelper* pimg = m_ImageContainer.LoadImage(MzGetInstanceHandle(), IDR_RCDATA_N_H, true);
		RECT rcImg = *prcItem;
		rcImg.right = rcImg.left + 80;
		if (pimg)
		{
		  pimg->Draw(hdcDst, &rcImg, false, false);
		}
		 // 绘制主文本
		RECT rcText = *prcItem;
		rcText.left = rcImg.right+15;
		rcText.right = 300;

		::SetTextColor(hdcDst, RGB(0,0,0));
		
		MzDrawText(hdcDst, pmlid->StringTitle.C_Str(), &rcText, DT_LEFT|DT_VCENTER|DT_SINGLELINE|DT_END_ELLIPSIS);

		// 绘制描述文本
		rcText.left = rcText.right + 20;
		rcText.right = 580;
		::SetTextColor(hdcDst, RGB(200,200,200));
		MzDrawText(hdcDst, pmlid->StringDescription.C_Str(), &rcText, DT_LEFT|DT_VCENTER|DT_SINGLELINE|DT_END_ELLIPSIS);
	}
	else
	{
		// 绘制左边的小图像
		ImagingHelper* pimg = m_ImageContainer.LoadImage(MzGetInstanceHandle(), IDR_RCDATA_N_V, true);
		RECT rcImg = *prcItem;
		rcImg.right = rcImg.left + MZM_MARGIN_MAX*2;
		if (pimg)
		{
		  pimg->Draw(hdcDst, &rcImg, false, false);
		}
		// 绘制主文本
		RECT rcText = *prcItem;
		rcText.left = rcImg.right+20;
		rcText.bottom = rcText.top + RECT_HEIGHT(rcText)/2;
		//::SetTextColor(hdcDst, RGB(0,200,0));
		::SetTextColor(hdcDst, RGB(0,0,0));
		
		MzDrawText(hdcDst, pmlid->StringTitle.C_Str(), &rcText, DT_LEFT|DT_BOTTOM|DT_SINGLELINE|DT_END_ELLIPSIS);

		// 绘制描述文本
		rcText.top = rcText.bottom;
		rcText.bottom = prcItem->bottom;
		::SetTextColor(hdcDst, RGB(200,200,200));
		MzDrawText(hdcDst, pmlid->StringDescription.C_Str(), &rcText, DT_LEFT|DT_TOP|DT_SINGLELINE|DT_END_ELLIPSIS);
	}

    // 绘制选中状态图标
    RECT rcSelectedIcon = *prcItem;
    rcSelectedIcon.left = rcSelectedIcon.right - 50;
    MzDrawControl(hdcDst, &rcSelectedIcon,  bSelected? MZC_TICK :MZCV2_CHECKBOX_SELECT, 0);
  }
protected:
private:
    ImageContainer m_ImageContainer;
};
// 从 CMzWndEx 派生的主窗口类
class CContactorsWnd: public CMzWndEx
{
  MZ_DECLARE_DYNAMIC(CContactorsWnd);
public:
	CContactorsWnd()
	{
		m_bInit = FALSE;
	}
	virtual ~CContactorsWnd()
	{
	}
  UiToolbar_Text m_Toolbar;
  //UiEditControl* m_pUIEditControl;
  // 列表控件
  MyList m_List;
  UiAlphabetBar m_AlpBar;

	UiEditControl* m_pParent;
	void SetParent(UiEditControl* pParent)
	{
		m_pParent = pParent;
	}
	DWORD m_accMsg;
protected:
  // 窗口的初始化
  virtual BOOL OnInitDialog();


  // 重载 MZFC 的消息处理函数
  LRESULT MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam);
	virtual void OnMzCommand(WPARAM wParam, LPARAM lParam);
  // 转屏后如果需要调整窗口的位置，重载此函数响应 WM_SETTINGCHANGE 消息
	virtual void OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName);

	void UpdateItem(MyListItemData* pmlid);
  BOOL						m_bInit;
  
private:
	
	void MakeLetters(wchar_t* pwcsNameLetters, wchar_t* pwcsFirstLetter, CSQL_query* pQFirstLetter, long lPID );
};


#endif //__ContactorsWnd_h__