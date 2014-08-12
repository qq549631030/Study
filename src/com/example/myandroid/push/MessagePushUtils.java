package com.example.myandroid.push;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CloudCodeListener;

import com.example.myandroid.bmob.BmobUtils;
import com.example.myandroid.bmob.model.MyMessage;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.model.Message;
import com.google.gson.Gson;

import android.content.Context;

public class MessagePushUtils {
	public static void sendMessage(Context context, MyMessage message,
			CloudCodeListener listener) throws JSONException {
		JSONObject parms = new JSONObject();
		parms.put("time", message.getTime());
		parms.put("uuid", message.getUuid());
		parms.put("msgType", message.getMsgType());
		parms.put("msgState", message.getMsgState());
		parms.put("requestId", message.getFromId());
		parms.put("targetId", message.getTargetId());
		parms.put("content", message.getContent());
		BmobUtils.callCloudCode(context,
				CloudMethodManager.METHOD_SEND_MESSAGE, parms, listener);
	}

	public static MyMessage createAddFriendMessage(Context context,
			MyUser target, String extend) {
		MyMessage myMessage = new MyMessage();
		long timeNow = System.currentTimeMillis();
		String uuid = UUID.randomUUID().toString();
		MyUser me = BmobUser.getCurrentUser(context, MyUser.class);
		Message mMessage = new Message();
		mMessage.setTime(timeNow + "");
		mMessage.setMsgType(MessageManager.MESSAGE_TYPE_ADD_FRIEND_REQUEST);
		mMessage.setState(MessageManager.MESSAGE_STATE_UNREAD);
		mMessage.setUuid(uuid);
		mMessage.setFromId(me.getObjectId());
		mMessage.setFromUsername(me.getUsername());
		mMessage.setFromNickname(me.getNickname());
		mMessage.setFromAvatar(me.getAvatarUrl());
		mMessage.setTargetId(target.getObjectId());
		mMessage.setTargetUsername(target.getUsername());
		mMessage.setTargetNickname(target.getNickname());
		mMessage.setTargetAvatar(target.getAvatarUrl());
		mMessage.setData(extend);
		Gson mGson = new Gson();
		String content = mGson.toJson(mMessage);
		myMessage.setFromId(me.getObjectId());
		myMessage.setTargetId(target.getObjectId());
		myMessage.setMsgState(MessageManager.MESSAGE_STATE_UNREAD);
		myMessage.setMsgType(MessageManager.MESSAGE_TYPE_ADD_FRIEND_REQUEST);
		myMessage.setTime(timeNow + "");
		myMessage.setUuid(uuid);
		myMessage.setContent(content);
		return myMessage;
	}
}
