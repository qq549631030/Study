package com.example.myandroid.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import cn.bmob.v3.BmobUser;

import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbStorageQuery;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInfoListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataOperationListener;
import com.ab.util.AbMd5;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.model.Avatar;
import com.example.myandroid.database.model.AvatarDao;
import com.example.myandroid.database.model.FriendShip;
import com.example.myandroid.database.model.FriendShipDao;
import com.example.myandroid.database.model.Message;
import com.example.myandroid.database.model.MessageDao;
import com.example.myandroid.database.model.User;
import com.example.myandroid.database.model.UserDao;
import com.example.myandroid.push.MessageManager;

public class DatabaseUtils {
	public static void saveAvatarToLocal(final Context context,
			String avatarDir, String url, Bitmap bitmap) {
		String avatarName = AbMd5.MD5(url)
				+ url.substring(url.lastIndexOf("."));
		File dir = new File(avatarDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File avatar = new File(avatarDir, avatarName);
		try {
			avatar.createNewFile();
			OutputStream outStream = new FileOutputStream(avatar);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		MyUser myUser = BmobUser.getCurrentUser(context, MyUser.class);
		final Avatar mAvatar = new Avatar();
		mAvatar.setUserId(myUser.getObjectId());
		mAvatar.setUrl(url);
		mAvatar.setFilepath(avatar.getAbsolutePath());
		findAvatarFromSd(context, myUser.getObjectId(),
				new AbDataInfoListener() {
					@Override
					public void onSuccess(List<?> arg0) {
						if (arg0 == null || arg0.size() == 0) {
							insertAvatar(context, mAvatar,
									new AbDataInsertListener() {

										@Override
										public void onSuccess(long arg0) {
											System.out
													.println("insert avatar success");
										}

										@Override
										public void onFailure(int arg0,
												String arg1) {
											System.out
													.println("insert avatar fail");
										}
									});
						} else {
							Avatar avatar = (Avatar) arg0.get(0);
							avatar.setUrl(mAvatar.getUrl());
							avatar.setFilepath(mAvatar.getFilepath());
							updateAvatar(context, avatar,
									new AbDataOperationListener() {

										@Override
										public void onSuccess(long arg0) {
											System.out
													.println("update avatar local database success");
										}

										@Override
										public void onFailure(int arg0,
												String arg1) {
											System.out
													.println("update avatar local database fail");
										}
									});
						}
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						insertAvatar(context, mAvatar,
								new AbDataInsertListener() {

									@Override
									public void onSuccess(long arg0) {
										System.out
												.println("insert avatar success");
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										System.out
												.println("insert avatar fail");
									}
								});
					}
				});
	}

	public static void findAvatarFromSd(Context context, String userId,
			AbDataInfoListener listener) {
		try {
			AbSqliteStorage mAbSqliteStorage = AbSqliteStorage
					.getInstance(context);
			AvatarDao avatarDao = new AvatarDao(context);
			AbStorageQuery query = new AbStorageQuery();
			query.equals("userId", userId);
			mAbSqliteStorage.findData(query, avatarDao, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insertAvatar(Context context, Avatar avatar,
			AbDataInsertListener listener) {
		try {
			AbSqliteStorage mAbSqliteStorage = AbSqliteStorage
					.getInstance(context);
			AvatarDao mAvatarDao = new AvatarDao(context);
			mAbSqliteStorage.insertData(avatar, mAvatarDao, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateAvatar(Context context, Avatar avatar,
			AbDataOperationListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		AvatarDao mAvatarDao = new AvatarDao(context);
		mAbSqliteStorage.updateData(avatar, mAvatarDao, listener);
	}

	public static void findUser(Context context, AbStorageQuery query,
			AbDataInfoListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		UserDao mUserDao = new UserDao(context);
		mAbSqliteStorage.findData(query, mUserDao, listener);
	}

	public static void insertUser(Context context, User user,
			AbDataInsertListener listener) {
		try {
			AbSqliteStorage mAbSqliteStorage = AbSqliteStorage
					.getInstance(context);
			UserDao mUserDao = new UserDao(context);
			mAbSqliteStorage.insertData(user, mUserDao, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateUser(Context context, User user,
			AbDataOperationListener listener) {
		if (user.get_id() > 0) {
			AbSqliteStorage mAbSqliteStorage = AbSqliteStorage
					.getInstance(context);
			UserDao mUserDao = new UserDao(context);
			mAbSqliteStorage.updateData(user, mUserDao, listener);
		} else {
			listener.onFailure(1005, "没有主键");
		}
	}

	public static void deleteUser(Context context, AbStorageQuery query,
			AbDataOperationListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		UserDao mUserDao = new UserDao(context);
		mAbSqliteStorage.deleteData(query, mUserDao, listener);
	}

	public static void insertOrupdateUser(final Context context,
			List<User> users) {
		for (final User user : users) {
			AbStorageQuery query = new AbStorageQuery();
			query.equals("objectId", user.getObjectId());
			DatabaseUtils.findUser(context, query, new AbDataInfoListener() {
				@Override
				public void onSuccess(List<?> arg0) {
					if (arg0 == null || arg0.size() == 0) {
						DatabaseUtils.insertUser(context, user,
								new AbDataInsertListener() {

									@Override
									public void onSuccess(long arg0) {
										System.out
												.println("insert user success :"
														+ arg0);
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										System.err.println("insert user fail :"
												+ arg1);
									}
								});
					} else {
						// User userLocal = (User) arg0.get(0);
						// user.set_id(userLocal.get_id());
						DatabaseUtils.updateUser(context, user,
								new AbDataOperationListener() {

									@Override
									public void onSuccess(long arg0) {
										System.out
												.println("update user success :"
														+ arg0);
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										System.out.println("update user fail :"
												+ arg1);
									}
								});
					}
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					System.out.println("find user fail" + arg1);
				}
			});
		}
	}

	public static void findFriendShip(Context context, AbStorageQuery query,
			AbDataInfoListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		FriendShipDao mFriendShipDao = new FriendShipDao(context);
		mAbSqliteStorage.findData(query, mFriendShipDao, listener);
	}

	public static void insertFriendShip(Context context, FriendShip friendShip,
			AbDataInsertListener listener) {
		try {
			AbSqliteStorage mAbSqliteStorage = AbSqliteStorage
					.getInstance(context);
			FriendShipDao mFriendShipDao = new FriendShipDao(context);
			mAbSqliteStorage.insertData(friendShip, mFriendShipDao, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateFriendShip(Context context, FriendShip friendShip,
			AbDataOperationListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		FriendShipDao mFriendShipDao = new FriendShipDao(context);
		mAbSqliteStorage.updateData(friendShip, mFriendShipDao, listener);
	}

	public static void deleteFriendShip(Context context, AbStorageQuery query,
			AbDataOperationListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		FriendShipDao mFriendShipDao = new FriendShipDao(context);
		mAbSqliteStorage.deleteData(query, mFriendShipDao, listener);
	}

	public static void findAllFriends(final Context context,
			final AbDataInfoListener listener) {
		final MyUser me = BmobUser.getCurrentUser(context, MyUser.class);
		AbStorageQuery query = new AbStorageQuery();
		AbStorageQuery query1 = new AbStorageQuery();
		query.equals("user_1", me.getObjectId());
		query1.equals("user_2", me.getObjectId());
		query.or(query);
		findFriendShip(context, query, new AbDataInfoListener() {

			@Override
			public void onSuccess(List<?> arg0) {
				System.err.println("findAllFriends findFriendShip count = "
						+ arg0.size());
				if (arg0 == null || arg0.size() == 0) {
					listener.onSuccess(arg0);
				} else {
					String[] friendIds = new String[arg0.size()];
					for (int i = 0; i < arg0.size(); i++) {
						friendIds[i] = getFriendIdFromFriendShip(context, me,
								(FriendShip) arg0.get(i));
					}
					AbStorageQuery query3 = new AbStorageQuery();
					query3.in("objectId", friendIds);
					findUser(context, query3, listener);
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				System.err.println("find friendship fail :" + arg1);
			}
		});
	}

	public static void insertOrUpdateFriendShip(final Context context,
			List<FriendShip> friendShips) {
		for (final FriendShip friendShip : friendShips) {
			AbStorageQuery mAbStorageQuery = new AbStorageQuery();
			mAbStorageQuery.equals("user_1", friendShip.getUser_1());
			mAbStorageQuery.equals("user_2", friendShip.getUser_2());
			findFriendShip(context, mAbStorageQuery, new AbDataInfoListener() {

				@Override
				public void onSuccess(List<?> arg0) {
					if (arg0 == null || arg0.size() == 0) {
						insertFriendShip(context, friendShip,
								new AbDataInsertListener() {

									@Override
									public void onSuccess(long arg0) {
										System.out
												.println("insert friendship success :"
														+ arg0);
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										System.err
												.println("insert friendship fail :"
														+ arg1);
									}
								});
					} else {
						FriendShip localFriendShip = (FriendShip) arg0.get(0);
						friendShip.set_id(localFriendShip.get_id());
						updateFriendShip(context, friendShip,
								new AbDataOperationListener() {

									@Override
									public void onSuccess(long arg0) {
										System.out
												.println("update friendship success :"
														+ arg0);
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										System.err
												.println("update friendship fail :"
														+ arg1);
									}
								});
					}
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					System.err.println("find friendship fail :" + arg1);
				}
			});
		}
	}

	public static String getFriendIdFromFriendShip(Context context, MyUser me,
			FriendShip friendShip) {
		String user1 = friendShip.getUser_1();
		String user2 = friendShip.getUser_2();
		if (me.getObjectId().equals(user1)) {
			return user2;
		} else {
			return user1;
		}
	}

	public static void findMessage(Context context, AbStorageQuery query,
			AbDataInfoListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		MessageDao messageDao = new MessageDao(context);
		mAbSqliteStorage.findData(query, messageDao, listener);
	}

	public static void insertMessage(Context context, Message message,
			AbDataInsertListener listener) {
		try {
			AbSqliteStorage mAbSqliteStorage = AbSqliteStorage
					.getInstance(context);
			MessageDao messageDao = new MessageDao(context);
			mAbSqliteStorage.insertData(message, messageDao, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateMessage(Context context, Message message,
			AbDataOperationListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		MessageDao messageDao = new MessageDao(context);
		mAbSqliteStorage.updateData(message, messageDao, listener);
	}

	public static void deleteMessage(Context context, AbStorageQuery query,
			AbDataOperationListener listener) {
		AbSqliteStorage mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		MessageDao messageDao = new MessageDao(context);
		mAbSqliteStorage.deleteData(query, messageDao, listener);
	}

	public static void insertOrUpdateMessages(final Context context,
			List<Message> messages) {
		for (final Message message : messages) {
			AbStorageQuery mAbStorageQuery = new AbStorageQuery();
			mAbStorageQuery.equals(MessageManager.MESSAGE_KEY_UUID,
					message.getUuid());
			findMessage(context, mAbStorageQuery, new AbDataInfoListener() {

				@Override
				public void onSuccess(List<?> arg0) {
					if (arg0 == null || arg0.size() == 0) {
						insertMessage(context, message,
								new AbDataInsertListener() {

									@Override
									public void onSuccess(long arg0) {
										System.out
												.println("insert message success :"
														+ arg0);
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										System.err
												.println("insert message fail :"
														+ arg1);
									}
								});
					} else {
						Message localMessage = (Message) arg0.get(0);
						message.set_id(localMessage.get_id());
						updateMessage(context, message,
								new AbDataOperationListener() {

									@Override
									public void onSuccess(long arg0) {
										System.out
												.println("update message success :"
														+ arg0);
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										System.err
												.println("update message fail :"
														+ arg1);
									}
								});
					}
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					System.err.println("find message fail :" + arg1);
				}
			});
		}
	}
}
