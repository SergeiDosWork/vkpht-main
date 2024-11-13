package me.goodt.vkpht.module.notification.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class NotificationConfig {
    @Value("${appConfig.notification.rootUrl}")
    private String rootUrl;
    @Value("${appConfig.notification.substituteLevel}")
    private int substituteLevel;
}
