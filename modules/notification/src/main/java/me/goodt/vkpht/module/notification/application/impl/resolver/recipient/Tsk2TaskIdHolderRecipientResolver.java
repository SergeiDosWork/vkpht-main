package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.EMPLOYEE;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_HOLDER;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_HOLDER_HEAD;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TEAM_DIVISION_ASSIGNMENT;

@Component
@Slf4j
public class Tsk2TaskIdHolderRecipientResolver implements RecipientResolver {
    @Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equalsAny(recipient.getBasicValue(), TASK_ID_HOLDER, TASK_ID_HOLDER_HEAD)) {
            return;
        }
        Long taskId = RecipientResolverUtils.findTaskId(context);
        log.info("Tsk2TaskIdHolderRecipientResolver, task id to find {}", taskId);
        if (Objects.isNull(taskId)) {
            return;
        }
        TaskDto tsk2taskDto = (TaskDto) context.getOrResolveObject(SavedObjectNames.TASK_2, () ->
            context.getResolverServiceContainer().getTasksettingServiceClient().taskFind(new TaskFindRequest().setIds(Collections.singletonList(taskId)))
                .stream().findFirst().orElse(null));
        if (Objects.isNull(tsk2taskDto)) {
            return;
        }
        List<DivisionTeamAssignmentDto> assignments = (List<DivisionTeamAssignmentDto>) context.getOrResolveObject(SavedObjectNames.ASSIGNMENTS, () -> {
            if (tsk2taskDto.getType() == null || tsk2taskDto.getType().getUserType() == null || StringUtils.isEmpty(tsk2taskDto.getType().getUserType().getName())) {
                return Collections.emptyList();
            }
            String type = tsk2taskDto.getType().getUserType().getName();
            if (StringUtils.equals(EMPLOYEE, type)) {
                return context.getResolverServiceContainer().getOrgstructureServiceAdapter().getAssignments(null, Collections.singletonList(tsk2taskDto.getUserId()));
            } else if (StringUtils.equals(TEAM_DIVISION_ASSIGNMENT, type)) {
                return context.getResolverServiceContainer().getOrgstructureServiceAdapter().getAssignments(Collections.singletonList(tsk2taskDto.getUserId()), null);
            } else {
                return Collections.emptyList();
            }

        });
        if (CollectionUtils.isEmpty(assignments)) {
            return;
        }

        findRecipientByAssignment(context, tsk2taskDto, recipient.getRecipient().getName(), recipientList, assignments);
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(TASK_ID_HOLDER, "Работник"),
            new RecipientToken(TASK_ID_HOLDER_HEAD, "Руководитель")
        );
    }

    private void findRecipientByAssignment(ResolverContext context, TaskDto task, String recipient,
                                           Set<RecipientInfoDto> employeeList, List<DivisionTeamAssignmentDto> assignments) {
        if (Objects.isNull(assignments) || assignments.isEmpty()) {
            log.error("division team assignment with id={} is not found", task.getUserId());
            return;
        }
        switch (recipient) {
            case TASK_ID_HOLDER: {
                log.info(LOG_MESSAGE_RECIPIENT, TASK_ID_HOLDER);
                employeeList.add(assignments.getFirst().getEmployee());
                break;
            }
            case TASK_ID_HOLDER_HEAD: {
                log.info(LOG_MESSAGE_RECIPIENT, TASK_ID_HOLDER_HEAD);
                Long employeeId = assignments.getFirst().getEmployee().getId();
                Long divisionTeamId = assignments.getFirst().getDivisionTeam().getId();
                DivisionTeamAssignmentDto headAssignment = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeHead(employeeId, divisionTeamId);
                employeeList.add(headAssignment.getEmployee());
                break;
            }
            default:
        }
    }
}
