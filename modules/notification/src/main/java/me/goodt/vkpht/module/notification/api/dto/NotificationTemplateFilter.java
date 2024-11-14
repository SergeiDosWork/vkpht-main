package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@Builder
@Schema(description = "Параметры фильтрации при получении списка уведомлений о событиях")
@ParameterObject
public class NotificationTemplateFilter {

    @Schema(description = "Уникальный идентификатор уведомления о событии",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        type = "integer")
    private Long id;

    @Schema(description = "Код события.\n\nПоиск на частичное совпадении с указанным значением.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;

    @Schema(description = "Название события.\n\nПоиск на частичное совпадении с указанным значением.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(description = "Описание события.\n\nПоиск на частичное совпадении с указанным значением.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    private String unitCode;

    @Builder.Default
    private Boolean actual = true;
}
