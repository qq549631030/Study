package com.example.myandroid.fragment;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

import com.ab.db.storage.AbSqliteStorageListener.AbDataInfoListener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.example.myandroid.R;
import com.example.myandroid.bmob.BmobUtils;
import com.example.myandroid.bmob.model.MyUser;
import com.example.myandroid.database.DatabaseUtils;
import com.example.myandroid.database.model.Avatar;
import com.example.myandroid.global.Constant;
import com.example.myandroid.global.MyApplication;
import com.example.myandroid.image.MergedCache;
import com.example.myandroid.utils.ImageUtils;
import com.example.myandroid.views.CircleImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class PersionalInfoFragment extends Fragment implements OnClickListener {

	private String TAG = PersionalInfoFragment.class.getSimpleName();

	MyApplication application;

	private CircleImageView avatarView;
	private TextView nicknameView;
	private TextView emailView;
	private TextView genderView;
	private TextView ageView;

	private Bitmap avatarBitmap;

	private final int IMAGE_REQUEST_CODE = 1;
	private final int CAMERA_REQUEST_CODE = 2;
	private final int ZOOM_REQUEST_CODE = 3;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		application = (MyApplication) activity.getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.persional_info_fragment,
				container, false);
		avatarView = (CircleImageView) view.findViewById(R.id.imageView_avatar);
		nicknameView = (TextView) view.findViewById(R.id.textView_nickname);
		emailView = (TextView) view.findViewById(R.id.textView_email);
		genderView = (TextView) view.findViewById(R.id.textView_gender);
		ageView = (TextView) view.findViewById(R.id.textView_age);
		avatarView.setOnClickListener(this);
		nicknameView.setOnClickListener(this);
		emailView.setOnClickListener(this);
		genderView.setOnClickListener(this);
		ageView.setOnClickListener(this);
		initViews();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initViews() {
		final MyUser myUser = BmobUser.getCurrentUser(getActivity(),
				MyUser.class);
		if (myUser.getNickname() != null) {
			nicknameView.setText(myUser.getNickname());
		}
		if (myUser.getEmail() != null) {
			emailView.setText(myUser.getEmail());
		}
		if (myUser.getGender() != null) {
			genderView.setText(myUser.getGender() ? "男" : "女");
		}
		if (myUser.getAge() != null) {
			ageView.setText(myUser.getAge() + "");
		}
		if (myUser.getAvatarUrl() != null) {
			DatabaseUtils.findAvatarFromSd(getActivity(), myUser.getObjectId(),
					new AbDataInfoListener() {

						@Override
						public void onSuccess(List<?> arg0) {
							if (arg0 == null || arg0.size() == 0) {
								loadAvatarFormServer(myUser.getAvatarUrl());
							} else {
								Avatar avatar = (Avatar) arg0.get(0);
								loadAvatarFromLocal(avatar);
							}
						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
		}
	}

	private void loadAvatarFromLocal(Avatar avatar) {
		Log.e(TAG, "loadAvatarFromLocal");
		try {
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			avatarBitmap = BitmapFactory.decodeFile(avatar.getFilepath(),
					bmpFactoryOptions);
			int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
					/ (float) 300);
			int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
					/ (float) 300);
			if (heightRatio > 1 && widthRatio > 1) {
				bmpFactoryOptions.inSampleSize = heightRatio > widthRatio ? heightRatio
						: widthRatio;
			}
			bmpFactoryOptions.inJustDecodeBounds = false;
			File file = new File(avatar.getFilepath());
			if (file.exists()) {
				avatarBitmap = BitmapFactory.decodeFile(avatar.getFilepath(),
						bmpFactoryOptions);
				if (avatarBitmap != null) {
					avatarView.setImageBitmap(avatarBitmap);
				} else {
					loadAvatarFormServer(avatar.getUrl());
				}
			} else {
				loadAvatarFormServer(avatar.getUrl());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadAvatarFormServer(String url) {
		Log.e(TAG, "loadAvatarFormServer");
		downloadAvatarFromServer(url);
	}

	private void downloadAvatarFromServer(final String url) {
		RequestQueue mRequestQueue = MyApplication
				.getRequestQueue(getActivity());
		MergedCache mImageMergedCache = new MergedCache(new File(
				Constant.IMAGE_CACHE_DIR), 100 * 1024 * 1024);
		ImageLoader mImageLoader = MyApplication.getImageLoader(getActivity(),
				mRequestQueue, mImageMergedCache);
		mImageLoader.get(url, new ImageListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.e(TAG, "onErrorResponse");
			}

			@Override
			public void onResponse(ImageContainer arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Log.e(TAG, "onResponse bitmap == null  "
						+ (arg0.getBitmap() == null));
				if (arg0.getBitmap() != null) {
					avatarView.setImageBitmap(arg0.getBitmap());
					saveAvatarToLocal(url, arg0.getBitmap());
				}
			}
		}, 300, 300);

	}

	private void saveAvatarToLocal(String url, Bitmap bitmap) {
		Log.e(TAG, "saveAvatarToLocal");
		DatabaseUtils.saveAvatarToLocal(getActivity(), Constant.AVATAR_DIR,
				url, bitmap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_avatar:
			showDialog();
			break;

		default:
			break;
		}
	}

	private void showDialog() {
		{

			new AlertDialog.Builder(getActivity())
					.setTitle("设置头像")
					.setItems(new String[] { "选择本地图片", "拍照" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										Intent intentFromGallery = new Intent();
										intentFromGallery.setType("image/*"); // 设置文件类型
										intentFromGallery
												.setAction(Intent.ACTION_GET_CONTENT);
										startActivityForResult(
												intentFromGallery,
												IMAGE_REQUEST_CODE);
										break;
									case 1:

										Intent intentFromCapture = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										intentFromCapture.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(new File(
														Constant.TEMP_AVATAR)));
										startActivityForResult(
												intentFromCapture,
												CAMERA_REQUEST_CODE);
										break;
									}
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();

		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, ZOOM_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity();
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				File tempFile = new File(Constant.TEMP_AVATAR);
				startPhotoZoom(Uri.fromFile(tempFile));
				break;
			case ZOOM_REQUEST_CODE:
				Bundle extras = data.getExtras();
				if (extras != null) {
					final Bitmap photo = extras.getParcelable("data");
					avatarView.setImageBitmap(photo);
					ImageUtils.saveBitmapToFile(photo, Constant.TEMP_AVATAR);
					BmobUtils.updateUserAvatar(getActivity(), new File(
							Constant.TEMP_AVATAR), new UpdateListener() {
						@Override
						public void onSuccess() {
							System.out
									.println("update avatar to server success");
							MyUser myUser = BmobUser.getCurrentUser(
									getActivity(), MyUser.class);
							DatabaseUtils.saveAvatarToLocal(getActivity(),
									Constant.AVATAR_DIR, myUser.getAvatarUrl(),
									photo);
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							System.out.println("update avatar to server fail");
						}
					});

				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (avatarBitmap != null) {
			avatarBitmap.recycle();
		}
	}

}
