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
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TabHost.OnTabChangeListener;

public class ViewList extends TabActivity
{
	//声明TabHost对象
	TabHost mTabHost;
	CNoteDBCtrl m_clCNoteDBCtrl = NoteWithYourMind.m_clCNoteDBCtrl;
	public static boolean m_bIsDelete = false;
	public static boolean m_bIsMoveIn = false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);	
		Cursor clCursor	=	m_clCNoteDBCtrl.getMemoRootInfo();
		if(clCursor!=null)
		{
			startManagingCursor(clCursor);
			int count = clCursor.getCount();
			if(count>=0)
			{
//				ListAdapter listadapter = new SimpleCursorAdapter(
//						this,
//						R.layout.memolistitem,
//						clCursor,
//						new String[]{CNoteDBCtrl.KEY_detail},
//						new int[]{R.id.memoitem_memotext}
//						);
				NoteListCursorAdapter myAdapter = new NoteListCursorAdapter(this,clCursor);
				ListView memoList = (ListView) findViewById(R.id.listviewmemo);
				memoList.setAdapter(myAdapter);
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
            		TableLayout TL = (TableLayout) findViewById(R.id.memolistmenu);  
            		
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
        		}	
        		else
        		{
        			//delete rec--->
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
		//取得TabHost对象
		mTabHost = getTabHost();
	    
		/* 为TabHost添加标签 */
		//新建一个newTabSpec(newTabSpec)
		//设置其标签和图标(setIndicator)
		//设置内容(setContent)
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
	    		.setIndicator("备忘",null)
	    		.setContent(R.id.memolist));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
	    		.setIndicator("提醒",null)
	    		.setContent(R.id.remindlist));
	    
	    //设置TabHost的背景颜色
	    mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //设置TabHost的背景图片资源
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //设置当前显示哪一个标签
	    mTabHost.setCurrentTab(0);
	}
	
	void Return2MemoList()
	{
		m_bIsMoveIn = false;
		m_bIsDelete = false;
		TableLayout TL = (TableLayout) findViewById(R.id.memolistmenu);  
		
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
	}
}
