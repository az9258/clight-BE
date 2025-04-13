package site.clight.login.filter;

import com.alibaba.fastjson2.JSON;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import site.clight.login.constant.enums.ResponseCode;
import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.service.UsersService;
import site.clight.login.utils.Result;
import site.clight.login.utils.UserThreadLocal;

import java.io.IOException;

/**
 * 登录拦截器
 * 用于验证用户登录状态和权限
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersService usersService;

    /**
     * 请求预处理方法
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器对象
     * @return 是否继续执行后续处理
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // handler 可能是 RequestResourceHandler springboot 程序 访问静态资源 默认去 classpath 下的 static 目录去查询
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader("Authorization");

        log.info("======================= request start =========================");
        String requestURI = request.getRequestURI();
        log.info("request uri: {}", requestURI);
        log.info("request method: {}", request.getMethod());
        log.info("token: {}", token);
        log.info("======================= request end =========================");

        // 查看 token 是否为空
        if (StringUtil.isNullOrEmpty(token)) {
            msgSet(response);
            return false;
        }

        // 是否能获取用户
        Result<UserResponse> tokenResult = usersService.findUserByToken(token);
        if (tokenResult.getData() == null) {
            msgSet(response);
            return false;
        }

        // 将响应用户信息放到本地线程
        UserThreadLocal.put(tokenResult.getData());

        // 登入放行
        return true;
    }

    /**
     * 设置未登录响应信息
     * @param response HTTP响应对象
     * @throws IOException IO异常
     */
    private static void msgSet(HttpServletResponse response) throws IOException {
        Result<String> result = Result.fail(ResponseCode.NO_LOGIN.getCode(), ResponseCode.NO_LOGIN.getMsg());
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(JSON.toJSONString(result));
    }

    /**
     * 请求完成后清理线程本地变量
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器对象
     * @param ex 可能抛出的异常
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
