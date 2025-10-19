package com.divillafajar.app.pos.pos_app_sini.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private I18nConfig i18nConfig;
    private final Environment env;

    @Value("${app.upload.image-dir}")
    private String imageBaseDir;

    @Value("${app.upload.image-base-url}")
    private String imageBaseUrl;

    public WebConfig(I18nConfig i18nConfig, Environment env) {
        this.i18nConfig=i18nConfig;
        this.env=env;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Pastikan path lokasi diawali dengan "file:" agar bisa dibaca Spring
        String location = Paths.get(imageBaseDir).toUri().toString();

        var handler = registry.addResourceHandler(imageBaseUrl + "/**")
                .addResourceLocations(location);

        // 🔹 Atur cache tergantung profile aktif
        if (env.acceptsProfiles("dev")) {
            handler.setCachePeriod(0); // No cache saat develop
            System.out.println("🧩 [WebConfig] Cache dimatikan untuk profile DEV");
        } else {
            handler.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());
            System.out.println("🚀 [WebConfig] Cache diaktifkan untuk profile PROD (7 hari)");
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(i18nConfig.localeChangeInterceptor());
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
