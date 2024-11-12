package me.goodt.vkpht.module.notification.api.dto.quiz;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_END_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_CONFIRM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_START_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.EMPLOYEE_ID_TAG;

@Getter
@Setter
public class UserPollDto {
	private Long id;
	@JsonSetter(EMPLOYEE_ID_TAG)
	private Long employeeId;
	private PollDto poll;
	@JsonSetter(DATE_FROM_CONFIRM_TAG)
	private Date dateFromConfirm;
	@JsonSetter(DATE_START_TAG)
	private Date dateStart;
	@JsonSetter(DATE_END_TAG)
	private Date dateEnd;
	@JsonSetter(DATE_FROM_TAG)
	private Date dateFrom;
	@JsonSetter(DATE_TO_TAG)
	private Date dateTo;
}
