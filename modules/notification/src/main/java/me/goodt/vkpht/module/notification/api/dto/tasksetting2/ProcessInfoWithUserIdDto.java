package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
public class ProcessInfoWithUserIdDto {

    @JsonSetter(DtoTagConstants.USER_ID_TAG)
    private Long userId;

    private ProcessDto process;
}
