package com.hyz.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyz.springbootinit.model.dto.user.UserQueryDTO;
import com.hyz.springbootinit.model.entity.User;
import com.hyz.springbootinit.model.vo.user.LoginUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author hegd
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-06-13 22:53:56
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户帐户
     * @param userPassword  用户密码
     * @param checkPassword 检查密码
     * @return {@link Long}
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户帐户
     * @param userPassword 用户密码
     * @return {@link String}
     */
    String userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request 请求
     * @return {@link Boolean}
     */
    Boolean userLogout(HttpServletRequest request);

    /**
     * 获取登录用户
     *
     * @param request 请求
     * @return {@link LoginUserVO}
     */
    LoginUserVO getLoginUser(HttpServletRequest request);

    /**
     * 添加用户
     *
     * @param userAccount  用户帐户
     * @param userPassword 用户密码
     * @param userRole     用户角色
     * @return {@link Long}
     */
    Long addUser(String userAccount, String userPassword, Integer userRole);

    /**
     * 获取查询条件
     *
     * @param userQueryDTO 用户查询dto
     * @return {@link QueryWrapper}<{@link User}>
     */
    QueryWrapper<User> getQueryWrapper(UserQueryDTO userQueryDTO);


    /**
     * 获取脱敏的用户信息
     *
     * @param userList 用户列表
     * @return {@link List}<{@link LoginUserVO}>
     */
    List<LoginUserVO> getLoginUserVO(List<User> userList);
}
