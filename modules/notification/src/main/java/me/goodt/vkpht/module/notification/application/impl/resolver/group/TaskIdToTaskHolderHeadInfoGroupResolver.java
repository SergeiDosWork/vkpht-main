package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getEmployeeFullName;
import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getEmployeeShortName;
import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getEmployeeSurnameAndName;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.ASSIGNMENT_ID;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FI;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FIO_SHORT;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_HEAD_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_HEAD_INFO_ASSIGNMENT_ID;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_HEAD_INFO_FI;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_HEAD_INFO_FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_HEAD_INFO_FIO_SHORT;

@Component
@Slf4j
public class TaskIdToTaskHolderHeadInfoGroupResolver implements TokenGroupResolver, TaskIdBasedGroupResolver {
	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, TASK_ID_TO_TASK_HOLDER_HEAD_INFO);
		Integer taskId = (Integer) context.getParameters().get(TASK_ID_TO_TASK_HOLDER_HEAD_INFO);
		log.info(TASK_ID_IS, taskId);
		Optional<TaskDto> taskDto = getTaskDto(taskId, context);
		if (taskDto.isEmpty()) {
			return;
		}

		List<DivisionTeamAssignmentDto> assignment = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getAssignments(Collections.singletonList(taskDto.get().getUserId()), null);
		if (assignment != null && !assignment.isEmpty()) {
			DivisionTeamAssignmentDto head = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeHead(assignment.getFirst().getEmployee().getId(), assignment.getFirst().getDivisionTeam().getId());
			if (head != null) {
				for (TokenWithValues token : context.getParsedTokens().get(TASK_ID_TO_TASK_HOLDER_HEAD_INFO)) {
					if (token.getBasicValue().equals(FIO_FULL)) {
						log.info(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_HEAD_INFO, FIO_FULL);
						resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_HEAD_INFO_FIO_FULL, getEmployeeFullName(head.getEmployee()));
					}
					if (token.getBasicValue().equals(FIO_SHORT)) {
						log.info(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_HEAD_INFO, FIO_SHORT);
						resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_HEAD_INFO_FIO_SHORT, getEmployeeShortName(head.getEmployee()));
					}
					if (token.getBasicValue().equals(FI)) {
						log.info(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_HEAD_INFO, FI);
						resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_HEAD_INFO_FI, getEmployeeSurnameAndName(head.getEmployee()));
					}
					if (token.getBasicValue().equals(ASSIGNMENT_ID)) {
						log.info(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_HEAD_INFO, ASSIGNMENT_ID);
						resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_HEAD_INFO_ASSIGNMENT_ID, String.valueOf(assignment.getFirst().getId()));
					}
				}
			}
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return Arrays.asList(
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_HEAD_INFO, FIO_FULL, "ФИО руководителя владельца БО в формате Фамилия Имя Отчество"),
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_HEAD_INFO, FIO_SHORT, "ФИО руководителя владельца БО в формате Фамилия И.О."),
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_HEAD_INFO, FI, "ФИО руководителя владельца БО в формате Фамилия Имя"),
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_HEAD_INFO, ASSIGNMENT_ID, "ID назначения руководителя владельца БО")
		);
	}
}
