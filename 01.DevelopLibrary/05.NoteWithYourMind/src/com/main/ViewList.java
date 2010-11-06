package com.main;
import android.app.Activity;
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
import android.app.AlertDialog;
import android.widget.EditText;
import java.util.Calendar;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

public class ViewList extends Activity
{
	private NoteListUICtrl m_NoteListUICtrl;
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
        Button btList = (Button)findViewById(R.id.B_main_View);
		Button btSave = (Button)findViewById(R.id.B_main_Save);
		btList.setVisibility(View.GONE);
		btSave.setVisibility(View.GONE);

		ListView memoList = (ListView) findViewById(R.id.listviewmemo);
		LinearLayout toolbarLayout = (LinearLayout) findViewById(R.id.memolistmenu);
		m_NoteListUICtrl = new NoteListUICtrl(this, memoList, 0, toolbarLayout);
		m_NoteListUICtrl.initializeSource();
		//取得TabHost对象
		TabHost TabHost = (TabHost)findViewById(android.R.id.tabhost);
		TabHost.setup();	
		TabSpec specmemo = TabHost.newTabSpec("tab_test1");
		
		specmemo.setIndicator(composeLayout("备忘",R.drawable.tabmemo));
		specmemo.setContent(R.id.memolist);
		
		TabSpec specremind = TabHost.newTabSpec("tab_test2");
		specremind.setIndicator(composeLayout("提醒",R.drawable.tabremind));
		specremind.setContent(R.id.remindlist);
		TabHost.addTab(specmemo);
	    TabHost.addTab(specremind);
	    
	    //设置当前显示哪一个标签
	    TabHost.setCurrentTab(0);
		Button clBTNewMemo = (Button) findViewById(R.id.B_main_NewMemo);
        clBTNewMemo.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(ViewList.this, NoteWithYourMind.class); 
        		intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_Intent);
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
		        		EditText FolderNameText = (EditText) DialogView.findViewById(R.id.foldername_edit);
		        		String strFolderNameText = FolderNameText.getText().toString();
		        		if(strFolderNameText.length()>0){
		        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		            		clCMemoInfo.iPreId	=	clCMemoInfo.PreId_Root;
		            		clCMemoInfo.iType	=	CMemoInfo.Type_Folder;
		            		clCMemoInfo.iIsRemind	=	CMemoInfo.IsRemind_No;

 						    c = Calendar.getInstance();
							clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
		            		clCMemoInfo.strDetail	=	strFolderNameText;
		            		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
		            		clCMemoInfo.strPassword = null;
		            		
		            		m_clCNoteDBCtrl.Create(clCMemoInfo);     		
		            		FolderNameText.setText("");
		            		
		            		m_NoteListUICtrl.updateListData();
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
	}
	public void onDestroy()
	{
		super.onDestroy();
		m_NoteListUICtrl.releaseSource();
		m_NoteListUICtrl = null;
	}
	
	public View composeLayout(String s, int i){
		LayoutInflater factory = LayoutInflater.from(this);
		LinearLayout layout = (LinearLayout)factory.inflate(R.layout.tabwidgetview, null, false); 
		TextView tv = (TextView)layout.findViewById(R.id.tabindicatorview_text);  
		tv.setText(s);  
		ImageView iv = (ImageView)layout.findViewById(R.id.tabindicatorview_image);  
		iv.setImageResource(i);  
		iv.setScaleType(ImageView.ScaleType.CENTER);
		return layout;  
	}  

}
