package com.example.myandroid.utils;

/**
 * 防止暴力点击
 * 
 * @author hx2lu
 * 
 */
public class ClickUtils {
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		if (time < lastClickTime) {// 防止调手机时间到过去导至点击失效
			lastClickTime = 0;
		}
		if (time - lastClickTime < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
