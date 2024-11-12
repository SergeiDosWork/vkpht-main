package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.notification.api.dto.rtcore.EvaluationEventNotificationDto;
import me.goodt.vkpht.module.notification.api.EvaluationService;
import me.goodt.vkpht.module.notification.api.rtcore.RtCoreServiceClient;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

	private final RtCoreServiceClient rtCoreServiceClient;

	@Override
	public EvaluationEventNotificationDto findEvaluationEventById(Long id) {
		return rtCoreServiceClient.findEvaluationEventById(id);
	}
}
