package com.example.myandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ab.db.storage.AbSqliteStorageListener.AbDataInfoListener;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.example.myandroid.database.DatabaseUtils;
import com.example.myandroid.database.model.User;
import com.example.myandroid.friend.FriendListAdapter;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.image.MergedCache;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FriendFragment extends ListFragment {
	private String TAG = FriendFragment.class.getSimpleName();

	private List<User> friends = new ArrayList<User>();
	private FriendListAdapter mAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		getFriendsFromDatabase();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RequestQueue mRequestQueue = MyApplication
				.getRequestQueue(getActivity());
		MergedCache mergedCache = MyApplication.getMergedCache(getActivity());
		ImageLoader mImageLoader = MyApplication.getImageLoader(getActivity(),
				mRequestQueue, mergedCache);
		mAdapter = new FriendListAdapter(getActivity(), mImageLoader, friends);
		setListAdapter(mAdapter);
	}

	private void getFriendsFromDatabase() {
		DatabaseUtils.findAllFriends(getActivity(), new AbDataInfoListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<?> arg0) {
				System.err.println("friend count = " + arg0.size());
				List<User> localFriendList = (List<User>) arg0;
				friends = localFriendList;
				mAdapter.setFriends(friends);
			}

			@Override
			public void onFailure(int arg0, String arg1) {

			}
		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
	
	
}
