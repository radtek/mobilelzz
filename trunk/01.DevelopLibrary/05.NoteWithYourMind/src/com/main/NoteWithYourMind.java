package com.main;

/*���ڱ༭����

�༭�����ͷ�Ϊ��
δ֪
Memo���༭���У�
�ļ��У��༭���У�

δ֪
3��CheckBox����ѡ
all default
����encode��detail��folder��remind��֮���������

Memo
���ܺ��ļ���ѡ���ѡ
����detail��remind
����detail��remind

�ļ���
�ļ��к����Ѳ���ѡ
���¼��ܡ�detail
������ܡ�detail*/

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;

public class NoteWithYourMind extends Activity {
	public enum NewNoteKindEnum{
		NewNoteKind_Unknown,
		NewNoteKind_Memo,
		NewNoteKind_Folder,
		NewNoteKind_Both
	}
	public static String ExtraData_MemoID = "com.main.ExtraData_MemoID";
	public static String NewNoteKind = "com.main.ExtraData_NewNoteKind";
	private static String m_strPassWord = "123456";
	
	public static final int ITEM0 = Menu.FIRST;
	public static final int ITEM1 = Menu.FIRST + 1;
	
	public static	CNoteDBCtrl		m_clCNoteDBCtrl;
	
	private Calendar c;

	private int m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
	private NewNoteKindEnum m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;


    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		
        m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
        m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;
        Intent iExtraData = this.getIntent();
		m_ExtraData_MemoID = iExtraData.getIntExtra(ExtraData_MemoID, CMemoInfo.Id_Invalid );
		m_ExtraData_NewNoteKind = (NewNoteKindEnum)iExtraData.getSerializableExtra(NewNoteKind);
		if(m_ExtraData_NewNoteKind==null){
			m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;
		}
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        UpdateViewStatus();
        
		/*
		Button clBT = (Button) findViewById(R.id.B_main_Exit);
        clBT.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		NoteWithYourMind.this.finish();
        	}
        });
        */
        c = Calendar.getInstance();
        Button clBTSave = (Button) findViewById(R.id.B_main_Save);
        clBTSave.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		EditText memotext = (EditText) findViewById(R.id.ET_main_Memo);
        		String strMemoText = memotext.getText().toString();
        		if(strMemoText.length()>0){
        			SaveEditData(strMemoText);       				     		
        			UpdateViewStatus();
            		Toast toast = Toast.makeText(NoteWithYourMind.this, "����ɹ�", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
        		}else{
        			Toast toast = Toast.makeText(NoteWithYourMind.this, "����������", Toast.LENGTH_SHORT);
            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
            		toast.show();
        		}
        	}
        });
        
        Button clBTView = (Button) findViewById(R.id.B_main_View);
        clBTView.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		Intent intent = new Intent();
        		intent.setClass(NoteWithYourMind.this, ViewList.class);
        		startActivity(intent);
        	}
        });
        
        CheckBox clCheckBoxWarning = (CheckBox) findViewById(R.id.CB_main_IsWarning);
        clCheckBoxWarning.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	{
        		CheckBox clCheckBoxFolder = (CheckBox) findViewById(R.id.CB_main_IsFolder);
        		if(isChecked)
        		{
        			clCheckBoxFolder.setChecked(false);
        			clCheckBoxFolder.setClickable(false);
        			LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
        			final View DialogView = factory.inflate(R.layout.dateandtime, null);
        			AlertDialog dlg = new AlertDialog.Builder(NoteWithYourMind.this)
        				.setTitle("������������")
        				.setView(DialogView)
        				.setPositiveButton("ȷ��",null)
        				.setNegativeButton("ȡ��",null)
        				.create();
        			dlg.show();
        			TimePicker clTP = (TimePicker) DialogView.findViewById(R.id.TimePicker01);
        			clTP.setIs24HourView(true);
        		}else{
        			clCheckBoxFolder.setClickable(true);
        		}
        	}
        });
        CheckBox clCheckBoxFolder = (CheckBox) findViewById(R.id.CB_main_IsFolder);
        clCheckBoxFolder.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        	{
        		CheckBox clCheckBoxWarnings = (CheckBox) findViewById(R.id.CB_main_IsWarning);
        		if(isChecked){
        			clCheckBoxWarnings.setChecked(false);
        			clCheckBoxWarnings.setClickable(false);
        		}else{
        			clCheckBoxWarnings.setClickable(true);
        		}
        	}
        });

	   	TableLayout TL = (TableLayout) findViewById(R.id.CTL_Title);		
		TL.setColumnCollapsed(0, false);
		TL.setColumnCollapsed(1, false);
		TL.setColumnCollapsed(2, true);
		TL.setColumnCollapsed(3, true);
		TL.setColumnStretchable(0, true);		
		TL.setColumnStretchable(1, true);	
		TL.setColumnStretchable(2, false);		
		TL.setColumnStretchable(3, false);	
		
    }
    private void UpdateViewStatus(){
    	Cursor curExtraMemo = null;
    	if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){
        	curExtraMemo = m_clCNoteDBCtrl.getMemoRec(m_ExtraData_MemoID);
        	startManagingCursor(curExtraMemo);
        	curExtraMemo.moveToFirst();
    	}
    	EditText etMemoDetail = (EditText) findViewById(R.id.ET_main_Memo);
    	CheckBox CBFolder = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsFolder);
 //   	CheckBox CBEncode = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsEncode);
    	CheckBox CBRemind = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsWarning);
        Button clBTView = (Button) findViewById(R.id.B_main_View);
    	if(m_ExtraData_NewNoteKind != NewNoteKindEnum.NewNoteKind_Unknown){
    		clBTView.setVisibility(View.INVISIBLE);
    	}else{
    		clBTView.setVisibility(View.VISIBLE);
    	}
    	if((m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Unknown)||
    			(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Both)){
    		etMemoDetail.setText("");
    		CBFolder.setChecked(false);
//        	CBEncode.setChecked(false);  
        	CBRemind.setChecked(false);
        	etMemoDetail.setHint("���ڴ˴��༭�ļ������ƻ��߱�ǩ����");
    	}else if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Memo){
    		CBFolder.setChecked(false);
    		CBFolder.setClickable(false);
    		if(curExtraMemo!=null){
    			UpdateDetail(curExtraMemo, etMemoDetail);
 //   			UpdateEncode(curExtraMemo, CBEncode);
    		}else{
    			etMemoDetail.setHint("���ڴ˴��༭��ǩ����");
    		}
    	}else if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Folder){
    		CBFolder.setChecked(false);
    		CBFolder.setClickable(false);
    		CBRemind.setChecked(false);
    		CBRemind.setClickable(false);
    		if(curExtraMemo!=null){
    			UpdateDetail(curExtraMemo, etMemoDetail);
 //   			UpdateEncode(curExtraMemo, CBEncode);
    		}else{
    			etMemoDetail.setHint("���ڴ˴��༭�ļ�������");
    		}
    	}else{
    		
    	}
    }
    private void UpdateDetail(Cursor cur, EditText etDetail){
    	int index = cur.getColumnIndex(CNoteDBCtrl.KEY_type);
		int value = cur.getInt(index);
		if(value == CMemoInfo.Type_Folder){
			
		}else{
			index = cur.getColumnIndex(CNoteDBCtrl.KEY_detail);
	    	String strDetail = cur.getString(index);
	    	etDetail.setText(strDetail);
		}	
    }
    private void UpdateEncode(Cursor cur, CheckBox isEncode){
    	int iIndex = cur.getColumnIndex(CNoteDBCtrl.KEY_password);
    	String strPassWord = cur.getString(iIndex);
    	if((strPassWord != null) && (strPassWord.length() > 0)){
    		isEncode.setChecked(true);
    	}else{
    		isEncode.setChecked(false);
    	}
    }
    private void SaveEditData(String strMemoText)
    {
    	CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		clCMemoInfo.strDetail	=	strMemoText;
		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();
		clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
//		CheckBox CBEncode = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsEncode);
		CheckBox CBFolder = (CheckBox) NoteWithYourMind.this.findViewById(R.id.CB_main_IsFolder);

    	if((m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Unknown)||
    			(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Both)){
 //   		if(CBEncode.isChecked()){
  //  			clCMemoInfo.strPassword = m_strPassWord;
  //  		}
 //   		else{
    			clCMemoInfo.strPassword = "";
 //   		}
    		if(CBFolder.isChecked()){
    			clCMemoInfo.iType = CMemoInfo.Type_Folder;
    		}else{
    			clCMemoInfo.iType = CMemoInfo.Type_Memo;
    		}
    		clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;
    		m_clCNoteDBCtrl.Create(clCMemoInfo);
    	}else if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Memo){
 //   		if(CBEncode.isChecked()){
 //   			clCMemoInfo.strPassword = m_strPassWord;
 //   		}else{
    			clCMemoInfo.strPassword = "";
 //   		}
    		if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){
    			Cursor cur = m_clCNoteDBCtrl.getMemoRec(m_ExtraData_MemoID);
    			cur.moveToFirst();
    			int index = cur.getColumnIndex(CNoteDBCtrl.KEY_type);
    			int value = cur.getInt(index);
    			if(value == CMemoInfo.Type_Folder){
    				clCMemoInfo.iPreId	=	m_ExtraData_MemoID;
    				m_clCNoteDBCtrl.Create(clCMemoInfo);
    			}else{
    				m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo);
    			}     				
            }
    	}else if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Folder){
 //   		if(CBEncode.isChecked()){
 //   			clCMemoInfo.strPassword = m_strPassWord;
  //  		}else{
    			clCMemoInfo.strPassword = "";
   // 		}
    		m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo);
    	}else{
    		
    	}
    }

		/*
	 * menu.findItem(EXIT_ID);�ҵ��ض���MenuItem
	 * MenuItem.setIcon.��������menu��ť�ı���
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ITEM0, 0, "Ƥ������");
		menu.add(0, ITEM1, 0, "��������");
		menu.findItem(ITEM1);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0: 
				/*   menu button push action */ 


				
			break;
		case ITEM1: 
				/*   menu button push action */ 


			break;

		}
		return super.onOptionsItemSelected(item);}
}
