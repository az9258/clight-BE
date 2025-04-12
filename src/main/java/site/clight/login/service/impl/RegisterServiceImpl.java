package site.clight.login.service.impl;

import com.alibaba.fastjson2.JSON;
import site.clight.login.constant.enums.ResponseCode;
import site.clight.login.mapper.UserMapper;
import site.clight.login.model.dto.request.RegisterRequest;
import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.model.entity.User;
import site.clight.login.service.RedisService;
import site.clight.login.service.RegisterService;
import site.clight.login.utils.JWTUtil;
import site.clight.login.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    @Override
    public Result<String> register(RegisterRequest registerRequest) {
        /*
            1. 账号是否存在?
            2. 存在 账号已经被注册
            3. 不存在 注册用户
            4. 生成 token
            5. 存入 token 到 redis
            6. 加上事务？
         */
        if (userMapper.findByUsername(registerRequest.getUsername()) != null) {
            return Result.fail(ResponseCode.ACCOUNT_EXIST.getCode(), ResponseCode.ACCOUNT_EXIST.getMsg());
        }
        String password = DigestUtils.md5Hex(registerRequest.getPassword() + salt);

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(password);
        user.setEmail(registerRequest.getEmail());
        user.setRoles(Set.of("NORMAL"));
        user.setStatus(1);

        userMapper.insertUser(user);

        // 检查用户插入是否成功
        UserResponse userResponse = userMapper.findByUsername(registerRequest.getUsername());
        if (userResponse == null) {
            return Result.fail();
        }
        String token = jwtUtil.generateToken(String.valueOf(userResponse.getId()));

        // 将 token 存入 Redis
        saveTokenInRedis(token, userResponse, user);

        return Result.success(token);
    }

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
