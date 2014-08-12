package com.example.myandroid;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.ab.activity.AbActivity;
import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener;
import com.ab.db.storage.AbStorageQuery;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.model.User;
import com.example.myandroid.database.model.UserDao;
import com.example.myandroid.global.Constant;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.user.LoginActivity;
import com.example.myandroid.utils.BmobLocalUtils;
import com.example.myandroid.utils.LockPatternUtils;
import com.example.myandroid.views.LockPatternView;
import com.example.myandroid.views.LockPatternView.Cell;

public class SplashActivity extends AbActivity {

	private final String TAG = SplashActivity.class.getSimpleName();

	private MyApplication application;

	// 数据库操作类
	public AbSqliteStorage mAbSqliteStorage = null;
	public UserDao mUserDao = null;

	private String patternStored;

	private TextView mTextView;
	private LockPatternView mLockPatternView;

	private Runnable mCancelPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
			mTextView.setText("请输入密码");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splach_activity);
		application = (MyApplication) abApplication;
		mAbSqliteStorage = AbSqliteStorage.getInstance(this);
		// 初始化数据库操作实现类
		mUserDao = new UserDao(SplashActivity.this);

		mTextView = (TextView) findViewById(R.id.textView1);

		mLockPatternView = (LockPatternView) findViewById(R.id.lockPatternView1);
		mLockPatternView.setOnPatternListener(mLockPatternListener);

		boolean isUsePattern = abSharedPreferences.getBoolean(
				Constant.IF_USE_PATTERN, false);
		patternStored = abSharedPreferences.getString(Constant.PATTERN_STRING,
				"");
		int[] digital = { 8, 7, 4, 1 };
		isUsePattern = true;
		patternStored = LockPatternUtils.digitalToPattern(digital);
		if (isUsePattern) {
			mTextView.setVisibility(View.VISIBLE);
			mLockPatternView.setVisibility(View.VISIBLE);
		} else {
			mTextView.setVisibility(View.GONE);
			mLockPatternView.setVisibility(View.GONE);
			dologin();
		}
	}

	private LockPatternView.OnPatternListener mLockPatternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			Log.e(TAG, "onPatternStart");
		}

		@Override
		public void onPatternDetected(List<Cell> pattern) {
			Log.e(TAG, "onPatternDetected");
			String result = LockPatternUtils.patternToString(pattern);
			if (result.equals(patternStored)) {
				dologin();
			} else {
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				mLockPatternView.postDelayed(mCancelPatternRunnable, 2000);
				mTextView.setText("密码错误，请重试");
			}
		}

		@Override
		public void onPatternCleared() {
			Log.e(TAG, "onPatternCleared");
			mTextView.setText("请输入密码");
		}

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {
			Log.e(TAG, "onPatternCellAdded");
		}
	};

	private void dologin() {
		String name = abSharedPreferences
				.getString(Constant.USERNAMECOOKIE, "");
		String password = abSharedPreferences.getString(
				Constant.USERPASSWORDCOOKIE, "");
		boolean userPwdRemember = abSharedPreferences.getBoolean(
				Constant.USERPASSWORDREMEMBERCOOKIE, false);
		if (userPwdRemember) {
			loginIMTask(name, password);
		} else {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	public void loginIMTask(final String userName, final String password) {
		final MyUser user = new MyUser();
		user.setUsername(userName);
		user.setPassword(password);
		user.login(SplashActivity.this, new SaveListener() {
			@Override
			public void onSuccess() {
				Log.e(TAG, "onSuccess");
				application.updateLoginParams(user);
				MyUser myUser = BmobUser.getCurrentUser(application,
						MyUser.class);
				application.mUser = myUser;
				saveUserData(BmobLocalUtils.bmob2Local(myUser));
			}

			@Override
			public void onFailure(int code, String msg) {
				Log.e(TAG, "onFailure" + msg);
				removeProgressDialog();
				showToast("登录失败:" + msg);
			}
		});
	}

	/**
	 * 保存登录数据
	 * 
	 */
	public void saveUserData(final User user) {
		Log.e(TAG, "user = " + user.toString());
		// 查询数据
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("username", user.getUsername());

		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao,
				new AbSqliteStorageListener.AbDataInfoListener() {

					@Override
					public void onSuccess(List<?> arg0) {
						if (arg0 == null || arg0.size() == 0) {
							Log.e(TAG, "no data insert");
							mAbSqliteStorage
									.insertData(
											user,
											mUserDao,
											new AbSqliteStorageListener.AbDataInsertListener() {

												@Override
												public void onSuccess(long arg0) {
													Log.e(TAG, "onSuccess:"
															+ arg0);
													removeProgressDialog();
													loginSuccess();
												}

												@Override
												public void onFailure(int arg0,
														String arg1) {
													Log.e(TAG, "onFailure1");
													removeProgressDialog();
													showToast(arg1);
												}
											});
						} else {
							mAbSqliteStorage
									.updateData(
											user,
											mUserDao,
											new AbSqliteStorageListener.AbDataOperationListener() {

												@Override
												public void onFailure(int arg0,
														String arg1) {
													Log.e(TAG, "onFailure2");
													removeProgressDialog();
													showToast(arg1);
												}

												@Override
												public void onSuccess(long arg0) {
													Log.e(TAG, "onSuccess:"
															+ arg0);
													removeProgressDialog();
													loginSuccess();
												}
											});
						}
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						Log.e(TAG, "onFailure3");
						removeProgressDialog();
						showToast(arg1);
					}
				});
	}

	private void loginSuccess() {
		Log.e(TAG, "loginSuccess");
		application.updateRelation();
		Intent intent = new Intent(SplashActivity.this,
				MainSlidingActivity.class);
		startActivity(intent);
		finish();
	}
}
