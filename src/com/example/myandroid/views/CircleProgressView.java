package com.example.myandroid.views;

import com.example.myandroid.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressView extends View {

	private final int BACKGROUND_STROKE_WIDTH_DEFAULT = 10;
	private final int BACKGROUND_COLOR_DEFAULT = Color.BLUE;
	private final int PROGRESS_STROKE_WIDTH_DEFAULT = 20;
	private final int PROGRESS_COLOR_DEFAULT = Color.RED;
	private final int INDICATOR_STROKE_WIDTH_DEFAULT = 2;
	private final int INDICATOR_COLOR_DEFAULT = Color.GREEN;
	private final int INDICATOR_RADIUS_DEFAULT = 15;
	private final int INDICATOR_STROKE_WIDTH1_DEFAULT = INDICATOR_STROKE_WIDTH_DEFAULT;
	private final int INDICATOR_COLOR1_DEFAULT = INDICATOR_COLOR_DEFAULT;
	private final int INDICATOR_STROKE_WIDTH2_DEFAULT = INDICATOR_STROKE_WIDTH_DEFAULT;
	private final int INDICATOR_COLOR2_DEFAULT = INDICATOR_COLOR_DEFAULT;
	private final int INDICATOR_GAP_DEFAULT = 5;
	private final int TITLE_TEXT_SIZE_DEFAULT = 80;
	private final int TITLE_TEXT_COLOR_DEFAULT = Color.RED;
	private final int SUB_TEXT_SIZE_DEFAULT = 60;
	private final int SUB_TEXT_COLOR_DEFAULT = Color.BLACK;

	private final long FLASH_DELAY = 200;

	private float mProgress;

	private String mTitleText = "title";
	private String mSubText = "title";

	private boolean flashing;
	private long flashTimeLength;
	private int mState = 0;

	private int radius;

	private PointF mCenter = new PointF();
	private PointF mIndicator = new PointF();
	private int backgroundStrokeWidth = BACKGROUND_STROKE_WIDTH_DEFAULT;
	private int backgroundColor = BACKGROUND_COLOR_DEFAULT;
	private int progressStrokeWidth = PROGRESS_STROKE_WIDTH_DEFAULT;
	private int progressColor = PROGRESS_COLOR_DEFAULT;
	private int indicatorStrokeWidth = INDICATOR_STROKE_WIDTH_DEFAULT;
	private int indicatorColor = INDICATOR_COLOR_DEFAULT;
	private int indicatorRadius = INDICATOR_RADIUS_DEFAULT;
	private int indicatorStrokeWidth1 = INDICATOR_STROKE_WIDTH_DEFAULT;
	private int indicatorColor1 = INDICATOR_COLOR1_DEFAULT;
	private int indicatorGap1 = INDICATOR_GAP_DEFAULT;
	private int indicatorStrokeWidth2 = INDICATOR_STROKE_WIDTH_DEFAULT;
	private int indicatorColor2 = INDICATOR_COLOR2_DEFAULT;
	private int indicatorGap2 = INDICATOR_GAP_DEFAULT;
	private int titleTextSize = TITLE_TEXT_SIZE_DEFAULT;
	private int titleTextColor = TITLE_TEXT_COLOR_DEFAULT;
	private int subTextSize = SUB_TEXT_SIZE_DEFAULT;
	private int subTextColor = SUB_TEXT_COLOR_DEFAULT;

	private RectF mProgressRectF;
	private RectF mTextRect;
	private RectF mSubTextRect;

	private Paint backgroundPaint;
	private Paint progressPaint;
	private Paint indicatorPaint;
	private Paint indicatorPaint1;
	private Paint indicatorPaint2;
	private Paint textPaint;
	private Paint subTextPaint;

	public CircleProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleProgressView, defStyleAttr, 0);
		backgroundStrokeWidth = a.getDimensionPixelSize(
				R.styleable.CircleProgressView_background_stroke_width,
				BACKGROUND_STROKE_WIDTH_DEFAULT);
		backgroundColor = a.getColor(
				R.styleable.CircleProgressView_background_color,
				BACKGROUND_COLOR_DEFAULT);
		progressStrokeWidth = a.getDimensionPixelSize(
				R.styleable.CircleProgressView_progress_stroke_width,
				PROGRESS_STROKE_WIDTH_DEFAULT);
		progressColor = a.getColor(
				R.styleable.CircleProgressView_progress_color,
				PROGRESS_COLOR_DEFAULT);
		indicatorStrokeWidth = a.getDimensionPixelSize(
				R.styleable.CircleProgressView_indicator_stroke_width,
				INDICATOR_STROKE_WIDTH_DEFAULT);
		indicatorColor = a.getColor(
				R.styleable.CircleProgressView_indicator_color,
				INDICATOR_COLOR_DEFAULT);
		indicatorRadius = a.getColor(
				R.styleable.CircleProgressView_indicator_radius,
				INDICATOR_RADIUS_DEFAULT);
		indicatorStrokeWidth1 = a.getDimensionPixelSize(
				R.styleable.CircleProgressView_indicator_stroke_width1,
				INDICATOR_STROKE_WIDTH1_DEFAULT);
		indicatorColor1 = a.getColor(
				R.styleable.CircleProgressView_indicator_color1,
				INDICATOR_COLOR1_DEFAULT);
		indicatorStrokeWidth2 = a.getDimensionPixelSize(
				R.styleable.CircleProgressView_indicator_stroke_width2,
				INDICATOR_STROKE_WIDTH2_DEFAULT);
		indicatorColor2 = a.getColor(
				R.styleable.CircleProgressView_indicator_color2,
				INDICATOR_COLOR2_DEFAULT);
		titleTextSize = a.getDimensionPixelSize(
				R.styleable.CircleProgressView_title_text_size,
				TITLE_TEXT_SIZE_DEFAULT);
		titleTextColor = a.getColor(
				R.styleable.CircleProgressView_title_text_color,
				TITLE_TEXT_COLOR_DEFAULT);
		subTextSize = a.getDimensionPixelSize(
				R.styleable.CircleProgressView_sub_text_size,
				SUB_TEXT_SIZE_DEFAULT);
		subTextColor = a.getColor(
				R.styleable.CircleProgressView_sub_text_color,
				SUB_TEXT_COLOR_DEFAULT);
		a.recycle();
		init();
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleProgressView(Context context) {
		this(context, null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		computeValues();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		computeValues();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		computeValues();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.e(VIEW_LOG_TAG, "onDraw mState = " + mState);
		canvas.drawCircle(mCenter.x, mCenter.y, radius, backgroundPaint);
		canvas.drawArc(mProgressRectF, 270, mProgress * 360, false,
				progressPaint);
		switch (mState) {
		case 2:
			canvas.drawCircle(mIndicator.x, mIndicator.y, indicatorRadius
					+ indicatorGap1 + indicatorGap2, indicatorPaint2);
		case 1:
			canvas.drawCircle(mIndicator.x, mIndicator.y, indicatorRadius
					+ indicatorGap1, indicatorPaint1);
		case 0:
			canvas.drawCircle(mIndicator.x, mIndicator.y, indicatorRadius,
					indicatorPaint);
			break;

		default:
			canvas.drawCircle(mIndicator.x, mIndicator.y, indicatorRadius,
					indicatorPaint);
			break;
		}
		canvas.drawText(mTitleText, mTextRect.left, mTextRect.bottom, textPaint);
		canvas.drawText(mSubText, mSubTextRect.left, mSubTextRect.bottom,
				subTextPaint);
		canvas.drawPoint(mCenter.x, mCenter.y, backgroundPaint);
		if (flashing) {
			if (flashTimeLength > 0) {
				flashTimeLength -= FLASH_DELAY;
				mState++;
				mState = mState % 3;
				postInvalidateDelayed(FLASH_DELAY);
			} else {
				flashing = false;
				mState = 0;
				invalidate();
			}
		}
	}

	private void init() {
		backgroundPaint = new Paint();
		backgroundPaint.setAntiAlias(true);
		backgroundPaint.setDither(true);
		backgroundPaint.setColor(backgroundColor);
		backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
		backgroundPaint.setStyle(Paint.Style.STROKE);
		backgroundPaint.setStrokeJoin(Paint.Join.ROUND);
		backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		progressPaint.setDither(true);
		progressPaint.setColor(progressColor);
		progressPaint.setStrokeWidth(progressStrokeWidth);
		progressPaint.setStyle(Paint.Style.STROKE);
		progressPaint.setStrokeJoin(Paint.Join.ROUND);
		progressPaint.setStrokeCap(Paint.Cap.ROUND);

		indicatorPaint = new Paint();
		indicatorPaint.setAntiAlias(true);
		indicatorPaint.setDither(true);
		indicatorPaint.setColor(indicatorColor);
		indicatorPaint.setStrokeWidth(indicatorStrokeWidth);
		indicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		indicatorPaint.setStrokeJoin(Paint.Join.ROUND);
		indicatorPaint.setStrokeCap(Paint.Cap.ROUND);

		indicatorPaint1 = new Paint();
		indicatorPaint1.setAntiAlias(true);
		indicatorPaint1.setDither(true);
		indicatorPaint1.setColor(indicatorColor1);
		indicatorPaint1.setStrokeWidth(indicatorStrokeWidth1);
		indicatorPaint1.setStyle(Paint.Style.STROKE);
		indicatorPaint1.setStrokeJoin(Paint.Join.ROUND);
		indicatorPaint1.setStrokeCap(Paint.Cap.ROUND);

		indicatorPaint2 = new Paint();
		indicatorPaint2.setAntiAlias(true);
		indicatorPaint2.setDither(true);
		indicatorPaint2.setColor(indicatorColor2);
		indicatorPaint2.setStrokeWidth(indicatorStrokeWidth2);
		indicatorPaint2.setStyle(Paint.Style.STROKE);
		indicatorPaint2.setStrokeJoin(Paint.Join.ROUND);
		indicatorPaint2.setStrokeCap(Paint.Cap.ROUND);

		textPaint = new Paint();
		textPaint.setTextSize(titleTextSize);
		textPaint.setColor(titleTextColor);

		subTextPaint = new Paint();
		subTextPaint.setTextSize(subTextSize);
		subTextPaint.setColor(subTextColor);
	}

	private void computeValues() {
		int width = getWidth();
		int height = getHeight();
		int minSide = Math.min(width, height);
		int maxGap = Math.max(progressStrokeWidth / 2, indicatorRadius
				+ indicatorGap1 + indicatorGap2 + indicatorStrokeWidth2);
		mCenter.x = mCenter.y = minSide / 2;
		radius = minSide / 2 - maxGap;
		mProgressRectF = new RectF(maxGap, maxGap, minSide - maxGap, minSide
				- maxGap);
		mIndicator.x = (float) (mCenter.x + Math.sin(mProgress * Math.PI * 2)
				* radius);
		mIndicator.y = (float) (mCenter.y - Math.cos(mProgress * Math.PI * 2)
				* radius);
		Rect textRect = new Rect();
		textPaint.getTextBounds(mTitleText, 0, mTitleText.length(), textRect);
		mTextRect = new RectF(mCenter.x - textRect.width() / 2, mCenter.y
				- textRect.height(), mCenter.x + textRect.width() / 2,
				mCenter.y);
		Rect subTextRect = new Rect();
		subTextPaint.getTextBounds(mSubText, 0, mSubText.length(), subTextRect);
		mSubTextRect = new RectF(mCenter.x - subTextRect.width() / 2,
				mCenter.y, mCenter.x + subTextRect.width() / 2, mCenter.y
						+ subTextRect.height());

	}

	public void setProgress(float progress) {
		if (progress > 1.0f) {
			mProgress = 1.0f;
		} else if (progress < 0.0f) {
			mProgress = 0.0f;
		} else {
			mProgress = progress;
		}
		computeValues();
		invalidate();
	}

	public void setTitleText(String text) {
		mTitleText = text;
		computeValues();
		invalidate();
	}

	public void setSubText(String subText) {
		mSubText = subText;
		computeValues();
		invalidate();
	}

	public void flashView(long timeLength) {
		flashTimeLength = timeLength;
		flashing = true;
		invalidate();
	}

}
