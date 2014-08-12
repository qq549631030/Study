package com.example.myandroid.bmob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.example.myandroid.bmob.model.MyFriendShip;
import com.example.myandroid.bmob.model.MyUser;

import android.content.Context;
import android.text.TextUtils;

public class MyFriendShipUtils {
	public static void addFriendShip(final Context context, String friendId,
			final String recogniseBy, final BmobListener listener) {
		final MyUser me = BmobUser.getCurrentUser(context, MyUser.class);
		if (friendId.equals(me.getObjectId())) {
			listener.onError(1006, "不可加自己为好友");
		}
		final MyFriendShip myFriendShip = new MyFriendShip();
		myFriendShip.setUser_1(me);
		myFriendShip.setRecogniseBy(recogniseBy);
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("objectId", friendId);
		BmobUtils.findBmobData(context, parms, new FindListener<MyUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				listener.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(List<MyUser> arg0) {
				if (arg0 == null || arg0.size() == 0) {
					listener.onError(1005, "no such user");
				} else {
					MyUser friend = arg0.get(0);
					myFriendShip.setUser_2(friend);
					BmobQuery<MyFriendShip> query = new BmobQuery<MyFriendShip>();
					List<BmobQuery<MyFriendShip>> queries = new ArrayList<BmobQuery<MyFriendShip>>();
					BmobQuery<MyFriendShip> query1 = new BmobQuery<MyFriendShip>();
					query1.addWhereEqualTo("user_1", me);
					query1.addWhereEqualTo("user_2", friend);
					BmobQuery<MyFriendShip> query2 = new BmobQuery<MyFriendShip>();
					query2.addWhereEqualTo("user_1", friend);
					query2.addWhereEqualTo("user_2", me);
					queries.add(query1);
					queries.add(query2);
					query.or(queries);
					BmobUtils.findBombData(context, query,
							new FindListener<MyFriendShip>() {

								@Override
								public void onError(int arg0, String arg1) {
									listener.onError(arg0, arg1);
								}

								@Override
								public void onSuccess(List<MyFriendShip> arg0) {
									if (arg0 == null || arg0.size() == 0) {
										BmobUtils.insertBmobData(context,
												myFriendShip,
												new SaveListener() {

													@Override
													public void onSuccess() {
														listener.onSuccess();
													}

													@Override
													public void onFailure(
															int arg0,
															String arg1) {
														listener.onError(arg0,
																arg1);
													}
												});
									} else {
										listener.onError(1007, "好友关系已存在");
									}
								}
							});
				}
			}
		});
	}

	public static void getAllFriends(final Context context,
			final FindListener<MyUser> listener) {
		getAllFriendShip(context, new FindListener<MyFriendShip>() {

			@Override
			public void onError(int arg0, String arg1) {
				listener.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(List<MyFriendShip> arg0) {
				System.out.println("friendship size = " + arg0.size());
				List<MyUser> friends = new ArrayList<MyUser>();
				for (MyFriendShip myFriendShip : arg0) {
					final MyUser me = BmobUser.getCurrentUser(context,
							MyUser.class);
					MyUser friend = getFriendFromFriendShip(context, me,
							myFriendShip);
					if (friend != null) {
						System.out.println(friend.toString());
						friends.add(friend);
					}
				}
				listener.onSuccess(friends);
			}

		});
	}

	public static void getAllFriendShip(final Context context,
			final FindListener<MyFriendShip> listener) {
		getAllFriendsByRecogniseBy(context, null, listener);
	}

	public static void getAllFriendsByRecogniseBy(final Context context,
			String recogniseBy, final FindListener<MyFriendShip> listener) {
		final MyUser me = BmobUser.getCurrentUser(context, MyUser.class);
		BmobQuery<MyFriendShip> query = new BmobQuery<MyFriendShip>();
		if (!TextUtils.isEmpty(recogniseBy)) {
			query.addWhereEqualTo("recogniseBy", recogniseBy);
		}
		List<BmobQuery<MyFriendShip>> queries = new ArrayList<BmobQuery<MyFriendShip>>();
		query.addWhereEqualTo("user_1", me);
		BmobQuery<MyFriendShip> query1 = new BmobQuery<MyFriendShip>();
		query1.addWhereEqualTo("user_2", me);
		queries.add(query1);
		query.or(queries);
		query.include("user_1");
		query.include("user_2");
		BmobUtils.findBombData(context, query, listener);
	}

	public static MyUser getFriendFromFriendShip(Context context, MyUser me,
			MyFriendShip myFriendShip) {
		MyUser user1 = myFriendShip.getUser_1();
		MyUser user2 = myFriendShip.getUser_2();
		if (me.getObjectId().equals(user1.getObjectId())) {
			return user2;
		} else if (me.getObjectId().equals(user2.getObjectId())) {
			return user1;
		} else {
			return null;
		}
	}
}
