package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskHistoryDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskHistoryResultDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HISTORY_LAST_AUTHOR;
import static java.lang.Boolean.FALSE;

@Component
@Slf4j
public class TaskIdToTaskHistoryLastAuthorRecipientResolver implements RecipientResolver {
	@Override
	public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(TASK_ID_TO_TASK_HISTORY_LAST_AUTHOR, recipient.getBasicValue())) {
            return;
        }
        log.info(LOG_MESSAGE_RECIPIENT, TASK_ID_TO_TASK_HISTORY_LAST_AUTHOR);
        TaskHistoryResultDto taskHistoryResult = (TaskHistoryResultDto) context.getOrResolveObject(SavedObjectNames.TASK_HISTORY_RESULT, () ->
            Optional.ofNullable(RecipientResolverUtils.findTaskId(context))
                .map(taskId -> context.getResolverServiceContainer().getTasksettingServiceClient()
                    .taskHistory(Collections.singletonList(taskId), FALSE))
                .orElse(null)
        );

        if (taskHistoryResult != null) {
            taskHistoryResult.getTaskHistory().get(0).getTaskHistory()
                .stream()
                .filter(th -> th.getDate() != null)
                .max(Comparator.comparing(TaskHistoryDto::getDate))
                .ifPresent(th -> {
                    EmployeeInfoDto employee = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeInfo(th.getEmployeeId());
                    if (employee != null) {
                        recipientList.add(employee);
                    }
                });
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(TASK_ID_TO_TASK_HISTORY_LAST_AUTHOR, "Работник, совершивший последнее изменение статуса таска")
        );
    }
}
