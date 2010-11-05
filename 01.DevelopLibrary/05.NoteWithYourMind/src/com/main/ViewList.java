package com.main;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
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
		/*TextView memotag = new TextView(ViewList.this);
		memotag.setText("����");
		memotag.setTextSize(25);
		memotag.setGravity(Gravity.CENTER);*/
		
		
		TabSpec specmemo = mTabHost.newTabSpec("tab_test1");
		specmemo.setIndicator("");
		//specmemo.setIndicator("����",getResources().getDrawable(R.drawable.tabmemo));
		specmemo.setContent(R.id.memolist);
		
		/*TextView remindtag = new TextView(ViewList.this);
		remindtag.setText("����");
		remindtag.setTextSize(25);
		remindtag.setGravity(Gravity.CENTER);*/
		TabSpec specremind = mTabHost.newTabSpec("tab_test2");
		specremind.setIndicator("");
		//specremind.setIndicator("����",getResources().getDrawable(R.drawable.tabremind));
		specremind.setContent(R.id.remindlist);
	    mTabHost.addTab(specmemo);
	    mTabHost.addTab(specremind);
	    
	    LinearLayout LL = (LinearLayout)mTabHost.getChildAt(0);
	    TabWidget TW = (TabWidget)LL.getChildAt(0);

	    updateWidgetView(TW,0,"����", R.drawable.tabmemo);
	    updateWidgetView(TW,1,"����", R.drawable.tabremind);
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
	
	public View composeLayout(String s, int i){  
		LinearLayout layout = new LinearLayout(this);  
		layout.setOrientation(LinearLayout.VERTICAL);  
		  
		TextView tv = new TextView(this);  
		tv.setGravity(Gravity.CENTER);  
		tv.setSingleLine(true);  
		tv.setText(s);  
		layout.addView(tv,   
		        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));  
		  
		ImageView iv = new ImageView(this);  
		iv.setImageResource(i);  
		layout.addView(iv,   
		        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));  
		return layout;  
	}  
	public void updateWidgetView(TabWidget TW, int i, String text, int image){  
        RelativeLayout rl =(RelativeLayout)TW.getChildAt(i);  
          
        rl.addView(composeLayout(text,image));  
    }  

}
