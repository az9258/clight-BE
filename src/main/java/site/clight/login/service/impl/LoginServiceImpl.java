package site.clight.login.service.impl;

import com.alibaba.fastjson2.JSON;
import site.clight.login.constant.enums.ResponseCode;
import site.clight.login.mapper.UserMapper;
import site.clight.login.model.dto.request.LoginRequest;
import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.service.LoginService;
import site.clight.login.service.RedisService;
import site.clight.login.utils.JWTUtil;
import site.clight.login.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Value("${md5.salt}")
    private String salt;

    @Autowired
    UserMapper userMapper;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    RedisService redisService;

    @Override
    public Result<String> login(LoginRequest loginRequest) {
        /*
            1. 检查参数是否合法
            2. 根据用户名再user表中查询 是否存在
            3. 如果不存在，登入失败
            4. 如果存在，使用JwtToken
            5. token放入redis中
         */
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        password = DigestUtils.md5Hex(password + salt);
        UserResponse userResponse = userMapper.findUser(username, password);
        if (userResponse == null) {
            return Result.fail(ResponseCode.ACCOUNT_PWD_NOT_ERROR.getCode(), ResponseCode.ACCOUNT_PWD_NOT_ERROR.getMsg());
        }
        String token = jwtUtil.generateToken(String.valueOf(userResponse.getId()));

        redisService.setWithExpire("TOKEN" + token, JSON.toJSONString(userResponse), 1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
