package com.example.myandroid.database.model;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.example.myandroid.database.DBSDHelper;

/**
 * @author hx2lu
 *
 */
public class UserDao extends AbDBDaoImpl<User> {
	public UserDao(Context context) {
		super(new DBSDHelper(context), User.class);
	}
}
