package site.clight.login.controller;

import site.clight.login.service.UsersService;
import site.clight.login.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退出登入
 */
@RestController
@RequestMapping("/api/auth")
public class LogoutController {

    @Autowired
    UsersService usersService;

    @GetMapping("logout")
    public Result<String> currentUser(@RequestHeader("Authorization") String token) {
        return usersService.logout(token);
    }
}
