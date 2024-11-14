package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_ID_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_ID_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_LINK_TYPE_ID_TAG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskLinkDto {

    private Long id;

    @JsonSetter(TASK_ID_FROM_TAG)
    private Long taskIdFrom;

    @JsonSetter(TASK_LINK_TYPE_ID_TAG)
    private Long taskLinkTypeId;

    @JsonSetter(TASK_ID_TO_TAG)
    private Long taskIdTo;

    @JsonSetter(DATE_FROM_TAG)
    private Date dateFrom;

    @JsonSetter(DATE_TO_TAG)
    private Date dateTo;
}
