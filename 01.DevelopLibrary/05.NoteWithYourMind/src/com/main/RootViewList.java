package com.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.view.Window;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import java.util.Calendar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.Toast;


public class RootViewList extends Activity
{
	//private NoteListUICtrl m_NoteListUICtrl;
	private Calendar c;
	TabHost m_TabHost;
	private CNoteDBCtrl m_clCNoteDBCtrl = NoteWithYourMind.m_clCNoteDBCtrl;
	private NoteListCursorAdapter m_memoListAdapter;
	private NoteListCursorAdapter m_remindListAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.view);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        Button btSkin = (Button)findViewById(R.id.B_main_setting_skin);
		Button btEncode = (Button)findViewById(R.id.B_main_setting_encode);
		btSkin.setVisibility(View.GONE);
		btEncode.setVisibility(View.GONE);

		//取得TabHost对象
		m_TabHost = (TabHost)findViewById(android.R.id.tabhost);
		m_TabHost.setup();	
		TabWidget tw = m_TabHost.getTabWidget();
		TabSpec specmemo = m_TabHost.newTabSpec("1");
		//specmemo.setIndicator(composeLayout("备忘",R.drawable.tabmemo));
		specmemo.setIndicator("备忘");
		specmemo.setContent(R.id.memolist);
		
		TabSpec specremind = m_TabHost.newTabSpec("2");
		//specremind.setIndicator(composeLayout("提醒",R.drawable.tabremind));
		specremind.setIndicator("提醒");
		specremind.setContent(R.id.remindlist);
		
		//TextView tvNewNote = new TextView(this);
		//tvNewNote.setText("新建便签");
		TabSpec specNewNote = m_TabHost.newTabSpec("3");
		specNewNote.setIndicator("新建便签");
		specNewNote.setContent(R.id.newnote);
		
		m_TabHost.addTab(specmemo);
		m_TabHost.addTab(specremind);
		m_TabHost.addTab(specNewNote);
	    
	    //设置当前显示哪一个标签
		m_TabHost.setCurrentTab(0);
		m_TabHost.setOnTabChangedListener(new OnTabChangeListener(){
	    	public void onTabChanged(String tabId){
	    		if(tabId.equals(String.valueOf(3))){
	    			Intent intent = new Intent();
	        		intent.setClass(RootViewList.this, NoteWithYourMind.class); 
	        		intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InRoot);
	 
	        		startActivity(intent);
	    		}else if(tabId.equals(String.valueOf(2))){
	    			
	    		}else if(tabId.equals(String.valueOf(1))){
	    			
	    		}else{
	    			
	    		}
	    	}
	    });
	    /*ImageButton clBTNewMemo = (ImageButton) findViewById(R.id.B_main_NewMemo);
        clBTNewMemo.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(RootViewList.this, NoteWithYourMind.class); 
        		intent.putExtra(NoteWithYourMind.ExtraData_NewNoteKind, NoteWithYourMind.NewNoteKindEnum.NewNoteKind_InRoot);
 
        		startActivity(intent);
        	}
        });*/
        View vMemoList = (View) findViewById(R.id.memolist);
        Button clBTMemoNewFolder = (Button) vMemoList.findViewById(R.id.RootViewListContent_NewFolder_B);
		clBTMemoNewFolder.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
        		PopUpNewFolderDlg(CMemoInfo.IsRemind_No); 			
        	}
        });
		View vRemindList = (View) findViewById(R.id.remindlist);
        Button clBTMemoNewFolder2 = (Button) vRemindList.findViewById(R.id.RootViewListContent_NewFolder_B);
        clBTMemoNewFolder2.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{  			
        		PopUpNewFolderDlg(CMemoInfo.IsRemind_Yes); 			
        	}
        });
        BindListViewData(vRemindList, CMemoInfo.IsRemind_Yes);
        BindListViewData(vMemoList, CMemoInfo.IsRemind_No);
	}
	
	public void onStop()
	{
		super.onStop();
	}
	public void onResume()
	{
		super.onResume();
		m_TabHost.setCurrentTab(0);
	}
	public void onDestroy()
	{
		super.onDestroy();
//		m_NoteListUICtrl.releaseSource();
//		m_NoteListUICtrl = null;
	}
	
	private void BindListViewData(View vParent, Integer bIsRemind){
		if(bIsRemind==CMemoInfo.IsRemind_Yes){
			Cursor rootRemindListCursor = m_clCNoteDBCtrl.getRemindsByID(CMemoInfo.PreId_Root);
			if(rootRemindListCursor!=null){
				startManagingCursor(rootRemindListCursor);
			}
			ListView remindlist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
			m_remindListAdapter = new NoteListCursorAdapter(RootViewList.this, rootRemindListCursor);
			remindlist.setAdapter(m_remindListAdapter);
		}else{
			Cursor rootMemoListCursor = m_clCNoteDBCtrl.getMemosByID(CMemoInfo.PreId_Root);
			if(rootMemoListCursor!=null){
				startManagingCursor(rootMemoListCursor);
			}
			ListView memolist = (ListView) vParent.findViewById(R.id.RootViewListContent_List);
			m_memoListAdapter = new NoteListCursorAdapter(RootViewList.this, rootMemoListCursor);
			memolist.setAdapter(m_memoListAdapter);
		}
	}
	
	private View composeLayout(String s, int i){
		LayoutInflater factory = LayoutInflater.from(this);
		LinearLayout layout = (LinearLayout)factory.inflate(R.layout.tabwidgetview, null, false); 
		TextView tv = (TextView)layout.findViewById(R.id.tabindicatorview_text);  
		tv.setText(s);  
		ImageView iv = (ImageView)layout.findViewById(R.id.tabindicatorview_image);  
		iv.setImageResource(i);  
		iv.setScaleType(ImageView.ScaleType.CENTER);
		return layout;  
	}  
	private void PopUpNewFolderDlg(Integer bIsRemind){
		LayoutInflater factory = LayoutInflater.from(RootViewList.this);
		final View DialogView = factory.inflate(R.layout.dialognewfolder, null);
		final Integer g_bIsRemind = bIsRemind;
		AlertDialog clDlgNewFolder = new AlertDialog.Builder(RootViewList.this)	
			.setIcon(R.drawable.clock)
			.setTitle("请输入文件夹名称")
			.setView(DialogView)
			.setPositiveButton("确定",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
	        		EditText FolderNameText = (EditText) DialogView.findViewById(R.id.foldername_edit);
	        		String strFolderNameText = FolderNameText.getText().toString();
	        		if(strFolderNameText.length()>0){
	        			CreateFolderRec(strFolderNameText, g_bIsRemind);
	            		dialog.cancel();
	        		}else{
	        			Toast toast = Toast.makeText(RootViewList.this, "请输入文件夹名称", Toast.LENGTH_SHORT);
	            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
	            		toast.show();
	        		}	
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
	private void CreateFolderRec(String strDetail, Integer bIsRemind){
		CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;
		clCMemoInfo.iType	=	CMemoInfo.Type_Folder;
		clCMemoInfo.iIsRemind	=	bIsRemind;
		c = Calendar.getInstance();
		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
		clCMemoInfo.strDetail	=	strDetail;
		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
		clCMemoInfo.iIsEncode = CMemoInfo.IsEncode_No;
		clCMemoInfo.strPassword = null;
		
		m_clCNoteDBCtrl.Create(clCMemoInfo); 
	}

}
