package com.example.myandroid.utils;

public abstract class FileOptionListener {
	public abstract void onProgress(int progress);

	public abstract void onSuccess(byte[] data);

	public abstract void onFail(String code);
}
