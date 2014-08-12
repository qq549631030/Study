package com.example.myandroid.database.model;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.example.myandroid.database.DBSDHelper;

public class MessageDao extends AbDBDaoImpl<Message> {

	public MessageDao(Context context) {
		super(new DBSDHelper(context), Message.class);
	}

}
