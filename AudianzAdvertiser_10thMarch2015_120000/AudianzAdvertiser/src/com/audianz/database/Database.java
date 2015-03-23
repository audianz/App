package com.audianz.database;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Database {
	ELogger mELogger;
	public SQLiteDatabase sqLiteDb = null;
	private static Database dbObj = null;
	Context mContext;
	private Database() {}
	public static Database getInstance() {
		if (dbObj == null) {
			dbObj = new Database();
		}
		return dbObj;
	}

	/**
	 * This method is called to initialize database module.
	 */
	public boolean init(Context context, ELogger logger) {

		createDatabase(context);

		return true;
	}

	/**
	 * This method is called to create new database if not created already.
	 * @param context 
	 * 
	 * @return boolean
	 */
	private boolean createDatabase(Context context) {

		sqLiteDb = (new DBHelper(context)).getWritableDatabase();
		sqLiteDb.setPageSize(4 * 1024);// default is 1 K
		return true;
	}

	//-------------------------------------insert data from content values to database---------------------------------------
	public int insertInDB(String tableName, ContentValues[] contentValues) {
		int retCode = -1;
		if (contentValues != null) {
			try {
				dbObj.sqLiteDb.beginTransaction();
				for (ContentValues stateValue : contentValues) {
					try {
						if (stateValue == null)
							return 0;
						retCode = (int) sqLiteDb.insert(tableName,null, stateValue);
					} catch (Exception e) {
					}
				}
			} finally {
				dbObj.sqLiteDb.setTransactionSuccessful();
				dbObj.sqLiteDb.endTransaction();
			}
		}
		return retCode;
	}
	public Cursor queryTable(String tableName, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor cur = null;
		try {
			cur = sqLiteDb.query(tableName, columns, selection,
					selectionArgs, groupBy, having, orderBy);
		} catch (Exception e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				mELogger.error("queryTable() : Exception is : " + e);
			// e.printStackTrace();
			if (cur != null && !cur.isClosed()) {
				mELogger.error("rawQuery() : closing cursor");
				cur.close();
			}
			cur = null;
		}
		return cur;
	}

}
