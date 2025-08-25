package com.divillafajar.app.pos.pos_app_sini.config.properties;

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
    private String adminSuperKey;
    private String masterClientName;
    private String masterClientEmail;
    private String masterClientPubid;
}
