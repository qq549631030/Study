package com.example.myandroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GifView extends ImageView {

	private long movieStart;

	private Movie movie;

	public GifView(Context context) {
		super(context);
	}

	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setGif(int gif) {
		movie = Movie.decodeStream(getResources().openRawResource(gif));
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long curTime = android.os.SystemClock.uptimeMillis();
		// 第一次播放
		if (movieStart == 0) {
			movieStart = curTime;
		}
		if (movie != null) {
			int duraction = movie.duration();
			int relTime = (int) ((curTime - movieStart) % duraction);
			movie.setTime(relTime);
			movie.draw(canvas, 0, 0);
			// 强制重绘
			invalidate();
		}
	}

}
