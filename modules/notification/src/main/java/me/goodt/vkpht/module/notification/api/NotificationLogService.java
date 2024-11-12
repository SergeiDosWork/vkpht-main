package me.goodt.vkpht.module.notification.api;

import java.util.Map;

import me.goodt.vkpht.module.notification.api.dto.NotificationLogDto;
import me.goodt.vkpht.module.notification.api.dto.data.NotificationLogStatusEnum;
import me.goodt.vkpht.module.notification.api.dto.kafka.NotificationLogResponseKafkaDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;

public interface NotificationLogService {
	NotificationLogEntity create(Long ntcId, Map<String, Object> content);

	/**
	 * Обновление статуса в Журнале уведомлений на основе ответа из kafka-messenger
	 */
	void updateStatusFromOutside(NotificationLogResponseKafkaDto notificationLogResponseKafkaDto);

    /**
     * Обновление статуса в Журнале уведомлений
     */
    void updateStatus(NotificationLogEntity notificationLog, NotificationLogStatusEnum newStatus);

	/**
	 * Загрузка карточки журнала уведмление
	 */
	NotificationLogDto load(Long id);

    NotificationLogEntity findById(Long id);
}
