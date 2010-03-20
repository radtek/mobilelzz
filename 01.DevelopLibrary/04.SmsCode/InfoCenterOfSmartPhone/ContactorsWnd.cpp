#include"stdafx.h"
#include"UiEditControl.h"
#include"ContactorsWnd.h"

BOOL CContactorsWnd::OnInitDialog()
{
	// 必须先调用基类的初始化
	if (!CMzWndEx::OnInitDialog())
	{
	  return FALSE;
	}
	//打开重力感应设备
	MzAccOpen();
	m_accMsg = MzAccGetMessage();
	// 初始化窗口中的控件
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
		long lWidth = GetWidth();
		long lHeight = GetHeight();
		RECT rc = MzGetWindowRect();
		RECT rc2 = MzGetClientRect();
		RECT rc3 = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_HEIGHT(rc)+rc.top,RECT_WIDTH(rc)  );
		lWidth = GetWidth();
		lHeight = GetHeight();
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720);
		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720-rc.top,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR_w720);
		m_List.SetItemHeight(70);
	}
	else
	{
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR);
		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR);
		m_List.SetItemHeight(90);
	}
	
	m_List.SetID(MZ_IDC_LIST);
	m_List.EnableScrollBarV(true);
	m_List.EnableNotifyMessage(true);
	
	m_List.SetTextColor(RGB(255,0,0));
	AddUiWin(&m_List);

	
	m_Toolbar.SetButton(0, true, true, L"取消");
	m_Toolbar.SetButton(2, true, true, L"确认");
	//m_Toolbar.SetButton(2, true, true, L"Setting");
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

	  // 设置列表项的自定义数据
	  MyListItemData *pmlid = new MyListItemData;
	  pmlid->StringTitle = strTitle;
	  pmlid->StringDescription = strDescription;
	  pmlid->Selected = false;

	  // 列表项的自定义数据指针设置到ListItem::Data
	  li.Data = pmlid;
	  m_List.AddItem(li);

	  hr = pq->Step();
	  i++;
	}

	return TRUE;
}
// 重载 MZFC 的消息处理函数
LRESULT CContactorsWnd::MzDefWndProc(UINT message, WPARAM wParam, LPARAM lParam)
{
	switch(message)
	{
		case MZ_WM_MOUSE_NOTIFY:
			{
				int nID = LOWORD(wParam);
				int nNotify = HIWORD(wParam);
				int x = LOWORD(lParam);
				int y = HIWORD(lParam);

				// 处理列表控件的鼠标按下通知
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

				// 处理列表控件的鼠标移动通知
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

			}
		default:
		  {
			if (message == m_accMsg)
			{
				// 转屏
			  switch(wParam)
			  {
					case SCREEN_PORTRAIT_P:
					{
						MzChangeDisplaySettingsEx(DMDO_90);
						g_bH = TRUE;
					}
					break;
				  case SCREEN_PORTRAIT_N:
					{
						MzChangeDisplaySettingsEx(DMDO_270);
						g_bH = FALSE;
					}
					break;
				  case SCREEN_LANDSCAPE_N:
					{
						MzChangeDisplaySettingsEx(DMDO_180);
						g_bH = TRUE;
					}
					break;
				  case SCREEN_LANDSCAPE_P:
					{
						MzChangeDisplaySettingsEx(DMDO_0);
						g_bH = FALSE;
					}
				break;
			  }
			}	    
		  }
		break;
	}

	return CMzWndEx::MzDefWndProc(message,wParam,lParam);
}

  // 重载 MZFC 的命令消息处理函数
void CContactorsWnd::OnMzCommand(WPARAM wParam, LPARAM lParam)
{
UINT_PTR id = LOWORD(wParam);
switch(id)
{
case MZ_IDC_TOOLBAR1:
  {
    int nIndex = lParam;
    if (nIndex==0)
    {
      
		EndModal(ID_CANCEL);  
      return;
    }
    if (nIndex==2)
    {
		long lCount = m_List.GetItemCount();
		MyListItemData* pRecievers = new MyListItemData[lCount];
		
		long lValidCount = 0;
		for(int i = 0; i < lCount; i++)
		{
			ListItem* pItem = m_List.GetItem(i);
			if(pItem)
			{
			  MyListItemData* mlid = (MyListItemData*)pItem->Data;
			  if(mlid)
			  {
				if(mlid->Selected)
				{
					pRecievers[lValidCount] = *mlid;
					lValidCount++;						
				}

			  }
				   
			}
			
		}
		
		m_pParent->UpdateData(pRecievers, lValidCount);
		EndModal(ID_CANCEL);  
      return;
    }
    
  }
  break;
}
}
// 转屏后如果需要调整窗口的位置，重载此函数响应 WM_SETTINGCHANGE 消息
void CContactorsWnd::OnSettingChange(DWORD wFlag, LPCTSTR pszSectionName)
{
  //设置新的屏幕方向的窗口大小及控件位置
  DEVMODE  devMode;
  memset(&devMode, 0, sizeof(DEVMODE));
  devMode.dmSize = sizeof(DEVMODE);
  devMode.dmFields = DM_DISPLAYORIENTATION;
  ChangeDisplaySettingsEx(NULL, &devMode, NULL, CDS_TEST, NULL);

  //横屏
  if (devMode.dmDisplayOrientation == DMDO_90 || devMode.dmDisplayOrientation == DMDO_270)
  {
		g_bH = TRUE;
		RECT rc = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_HEIGHT(rc)+rc.top, RECT_WIDTH(rc)  );
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720);
		m_List.SetItemHeight(70);

		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR_w720);
  }

  //竖屏
	if (devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0)
	{
		g_bH = FALSE;
		RECT rc = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR);
		m_List.SetItemHeight(90);

		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR);
	}
}
