package com.learn.logindemo.controller;

import com.learn.logindemo.service.UsersService;
import com.learn.logindemo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退出登入
 */
@RestController
@RequestMapping("api")
public class LogoutController {

    @Autowired
    UsersService usersService;

    @GetMapping("logout")
    public Result<String> currentUser(@RequestHeader("Authorization") String token) {
        return usersService.logout(token);
    }
}
