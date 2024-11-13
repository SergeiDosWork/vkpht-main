package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
public class StageTypeDto {
    private Long id;
    private String name;
    private String code;
    @JsonProperty(DtoTagConstants.TASK_TYPE_TAG)
    private TaskTypeDto taskType;
}
