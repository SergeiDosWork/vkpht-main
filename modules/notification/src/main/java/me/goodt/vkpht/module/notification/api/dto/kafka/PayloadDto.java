package me.goodt.vkpht.module.notification.api.dto.kafka;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Map;

import static com.goodt.drive.auth.util.TextConstants.UNIT_CODE;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.EMAIL_COPY_IDS_TAG;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.EMAIL_IDS_TAG;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.EMPLOYEE_COPY_IDS_TAG;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.EMPLOYEE_IDS_TAG;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.EVENT_SUBTYPE_TAG;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.INITIATOR_KEYCLOAK_ID_TAG;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.NOTIFICATION_LOG_IDS;
import static com.goodt.drive.notify.application.dto.DtoTagConstants.USERS_KEYCLOAK_IDS_TAG;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PayloadDto {
	@JsonSetter(EVENT_SUBTYPE_TAG)
	private String eventSubtype;
	private Collection<String> channels;
	@JsonSetter(NOTIFICATION_LOG_IDS)
	private Collection<Long> notificationLogIds;
	private Map<String, Object> content;
	@JsonSetter(EMPLOYEE_IDS_TAG)
	private Collection<Long> employeeIds;
	@JsonSetter(EMPLOYEE_COPY_IDS_TAG)
	private Collection<Long> employeeCopyIds;
    @JsonSetter(EMAIL_IDS_TAG)
    private Collection<Long> emailIds;
    @JsonSetter(EMAIL_COPY_IDS_TAG)
    private Collection<Long> emailCopyIds;
	@JsonSetter(INITIATOR_KEYCLOAK_ID_TAG)
	private String initiatorEmployeeId;
	@JsonSetter(USERS_KEYCLOAK_IDS_TAG)
	private Collection<String> usersKeycloakIds;
    @JsonSetter(UNIT_CODE)
    private String unitCode;
}
