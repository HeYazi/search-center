package com.hyz.springbootinit.utils;

import com.hyz.springbootinit.common.ErrorCode;
import com.hyz.springbootinit.exception.BusinessException;

/**
 * 抛异常工具类
 *
 * @author hegd
 * @date 2023/6/20 20:18
 */
public class ThrowUtils {
    public static void throwIf(Boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    public static void throwIf(Boolean condition, ErrorCode errorCode, String message) {
        if (condition) {
            throw new BusinessException(errorCode, message);
        }
    }
}
