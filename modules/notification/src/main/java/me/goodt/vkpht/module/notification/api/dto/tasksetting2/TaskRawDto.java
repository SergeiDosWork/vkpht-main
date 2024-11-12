package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
public class TaskRawDto {
	private Long id;
    @JsonSetter(DtoTagConstants.PARENT_ID_TAG)
	private Long parentId;
    @JsonSetter(DtoTagConstants.TYPE_ID_TAG)
    private Long typeId;
    @JsonSetter(DtoTagConstants.USER_ID_TAG)
    private Integer userId;
}
