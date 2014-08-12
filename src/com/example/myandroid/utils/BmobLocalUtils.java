package com.example.myandroid.utils;

import com.example.myandroid.bmob.model.MyFriendShip;
import com.example.myandroid.bmob.model.MyMessage;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.model.FriendShip;
import com.example.myandroid.database.model.Message;
import com.example.myandroid.database.model.User;
import com.google.gson.Gson;

public class BmobLocalUtils {
	public static User bmob2Local(MyUser myUser) {
		User user = new User();
		user.setObjectId(myUser.getObjectId());
		user.setUsername(myUser.getUsername());
		user.setPassword(myUser.getPassword());
		user.setEmail(myUser.getEmail());
		user.setEmailVerified(myUser.getEmailVerified());
		user.setCreatedAt(myUser.getCreatedAt());
		user.setUpdatedAt(myUser.getUpdatedAt());
		user.setNickname(myUser.getNickname());
		user.setAvatarUrl(myUser.getAvatarUrl());
		user.setAge(myUser.getAge());
		user.setGender(myUser.getGender());
		return user;
	}

	public static FriendShip bmob2Local(MyFriendShip myFriendShip) {
		FriendShip friendShip = new FriendShip();
		friendShip.setUser_1(myFriendShip.getUser_1().getObjectId());
		friendShip.setUser_2(myFriendShip.getUser_2().getObjectId());
		friendShip.setRecogniseBy(myFriendShip.getRecogniseBy());
		return friendShip;
	}

	public static Message bmob2Local(MyMessage myMessage) {
		String messagestr = myMessage.getContent();
		Gson mGson = new Gson();
		Message message = mGson.fromJson(messagestr, Message.class);
		return message;
	}
}
