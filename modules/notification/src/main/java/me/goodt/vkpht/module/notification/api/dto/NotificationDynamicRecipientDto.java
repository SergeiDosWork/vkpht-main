package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Информация о токене вычисляемого получателя уведомлений")
public class NotificationDynamicRecipientDto {

    @Schema(description = "Уникальный идентификатор записи получателя, к которой относится токен.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private Long recipientId;

    @Schema(description = "Наименование токена, идентифицирующего вычисляемого получателя.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @Schema(description = "Текстовое описание вычисляемого получателя.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
}
