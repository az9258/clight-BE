package site.clight.login.service;

import site.clight.login.model.dto.request.LoginRequest;
import site.clight.login.utils.Result;

/**
 * 登录服务接口，定义了用户登录的方法。
 */
public interface LoginService {

    /**
     * 登入功能
     * @param loginRequest 用户名和密码
     * @return 结果
     */
    Result<String> login(LoginRequest loginRequest);
}
