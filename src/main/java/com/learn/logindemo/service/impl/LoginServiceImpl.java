package com.learn.logindemo.service.impl;

import com.alibaba.fastjson2.JSON;
import com.learn.logindemo.constant.enums.ResponseCode;
import com.learn.logindemo.mapper.UserMapper;
import com.learn.logindemo.model.dto.request.LoginRequest;
import com.learn.logindemo.model.dto.response.UserResponse;
import com.learn.logindemo.service.LoginService;
import com.learn.logindemo.service.RedisService;
import com.learn.logindemo.utils.JWTUtil;
import com.learn.logindemo.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录服务实现类
 * 该类实现了登录服务接口，提供了用户登录的业务逻辑。
 * 主要功能包括：
 * - 用户名和密码的校验
 * - JWT Token 的生成
 * - 用户信息存储到 Redis
 */
@Service
public class LoginServiceImpl implements LoginService {

    /**
     * MD5 加密的盐值
     * 从配置文件中加载，用于密码加密
     */
    @Value("${md5.salt}")
    private String salt;

    /**
     * 用户 MongDB 数据访问对象
     * 用于访问用户信息。
     */
    @Autowired
    UserMapper userMapper;

    @Autowired
    JWTUtil jwtUtil;

    /**
     * Redis 服务
     * 用于将用户信息存储到 Redis。
     */
    @Autowired
    RedisService redisService;

    @Override
    public Result<String> login(LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 使用 MD5 加密密码
        password = DigestUtils.md5Hex(password + salt);

        // 检查参数是否合法
        UserResponse userResponse = userMapper.findUser(username, password);
        if (userResponse == null) {
            // 如果用户不存在或密码错误，返回失败结果
            return Result.fail(ResponseCode.ACCOUNT_PWD_NOT_ERROR.getCode(), ResponseCode.ACCOUNT_PWD_NOT_ERROR.getMsg());
        }

        // 生成 JWT Token
        String token = jwtUtil.generateToken(String.valueOf(userResponse.getId()));

        // 将用户信息存储到 Redis，设置过期时间为 1 天
        redisService.setWithExpire("TOKEN" + token, JSON.toJSONString(userResponse), 1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
