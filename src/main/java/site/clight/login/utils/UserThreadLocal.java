package site.clight.login.utils;

import site.clight.login.model.dto.response.UserResponse;

/**
 * 用户线程本地变量工具类
 * 用于在当前线程中存储和获取用户信息
 */
public class UserThreadLocal {

    private UserThreadLocal() {

    }

    // 线程本地变量，存储UserResponse对象
    private static final ThreadLocal<UserResponse> LOCAL = new ThreadLocal<UserResponse>();

    /**
     * 将用户信息存入当前线程
     * @param userResponse 用户响应对象
     */
    public static void put(final UserResponse userResponse) {
        LOCAL.set(userResponse);
    }

    /**
     * 从当前线程获取用户信息
     * @return 用户响应对象
     */
    public static UserResponse get() {
        return LOCAL.get();
    }

    /**
     * 清除当前线程中的用户信息
     */
    public static void remove() {
        LOCAL.remove();
    }
}
