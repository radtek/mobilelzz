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
	public Resources m_r = NoteWithYourMind.m_r;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		String Detail = m_r.getString(R.string.db_field_Detail);
		String LastModifyTime = m_r.getString(R.string.db_field_LastModifyTime);
		Cursor clCursor	=	m_clCNoteDBCtrl.getMemoRootInfo();
		startManagingCursor(clCursor);
		ListAdapter listadapter = new SimpleCursorAdapter(
				ViewList.this,
				R.layout.memolistitem,
				clCursor,
				null,//new String[]{Detail},
				null//new int[]{R.id.memoitem_memotext}
				);
		
		ListView memoList = (ListView) findViewById(R.id.listviewmemo);
		memoList.setAdapter(listadapter);
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
        		
        		TL.requestLayout();
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
	    //mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //设置TabHost的背景图片资源
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //设置当前显示哪一个标签
	    mTabHost.setCurrentTab(0);
	}
}
