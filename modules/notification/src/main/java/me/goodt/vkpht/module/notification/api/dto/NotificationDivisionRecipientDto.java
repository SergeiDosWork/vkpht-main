package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Информация о подразделении, являющееся получателем уведомлений.")
public class NotificationDivisionRecipientDto {

	@Schema(description = "Системный идентификатор записи о получателе, в рамках которой сохранены данные подразделения.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private Long recipientId;

	@Schema(description = "Идентификатор подразделения в оргструктуре.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private Long divisionId;

	@Schema(description = "Аббревиатура подразделения.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String abbreviation;

	@Schema(description = "Полное наименование подразделения.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String fullName;

	@Schema(description = "Сокращенное наименование подразделения.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String shortName;
}
