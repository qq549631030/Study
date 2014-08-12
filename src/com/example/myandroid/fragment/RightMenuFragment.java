package com.example.myandroid.fragment;

import com.example.myandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RightMenuFragment extends ListFragment implements OnClickListener {
	public static final String[] TITLES = { "添加好友", "消息中心" };

	private FragmentListener mListener;

	private TextView persionalInfo;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, TITLES));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.right_menu_fragment, container,
				false);
		persionalInfo = (TextView) view.findViewById(R.id.textView1);
		persionalInfo.setOnClickListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mListener = (FragmentListener) activity;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mListener.onListItemClickF(getId(), l, v, position, id);
	}

	@Override
	public void onClick(View v) {
		mListener.onClickF(getId(), v);
	}

}
