package com.example.myandroid.database.model;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.example.myandroid.database.DBSDHelper;

public class FriendShipDao extends AbDBDaoImpl<FriendShip> {
	public FriendShipDao(Context context) {
		super(new DBSDHelper(context), FriendShip.class);
	}
}
