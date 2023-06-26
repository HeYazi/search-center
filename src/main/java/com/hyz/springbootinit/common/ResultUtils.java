package com.hyz.springbootinit.common;

import com.hyz.springbootinit.exception.BusinessException;

/**
 * 返回体工具
 *
 * @author hegd
 * @date 2023/6/13 23:10
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }

    public static <T> BaseResponse<T> error(BusinessException e) {
        return new BaseResponse<>(e.getCode(), null, e.getMessage());
    }

}
