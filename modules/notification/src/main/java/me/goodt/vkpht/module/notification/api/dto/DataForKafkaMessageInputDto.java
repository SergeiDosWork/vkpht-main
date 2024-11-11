package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataForKafkaMessageInputDto {
	@JsonSetter(DtoTagConstants.EMPLOYEE_IDS_TAG)
	private List<Long> employeeIds;
	private String message;
	private String subject;
	@JsonSetter(DtoTagConstants.EVENT_SUB_TYPE_TAG)
	private String eventSubType;
	@JsonSetter(DtoTagConstants.USERS_KEYCLOAK_IDS_TAG)
	private List<String> usersKeycloakIds;
	@JsonSetter(DtoTagConstants.USE_SYSTEM_TEMPLATES_TAG)
	private Boolean useSystemTemplates;
}
