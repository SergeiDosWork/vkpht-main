package me.goodt.vkpht.module.notification.application;

import me.goodt.vkpht.module.notification.domain.entity.NotificationDeferredSendEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;

public interface NotificationDeferredSendService {
    void create(NotificationLogEntity notificationLogEntity, String noticeBodyJson);

    NotificationDeferredSendEntity save(NotificationDeferredSendEntity entity);

    NotificationDeferredSendEntity findByNotificationLogId(Long id);

    void startDeferredSendNotification();
}
