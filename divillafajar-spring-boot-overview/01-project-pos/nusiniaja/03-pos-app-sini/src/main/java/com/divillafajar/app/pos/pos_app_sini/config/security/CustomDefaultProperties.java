package com.divillafajar.app.pos.pos_app_sini.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "default")
@Data
public class CustomDefaultProperties {
    private String customerPwd;
    private String customerRole;
    private String userRole;
    private String customerDummyEmail;
}
