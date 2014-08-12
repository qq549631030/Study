package com.example.myandroid.ormlite;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.myandroid.ormlite.bean.User;
import com.example.myandroid.utils.SDCardUtils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "CHENGYIJI.db";

	private static final int DATABASE_VERSION = 1;

	private static String DATABASE_PATH = Environment
			.getExternalStorageDirectory() + "/CHENGYIJI.db";

	private static String DATABASE_PATH_JOURN = Environment
			.getExternalStorageDirectory() + "/CHEYIJI.db-journal";

	private Context mContext;

	public DataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
		initDtaBasePath();
		try {
			File f = new File(DATABASE_PATH);
			if (!f.exists()) {
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
						DATABASE_PATH, null);
				onCreate(db);
				db.close();
			}
		} catch (Exception e) {
		}
	}

	private void initDtaBasePath() {
		if (!SDCardUtils.ExistSDCard()) {
			DATABASE_PATH = mContext.getFilesDir().getAbsolutePath()
					+ "/CHENGYIJI.db";
			DATABASE_PATH_JOURN = mContext.getFilesDir().getAbsolutePath()
					+ "/CHEYIJI.db-journal";
		}
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTableIfNotExists(arg1, User.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		try {
			TableUtils.dropTable(arg1, User.class, true);
			onCreate(arg0, arg1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
