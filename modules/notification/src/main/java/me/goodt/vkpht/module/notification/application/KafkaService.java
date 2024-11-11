package me.goodt.vkpht.module.notification.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.api.dto.KafkaEmployeeInfo;
import me.goodt.vkpht.module.notification.api.dto.kafka.NoticeDto;

public interface KafkaService {
	void sendKafkaMessage(List<Long> notificationTemplateContentIds, Collection<Long> employeeIds, Collection<Long> employeeCopyIds, Collection<Long> emailIds, Collection<Long> emailCopyIds, Collection<String> usersKeycloakIds, Collection<String> channels, Map<String, Object> content, String eventSubType);

	void sendKafkaMessage(Collection<Long> employeeIds, Map<String, Object> content, String eventSubType);

	void sendKafkaMessage(Map<KafkaEmployeeInfo, Map<String, Object>> data, Collection<String> channels, String eventSubType);

    boolean kafkaTemplateSend(NoticeDto notice);
}
