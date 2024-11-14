package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFindRequest;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.*;

@Component
@Slf4j
public class TaskIdHolderIfNotTaskAuthorRecipientResolver implements RecipientResolver {
    @Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(TASK_ID_HOLDER_IF_NOT_TASK_AUTHOR, recipient.getBasicValue())) {
            return;
        }

        log.info(LOG_MESSAGE_RECIPIENT, TASK_ID_HOLDER_IF_NOT_TASK_AUTHOR);

        Long taskId = RecipientResolverUtils.findTaskId(context);
        if (Objects.isNull(taskId)) {
            return;
        }

        TaskDto taskDto = (TaskDto) context.getOrResolveObject(SavedObjectNames.TASK, () ->
            context.getResolverServiceContainer().getTasksettingServiceClient().taskFind(new TaskFindRequest().setIds(Collections.singletonList(taskId)))
                .stream().findFirst().orElse(null));
        if (Objects.isNull(taskDto)) {
            return;
        }

        List<DivisionTeamAssignmentDto> assignments = (List<DivisionTeamAssignmentDto>) context.getOrResolveObject(SavedObjectNames.ASSIGNMENTS, () ->
            context.getResolverServiceContainer().getOrgstructureServiceAdapter().getAssignments(Collections.singletonList(taskDto.getUserId()), null));
        if (CollectionUtils.isEmpty(assignments)) {
            return;
        }

        Long employeeId = assignments.getFirst().getEmployee().getId();
        if (!employeeId.equals(taskDto.getAuthorEmployeeId())) {
            recipientList.add(assignments.getFirst().getEmployee());
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(TASK_ID_HOLDER_IF_NOT_TASK_AUTHOR, "Владелец таска, в случае если таск создал другой пользователь")
        );
    }
}
