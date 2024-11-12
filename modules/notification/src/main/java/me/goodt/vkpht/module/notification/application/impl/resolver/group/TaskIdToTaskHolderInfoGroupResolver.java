package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static com.goodt.drive.notify.application.utils.DataUtils.getEmployeeFullName;
import static com.goodt.drive.notify.application.utils.DataUtils.getEmployeeShortName;
import static com.goodt.drive.notify.application.utils.DataUtils.getEmployeeSurnameAndName;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.ASSIGNMENT_ID;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FI;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FIO_SHORT;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_INFO_ASSIGNMENT_ID;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_INFO_FI;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_INFO_FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_INFO_FIO_SHORT;

@Component
@Slf4j
public class TaskIdToTaskHolderInfoGroupResolver implements TokenGroupResolver, TaskIdBasedGroupResolver {

	private static final String DIVISION_TEAM_ASSIGNMENT_IS_NOT_FOUND = "division team assignment with id={} is not found";
	private static final String DIVISION_TEAM_ASSIGNMENT_IS_FOUND = "division team assignment with id={} is found";

	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.debug(LOG_MESSAGE_GROUP, TASK_ID_TO_TASK_HOLDER_INFO);
		Integer taskId = (Integer) context.getParameters().get(TASK_ID_TO_TASK_HOLDER_INFO);
		Optional<TaskDto> taskDto = getTaskDto(taskId, context);
		if (taskDto.isEmpty()) {
			return;
		}

		List<DivisionTeamAssignmentDto> assignments = (List<DivisionTeamAssignmentDto>) context.getOrResolveObject(SavedObjectNames.ASSIGNMENTS,
																												   () -> context.getResolverServiceContainer().getOrgstructureServiceClient().getAssignments(Collections.singletonList(taskDto.get().getUserId()), null));

		if (CollectionUtils.isEmpty(assignments)) {
			log.debug(DIVISION_TEAM_ASSIGNMENT_IS_NOT_FOUND, taskDto.get().getUserId());
			return;
		}

		log.info(DIVISION_TEAM_ASSIGNMENT_IS_FOUND, taskDto.get().getUserId());
		List<TokenWithValues> tokens = context.getParsedTokens().get(TASK_ID_TO_TASK_HOLDER_INFO);
		tokens.forEach(token -> {
			if (token.getBasicValue().equals(FIO_FULL)) {
				log.debug(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_INFO, FIO_FULL);
				resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_INFO_FIO_FULL, getEmployeeFullName(assignments.getFirst().getEmployee()));
			}
			if (token.getBasicValue().equals(FIO_SHORT)) {
				log.debug(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_INFO, FIO_SHORT);
				resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_INFO_FIO_SHORT, getEmployeeShortName(assignments.getFirst().getEmployee()));
			}
			if (token.getBasicValue().equals(FI)) {
				log.debug(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_INFO, FI);
				resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_INFO_FI, getEmployeeSurnameAndName(assignments.getFirst().getEmployee()));
			}
			if (token.getBasicValue().equals(ASSIGNMENT_ID)) {
				log.debug(LOG_MESSAGE_TOKEN, TASK_ID_TO_TASK_HOLDER_INFO, ASSIGNMENT_ID);
				resolvedTokenValues.put(TASK_ID_TO_TASK_HOLDER_INFO_ASSIGNMENT_ID, String.valueOf(assignments.getFirst().getId()));
			}
		});
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return Arrays.asList(
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_INFO, FIO_FULL, "ФИО владельца БО в формате Фамилия Имя Отчество"),
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_INFO, FIO_SHORT, "ФИО владельца БО в формате Фамилия И.О."),
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_INFO, FI, "ФИО владельца БО в формате Фамилия Имя"),
			new NotificationToken(TASK_ID_TO_TASK_HOLDER_INFO, ASSIGNMENT_ID, "ID назначения владельца БО")
		);
	}
}
