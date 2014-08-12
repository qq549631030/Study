package com.example.myandroid.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

	private final String TAG = MyContentProvider.class.getSimpleName();

	private MySQLiteOpenHelper databaseHelper;

	private static final int USER_INFOS = 1;
	private static final int USER_INFO_ITEM = 2;

	private static UriMatcher uriMatcher; // URi 操作类

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(DataStore.AUTHORITY, DataStore.UserTable.TABLE_NAME,
				USER_INFOS);
		uriMatcher.addURI(DataStore.AUTHORITY, DataStore.UserTable.TABLE_NAME
				+ "/#", USER_INFO_ITEM);
	}

	@Override
	public boolean onCreate() {
		Log.e(TAG, "onCreate");
		databaseHelper = new MySQLiteOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		try {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			switch (uriMatcher.match(uri)) {
			case USER_INFOS:
				qb.setTables(DataStore.UserTable.TABLE_NAME);
				break;
			case USER_INFO_ITEM:
				qb.setTables(DataStore.UserTable.TABLE_NAME);
				qb.appendWhere(DataStore.UserTable._ID + " = "
						+ uri.getPathSegments().get(1));
				break;
			default:
				throw new IllegalArgumentException("****query Unknown URI "
						+ uri);
			}
			String orderBy;
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = null;
			} else {
				orderBy = sortOrder;
			}
			SQLiteDatabase db = databaseHelper.getReadableDatabase();
			cursor = qb.query(db, projection, selection, selectionArgs, null,
					null, orderBy);
			if (cursor != null) {
				cursor.setNotificationUri(getContext().getContentResolver(),
						uri);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case USER_INFOS:
			return DataStore.UserTable.CONTENT_TYPE;
		case USER_INFO_ITEM:
			return DataStore.UserTable.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("**** get type Unknown URI "
					+ uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri uriNew = null;
		try {
			if (values == null) {
				values = new ContentValues();
			}
			SQLiteDatabase db = databaseHelper.getWritableDatabase();
			long rowId = -1;
			switch (uriMatcher.match(uri)) {
			case USER_INFOS:
				rowId = db.insert(DataStore.UserTable.TABLE_NAME, null, values);
				if (rowId > 0) {
					uriNew = ContentUris.withAppendedId(
							DataStore.UserTable.CONTENT_URI, rowId);

				}
				break;

			default:
				break;
			}
			if (uri != null) {
				getContext().getContentResolver().notifyChange(uriNew, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uriNew;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		try {
			SQLiteDatabase db = databaseHelper.getWritableDatabase();
			String id;
			switch (uriMatcher.match(uri)) {
			case USER_INFOS:// 删除整张表
				count = db.delete(DataStore.UserTable.TABLE_NAME, selection,
						selectionArgs);
				break;
			case USER_INFO_ITEM:// 删除表中指定项
				id = uri.getPathSegments().get(1);
				count = db.delete(
						DataStore.UserTable.TABLE_NAME,
						DataStore.UserTable._ID
								+ " = "
								+ id
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
				break;

			default:
				break;
			}
			getContext().getContentResolver().notifyChange(uri, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		try {
			SQLiteDatabase db = databaseHelper.getWritableDatabase();
			String id;
			switch (uriMatcher.match(uri)) {
			case USER_INFOS:// 更新整张表
				count = db.update(DataStore.UserTable.TABLE_NAME, values,
						selection, selectionArgs);
				break;
			case USER_INFO_ITEM:// 更新表中指定项
				id = uri.getPathSegments().get(1);
				count = db.update(
						DataStore.UserTable.TABLE_NAME,
						values,
						DataStore.UserTable._ID
								+ " = "
								+ id
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
				break;

			default:
				break;
			}
			getContext().getContentResolver().notifyChange(uri, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

}
