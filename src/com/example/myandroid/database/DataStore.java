package com.example.myandroid.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataStore {
	public static final String AUTHORITY = "com.example.myandroid";

	// 表定义
	public static class UserTable implements BaseColumns {
		public static final String TABLE_NAME = "user";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_NAME);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.myandroid.user";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.myandroid.user";

		public static final String USER_NAME = "name";
		public static final String USER_AGE = "age";
	}
	
}
