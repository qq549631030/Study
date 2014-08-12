package com.example.myandroid.database.model;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "user")
public class User {
	@Id
	@Column(name = "_id")
	private int _id;
	@Column(name = "objectId")
	private String objectId;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "email")
	private String email;
	@Column(name = "emailVerified")
	private Boolean emailVerified;
	@Column(name = "createdAt")
	private String createdAt;
	@Column(name = "updateAt")
	private String updatedAt;

	@Column(name = "nickname")
	private String nickname;
	@Column(name = "avatarUrl")
	private String avatarUrl;
	@Column(name = "age")
	private Integer age;
	@Column(name = "gender")
	private Boolean gender;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

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
		return "User [_id=" + _id + ", username=" + username + ", password="
				+ password + ", email=" + email + ", emailVerified="
				+ emailVerified + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + ", nickname=" + nickname + ", avatarUrl="
				+ avatarUrl + ", age=" + age + ", gender=" + gender + "]";
	}

}
