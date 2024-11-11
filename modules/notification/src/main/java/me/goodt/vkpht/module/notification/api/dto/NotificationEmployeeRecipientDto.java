package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Информация о сотруднике, являющимся получателем уведомлений")
public class NotificationEmployeeRecipientDto {

	@Schema(description = "Системный идентификатор записи о получателе, в рамках которой сохранены данные сотрудника.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private Long recipientId;

	@Schema(description = "Идентификатор сотрудника в оргструктуре.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private Long employeeId;

	@Schema(description = "Имя сотрудника.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String firstName;

	@Schema(description = "Фамилия сотрудника.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String lastName;

	@Schema(description = "Отчество сотрудника.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private String middleName;
}
