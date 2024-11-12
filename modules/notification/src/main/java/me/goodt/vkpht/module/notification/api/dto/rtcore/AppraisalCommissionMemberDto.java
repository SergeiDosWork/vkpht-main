package me.goodt.vkpht.module.notification.api.dto.rtcore;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalCommissionMemberDto {
	private Long id;
	@JsonSetter(DtoTagConstants.EMPLOYEE_ID_TAG)
	private Long employeeId;
	@JsonSetter(DtoTagConstants.FIRST_NAME_TAG)
	private String firstName;
	@JsonSetter(DtoTagConstants.LAST_NAME_TAG)
	private String lastName;
	private String patronymic;
	@JsonSetter(DtoTagConstants.JOB_TITLE_TAG)
	private String jobTitle;
	private AppraisalCommissionMemberDto substitute;
	@JsonSetter(DtoTagConstants.APPRAISAL_COMMISSION_TAG)
	private AppraisalCommissionDto appraisalCommission;
	@JsonSetter(DtoTagConstants.APPRAISAL_COMMISSION_ROLE_TAG)
	private AppraisalCommissionRoleDto appraisalCommissionRole;
	@JsonSetter(DtoTagConstants.DATE_FROM_TAG)
	private Date dateFrom;
	@JsonSetter(DtoTagConstants.DATE_TO_TAG)
	private Date dateTo;
	@JsonSetter(DtoTagConstants.KEY_MEMBER_TAG)
	private Boolean keyMember;
}
