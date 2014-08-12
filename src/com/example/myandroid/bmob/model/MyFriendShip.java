package com.example.myandroid.bmob.model;

import cn.bmob.v3.BmobObject;

public class MyFriendShip extends BmobObject {
	private MyUser user_1;
	private MyUser user_2;
	private String recogniseBy;

	public MyUser getUser_1() {
		return user_1;
	}

	public void setUser_1(MyUser user_1) {
		this.user_1 = user_1;
	}

	public MyUser getUser_2() {
		return user_2;
	}

	public void setUser_2(MyUser user_2) {
		this.user_2 = user_2;
	}

	public String getRecogniseBy() {
		return recogniseBy;
	}

	public void setRecogniseBy(String recogniseBy) {
		this.recogniseBy = recogniseBy;
	}

}
