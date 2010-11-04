package com.main;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class ViewList extends TabActivity
{
	private NoteListUICtrl m_NoteListUICtrl;
	//����TabHost����
	TabHost mTabHost;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);	
		ListView memoList = (ListView) findViewById(R.id.listviewmemo);
		LinearLayout toolbarLayout = (LinearLayout) findViewById(R.id.memolistmenu);
		m_NoteListUICtrl = new NoteListUICtrl(this, memoList, 0, toolbarLayout);
		m_NoteListUICtrl.initializeSource();
		//ȡ��TabHost����
		mTabHost = getTabHost();
	    
		/* ΪTabHost��ӱ�ǩ */
		//�½�һ��newTabSpec(newTabSpec)
		//�������ǩ��ͼ��(setIndicator)
		//��������(setContent)
		TextView memotag = new TextView(ViewList.this);
		memotag.setText("����");
		memotag.setTextSize(40);
		memotag.setGravity(Gravity.CENTER);
		TabSpec specmemo = mTabHost.newTabSpec("tab_test1");
		specmemo.setIndicator(memotag);
		specmemo.setContent(R.id.memolist);
		
		TextView remindtag = new TextView(ViewList.this);
		remindtag.setText("����");
		remindtag.setTextSize(40);
		remindtag.setGravity(Gravity.CENTER);
		TabSpec specremind = mTabHost.newTabSpec("tab_test2");
		specremind.setIndicator(remindtag);
		specremind.setContent(R.id.remindlist);
	    mTabHost.addTab(specmemo);
	    mTabHost.addTab(specremind);
	    
	    //����TabHost�ı�����ɫ
	    //mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //����TabHost�ı���ͼƬ��Դ
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //���õ�ǰ��ʾ��һ����ǩ
	    mTabHost.setCurrentTab(0);
	}
	
	public void onStop()
	{
		super.onStop();
		//m_NoteListUICtrl.releaseSource();
		//m_NoteListUICtrl = null;
		//mTabHost = null;
	}
	public void onDestroy()
	{
		super.onDestroy();
		m_NoteListUICtrl.releaseSource();
		m_NoteListUICtrl = null;
		mTabHost = null;
	}

}
