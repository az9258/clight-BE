package site.clight.login.service.impl;

import com.alibaba.fastjson2.JSON;
import site.clight.login.constant.enums.ResponseCode;
import site.clight.login.mapper.UserMapper;
import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.service.RedisService;
import site.clight.login.service.UsersService;
import site.clight.login.utils.JWTUtil;
import site.clight.login.utils.Result;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类，提供基于token的用户信息查询和登出功能
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserMapper userMapper; // 用户数据持久层接口

    @Autowired
    private JWTUtil jwtUtil; // JWT工具类，用于token验证和解析

    @Autowired
    private RedisService redisService; // Redis服务，用于token存储管理

    /**
     * 根据token获取用户信息
     * @param token 包含Bearer前缀的认证token
     * @return 包含用户信息的Result对象
     *         失败时返回TOKEN_ERROR状态码和消息
     */
    @Override
    public Result<UserResponse> findUserByToken(String token) {
        // 验证token格式有效性
        if (token == null || !token.startsWith("Bearer")) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }
        
        // 去除Bearer前缀
        token = token.substring(7);

        // 验证token签名有效性
        if (!jwtUtil.verifyToken(token)) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }

        // 从Redis获取存储的用户信息
        String userJson = redisService.get("TOKEN" + token, String.class);
        if (StringUtil.isNullOrEmpty(userJson)) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }

        // 反序列化用户信息
        UserResponse userResponse = JSON.parseObject(userJson, UserResponse.class);
        return Result.success(userResponse);
    }

    /**
     * 用户登出，删除Redis中的token记录
     * @param token 包含Bearer前缀的认证token
     * @return 操作结果
     *         失败时返回TOKEN_ERROR状态码和消息
     */
    @Override
    public Result<String> logout(String token) {
        // 验证token格式有效性
        if (token == null || !token.startsWith("Bearer")) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }
        
        // 去除Bearer前缀并删除Redis中的token
        token = token.substring(7);
        redisService.delete("TOKEN" + token);
        
        return Result.success();
    }
}
