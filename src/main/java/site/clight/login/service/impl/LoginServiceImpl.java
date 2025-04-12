/**
 * 登录服务接口的实现类，负责处理用户登录的具体业务逻辑。
 * 使用 Spring 的 @Service 注解将该类注册为一个服务组件。
 */
package site.clight.login.service.impl;

import com.alibaba.fastjson2.JSON;
import site.clight.login.constant.enums.ResponseCode;
import site.clight.login.mapper.UserMapper;
import site.clight.login.model.dto.request.LoginRequest;
import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.model.entity.User;
import site.clight.login.service.LoginService;
import site.clight.login.service.RedisService;
import site.clight.login.utils.JWTUtil;
import site.clight.login.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 登录服务实现类，实现了 LoginService 接口，处理用户登录的业务逻辑。
 */
@Service
public class LoginServiceImpl implements LoginService {

    /**
     * 从配置文件中读取 MD5 加密的盐值。
     */
    @Value("${md5.salt}")
    private String salt;

    /**
     * 用户映射器，用于与数据库进行交互，查询用户信息。
     */
    @Autowired
    UserMapper userMapper;

    /**
     * JWT 工具类，用于生成 JWT 令牌。
     */
    @Autowired
    JWTUtil jwtUtil;

    /**
     * Redis 服务类，用于操作 Redis 缓存。
     */
    @Autowired
    RedisService redisService;

    /**
     * 处理用户登录请求的方法。
     * 具体步骤包括：检查请求参数是否合法，根据用户名和密码查询用户信息，
     * 若用户存在则生成 JWT 令牌并将其存入 Redis 缓存，最后返回登录结果。
     * 
     * @param loginRequest 包含用户登录信息的请求对象。
     * @return 包含登录结果的 Result 对象，成功时返回 JWT 令牌，失败时返回错误信息。
     */
    @Override
    public Result<String> login(LoginRequest loginRequest) {
        
        // 从请求对象中获取用户名
        String username = loginRequest.getUsername();
        // 从请求对象中获取密码
        String password = loginRequest.getPassword();

        // 对密码进行 MD5 加密，加入盐值
        password = DigestUtils.md5Hex(password + salt);
        // 根据用户名和加密后的密码查询用户信息
        UserResponse userResponse = userMapper.findUser(username, password);
        // 如果用户信息为空，说明用户名或密码错误，返回失败结果
        if (userResponse == null) {
            return Result.fail(ResponseCode.ACCOUNT_PWD_NOT_ERROR.getCode(), ResponseCode.ACCOUNT_PWD_NOT_ERROR.getMsg());
        }
        // 更新登入时间
        User user = new User();
        user.setUsername(username);
        user.setUpdatedAt(new Date());
        if (!userMapper.updateUser(user)) {
            return Result.fail();
        }
        // 生成 JWT 令牌
        String token = jwtUtil.generateToken(String.valueOf(userResponse.getId()));

        // 将令牌和用户信息存入 Redis 缓存，并设置过期时间为 1 天
        redisService.setWithExpire("TOKEN" + token, JSON.toJSONString(userResponse), 1, TimeUnit.DAYS);

        // 返回登录成功结果，包含生成的 JWT 令牌
        return Result.success(token);
    }
}
