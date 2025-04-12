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

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    RedisService redisService;

    @Override
    public Result<UserResponse> findUserByToken(String token) {

        if (token == null || !token.startsWith("Bearer")) {
            return Result.fail(ResponseCode.TOKEN_ERROR.getCode(), ResponseCode.TOKEN_ERROR.getMsg());
        }
        token = token.substring(7);
//        System.out.println("===========================");
//        System.out.printf(token);
//        System.out.println("\n===========================");

        // 检查 token
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
