package com.example.myandroid.bmob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.myandroid.bmob.model.MyFriend;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.model.User;
import com.example.myandroid.friend.FriendManager;

public class MyFriendUtils {
	// 添加好友（测试用，无需要好友同意，不发消息给好友）
	public static void addFriend(final Context context, String friendId,
			final String from, final BmobListener listener) {
		final MyUser me = BmobUser.getCurrentUser(context, MyUser.class);
		Map<String, String> parms1 = new HashMap<String, String>();
		parms1.put("objectId", friendId);
		// 查询好友是否存在
		BmobUtils.findBmobData(context, parms1, new FindListener<MyUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				System.err.println("查找用户失败(" + arg0 + "):" + arg1);
				listener.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(List<MyUser> arg0) {
				if (arg0 == null || arg0.size() == 0) {
					// 不存在该用户
					System.err.println("该用户不存在");
					listener.onError(1001, "user not exist");
				} else {
					// 找到了该用户
					MyUser friend = arg0.get(0);
					final MyFriend myFriend = new MyFriend();
					myFriend.setMe(me);
					myFriend.setFrom(from);
					BmobRelation friends = new BmobRelation(friend);
					myFriend.setFriends(friends);
					// 查找当前用户的好友关系
					Map<String, MyUser> parms2 = new HashMap<String, MyUser>();
					parms2.put("me", me);
					BmobUtils.findBmobData(context, parms2,
							new FindListener<MyFriend>() {

								@Override
								public void onError(int arg0, String arg1) {
									// 查找好友关系失败，直接插入新好友关系
									System.err.println("查找好友关系失败(" + arg0
											+ "):" + arg1);
									BmobUtils.insertBmobData(context, myFriend,
											new SaveListener() {

												@Override
												public void onSuccess() {
													System.out
															.println("insert myfriend success");
													listener.onSuccess();
												}

												@Override
												public void onFailure(int arg0,
														String arg1) {
													System.err
															.println("insert myfriend fail : "
																	+ arg1);
													listener.onError(arg0, arg1);
												}
											});
								}

								@Override
								public void onSuccess(List<MyFriend> arg0) {

									System.out
											.println("find my friend ship onSuccess : "
													+ arg0.size());
									if (arg0 == null || arg0.size() == 0) {
										// 当前用户的好友关系为空，插入新关系
										BmobUtils.insertBmobData(context,
												myFriend, new SaveListener() {

													@Override
													public void onSuccess() {
														System.out
																.println("insert myfriend success");
														listener.onSuccess();
													}

													@Override
													public void onFailure(
															int arg0,
															String arg1) {
														System.err
																.println("insert myfriend fail : "
																		+ arg1);
														listener.onError(arg0,
																arg1);
													}
												});
									} else {
										MyFriend serverFriend = null;
										for (MyFriend eachFriend : arg0) {
											if (eachFriend.getFrom().equals(
													from)) {
												serverFriend = eachFriend;
											}
										}
										// 服务器存在当前类型的好友关系，更新好友
										if (serverFriend != null) {
											BmobUtils.updateBmobData(context,
													myFriend,
													serverFriend.getObjectId(),
													new UpdateListener() {

														@Override
														public void onSuccess() {
															System.out
																	.println("update myfriend success");
															listener.onSuccess();
														}

														@Override
														public void onFailure(
																int arg0,
																String arg1) {
															System.err
																	.println("update myfriend fail : "
																			+ arg1);
															listener.onError(
																	arg0, arg1);
														}
													});
										} else {
											// 服务器不存在当前类型的好友关系，新增
											BmobUtils.insertBmobData(context,
													myFriend,
													new SaveListener() {

														@Override
														public void onSuccess() {
															System.out
																	.println("insert myfriend success");
															listener.onSuccess();
														}

														@Override
														public void onFailure(
																int arg0,
																String arg1) {
															System.err
																	.println("insert myfriend fail : "
																			+ arg1);
															listener.onError(
																	arg0, arg1);
														}
													});
										}
									}
								}
							});
				}
			}
		});
	}

	public static void getAllFriends(final Context context, final MyUser me,
			final FindListener<MyUser> listener) {
		System.out.println("getAllFriends");
		final List<MyUser> friends = new ArrayList<MyUser>();
		getAllFriendsByFrom(context, me, FriendManager.FRIEND_FROM_1,
				new FindListener<MyUser>() {

					@Override
					public void onError(int arg0, String arg1) {
						listener.onError(arg0, arg1);
					}

					@Override
					public void onSuccess(List<MyUser> arg0) {
						friends.addAll(arg0);
						getAllFriendsByFrom(context, me,
								FriendManager.FRIEND_FROM_2,
								new FindListener<MyUser>() {

									@Override
									public void onError(int arg0, String arg1) {
										listener.onError(arg0, arg1);
									}

									@Override
									public void onSuccess(List<MyUser> arg0) {
										friends.addAll(arg0);
										getAllFriendsByFrom(context, me,
												FriendManager.FRIEND_FROM_3,
												new FindListener<MyUser>() {

													@Override
													public void onError(
															int arg0,
															String arg1) {
														listener.onError(arg0,
																arg1);
													}

													@Override
													public void onSuccess(
															List<MyUser> arg0) {
														friends.addAll(arg0);
														getAllFriendsByFrom(
																context,
																me,
																FriendManager.FRIEND_FROM_4,
																new FindListener<MyUser>() {

																	@Override
																	public void onError(
																			int arg0,
																			String arg1) {
																		listener.onError(
																				arg0,
																				arg1);
																	}

																	@Override
																	public void onSuccess(
																			List<MyUser> arg0) {
																		friends.addAll(arg0);
																		listener.onSuccess(friends);
																	}
																});
													}
												});
									}
								});
					}
				});
	}

	public static <T> void getAllFriendsByFrom(final Context context,
			MyUser me, final String from, final FindListener<MyUser> listener) {
		System.out.println("getAllFriendsByFrom " + from);
		final List<MyUser> friends = new ArrayList<MyUser>();
		if (TextUtils.isEmpty(from)) {
			listener.onError(1010, "from can not be null");
			return;
		}
		Map<String, T> parms = new HashMap<String, T>();
		parms.put("me", (T) me);
		parms.put("from", (T) from);
		BmobUtils.findBmobData(context, parms, new FindListener<MyFriend>() {

			@Override
			public void onError(int arg0, String arg1) {
				listener.onError(arg0, arg1);
			}

			@Override
			public void onSuccess(List<MyFriend> arg0) {
				if (arg0 == null || arg0.size() == 0) {
					listener.onSuccess(friends);
				} else {
					MyFriend friend = arg0.get(0);
					getFriendFromShip(context, friend,
							new FindListener<MyUser>() {

								@Override
								public void onError(int arg0, String arg1) {
									listener.onError(arg0, arg1);
								}

								@Override
								public void onSuccess(List<MyUser> arg0) {
									friends.addAll(arg0);
									listener.onSuccess(friends);

								}
							});
				}
			}
		});
	}

	public static void getFriendFromShip(Context context, MyFriend myFriend,
			FindListener<MyUser> listener) {
		System.out.println("getFriendFromShip");
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereRelatedTo("friends", new BmobPointer(myFriend));
		query.include("me");
		query.findObjects(context, listener);
	}
}
