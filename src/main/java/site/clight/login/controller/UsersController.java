package site.clight.login.controller;

import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.service.UsersService;
import site.clight.login.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口
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
