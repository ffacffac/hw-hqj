package com.hw.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 展示用户为何要开启权限
 * Created by huangqj on 2019-04-16.
 */
@Target(ElementType.METHOD)//作用在方法之上
@Retention(RetentionPolicy.RUNTIME)//源码级别
public @interface OnShowRationale {
}
