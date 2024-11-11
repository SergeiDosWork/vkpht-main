package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Кратка информация о шаблона уведомления, являющимся замещающим шаблоном.")
public class NotificationTemplateContentSubstituteInfo {

	@Schema(description = "Уникальный идентификатор шаблона уведомления.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private Long id;

	@Schema(description = "Название шаблона.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String description;
}
