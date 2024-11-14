package me.goodt.vkpht.module.orgstructure.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Объект запроса для получения информации по физическому лицу")
public class PersonInfoRequest {

    @Schema(description = "Идентификатор сотрудника.\n\n" +
        "Условно обязательное (необходимо заполнение employeeId или snils).\n\n" +
        "Данное значение игнорируется, в случае заполнения snils",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long employeeId;

    @Schema(description = "Признак, который сигнализирует о необходимости возврата " +
        "дополнительной информацию о физ.лице",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "false",
        type = "boolean")
    private Boolean includeSecondaryInfo = false;

    @Schema(description = "СНИЛС физ. лица.\n\n" +
        "Условно обязательное (необходимо заполнение employeeId или snils).",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String snils;

}
