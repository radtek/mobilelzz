package com.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class NoteListCursorAdapter extends CursorAdapter {
	private LayoutInflater m_inflater;
	public NoteListCursorAdapter(Context context, Cursor c) {
		super(context, c);
		m_inflater = LayoutInflater.from(context);
	}
 
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
	}
 
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = m_inflater.inflate(R.layout.memolistitem, parent, false);
		return v;
	}
}