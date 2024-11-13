package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.notification.api.dto.tasksetting2.StatusDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NAME;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_STATUSCHANGE_STATUS_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_STATUSCHANGE_STATUS_INFO_NAME;

@Component
@Slf4j
public class TaskStatuschangeStatusInfoGroupResolver implements TokenGroupResolver {
    @Override
    public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
        log.info(LOG_MESSAGE_GROUP, TASK_STATUSCHANGE_STATUS_INFO);
        Integer statusId = (Integer) context.getParameters().get(TASK_STATUSCHANGE_STATUS_INFO);
        log.info("status_id = {}", statusId);
        if (Objects.isNull(statusId)) {
            return;
        }
        try {
            for (TokenWithValues token : context.getParsedTokens().get(TASK_STATUSCHANGE_STATUS_INFO)) {
                if (token.getBasicValue().equals(NAME)) {
                    log.info(LOG_MESSAGE_TOKEN, TASK_STATUSCHANGE_STATUS_INFO, NAME);
                    StatusDto status = context.getResolverServiceContainer().getTasksettingServiceClient().getStatusById(statusId.longValue());
                    resolvedTokenValues.put(TASK_STATUSCHANGE_STATUS_INFO_NAME, status.getName());
                }
            }
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<NotificationToken> getResolvedTokens() {
        return List.of(
            new NotificationToken(TASK_STATUSCHANGE_STATUS_INFO, NAME, "Наименование статуса в таблице tasksetting.status")
        );
    }
}
