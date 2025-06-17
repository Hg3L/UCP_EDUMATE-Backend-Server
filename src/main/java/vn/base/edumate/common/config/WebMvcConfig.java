package vn.base.edumate.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import vn.base.edumate.interceptor.UserStatusInterceptor;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserStatusInterceptor userStatusInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userStatusInterceptor).addPathPatterns("/post")
                .addPathPatterns("/post/like/**")
                .addPathPatterns("/image")
                .addPathPatterns("/comment")
                .addPathPatterns("/comment/like/**").
                addPathPatterns("/comment/*/replies")
                .addPathPatterns("/user")
                .addPathPatterns("/v1/auth/firebase")
                .addPathPatterns("/history/**"); // Áp dụng cho
    }
}
