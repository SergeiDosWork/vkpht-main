package me.goodt.vkpht.module.notification.application;

import java.util.Map;

public interface NotificationService {
    void baseNotification(Map<String, Object> tokenValues, String code, Map<String, Object> parameters);
}
