package com.example.myandroid.user;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.ab.activity.AbActivity;
import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.util.AbStrUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.example.myandroid.MainFragmentActivity;
import com.example.myandroid.MainSlidingActivity;
import com.example.myandroid.R;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.model.User;
import com.example.myandroid.database.model.UserDao;
import com.example.myandroid.global.Constant;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.utils.BmobLocalUtils;

public class LoginActivity extends AbActivity {

	private final String TAG = LoginActivity.class.getSimpleName();

	private EditText userName = null;
	private EditText userPwd = null;
	private MyApplication application;
	private String mStr_name = null;
	private String mStr_pwd = null;
	private ImageButton mClear1;
	private ImageButton mClear2;
	private AbTitleBar mAbTitleBar = null;

	private Button loginBtn = null;

	// 数据库操作类
	public AbSqliteStorage mAbSqliteStorage = null;
	public UserDao mUserDao = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.login);
		application = (MyApplication) abApplication;

		// 初始化AbSqliteStorage
		mAbSqliteStorage = AbSqliteStorage.getInstance(this);

		// 初始化数据库操作实现类
		mUserDao = new UserDao(LoginActivity.this);

		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.im_login);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		userName = (EditText) this.findViewById(R.id.userName);
		userPwd = (EditText) this.findViewById(R.id.userPwd);
		CheckBox checkBox = (CheckBox) findViewById(R.id.login_check);
		mClear1 = (ImageButton) findViewById(R.id.clearName);
		mClear2 = (ImageButton) findViewById(R.id.clearPwd);

		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		Button register = (Button) this.findViewById(R.id.registerBtn);
		loginBtn.setOnClickListener(new LoginOnClickListener());

		Button pwdBtn = (Button) findViewById(R.id.pwdBtn);
		pwdBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(LoginActivity.this,
				// FindPwdActivity.class);
				// startActivity(intent);
			}
		});

		mAbTitleBar.getLogoView().setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Editor editor = abSharedPreferences.edit();
				editor.putBoolean(Constant.USERPASSWORDREMEMBERCOOKIE,
						isChecked);
				editor.commit();
				application.userPasswordRemember = isChecked;
			}
		});

		String name = abSharedPreferences
				.getString(Constant.USERNAMECOOKIE, "");
		String password = abSharedPreferences.getString(
				Constant.USERPASSWORDCOOKIE, "");
		boolean userPwdRemember = abSharedPreferences.getBoolean(
				Constant.USERPASSWORDREMEMBERCOOKIE, false);

		if (userPwdRemember) {
			userName.setText(name);
			userPwd.setText(password);
			checkBox.setChecked(true);
		} else {
			userName.setText("");
			userPwd.setText("");
			checkBox.setChecked(false);
		}

		initTitleRightLayout();

		userName.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = userName.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear1.setVisibility(View.VISIBLE);
					if (!AbStrUtil.isNumberLetter(str)) {
						str = str.substring(0, length - 1);
						userName.setText(str);
						String str1 = userName.getText().toString().trim();
						userName.setSelection(str1.length());
						showToast(R.string.error_name_expr);
					}
					mClear1.postDelayed(new Runnable() {

						@Override
						public void run() {
							mClear1.setVisibility(View.INVISIBLE);
						}

					}, 5000);

				} else {
					mClear1.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {

			}
		});

		userPwd.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = userPwd.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear2.setVisibility(View.VISIBLE);
					if (!AbStrUtil.isNumberLetter(str)) {
						str = str.substring(0, length - 1);
						userPwd.setText(str);
						String str1 = userPwd.getText().toString().trim();
						userPwd.setSelection(str1.length());
						showToast(R.string.error_pwd_expr);
					}
					mClear2.postDelayed(new Runnable() {

						@Override
						public void run() {
							mClear2.setVisibility(View.INVISIBLE);
						}

					}, 5000);
				} else {
					mClear2.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {

			}
		});

		mClear1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userName.setText("");
			}
		});

		mClear2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userPwd.setText("");
			}
		});
	}

	private void initTitleRightLayout() {

	}

	public class LoginOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == loginBtn) {
				mStr_name = userName.getText().toString();
				mStr_pwd = userPwd.getText().toString();

				if (TextUtils.isEmpty(mStr_name)) {
					showToast(R.string.error_name);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}

				if (!AbStrUtil.isNumberLetter(mStr_name)) {
					showToast(R.string.error_name_expr);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}

				if (AbStrUtil.strLength(mStr_name) < 3) {
					showToast(R.string.error_name_length1);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}

				if (AbStrUtil.strLength(mStr_name) > 20) {
					showToast(R.string.error_name_length2);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(mStr_pwd)) {
					showToast(R.string.error_pwd);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}

				if (AbStrUtil.strLength(mStr_pwd) < 6) {
					showToast(R.string.error_pwd_length1);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}

				if (AbStrUtil.strLength(mStr_pwd) > 20) {
					showToast(R.string.error_pwd_length2);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}

				showProgressDialog("登录到IM");

				loginIMTask(mStr_name, mStr_pwd);

			}
		}
	}

	public void loginIMTask(final String userName, final String password) {
		final MyUser user = new MyUser();
		user.setUsername(userName);
		user.setPassword(password);
		user.login(LoginActivity.this, new SaveListener() {
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
		Intent intent = new Intent(LoginActivity.this,
				MainSlidingActivity.class);
		startActivity(intent);
		finish();
	}

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Log.d(TAG, msg);
	}
}
