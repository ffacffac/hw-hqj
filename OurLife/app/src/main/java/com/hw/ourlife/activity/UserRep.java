package com.hw.ourlife.activity;

/**
 * Created by huangqj on 2019-07-05.
 */

public class UserRep {
    private String name;

    public UserRep(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserRep{" + "name='" + name + '\'' + '}';
    }
}
