package site.clight.login.service;

import site.clight.login.model.dto.request.RegisterRequest;
import site.clight.login.utils.Result;

/**
 * 注册服务接口，定义了注册相关的业务逻辑。
 */
public interface RegisterService {

    /**
     * 登入接口
     * @param registerRequest register数据
     * @return success/fail
     */
    public Result<String> register(RegisterRequest registerRequest);
}
