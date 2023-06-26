package com.hyz.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hegd
 * @date 2023/6/20 20:11
 */
@Data
public class UpdateUserDTO implements Serializable {
    private static final long serialVersionUID = 5155784359116238804L;

    /**
     * id
     */
    private Long id;

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
}
