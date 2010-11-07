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
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;


public class NoteWithYourMind extends Activity {
	public enum NewNoteKindEnum{
		NewNoteKind_Unknown,
		NewNoteKind_InRoot,
		NewNoteKind_InFolder,
	}
	public static String ExtraData_MemoID = "com.main.ExtraData_MemoID";
	public static String ExtraData_NewNoteKind = "com.main.ExtraData_NewNoteKind";
	private static String m_strPassWord = "";
	
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
        ImageButton btNewMemo = (ImageButton)findViewById(R.id.B_main_NewMemo);
        ImageButton btNewFolder = (ImageButton)findViewById(R.id.B_main_NewFolder);
		btNewMemo.setVisibility(View.GONE);
		btNewFolder.setVisibility(View.GONE);
		
        m_ExtraData_MemoID = CMemoInfo.Id_Invalid;
        m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;
        Intent iExtraData = this.getIntent();
		m_ExtraData_MemoID = iExtraData.getIntExtra(ExtraData_MemoID, CMemoInfo.Id_Invalid );
		m_ExtraData_NewNoteKind = (NewNoteKindEnum)iExtraData.getSerializableExtra(ExtraData_NewNoteKind);
		if(m_ExtraData_NewNoteKind==null){
			m_ExtraData_NewNoteKind = NewNoteKindEnum.NewNoteKind_Unknown;
		}
        m_clCNoteDBCtrl	=	new	CNoteDBCtrl( this );
        UpdateViewStatus();

    	Cursor curPassWord = m_clCNoteDBCtrl.getPassWord();

		startManagingCursor(curPassWord);
        curPassWord.moveToFirst();
		int count = curPassWord.getCount();
		
    	if(count > 0 ){	
			int index = curPassWord.getColumnIndex(CNoteDBCtrl.KEY_type);
			int value = curPassWord.getInt(index);
			if(value == CMemoInfo.Type_PassWord){
				index = curPassWord.getColumnIndex(CNoteDBCtrl.KEY_password);
		    	m_strPassWord = curPassWord.getString(index);			
			}else{
				m_strPassWord = "";			
			}		

		}else{
			m_strPassWord = "";
		}
		

		Button clBTSkin = (Button) findViewById(R.id.B_main_setting_skin);
        clBTSkin.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		
        	}
        });

		Button clBTEncode = (Button) findViewById(R.id.B_main_setting_encode);
        clBTEncode.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v)
        	{
        		EncodeSettingDlg();
        	}
        });

        c = Calendar.getInstance();
        ImageButton clBTSave = (ImageButton) findViewById(R.id.B_main_Save);
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
        
        ImageButton clBTView = (ImageButton) findViewById(R.id.B_main_View);
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
        		if(isChecked)
        		{
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
        		}
        	}
        });
    }
    private void UpdateViewStatus(){
    	ImageButton btViewList = (ImageButton)findViewById(R.id.B_main_View);
    	if(m_ExtraData_NewNoteKind == NewNoteKindEnum.NewNoteKind_Unknown){
    		btViewList.setVisibility(View.VISIBLE);
    	}else{
    		btViewList.setVisibility(View.GONE);
    	}
    	if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){
    		Cursor curExtraMemo = m_clCNoteDBCtrl.getMemoRec(m_ExtraData_MemoID);
        	startManagingCursor(curExtraMemo);
        	if(curExtraMemo.getCount()>0){
        		curExtraMemo.moveToFirst();
        		int index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_type);
        		int TypeValue = curExtraMemo.getInt(index);
        		if(TypeValue==CMemoInfo.Type_Folder){
        			UpdateStatusOfMemoInfo("",false);
        		}else{
        			index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_detail);
        	    	String strDetail = curExtraMemo.getString(index);
        	    	index = curExtraMemo.getColumnIndex(CNoteDBCtrl.KEY_isremind);
        	    	int isRemind = curExtraMemo.getInt(index);
        	    	if(isRemind == CMemoInfo.IsRemind_Yes){
        	    		UpdateStatusOfMemoInfo(strDetail,true);
        	    	}else{
        	    		UpdateStatusOfMemoInfo(strDetail,false);
        	    	}
        		}
        	}else{
        		UpdateStatusOfMemoInfo("",false);
        	}

    	}else{
    		UpdateStatusOfMemoInfo("",false);
    	}
	}
    private void UpdateStatusOfMemoInfo(String detail, boolean bIsRemind){
    	EditText etDetail = (EditText)findViewById(R.id.ET_main_Memo);
    	CheckBox cbIsRemind = (CheckBox)findViewById(R.id.CB_main_IsWarning);
    	etDetail.setText(detail);
    	cbIsRemind.setChecked(bIsRemind);
    }

    private void SaveEditData(String strMemoText)
    {
    	CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		clCMemoInfo.strDetail	=	strMemoText;
		clCMemoInfo.dLastModifyTime = c.getTimeInMillis();
		
		if(m_ExtraData_MemoID!=CMemoInfo.Id_Invalid){
			if(m_ExtraData_NewNoteKind==NewNoteKindEnum.NewNoteKind_InFolder){
				clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
	    		clCMemoInfo.iType = CMemoInfo.Type_Memo;
	        	clCMemoInfo.iPreId	=	m_ExtraData_MemoID;
	        	m_clCNoteDBCtrl.Create(clCMemoInfo);
			}else{
				m_clCNoteDBCtrl.Update(m_ExtraData_MemoID,clCMemoInfo);
			}
        }else{
        	clCMemoInfo.iIsEditEnable = CMemoInfo.IsEditEnable_Enable;
    		clCMemoInfo.iType = CMemoInfo.Type_Memo;
        	clCMemoInfo.iPreId	=	CMemoInfo.PreId_Root;
        	m_clCNoteDBCtrl.Create(clCMemoInfo);
        }
    }

    private void EncodeSettingDlg()
	{
		if(m_strPassWord==""){
			
				LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
				final View DialogView = factory.inflate(R.layout.dialog_passwordsetting, null);
				
				AlertDialog clDlgNewFolder = new AlertDialog.Builder(NoteWithYourMind.this)	
					.setIcon(R.drawable.clock)
					.setTitle("����������˽������")
					.setView(DialogView)
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
			        		EditText PassWord = (EditText) DialogView.findViewById(R.id.ET_passwordsetting);
			        		String strPassWord = PassWord.getText().toString();
			        		if(strPassWord.length()>0){
			        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
	 						    c = Calendar.getInstance();
								clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
			            		clCMemoInfo.strPassword = strPassWord;	
			            		clCMemoInfo.iPreId = CMemoInfo.Id_Invalid;										
			            		clCMemoInfo.iType = CMemoInfo.Type_PassWord;		            		
			            		m_clCNoteDBCtrl.Create(clCMemoInfo);
								m_strPassWord = strPassWord;
			            		PassWord.setText("");
			            		Toast toast = Toast.makeText(NoteWithYourMind.this, "˽������������", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}else{
			        			Toast toast = Toast.makeText(NoteWithYourMind.this, "������˽������", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}
							dialog.cancel();
						}
					})
					.setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
							dialog.cancel();
						}
					})

					
					.create();

				clDlgNewFolder.show(); 

		}else{

				LayoutInflater factory = LayoutInflater.from(NoteWithYourMind.this);
				final View DialogView = factory.inflate(R.layout.dialog_passwordchang, null);
				
				AlertDialog clDlgNewFolder = new AlertDialog.Builder(NoteWithYourMind.this)	
					.setIcon(R.drawable.clock)
					.setView(DialogView)
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
			        		EditText PassWord_old = (EditText) DialogView.findViewById(R.id.ET_password_old);
			        		String strPassWord_old = PassWord_old.getText().toString();
			        		EditText PassWord_new = (EditText) DialogView.findViewById(R.id.ET_password_new);
			        		String strPassWord_new = PassWord_new.getText().toString();
			        		EditText PassWord_new2 = (EditText) DialogView.findViewById(R.id.ET_password_new2);
			        		String strPassWord_new2 = PassWord_new2.getText().toString();
							
			        		if(strPassWord_old.equals( m_strPassWord)){

								if(strPassWord_new.equals(strPassWord_new2)){
				        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		 						    c = Calendar.getInstance();
									clCMemoInfo.dLastModifyTime = c.getTimeInMillis();							
				            		clCMemoInfo.strPassword = strPassWord_new;	
				            		clCMemoInfo.iPreId = CMemoInfo.Id_Invalid;										
				            		clCMemoInfo.iType = CMemoInfo.Type_PassWord;		            		
				            		m_clCNoteDBCtrl.Create(clCMemoInfo);
									m_strPassWord = strPassWord_new;
									PassWord_old.setText("");
									PassWord_new.setText("");
									PassWord_new2.setText("");
									Toast toast = Toast.makeText(NoteWithYourMind.this, "������������", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
				            		toast.show();
									dialog.cancel();
								}else{
				        			Toast toast = Toast.makeText(NoteWithYourMind.this, "�����벻һ��!����������", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
									PassWord_new.setText("");
									PassWord_new2.setText("");
				            		toast.show();

								}									

			        		}else{
			        			PassWord_old.setText("");
								PassWord_new.setText("");
								PassWord_new2.setText("");
			        			Toast toast = Toast.makeText(NoteWithYourMind.this, "�������!����������", Toast.LENGTH_SHORT);
			            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
			            		toast.show();
			        		}

						}
					})
					.setNegativeButton("ȡ��",new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int i)
						{
							dialog.cancel();
						}
					})

					
					.create();

				clDlgNewFolder.show(); 

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
			EncodeSettingDlg();
			break;

		}
		return super.onOptionsItemSelected(item);}
}
