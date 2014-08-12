package com.example.myandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Path.FillType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 贝塞尔曲线
 * 
 * @author hx2lu
 * 
 */
public class BezierView extends View {

	private int state = 0;

	private PointF mTouchPoint = new PointF();

	public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public BezierView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BezierView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

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

		Path path1 = new Path();
		Point[] mPoints = new Point[8];
		mPoints[0] = new Point(0, 200);
		mPoints[1] = new Point(100, 100);
		mPoints[2] = new Point(200, 200);
		mPoints[3] = new Point(300, 100);
		mPoints[4] = new Point(400, 200);
		mPoints[5] = new Point(500, 100);
		mPoints[6] = new Point(600, 200);
		mPoints[7] = new Point(700, 100);
		/**
		 * 三次方贝塞尔曲线（两个控制点）
		 */
		for (int i = 0; i < mPoints.length - 1; i++) {
			path1.moveTo(mPoints[i].x, mPoints[i].y);
			/**
			 * 两点x轴距离的一半
			 */
			float d_value = Math.abs((mPoints[i + 1].x - mPoints[i].x) / 2);
			/**
			 * 两个控制点x坐标均为两个端点x坐标平均值，前一个控制点y坐标为起点y坐标， 后一个控制点y坐标为终点y坐标。
			 */
			path1.cubicTo(mPoints[i].x + d_value, mPoints[i].y, mPoints[i].x
					+ d_value, mPoints[i + 1].y, mPoints[i + 1].x,
					mPoints[i + 1].y);
		}

		canvas.drawPath(path1, paint);

		Path path3 = new Path();
		mPoints = new Point[9];
		mPoints[0] = new Point(0, 200);
		mPoints[1] = new Point(100, 300);
		mPoints[2] = new Point(200, 250);
		mPoints[3] = new Point(300, 300);
		mPoints[4] = new Point(400, 250);
		mPoints[5] = new Point(500, 300);
		mPoints[6] = new Point(600, 250);
		mPoints[7] = new Point(700, 300);
		mPoints[8] = new Point(getWidth(), 200);
		/**
		 * 三次方贝塞尔曲线（两个控制点）
		 */
		for (int i = 0; i < mPoints.length - 1; i++) {
			path3.moveTo(mPoints[i].x, mPoints[i].y);
			/**
			 * 两点x轴距离的一半
			 */
			float d_value = Math.abs((mPoints[i + 1].x - mPoints[i].x) / 2);
			/**
			 * 两个控制点x坐标均为两个端点x坐标平均值，前一个控制点y坐标为起点y坐标， 后一个控制点y坐标为终点y坐标。
			 */
			path3.cubicTo(mPoints[i].x + d_value, mPoints[i].y, mPoints[i].x
					+ d_value, mPoints[i + 1].y, mPoints[i + 1].x,
					mPoints[i + 1].y);
		}

		path3.moveTo(mPoints[8].x, mPoints[8].y);
		path3.lineTo(mPoints[0].x, mPoints[0].y);
		path3.setFillType(FillType.WINDING);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawPath(path3, paint);

		paint.setStyle(Paint.Style.STROKE);
		Path path2 = new Path();
		mPoints[0] = new Point(0, 400);
		mPoints[1] = new Point(100, 300);
		mPoints[2] = new Point(200, 400);
		mPoints[3] = new Point(300, 300);
		mPoints[4] = new Point(400, 400);
		mPoints[5] = new Point(500, 300);
		mPoints[6] = new Point(600, 400);
		mPoints[7] = new Point(700, 300);
		/**
		 * 二次方贝塞尔曲线（一个控制点）
		 */
		for (int i = 0; i < mPoints.length - 1; i++) {
			path2.moveTo(mPoints[i].x, mPoints[i].y);
			/**
			 * 控制点x坐标为起点x坐标，y坐标为终点的y坐标。
			 */
			// path.quadTo(mPoints[i].x, mPoints[i + 1].y, mPoints[i + 1].x,
			// mPoints[i + 1].y);

			/**
			 * 控制点为屏幕点击位置
			 */
			path2.quadTo(mTouchPoint.x, mTouchPoint.y, mPoints[i + 1].x,
					mPoints[i + 1].y);
		}
		canvas.drawPath(path2, paint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mTouchPoint.x = event.getX();
		mTouchPoint.y = event.getY();
		invalidate();
		return true;
	}

	public void flashView() {
		int count = 0;
		while (count < 7) {
			state++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			invalidate();
		}
	}

//	/**
//	 * 描述：TODO
//	 * 
//	 * @see com.ab.view.chart.AbstractChart#drawPath(android.graphics.Canvas,
//	 *      float[], android.graphics.Paint, boolean)
//	 */
//	@Override
//	protected void drawPath(Canvas canvas, PointF[] points, Paint paint,
//			boolean circular) {
//		Path p = new Path();
//		p.moveTo(points[0].x, points[0].y);
//
//		int length = points.length;
//		if (circular) {
//			length -= 2;
//		}
//
//		for (int i = 0; i < length; i += 1) {
//			int nextIndex = i + 2 < length ? i + 2 : i;
//			int nextNextIndex = i + 4 < length ? i + 4 : nextIndex;
//			calc(points, p1, i, nextIndex, secondMultiplier);
//			p2.setX(points[nextIndex]);
//			p2.setY(points[nextIndex + 1]);
//			calc(points, p3, nextIndex, nextNextIndex, firstMultiplier);
//			// From last point, approaching x1/y1 and x2/y2 and ends up at x3/y3
//			p.cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(),
//					p3.getY());
//		}
//		if (circular) {
//			for (int i = length; i < length + 4; i += 2) {
//				p.lineTo(points[i], points[i + 1]);
//			}
//			p.lineTo(points[0], points[1]);
//		}
//		canvas.drawPath(p, paint);
//	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param multiplier
	 * @return
	 */
	private PointF calc(PointF p1, PointF p2, final float multiplier) {
		PointF result = new PointF();
		float diffX = p2.x - p1.x;
		float diffY = p2.y - p1.y;
		result.x = p1.x + (diffX * multiplier);
		result.y = p1.y + (diffY * multiplier);
		return result;
	}

}