package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    private Long id;
    private String name;
    private String description;
    @JsonSetter(DtoTagConstants.STATUS_NAMES_TAG)
    private List<StatusNameDto> statusNames;
    @JsonSetter(DtoTagConstants.CANBAN_LEVEL_TAG)
    private CanbanLevelDto canbanLevel;
    private Long index;
    @JsonSetter(DtoTagConstants.IS_SYSTEM_TAG)
    private boolean isSystem;
    @JsonSetter(DtoTagConstants.IS_EDITABLE_IF_SYSTEM_TAG)
    private boolean isEditableIfSystem;
}
