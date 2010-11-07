package com.main;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CNoteDBCtrl extends SQLiteOpenHelper {

	public static final String	KEY_id					= "_id";												
	public static final String	KEY_preid				= "preid";											
	public static final String	KEY_type				= "type";
	public static final String	KEY_isremind			= "isremind";
	public static final String	KEY_remindtime			= "remindtime";
	public static final String	KEY_createtime			= "createtime";
	public static final String	KEY_lastmodifytime		= "lastmodifytime";
	public static final String	KEY_iseditenable		= "iseditenable";
	public static final String	KEY_remindmask			= "remindmask";
	public static final String	KEY_detail				= "detail";
	public static final String	KEY_password			= "password";
	public static final String	KEY_isencode		= "isencode";
	// 数据库名称为data
	private static final String	DB_NAME			= "NoteWithYourMind.db";
	
	// 数据库表名
	private static final String	DB_TABLE				= "Notes";
	private static final String	DB_TABLE_PassWord		= "PassWord";
	// 数据库版本
	private static final int	DB_VERSION		= 2;
	
	private static final String	DB_CREATE		= "CREATE TABLE  if not exists " + DB_TABLE + " (" + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
		KEY_preid + " INTERGER,"+ KEY_type + " INTERGER," + KEY_isremind + " INTERGER," + KEY_remindtime + " double," + KEY_createtime + " double,"+
		KEY_lastmodifytime + " double,"+ KEY_iseditenable + " INTERGER,"+ KEY_remindmask + " INTERGER,"+ KEY_detail + " TEXT," + KEY_password + " TEXT," + KEY_isencode + " INTERGER )";

	private static final String	Trigger_CREATE	=	"create trigger delete_sub_rec before delete on " + DB_TABLE +" for each row " +
			"begin " +
			"delete from " + DB_TABLE + " where Preid=old._id;" +
			"end;";
	private static final String	DB_CREATE_Table_PassWord = "CREATE TABLE  if not exists "+ DB_TABLE_PassWord + " (" + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
		KEY_password+" TEXT)";
	private	SQLiteDatabase m_db;
	public CNoteDBCtrl(Context context) {		
		super( context, DB_NAME, null, DB_VERSION);
		m_db	=	this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( DB_CREATE );	
		db.execSQL( Trigger_CREATE );
		db.execSQL( DB_CREATE_Table_PassWord );
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_password, "");
		db.insert(DB_TABLE_PassWord, KEY_id, initialValues);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
		onCreate(db);
	} 
	
//	public Cursor findEncodeFolder()
//	{
//		return m_db.rawQuery("select _id from "+DB_TABLE+" where "+KEY_detail+"=?", new String[]{CMemoInfo.Encode_Folder});
//	}
	
//	public	Cursor	getMemoRootInfo()
//	{
//		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? order by "+KEY_type+" asc", new String[]{String.valueOf(0)});
//	}
	
	public	Cursor	getFolderInRoot()
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? and "+
					KEY_type+"=?", 
					new String[]{String.valueOf(0), String.valueOf(CMemoInfo.Type_Folder)});
	}
	
	public	Cursor	getMemosByID( int id )
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? order by "+KEY_type+" asc", new String[]{String.valueOf(id)});
	}
	
	public	Cursor	getMemoRec( int id )
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_id+"=?", new String[]{String.valueOf(id)});
	}
	
	public	Cursor	getRemindInfo()
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_isremind+"=?", new String[]{String.valueOf(1)});
	}
	public	Cursor	getEncodeInfo()
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_isencode+"=?", new String[]{String.valueOf(1)});
	}
	public	Cursor	getPassWord()
	{
		return	m_db.rawQuery("select * from "+DB_TABLE_PassWord, 
					null);
	}	
	public	void	setPassWord(String strPassWord)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_password, strPassWord);
		String[] whereValue={ Integer.toString(1)};
		m_db.update(DB_TABLE_PassWord, initialValues, KEY_id, whereValue);
	}
	public	void Create( CMemoInfo clCMemoInfo )
	{
		//m_db.execSQL("insert into DB_TABLE("+KEY_preid+","+KEY_type+","+KEY_isremind+","+KEY_remindtime+","+KEY_createtime+","+KEY_lastmodifytime+","+
		//		KEY_iseditenable+","+KEY_remindmask+","+KEY_detail+") values(?,?,?,?,?,?,?,?,?)"
		//			, new Object[]{clCMemoInfo.iPreId,clCMemoInfo.iType,clCMemoInfo.iIsRemind
		//			,clCMemoInfo.dRemindTime,clCMemoInfo.dCreateTime,clCMemoInfo.dLastModifyTime,clCMemoInfo.iIsEditEnable,
		//			clCMemoInfo.iRemindMask,clCMemoInfo.strDetail} );
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_preid, clCMemoInfo.iPreId);
		initialValues.put(KEY_type, clCMemoInfo.iType);
		initialValues.put(KEY_isremind, clCMemoInfo.iIsRemind);
		initialValues.put(KEY_remindtime, clCMemoInfo.dRemindTime);
		initialValues.put(KEY_createtime, clCMemoInfo.dCreateTime);
		initialValues.put(KEY_lastmodifytime, clCMemoInfo.dLastModifyTime);
		initialValues.put(KEY_iseditenable, clCMemoInfo.iIsEditEnable);
		initialValues.put(KEY_remindmask, clCMemoInfo.iRemindMask);
		initialValues.put(KEY_detail, clCMemoInfo.strDetail);
		initialValues.put(KEY_password, clCMemoInfo.strPassword);
		initialValues.put(KEY_isencode, clCMemoInfo.iIsEncode);

		m_db.insert(DB_TABLE, KEY_id, initialValues);
	}

	public	void Delete( Integer[] id )
	{
		int iCnt	=	id.length;
		
		for ( int i = 0; i < iCnt; ++i )
		{
			String[] temp = new String[]{id[i].toString()};
			m_db.execSQL( "delete from " + DB_TABLE + " where " + KEY_id + "=?", temp );
		}	
	}
	
	public	void Update( int id, CMemoInfo clCMemoInfo )
	{
//		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		if ( -1 != clCMemoInfo.iPreId )
		{
			cv.put(KEY_preid, clCMemoInfo.iPreId.toString());
		}
		
		if ( -1 != clCMemoInfo.iType )
		{
			cv.put(KEY_type, clCMemoInfo.iType.toString());
		}
		
		if ( -1 != clCMemoInfo.iIsRemind )
		{
			cv.put(KEY_isremind, clCMemoInfo.iIsRemind.toString());
		}
		
		if ( -1 != clCMemoInfo.dRemindTime )
		{
			cv.put(KEY_remindtime, Long.toString(clCMemoInfo.dRemindTime));
		}
		
		if ( -1 != clCMemoInfo.dCreateTime )
		{
			cv.put(KEY_createtime, Long.toString(clCMemoInfo.dCreateTime));
		}
		
		if ( -1 != clCMemoInfo.dLastModifyTime )
		{
			cv.put(KEY_lastmodifytime, Long.toString(clCMemoInfo.dLastModifyTime));
		}
		
		if ( -1 != clCMemoInfo.iIsEditEnable )
		{
			cv.put(KEY_iseditenable, clCMemoInfo.iIsEditEnable.toString());
		}
		
		if ( -1 != clCMemoInfo.iRemindMask )
		{
			cv.put(KEY_remindmask, clCMemoInfo.iRemindMask.toString());
		}
		
		if ( null != clCMemoInfo.strDetail )
		{
			cv.put(KEY_detail, clCMemoInfo.strDetail);
		}
		
		if ( null != clCMemoInfo.strPassword )
		{
			cv.put(KEY_password, clCMemoInfo.strPassword);
		}
		
		if ( -1 != clCMemoInfo.iIsEncode)
		{
			cv.put(KEY_isencode, clCMemoInfo.iIsEncode.toString());
		}
		
		String[] whereValue={ Integer.toString(id)};
		
		m_db.update(DB_TABLE, cv, KEY_id+"=?", whereValue);
	}
}
