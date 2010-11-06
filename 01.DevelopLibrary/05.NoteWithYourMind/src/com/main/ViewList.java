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
import android.widget.TableLayout;
import android.view.Window;
import android.widget.Button;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.EditText;
import java.util.Calendar;
import android.widget.Toast;

public class ViewList extends TabActivity
{
	private NoteListUICtrl m_NoteListUICtrl;
	//声明TabHost对象
	TabHost mTabHost;
	private Calendar c;
	CNoteDBCtrl m_clCNoteDBCtrl = NoteWithYourMind.m_clCNoteDBCtrl;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.view);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		

		ListView memoList = (ListView) findViewById(R.id.listviewmemo);
		LinearLayout toolbarLayout = (LinearLayout) findViewById(R.id.memolistmenu);
		m_NoteListUICtrl = new NoteListUICtrl(this, memoList, 0, toolbarLayout);
		m_NoteListUICtrl.initializeSource();
		//取得TabHost对象
		mTabHost = getTabHost();
	    
		/* 为TabHost添加标签 */
		//新建一个newTabSpec(newTabSpec)
		//设置其标签和图标(setIndicator)
		//设置内容(setContent)
		/*TextView memotag = new TextView(ViewList.this);
		memotag.setText("备忘");
		memotag.setTextSize(25);
		memotag.setGravity(Gravity.CENTER);*/
		
		
		TabSpec specmemo = mTabHost.newTabSpec("tab_test1");
		specmemo.setIndicator("");
		//specmemo.setIndicator("备忘",getResources().getDrawable(R.drawable.tabmemo));
		specmemo.setContent(R.id.memolist);
		
		/*TextView remindtag = new TextView(ViewList.this);
		remindtag.setText("提醒");
		remindtag.setTextSize(25);
		remindtag.setGravity(Gravity.CENTER);*/
		TabSpec specremind = mTabHost.newTabSpec("tab_test2");
		specremind.setIndicator("");
		//specremind.setIndicator("提醒",getResources().getDrawable(R.drawable.tabremind));
		specremind.setContent(R.id.remindlist);
	    mTabHost.addTab(specmemo);
	    mTabHost.addTab(specremind);
	    
	    LinearLayout LL = (LinearLayout)mTabHost.getChildAt(0);
	    TabWidget TW = (TabWidget)LL.getChildAt(0);

	    updateWidgetView(TW,0,"备忘", R.drawable.tabmemo);
	    updateWidgetView(TW,1,"提醒", R.drawable.tabremind);
	    //设置TabHost的背景颜色
	    //mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    //设置TabHost的背景图片资源
	    //mTabHost.setBackgroundResource(R.drawable.bg0);
	    
	    //设置当前显示哪一个标签
	    mTabHost.setCurrentTab(0);

	   	TableLayout TL = (TableLayout) findViewById(R.id.CTL_Title);		
		TL.setColumnCollapsed(0, true);
		TL.setColumnCollapsed(1, true);
		TL.setColumnCollapsed(2, false);
		TL.setColumnCollapsed(3, false);
		TL.setColumnStretchable(0, false);		
		TL.setColumnStretchable(1, false);	
		TL.setColumnStretchable(2, true);		
		TL.setColumnStretchable(3, true);	


        Button clBTNewMemo = (Button) findViewById(R.id.B_main_NewMemo);
        clBTNewMemo.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(ViewList.this, NoteWithYourMind.class);
        		startActivity(intent);
        	}
        });

        Button clBTMemoNewFolder = (Button) findViewById(R.id.B_main_NewFolder);
		clBTMemoNewFolder.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			

			LayoutInflater factory = LayoutInflater.from(ViewList.this);
			final View DialogView = factory.inflate(R.layout.dialognewfolder, null);
			
			AlertDialog clDlgNewFolder = new AlertDialog.Builder(ViewList.this)	
				.setIcon(R.drawable.clock)
				.setTitle("请输入文件夹名称")
				.setView(DialogView)
				.setPositiveButton("确定",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
		        		EditText FolderNameText = (EditText) findViewById(R.id.foldername_edit);
		        		String strFolderNameText = FolderNameText.getText().toString();
		        		if(strFolderNameText.length()>0){
		        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		            		clCMemoInfo.iPreId	=	0;
		            		clCMemoInfo.iType	=	CMemoInfo.Type_Folder;
		            		clCMemoInfo.iIsRemind	=	CMemoInfo.IsRemind_No;

 						    c = Calendar.getInstance();
							clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
		            		clCMemoInfo.strDetail	=	strFolderNameText;
		            		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
		            		m_clCNoteDBCtrl.Create(clCMemoInfo);     		
		            		FolderNameText.setText("");
		            		Toast toast = Toast.makeText(ViewList.this, "保存成功", Toast.LENGTH_SHORT);
		            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
		            		toast.show();
		        		}else{
		        			Toast toast = Toast.makeText(ViewList.this, "请输入文件夹名称", Toast.LENGTH_SHORT);
		            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
		            		toast.show();
		        		}
						dialog.cancel();
					}
				})
				.setNegativeButton("取消",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int i)
					{
						dialog.cancel();
					}
				})

				
				.create();

			clDlgNewFolder.show();      			

        	}
        });
		


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
