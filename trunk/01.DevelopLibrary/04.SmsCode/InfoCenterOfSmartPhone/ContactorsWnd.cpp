#include"stdafx.h"
#include"UiEditControl.h"
#include"ContactorsWnd.h"

BOOL CContactorsWnd::OnInitDialog()
{
	if(m_bInit)
	{
		return TRUE;
	}
	// 必须先调用基类的初始化
	if (!CMzWndEx::OnInitDialog())
	{
	  return FALSE;
	}
	//打开重力感应设备
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
		g_bH = TRUE;
		long lWidth = GetWidth();
		long lHeight = GetHeight();
		RECT rc = MzGetWindowRect();
		RECT rc2 = MzGetClientRect();
		RECT rc3 = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc3.left, rc3.top,RECT_WIDTH(rc3), RECT_HEIGHT(rc3)  );
		lWidth = GetWidth();
		lHeight = GetHeight();
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720);
		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR_w720);
		m_List.SetItemHeight(70);

		//m_AlpBar.SetPos(650,0,70,GetHeight());
	}
	else
	{
		g_bH = FALSE;
		long lWidth = GetWidth();
		long lHeight = GetHeight();
		RECT rc = MzGetWindowRect();
		RECT rc2 = MzGetClientRect();
		RECT rc3 = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc3.left, rc3.top,RECT_WIDTH(rc3), RECT_HEIGHT(rc3)  );
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR);
		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR);
		m_List.SetItemHeight(90);

		//m_AlpBar.SetPos(430,0,50,GetHeight());
	}
	
	m_List.SetID(MZ_IDC_LIST);
	m_List.EnableScrollBarV(true);
	m_List.EnableNotifyMessage(true);
	
	m_List.SetTextColor(RGB(255,0,0));
	AddUiWin(&m_List);

	
    //m_AlpBar.SetID(MZ_IDC_ALPBAR);
    //m_AlpBar.EnableZoomAlphabet(true);
    //m_AlpBar.EnableNotifyMessage(true);
    //AddUiWin(&m_AlpBar);

	
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
		pq->GetField(1, &PName);
		long lPID = 0;
		pq->GetField(0, &lPID);
		wchar_t* pNumber = NULL;
		pq->GetField(4, &pNumber);    CMzString strTitle(256);
		CMzString strDescription(256);
		wsprintf(strTitle.C_Str(), PName, i);
		wsprintf(strDescription.C_Str(), pNumber, i);

		// 设置列表项的自定义数据
		MyListItemData *pmlid = new MyListItemData;
		pmlid->StringTitle = strTitle;
		pmlid->StringDescription = strDescription;
		pmlid->Selected = false;
		pmlid->lPID = lPID;
		UpdateItem(pmlid);
		// 列表项的自定义数据指针设置到ListItem::Data
		li.Data = pmlid;
		m_List.AddItem(li);

		hr = pq->Step();
		i++;
	}

	bool DisConect  = false;

#if 0 
	pq->Finalize();
	pSession->DisConnect(&DisConect);

	pSession->Query_Delete(q_id);
	
	pm->ReleaseInstance();
	
#endif 
	m_bInit = TRUE;
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
				m_pParent->UpdateData(0);
				EndModal(ID_CANCEL);  
			  return;
			}
			if (nIndex==2)
			{
				g_ReciversList.Clear();
				long lCount = m_List.GetItemCount();
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
							g_ReciversList.AppendItem(mlid);					
						}
					  }				   
					}			
				}
				
				m_pParent->UpdateData(1);
				EndModal(ID_CANCEL); 
				//MzOpenSip(IM_SIP_MODE_KEEP,0);
			  return;
			}

		}
		break;
		case MZ_IDC_ALPBAR:
		{
			//if(m_AlpBar.GetCurLetter())
			//{
			//	for (int i=0;i<m_List.GetItemCount();i++)
			//	{
			//		MyListItemData* itemData = (MyListItemData*)m_List.GetItem(i)->Data;
			//		wchar_t* fLetter = itemData->wcsfirstLetter;
			//		//将找到的列表项显示在屏幕顶端。
			//		if (wcscmp(m_AlpBar.GetCurLetter(),fLetter) == 0)
			//		{
			//		  int topPos = m_List.CalcItemTopPos(i);
			//		  m_List.SetTopPos(m_List.GetTopPos()-topPos);
			//		  m_List.Invalidate();
			//		  m_List.Update();
			//		  break;
			//		}
			//	}
			//}
		}
	    break;

		default:
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


  if (devMode.dmDisplayOrientation == DMDO_180 || devMode.dmDisplayOrientation == DMDO_0)
  {
		g_bH = TRUE;
		RECT rc = MzGetWorkArea();
		//modify by zhaodsh  begin at 2010/03/21 12:45
		//SetWindowPos(m_hWnd, rc.left, rc.top,RECT_HEIGHT(rc)+rc.top, RECT_WIDTH(rc)  );
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc)  );
		//modify by zhaodsh  end at 2010/03/21 12:45
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720);
		m_List.SetItemHeight(70);

		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR_w720,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR_w720);
  }

    if (devMode.dmDisplayOrientation == DMDO_90 || devMode.dmDisplayOrientation == DMDO_270)
	  
	{
		g_bH = FALSE;
		RECT rc = MzGetWorkArea();
		SetWindowPos(m_hWnd, rc.left, rc.top,RECT_WIDTH(rc), RECT_HEIGHT(rc) );
		m_List.SetPos(0,0,GetWidth(),GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR);
		m_List.SetItemHeight(90);

		m_Toolbar.SetPos(0,GetHeight()-MZM_HEIGHT_TEXT_TOOLBAR,GetWidth(),MZM_HEIGHT_TEXT_TOOLBAR);
	}
}

void CContactorsWnd::UpdateItem(MyListItemData* pmlid)
{
	long lCount = g_ReciversList.GetItemCount();
	for(int i = 0; i < lCount; i++)
	{
		BOOL b = pmlid->Compare(g_ReciversList.GetItem(i));
		if(b)
		{
			pmlid->Selected = g_ReciversList.GetItem(i)->Selected;
		}
	}
}