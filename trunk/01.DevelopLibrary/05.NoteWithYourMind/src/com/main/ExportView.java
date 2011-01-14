package com.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
public class ExportView extends Activity 
implements View.OnClickListener
{

	
	public static String ExtraData_initListItemDBID		=	"com.main.ExtraData_RootList_initListItemDBID";
	
	private NoteListUICtrl m_NoteListUICtrl;
	private Calendar c;
	private CNoteDBCtrl		m_clCNoteDBCtrl;
	private View m_toolBarLayout;
	private int m_iContextMenu_DBID = CommonDefine.g_int_Invalid_ID;


	private	EditText m_ETExportDir ;
	private boolean m_bIsListInit = false;


	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        m_clCNoteDBCtrl = CommonDefine.getNoteDBCtrl(this);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.exportview);	
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

 		EditText m_ETExportDir = (EditText) findViewById(R.id.EditText_ExportDir);
        m_ETExportDir.setText("ËÑË÷±ãÇ©");
		
        ImageButton btnExport = (ImageButton) findViewById(R.id.ExportView_toolbar_ok);
        btnExport.setOnClickListener(this);

        ImageButton btnExportCancel = (ImageButton) findViewById(R.id.ExportView_toolbar_cancel);
        btnExportCancel.setOnClickListener(this);
		
	}
	
	public void onStop()
	{
		super.onStop();
	}
	public void onResume()
	{
		super.onResume();
	}
	public void onDestroy()
	{
		super.onDestroy();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event); 
    }
	public void onClick(View view){
		switch(view.getId()){
		case R.id.ExportView_toolbar_ok:
			processExportClick(view);
		    break;
		case R.id.ExportView_toolbar_cancel:
			processCancelClick(view);
			break;
		default:
			break;
		}
	}

	private void processExportClick(View view){

		String strExportDir = m_ETExportDir.getText().toString();
//		if( strExportDir )

	}	

	private void processCancelClick(View view){


	}	

	private boolean IsSameNameFileExist(){
		boolean bisExist = false;

		return bisExist;
	}
	
}
