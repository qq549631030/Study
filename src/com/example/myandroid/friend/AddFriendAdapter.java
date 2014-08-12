package com.example.myandroid.friend;

import java.util.List;

import org.json.JSONException;

import cn.bmob.v3.listener.CloudCodeListener;

import com.ab.util.AbViewUtil;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.example.myandroid.R;
import com.example.myandroid.bmob.model.MyMessage;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.push.MessagePushUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddFriendAdapter extends BaseAdapter {

	private Context mContext;
	private List<MyUser> users;
	private ImageLoader mImageLoader;

	public AddFriendAdapter(Context mContext, List<MyUser> users,
			ImageLoader mImageLoader) {
		this.mContext = mContext;
		this.users = users;
		this.mImageLoader = mImageLoader;
	}

	public void setFriends(List<MyUser> users) {
		this.users = users;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView " + position);
		ViewHolder viewHolder = null;
		final MyUser user = users.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_add_friend, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.name);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.avatar);
			viewHolder.mButton = (Button) convertView
					.findViewById(R.id.btn_add);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageListener mListener = ImageLoader.getImageListener(
				viewHolder.mImageView, R.drawable.ic_launcher,
				R.drawable.clear_icon_f);
		String avatar = user.getAvatarUrl();
		if (avatar != null && !avatar.equals("")) {
			mImageLoader.get(avatar, mListener,
					AbViewUtil.dip2px(mContext, 40),
					AbViewUtil.dip2px(mContext, 40));
		} else {
			viewHolder.mImageView.setImageResource(R.drawable.ic_launcher);
		}
		String nickname = user.getNickname();
		if (nickname == null) {
			nickname = "";
		}
		viewHolder.mTextView.setText(nickname);
		viewHolder.mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					MyMessage myMessage = MessagePushUtils
							.createAddFriendMessage(mContext, user, "请求加为好友");
					MessagePushUtils.sendMessage(mContext, myMessage,
							new CloudCodeListener() {

								@Override
								public void onSuccess(Object arg0) {
									System.out.println("请求发送成功");
									
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									System.out.println("请求发送失败:" + arg1);
								}
							});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView mTextView;
		ImageView mImageView;
		Button mButton;
	}
}
