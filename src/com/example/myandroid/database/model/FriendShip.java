package com.example.myandroid.database.model;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "friendship")
public class FriendShip {
	@Id
	@Column(name = "_id")
	private int _id;
	@Column(name = "user_1")
	private String user_1;
	@Column(name = "user_2")
	private String user_2;
	@Column(name = "recogniseBy")
	private String recogniseBy;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUser_1() {
		return user_1;
	}

	public void setUser_1(String user_1) {
		this.user_1 = user_1;
	}

	public String getUser_2() {
		return user_2;
	}

	public void setUser_2(String user_2) {
		this.user_2 = user_2;
	}

	public String getRecogniseBy() {
		return recogniseBy;
	}

	public void setRecogniseBy(String recogniseBy) {
		this.recogniseBy = recogniseBy;
	}

}
