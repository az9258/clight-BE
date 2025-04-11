package com.learn.logindemo.controller;

import com.learn.logindemo.model.dto.request.LoginRequest;
import com.learn.logindemo.service.LoginService;
import com.learn.logindemo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登入
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }
}
