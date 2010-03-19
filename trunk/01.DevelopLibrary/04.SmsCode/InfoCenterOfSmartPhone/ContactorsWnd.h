
#ifndef __ContactorsWnd_h__
#define __ContactorsWnd_h__


#include ".\\Sqlite\\CSearch_SQL_base_handler.h"

#define   RLH_FAIL   -1
#define   RLH_OK	 0

#define   SQL_GET_CONTACTS  L"select ABPerson.Name , ABPhones.*  from ABPerson, ABPhones where ABPerson.ROWID = ABPhones.record_id "

#define MZ_IDC_LIST      101
#define MZ_IDC_TOOLBAR1    102
#define IDC_PPM_OK      103
#define IDC_PPM_CANCEL    104
#define MZ_IDC_SCROLLWIN  105

#include "resource.h"

// �б�����Զ�������
class MyListItemData
{
public:
  CMzString StringTitle;  // ������ı�
  CMzString StringDescription;  // ��������ı�
  BOOL Selected; // ���Ƿ�ѡ��

};

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
  void MultiSelectItem(int nIndex, bool bSelect)
  {
    ListItem* pItem = GetItem(nIndex);
    if(pItem)
    {
      MyListItemData* mlid = (MyListItemData*)pItem->Data;
      if(mlid)
          mlid->Selected = bSelect; 
    }
  }

  // �ж�ĳһ���Ƿ�ѡ��
  bool IsMultiSelect(int nIndex)
  {
    ListItem* pItem = GetItem(nIndex);
    if(pItem)
    {
      MyListItemData* mlid = (MyListItemData*)pItem->Data;
      if(mlid)
          return mlid->Selected; 
    }
    return false;
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
	CContactorsWnd()
	{
	}
  UiToolbar_Text m_Toolbar;
  
  // �б�ؼ�
  MyList m_List;

protected:
  // ���ڵĳ�ʼ��
  virtual BOOL OnInitDialog()
  {
    // �����ȵ��û���ĳ�ʼ��
    if (!CMzWndEx::OnInitDialog())
    {
      return FALSE;
    }

	// ��ʼ�������еĿؼ�

    m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR);
    m_List.SetID(MZ_IDC_LIST);
    m_List.EnableScrollBarV(true);
    m_List.EnableNotifyMessage(true);
    m_List.SetItemHeight(90);
    m_List.SetTextColor(RGB(255,0,0));
    AddUiWin(&m_List);

    m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR);
    m_Toolbar.SetButton(0, true, true, L"Exit");
    m_Toolbar.SetButton(1, true, true, L"Delete");
    m_Toolbar.SetButton(2, true, true, L"Setting");
    m_Toolbar.SetID(MZ_IDC_TOOLBAR1);
    AddUiWin(&m_Toolbar);

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
    ListItem li;
	int i = 0;

	while ( hr != E_FAIL && hr != S_OK )
	{
	  wchar_t* PName = NULL;
	  pq->GetField(0, &PName);


	  wchar_t* pNumber = NULL;
      pq->GetField(3, &pNumber);    CMzString strTitle(256);
	  CMzString strDescription(256);
	  wsprintf(strTitle.C_Str(), PName, i);
	  wsprintf(strDescription.C_Str(), pNumber, i);

	  // �����б�����Զ�������
	  MyListItemData *pmlid = new MyListItemData;
	  pmlid->StringTitle = strTitle;
	  pmlid->StringDescription = strDescription;
	  pmlid->Selected = false;

	  // �б�����Զ�������ָ�����õ�ListItem::Data
	  li.Data = pmlid;
	  m_List.AddItem(li);

	  hr = pq->Step();
	  i++;
	}




	


    return TRUE;
  }

  // ���� MZFC ����Ϣ������
  LRESULT MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam)
  {
    switch(message)
    {
    case MZ_WM_MOUSE_NOTIFY:
      {
        int nID = LOWORD(wParam);
        int nNotify = HIWORD(wParam);
        int x = LOWORD(lParam);
        int y = HIWORD(lParam);

        // �����б�ؼ�����갴��֪ͨ
        if (nID==MZ_IDC_LIST && nNotify==MZ_MN_LBUTTONDOWN)
        {
          if (!m_List.IsMouseDownAtScrolling() && !m_List.IsMouseMoved())
          {
            int nIndex = m_List.CalcIndexOfPos(x, y);
            m_List.SetSelectedIndex(nIndex);
            m_List.Invalidate();
            m_List.Update();
          }
          return 0;
        }

        // �����б�ؼ�������ƶ�֪ͨ
        if (nID==MZ_IDC_LIST && nNotify==MZ_MN_MOUSEMOVE)
        {
		  if(m_List.GetSelectedIndex()!=-1)
		  {
            m_List.SetSelectedIndex(-1);
            m_List.Invalidate();
            m_List.Update();
		  }
          return 0;
        }
        //if (nID==MZ_IDC_LIST && nNotify==MZ_MN_LBUTTONDOWN)
        //{
        //  return 0;
        //}
      }
      return 0;
    }
    return CMzWndEx::MzDefWndProc(message,wParam,lParam);
  }

  // ���� MZFC ��������Ϣ������
  virtual void OnMzCommand(WPARAM wParam, LPARAM lParam)
  {
    UINT_PTR id = LOWORD(wParam);
    switch(id)
    {
    case MZ_IDC_TOOLBAR1:
      {
        int nIndex = lParam;
        if (nIndex==0)
        {
          //CMzString str(128);
          //wsprintf(str.C_Str(), L"You pressed the %s button!", m_Toolbar.GetButtonText(0).C_Str());
          //MzMessageBoxEx(m_hWnd, str.C_Str(), L"Test", MB_OK);
          PostQuitMessage(0);
          return;
        }
        if (nIndex==1)
        {
          // �����˵�
          CPopupMenu ppm;
          struct PopupMenuItemProp pmip;      

          pmip.itemCr = MZC_BUTTON_PELLUCID;
          pmip.itemRetID = IDC_PPM_CANCEL;
          pmip.str = L"Cancel";
          ppm.AddItem(pmip);

          pmip.itemCr = MZC_BUTTON_ORANGE;
          pmip.itemRetID = IDC_PPM_OK;
          pmip.str = L"OK";
          ppm.AddItem(pmip);  

          RECT rc = MzGetWorkArea();      
          rc.top = rc.bottom - ppm.GetHeight();
          ppm.Create(rc.left,rc.top,RECT_WIDTH(rc),RECT_HEIGHT(rc),m_hWnd,0,WS_POPUP);      
          int nID = ppm.DoModal();
          if (nID==IDC_PPM_OK)
          {
            // ɾ��ѡ�е��б���
            for (int i=m_List.GetItemCount()-1; i>=0; i--)
            {
              if(m_List.IsMultiSelect(i))
              {
                m_List.RemoveItem(i);
              }
            }

            // ˢ���б�
            m_List.Invalidate();
            m_List.Update();
          }
          if (nID==IDC_PPM_CANCEL)
          {
            // do what you want...
          }
          return;
        }
        if (nIndex==2)
        {
          //...
          return;
        }
      }
      break;
    }
  }
};


#endif //__ContactorsWnd_h__