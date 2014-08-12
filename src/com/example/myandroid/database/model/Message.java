package com.example.myandroid.database.model;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "message")
public class Message {
	@Id
	@Column(name = "_id")
	private int _id;
	@Column(name = "time")
	private String time;
	@Column(name = "msgType")
	private String msgType;
	@Column(name = "state")
	private String state;
	@Column(name = "uuid")
	private String uuid;
	@Column(name = "fromId")
	private String fromId;
	@Column(name = "fromNickname")
	private String fromNickname;
	@Column(name = "fromUsername")
	private String fromUsername;
	@Column(name = "fromAvatar")
	private String fromAvatar;
	@Column(name = "targetId")
	private String targetId;
	@Column(name = "targetNickname")
	private String targetNickname;
	@Column(name = "targetUsername")
	private String targetUsername;
	@Column(name = "targetAvatar")
	private String targetAvatar;
	@Column(name = "data")
	private String data;

	
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getFromNickname() {
		return fromNickname;
	}

	public void setFromNickname(String fromNickname) {
		this.fromNickname = fromNickname;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	public String getFromAvatar() {
		return fromAvatar;
	}

	public void setFromAvatar(String fromAvatar) {
		this.fromAvatar = fromAvatar;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetNickname() {
		return targetNickname;
	}

	public void setTargetNickname(String targetNickname) {
		this.targetNickname = targetNickname;
	}

	public String getTargetUsername() {
		return targetUsername;
	}

	public void setTargetUsername(String targetUsername) {
		this.targetUsername = targetUsername;
	}

	public String getTargetAvatar() {
		return targetAvatar;
	}

	public void setTargetAvatar(String targetAvatar) {
		this.targetAvatar = targetAvatar;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
