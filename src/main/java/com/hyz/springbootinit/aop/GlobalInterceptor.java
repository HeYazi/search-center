package com.hyz.springbootinit.aop;

import com.hyz.springbootinit.annotation.AuthCheck;
import com.hyz.springbootinit.annotation.ParamsCheck;
import com.hyz.springbootinit.common.ErrorCode;
import com.hyz.springbootinit.exception.BusinessException;
import com.hyz.springbootinit.model.enums.UserRoleEnum;
import com.hyz.springbootinit.model.vo.user.LoginUserVO;
import com.hyz.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 全球拦截器
 *
 * @author hegd
 * @date 2023/6/20 11:07
 */
@Aspect
@Component
@Slf4j
public class GlobalInterceptor {
    @Resource
    private UserService userService;

    /**
     * 参数校验拦截器
     *
     * @param pjp pjp
     * @param pc  个人电脑
     * @return {@link Object}
     * @throws Throwable throwable
     */
    @Around("@annotation(pc)")
    public Object paramsCheckInterceptor(ProceedingJoinPoint pjp, ParamsCheck pc) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg == null  ) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        return pjp.proceed();
    }


    /**
     * 身份验证检查拦截器
     *
     * @param pjp       pjp
     * @param authCheck 身份验证检查
     * @return {@link Object}
     * @throws Throwable throwable
     */
    @Around("@annotation(authCheck)")
    public Object authCheckInterceptor(ProceedingJoinPoint pjp, AuthCheck authCheck) throws Throwable {
        UserRoleEnum mustRole = authCheck.value();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户信息
        LoginUserVO loginUser = userService.getLoginUser(request);
        // 判断注解不为空
        if (mustRole != null) {
            UserRoleEnum userRole = UserRoleEnum.getByValue(loginUser.getUserRole());
            if (userRole == null) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有对应的权限，请联系管理员");
            }
            // 如果被封禁则直接爆异常
            if (UserRoleEnum.BAN.equals(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "用户已被禁用");
            }
            // 如果符合权限，则通过
            if (mustRole.equals(userRole)) {
                return pjp.proceed();
            }
        }
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }


}
