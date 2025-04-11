package com.learn.logindemo.service;

import com.learn.logindemo.model.dto.request.RegisterRequest;
import com.learn.logindemo.utils.Result;

public interface RegisterService {

    /**
     * 登入接口
     * @param registerRequest register数据
     * @return success/fail
     */
    public Result<String> register(RegisterRequest registerRequest);
}
