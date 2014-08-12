package com.example.myandroid.database;

import android.content.Context;

import com.ab.db.orm.AbSDDBHelper;
import com.example.myandroid.database.model.Avatar;
import com.example.myandroid.database.model.FriendShip;
import com.example.myandroid.database.model.Message;
import com.example.myandroid.database.model.User;

public class DBSDHelper extends AbSDDBHelper {
	// 数据库名
	private static final String DBNAME = "myandroid.db";
	// 数据库 存放路径
	private static final String DBPATH = "MyAndroid";

	// 当前数据库的版本
	private static final int DBVERSION = 1;
	// 要初始化的表
	private static final Class<?>[] clazz = { User.class, Avatar.class,
			FriendShip.class, Message.class };

	public DBSDHelper(Context context) {
		super(context, DBPATH, DBNAME, null, DBVERSION, clazz);
	}

}
