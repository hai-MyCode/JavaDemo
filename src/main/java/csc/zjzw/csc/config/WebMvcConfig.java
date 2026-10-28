package csc.zjzw.csc.config;

import csc.zjzw.csc.interceptor.LogInterceptor;
import csc.zjzw.csc.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final LogInterceptor logInterceptor;
    private final LoginInterceptor loginInterceptor;

    public WebMvcConfig(LogInterceptor logInterceptor, LoginInterceptor loginInterceptor) {     // ← 构造器注入
        this.logInterceptor = logInterceptor;
        this.loginInterceptor = loginInterceptor;

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error");

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/**")               // 拦截user下的路径
                .excludePathPatterns("/user/login","/error");       // 但排除 /error

    }
}
