package com.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDBAdapter
{
	// 用于打印log
	private static final String	TAG				= "NoteDBAdapter";
	
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

	// 数据库名称为data
	private static final String	DB_NAME			= "NoteWithYourMind.db";
	
	// 数据库表名
	private static final String	DB_TABLE		= "Notes";
	
	// 数据库版本
	private static final int	DB_VERSION		= 3;

	// 本地Context对象
	private Context				mContext		= null;
	
	//创建一个表
	//private static final String	DB_CREATE		= "CREATE TABLE  if not exists " + DB_TABLE + " (" + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
	//	KEY_preid + " INTERGER,"+ KEY_type + " INTERGER," + KEY_isremind + " INTERGER," + KEY_remindtime + " double," + KEY_createtime + " double,"+
	//	KEY_lastmodifytime + " double,"+ KEY_iseditenable + " INTERGER,"+ KEY_remindmask + " INTERGER,"+ KEY_detail + " INTERGER )";
 
	private static final String	DB_CREATE		= "CREATE TABLE  if not exists " + DB_TABLE + " (" + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
	KEY_preid + " INTERGER,"+ KEY_detail + " TEXT )";

	// 执行open（）打开数据库时，保存返回的数据库对象
	private SQLiteDatabase		mSQLiteDatabase	= null;

	// 由SQLiteOpenHelper继承过来
	private DatabaseHelper		mDatabaseHelper	= null;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		/* 构造函数-创建一个数据库 */
		DatabaseHelper(Context context)
		{
			super(context, DB_NAME, null, DB_VERSION);
		}

		/* 创建一个表 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// 数据库没有表时创建一个
			db.execSQL(DB_CREATE);
		}

		/* 升级数据库 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
			onCreate(db);
		}
	}
	
	/* 构造函数-取得Context */
	public NoteDBAdapter(Context context)
	{
		mContext = context;
	}


	// 打开数据库，返回数据库对象
	public void open() throws SQLException
	{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}
	// 关闭数据库
	public void close()
	{
		mDatabaseHelper.close();
	}

	/* 插入一条数据 */
	public long insertData(CMemoInfo clCMemoInfo)
	{
		//m_db.execSQL("insert into DB_TABLE(KEY_id,KEY_type,KEY_isremind,KEY_remindtime,KEY_createtime,KEY_lastmodifytime," +
		//		" KEY_iseditenable,KEY_remindmask,KEY_detail) values(?,?,?,?,?,?,?,?,?)"
		//			, new Object[]{clCMemoInfo.iPreId,clCMemoInfo.iType,clCMemoInfo.iIsRemind
		//			,clCMemoInfo.dRemindTime,clCMemoInfo.dCreateTime,clCMemoInfo.dLastModifyTime,clCMemoInfo.iIsEditEnable,
		//			clCMemoInfo.iRemindMask,clCMemoInfo.strDetail} );		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_preid, clCMemoInfo.iPreId);
		//initialValues.put(KEY_type, clCMemoInfo.iType);
		//initialValues.put(KEY_isremind, clCMemoInfo.iIsRemind);
		//initialValues.put(KEY_remindtime, clCMemoInfo.dRemindTime);
		//initialValues.put(KEY_createtime, clCMemoInfo.dCreateTime);
		//initialValues.put(KEY_lastmodifytime, clCMemoInfo.dLastModifyTime);
		//initialValues.put(KEY_iseditenable, clCMemoInfo.iIsEditEnable);
		//initialValues.put(KEY_remindmask, clCMemoInfo.iRemindMask);
		initialValues.put(KEY_detail, clCMemoInfo.strDetail);

		return mSQLiteDatabase.insert(DB_TABLE, KEY_id, initialValues);
	}

	/* 删除一条数据 */
	public boolean deleteData(long rowId)
	{
		return mSQLiteDatabase.delete(DB_TABLE, KEY_id + "=" + rowId, null) > 0;
	}

	/* 通过Cursor查询所有数据 */
	public Cursor getMemoRootInfo()
	{
		return mSQLiteDatabase.query(DB_TABLE, new String[] { KEY_id, KEY_preid, KEY_detail }, null, null, null, null, null);
		//return mSQLiteDatabase.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=?", new String[]{String.valueOf(0)});
	}

}

