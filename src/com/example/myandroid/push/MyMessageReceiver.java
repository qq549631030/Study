package com.example.myandroid.push;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.myandroid.R;
import com.example.myandroid.global.Constant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.bmob.push.BmobPushMessageReceiver;
import cn.bmob.v3.BmobUser;

public class MyMessageReceiver extends BmobPushMessageReceiver {

	public void receivePushMessage(Context context, JSONObject content) {
		System.out.println(content.toString());
		// TODO Auto-generated method stub
		// 发送通知
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification n = new Notification();
		n.icon = R.drawable.ic_launcher;
		n.tickerText = "收到推送消息";
		n.when = System.currentTimeMillis();
		// n.flags=Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent();
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		n.setLatestEventInfo(context, "消息", content.toString(), pi);
		n.defaults |= Notification.DEFAULT_SOUND;
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(1, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.bmob.push.BmobPushMessageReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Bundle mBundle = arg1.getExtras();
		String messageString = mBundle.getString("msg");
		System.out.println("onReceive" + messageString);
		try {
			String currentId = BmobUser.getCurrentUser(arg0).getObjectId();
			JSONObject mObject = new JSONObject(messageString);
			String appId = mObject.getString("appId");
			JSONObject msgObject = mObject.getJSONObject("msg");
			String targetId = "";
			if (msgObject.has("targetId")) {
				targetId = msgObject.getString("targetId");
			}
			if (appId.equals(Constant.APP_ID)
					&& (targetId == null || targetId.equals("") || targetId
							.equals(currentId))) {
				JSONObject content = new JSONObject();
				if (msgObject.has("content")) {
					content = msgObject.getJSONObject("content");
					receivePushMessage(arg0, content);
				} else {
					if (msgObject.has("alert")) {
						super.onReceive(arg0, arg1);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("onMessage : " + arg1);
		NotificationManager nm = (NotificationManager) arg0
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification n = new Notification();
		n.icon = R.drawable.ic_launcher;
		n.tickerText = "收到推送消息";
		n.when = System.currentTimeMillis();
		// n.flags=Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent();
		PendingIntent pi = PendingIntent.getActivity(arg0, 0, intent, 0);
		n.setLatestEventInfo(arg0, "消息", arg1, pi);
		n.defaults |= Notification.DEFAULT_SOUND;
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(1, n);
	}

}
