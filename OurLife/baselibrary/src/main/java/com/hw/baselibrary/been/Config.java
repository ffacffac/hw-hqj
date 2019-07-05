package com.hw.baselibrary.been;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-09.
 */

public class Config {

    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Config{" + "password='" + password + '\'' + '}';
    }
}
