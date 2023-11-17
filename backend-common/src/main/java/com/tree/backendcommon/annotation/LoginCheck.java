package com.tree.backendcommon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验 用于判断用户状态UserStatus,以确定用户是否可以正常登录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCheck {

    /**
     * 必须有某个角色
     *
     * @return
     */
    String mustStatus() default "";

}

