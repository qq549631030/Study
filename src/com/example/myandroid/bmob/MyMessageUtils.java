package com.example.myandroid.bmob;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.myandroid.bmob.model.MyMessage;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.push.MessageManager;

import android.content.Context;

public class MyMessageUtils {
	public static void getAllMessage(final Context context,
			FindListener<MyMessage> listener) {
		final MyUser me = BmobUser.getCurrentUser(context, MyUser.class);
		BmobQuery<MyMessage> query = new BmobQuery<MyMessage>();
		query.addWhereEqualTo(MessageManager.MESSAGE_KEY_TARGET_ID,
				me.getObjectId());
		query.findObjects(context, listener);
	}
}
