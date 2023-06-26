package com.hyz.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyz.springbootinit.annotation.AuthCheck;
import com.hyz.springbootinit.annotation.ParamsCheck;
import com.hyz.springbootinit.common.BaseResponse;
import com.hyz.springbootinit.common.DeleteDTO;
import com.hyz.springbootinit.common.ErrorCode;
import com.hyz.springbootinit.common.ResultUtils;
import com.hyz.springbootinit.exception.BusinessException;
import com.hyz.springbootinit.model.dto.user.*;
import com.hyz.springbootinit.model.entity.User;
import com.hyz.springbootinit.model.enums.UserRoleEnum;
import com.hyz.springbootinit.model.vo.user.LoginUserVO;
import com.hyz.springbootinit.service.UserService;
import com.hyz.springbootinit.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hegd
 * @date 2023/6/13 23:19
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    // todo 参数校验做成 AOP 切面 / sdk

    // region 注册、登录、登出、获取当前用户信息

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册dto
     * @return {@link BaseResponse}<{@link Long}>
     */
    @PostMapping("/register")
    @ParamsCheck
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            log.error(ErrorCode.PARAMS_ERROR.getMessage());
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            log.error("请求参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(id);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录dto
     * @param request      请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginDTO userLoginDTO
            , @Nullable HttpServletRequest request) {
        if (userLoginDTO == null) {
            log.error(ErrorCode.PARAMS_ERROR.getMessage());
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            log.error("请求参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String token = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(token);
    }

    /**
     * 用户注销
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        Boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    // todo 获取所有用户信息、获取所有当前登录用户信息

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link LoginUserVO}>
     */
    @PostMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        LoginUserVO vo = userService.getLoginUser(request);
        return ResultUtils.success(vo);
    }

    // endregion

    // region 增删改查

    /**
     * 添加用户
     *
     * @param userAddDTO 用户添加dto
     * @return {@link BaseResponse}<{@link Long}>
     */
    @PostMapping("/admin/add")
    @AuthCheck(UserRoleEnum.ADMIN)
    @ParamsCheck
    public BaseResponse<Long> addUser(@RequestBody UserAddDTO userAddDTO) {
        String userAccount = userAddDTO.getUserAccount();
        String userPassword = userAddDTO.getUserPassword();
        Integer userRole = userAddDTO.getUserRole();
        if (StringUtils.isAllBlank(userAccount, userPassword) || userRole == null) {
            log.error("请求参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userService.addUser(userAccount, userPassword, userRole);
        return ResultUtils.success(id);
    }

    /**
     * 删除用户
     *
     * @param deleteDTO 删除dto
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/admin/delete")
    @AuthCheck(UserRoleEnum.ADMIN)
    @ParamsCheck
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteDTO deleteDTO) {
        Long id = deleteDTO.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userService.removeById(id)) {
            return ResultUtils.success(Boolean.TRUE);
        }
        return ResultUtils.success(Boolean.FALSE);
    }

    /**
     * 更新用户
     *
     * @param updateUserDTO 更新用户dto
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/admin/update")
    @ParamsCheck
    @AuthCheck(UserRoleEnum.ADMIN)
    public BaseResponse<Boolean> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        if (updateUserDTO.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(updateUserDTO, user);
        if (userService.updateById(user)) {
            return ResultUtils.success(true);
        }
        throw new BusinessException(ErrorCode.ADMIN_USER_OPERATION_ERROR, "更新失败");
    }

    /**
     * 通过 id 获取用户信息
     *
     * @param id id
     * @return {@link BaseResponse}<{@link User}>
     */
    @PostMapping("/admin/get")
    @ParamsCheck
    @AuthCheck(UserRoleEnum.ADMIN)
    public BaseResponse<User> getUserById(@RequestBody Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id id
     * @return {@link BaseResponse}<{@link LoginUserVO}>
     */
    @GetMapping("/get/vo")
    public BaseResponse<LoginUserVO> getLoginUserVoById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * 分页获取用户列表
     *
     * @param userQueryDTO 用户查询dto
     * @return {@link BaseResponse}<{@link Page}<{@link User}>>
     */
    @PostMapping("/admin/list")
    @AuthCheck(UserRoleEnum.ADMIN)
    @ParamsCheck
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryDTO userQueryDTO) {
        long current = userQueryDTO.getCurrent();
        long size = userQueryDTO.getPageSize();
        Page<User> page = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryDTO));
        return ResultUtils.success(page);
    }

    /**
     * 用户签证官通过页面列表
     * 分页获取用户封装列表
     *
     * @param userQueryDTO 用户查询dto
     * @return {@link BaseResponse}<{@link Page}<{@link LoginUserVO}>>
     */
    @PostMapping("/list/page/vo")
    @AuthCheck
    @ParamsCheck
    public BaseResponse<Page<LoginUserVO>> listUserVoByPage(@RequestBody UserQueryDTO userQueryDTO) {
        if (userQueryDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryDTO.getCurrent();
        long size = userQueryDTO.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryDTO));
        Page<LoginUserVO> loginUserVoPage = new Page<>(current, size, userPage.getTotal());
        List<LoginUserVO> loginUserVo = userService.getLoginUserVO(userPage.getRecords());
        loginUserVoPage.setRecords(loginUserVo);
        return ResultUtils.success(loginUserVoPage);
    }
    // endregion

}
