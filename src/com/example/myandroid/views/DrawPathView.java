package com.example.myandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class DrawPathView extends View implements
		ViewTreeObserver.OnPreDrawListener {

	private Paint mLineBackgroundPaint = new Paint();

	private Paint mPaint = new Paint();

	private Paint mLinePaint = new Paint();

	private Paint mCirclePaint = new Paint();

	private Path path = new Path();

	private float mWidth, mHeight;

	public DrawPathView(Context context) {
		this(context, null);
	}

	public DrawPathView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DrawPathView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {

		mLineBackgroundPaint.setAntiAlias(true);
		mLineBackgroundPaint.setStyle(Paint.Style.STROKE);
		mLineBackgroundPaint.setStrokeWidth(2.0f);
		mLineBackgroundPaint.setColor(Color.argb(255, 0, 0, 255));

		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		LinearGradient gradient = new LinearGradient(0, 0, 0, mHeight,
				Color.argb(77, 254, 203, 191), Color.argb(5, 254, 203, 191),
				Shader.TileMode.REPEAT);
		mPaint.setShader(gradient);

		mLinePaint.setAntiAlias(true);
		mLinePaint.setColor(Color.argb(255, 244, 104, 73));
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setStrokeWidth(2.0f);

		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(Color.argb(255, 254, 203, 191));
		mCirclePaint.setStyle(Paint.Style.FILL);

		getViewTreeObserver().addOnPreDrawListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		for (int i = 0; i < 6; i++) {
			canvas.drawLine(0, mHeight * i / 6.0f, mWidth, mHeight * i / 6.0f,
					mLineBackgroundPaint);
		}
		path.moveTo(10, 400);
		path.lineTo(100, 200);
		path.lineTo(200, 300);
		path.lineTo(300, 200);
		path.lineTo(400, 300);
		path.lineTo(500, 200);
		path.lineTo(600, 400);
		canvas.drawPath(path, mPaint);

		canvas.drawLine(10, 400, 100, 200, mLinePaint);
		canvas.drawLine(100, 200, 200, 300, mLinePaint);
		canvas.drawLine(200, 300, 300, 200, mLinePaint);
		canvas.drawLine(300, 200, 400, 300, mLinePaint);
		canvas.drawLine(400, 300, 500, 200, mLinePaint);
		canvas.drawLine(500, 200, 600, 400, mLinePaint);

		canvas.drawCircle(100, 200, 5.0f, mCirclePaint);
		canvas.drawCircle(200, 300, 5.0f, mCirclePaint);
		canvas.drawCircle(300, 200, 5.0f, mCirclePaint);
		canvas.drawCircle(400, 300, 5.0f, mCirclePaint);
		canvas.drawCircle(500, 200, 5.0f, mCirclePaint);

		canvas.drawCircle(100, 200, 5.0f, mLinePaint);
		canvas.drawCircle(200, 300, 5.0f, mLinePaint);
		canvas.drawCircle(300, 200, 5.0f, mLinePaint);
		canvas.drawCircle(400, 300, 5.0f, mLinePaint);
		canvas.drawCircle(500, 200, 5.0f, mLinePaint);

		canvas.drawText("mWidth = " + mWidth, 200, 300, mLinePaint);
		canvas.drawText("mHeight = " + mHeight, 200, 400, mLinePaint);
	}

	@Override
	public boolean onPreDraw() {
		mWidth = getWidth();
		mHeight = getHeight();
		return true;
	}

}
