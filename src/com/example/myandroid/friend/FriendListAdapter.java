package com.example.myandroid.friend;

import java.util.List;

import com.ab.util.AbViewUtil;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.example.myandroid.R;
import com.example.myandroid.database.model.User;
import com.example.myandroid.utils.UrlUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter {

	private static final String TAG = FriendListAdapter.class.getSimpleName();

	private Context mContext;
	private List<User> mFriends;
	private ImageLoader mImageLoader;

	private int duration = 300;
	private Animation push_left_in, push_right_in;
	private Animation slide_top_to_bottom, slide_bottom_to_top;

	public FriendListAdapter(Context mContext, ImageLoader imageLoader,
			List<User> mFriends) {
		this.mContext = mContext;
		this.mFriends = mFriends;
		this.mImageLoader = imageLoader;
		push_left_in = AnimationUtils.loadAnimation(mContext,
				R.anim.push_left_in);
		push_right_in = AnimationUtils.loadAnimation(mContext,
				R.anim.push_right_in);
		slide_top_to_bottom = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_top_to_bottom);
		slide_bottom_to_top = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_bottom_to_top);
	}

	public void setFriends(List<User> friends) {
		this.mFriends = friends;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mFriends.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.friend_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.nickname);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.avatar);
			convertView.setTag(viewHolder);
			if (false) {
				Animation push_left_in1 = AnimationUtils.loadAnimation(
						mContext, R.anim.push_left_in);
				push_left_in1.setDuration(duration * position);
				convertView.setAnimation(push_left_in1);
			} else {
				Animation push_right_in1 = AnimationUtils.loadAnimation(
						mContext, R.anim.push_right_in);
				push_right_in1.setDuration(duration * position);
				convertView.setAnimation(push_right_in1);
			}
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		User user = mFriends.get(position);
		String nickname = user.getNickname();
		if (nickname == null) {
			nickname = "";
		}
		viewHolder.mTextView.setText(nickname);
		String url = user.getAvatarUrl();
		if (url == null) {
			url = "";
		}
		ImageListener mListener = ImageLoader.getImageListener(
				viewHolder.mImageView, R.drawable.ic_launcher,
				R.drawable.clear_icon_f);
		Log.d(TAG, "url = " + url);
		if (!TextUtils.isEmpty(url)) {
			mImageLoader.get(url, mListener, AbViewUtil.dip2px(mContext, 40),
					AbViewUtil.dip2px(mContext, 40));
		} else {
			viewHolder.mImageView.setImageResource(R.drawable.ic_launcher);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView mTextView;
		ImageView mImageView;
	}
}
