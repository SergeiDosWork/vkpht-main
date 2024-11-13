package me.goodt.vkpht.module.notification.api;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentEmployeeSubscribeDto;

public interface SubscribeEmployeeToNotificationService {

    /**
     * Доступность отправки шаблона уведомления сотруднику через канал отправки
     */
    boolean isPossibleSendNotificationToEmployee(Long employeeId, Long notificationTemplateContentId, Long notificationReceiverSystemId);

    /**
     * Загрузка "Управление подписками"
     */
    NotificationTemplateContentEmployeeSubscribeDto load(Long employeeId, String receiverSystemName);

    /**
     * Сохранение "Управление подписками"
     */
    void save(NotificationTemplateContentEmployeeSubscribeDto dto);
}
