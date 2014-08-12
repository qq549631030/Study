package com.example.myandroid.global;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.ab.global.AbConstant;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.bmob.model.MyRelation;
import com.example.myandroid.image.MergedCache;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyApplication extends Application {

	// 登录用户
	public MyUser mUser = null;

	public boolean userPasswordRemember = false;
	public boolean ad = false;
	public boolean isFirstStart = true;
	public SharedPreferences mSharedPreferences = null;

	BmobPushManager bmobPush;

	private static RequestQueue mRequestQueue;

	private static ImageLoader mImageLoader;

	private static MergedCache mergedCache;

	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPreferences = getSharedPreferences(AbConstant.SHAREPATH,
				Context.MODE_PRIVATE);
		Bmob.initialize(this, Constant.APP_ID);
		BmobPush.startWork(this, Constant.APP_ID);

		bmobPush = new BmobPushManager(this);
		initLoginParams();
		uploadInstallaction();
	}

	public synchronized static RequestQueue getRequestQueue(Context context) {
		if (mRequestQueue == null) {
			synchronized (MyApplication.class) {
				if (mRequestQueue == null) {
					mRequestQueue = Volley.newRequestQueue(context);
				}
			}
		}
		return mRequestQueue;
	}

	public static MergedCache getMergedCache(Context context) {
		if (mergedCache == null) {
			synchronized (MyApplication.class) {
				if (mergedCache == null) {
					mergedCache = new MergedCache(new File(
							Constant.IMAGE_CACHE_DIR), 100 * 1024 * 1024);
				}
			}
		}
		return mergedCache;
	}

	public synchronized static ImageLoader getImageLoader(Context context,
			RequestQueue requestQueue, ImageCache imageCache) {
		if (mImageLoader == null) {
			synchronized (MyApplication.class) {
				if (mImageLoader == null) {
					mImageLoader = new ImageLoader(requestQueue, imageCache);
				}
			}
		}
		return mImageLoader;
	}

	/**
	 * 上次登录参数
	 */
	private void initLoginParams() {
		SharedPreferences preferences = getSharedPreferences(
				AbConstant.SHAREPATH, Context.MODE_PRIVATE);
		String userName = preferences.getString(Constant.USERNAMECOOKIE, null);
		String userPwd = preferences.getString(Constant.USERPASSWORDCOOKIE,
				null);
		Boolean userPwdRemember = preferences.getBoolean(
				Constant.USERPASSWORDREMEMBERCOOKIE, false);
		if (userName != null) {
			mUser = new MyUser();
			mUser.setUsername(userName);
			mUser.setPassword(userPwd);
			userPasswordRemember = userPwdRemember;
		}
	}

	public void updateLoginParams(MyUser user) {
		mUser = user;
		if (userPasswordRemember) {
			Editor editor = mSharedPreferences.edit();
			editor.putString(Constant.USERNAMECOOKIE, user.getUsername());
			editor.putString(Constant.USERPASSWORDCOOKIE, user.getPassword());
			editor.putBoolean(Constant.ISFIRSTSTART, false);
			editor.commit();
		} else {
			Editor editor = mSharedPreferences.edit();
			editor.putBoolean(Constant.ISFIRSTSTART, false);
			editor.commit();
		}
		isFirstStart = false;
	}

	/**
	 * 清空上次登录参数
	 */
	public void clearLoginParams() {
		Editor editor = mSharedPreferences.edit();
		editor.clear();
		editor.commit();
		mUser = null;
	}

	private void uploadInstallaction() {
		final BmobInstallation mInstallation = BmobInstallation
				.getCurrentInstallation(this);
		BmobQuery<BmobInstallation> query = new BmobQuery<BmobInstallation>();
		query.addWhereEqualTo("installationId",
				mInstallation.getInstallationId());
		query.findObjects(this, new FindListener<BmobInstallation>() {

			@Override
			public void onSuccess(List<BmobInstallation> arg0) {
				if (arg0 == null || arg0.size() == 0) {
					mInstallation.save(getApplicationContext(),
							new SaveListener() {

								@Override
								public void onSuccess() {
									System.out
											.println("uploadInstallaction success");
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									System.out
											.println("uploadInstallaction fail:"
													+ arg1);
								}
							});
				} else {
					System.out.println("Installaction already save on server");
					// already save on server
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				System.out.println("findObjects fail:" + arg1);
			}
		});
	}

	public void updateRelation() {
		BmobInstallation mInstallation = BmobInstallation
				.getCurrentInstallation(this);
		final MyRelation mRelation = new MyRelation();
		mRelation.setUserId(mUser.getObjectId());
		mRelation.setInstallactionId(mInstallation.getInstallationId());
		BmobQuery<MyRelation> query = new BmobQuery<MyRelation>();
		query.addWhereEqualTo("userId", mUser.getObjectId());
		query.findObjects(this, new FindListener<MyRelation>() {
			@Override
			public void onError(int arg0, String arg1) {
				System.out.println("findObjects fail:" + arg1 + " errorCode = "
						+ arg0);
				if (arg0 == 101) {
					mRelation.save(getApplicationContext(), new SaveListener() {
						@Override
						public void onSuccess() {
							System.out.println("save relation success");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							System.out.println("save relation fail:" + arg1);
						}
					});
				}
			}

			@Override
			public void onSuccess(List<MyRelation> arg0) {
				if (arg0 == null || arg0.size() == 0) {
					mRelation.save(getApplicationContext(), new SaveListener() {
						@Override
						public void onSuccess() {
							System.out.println("save relation success");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							System.out.println("save relation fail:" + arg1);
						}
					});
				} else {
					mRelation.update(getApplicationContext(), arg0.get(0)
							.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							System.out.println("update relation success");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							System.out.println("update relation fail:" + arg1);
						}
					});
				}
			}
		});
	}
}
