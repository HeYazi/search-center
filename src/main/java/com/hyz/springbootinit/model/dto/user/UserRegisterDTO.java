package com.hyz.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册dto
 *
 * @author heguande
 * @date 2023/6/15 21:05
 */
@Data
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = 3802034842472892243L;
    /**
     * 用户帐户
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 检查密码
     */
    private String checkPassword;

}
