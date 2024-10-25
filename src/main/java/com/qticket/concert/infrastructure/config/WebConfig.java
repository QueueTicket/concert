package com.qticket.concert.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ConcertInterceptor concertInterceptor;

    @Autowired
    public WebConfig(ConcertInterceptor concertInterceptor) {
        this.concertInterceptor = concertInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 `/concerts/**` 요청에 대해 ConcertInterceptor 적용
        registry.addInterceptor(concertInterceptor).addPathPatterns("/concerts/**");
        registry.addInterceptor(concertInterceptor).addPathPatterns("/concert-seats/**");
    }
}
