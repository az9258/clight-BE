package com.learn.logindemo.service;

import com.learn.logindemo.model.dto.request.LoginRequest;
import com.learn.logindemo.utils.Result;

/**
 * 登录服务类
 * 该类提供了登录服务接口
 * 主要功能包括：
 * - 用户名和密码的校验
 * - JWT Token 的生成
 * - 用户信息存储到 Redis
 */
public interface LoginService {

    /**
     * 登入功能
     * @param loginRequest 用户名和密码
     * @return 结果
     */
    Result<String> login(LoginRequest loginRequest);
}
