package com.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class ViewList extends TabActivity
{
	//����TabHost����
	TabHost mTabHost;
	CNoteDBCtrl m_clCNoteDBCtrl = NoteWithYourMind.m_clCNoteDBCtrl;
	public static boolean m_bIsDelete = false;
	public static boolean m_bIsMoveIn = false;
	
	public static NoteListCursorAdapter m_myAdapter;
	public static Cursor m_clCursor;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);	
		m_clCursor	=	m_clCNoteDBCtrl.getMemoRootInfo();
		if(m_clCursor!=null)
		{
			startManagingCursor(m_clCursor);
			int count = m_clCursor.getCount();
			if(count>=0)
			{
//				ListAdapter listadapter = new SimpleCursorAdapter(
//						this,
//						R.layout.memolistitem,
//						clCursor,
//						new String[]{CNoteDBCtrl.KEY_detail},
//						new int[]{R.id.memoitem_memotext}
//						);
				m_myAdapter = new NoteListCursorAdapter(this,m_clCursor);
				ListView memoList = (ListView) findViewById(R.id.listviewmemo);
				memoList.setAdapter(m_myAdapter);
//zhu.t test
				memoList.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
//						View item	=	memoList.getChildAt(1);
						arg1.setOnTouchListener(new OnTouchListener()
						{
							int[] temp = new int[] { 0, 0 };
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								int eventaction = event.getAction();
								Log.i("&&&", "onTouchEvent:" + eventaction);
								int x = (int) event.getRawX();
				                int y = (int) event.getRawY();
				                switch (eventaction)
				                {
				                	case MotionEvent.ACTION_DOWN:
				                        temp[0] = (int) event.getX();
				                        temp[1] = y - v.getTop();
				                        break;
				                	case MotionEvent.ACTION_MOVE:
				                        v.layout(x - temp[0], y - temp[1], x + v.getWidth() - temp[0], y - temp[1] + v.getHeight());
				                        v.postInvalidate();
				                        break;
				                    case MotionEvent.ACTION_UP:
				                        break;
				                }
								return false;
							}	
						});	
					}
					
				});
				
//				
				
			}
		}
		Button clBTMemoR = (Button) findViewById(R.id.B_view_memo_return);
		clBTMemoR.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{      		
        		ViewList.this.finish();
        	}
        });
		Button clBTMemoDelete = (Button) findViewById(R.id.B_view_memo_delete);
		clBTMemoDelete.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		if(!m_bIsDelete)
        		{
        			m_bIsDelete = true;
            		TableLayout TL = (TableLayout) ViewList.this.findViewById(R.id.memolistmenu);  
            		
            		TL.setColumnCollapsed(0, true);
            		TL.setColumnCollapsed(1, true);
            		TL.setColumnCollapsed(2, true);	

            		TL.setColumnCollapsed(4, false);

            		TL.setColumnStretchable(0, false);
            		TL.setColumnStretchable(1, false);
            		TL.setColumnStretchable(2, false);
            		//TL.setColumnStretchable(3, false);
            		TL.setColumnStretchable(4, true);            		
            		//TL.setColumnStretchable(3, true);   
            		TL.invalidate();
            		ViewList.m_myAdapter = new NoteListCursorAdapter(ViewList.this,ViewList.m_clCursor);
            		ListView memoList = (ListView) ViewList.this.findViewById(R.id.listviewmemo);
            		memoList.setAdapter(m_myAdapter);
        		}	
        		else
        		{
        			//delete rec--->
        			ArrayList<Integer> alIDs = new ArrayList<Integer>();
        			findSelectItemDBID(alIDs);
        			if(alIDs.size()>0){
        				Integer[] needDeleteIDs = (Integer[])alIDs.toArray(new Integer[0]);
            			m_clCNoteDBCtrl.Delete(needDeleteIDs);
        			}        			
        			Return2MemoList();
        		}	
        	}
        });
		Button clBTMemoCancel = (Button) findViewById(R.id.B_view_memo_cancel);
		clBTMemoCancel.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Return2MemoList();
         	}
        });
		Button clBTWarningR = (Button) findViewById(R.id.B_view_remind_return);
		clBTWarningR.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  		
        		ViewList.this.finish();
        	}
        });
		//ȡ��TabHost����
		mTabHost = getTabHost();
	    
		/* ΪTabHost���ӱ�ǩ */
		//�½�һ��newTabSpec(newTabSpec)
		//�������ǩ��ͼ��(setIndicator)
		//��������(setContent)
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
	    		.setIndicator("����",null)
	    		.setContent(R.id.memolist));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
	    		.setIndicator("����",null)
	    		.setContent(R.id.remindlist));
	    
	    //����TabHost�ı�����ɫ
	    mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //����TabHost�ı���ͼƬ��Դ
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //���õ�ǰ��ʾ��һ����ǩ
	    mTabHost.setCurrentTab(0);
	}
	
	void Return2MemoList()
	{
		m_bIsMoveIn = false;
		m_bIsDelete = false;
		TableLayout TL = (TableLayout) ViewList.this.findViewById(R.id.memolistmenu);  
		
		TL.setColumnCollapsed(0, false);
		TL.setColumnCollapsed(1, false);
		TL.setColumnCollapsed(2, false);	

		TL.setColumnCollapsed(4, true);

		TL.setColumnStretchable(0, true);
		TL.setColumnStretchable(1, true);
		TL.setColumnStretchable(2, true);
		//TL.setColumnStretchable(3, false);
		TL.setColumnStretchable(4, false);
		//TL.setColumnStretchable(3, true); 
		TL.invalidate();
		m_clCursor	=	m_clCNoteDBCtrl.getMemoRootInfo();
		ViewList.m_myAdapter = new NoteListCursorAdapter(ViewList.this,ViewList.m_clCursor);
		ListView memoList = (ListView) ViewList.this.findViewById(R.id.listviewmemo);
		memoList.setAdapter(m_myAdapter);
	}
	void findSelectItemDBID(ArrayList<Integer> alIDs)
	{
		int count = m_myAdapter.getCount();
		for(int i = 0; i < count; i++)
		{
			//View item = m_myAdapter.getView(i, null, null);
			ListView memoList = (ListView) ViewList.this.findViewById(R.id.listviewmemo);
			final CheckBox cb = (CheckBox)memoList.getChildAt(i).findViewById(R.id.memoitem_memoselect);
			if(cb!=null){
				boolean b = cb.isChecked();
				if(b==true){
					int iID = (int)m_myAdapter.getItemId(i);
					alIDs.add(new Integer(iID));
				}
			}
		}
	}
}