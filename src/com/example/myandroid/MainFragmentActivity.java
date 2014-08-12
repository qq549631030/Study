package com.example.myandroid;

import cn.bmob.v3.BmobUser;

import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.fragment.CustomViewFragment;
import com.example.myandroid.fragment.FragmentListener;
import com.example.myandroid.fragment.HomeFragment;
import com.example.myandroid.fragment.LeftMenuFragment;
import com.example.myandroid.fragment.PersionalInfoFragment;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.user.LoginActivity;
import com.example.myandroid.utils.ViewUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

@SuppressLint("Recycle")
public class MainFragmentActivity extends FragmentActivity implements
		OnClickListener, FragmentListener {

	private final String TAG = MainFragmentActivity.class.getSimpleName();
	private DrawerLayout mDrawerLayout;

	private ImageButton menuLeft, menuRight;

	FragmentManager mFragmentManager;

	private MyApplication application;

	private int currentPos = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_fragment_activity);
		initViews();
		application = (MyApplication) getApplication();
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		mTransaction.add(R.id.content_frame, new HomeFragment());
		mTransaction.add(R.id.left_drawer, new LeftMenuFragment(), "left_menu");
		mTransaction.commit();
	}

	private void initViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(new SimpleDrawerListener());
		menuLeft = (ImageButton) findViewById(R.id.top_left_menu);
		menuRight = (ImageButton) findViewById(R.id.top_right_menu);
		menuLeft.setOnClickListener(this);
		menuRight.setOnClickListener(this);
	}

	private class SimpleDrawerListener implements DrawerLayout.DrawerListener {

		@Override
		public void onDrawerClosed(View arg0) {
		}

		@Override
		public void onDrawerOpened(View arg0) {
		}

		@Override
		public void onDrawerSlide(View arg0, float arg1) {
		}

		@Override
		public void onDrawerStateChanged(int arg0) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_menu:
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.top_right_menu:
			mDrawerLayout.openDrawer(Gravity.RIGHT);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onClickF(int fragmentId, View view) {
		Log.d(TAG,
				"onClickF fragmentId = " + fragmentId + "view Id = "
						+ view.getId());
	}

	@Override
	public void onListItemClickF(int fragmentId, ListView l, View v,
			int position, long id) {
		Log.d(TAG, "onListItemClickF fragmentId = " + fragmentId
				+ "ListView Id = " + l.getId() + "view id = " + v.getId()
				+ " position = " + position + " id = " + id);

		if (fragmentId == R.id.left_drawer) {
			if (position == currentPos) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
				return;
			}

			if (position == 6) {
				BmobUser.logOut(application);
				Intent loginIntent = new Intent(MainFragmentActivity.this,
						LoginActivity.class);
				startActivity(loginIntent);
				finish();
				return;
			}
			Fragment newFragment;
			switch (position) {
			case 0:
				newFragment = new HomeFragment();
				break;
			case 1:
				newFragment = new CustomViewFragment();
				break;
			case 2:
				newFragment = new PersionalInfoFragment();
				break;

			default:
				newFragment = new HomeFragment();
				break;
			}
			FragmentTransaction mTransaction = mFragmentManager
					.beginTransaction();
			mTransaction = mFragmentManager.beginTransaction();
			mTransaction.setCustomAnimations(R.anim.scale_anim1,
					R.anim.scale_anim2);
			mTransaction.replace(R.id.content_frame, newFragment,
					"content_frame");
			mTransaction.commit();
			currentPos = position;
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} else if (fragmentId == R.id.right_drawer) {
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
		}
	}
}
