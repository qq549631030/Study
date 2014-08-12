package com.example.myandroid.fragment;

import android.view.View;
import android.widget.ListView;

public interface FragmentListener {

	public void onClickF(int fragmentId, View view);

	public void onListItemClickF(int fragmentId, ListView l, View v,
			int position, long id);

}
