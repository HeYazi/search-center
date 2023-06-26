package com.hyz.springbootinit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyz.springbootinit.common.ErrorCode;
import com.hyz.springbootinit.constant.UserConstant;
import com.hyz.springbootinit.exception.BusinessException;
import com.hyz.springbootinit.mapper.UserMapper;
import com.hyz.springbootinit.model.dto.user.UserQueryDTO;
import com.hyz.springbootinit.model.entity.User;
import com.hyz.springbootinit.model.enums.UserRoleEnum;
import com.hyz.springbootinit.model.vo.user.LoginUserVO;
import com.hyz.springbootinit.service.UserService;
import com.hyz.springbootinit.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author hegd
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-06-13 22:53:56
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String SALT = "hyz";

    //region 注册、登录、登出、获取当前用户信息

    @Override
    @Transactional
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 参数校验
        if (userAccount.length() < 3) {
            log.error("账号过短，请输入 3 位以上的账号");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "账号过短，请输入 3 位以上的账号");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            log.error("密码过短，请输入 6 位以上的密码");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "密码过短，请输入 6 位以上的密码");
        }
        if (!userPassword.equals(checkPassword)) {
            log.error("两次密码不一致");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "两次密码不一致");
        }
        // 2. 账号不能重复
        // todo 第一版，用 string 来存储，第二版修改用 set 来存储。比较两版直接的差距
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(UserConstant.REGISTER_CACHE + userAccount))) {
            log.error("重复注册");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "重复注册");
        } else {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUserAccount, userAccount);
            User one = this.getOne(queryWrapper);
            stringRedisTemplate.opsForValue().set(UserConstant.REGISTER_CACHE + userAccount, "已注册", 1, TimeUnit.MINUTES);
            if (one != null) {
                log.error("重复注册");
                throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "重复注册");
            }
        }
        // 3. 密码加密
        String encryptPassword = DigestUtil.md5Hex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            log.error("添加失败");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "添加失败");
        }
        stringRedisTemplate.opsForValue().set(UserConstant.REGISTER_CACHE + userAccount, "已注册", 1, TimeUnit.MINUTES);
        return user.getId();
    }

    @Override
    public String userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 参数校验
        userInfoCheck(userAccount, userPassword);
        // 2. 判断用户是否重复登录
        if (request != null) {
            String token = request.getHeader("token");
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(UserConstant.LOGIN_USER_KEY + token))) {
                log.error("用户【" + userAccount + "】重复登陆了");
                return token;
            }
        }
        // 3. 获取用户数据
        String encryptPassword = DigestUtil.md5Hex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            log.error("用户不存在或密码错误");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "用户不存在或密码错误");
        }
        // 4. 登录用户转换
        LoginUserVO loginUserVO = BeanUtil.copyProperties(user, LoginUserVO.class);
        // 5. 生成 token
        String token = IdUtil.simpleUUID();
        // 6. 用户信息放入 redis 当中
        Map<String, String> map = ConvertUtil.convertObjectToMap(loginUserVO);

        stringRedisTemplate.opsForHash().putAll(UserConstant.LOGIN_USER_KEY + token, map);
        stringRedisTemplate.expire(UserConstant.LOGIN_USER_KEY + token, UserConstant.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        String token = tokenCheck(request);
        String key = UserConstant.LOGIN_USER_KEY + token;
        // 如果用户存在
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            stringRedisTemplate.delete(key);
        } else {
            // 不存在则抛出异常
            log.error("用户不存在");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "用户不存在");
        }
        return true;
    }

    @Override
    public LoginUserVO getLoginUser(HttpServletRequest request) {
        String token = tokenCheck(request);
        String key = UserConstant.LOGIN_USER_KEY + token;
        // 如果用户存在
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
            if (userMap.isEmpty()) {
                log.error("用户不存在");
                throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "用户不存在");
            }
            return BeanUtil.fillBeanWithMap(userMap, new LoginUserVO(), false);
        } else {
            // 不存在则抛出异常
            log.error("用户不存在");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "用户不存在");
        }
    }

    @Override
    public Long addUser(String userAccount, String userPassword, Integer userRole) {
        // 1. 参数校验
        userInfoCheck(userAccount, userPassword);
        // 2. 判断用户权限是否存在
        if (!UserRoleEnum.getValueList().contains(userRole)) {
            log.error("请分配有意义的用户权限");
            throw new BusinessException(ErrorCode.ADMIN_USER_OPERATION_ERROR, "请分配有意义的用户权限");
        }
        // 3. 用户不存在则保存用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUserAccount, userAccount);
        List<User> list = this.list(wrapper);
        if (list == null) {
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(userPassword);
            user.setUserRole(userRole);
            if (this.save(user)) {
                return user.getId();
            }
        }
        throw new BusinessException(ErrorCode.ADMIN_USER_OPERATION_ERROR, "添加用户失败");
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryDTO userQueryDTO) {
        if (userQueryDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryDTO.getId();
        String userName = userQueryDTO.getUserName();
        String userProfile = userQueryDTO.getUserProfile();
        String userRole = userQueryDTO.getUserRole();
        String sortField = userQueryDTO.getSortField();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(false, true, sortField);
        return queryWrapper;
    }

    @Override
    public List<LoginUserVO> getLoginUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map((user -> BeanUtil.copyProperties(user, LoginUserVO.class))).collect(Collectors.toList());
    }

    /**
     * 令牌检查
     *
     * @param request 请求
     * @return {@link String}
     */
    private String tokenCheck(HttpServletRequest request) {
        if (request == null) {
            log.error(ErrorCode.PARAMS_ERROR.getMessage());
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String token = request.getHeader("token");

        if (StringUtils.isAllBlank(token)) {
            log.error("用户未登录");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        return token;
    }

    /**
     * 用户信息检查
     *
     * @param userAccount  用户帐户
     * @param userPassword 用户密码
     */
    private void userInfoCheck(String userAccount, String userPassword) {
        // 1. 参数校验
        if (userAccount.length() < 3) {
            log.error("账号过短，请输入 3 位以上的账号");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "账号过短，请输入 3 位以上的账号");
        }
        if (userPassword.length() < 6) {
            log.error("密码过短，请输入 6 位以上的密码");
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "密码过短，请输入 6 位以上的密码");
        }
    }
    // endregion
}



