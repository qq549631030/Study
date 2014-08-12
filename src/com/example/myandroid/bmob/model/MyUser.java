package com.example.myandroid.bmob.model;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	private String nickname;
	private String avatarUrl;
	private Integer age;
	private Boolean gender;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "MyUser [nickname=" + nickname + ", avatarUrl=" + avatarUrl
				+ ", age=" + age + ", gender=" + gender + "]";
	}

}
