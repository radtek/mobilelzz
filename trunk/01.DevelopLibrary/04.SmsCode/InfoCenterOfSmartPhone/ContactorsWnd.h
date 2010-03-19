
#ifndef __ContactorsWnd_h__
#define __ContactorsWnd_h__


#include ".\\Sqlite\\CSearch_SQL_base_handler.h"

#define   RLH_FAIL   -1
#define   RLH_OK	 0

#define   SQL_GET_CONTACTS  L"select ABPerson.Name , ABPhones.*  from ABPerson, ABPhones where ABPerson.ROWID = ABPhones.record_id "

#define MZ_IDC_LIST      101
#define MZ_IDC_TOOLBAR1    102

#define MZ_IDC_SCROLLWIN  105

#include "resource.h"

#include"UiEditControl.h"

// 从 UiList 派生的自定义列表类
class MyList:
  public UiList
{
public:
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
        bool bSelect = IsMultiSelect(nIndex);

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
    bool bSelected = pmlid->Selected;

    

	if(g_bH)
	{
		// 绘制左边的小图像
		ImagingHelper* pimg = m_ImageContainer.LoadImage(MzGetInstanceHandle(), IDR_RCDATA1, true);
		RECT rcImg = *prcItem;
		rcImg.right = rcImg.left + 30;
		if (pimg)
		{
		  pimg->Draw(hdcDst, &rcImg, false, false);
		}
		 // 绘制主文本
		RECT rcText = *prcItem;
		rcText.left = rcImg.right+40;
		rcText.right = 300;
		::SetTextColor(hdcDst, RGB(0,200,0));
		rcText.top -=10;
		MzDrawText(hdcDst, pmlid->StringTitle.C_Str(), &rcText, DT_LEFT|DT_BOTTOM|DT_SINGLELINE|DT_END_ELLIPSIS);

		// 绘制描述文本
		rcText.left = rcText.left+rcText.right;
		rcText.right = 680;
		rcText.top +=15;
		::SetTextColor(hdcDst, RGB(200,200,200));
		MzDrawText(hdcDst, pmlid->StringDescription.C_Str(), &rcText, DT_LEFT|DT_TOP|DT_SINGLELINE|DT_END_ELLIPSIS);
	}
	else
	{
		// 绘制左边的小图像
		ImagingHelper* pimg = m_ImageContainer.LoadImage(MzGetInstanceHandle(), IDR_RCDATA1, true);
		RECT rcImg = *prcItem;
		rcImg.right = rcImg.left + MZM_MARGIN_MAX*2;
		if (pimg)
		{
		  pimg->Draw(hdcDst, &rcImg, false, false);
		}
		// 绘制主文本
		RECT rcText = *prcItem;
		rcText.left = rcImg.right;
		rcText.bottom = rcText.top + RECT_HEIGHT(rcText)/2;
		::SetTextColor(hdcDst, RGB(0,200,0));
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
    MzDrawControl(hdcDst, &rcSelectedIcon,  bSelected?MZC_SELECTED:MZC_UNSELECTED, 0);
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
	//CContactorsWnd(UiEditControl* pUIEditControl)
	//{
	//	m_pUIEditControl = pUIEditControl;
	//}
  UiToolbar_Text m_Toolbar;
  //UiEditControl* m_pUIEditControl;
  // 列表控件
  MyList m_List;
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
};


#endif //__ContactorsWnd_h__