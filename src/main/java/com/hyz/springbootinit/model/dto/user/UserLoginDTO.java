package com.hyz.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author heguande
 * @date 2023/6/17 16:49
 */
@Data
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = -3466603733700005426L;
    /**
     * 用户帐户
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;
}
