package com.example.myandroid.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.myandroid.R;
import com.example.myandroid.bmob.BmobListener;
import com.example.myandroid.bmob.BmobUtils;
import com.example.myandroid.bmob.MyFriendShipUtils;
import com.example.myandroid.bmob.MyFriendUtils;
import com.example.myandroid.bmob.model.MyFriend;
import com.example.myandroid.bmob.model.MyMessage;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.DataStore;
import com.example.myandroid.database.MySQLiteOpenHelper;
import com.example.myandroid.database.model.User;
import com.example.myandroid.friend.FriendManager;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.http.JsonParserUtils;
import com.example.myandroid.push.CloudMethodManager;
import com.example.myandroid.push.MessageManager;
import com.example.myandroid.push.MessagePushUtils;
import com.example.myandroid.utils.FileUtil;
import com.example.myandroid.utils.FileOptionListener;
import com.example.myandroid.views.AnimationView;
import com.example.myandroid.views.CircleProgressView;
import com.example.myandroid.xml.BookParser;
import com.example.myandroid.xml.PullBookParser;
import com.example.myandroid.xml.model.Book;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class HomeFragment extends Fragment implements OnClickListener {

	private String TAG = HomeFragment.class.getSimpleName();

	MyApplication application;
	private Button mButton1, mButton2;
	private AnimationView mAnimationView;
	private CircleProgressView mCircleProgressView;
	private float mProgress = 0.0f;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.home_layout, container, false);
		mButton1 = (Button) view.findViewById(R.id.button1);
		mButton2 = (Button) view.findViewById(R.id.button2);
		mButton1.setOnClickListener(this);
		mButton2.setOnClickListener(this);
		mAnimationView = (AnimationView) view.findViewById(R.id.animationView1);
		mCircleProgressView = (CircleProgressView) view
				.findViewById(R.id.circleProgressView1);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		application = (MyApplication) getActivity().getApplication();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			// testWriteToFile();
			// testReadFormFile();
			// testXmlParser();
			// mAnimationView.flashView();
			// testJson();
			// testDatabase1();
			// testDatabase2();
			// mProgress = mProgress + 0.05f;
			// if (mProgress >= 1.0f) {
			// mProgress = 1.0f;
			// }
			// mCircleProgressView.setTitleText((int) (mProgress * 10000) + "");
			// mCircleProgressView.setSubText("步");
			// mCircleProgressView.setProgress(mProgress);
			// mCircleProgressView.flashView(1000);

			ContentValues cv = new ContentValues();
			cv.put("package", getActivity().getPackageName());
			cv.put("class", "com.example.myandroid.SplashActivity");
			cv.put("badgecount", 1);
			getActivity().getContentResolver().insert(
					Uri.parse("content://com.sec.badge/apps"), cv);

			break;
		case R.id.button2:
			// mProgress = mProgress - 0.05f;
			// if (mProgress <= 0.0f) {
			// mProgress = 0.0f;
			// }
			// mCircleProgressView.setTitleText((int) (mProgress * 10000) + "");
			// mCircleProgressView.setSubText("步");
			// mCircleProgressView.setProgress(mProgress);
			// mCircleProgressView.flashView(1000);

			// testQueryData();
			// testJson();
			// testAddFriend();
			// testAddFriendShip();
			Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.shake);
			mButton2.startAnimation(mAnimation);
			break;
		default:
			break;
		}
	}

	private void testWriteToFile() {
		String dataString = "";
		for (int i = 0; i < 500; i++) {
			dataString = dataString + "abcdefghijklmnopqrstuvwxyz";
		}
		FileUtil.writeToFile(dataString, Environment
				.getExternalStorageDirectory().getAbsolutePath() + "/test.txt",
				new FileOptionListener() {
					@Override
					public void onProgress(int progress) {
						Log.d(TAG, "progress = " + progress);
					}

					@Override
					public void onFail(String code) {
						Log.d(TAG, "onFail : " + code);
					}

					@Override
					public void onSuccess(byte[] data) {

					}
				});
	}

	private void testReadFormFile() {
		FileUtil.readFromFile(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/test.txt", new FileOptionListener() {

			@Override
			public void onSuccess(byte[] data) {
				String dataString = new String(data);
				Log.d(TAG, "success data = " + dataString);
			}

			@Override
			public void onProgress(int progress) {
				Log.d(TAG, "progress = " + progress);
			}

			@Override
			public void onFail(String code) {
				Log.d(TAG, "onFail : " + code);
			}
		});
	}

	private void testXmlParser() {
		try {
			BookParser parser;
			// parser = new SAXBookParser();
			// parser = new DomBookParser();
			parser = new PullBookParser();
			InputStream input = getActivity().getAssets().open("books.xml");
			List<Book> books = parser.parser(input);
			for (Book book : books) {
				Log.i(TAG, book.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testJson() {
		// JSONStringer mStringer = new JSONStringer();
		// try {
		// mStringer.object();
		// mStringer.key("test");
		// mStringer.value("test1");
		// mStringer.key("test");
		// mStringer.value("test2");
		// mStringer.endObject();
		// Log.e(TAG, mStringer.toString());
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

		try {
			JSONObject mObject = new JSONObject();
			JSONArray mArray = new JSONArray();
			mObject.put("sleep", "sleepdata1");
			mObject.put("sleep", mArray);
			Log.e(TAG, mObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testDatabase1() {
		MySQLiteOpenHelper mHelper = new MySQLiteOpenHelper(getActivity());
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues mValues = new ContentValues();
		mValues.put(DataStore.UserTable.USER_NAME, "huangxiang");
		mValues.put(DataStore.UserTable.USER_AGE, "24");
		database.insert(DataStore.UserTable.TABLE_NAME.toString(), null,
				mValues);
	}

	private void testDatabase2() {
		ContentResolver mResolver = getActivity().getContentResolver();
		ContentValues mValues = new ContentValues();
		mValues.put(DataStore.UserTable.USER_NAME, "huangxiang");
		mValues.put(DataStore.UserTable.USER_AGE, "24");
		mResolver.insert(DataStore.UserTable.CONTENT_URI, mValues);
	}

	private void testQueryData() {
		ContentResolver mResolver = getActivity().getContentResolver();
		Cursor mCursor = mResolver.query(DataStore.UserTable.CONTENT_URI, null,
				null, null, null);
		while (mCursor.moveToNext()) {
			int idIndex = mCursor.getColumnIndex(DataStore.UserTable._ID);
			int nameIndex = mCursor
					.getColumnIndex(DataStore.UserTable.USER_NAME);
			int ageIndex = mCursor.getColumnIndex(DataStore.UserTable.USER_AGE);
			int id = mCursor.getInt(idIndex);
			String name = mCursor.getString(nameIndex);
			String age = mCursor.getString(ageIndex);
			Log.d(TAG, "_id = " + id + " name = " + name + " age = " + age);
		}
	}

	private void pushMessage() {
		MyUser myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
		MyMessage myMessage = new MyMessage();
		myMessage.setFromId(myUser.getObjectId());
		myMessage.setTargetId("278461da62");
		myMessage.setMsgType(MessageManager.MESSAGE_TYPE_ADD_FRIEND_REQUEST);
		myMessage.setMsgState("0");
		myMessage.setContent(myUser.getNickname() + "请求加你为好友");
		try {
			MessagePushUtils.sendMessage(getActivity(), myMessage,
					new CloudCodeListener() {

						@Override
						public void onSuccess(Object arg0) {
							System.out.println("onSuccess:" + arg0.toString());
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							System.out.println("onFailure:" + arg1);
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void testAddFriend() {
		MyFriendUtils.addFriend(getActivity(), "5be6024ecd", "2",
				new BmobListener() {

					@Override
					public void onSuccess() {
						System.out.println("add friend success");
					}

					@Override
					public void onError(int arg0, String arg1) {
						System.err.println("add friend fail error code:" + arg0
								+ " : " + arg1);
					}
				});
	}

	private void testAddFriendShip() {
		MyFriendShipUtils.addFriendShip(getActivity(), "05c318c6ba",
				FriendManager.FRIEND_FROM_1, new BmobListener() {

					@Override
					public void onSuccess() {
						System.out.println("add friend ship success");
					}

					@Override
					public void onError(int arg0, String arg1) {
						System.err.println("add friend fail error code:" + arg0
								+ " : " + arg1);
					}

				});
	}
}
