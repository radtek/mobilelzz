
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

// �� UiList �������Զ����б���
class MyList:
  public UiList
{
public:
  // ��ĳһ�����ɾ��֮ǰ��delete ���Զ���������
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

  // ѡ�С���ѡ��ĳһ��
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

  // �ж�ĳһ���Ƿ�ѡ��
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

  // �� OnLButtonUp �иı����ѡ��״̬
  virtual int OnLButtonUp(UINT fwKeys, int xPos, int yPos)
  {
    // ��ÿؼ������ſ�ǰ��һЩ�������״̬���ڵ��� UiLst::OnLButtonUp()��
    bool b1 = IsMouseDownAtScrolling();
    bool b2 = IsMouseMoved();

    int Ret = UiList::OnLButtonUp(fwKeys, xPos, yPos);

    if((!b1) && (!b2))
    {
      // �����������λ�õ��������
      int nIndex = CalcIndexOfPos(xPos, yPos);
      if(nIndex>=0)
      {
        bool bSelect = IsMultiSelect(nIndex);

        // �ı����ѡ��״̬
        MultiSelectItem(nIndex, !bSelect);

        // ˢ����
        InvalidateItem(nIndex);
        Update();
      }
    }
    return Ret;
  }

  // ����DrawItem���Զ���ÿһ��Ļ���
  virtual void DrawItem(HDC hdcDst, int nIndex, RECT* prcItem, RECT *prcWin, RECT *prcUpdate)
  {
    ListItem* pItem = GetItem(nIndex);
    if (pItem==0)
      return;
    if (pItem->Data==0)
      return;

    // ����Զ���������� MyListItemData
    MyListItemData *pmlid = (MyListItemData*)pItem->Data;

    // �Ƿ�ѡ��
    bool bSelected = pmlid->Selected;

    

	if(g_bH)
	{
		// ������ߵ�Сͼ��
		ImagingHelper* pimg = m_ImageContainer.LoadImage(MzGetInstanceHandle(), IDR_RCDATA1, true);
		RECT rcImg = *prcItem;
		rcImg.right = rcImg.left + 30;
		if (pimg)
		{
		  pimg->Draw(hdcDst, &rcImg, false, false);
		}
		 // �������ı�
		RECT rcText = *prcItem;
		rcText.left = rcImg.right+40;
		rcText.right = 300;
		::SetTextColor(hdcDst, RGB(0,200,0));
		rcText.top -=10;
		MzDrawText(hdcDst, pmlid->StringTitle.C_Str(), &rcText, DT_LEFT|DT_BOTTOM|DT_SINGLELINE|DT_END_ELLIPSIS);

		// ���������ı�
		rcText.left = rcText.left+rcText.right;
		rcText.right = 680;
		rcText.top +=15;
		::SetTextColor(hdcDst, RGB(200,200,200));
		MzDrawText(hdcDst, pmlid->StringDescription.C_Str(), &rcText, DT_LEFT|DT_TOP|DT_SINGLELINE|DT_END_ELLIPSIS);
	}
	else
	{
		// ������ߵ�Сͼ��
		ImagingHelper* pimg = m_ImageContainer.LoadImage(MzGetInstanceHandle(), IDR_RCDATA1, true);
		RECT rcImg = *prcItem;
		rcImg.right = rcImg.left + MZM_MARGIN_MAX*2;
		if (pimg)
		{
		  pimg->Draw(hdcDst, &rcImg, false, false);
		}
		// �������ı�
		RECT rcText = *prcItem;
		rcText.left = rcImg.right;
		rcText.bottom = rcText.top + RECT_HEIGHT(rcText)/2;
		::SetTextColor(hdcDst, RGB(0,200,0));
		MzDrawText(hdcDst, pmlid->StringTitle.C_Str(), &rcText, DT_LEFT|DT_BOTTOM|DT_SINGLELINE|DT_END_ELLIPSIS);

		// ���������ı�
		rcText.top = rcText.bottom;
		rcText.bottom = prcItem->bottom;
		::SetTextColor(hdcDst, RGB(200,200,200));
		MzDrawText(hdcDst, pmlid->StringDescription.C_Str(), &rcText, DT_LEFT|DT_TOP|DT_SINGLELINE|DT_END_ELLIPSIS);
	}

    // ����ѡ��״̬ͼ��
    RECT rcSelectedIcon = *prcItem;
    rcSelectedIcon.left = rcSelectedIcon.right - 50;
    MzDrawControl(hdcDst, &rcSelectedIcon,  bSelected?MZC_SELECTED:MZC_UNSELECTED, 0);
  }
protected:
private:
    ImageContainer m_ImageContainer;
};
// �� CMzWndEx ��������������
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
  // �б�ؼ�
  MyList m_List;
	UiEditControl* m_pParent;
	void SetParent(UiEditControl* pParent)
	{
		m_pParent = pParent;
	}
	DWORD m_accMsg;
protected:
  // ���ڵĳ�ʼ��
  virtual BOOL OnInitDialog();


  // ���� MZFC ����Ϣ������
  LRESULT MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam);
	virtual void OnMzCommand(WPARAM wParam, LPARAM lParam);
  // ת���������Ҫ�������ڵ�λ�ã����ش˺�����Ӧ WM_SETTINGCHANGE ��Ϣ
	virtual void OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName);
};


#endif //__ContactorsWnd_h__