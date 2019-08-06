package com.hw.ormlitedemo;

import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

import com.hw.ormlitedemo.bean.User;
import com.hw.ormlitedemo.database.DaoManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * @author huangqj
 * @date 2019-07-26
 */

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getName();

    public void myClick(View v) {
        Log.e(TAG, "onClick: " + "view id is==" + v.getId());
    }

    public void insertUser(User user) {
        try {
            Dao<User, Long> dao = DaoManager.getDao(User.class);
            dao.create(user);
            Log.e(TAG, "insertUser: " + "保存成功--->" + user.toString());
        } catch (SQLException e) {
            Log.e(TAG, "onClick: ", e);
        }
    }

    public List<User> findUserAll() {
        try {
            Dao<User, Long> userDao = DaoManager.getUserDao();
            List<User> users = userDao.queryForAll();
            Log.e(TAG, "findUserAll: " + users.toString());

            // List<User> forEqUserList = userDao.queryForEq(User.NAME, "风格哈");
            // Log.e(TAG, "queryForEq: " + forEqUserList.toString());
            //
            // User user = userDao.queryForId(1L);
            // Log.e(TAG, "queryForId: " + user.toString());
            //
            // QueryBuilder<User, Long> queryBuilder = userDao.queryBuilder();
            // Where<User, Long> userLongWhere = queryBuilder.where().eq(User.NAME, "风格哈").and().eq(User.AGE, 45666);
            // List<User> query = userLongWhere.query();
            // // List<User> query = userDao.query(queryBuilder.prepare());
            // Log.e(TAG, "queryBuilder: " + query.toString());
            //
            // List<User> queryLike = userDao.queryBuilder().where().like(User.ADDRESS, "大哥哥").query();
            // Log.e(TAG, "queryLike: " + queryLike.toString());
            //
            // List<User> andOrList = userDao.queryBuilder().where().eq(User.NAME, "风格哈").and().eq(User.AGE, 45666).or()
            //                               .eq(User.AGE, 456667788).query();
            // Log.e(TAG, "andOrList: " + andOrList.toString());
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findById(long id) {
        try {
            return DaoManager.getUserDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete() throws SQLException {
        DaoManager.getUserDao().deleteById(2L);
    }

    public void update() throws SQLException {

        List<User> userList = DaoManager.getUserDao().queryForAll();

        // User user = new User("张三111", "123", "123", 50);
        // UpdateBuilder<User, Long> updateBuilder = DaoManager.getUserDao().updateBuilder();
        // updateBuilder.updateColumnValue(User.NAME, user.getName()).updateColumnValue(User.AGE, user.getAge());
        // updateBuilder.where().eq(User.ID, 1L);
        // updateBuilder.update();

        // DaoManager.getUserDao().updateId(user, 1L);
    }

    public List<User> userLinke() throws SQLException {
        QueryBuilder<User, Long> builder = DaoManager.getUserDao().queryBuilder();
        Where<User, Long> where = builder.where().eq(User.AGE, 20);
        where.and().like(User.ADDRESS, "%" + "gzs" + "%");
        return builder.query();
    }
}
