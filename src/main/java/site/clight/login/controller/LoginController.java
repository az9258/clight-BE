package site.clight.login.controller;

import site.clight.login.model.dto.request.LoginRequest;
import site.clight.login.service.LoginService;
import site.clight.login.utils.Result;
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
