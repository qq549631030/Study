package com.example.myandroid.database.model;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.example.myandroid.database.DBSDHelper;

public class AvatarDao extends AbDBDaoImpl<Avatar> {
	public AvatarDao(Context context) {
		super(new DBSDHelper(context), Avatar.class);
	}

}
