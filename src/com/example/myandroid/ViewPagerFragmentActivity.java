package com.example.myandroid;

import java.util.LinkedList;

import com.example.myandroid.fragment.HomeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ViewGroup;

public class ViewPagerFragmentActivity extends FragmentActivity {

	private final String TAG = ViewPagerFragmentActivity.class.getSimpleName();

	private ViewPager viewPager;

	private int currentPage;

	private FragmentManager mFragmentManager;

	private MyFragmentPagerAdapter mAdapter;

	private LinkedList<Fragment> mFragments = new LinkedList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager_fragment_layout);
		initViews();
		mFragments.push(new HomeFragment());
		mFragments.push(new HomeFragment());
		mFragments.push(new HomeFragment());
		mFragmentManager = getSupportFragmentManager();
		mAdapter = new MyFragmentPagerAdapter(mFragmentManager);
		viewPager.setAdapter(mAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initViews() {
		viewPager = (ViewPager) findViewById(R.id.view_pager1);
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragments.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			Log.e(TAG, "destroyItem = " + position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

	}

	@Override
	public void onBackPressed() {
		if (currentPage == 0) {
			super.onBackPressed();
		} else {
			viewPager.setCurrentItem(currentPage - 1);
		}
	}
}
