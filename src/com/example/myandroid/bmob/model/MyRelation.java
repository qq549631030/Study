package com.example.myandroid.bmob.model;

import cn.bmob.v3.BmobObject;

public class MyRelation extends BmobObject {
	private String userId;
	private String installactionId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInstallactionId() {
		return installactionId;
	}

	public void setInstallactionId(String installactionId) {
		this.installactionId = installactionId;
	}

}
