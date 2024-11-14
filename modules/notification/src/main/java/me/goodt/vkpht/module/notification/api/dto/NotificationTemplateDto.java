package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Событие")
public class NotificationTemplateDto {

    @Schema(description = "Синтетический id", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Дата создания записи", accessMode = Schema.AccessMode.READ_ONLY)
    private Date dateFrom;

    @Schema(description = "Дата окончания записи", accessMode = Schema.AccessMode.READ_ONLY)
    private Date dateTo;

    @Schema(description = "ID пользователя, создавшего запись", accessMode = Schema.AccessMode.READ_ONLY)
    private Long authorEmployeeId;

    @Schema(description = "ID пользователя, обновившего запись", accessMode = Schema.AccessMode.READ_ONLY)
    private Long authorUpdateEmployeeId;

    @Schema(description = "Дата обновления", accessMode = Schema.AccessMode.READ_ONLY)
    private Date dateUpdate;

    @Schema(description = "Название события", accessMode = Schema.AccessMode.READ_WRITE)
    private String name;

    @Schema(description = "Код события", accessMode = Schema.AccessMode.READ_WRITE)
    private String code;

    @Schema(description = "Описание события", accessMode = Schema.AccessMode.READ_WRITE)
    private String description;

    @Schema(description = "Состояние активности", accessMode = Schema.AccessMode.READ_WRITE)
    private Integer isEnabled;

}
