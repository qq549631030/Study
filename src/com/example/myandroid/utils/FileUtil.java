package com.example.myandroid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.myandroid.utils.FileOptionListener;

public class FileUtil {

	public final static int BUFFER_SIZE = 1024;

	public static void writeToFile(final String data, final String path,
			final FileOptionListener mListener) {
		writeToFile(data.getBytes(), path, mListener);
	}

	public static void writeToFile(final byte[] data, final String path,
			final FileOptionListener mListener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File file = new File(path);
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					FileOutputStream mFileOutputStream = new FileOutputStream(
							file);
					byte[] dataByte = data;
					int pos = 0;
					int length = dataByte.length;
					while (length > 0) {
						byte[] buffer = new byte[BUFFER_SIZE];
						if (length < BUFFER_SIZE) {
							buffer = new byte[length];
						}
						for (int i = 0; i < buffer.length; i++) {
							buffer[i] = dataByte[pos++];
						}
						mFileOutputStream.write(buffer);
						mFileOutputStream.flush();
						mListener.onProgress(pos * 100 / dataByte.length);
						length -= buffer.length;
					}
					mFileOutputStream.close();
					mListener.onSuccess(null);
					mFileOutputStream = null;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					mListener.onFail("FileNotFoundException");
				} catch (IOException e) {
					e.printStackTrace();
					mListener.onFail("IOException");
				}
			}
		}).start();
	}

	public static void readFromFile(final String path, final FileOptionListener mListener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					byte[] data = null;
					File file = new File(path);
					if (!file.exists()) {
						mListener.onFail("file not exist");
					} else {
						FileInputStream mFileInputStream = new FileInputStream(
								file);
						int length = mFileInputStream.available();
						data = new byte[mFileInputStream.available()];
						byte[] buffer = new byte[BUFFER_SIZE];
						int pos = 0;
						int line = 0;
						while ((line = mFileInputStream.read(buffer)) > 0) {
							for (int i = 0; i < line; i++) {
								data[pos++] = buffer[i];
							}
							mListener.onProgress(pos * 100 / length);
						}
						mFileInputStream.close();
						mListener.onSuccess(data);
						mFileInputStream = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
