package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalCommissionMemberDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalEventCommissionMemberNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalEventNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalRecommendationDto;
import me.goodt.vkpht.module.notification.api.AppraisalService;
import me.goodt.vkpht.module.notification.api.rtcore.RtCoreServiceClient;

@Service
@RequiredArgsConstructor
public class AppraisalServiceImpl implements AppraisalService {

	private final RtCoreServiceClient rtCoreServiceClient;

	@Override
	public AppraisalEventNotificationDto findAppraisalEventById(Long id) {
		return rtCoreServiceClient.findAppraisalEventById(id);
	}

	@Override
	public List<AppraisalEventCommissionMemberNotificationDto> findActualAppraisalEventCommissionMemberByAppraisalEventId(Long appraisalEventId) {
		return rtCoreServiceClient.findActualAppraisalEventCommissionMemberByAppraisalEventId(appraisalEventId);
	}

	@Override
	public List<AppraisalCommissionMemberDto> findAppraisalCommissionMemberByParams(Long appraisalEventId,
																					Long appraisalCommissionRoleId) {
		return rtCoreServiceClient.findAppraisalCommissionMemberByParams(
			appraisalEventId,
			appraisalCommissionRoleId
		);
	}

	@Override
	public List<AppraisalRecommendationDto> getAppraisalRecommendations(List<Long> appraisalRecommendationIds) {
		return rtCoreServiceClient.getAppraisalRecommendations(appraisalRecommendationIds);
	}
}
