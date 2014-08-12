package com.example.myandroid.image;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class ImageFileCache extends DiskBasedCache implements ImageCache {

	public ImageFileCache(File rootDirectory) {
		super(rootDirectory);
	}

	public ImageFileCache(File rootDirectory, int maxCacheSizeInBytes) {
		super(rootDirectory, maxCacheSizeInBytes);
	}

	@Override
	public Bitmap getBitmap(String url) {
		Entry entry = get(url);
		if (entry == null) {
			return null;
		} else {
			return BitmapFactory.decodeByteArray(entry.data, 0,
					entry.data.length);
		}

	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		Entry entry = new Entry();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		entry.data = baos.toByteArray();
		put(url, entry);
	}
}
