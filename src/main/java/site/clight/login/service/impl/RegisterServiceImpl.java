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

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 用户注册服务实现类
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Value("${md5.salt}")
    private String salt;  // MD5加密盐值

    @Autowired
    private UserMapper userMapper;  // 用户数据访问接口

    @Autowired
    private JWTUtil jwtUtil;  // JWT工具类

    @Autowired
    private RedisService redisService;  // Redis服务

    @Autowired
    private ExecutorService executorService;  // 线程池，用于异步操作

    /**
     * 用户注册方法
     * @param registerRequest 注册请求参数
     * @return 包含JWT token的响应结果
     */
    @Override
    public Result<String> register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(registerRequest.getUsername()) != null) {
            return Result.fail(ResponseCode.ACCOUNT_EXIST.getCode(), ResponseCode.ACCOUNT_EXIST.getMsg());
        }
        
        // 使用MD5加盐加密密码
        String password = DigestUtils.md5Hex(registerRequest.getPassword() + salt);

        // 创建用户实体
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(password);
        user.setEmail(registerRequest.getEmail());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setRoles(Set.of("NORMAL"));  // 默认角色为NORMAL
        user.setStatus(1);  // 默认状态为激活

        // 插入用户数据
        userMapper.insertUser(user);

        // 验证用户是否插入成功
        UserResponse userResponse = userMapper.findByUsername(registerRequest.getUsername());
        if (userResponse == null) {
            return Result.fail();
        }
        
        // 生成JWT token
        String token = jwtUtil.generateToken(String.valueOf(userResponse.getId()));

        // 将token存入Redis
        saveTokenInRedis(token, userResponse, user);

        return Result.success(token);
    }

    /**
     * 将token保存到Redis中
     * @param token JWT token
     * @param userResponse 用户响应数据
     * @param user 用户实体(用于回滚操作)
     */
    private void saveTokenInRedis(String token, UserResponse userResponse, User user) {
        try {
            // 使用Future来控制Redis操作的超时时间
            Future<?> future = executorService.submit(() -> {
                redisService.setWithExpire("TOKEN" + token, JSON.toJSONString(userResponse), 1, TimeUnit.DAYS);
            });

            // 设置1秒超时等待
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // Redis操作超时，回滚用户数据
            userMapper.deleteByUsername(user.getUsername());
            throw new RuntimeException("Redis 操作超时", e);
        } catch (Exception e) {
            // 其他异常处理，回滚用户数据
            userMapper.deleteByUsername(user.getUsername());
            throw new RuntimeException("Redis 操作失败", e);
        }
    }
}
