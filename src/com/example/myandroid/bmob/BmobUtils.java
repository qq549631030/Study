package com.example.myandroid.bmob;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.example.myandroid.bmob.model.MyFile;
import com.example.myandroid.bmob.model.MyUser;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import android.content.Context;

public class BmobUtils {

	public static <T extends BmobObject, P> void findBmobData(Context context,
			Map<String, P> parms, FindListener<T> listener) {
		BmobQuery<T> query = new BmobQuery<T>();
		for (Map.Entry<String, P> entry : parms.entrySet()) {
			query.addWhereEqualTo(entry.getKey(), entry.getValue());
		}
		query.findObjects(context, listener);
	}

	public static <T extends BmobObject> void findBombData(Context context,
			BmobQuery<T> query, FindListener<T> listener) {
		query.findObjects(context, listener);
	}

	public static <T extends BmobObject> void insertBmobData(Context context,
			T data, SaveListener listener) {
		data.save(context, listener);
	}

	public static <T extends BmobObject> void updateBmobData(Context context,
			T data, UpdateListener listener) {
		data.update(context, listener);
	}

	public static <T extends BmobObject> void updateBmobData(Context context,
			T data, String objectId, UpdateListener listener) {
		data.update(context, objectId, listener);
	}

	public static <T extends BmobObject> void deleteBmobData(Context context,
			T data, DeleteListener listener) {
		data.delete(context, listener);
	}

	public static void uploadInstallaction(Context context) {
		try {
			final Context applicationContext = context.getApplicationContext();
			final BmobInstallation mInstallation = BmobInstallation
					.getCurrentInstallation(applicationContext);
			BmobQuery<BmobInstallation> query = new BmobQuery<BmobInstallation>();
			query.addWhereEqualTo("installationId",
					mInstallation.getInstallationId());
			query.findObjects(applicationContext,
					new FindListener<BmobInstallation>() {

						@Override
						public void onSuccess(List<BmobInstallation> arg0) {
							if (arg0 == null || arg0.size() == 0) {
								mInstallation.save(applicationContext,
										new SaveListener() {

											@Override
											public void onSuccess() {
												System.out
														.println("uploadInstallaction success");
											}

											@Override
											public void onFailure(int arg0,
													String arg1) {
												System.out
														.println("uploadInstallaction fail:"
																+ arg1);
											}
										});
							} else {
								System.out
										.println("Installaction already save on server");
							}
						}

						@Override
						public void onError(int arg0, String arg1) {
							System.out.println("findObjects fail:" + arg1);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void uploadFile(Context context, File file) {
		try {
			final Context applicationContext = context.getApplicationContext();
			final BmobFile mBmobFile = new BmobFile(file);
			mBmobFile.uploadblock(applicationContext, new UploadFileListener() {
				@Override
				public void onSuccess() {
					MyFile myFile = new MyFile();
					myFile.setGroup(mBmobFile.getGroup());
					myFile.setFilename(mBmobFile.getFilename());
					myFile.setFileUrl(mBmobFile.getFileUrl());
					myFile.setFile(mBmobFile);
					myFile.save(applicationContext, new SaveListener() {
						@Override
						public void onSuccess() {
							System.out.println("uploadFile onSuccess:");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							System.out.println("uploadFile fail:" + arg1);
						}
					});
				}

				@Override
				public void onProgress(Integer arg0) {

				}

				@Override
				public void onFailure(int arg0, String arg1) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateUserAvatar(Context context, File avater,
			final UpdateListener listener) {
		try {
			final Context applicationContext = context.getApplicationContext();
			final BmobFile mBmobFile = new BmobFile(avater);
			mBmobFile.uploadblock(applicationContext, new UploadFileListener() {
				@Override
				public void onSuccess() {
					MyUser myUser = BmobUser.getCurrentUser(applicationContext,
							MyUser.class);
					myUser.setAvatarUrl(mBmobFile.getFileUrl());
					myUser.update(applicationContext, listener);
				}

				@Override
				public void onProgress(Integer arg0) {

				}

				@Override
				public void onFailure(int arg0, String arg1) {
					listener.onFailure(arg0, arg1);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 云端代码
	 */
	public static void callCloudCode(Context context, String methodName,
			JSONObject parms, CloudCodeListener listener) {
		AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
		ace.callEndpoint(context, methodName, parms, listener);
	}

	public static void findUsersByName(Context context, String name,
			FindListener<MyUser> listener) {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereStartsWith("nickname", name);
		findBombData(context, query, listener);
	}

}
