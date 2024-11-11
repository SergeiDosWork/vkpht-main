package me.goodt.vkpht.module.notification.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class ProcessedTokensData {
	Map<KafkaEmployeeInfo, Map<String, String>> kafkaEmployeeInfoAndProcessedTokens;
	// ассоциативный массив обработанных токенов (токен=реальное значение)
	private Map<String, String> processedTokens;
	// список идентификаторов получателей (employee_id)
	private Collection<Long> recipients;
	// список идентификаторов из БД кейклока (нужно для Лукойла)
	private Collection<String> usersKeycloakIds;
}
