
package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TaskFindRequest {
    private Collection<Long> ids;
    private Collection<Integer> users;
    @JsonSetter(TASK_TYPE_TAG)
    private Collection<Long> taskType;
    @JsonSetter(PROCESS_TYPE_TAG)
    private Collection<String> processType;
    @JsonSetter(PARENT_ID_TAG)
    private Collection<Long> parentIds;
    @JsonSetter(TASK_FIELD_TYPE_ID_TAG)
    private Long taskTypeFieldId;
    @JsonSetter(TASK_FIELD_TYPE_PARAMS_TAG)
    private String taskTypeFieldParams;
    @JsonSetter(TASK_FIELD_VALUE_TAG)
    private String taskFieldValue;
    @JsonSetter(PROCESS_ID_TAG)
    private Collection<Long> processIds;
    @JsonSetter(ROOT_ID_TAG)
    private Long rootId;
    @JsonSetter(STATUS_ID_TAG)
    private Collection<Long> statusIds;

    @JsonIgnore
    public boolean isEmpty() {
        return (CollectionUtils.isEmpty(ids)) &&
            (CollectionUtils.isEmpty(users)) &&
            (CollectionUtils.isEmpty(taskType)) &&
            (CollectionUtils.isEmpty(processType)) &&
            (CollectionUtils.isEmpty(parentIds)) &&
            (taskTypeFieldId == null) &&
            (StringUtils.isEmpty(taskTypeFieldParams)) &&
            (StringUtils.isEmpty(taskFieldValue)) &&
            CollectionUtils.isEmpty(processIds) &&
            (rootId == null) &&
            CollectionUtils.isEmpty(statusIds);
    }
}
