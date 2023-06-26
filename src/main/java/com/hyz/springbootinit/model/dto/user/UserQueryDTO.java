package com.hyz.springbootinit.model.dto.user;

import com.hyz.springbootinit.common.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author hegd
 * @date 2023/6/20 22:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -7034890427238776967L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
}
