package com.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NoteListCursorAdapter extends CursorAdapter {
	private boolean m_isSelectableStyle = false;
	private boolean m_isFolderSelectable = true;
	private LayoutInflater m_inflater;
	private Cursor m_cursor;
	public NoteListCursorAdapter(Context context, Cursor c) {
		super(context, c);
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
				if(iTypeValue==CMemoInfo.Type_Folder&&(!m_isFolderSelectable)){
					v = m_inflater.inflate(R.layout.memolistitem, parent, false);
				}else{
					v = m_inflater.inflate(R.layout.memolistitemselect, parent, false);
				}				
			}else{
				v = m_inflater.inflate(R.layout.memolistitem, parent, false);
			}		
		}else{
			v = m_inflater.inflate(R.layout.memolistitem, parent, false);
		}	
		if(iTypeValue == CMemoInfo.Type_Folder){
			TextView tv = (TextView) v.findViewById(R.id.memoitem_memotext);
			tv.setCompoundDrawables(null, null, null, null);
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
}