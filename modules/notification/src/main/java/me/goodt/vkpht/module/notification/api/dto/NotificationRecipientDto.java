package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о получателе оповещений.")
public class NotificationRecipientDto {

	@Schema(description = "Уникальный идентификатор записи получателя.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long id;

	@Schema(description = "Наименование получателя оповещений.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String name;

	@Schema(description = "Текстовое описание получателя оповещений.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private String description;

	@Schema(description = "Список значений параметров для указанного получателя.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private List<Long> parameters;

    @Schema(description = "Список email для получателя STATIC_EMAIL",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> emails;
}
