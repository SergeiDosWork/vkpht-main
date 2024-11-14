package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import me.goodt.vkpht.module.notification.api.dto.tasksetting2.StatusDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NAME;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_STATUS_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_STATUS_INFO_NAME;

@Component
@Slf4j
public class TaskIdToStatusInfoGroupResolver implements TokenGroupResolver, TaskIdBasedGroupResolver {
    @Override
    public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
        log.info(LOG_MESSAGE_GROUP, TASK_ID_TO_STATUS_INFO);
        Integer taskId = (Integer) context.getParameters().get(TASK_ID_TO_STATUS_INFO);
        log.info(TASK_ID_IS, taskId);
        Optional<TaskDto> taskDto = getTaskDto(taskId, context);
        if (taskDto.isEmpty()) {
            return;
        }

        try {
            StatusDto status = taskDto.get().getStatus();
            for (TokenWithValues token : context.getParsedTokens().get(TASK_ID_TO_STATUS_INFO)) {
                if (token.getBasicValue().equals(NAME)) {
                    log.info(LOG_MESSAGE_TOKEN, TASK_ID_TO_STATUS_INFO, NAME);
                    resolvedTokenValues.put(TASK_ID_TO_STATUS_INFO_NAME, status.getName());
                }
            }
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<NotificationToken> getResolvedTokens() {
        return List.of(
            new NotificationToken(TASK_ID_TO_STATUS_INFO, NAME, "Наименование статуса в таблице tasksetting.status")
        );
    }
}
