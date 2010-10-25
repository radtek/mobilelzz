package com.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NoteListCursorAdapter extends CursorAdapter {
	private LayoutInflater m_inflater;
	public NoteListCursorAdapter(Context context, Cursor c) {
		super(context, c);
		m_inflater = LayoutInflater.from(context);
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
		View v = m_inflater.inflate(R.layout.memolistitem, parent, false);
		int iTypeIndex = cursor.getColumnIndex(CNoteDBCtrl.KEY_type);
		int iTypeValue = cursor.getInt(iTypeIndex);
		if(iTypeValue == CMemoInfo.Type_Folder){
			v.setBackgroundColor(Color.YELLOW);
		}else if(iTypeValue == CMemoInfo.Type_Memo){
			
		}else{			
		}
		
		return v;
	}
}