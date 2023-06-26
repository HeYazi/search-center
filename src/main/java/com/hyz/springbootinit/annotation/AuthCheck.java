package com.hyz.springbootinit.annotation;


import com.hyz.springbootinit.model.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 身份验证检查
 *
 * @author hegd
 * @date 2023/06/20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某个角色
     *
     * @return {@link String}
     */
    UserRoleEnum value() default UserRoleEnum.USER;

}