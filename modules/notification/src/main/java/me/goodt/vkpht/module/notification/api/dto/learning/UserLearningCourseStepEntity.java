package me.goodt.vkpht.module.notification.api.dto.learning;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_PRESENCE_FACT_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_PRESENCE_PLAN_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_PRESENCE_REFUSAL_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_PRESENCE_START_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;

@Getter
@Setter
public class UserLearningCourseStepEntity {
	private Long id;
	@JsonSetter(DATE_FROM_TAG)
	private Date dateFrom;
	@JsonSetter(DATE_TO_TAG)
	private Date dateTo;
	@JsonSetter(DATE_PRESENCE_PLAN_TAG)
	private Date datePresencePlan;
	@JsonSetter(DATE_PRESENCE_FACT_TAG)
	private Date datePresenceFact;
	@JsonSetter(DATE_PRESENCE_START_TAG)
	private Date datePresenceStart;
	@JsonSetter(DATE_PRESENCE_REFUSAL_TAG)
	private Date datePresenceRefusal;
}
