package com.learn.logindemo.service.impl;

import com.alibaba.fastjson2.JSON;
import com.learn.logindemo.constant.enums.ResponseCode;
import com.learn.logindemo.mapper.UserMapper;
import com.learn.logindemo.model.dto.response.UserResponse;
import com.learn.logindemo.service.RedisService;
import com.learn.logindemo.service.UsersService;
import com.learn.logindemo.utils.JWTUtil;
import com.learn.logindemo.utils.Result;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 该类实现了 UsersService 接口
 * 提供了用户相关的业务逻辑，包括通过 Token 查找用户信息和用户登出功能。
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    RedisService redisService;

    /**
     * 通过 Token 查找用户信息
     * 根据提供的 Token 验证其有效性，并从 Redis 中获取用户信息。
     * @param token 用户提供的 Token，格式为 "Bearer {token}"
     * @return 返回用户信息或错误信息
     */
    @Override
    public Result<UserResponse> findUserByToken(String token) {

        if (token == null || !token.startsWith("Bearer")) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }
        token = token.substring(7);

        boolean Ok = jwtUtil.verifyToken(token);
        if (!Ok) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }
        String userId = jwtUtil.extractUserId(token);

        // 从 redis 获取 token
        String userJson = redisService.get("TOKEN" + token, String.class);
        if (StringUtil.isNullOrEmpty(userJson)) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }

        UserResponse userResponse = JSON.parseObject(userJson, UserResponse.class);

        return Result.success(userResponse);
    }

    /**
     * 用户登出
     * 根据提供的 Token 从 Redis 中删除用户信息，实现登出功能。
     * @param token 用户提供的 Token，格式为 "Bearer {token}"
     * @return 返回登出结果
     */
    @Override
    public Result<String> logout(String token) {
        if (token == null || !token.startsWith("Bearer")) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }
        token = token.substring(7);

        redisService.delete("TOKEN" + token);
        return Result.success();
    }
}
