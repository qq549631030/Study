package com.example.myandroid;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.myandroid.activity.TooltipsActivity;
import com.example.myandroid.bmob.MyFriendShipUtils;
import com.example.myandroid.bmob.MyMessageUtils;
import com.example.myandroid.bmob.model.MyFriendShip;
import com.example.myandroid.bmob.model.MyMessage;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.DatabaseUtils;
import com.example.myandroid.database.model.FriendShip;
import com.example.myandroid.database.model.Message;
import com.example.myandroid.database.model.User;
import com.example.myandroid.fragment.AddFriendFragment;
import com.example.myandroid.fragment.CustomViewFragment;
import com.example.myandroid.fragment.FragmentListener;
import com.example.myandroid.fragment.FriendFragment;
import com.example.myandroid.fragment.HomeFragment;
import com.example.myandroid.fragment.LeftMenuFragment;
import com.example.myandroid.fragment.PersionalInfoFragment;
import com.example.myandroid.fragment.RightMenuFragment;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.user.LoginActivity;
import com.example.myandroid.utils.BmobLocalUtils;
import com.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainSlidingActivity extends FragmentActivity implements
		OnClickListener, FragmentListener {
	private final String TAG = MainFragmentActivity.class.getSimpleName();

	private ImageButton menuLeft, menuRight;

	FragmentManager mFragmentManager;

	private MyApplication application;

	private SlidingMenu slidingMenu;

	private int currentLeftPos = -1;

	private int currentRightPos = -1;

	private boolean currentLeftSide = true;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_sliding_activity);
		initSlidingMenu();
		initViews();
		application = (MyApplication) getApplication();
		mFragmentManager = getSupportFragmentManager();
		FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
		mTransaction.add(R.id.content_frame, new HomeFragment());
		mTransaction.add(R.id.sliding_left_menu_frame, new LeftMenuFragment());
		mTransaction
				.add(R.id.sliding_right_menu_frame, new RightMenuFragment());
		mTransaction.commit();
		updateFriends();
		updateMessages();
	}

	private void initSlidingMenu() {
		WindowManager wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = wManager.getDefaultDisplay().getWidth();
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setFadeDegree(1f);
		slidingMenu.setBehindWidth((int) (screenWidth * 0.8));
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.sliding_left_menu_frame);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setSecondaryMenu(R.layout.sliding_right_menu_frame);
		slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		slidingMenu.setBehindScrollScale(1.0f);
	}

	private void initViews() {
		menuLeft = (ImageButton) findViewById(R.id.top_left_menu);
		menuRight = (ImageButton) findViewById(R.id.top_right_menu);
		menuLeft.setOnClickListener(this);
		menuRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_menu:
			if (slidingMenu.isMenuShowing()) {
				slidingMenu.showContent();
			} else {
				slidingMenu.showMenu();
			}
			break;
		case R.id.top_right_menu:
			if (slidingMenu.isMenuShowing()) {
				slidingMenu.showContent();
			} else {
				slidingMenu.showSecondaryMenu();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (slidingMenu.isMenuShowing()) {
			slidingMenu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClickF(int fragmentId, View view) {

	}

	@Override
	public void onListItemClickF(int fragmentId, ListView l, View v,
			int position, long id) {
		if (fragmentId == R.id.sliding_left_menu_frame) {
			if (position == currentLeftPos && currentLeftSide) {
				slidingMenu.showContent();
				return;
			}

			if (position == 6) {
				BmobUser.logOut(application);
				Intent loginIntent = new Intent(MainSlidingActivity.this,
						LoginActivity.class);
				startActivity(loginIntent);
				finish();
				return;
			}
			if (position == 3) {
				Intent intent = new Intent(MainSlidingActivity.this,
						MainActionBarActivity.class);
				startActivity(intent);
				slidingMenu.showContent();
				return;
			}

			if (position == 5) {
				Intent intent = new Intent(MainSlidingActivity.this,
						TooltipsActivity.class);
				startActivity(intent);
				slidingMenu.showContent();
				return;
			}

			Fragment newFragment;
			switch (position) {
			case 0:
				newFragment = new HomeFragment();
				break;
			case 1:
				newFragment = new FriendFragment();
				break;
			case 2:
				newFragment = new PersionalInfoFragment();
				break;
			case 4:
				newFragment = new CustomViewFragment();
				break;

			default:
				newFragment = new HomeFragment();
				break;
			}
			FragmentTransaction mTransaction = mFragmentManager
					.beginTransaction();
			mTransaction.setCustomAnimations(R.anim.scale_anim1,
					R.anim.scale_anim2);
			mTransaction.replace(R.id.content_frame, newFragment,
					"content_frame");
			mTransaction.commit();
			currentLeftSide = true;
			currentLeftPos = position;
			slidingMenu.showContent();
		} else if (fragmentId == R.id.sliding_right_menu_frame) {
			if (position == currentRightPos && !currentLeftSide) {
				slidingMenu.showContent();
				return;
			}
			Fragment newFragment = null;
			switch (position) {
			case 0:
				newFragment = new AddFriendFragment();
				break;

			default:
				break;
			}
			if (newFragment != null) {
				FragmentTransaction mTransaction = mFragmentManager
						.beginTransaction();
				mTransaction.setCustomAnimations(R.anim.scale_anim1,
						R.anim.scale_anim2);
				mTransaction.replace(R.id.content_frame, newFragment,
						"content_frame");
				mTransaction.commit();
				currentLeftSide = false;
				currentRightPos = position;
			}
			slidingMenu.showContent();
		}
	}

	private void updateFriends() {
		MyFriendShipUtils.getAllFriendShip(MainSlidingActivity.this,
				new FindListener<MyFriendShip>() {

					@Override
					public void onSuccess(List<MyFriendShip> arg0) {
						List<FriendShip> friendShips = new ArrayList<FriendShip>();
						for (MyFriendShip myFriendShip : arg0) {
							friendShips.add(BmobLocalUtils
									.bmob2Local(myFriendShip));
						}
						DatabaseUtils.insertOrUpdateFriendShip(
								getApplicationContext(), friendShips);
					}

					@Override
					public void onError(int arg0, String arg1) {

					}
				});
		MyFriendShipUtils.getAllFriends(MainSlidingActivity.this,
				new FindListener<MyUser>() {

					@Override
					public void onError(int arg0, String arg1) {

					}

					@Override
					public void onSuccess(List<MyUser> arg0) {
						List<User> users = new ArrayList<User>();
						for (MyUser myUser : arg0) {
							User user = BmobLocalUtils.bmob2Local(myUser);
							System.out.println(user.toString());
							users.add(user);
						}
						DatabaseUtils.insertOrupdateUser(
								getApplicationContext(), users);
					}
				});
	}

	private void updateMessages() {
		MyMessageUtils.getAllMessage(MainSlidingActivity.this,
				new FindListener<MyMessage>() {

					@Override
					public void onError(int arg0, String arg1) {

					}

					@Override
					public void onSuccess(List<MyMessage> arg0) {
						List<Message> messages = new ArrayList<Message>();
						for (MyMessage myMessage : arg0) {
							Message message = BmobLocalUtils
									.bmob2Local(myMessage);
							messages.add(message);
						}
						DatabaseUtils.insertOrUpdateMessages(
								MainSlidingActivity.this, messages);
					}
				});
	}
}
