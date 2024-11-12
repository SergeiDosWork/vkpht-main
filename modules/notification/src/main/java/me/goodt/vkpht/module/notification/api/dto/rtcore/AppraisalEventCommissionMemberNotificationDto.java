package me.goodt.vkpht.module.notification.api.dto.rtcore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppraisalEventCommissionMemberNotificationDto {
	private Long id;
	private Long appraisalCommissionMemberId;
	private Long appraisalCommissionMemberEmployeeId;
}
