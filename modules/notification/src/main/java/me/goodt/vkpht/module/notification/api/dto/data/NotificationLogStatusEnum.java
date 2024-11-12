package me.goodt.vkpht.module.notification.api.dto.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Статус отправки уведомления в Журнале уведомлений
 */
@Getter
@AllArgsConstructor
public enum NotificationLogStatusEnum {

	READY("Подготовлено к отправке"),
	SUCCESS("Отправлено"),
	ERROR("Ошибка"),
    BROKER_UNAVAILABLE("Брокер сообщений недоступен");

	private String statusName;
}
