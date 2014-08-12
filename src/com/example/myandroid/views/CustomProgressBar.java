package com.example.myandroid.views;

import com.example.myandroid.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class CustomProgressBar extends View {

	private final int BACKGROUND_COLOR_DEFAULT = Color.BLACK;
	private final int PROGRESS_TOP_COLOR_DEFAULT = Color.BLUE;
	private final int PROGRESS_BOTTOM_COLOR_DEFAULT = Color.RED;
	private final int HORIZONTAL_MARGIN_DEFAULT = 2;
	private final int VERTICAL_MARGIN_DEFAULT = 2;
	private int mWidth, mHeight;

	private Paint mBackgroundPaint;
	private Paint mProgressPaint;

	private int progressBackgroundColor;
	private int progressTopColor;
	private int progressBottomColor;

	private int horizontalMargin;
	private int verticalMargin;

	private RectF rectBackground;
	private RectF rectProgress;

	private LinearGradient gradient;

	private float mProgress = 0.5f;

	public CustomProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CustomProgressBar, defStyleAttr, 0);
		progressBackgroundColor = a.getColor(
				R.styleable.CustomProgressBar_progress_background_color,
				BACKGROUND_COLOR_DEFAULT);
		progressTopColor = a.getColor(
				R.styleable.CustomProgressBar_progress_top_color,
				PROGRESS_TOP_COLOR_DEFAULT);
		progressBottomColor = a.getColor(
				R.styleable.CustomProgressBar_progress_bottom_color,
				PROGRESS_BOTTOM_COLOR_DEFAULT);
		horizontalMargin = a.getDimensionPixelSize(
				R.styleable.CustomProgressBar_horizontal_margin,
				HORIZONTAL_MARGIN_DEFAULT);
		verticalMargin = a.getDimensionPixelSize(
				R.styleable.CustomProgressBar_vertical_margin,
				VERTICAL_MARGIN_DEFAULT);
		a.recycle();
		init(context);
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context);
	}

	public CustomProgressBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		rectBackground = new RectF();
		rectProgress = new RectF();

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setAntiAlias(true);
		mBackgroundPaint.setColor(progressBackgroundColor);

		mProgressPaint = new Paint();
		mProgressPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		gradient = new LinearGradient(horizontalMargin, verticalMargin,
				horizontalMargin, mHeight - verticalMargin * 2,
				progressTopColor, progressBottomColor, Shader.TileMode.CLAMP);
		mProgressPaint.setShader(gradient);
		int round = mHeight - verticalMargin * 2;
		rectBackground.set(0, 0, mWidth, mHeight);
		canvas.drawRect(rectBackground, mBackgroundPaint);
		rectProgress.set(horizontalMargin - (mHeight - horizontalMargin),
				verticalMargin, (mWidth - horizontalMargin) * mProgress,
				mHeight - verticalMargin);
		canvas.drawRoundRect(rectProgress, round, round, mProgressPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpecMode == MeasureSpec.EXACTLY
				|| widthSpecMode == MeasureSpec.AT_MOST) {
			mWidth = widthSpecSize;
		} else {
			mWidth = 0;
		}
		if (heightSpecMode == MeasureSpec.AT_MOST
				|| heightSpecMode == MeasureSpec.UNSPECIFIED) {
			mHeight = dipToPx(15);
		} else {
			mHeight = heightSpecSize;
		}
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

	}

	public void setProgress(float progress) {
		if (progress > 1.0f) {
			mProgress = 1.0f;
		} else if (progress < 0.0f) {
			mProgress = 0.0f;
		} else {
			mProgress = progress;
		}
		invalidate();
	}

	private int dipToPx(int dip) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}
}
