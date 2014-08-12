package com.example.myandroid.database;

import com.example.myandroid.database.DataStore.UserTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	private final String TAG = MySQLiteOpenHelper.class.getSimpleName();
	/**
	 * 数据库名
	 */
	public final static String DATABASE_NAME = "mydata.db";
	private final static int DATABASE_VERSION = 1;

	/**
	 * 数据库建表语句
	 */
	private final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ UserTable.TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ UserTable.USER_NAME + " TEXT," + UserTable.USER_AGE + " TEXT);";

	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate");
		/**
		 * 创建用于存储数据的表
		 */
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/**
		 * 删除已经存在表
		 */
		db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
		/**
		 * 重建表
		 */
		onCreate(db);
	}

}
