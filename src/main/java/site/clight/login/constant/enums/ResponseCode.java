package site.clight.login.constant.enums;

import lombok.*;

@Getter
public enum ResponseCode {

    PARAMS_ERROR(10001, "参数有误"),
    ACCOUNT_PWD_NOT_ERROR(10002, "用户名或密码错误"),
    NO_PERMISSION(70001, "无访问权限"),
    SESSION_TIME_OUT(90001, "会话超时"),
    NO_LOGIN(90002, "未登入"),
    ACCOUNT_EXIST(10004, "用户已存在"),
    ACCOUNT_BANNED(10005, "用户封禁中"),

    SUCCESS_OK(200, "操作成功"),
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "未验证"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源未找到"),
    SERVER_ERROR(500, "服务器错误"),

    TOKEN_EXPIRED(10002, "token 过期"),
    TOKEN_ERROR(10003, "token 不合法");

    private final int code;
    private final String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
