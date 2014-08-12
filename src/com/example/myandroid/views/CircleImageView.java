package com.example.myandroid.views;

import com.example.myandroid.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 1;

	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;

	private final RectF mDrawableRect = new RectF();
	private final RectF mBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	private final Paint mBitmapPaint = new Paint();
	private final Paint mBorderInPaint = new Paint();
	private final Paint mBorderOutPaint = new Paint();

	private int mBorderInColor = DEFAULT_BORDER_COLOR;
	private int mBorderInWidth = DEFAULT_BORDER_WIDTH;

	private int mBorderOutColor = DEFAULT_BORDER_COLOR;
	private int mBorderOutWidth = DEFAULT_BORDER_WIDTH;

	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private int mBitmapWidth;
	private int mBitmapHeight;

	private float mDrawableRadius;
	private float mBorderInRadius;
	private float mBorderOutRadius;

	private boolean mReady;
	private boolean mSetupPending;

	public CircleImageView(Context context) {
		super(context);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setScaleType(SCALE_TYPE);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleImageView, defStyle, 0);

		mBorderInWidth = a.getDimensionPixelSize(
				R.styleable.CircleImageView_border_in_width,
				DEFAULT_BORDER_WIDTH);
		mBorderInColor = a.getColor(
				R.styleable.CircleImageView_border_in_color,
				DEFAULT_BORDER_COLOR);
		mBorderOutWidth = a.getDimensionPixelSize(
				R.styleable.CircleImageView_border_out_width,
				DEFAULT_BORDER_WIDTH);
		mBorderOutColor = a.getColor(
				R.styleable.CircleImageView_border_out_color,
				DEFAULT_BORDER_COLOR);

		a.recycle();

		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format(
					"ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}

		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
				mBitmapPaint);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderInRadius,
				mBorderInPaint);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderOutRadius,
				mBorderOutPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	public int getBorderInColor() {
		return mBorderInColor;
	}

	public void setBorderInColor(int borderColor) {
		if (borderColor == mBorderInColor) {
			return;
		}

		mBorderInColor = borderColor;
		mBorderInPaint.setColor(mBorderInColor);
		invalidate();
	}

	public int getBorderOutColor() {
		return mBorderOutColor;
	}

	public void setBorderOutColor(int borderColor) {
		if (borderColor == mBorderOutColor) {
			return;
		}

		mBorderOutColor = borderColor;
		mBorderOutPaint.setColor(mBorderOutColor);
		invalidate();
	}

	public int getBorderInWidth() {
		return mBorderInWidth;
	}

	public void setBorderInWidth(int borderWidth) {
		if (borderWidth == mBorderInWidth) {
			return;
		}

		mBorderInWidth = borderWidth;
		setup();
	}

	public int getBorderOutWidth() {
		return mBorderOutWidth;
	}

	public void setBorderOutWidth(int borderWidth) {
		if (borderWidth == mBorderOutWidth) {
			return;
		}

		mBorderOutWidth = borderWidth;
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		mBorderInPaint.setStyle(Paint.Style.STROKE);
		mBorderInPaint.setAntiAlias(true);
		mBorderInPaint.setColor(mBorderInColor);
		mBorderInPaint.setStrokeWidth(mBorderInWidth);

		mBorderOutPaint.setStyle(Paint.Style.STROKE);
		mBorderOutPaint.setAntiAlias(true);
		mBorderOutPaint.setColor(mBorderOutColor);
		mBorderOutPaint.setStrokeWidth(mBorderOutWidth);

		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		mBorderRect.set(0, 0, getWidth(), getHeight());
		mBorderOutRadius = Math.min(
				(mBorderRect.height() - mBorderInWidth) / 2,
				(mBorderRect.width() - mBorderInWidth) / 2)
				- mBorderOutWidth / 2;
		mBorderInRadius = mBorderOutRadius - (mBorderInWidth + mBorderOutWidth)
				/ 2;
		mDrawableRect.set(mBorderInWidth + mBorderOutWidth, mBorderInWidth
				+ mBorderOutWidth, mBorderRect.width() - mBorderInWidth
				- mBorderOutWidth, mBorderRect.height() - mBorderInWidth
				- mBorderOutWidth);
		mDrawableRadius = Math.min(mDrawableRect.height() / 2,
				mDrawableRect.width() / 2);
		// LinearGradient mGradient = new LinearGradient(getWidth() / 2,
		// 0,
		// getWidth() / 2, getHeight(), Color.WHITE, Color.GRAY,
		// Shader.TileMode.REPEAT);

		RadialGradient mGradient = new RadialGradient(getWidth() / 2,
				getHeight() / 2, mBorderOutWidth, Color.WHITE, Color.GREEN,
				Shader.TileMode.REPEAT);
		mBorderOutPaint.setShader(mGradient);
		updateShaderMatrix();
		invalidate();
	}

	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix.set(null);

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
				* mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}

		mShaderMatrix.setScale(scale, scale);
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderInWidth
				+ mBorderOutWidth, (int) (dy + 0.5f) + mBorderInWidth
				+ mBorderOutWidth);

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

}