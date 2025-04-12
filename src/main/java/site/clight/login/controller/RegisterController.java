package site.clight.login.controller;

import site.clight.login.model.dto.request.RegisterRequest;
import site.clight.login.service.RegisterService;
import site.clight.login.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注册控制器类，用于处理用户注册相关的请求。
 * 该类提供了一个注册接口，用于接收用户注册请求并调用注册服务进行注册操作。
 */
@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("register")
    public Result<String> register(@RequestBody RegisterRequest registerRequest) {
        return registerService.register(registerRequest);
    }
}