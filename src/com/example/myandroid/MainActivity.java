package com.example.myandroid;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONStringer;

import com.example.myandroid.database.DataStore;
import com.example.myandroid.database.MySQLiteOpenHelper;
import com.example.myandroid.utils.FileUtil;
import com.example.myandroid.utils.FileOptionListener;
import com.example.myandroid.views.AnimationView;
import com.example.myandroid.xml.BookParser;
import com.example.myandroid.xml.DomBookParser;
import com.example.myandroid.xml.PullBookParser;
import com.example.myandroid.xml.SAXBookParser;
import com.example.myandroid.xml.model.Book;
import com.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private String TAG = MainActivity.class.getSimpleName();

	private Button mButton1, mButton2;

	private AnimationView mAnimationView;

	private SlidingMenu slidingMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initViews();
		initSlidingMenu();
	}

	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindWidth(200);// 菜单宽带
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setBehindScrollScale(1.0f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	}

	private void initViews() {
		mButton1 = (Button) findViewById(R.id.button1);
		mButton2 = (Button) findViewById(R.id.button2);
		mAnimationView = (AnimationView) findViewById(R.id.animationView);
		mButton1.setOnClickListener(this);
		mButton2.setOnClickListener(this);
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
			testDatabase2();
			break;
		case R.id.button2:
			testQueryData();
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
			InputStream input = getAssets().open("books.xml");
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
		JSONStringer mStringer = new JSONStringer();
		try {
			mStringer.object();
			mStringer.key("test");
			mStringer.value("test1");
			mStringer.key("test");
			mStringer.value("test2");
			mStringer.endObject();
			Log.e(TAG, mStringer.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void testDatabase1() {
		MySQLiteOpenHelper mHelper = new MySQLiteOpenHelper(MainActivity.this);
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues mValues = new ContentValues();
		mValues.put(DataStore.UserTable.USER_NAME, "huangxiang");
		mValues.put(DataStore.UserTable.USER_AGE, "24");
		database.insert(DataStore.UserTable.TABLE_NAME.toString(), null,
				mValues);
	}

	private void testDatabase2() {
		ContentResolver mResolver = getContentResolver();
		ContentValues mValues = new ContentValues();
		mValues.put(DataStore.UserTable.USER_NAME, "huangxiang");
		mValues.put(DataStore.UserTable.USER_AGE, "24");
		mResolver.insert(DataStore.UserTable.CONTENT_URI, mValues);
	}

	private void testQueryData() {
		ContentResolver mResolver = getContentResolver();
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
}
