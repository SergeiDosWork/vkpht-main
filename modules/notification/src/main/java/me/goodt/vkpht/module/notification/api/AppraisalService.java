package me.goodt.vkpht.module.notification.api;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalCommissionMemberDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalEventCommissionMemberNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalEventNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.AppraisalRecommendationDto;

public interface AppraisalService {
    AppraisalEventNotificationDto findAppraisalEventById(Long id);

    List<AppraisalEventCommissionMemberNotificationDto> findActualAppraisalEventCommissionMemberByAppraisalEventId(Long appraisalEventId);

    List<AppraisalCommissionMemberDto> findAppraisalCommissionMemberByParams(Long appraisalEventId,
                                                                             Long appraisalCommissionRoleId);

    List<AppraisalRecommendationDto> getAppraisalRecommendations(List<Long> appraisalRecommendationIds);
}
