package com.hw.ormlitedemo.database;

import com.hw.ormlitedemo.App;
import com.hw.ormlitedemo.bean.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by huangqj on 2019-07-25.
 */

public class DaoManager {

    public static <T, K> Dao<T, K> getDao(Class<T> cls) throws SQLException {
        // try {
        return App.getDBHelper().getDao(cls);
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        // return null;
    }

    public static Dao<User, Long> getUserDao() throws SQLException {
        return getDao(User.class);
    }
}
