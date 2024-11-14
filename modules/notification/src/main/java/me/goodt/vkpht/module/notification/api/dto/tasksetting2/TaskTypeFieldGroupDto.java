package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskTypeFieldGroupDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Long index;
    @JsonSetter(DtoTagConstants.IS_SYSTEM_TAG)
    private boolean isSystem;
    @JsonSetter(DtoTagConstants.IS_EDITABLE_IF_SYSTEM_TAG)
    private boolean isEditableIfSystem;
}
