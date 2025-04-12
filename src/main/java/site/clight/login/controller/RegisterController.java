package site.clight.login.controller;

import site.clight.login.model.dto.request.RegisterRequest;
import site.clight.login.service.RegisterService;
import site.clight.login.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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