package com.learn.logindemo.utils;

import com.learn.logindemo.constant.enums.ResponseCode;
import lombok.*;

/**
 * 用于返回结果
 */
@Getter
@Setter
public class Result<T> {
    private boolean success;
    private int code;
    private String msg;
    private T data;

    private Result(boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /*
        成功静态方法
    */

    // 快速成功响应
    public static <T> Result<T> success() {
        return new Result<>(true, ResponseCode.SUCCESS_OK.getCode(), ResponseCode.SUCCESS_OK.getMsg(), null);
    }

    // 自定义成功响应
    public static <T> Result<T> success(T data) {
        return new Result<>(true, ResponseCode.SUCCESS_OK.getCode(), ResponseCode.SUCCESS_OK.getMsg(), data);
    }

    /*
        失败静态方法
     */

    // 快速失败响应
    public static <T> Result<T> fail() {
        return new Result<>(false, ResponseCode.SERVER_ERROR.getCode(), ResponseCode.SERVER_ERROR.getMsg(), null);
    }

    // 自定义失败消息响应
    public static <T> Result<T> fail(String msg) {
        return new Result<>(false, ResponseCode.SERVER_ERROR.getCode(), msg, null);
    }

    // 自定义响应代码/消息失败响应
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(false, code, msg, null);
    }

    // 完全自定义失败响应
    public static <T> Result<T> fail(int code, String msg, T data) {
        return new Result<>(false, code, msg, data);
    }
}
