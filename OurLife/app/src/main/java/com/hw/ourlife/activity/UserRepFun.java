package com.hw.ourlife.activity;

/**
 * Created by huangqj on 2019-07-05.
 */

public class UserRepFun extends BaseRepFun<UserRep> {

    @Override
    public UserRep parse(String s) {
        return new UserRep(s);
    }
}
