package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.AUTHOR_NAME_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.BUTTON_LINK_EDIT_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.BUTTON_LINK_ONE_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.BUTTON_LINK_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.BUTTON_LINK_TWO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.BUTTON_LINK_UPDATE_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DIVISION_TEAM_ASSIGNMENT_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.EMPLOYEE_ASSIGNMENT_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.EXPERT_ASSIGNMENT_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.UPDATE_REQUIRED_DAYS_TAG;

@Getter
@Setter
public class EventInputDto {
	@JsonSetter(AUTHOR_NAME_TAG)
	private String authorName;
	@JsonSetter(BUTTON_LINK_TAG)
	private String buttonLink;
	@JsonSetter(BUTTON_LINK_EDIT_TAG)
	private String buttonLinkEdit;
	@JsonSetter(BUTTON_LINK_UPDATE_TAG)
	private String buttonLinkUpdate;
	@JsonSetter(UPDATE_REQUIRED_DAYS_TAG)
	private Integer updateRequiredDays;
	@JsonSetter(TASK_ID_TAG)
	private Long taskId;
	@JsonSetter(EMPLOYEE_ASSIGNMENT_ID_TAG)
	private Long employeeAssignmentId;
	@JsonSetter(EXPERT_ASSIGNMENT_ID_TAG)
	private Long expertAssignmentId;
	@JsonSetter(BUTTON_LINK_ONE_TAG)
	private String buttonLinkOne;
	@JsonSetter(BUTTON_LINK_TWO_TAG)
	private String buttonLinkTwo;
	@JsonSetter(DIVISION_TEAM_ASSIGNMENT_ID_TAG)
	private Long divisionTeamAssignmentId;
}
