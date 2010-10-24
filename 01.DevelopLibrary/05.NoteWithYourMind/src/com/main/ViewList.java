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
	//����TabHost����
	TabHost mTabHost;
	CNoteDBCtrl m_clCNoteDBCtrl = NoteWithYourMind.m_clCNoteDBCtrl;
	NoteDBAdapter	m_clNoteDBAdapter = NoteWithYourMind.m_clNoteDBAdapter;
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
		Cursor cur = m_clNoteDBAdapter.getMemoRootInfo();
		startManagingCursor(cur);
		ListAdapter listadapter1 = new SimpleCursorAdapter(
				this,
				R.layout.memolistitem,
				cur,
				new String[]{NoteDBAdapter.KEY_detail},
				new int[]{R.id.memoitem_memotext}
				);
		
		Cursor clCursor	=	m_clCNoteDBCtrl.getMemoRootInfo();
		if(clCursor!=null)
		{
			startManagingCursor(clCursor);
			int count = clCursor.getCount();
			if(count>=0)
			{
				ListAdapter listadapter = new SimpleCursorAdapter(
						this,
						R.layout.memolistitem,
						clCursor,
						new String[]{NoteDBAdapter.KEY_detail},
						new int[]{R.id.memoitem_memotext}
						);
				
				ListView memoList = (ListView) findViewById(R.id.listviewmemo);
				memoList.setAdapter(listadapter);
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
		//ȡ��TabHost����
		mTabHost = getTabHost();
	    
		/* ΪTabHost��ӱ�ǩ */
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
	    //mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //����TabHost�ı���ͼƬ��Դ
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //���õ�ǰ��ʾ��һ����ǩ
	    mTabHost.setCurrentTab(0);
	}
}
