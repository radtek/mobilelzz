package com.main;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class CNoteDBCtrl extends SQLiteOpenHelper {

	private	SQLiteDatabase m_db;
	public CNoteDBCtrl(Context context, String name, CursorFactory factory,
			int version) {
		
		super( context, "Note.db", null, 2);
		// TODO Auto-generated constructor stub
		
		m_db	=	this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL( "create table if not exists Memo( _id integer PRIMARY KEY AUTOINCREMENT, preid integer, type integer, " +
				"isremind integer, remindtime double, createtime double, lastmodifytime double," +
				"iseditenable integer, remindmask integer, detail string ) " );
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS Memo");
		onCreate(db);
	} 
	
	public	Cursor	getMemoRootInfo()
	{
		return	m_db.rawQuery("select * from Memo where Preid=?", new String[]{String.valueOf(0)});
	}
	
	public	Cursor	getMemoInFolder( int id )
	{
		return	m_db.rawQuery("select * from Memo where Preid=?", new String[]{String.valueOf(id)});
	}
	
	public	Cursor	getRemindInfo()
	{
		return	m_db.rawQuery("select * from Memo where IsRemind=?", new String[]{String.valueOf(1)});
	}
	
	public	void Create( CMemoInfo clCMemoInfo )
	{
//		Cursor clCursor	=	m_db.rawQuery( "select max(ID) from Memo", null );
//		int icolumn	=	clCursor.getColumnIndex("ID");
//		if(clCursor.moveToFirst())
//		{
//			int iCount	=	0;	
//			if ( -1 == icolumn )
//			{
//				iCount	=	1;
//			}
//			else
//			{
//				iCount	=	clCursor.getInt(icolumn);
//				++ iCount;			
//			}
//
//		
//			clCMemoInfo.iId	=	iCount;
//			
//
//		} 
		m_db.execSQL("insert into Memo(Preid,Type,IsRemind,RemindTime,CreateTime,LastModifyTime," +
				" IsEditEnable,RemindMask,Detail) values(?,?,?,?,?,?,?,?,?)"
					, new Object[]{clCMemoInfo.iPreId,clCMemoInfo.iType,clCMemoInfo.iIsRemind
					,clCMemoInfo.dRemindTime,clCMemoInfo.dCreateTime,clCMemoInfo.dLastModifyTime,clCMemoInfo.iIsEditEnable,
					clCMemoInfo.iRemindMask,clCMemoInfo.strDetail} );

	}

	public	void Delete( int[] id )
	{
		
	}
	
	public	void Update( int id, CMemoInfo clCMemoInfo )
	{
//		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		if ( -1 != clCMemoInfo.iPreId )
		{
			cv.put("Preid", clCMemoInfo.iPreId.toString());
		}
		
		if ( -1 != clCMemoInfo.iType )
		{
			cv.put("Type", clCMemoInfo.iType.toString());
		}
		
		if ( -1 != clCMemoInfo.iIsRemind )
		{
			cv.put("IsRemind", clCMemoInfo.iIsRemind.toString());
		}
		
		if ( -1.0 != clCMemoInfo.dRemindTime )
		{
			cv.put("RemindTime", Long.toString(clCMemoInfo.dRemindTime));
		}
		
		if ( -1.0 != clCMemoInfo.dCreateTime )
		{
			cv.put("CreateTime", Long.toString(clCMemoInfo.dCreateTime));
		}
		
		if ( -1.0 != clCMemoInfo.dLastModifyTime )
		{
			cv.put("LastModifyTime", Long.toString(clCMemoInfo.dLastModifyTime));
		}
		
		if ( -1 != clCMemoInfo.iIsEditEnable )
		{
			cv.put("IsEditEnable", clCMemoInfo.iIsEditEnable.toString());
		}
		
		if ( -1 != clCMemoInfo.iRemindMask )
		{
			cv.put("RemindMask", clCMemoInfo.iRemindMask.toString());
		}
		
		if ( null != clCMemoInfo.strDetail )
		{
			cv.put("Detail", clCMemoInfo.strDetail);
		}
		
		String[] whereValue={ Integer.toString(id)};
		
		m_db.update("Memo", cv, "ID=?", whereValue);
	}
}
