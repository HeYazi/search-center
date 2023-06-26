package com.hyz.springbootinit.constant;

/**
 * @author heguande
 * @date 2023/6/19 9:23
 */
public interface UserConstant {
    String REGISTER_CACHE = "user:register:";
    String LOGIN_USER_KEY = "login:token:";
    Long LOGIN_USER_TTL = 30L;
}
