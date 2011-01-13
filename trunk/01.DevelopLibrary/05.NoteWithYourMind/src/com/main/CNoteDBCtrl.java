package com.main;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

//SDSQLiteOpenHelper
public class CNoteDBCtrl extends  SQLiteOpenHelper{

	public static final String	KEY_id					= "_id";												
	public static final String	KEY_preid				= "preid";											
	public static final String	KEY_type				= "type";
	public static final String	KEY_isremind			= "isremind";
	public static final String	KEY_remindtime			= "remindtime";
	public static final String	KEY_isremindable		= "isremindable";
	public static final String	KEY_remindtype			= "remindtype";
	public static final String	KEY_createtime			= "createtime";
	public static final String	KEY_lastmodifytime		= "lastmodifytime";
	public static final String	KEY_iseditenable		= "iseditenable";
	public static final String	KEY_detail				= "detail";
	public static final String	KEY_password			= "password";
	public static final String	KEY_isencode			= "isencode";
	
	public static final String	KEY_monday				= "monday";
	public static final String	KEY_tuesday				= "tuesday";
	public static final String	KEY_wednesday			= "wednesday";
	public static final String	KEY_thursday			= "thursday";
	public static final String	KEY_friday				= "friday";
	public static final String	KEY_staturday			= "staturday";
	public static final String	KEY_sunday				= "sunday";
	
	public static final String	KEY_isHaveAudioData		= "ishavevoice";
	public static final String	KEY_audioDataName		= "audiodataname";
	
	public static final String	KEY_isRing				= "isring";
	public static final String	KEY_isVibrate			= "isvibrate";
	
	public static final String	KEY_rec_count_in_folder = "reccountinfolder";
	// 数据库名称为data
	private static final String	DB_NAME			= "NoteWithYourMind.db";
	
	// 数据库表名
	private static final String	DB_TABLE				= "Notes";
	private static final String	DB_TABLE_PassWord		= "PassWord";
	// 数据库版本
	private static final int	DB_VERSION		= 4;
	
	private static final String	DB_CREATE		= "CREATE TABLE  if not exists " + DB_TABLE + " (" 
												+ KEY_id 				+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
												+ KEY_preid 			+ " INTERGER,"
												+ KEY_type 				+ " INTERGER," 
												+ KEY_isremind 			+ " INTERGER," 
												+ KEY_remindtime 		+ " double," 
												+ KEY_isremindable 		+ " INTERGER,"
												+ KEY_remindtype		+ " INTERGER,"
												+ KEY_createtime 		+ " double,"
												+ KEY_lastmodifytime 	+ " double,"
												+ KEY_iseditenable 		+ " INTERGER,"											
												+ KEY_detail 			+ " TEXT," 
												+ KEY_password 			+ " TEXT," 
												+ KEY_isencode 			+ " INTERGER,"
												+ KEY_monday 			+ " INTERGER,"
												+ KEY_tuesday 			+ " INTERGER,"
												+ KEY_wednesday 		+ " INTERGER,"
												+ KEY_thursday 			+ " INTERGER,"
												+ KEY_friday 			+ " INTERGER,"
												+ KEY_staturday 		+ " INTERGER,"
												+ KEY_sunday 			+ " INTERGER,"
												+ KEY_isHaveAudioData	+ " INTERGER,"
												+ KEY_audioDataName		+ " TEXT,"
												+ KEY_isRing			+ " INTERGER,"
												+ KEY_isVibrate			+ " INTERGER"
												+ ")";

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
	public File getDataBaseFile(){
//		File data = Environment.getDataDirectory();
//		String filepath = m_db.getPath()+DB_NAME;
		return new File(m_db.getPath());
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( DB_CREATE );	
		db.execSQL( Trigger_CREATE );
		db.execSQL( DB_CREATE_Table_PassWord );
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_password, "123456");
		db.insert(DB_TABLE_PassWord, KEY_id, initialValues);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_PassWord);
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
	public long getRecCountInFolder(int iFolderDBID){
		long iCount = 0;
		Cursor cur = m_db.rawQuery("select count(*) AS "+KEY_rec_count_in_folder+" from "+DB_TABLE+" where "+KEY_preid+"=?", 
				new String[]{String.valueOf(iFolderDBID)});
		cur.moveToFirst();
		int iIndex = cur.getColumnIndex(KEY_rec_count_in_folder);
		iCount = cur.getLong(iIndex);
		cur.close();
		return iCount;
	}
	
	public	Cursor	getFolderInRoot()
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? and "+
					KEY_type+"=? order by "+KEY_detail+" asc", 
					new String[]{String.valueOf(0), String.valueOf(CMemoInfo.Type_Folder) });
	}
//	public	Cursor	getRemindFolderInRoot()
//	{
//		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? and "+
//					KEY_type+"=? and "+KEY_isremind+"=? order by "+KEY_createtime+" desc", 
//					new String[]{String.valueOf(0), String.valueOf(CMemoInfo.Type_Folder), String.valueOf(CMemoInfo.IsRemind_Yes)});
//	}
	
//	public	Cursor	getMemosByID( int id )
//	{
//		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? and "+KEY_isremind+"!=? order by "+KEY_type+" asc, "+
//					KEY_createtime+" desc", 
//					new String[]{String.valueOf(id), String.valueOf(CMemoInfo.IsRemind_Yes)});
//	}
//	public	Cursor	getRemindsByID( int id )
//	{
//		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? and "+KEY_isremind+"=? order by "+KEY_type+" asc, "+
//				KEY_createtime+" desc", 
//					new String[]{String.valueOf(id), String.valueOf(CMemoInfo.IsRemind_Yes)});
//	}
	public	Cursor	getNotesByID( int id )
	{
//		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_type+"="+String.valueOf(CMemoInfo.Type_Folder)+" union "+
//				"select * from "+DB_TABLE+" where "+KEY_type+"="+String.valueOf(CMemoInfo.Type_Memo), 
//				null);
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_preid+"=? order by "+KEY_type+" asc, "+
				KEY_lastmodifytime+" desc", 
					new String[]{String.valueOf(id)});
	}
	
	public	Cursor	getNoteRec( int id )
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_id+"=?", new String[]{String.valueOf(id)});
	}
	
	public	Cursor	getRemindInfo()
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_isremind+"=? and "+KEY_isremindable+"=?", new String[]{String.valueOf(CMemoInfo.IsRemind_Yes),String.valueOf(CMemoInfo.IsRemind_Able_Yes)} );
	}
	
	public	Cursor	getAllNotEncodeMemo()
	{
		return	m_db.rawQuery("select * from "+DB_TABLE+" where "+KEY_isencode+"!=? and "+KEY_type+"!=? order by "+
				KEY_lastmodifytime+" asc",  new String[]{String.valueOf(CMemoInfo.IsEncode_Yes), String.valueOf(CMemoInfo.Type_Folder)} );
	}

	public	Cursor	getRemindByID( int id )
	{
		
		return	getNoteRec( id );
//		return	m_db.rawQuery(" select * from " + DB_TABLE + " where "
//				+ KEY_isremind + "=? and " +KEY_isremindable + "=? and " 
//				+ KEY_id + "=?", new String[]{String.valueOf(CMemoInfo.IsRemind_Yes),String.valueOf(CMemoInfo.IsRemind_Able_Yes), String.valueOf(id)} );	
//		return	m_db.rawQuery(" select * from " + DB_TABLE + " where "+ KEY_id + "=?", new String[]{String.valueOf(id)} );	
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
		//String[] whereValue={ Integer.toString(1)};
		m_db.update(DB_TABLE_PassWord, initialValues, null, null);
		
	}
	public	int Create( CMemoInfo clCMemoInfo )
	{
		//m_db.execSQL("insert into DB_TABLE("+KEY_preid+","+KEY_type+","+KEY_isremind+","+KEY_remindtime+","+KEY_createtime+","+KEY_lastmodifytime+","+
		//		KEY_iseditenable+","+KEY_remindmask+","+KEY_detail+") values(?,?,?,?,?,?,?,?,?)"
		//			, new Object[]{clCMemoInfo.iPreId,clCMemoInfo.iType,clCMemoInfo.iIsRemind
		//			,clCMemoInfo.dRemindTime,clCMemoInfo.dCreateTime,clCMemoInfo.dLastModifyTime,clCMemoInfo.iIsEditEnable,
		//			clCMemoInfo.iRemindMask,clCMemoInfo.strDetail} );
		clCMemoInfo.dCreateTime		=	System.currentTimeMillis();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_preid, clCMemoInfo.iPreId);
		initialValues.put(KEY_type, clCMemoInfo.iType);
		initialValues.put(KEY_isremind, clCMemoInfo.iIsRemind);
		initialValues.put(KEY_remindtime, clCMemoInfo.dRemindTime);
		initialValues.put(KEY_createtime, clCMemoInfo.dCreateTime);
		initialValues.put(KEY_lastmodifytime, clCMemoInfo.dLastModifyTime);
		initialValues.put(KEY_iseditenable, clCMemoInfo.iIsEditEnable);
		initialValues.put(KEY_isremindable, clCMemoInfo.iIsRemindAble);
		initialValues.put(KEY_remindtype, clCMemoInfo.RemindType);
		initialValues.put(KEY_detail, clCMemoInfo.strDetail);
		initialValues.put(KEY_password, clCMemoInfo.strPassword);
		initialValues.put(KEY_isencode, clCMemoInfo.iIsEncode);
		initialValues.put(KEY_monday, clCMemoInfo.m_Week[0]);
		initialValues.put(KEY_tuesday, clCMemoInfo.m_Week[1]);
		initialValues.put(KEY_wednesday, clCMemoInfo.m_Week[2]);
		initialValues.put(KEY_thursday, clCMemoInfo.m_Week[3]);
		initialValues.put(KEY_friday, clCMemoInfo.m_Week[4]);
		initialValues.put(KEY_staturday, clCMemoInfo.m_Week[5]);
		initialValues.put(KEY_sunday, clCMemoInfo.m_Week[6]);
		initialValues.put(KEY_isHaveAudioData, clCMemoInfo.iIsHaveAudioData);
		initialValues.put(KEY_audioDataName, clCMemoInfo.strAudioFileName);
		
		initialValues.put(KEY_isRing, clCMemoInfo.iRing);
		initialValues.put(KEY_isVibrate, clCMemoInfo.iVibrate);

		return	(int)m_db.insert(DB_TABLE, KEY_id, initialValues);
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
	
	public	boolean Delete( ArrayList<DetailInfoOfSelectItem> alIDs )
	{
		boolean bIsHaveFailed = false;
		int iCnt	=	alIDs.size();
		for ( int i = 0; i < iCnt; ++i )
		{
			DetailInfoOfSelectItem detail = alIDs.get(i);
			if(detail.bIsFolder){
				boolean b = deleteAdditionalFile(detail.iDBRecID);
				if(!b){
					if(!bIsHaveFailed){
						bIsHaveFailed = true;
					}else{
						
					}
					continue;
				}
			}
			if(detail.bIsHaveAudioData){
				//delete audio file
				boolean b = SDCardAccessor.deleteAudioDataFile(detail.strAudioFileName);
				if(b){
					String[] temp = new String[]{String.valueOf(detail.iDBRecID)};
					m_db.execSQL( "delete from " + DB_TABLE + " where " + KEY_id + "=?", temp );
				}else{
					if(!bIsHaveFailed){
						bIsHaveFailed = true;
					}else{
						
					}
				}
			}else{
				String[] temp = new String[]{String.valueOf(detail.iDBRecID)};
				m_db.execSQL( "delete from " + DB_TABLE + " where " + KEY_id + "=?", temp );
			}
		}	
		return !bIsHaveFailed;
	}
	
	/*
	 * 如果要更新的记录是便签，那么在输入参数中必须提供type、preid，
	 * 用于更新便签所在的文件夹的最后修改时间
	 */
	public	int Update( CMemoInfo clCMemoInfo )
	{
		Calendar		clCalendar	=	Calendar.getInstance();
		clCMemoInfo.dLastModifyTime =	clCalendar.getTimeInMillis();
		if((clCMemoInfo.iType == CMemoInfo.Type_Memo) && (clCMemoInfo.iPreId!=CMemoInfo.PreId_Root)){
			CMemoInfo clFolderInfo = new CMemoInfo();
			clFolderInfo.iId = clCMemoInfo.iPreId;
			clFolderInfo.iType = CMemoInfo.Type_Folder;
			Update(clFolderInfo);	
		}
		int iEffectedID = CommonDefine.g_int_Invalid_ID;
		int id = clCMemoInfo.iId;
		if(id == CommonDefine.g_int_Invalid_ID){
			iEffectedID = Create(clCMemoInfo);
		}else{
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
			if ( -1 != clCMemoInfo.iIsRemindAble )
			{
				cv.put(KEY_isremindable, clCMemoInfo.iIsRemindAble.toString());
			}
			if ( -1 != clCMemoInfo.RemindType )
			{
				cv.put(KEY_remindtype, clCMemoInfo.RemindType.toString());
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
			if ( -1 != clCMemoInfo.m_Week[0])
			{
				cv.put(KEY_monday, clCMemoInfo.m_Week[0].toString());
			}
			if ( -1 != clCMemoInfo.m_Week[1])
			{
				cv.put(KEY_tuesday, clCMemoInfo.m_Week[1].toString());
			}
			if ( -1 != clCMemoInfo.m_Week[2])
			{
				cv.put(KEY_wednesday, clCMemoInfo.m_Week[2].toString());
			}
			if ( -1 != clCMemoInfo.m_Week[3])
			{
				cv.put(KEY_thursday, clCMemoInfo.m_Week[3].toString());
			}
			
			if ( -1 != clCMemoInfo.m_Week[4])
			{
				cv.put(KEY_friday, clCMemoInfo.m_Week[4].toString());
			}
			if ( -1 != clCMemoInfo.m_Week[5])
			{
				cv.put(KEY_staturday, clCMemoInfo.m_Week[5].toString());
			}
			if ( -1 != clCMemoInfo.m_Week[6])
			{
				cv.put(KEY_sunday, clCMemoInfo.m_Week[6].toString());
			}
			if ( -1 != clCMemoInfo.iIsHaveAudioData)
			{
				cv.put(KEY_isHaveAudioData, clCMemoInfo.iIsHaveAudioData);
			}
			if ( null != clCMemoInfo.strAudioFileName)
			{
				cv.put(KEY_audioDataName, clCMemoInfo.strAudioFileName);
			}
			
			if ( -1 != clCMemoInfo.iRing)
			{
				cv.put(KEY_isRing, clCMemoInfo.iRing);
			}
			if ( -1 != clCMemoInfo.iVibrate)
			{
				cv.put(KEY_isVibrate, clCMemoInfo.iVibrate);
			}
			
			String[] whereValue={ Integer.toString(id)};
			m_db.update(DB_TABLE, cv, KEY_id+"=?", whereValue);
			iEffectedID = clCMemoInfo.iId;
		}
		
		return iEffectedID;
	}
	private boolean deleteAdditionalFile(int iDBRecID){
		boolean bIsHaveFailed = false;
		Cursor cur = getNotesByID(iDBRecID);
		if(cur.getCount()>0){
			cur.moveToFirst();
			do{
				int index = cur.getColumnIndex(CNoteDBCtrl.KEY_isHaveAudioData);
				int value = cur.getInt(index);
				if(value == CMemoInfo.IsHaveAudioData_Yes){
					index = cur.getColumnIndex(CNoteDBCtrl.KEY_audioDataName);
					String filename = cur.getString(index);
					boolean b = SDCardAccessor.deleteAudioDataFile(filename);
					if(b){
					}else{
						if(!bIsHaveFailed){
							bIsHaveFailed = true;
						}else{
							
						}
					}
				}
			}while(cur.moveToNext());
		}
		cur.close();
		return !bIsHaveFailed;
	}
}
