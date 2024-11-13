package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFindRequest;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_AUTHOR_EMPLOYEE_ID;

@Component
@Slf4j
public class TaskAuthorEmployeeIdRecipientResolver implements RecipientResolver {

    @Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {

        if (!StringUtils.equals(TASK_AUTHOR_EMPLOYEE_ID, recipient.getBasicValue())) {
            return;
        }

        log.info(LOG_MESSAGE_RECIPIENT, TASK_AUTHOR_EMPLOYEE_ID);

        Long taskId = RecipientResolverUtils.findTaskId(context);
        if (Objects.isNull(taskId)) {
            return;
        }

        TaskDto task = context.getResolverServiceContainer()
            .getTasksettingServiceClient().taskFind(new TaskFindRequest().setIds(Collections.singletonList(taskId)))
            .stream()
            .findFirst()
            .orElse(null);

        if (Objects.isNull(task) || Objects.isNull(task.getAuthorEmployeeId())) {
            return;
        }

        EmployeeInfoDto employee = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeInfo(task.getAuthorEmployeeId());
        if (employee != null) {
            recipientList.add(employee);
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(TASK_AUTHOR_EMPLOYEE_ID, "Автор созданного таска")
        );
    }
}
