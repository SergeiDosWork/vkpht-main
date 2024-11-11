package me.goodt.vkpht.module.notification.api;

import com.goodt.drive.notify.application.dto.NotificationTemplateContentEmployeeSubscribeDto;

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
