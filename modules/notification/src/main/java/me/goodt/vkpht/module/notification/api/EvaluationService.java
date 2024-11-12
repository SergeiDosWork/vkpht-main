package me.goodt.vkpht.module.notification.api;

import me.goodt.vkpht.module.notification.api.dto.rtcore.EvaluationEventNotificationDto;

public interface EvaluationService {
	EvaluationEventNotificationDto findEvaluationEventById(Long id);
}
