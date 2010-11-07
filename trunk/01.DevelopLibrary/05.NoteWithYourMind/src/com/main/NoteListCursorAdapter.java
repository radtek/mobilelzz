package com.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.app.AlertDialog;
import java.util.Calendar;
import android.content.DialogInterface;
import android.widget.EditText;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.ImageButton;



public class NoteListCursorAdapter extends CursorAdapter {
	private boolean m_isSelectableStyle = false;
	private boolean m_isFolderSelectable = true;
	private Context m_context;
	private LayoutInflater m_inflater;
	private Cursor m_cursor;
	private Calendar m_c;
	private CNoteDBCtrl m_clCNoteDBCtrl;
	private String  m_strPassWord;
	
	public NoteListCursorAdapter(Context context, Cursor c) {
		super(context, c);
		m_context = context;
		m_inflater = LayoutInflater.from(context);
		m_cursor = c;
	}
 
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int iDetailIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_detail);
		String sDetail = cursor.getString(iDetailIndex);
		TextView tV = (TextView)view.findViewById(R.id.memoitem_memotext);
		tV.setText(sDetail);
	}
 
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v;
		int iTypeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_type);
		int iTypeValue = cursor.getInt(iTypeIndex);
		if(m_isSelectableStyle)
		{
			int iIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_iseditenable);
			int iValue = cursor.getInt(iIndex);
			if(iValue != CMemoInfo.IsEditEnable_Disable)
			{

				if( iTypeValue==CMemoInfo.Type_Folder ){
					if(m_isFolderSelectable){
						v = m_inflater.inflate(R.layout.memolistitemfolderwithselect, parent, false);
					}else{

						v = m_inflater.inflate(R.layout.memolistitemfolder, parent, false);		
					}					
				}else{
						v = m_inflater.inflate(R.layout.memolistitemselect, parent, false);
				}

			}else{
				if( iTypeValue == CMemoInfo.Type_Folder ){
					v = m_inflater.inflate(R.layout.memolistitemfolder, parent, false);
				}else{
					v = m_inflater.inflate(R.layout.memolistitem, parent, false);
				}
			}		
		}
		else{
			if(iTypeValue == CMemoInfo.Type_Folder){
				v = m_inflater.inflate(R.layout.memolistitemfolder, parent, false);

			}else{
				v = m_inflater.inflate(R.layout.memolistitem, parent, false);

			}
		}	
		if(iTypeValue == CMemoInfo.Type_Folder){
			Button clBTFolder = (Button) v.findViewById(R.id.B_FolderItem_FolderIcon);
			//clBTFolder.setBackgroundColor(Color.argb(0, 0, 0, 0));
	        clBTFolder.setOnClickListener(new Button.OnClickListener(){
	        	public void onClick(View v)
	        	{		
					final View DialogView = m_inflater.inflate(R.layout.dialognewfolder, null);
					
					AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
						.setIcon(R.drawable.clock)
						.setTitle("请编辑文件夹名称")
						.setView(DialogView)
						.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int i)
							{
								int Index = m_cursor.getColumnIndex(CNoteDBCtrl.KEY_id);
								int iIDValue = m_cursor.getInt(Index);
								
								EditText FolderNameText = (EditText) DialogView.findViewById(R.id.foldername_edit);
				        		String strFolderNameText = FolderNameText.getText().toString();
				        		if(strFolderNameText.length()>0){
				        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		 						    m_c = Calendar.getInstance();
									clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
				            		clCMemoInfo.strDetail	=	strFolderNameText;
				            		m_clCNoteDBCtrl.Update(iIDValue,clCMemoInfo);     		
				            		FolderNameText.setText("");

				            		Toast toast = Toast.makeText(m_context, "保存成功", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
				            		toast.show();
				        		}else{
				        			Toast toast = Toast.makeText(m_context, "请输入文件夹名称", Toast.LENGTH_SHORT);
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


			int Index = cursor.getColumnIndex(CNoteDBCtrl.KEY_isencode);
			int iEncodeValue = cursor.getInt(Index);

			Button  clBTLockIf;			
			if(iEncodeValue == CMemoInfo.IsEncode_Yes){
				clBTLockIf = (Button)  v.findViewById(R.id.B_FolderItem_LockIcon);
				clBTLockIf.setBackgroundResource(R.drawable.itemicon_lock);
			}else{
				clBTLockIf = (Button)  v.findViewById(R.id.B_FolderItem_LockIcon);
				clBTLockIf.setBackgroundResource(R.drawable.itemicon_unlock);
			}	
			//clBTLockIf.setBackgroundColor(Color.argb(0, 0, 0, 0));
	        clBTLockIf.setOnClickListener(new Button.OnClickListener(){
        		public void onClick(View v)
	        	{
					int Index = m_cursor.getColumnIndex(CNoteDBCtrl.KEY_isencode);
					int iEncodeValue = m_cursor.getInt(Index);
					
					if(iEncodeValue == CMemoInfo.IsEncode_Yes){
	
						final View DialogView = m_inflater.inflate(R.layout.dialog_encodesetting, null);
						
						AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
							.setIcon(R.drawable.clock)
							.setTitle("取消加密请输入密码")
							.setView(DialogView)
							.setPositiveButton("确定",new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int i)
								{
					        		EditText PassWord = (EditText) DialogView.findViewById(R.id.ET_passwordsetting);
					        		String strPassWord = PassWord.getText().toString();
					        		if(strPassWord.length()>0){

										if(strPassWord.equals(m_strPassWord)){
						            		Toast toast = Toast.makeText(m_context, "加密已取消", Toast.LENGTH_SHORT);
						            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
						            		toast.show();

										}else{
						        			Toast toast = Toast.makeText(m_context, "密码错误!请重新输入", Toast.LENGTH_SHORT);
						            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
						            		toast.show();
										}

					        		}else{
					        			Toast toast = Toast.makeText(m_context, "请输入私人密码", Toast.LENGTH_SHORT);
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
					}else{
						AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
							.setIcon(R.drawable.clock)
							.setTitle("设置为加密文件夹")
							.setPositiveButton("确定",new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int i)
								{
									int Index = m_cursor.getColumnIndex(CNoteDBCtrl.KEY_id);
									int iIDValue = m_cursor.getInt(Index);
									
				        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		 						    m_c = Calendar.getInstance();
									clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
				            		clCMemoInfo.iIsEncode	=	CMemoInfo.IsEncode_Yes;
				            		m_clCNoteDBCtrl.Update(iIDValue,clCMemoInfo);     		

				            		Toast toast = Toast.makeText(m_context, "已设置为加密文件夹", Toast.LENGTH_SHORT);
				            		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0 );
				            		toast.show();

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
	        	}
		    });
		}else if(iTypeValue == CMemoInfo.Type_Memo){
			
		}else{	
			
		}
		
		return v;
	}
	
	public void setSelectableStyle(boolean bEnable){
		m_isSelectableStyle = bEnable;
	}
	public void setFolderSelectable(boolean bEnable){
		m_isFolderSelectable = bEnable;
	}
	
	public Cursor getCursor(){
		return m_cursor;
	}
	public void setNoteDBCtrl(CNoteDBCtrl clCNoteDBCtrl){
		m_clCNoteDBCtrl = clCNoteDBCtrl;
	}
	public void TransforPassWord(String strPassWord){
		m_strPassWord = strPassWord;
	}

}
