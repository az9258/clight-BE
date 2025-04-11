package com.learn.logindemo.controller;

import com.learn.logindemo.model.dto.response.UserResponse;
import com.learn.logindemo.service.UsersService;
import com.learn.logindemo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 获取用户信息
 */
@RestController
@RequestMapping("/api/auth")
public class UsersController {

    @Autowired
    UsersService usersService;

    @GetMapping("currentUser")
    public Result<UserResponse> currentUser(@RequestHeader("Authorization") String token) {
        return usersService.findUserByToken(token);
    }
}
