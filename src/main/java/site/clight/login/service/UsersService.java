package site.clight.login.service;

import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.utils.Result;

public interface UsersService {

    /**
     * 验证 token
     * @param token token
     * @return 用户对象
     */
    Result<UserResponse> findUserByToken(String token);

    /**
     * 退出登入
     * @param token token
     * @return null
     */
    Result<String> logout(String token);
}
