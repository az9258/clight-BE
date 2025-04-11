package com.learn.logindemo.controller;

import com.learn.logindemo.model.dto.request.RegisterRequest;
import com.learn.logindemo.service.RegisterService;
import com.learn.logindemo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注册
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