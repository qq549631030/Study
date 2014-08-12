package com.example.myandroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class BounceListViewTestActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		setContentView(linearLayout);

		BounceListView bounceListView = new BounceListView(this);

		String[] data = new String[30];
		for (int i = 0; i < data.length; i++) {
			data[i] = "天天记录 " + i;
		}

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data);

		bounceListView.setAdapter(arrayAdapter);

		linearLayout.addView(bounceListView);
	}

	public class BounceListView extends ListView {
		private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

		private Context mContext;
		private int mMaxYOverscrollDistance;

		public BounceListView(Context context) {
			super(context);
			mContext = context;
			initBounceListView();
		}

		public BounceListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			mContext = context;
			initBounceListView();
		}

		public BounceListView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			mContext = context;
			initBounceListView();
		}

		private void initBounceListView() {
			// get the density of the screen and do some maths with it on the
			// max overscroll distance
			// variable so that you get similar behaviors no matter what the
			// screen size

			final DisplayMetrics metrics = mContext.getResources()
					.getDisplayMetrics();
			final float density = metrics.density;
			mMaxYOverscrollDistance = metrics.heightPixels;
			//mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
				int scrollY, int scrollRangeX, int scrollRangeY,
				int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
			// This is where the magic happens, we have replaced the incoming
			// maxOverScrollY with our own custom variable
			// mMaxYOverscrollDistance;
			return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
					scrollRangeX, scrollRangeY, maxOverScrollX,
					mMaxYOverscrollDistance, isTouchEvent);
		}

	}
}
