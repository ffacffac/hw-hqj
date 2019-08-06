package com.hw.ormlitedemo;

import com.hw.ormlitedemo.bean.User;
import com.hw.ormlitedemo.database.DaoManager;
import com.j256.ormlite.dao.Dao;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by huangqj on 2019-07-26.
 */
public class MainViewModelTest {

    MainViewModel model = new MainViewModel();

    @Test
    public void insertUser() throws Exception {

    }

    @Test
    public void findUserAll() throws Exception {
        Dao<User, Long> userDao = DaoManager.getUserDao();
        List<User> userList = userDao.queryForAll();
        for (User u : userList) {
            System.out.println("u--" + u.toString());
        }
    }

    @Test
    public void findById() throws Exception {
        User user = model.findById(1L);
        assertNotNull(user);
    }

    @Test
    public void delete() throws Exception {
        Dao<User, Long> userDao = DaoManager.getUserDao();
        userDao.deleteBuilder().where();
    }
}