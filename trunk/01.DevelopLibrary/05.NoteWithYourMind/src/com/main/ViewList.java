package com.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class ViewList extends TabActivity
{
	//����TabHost����
	TabHost mTabHost;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		
		ListView memoList = (ListView) findViewById(R.id.listviewmemo);
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> item1 = new HashMap<String,String>();
		item1.put("memotext", "�Ұ����Ұ����Ұ����Ұ����Ұ����Ұ����Ұ����Ұ����Ұ���");
		item1.put("date", "10/10/10");
		list.add(item1);
		HashMap<String,String> item2 = new HashMap<String,String>();
		item2.put("memotext", "�㰮����");
		item2.put("date", "11/11/11");
		list.add(item2);
		SimpleAdapter listadapter = new SimpleAdapter(
				ViewList.this,
				list,
				R.layout.memolistitem,
				new String[]{"memotext","date"},
				new int[]{R.id.memoitem_memotext, R.id.memoitem_memodate}
				);
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
        		ListView memoList = (ListView) findViewById(R.id.listviewmemo);
        		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        		HashMap<String,String> item1 = new HashMap<String,String>();
        		item1.put("memotext", "�Ұ����Ұ����Ұ����Ұ����Ұ����Ұ����Ұ����Ұ����Ұ���");
        		list.add(item1);
        		HashMap<String,String> item2 = new HashMap<String,String>();
        		item2.put("memotext", "�㰮����");
        		list.add(item2);
        		SimpleAdapter listadapter = new SimpleAdapter(
        				ViewList.this,
        				list,
        				R.layout.memolistitemselect,
        				new String[]{"memotext"},
        				new int[]{R.id.memoitem_memotext}
        				);
        		memoList.setAdapter(listadapter);
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
