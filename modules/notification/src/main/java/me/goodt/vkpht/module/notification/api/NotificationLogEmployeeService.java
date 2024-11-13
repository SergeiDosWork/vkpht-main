package me.goodt.vkpht.module.notification.api;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;

public interface NotificationLogEmployeeService {
    void create(NotificationLogEntity notificationLog, Map<Long, Boolean> employeeIdIsCopy);

    List<Long> findEmployeeIdByNotificationLogId(Long notificationLogId, boolean isCopy);
}
