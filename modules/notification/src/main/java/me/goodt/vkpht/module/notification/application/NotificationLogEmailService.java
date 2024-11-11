package me.goodt.vkpht.module.notification.application;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;

public interface NotificationLogEmailService {
    void create(NotificationLogEntity notificationLog, Map<String, Boolean> emailIsCopy);

    List<String> findEmailByNotificationLogId(Long notificationLogId, boolean isCopy);
}
