package com.example.myandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BMICircleView extends View {

	private static final float DEFAULT_BORDER_WIDTH = 15.0f;
	private static final int DEFAULT_BORDER_COLOR = Color.GREEN;

	private final RectF mBorderRect = new RectF();

	private final Paint mBorderPaint = new Paint();
	private final Paint mBorderPaintShadow = new Paint();

	private int mBorderColor = DEFAULT_BORDER_COLOR;
	private float mBorderRadius;
	private float mBorderWidth = DEFAULT_BORDER_WIDTH;

	private float borderScale = 0.2f;

	public BMICircleView(Context context) {
		this(context, null);
	}

	public BMICircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BMICircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius,
				mBorderPaint);
		canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius,
				mBorderPaintShadow);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		setup();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setup();
	}

	private void setup() {
		int width = getWidth();
		int height = getHeight();

		Log.e("hx", "width = " + width + " height = " + height);
		mBorderRect.set(0, 0, getWidth(), getHeight());

		float minWidth = Math.min(mBorderRect.height() / 2.0f,
				mBorderRect.width() / 2.0f);

		mBorderWidth = (int) (minWidth * borderScale);

		mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f,
				(mBorderRect.width() - mBorderWidth) / 2.0f);

		Log.e("hx", "mBorderWidth = " + mBorderWidth + " mBorderRadius = "
				+ mBorderRadius);

		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);

		mBorderPaintShadow.setStyle(Paint.Style.STROKE);
		mBorderPaintShadow.setAntiAlias(true);
		mBorderPaintShadow.setStrokeWidth(mBorderWidth);

		RadialGradient mGradient = new RadialGradient(getWidth() / 2.0f,
				getHeight() / 2.0f,
				Math.abs(mBorderWidth) > 0 ? Math.abs(mBorderWidth)
						: DEFAULT_BORDER_WIDTH, new int[] {
						Color.argb(102, 130, 130, 130),
						Color.argb(0, 130, 130, 130),
						Color.argb(0, 130, 130, 130),
						Color.argb(102, 130, 130, 130) }, null,
				Shader.TileMode.REPEAT);
		mBorderPaintShadow.setShader(mGradient);
		invalidate();
	}

}
