package com.hw.ourlife.activity;


import io.reactivex.functions.Function;

/**
 * Created by huangqj on 2019-07-05.
 */

public abstract class BaseRepFun<T> implements Function<String, T> {

    @Override
    public T apply(String s) throws Exception {
        s = s + "改造后的----";
        return parse(s);
    }

    public abstract T parse(String s);
}
