package com.hw.ormlitedemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.hw.ormlitedemo.bean.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by huangqj on 2019-07-25.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DBHelper.class.getName();
    // private static final String DB_NAME = "hqj_test.db";
    /**
     * 这里生成的db文件放在自定义的文件夹内
     */
    private static final String DB_NAME =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/hqj/" + "hqj_test.db";
    private static DBHelper dbHelper;
    private static final String PASSWORD = "hqj123456";

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    /**
     * 第一次操作数据库时候,被调用，只被调用一次
     *
     * @param database         database
     * @param connectionSource connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //创建表
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    /**
     * 升级数据库
     *
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            //这里只是简单移除了旧表,新建新表,这样会导致数据丢失,现实一般不这么做
            TableUtils.dropTable(connectionSource, User.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "onUpgrade: ", e);
        }
    }
}
