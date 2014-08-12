package com.example.myandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleAlphaView extends View {

	public CircleAlphaView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CircleAlphaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CircleAlphaView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		/**
		 * 创建Paint
		 */
		Paint paint = new Paint();
		/**
		 * 设置抗锯齿效果
		 */
		paint.setAntiAlias(true);
		/**
		 * 防抖动
		 */
		paint.setDither(true);
		/**
		 * 设置画刷的颜色
		 */
		paint.setColor(Color.BLUE);
		/**
		 * 设置线的粗细
		 */
		paint.setStrokeWidth(5.0f);
		/**
		 * 设置画法
		 */
		paint.setStyle(Paint.Style.STROKE);

		paint.setStrokeJoin(Paint.Join.ROUND);

		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setColor(0x66000000);
		paint.setStyle(Paint.Style.FILL);
		Path path3 = new Path();
		path3.moveTo(0, 0);
		path3.lineTo(0, height);
		path3.lineTo(width / 2, height);
		path3.lineTo(width / 2, height * 2 / 3);
		path3.addArc(new RectF(width / 2 - height / 6, height / 3, width / 2
				+ height / 6, height * 2 / 3), 90, 180);
		path3.lineTo(width / 2, 0);
		path3.lineTo(0, 0);
		canvas.drawPath(path3, paint);

		Path path4 = new Path();
		path4.moveTo(width / 2, 0);
		path4.lineTo(width / 2, height / 3);
		path4.addArc(new RectF(width / 2 - height / 6, height / 3, width / 2
				+ height / 6, height * 2 / 3), 270, 180);
		path4.lineTo(width / 2, height);
		path4.lineTo(width, height);
		path4.lineTo(width, 0);
		path4.lineTo(width / 2, 0);
		canvas.drawPath(path4, paint);
		paint.setTextSize(40);
		canvas.drawText("这里没东西", width / 3 + 20, height / 2 - 20, paint);
	}

}
