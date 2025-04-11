package com.learn.logindemo.service;

import com.learn.logindemo.model.dto.response.UserResponse;
import com.learn.logindemo.utils.Result;

/**
 * 用户服务类
 * 该类实现了 UsersService 接口
 * 提供了用户相关的业务逻辑，包括通过 Token 查找用户信息和用户登出功能。
 */
public interface UsersService {

    /**
     * 验证 token
     * @param token token
     * @return 用户对象
     */
    Result<UserResponse> findUserByToken(String token);

    /**
     * 退出登入
     * @param token token
     * @return null
     */
    Result<String> logout(String token);
}
