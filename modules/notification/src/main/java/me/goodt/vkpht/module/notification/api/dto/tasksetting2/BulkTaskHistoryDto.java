package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_HISTORY_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_ID_TAG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkTaskHistoryDto {
    @JsonSetter(TASK_ID_TAG)
    private Long taskId;
    @JsonSetter(TASK_HISTORY_TAG)
    private List<TaskHistoryDto> taskHistory;
}
