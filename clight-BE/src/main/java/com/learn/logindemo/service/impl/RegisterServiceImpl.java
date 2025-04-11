package com.learn.logindemo.service.impl;

import com.alibaba.fastjson2.JSON;
import com.learn.logindemo.constant.enums.ResponseCode;
import com.learn.logindemo.mapper.UserMapper;
import com.learn.logindemo.model.dto.request.RegisterRequest;
import com.learn.logindemo.model.dto.response.UserResponse;
import com.learn.logindemo.model.entity.User;
import com.learn.logindemo.service.RedisService;
import com.learn.logindemo.service.RegisterService;
import com.learn.logindemo.utils.JWTUtil;
import com.learn.logindemo.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 登入服务实现类
 * 该类实现了注册服务接口，提供了用户注册的业务逻辑
 * 主要功能包括
 * -
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Value("${md5.salt}")
    private String salt;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ExecutorService executorService;

    /**
     * 登入接口实现函数
     */
    @Override
    public Result<String> register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(registerRequest.getUsername()) != null) {
            // 如果用户名已存在，返回失败结果
            return Result.fail(ResponseCode.ACCOUNT_EXIST.getCode(), ResponseCode.ACCOUNT_EXIST.getMsg());
        }
        // 对密码进行MD5加密
        String password = DigestUtils.md5Hex(registerRequest.getPassword() + salt);

        // 创建用户实体对象
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(password);
        user.setEmail(registerRequest.getEmail());
        user.setRoles(Set.of("NORMAL"));
        user.setStatus(1);

        // 插入用户到数据库
        userMapper.insertUser(user);

        // 检查用户插入是否成功
        UserResponse userResponse = userMapper.findByUsername(registerRequest.getUsername());
        if (userResponse == null) {
            return Result.fail();
        }
        // 生成 token
        String token = jwtUtil.generateToken(String.valueOf(userResponse.getId()));

        // 将 token 存入 Redis
        saveTokenInRedis(token, userResponse, user);

        return Result.success(token);
    }

    /**
     * 将 token + 用户信息 存放到 Redis
     * 并防止 Redis 缓存失败但是用户已插入到数据库的情况
     */
    private void saveTokenInRedis(String token, UserResponse userResponse, User user) {
        try {
            // 使用 Future 来控制 Redis 操作的超时时间
            Future<?> future = executorService.submit(() -> {
                redisService.setWithExpire("TOKEN" + token, JSON.toJSONString(userResponse), 1, TimeUnit.DAYS);
            });

            // 等待1秒，如果超过1秒未完成，抛出 TimeoutException
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // 如果 Redis 操作超时，回滚事务并删除用户
            userMapper.deleteByUsername(user.getUsername());
            throw new RuntimeException("Redis 操作超时", e);
        } catch (Exception e) {
            // 其他异常处理
            userMapper.deleteByUsername(user.getUsername());
            throw new RuntimeException("Redis 操作失败", e);
        }
    }
}
