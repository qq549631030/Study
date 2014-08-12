package com.example.myandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.example.myandroid.R;
import com.example.myandroid.bmob.BmobUtils;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.friend.AddFriendAdapter;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.image.MergedCache;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddFriendFragment extends ListFragment implements OnClickListener {

	private EditText et_find_name;
	private Button btn_search;
	private boolean first = true;

	private AddFriendAdapter mAdapter;
	private List<MyUser> users = new ArrayList<MyUser>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RequestQueue mRequestQueue = MyApplication
				.getRequestQueue(getActivity());
		MergedCache mergedCache = MyApplication.getMergedCache(getActivity());
		ImageLoader mImageLoader = MyApplication.getImageLoader(getActivity(),
				mRequestQueue, mergedCache);
		mAdapter = new AddFriendAdapter(getActivity(), users, mImageLoader);
		setListAdapter(mAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.add_friend_layout, container,
				false);
		et_find_name = (EditText) view.findViewById(R.id.et_find_name);
		btn_search = (Button) view.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		return view;
	}

	private void updateItem(int index) {
		ListView list = getListView();
		int start = list.getFirstVisiblePosition();
		int end = list.getLastVisiblePosition();
		if (index <= end && index >= start) {
			View view = list.getChildAt(index - start);
			list.getAdapter().getView(index, view, list);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			// users.clear();
			String searchName = et_find_name.getText().toString();
			final ProgressDialog progress = new ProgressDialog(getActivity());
			progress.setMessage("正在搜索...");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			BmobUtils.findUsersByName(getActivity(), searchName,
					new FindListener<MyUser>() {

						@Override
						public void onSuccess(List<MyUser> arg0) {
							progress.dismiss();
							if (arg0 != null && arg0.size() > 0) {
								System.out.println("add users");
								if (first) {
									users.addAll(arg0);
									first = false;
								}
								mAdapter.notifyDataSetChanged();
							} else {
								System.out.println("no user found");
							}
						}

						@Override
						public void onError(int arg0, String arg1) {
							progress.dismiss();
						}
					});
			break;

		default:
			break;
		}
	}

}
