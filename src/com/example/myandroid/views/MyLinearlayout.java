package com.example.myandroid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class MyLinearlayout extends LinearLayout {

	private Scroller mScroller;

	public MyLinearlayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
	}

	public MyLinearlayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	public MyLinearlayout(Context context) {
		super(context);
		mScroller = new Scroller(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (!mScroller.isFinished()) {
			return super.onTouchEvent(ev);
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
	}

}
