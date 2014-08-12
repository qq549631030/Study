package com.example.myandroid.image;

import java.io.File;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class MergedCache implements ImageCache {

	private final static String TAG = "ImageManager";

	private ImageMemoryCache imageMemoryCache; // 内存缓存

	private ImageFileCache imageFileCache; // 文件缓存

	public MergedCache(File sdCacheFile, int maxSdCacheSize) {
		imageMemoryCache = new ImageMemoryCache();
		imageFileCache = new ImageFileCache(sdCacheFile, maxSdCacheSize);
		// 读取文件缓存
		imageFileCache.initialize();
	}

	@Override
	public Bitmap getBitmap(String url) {
		// 从内存缓存中获取图片
		Bitmap bitmap = imageMemoryCache.getBitmapFromMemory(url);
		if (bitmap == null) {
			// 文件缓存中获取
			bitmap = imageFileCache.getBitmap(url);
			if (bitmap != null) {
				// 添加到内存缓存
				imageMemoryCache.addBitmapToMemory(url, bitmap);
			}
		}
		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		imageMemoryCache.addBitmapToMemory(url, bitmap);
		imageFileCache.putBitmap(url, bitmap);
	}

}
