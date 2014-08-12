package com.example.myandroid;

import java.lang.reflect.Field;

import com.android.volley.RetryPolicy;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class MainActionBarActivity extends Activity implements OnClickListener {

	private final String TAG = MainActionBarActivity.class.getSimpleName();

	private static Context mContext;

	private PopupWindow popupWindow;
	private View mPopupWindowView;
	private ActionBar mActionBar;
	private int statusBarHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		mContext = this;
		mActionBar = getActionBar();
		// getOverflowMenu();
		initPopupWindow();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initPopupWindow() {
		initPopupWindowView();
		// 初始化popupwindow，绑定显示view，设置该view的宽度/高度
		popupWindow = new PopupWindow(mPopupWindowView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 不要设焦点，否则activity接收不到menu key
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景；使用该方法点击窗体之外，才可关闭窗体
		// popupWindow.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.bitmap_book_read_chapterlist_repeat));
		// Background不能设置为null，dismiss会失效
		// popupWindow.setBackgroundDrawable(null);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置渐入、渐出动画效果
		// popupWindow.setAnimationStyle(R.style.popupwindow);
		popupWindow.update();
		// popupWindow调用dismiss时触发，设置了setOutsideTouchable(true)，点击view之外/按键back的地方也会触发
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// showToast("关闭popupwindow");
			}
		});
	}

	private void initPopupWindowView() {
		mPopupWindowView = LayoutInflater.from(mContext).inflate(
				R.layout.popup_menu_layout, null);
		TextView menuItem1 = (TextView) mPopupWindowView
				.findViewById(R.id.popup_menu_item1);
		menuItem1.setOnClickListener(this);
		TextView menuItem2 = (TextView) mPopupWindowView
				.findViewById(R.id.popup_menu_item2);
		menuItem2.setOnClickListener(this);
		TextView menuItem3 = (TextView) mPopupWindowView
				.findViewById(R.id.popup_menu_item3);
		menuItem3.setOnClickListener(this);
	}

	/** 显示popupwindow */
	private void showPopupWindow() {
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		Log.e("hx", "status bar height = " + statusBarHeight);
		if (!popupWindow.isShowing()) {
			popupWindow.showAtLocation(getWindow().getDecorView(),
					Gravity.RIGHT | Gravity.TOP, 5, statusBarHeight
							+ mActionBar.getHeight());
		} else {
			popupWindow.dismiss();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_more:
			showPopupWindow();
			break;

		default:
			break;
		}
		return true;
	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Log.e(TAG, "KEYCODE_MENU down");
			showPopupWindow();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
