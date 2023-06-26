package com.hyz.springbootinit.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 前后端交互协议
 *
 * @author hegd
 * @date 2023/6/13 23:06
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 2382385883826279935L;

    private int code;
    private T data;
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        // 直接调用本类的构造函数
        this(code, data, null);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
