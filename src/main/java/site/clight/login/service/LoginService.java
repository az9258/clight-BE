package site.clight.login.service;

import site.clight.login.model.dto.request.LoginRequest;
import site.clight.login.utils.Result;

public interface LoginService {

    /**
     * 登入功能
     * @param loginRequest 用户名和密码
     * @return 结果
     */
    Result<String> login(LoginRequest loginRequest);
}
