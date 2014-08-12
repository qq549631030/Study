package com.example.myandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AnimationView extends View {

	private int mState = -1;
	private Paint paint1;
	private Paint paint2;
	private int count = 22;

	public AnimationView(Context context) {
		super(context);
		init();
	}

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {

		/**
		 * 创建Paint
		 */
		paint1 = new Paint();
		paint2 = new Paint();
		/**
		 * 设置抗锯齿效果
		 */
		paint1.setAntiAlias(true);
		paint2.setAntiAlias(true);
		/**
		 * 防抖动
		 */
		paint1.setDither(true);
		paint2.setDither(true);
		/**
		 * 设置画刷的颜色
		 */
		paint1.setColor(Color.BLUE);
		paint2.setColor(Color.BLUE);

		/**
		 * 设置线的粗细
		 */
		paint1.setStrokeWidth(5.0f);
		paint2.setStrokeWidth(5.0f);
		/**
		 * 设置画法
		 */
		paint1.setStyle(Paint.Style.FILL_AND_STROKE);
		paint2.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.e(VIEW_LOG_TAG, "onDraw");
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		int shortSide = Math.min(width, height);
		if (mState == 0) {
			canvas.drawCircle(width / 2, height / 2, shortSide / 8, paint1);
		} else if (mState == 1) {
			canvas.drawCircle(width / 2, height / 2, shortSide / 8, paint1);
			canvas.drawCircle(width / 2, height / 2, shortSide / 4, paint2);
		} else if (mState == 2) {
			canvas.drawCircle(width / 2, height / 2, shortSide / 8, paint1);
			canvas.drawCircle(width / 2, height / 2, shortSide / 4, paint2);
			canvas.drawCircle(width / 2, height / 2, shortSide * 3 / 8, paint2);
		}
		if (count < 22) {
			mState = count % 3;
			count++;
			postInvalidateDelayed(250);
		}
	}

	public void flashView() {
		if (count >= 22) {
			count = 0;
			invalidate();
		}
	}
}
