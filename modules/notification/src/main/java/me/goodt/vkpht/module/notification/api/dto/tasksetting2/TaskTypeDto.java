package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
public class TaskTypeDto {
	private Long id;
	private String name;
    @JsonSetter(DtoTagConstants.ACCESS_TYPE_TAG)
	private AccessTypeDto accessType;
	private TaskTypeDto parent;
    @JsonSetter(DtoTagConstants.USER_TYPE_TAG)
    private UserTypeDto userType;
	private String description;
}
