package com.divillafajar.app.pos.pos_app_sini.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    private I18nConfig i18nConfig;

    public WebConfig(I18nConfig i18nConfig) {
        this.i18nConfig=i18nConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(i18nConfig.localeChangeInterceptor());
    }
}
