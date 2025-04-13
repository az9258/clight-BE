package site.clight.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.clight.login.filter.LoginInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Web MVC 配置类
 * 用于配置跨域和拦截器
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 配置跨域规则
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域配置
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

    /**
     * 配置拦截器规则
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 不拦截的请求
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/api/auth/**");
        excludePath.add("/static/**");
        excludePath.add("/assets/**");

        // 需要拦截的请求
        List<String> addPath = new ArrayList<>();
        addPath.add("/admin/**");
        addPath.add("/test/**");

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(addPath.toArray(new String[0]))
                .excludePathPatterns(excludePath);

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
