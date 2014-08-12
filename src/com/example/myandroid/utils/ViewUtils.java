package com.example.myandroid.utils;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.view.Gravity;

public class ViewUtils {
	@SuppressLint("NewApi")
	public static void setDrawerLeftEdgeSize(Activity activity,
			DrawerLayout drawerLayout, int gravity, float displayWidthPercentage) {
		if (activity == null || drawerLayout == null)
			return;

		try {
			ViewDragHelper dragger = null;
			if (gravity == Gravity.LEFT) {
				// find left ViewDragHelper and set it accessible
				Field leftDraggerField = drawerLayout.getClass()
						.getDeclaredField("mLeftDragger");
				leftDraggerField.setAccessible(true);
				dragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
			} else if (gravity == Gravity.RIGHT) {
				// find right ViewDragHelper and set it accessible
				Field rightDraggerField = drawerLayout.getClass()
						.getDeclaredField("mRightDragger");
				rightDraggerField.setAccessible(true);
				dragger = (ViewDragHelper) rightDraggerField.get(drawerLayout);
			} else {
				return;
			}
			// find edgesize and set is accessible
			Field edgeSizeField = dragger.getClass().getDeclaredField(
					"mEdgeSize");
			edgeSizeField.setAccessible(true);
			int edgeSize = edgeSizeField.getInt(dragger);
			// set new edgesize
			Point displaySize = new Point();
			activity.getWindowManager().getDefaultDisplay()
					.getSize(displaySize);
			edgeSizeField.setInt(dragger, Math.max(edgeSize,
					(int) (displaySize.x * displayWidthPercentage)));
		} catch (NoSuchFieldException e) {
			// ignore
		} catch (IllegalArgumentException e) {
			// ignore
		} catch (IllegalAccessException e) {
			// ignore
		}
	}
}
