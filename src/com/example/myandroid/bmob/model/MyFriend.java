package com.example.myandroid.bmob.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class MyFriend extends BmobObject {
	private MyUser me;
	private BmobRelation friends;
	private String from;

	public MyUser getMe() {
		return me;
	}

	public void setMe(MyUser me) {
		this.me = me;
	}

	public BmobRelation getFriends() {
		return friends;
	}

	public void setFriends(BmobRelation friends) {
		this.friends = friends;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
