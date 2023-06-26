package com.hyz.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hegd
 * @date 2023/6/20 9:33
 */
@Data
public class UserAddDTO implements Serializable {
    private static final long serialVersionUID = -5606502100239543807L;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户角色：0:user 1:admin 2:ban
     */
    private Integer userRole;
}
