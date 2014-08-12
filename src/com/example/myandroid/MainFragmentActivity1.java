package com.example.myandroid;

import cn.bmob.v3.BmobUser;

import com.example.myandroid.fragment.CustomViewFragment;
import com.example.myandroid.fragment.FragmentListener;
import com.example.myandroid.fragment.HomeFragment;
import com.example.myandroid.fragment.LeftMenuFragment;
import com.example.myandroid.fragment.PersionalInfoFragment;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.user.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.SimplePanelSlideListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainFragmentActivity1 extends FragmentActivity implements
		OnClickListener, FragmentListener {
	private final String TAG = MainFragmentActivity1.class.getSimpleName();

	private SlidingPaneLayout mSlidingPaneLayout;

	private DrawerLayout mDrawerLayout;

	private ImageButton menuLeft, menuRight;

	FragmentManager mFragmentManager;

	private MyApplication application;

	private int currentPos = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_fragment_activity_1);
		initViews();
		application = (MyApplication) getApplication();
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		mTransaction.add(R.id.content_frame, new HomeFragment());
		mTransaction.add(R.id.left_pane, new LeftMenuFragment(), "left_menu");
		mTransaction.commit();
	}

	private void initViews() {
		mSlidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
		mSlidingPaneLayout.setPanelSlideListener(mPanelSlideListener);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(new SimpleDrawerListener());
		menuLeft = (ImageButton) findViewById(R.id.top_left_menu);
		menuRight = (ImageButton) findViewById(R.id.top_right_menu);
		menuLeft.setOnClickListener(this);
		menuRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_menu:
			if (mSlidingPaneLayout.isOpen()) {
				mSlidingPaneLayout.closePane();
			} else {
				mSlidingPaneLayout.openPane();
			}
			break;
		case R.id.top_right_menu:
			mDrawerLayout.openDrawer(Gravity.RIGHT);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mSlidingPaneLayout.isOpen()) {
			mSlidingPaneLayout.closePane();
		} else {
			super.onBackPressed();
		}
	}

	private SimplePanelSlideListener mPanelSlideListener = new SimplePanelSlideListener() {

		@Override
		public void onPanelClosed(View panel) {
			super.onPanelClosed(panel);
		}

		@Override
		public void onPanelOpened(View panel) {
			super.onPanelOpened(panel);
		}

		@Override
		public void onPanelSlide(View panel, float slideOffset) {
			super.onPanelSlide(panel, slideOffset);
		}

	};

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
	public void onClickF(int fragmentId, View view) {

	}

	@Override
	public void onListItemClickF(int fragmentId, ListView l, View v,
			int position, long id) {
		Log.d(TAG, "onListItemClickF fragmentId = " + fragmentId
				+ "ListView Id = " + l.getId() + "view id = " + v.getId()
				+ " position = " + position + " id = " + id);

		if (fragmentId == R.id.left_pane) {
			if (position == currentPos) {
				mSlidingPaneLayout.closePane();
				return;
			}

			if (position == 6) {
				BmobUser.logOut(application);
				Intent loginIntent = new Intent(MainFragmentActivity1.this,
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
			mSlidingPaneLayout.closePane();
		}
	}

}
