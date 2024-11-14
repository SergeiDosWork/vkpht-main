package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.FIELD_HISTORY_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_HISTORY_TAG;

@Getter
@Setter
public class TaskHistoryResultDto {
    @JsonSetter(TASK_HISTORY_TAG)
    private List<BulkTaskHistoryDto> taskHistory;
    @JsonSetter(FIELD_HISTORY_TAG)
    private Map<Long, List<TaskFieldDto>> fieldHistory;
}
