package com.example.myandroid.global;

import android.os.Environment;

public class Constant {
	public static final String APP_ID = "01e804b4b436c9fc08647e0cf8525933";

	public static final boolean DEBUG = true;
	public static final String sharePath = "andbase_share";
	public static final String USERSID = "user";

	public static final String APP_ROOT_DIR = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/MyAndroid/";

	public static final String AVATAR_DIR = APP_ROOT_DIR + "avatar/";
	public static final String IMAGE_CACHE_DIR = APP_ROOT_DIR + "cache/";
	public static final String TEMP_AVATAR = IMAGE_CACHE_DIR
			+ "temp_avatar.jpg";
	// cookies
	public static final String USERNAMECOOKIE = "cookieName";
	public static final String USERPASSWORDCOOKIE = "cookiePassword";
	public static final String USERPASSWORDREMEMBERCOOKIE = "cookieRemember";
	public static final String ISFIRSTSTART = "isfirstStart";

	public static final String IF_USE_PATTERN = "usePattern";
	public static final String PATTERN_STRING = "pattern";
}
