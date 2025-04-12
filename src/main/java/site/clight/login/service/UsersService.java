package site.clight.login.service;

import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.utils.Result;

/**
 * 用户服务接口，定义了用户相关的操作。
 */
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
