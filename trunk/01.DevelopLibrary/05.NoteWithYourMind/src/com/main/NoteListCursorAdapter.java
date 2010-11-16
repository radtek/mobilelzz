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

import java.util.ArrayList;
import java.util.Calendar;
import android.content.DialogInterface;
import android.widget.EditText;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.ImageButton;

public class NoteListCursorAdapter extends CursorAdapter {
	public class CursorDataHolder{
		View btFolder;
		View btEncode;
		int iItemPos;
		boolean bCheckedStatus;
		int iDBID;
		CursorDataHolder(){
			btFolder = null;
			btEncode = null;
			iItemPos = CommonDefine.g_int_Invalid_ID;
			iDBID = CommonDefine.g_int_Invalid_ID;
			bCheckedStatus = false;
		}
	}
	private ArrayList<CursorDataHolder> m_CursorDataHolder;
	private boolean m_isSelectableStyle = false;
	private boolean m_isFolderSelectable = true;
	private Context m_context;
	private LayoutInflater m_inflater;
	private Cursor m_cursor;
	private Calendar m_c;
	private CNoteDBCtrl m_clCNoteDBCtrl;
	
	private View m_DialogView;
	//private NoteListUICtrl m_NoteListUICtrl;
	//private String  m_strPassWord;
	private int m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
	
	public NoteListCursorAdapter(Context context, Cursor c) {
		super(context, c);
		m_context = context;
		m_inflater = LayoutInflater.from(context);
		m_cursor = c;
		//m_NoteListUICtrl = NoteListUICtrl;
		m_CursorDataHolder = new ArrayList<CursorDataHolder>();
	}
 
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int iTypeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_type);
		int iTypeValue = cursor.getInt(iTypeIndex);
		int iDetailIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_detail);
		String sDetail = cursor.getString(iDetailIndex);
		TextView tV = (TextView)view.findViewById(R.id.noteitem_notetext);
		tV.setCompoundDrawablePadding(15);
		if(iTypeValue==CMemoInfo.Type_Folder){
			int iEncodeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_isencode);
			int iEncodeFlag = cursor.getInt(iEncodeIndex);
			if(iEncodeFlag==CMemoInfo.IsEncode_Yes){
				//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_FolderLocked, null, null, null);
				tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.folderlocked, 0, 0, 0);
			}else{
				//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_Folder, null, null, null);
				tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.folder, 0, 0, 0);
			}
		}else{
			//tV.setCompoundDrawables(NoteWithYourMind.g_drawable_Memo, null, null, null);
			tV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.memo, 0, 0, 0);
		}
		tV.setText(sDetail);
	}
 
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = null;
		if(m_isSelectableStyle)
		{
/*			int iIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_iseditenable);
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
					v = m_inflater.inflate(R.layout.notelistitem, parent, false);
				}
			}	*/	
		}
		else{
			v = m_inflater.inflate(R.layout.notelistitem, parent, false);
		}	
		return v;
/*		Button clBTFolder=null;
		Button  clBTLockIf=null;		
		if(iTypeValue == CMemoInfo.Type_Folder){
			clBTFolder = (Button) v.findViewById(R.id.B_FolderItem_FolderIcon);
			//clBTFolder.setBackgroundColor(Color.argb(0, 0, 0, 0));
	        clBTFolder.setOnClickListener(new Button.OnClickListener(){
	        	public void onClick(View v)
	        	{		
	        		m_DBId2BeEdited = findDBIdByFolderButton(v);
	        		m_DialogView = m_inflater.inflate(R.layout.dialognewfolder, null);
					AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
						.setIcon(R.drawable.clock)
						.setTitle("请编辑文件夹名称")
						.setView(m_DialogView)
						.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int i)
							{
								EditText FolderNameText = (EditText) m_DialogView.findViewById(R.id.foldername_edit);
				        		String strFolderNameText = FolderNameText.getText().toString();
				        		if(strFolderNameText.length()>0){
				        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		 						    m_c = Calendar.getInstance();
									clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
				            		clCMemoInfo.strDetail	=	strFolderNameText;
				            		m_clCNoteDBCtrl.Update(m_DBId2BeEdited,clCMemoInfo); 
//				            		m_NoteListUICtrl.updateListData();
				            		m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
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
        			m_DBId2BeEdited = findDBIdByEncodeButton(v);
        			int iEncodeValue=CMemoInfo.IsEncode_Invalid;
        			Cursor cur = m_clCNoteDBCtrl.getMemoRec(m_DBId2BeEdited);
        			if(cur!=null){
        				cur.moveToFirst();
        				int Index = cur.getColumnIndex(CNoteDBCtrl.KEY_isencode);
    					iEncodeValue = cur.getInt(Index);
        			}
        			cur = null;
					if(iEncodeValue == CMemoInfo.IsEncode_Yes){
	
						m_DialogView = m_inflater.inflate(R.layout.dialog_encodesetting, null);
						
						AlertDialog clDlgNewFolder = new AlertDialog.Builder(m_context)	
							.setIcon(R.drawable.clock)
							.setTitle("取消加密请输入密码")
							.setView(m_DialogView)
							.setPositiveButton("确定",new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int i)
								{
					        		EditText PassWord = (EditText) m_DialogView.findViewById(R.id.ET_encodesetting);
					        		String strPassWord = PassWord.getText().toString();
					        		if(strPassWord.length()>0){
										if(strPassWord.equals(CommonDefine.g_str_PassWord)){
											CMemoInfo clCMemoInfo	=	new	CMemoInfo();
				 						    m_c = Calendar.getInstance();
											clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
						            		clCMemoInfo.iIsEncode	=	CMemoInfo.IsEncode_No;
						            		m_clCNoteDBCtrl.Update(m_DBId2BeEdited,clCMemoInfo);     		
						            		m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
//						            		m_NoteListUICtrl.updateListData();
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
				        			CMemoInfo clCMemoInfo	=	new	CMemoInfo();
		 						    m_c = Calendar.getInstance();
									clCMemoInfo.dLastModifyTime = m_c.getTimeInMillis();							
				            		clCMemoInfo.iIsEncode	=	CMemoInfo.IsEncode_Yes;
				            		m_clCNoteDBCtrl.Update(m_DBId2BeEdited,clCMemoInfo);     		
				            		m_DBId2BeEdited = CommonDefine.g_int_Invalid_ID;
//				            		m_NoteListUICtrl.updateListData();
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
		CursorDataHolder clHolder = new CursorDataHolder();
		clHolder.btFolder = (View)clBTFolder;
		clHolder.btEncode = (View)clBTLockIf;
		int iIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_id);
		int iValue = cursor.getInt(iIndex);
		clHolder.iDBID = iValue;
		m_CursorDataHolder.add(clHolder);
		*/
		
	}
	public void updateCursor(){
		m_cursor.deactivate();
		m_cursor.requery();
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
	/*public void TransforPassWord(String strPassWord){
		m_strPassWord = strPassWord;
	}*/
	private int findDBIdByFolderButton(View v2BeFinded){
		int iDBId = CommonDefine.g_int_Invalid_ID;
		int iCount = m_CursorDataHolder.size();
		for(int i = 0; i < iCount; i++){
			CursorDataHolder clHolder = m_CursorDataHolder.get(i);
			if(clHolder.btFolder == v2BeFinded){
				iDBId = clHolder.iDBID;
			}
		}
		return iDBId;
	}
	private int findDBIdByEncodeButton(View v2BeFinded){
		int iDBId = CommonDefine.g_int_Invalid_ID;
		int iCount = m_CursorDataHolder.size();
		for(int i = 0; i < iCount; i++){
			CursorDataHolder clHolder = m_CursorDataHolder.get(i);
			if(clHolder.btEncode == v2BeFinded){
				iDBId = clHolder.iDBID;
			}
		}
		return iDBId;
	}
}
