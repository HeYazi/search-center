package com.hyz.springbootinit.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author heguande
 * @date 2023/6/17 16:47
 */
@Data
public class LoginUserVO implements Serializable {
    private static final long serialVersionUID = 2568945171174001090L;
    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：0:user 1:admin 2:ban
     */
    private Integer userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
