package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFindRequest;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

@UtilityClass
public class RecipientResolverHelper {

	public static TaskDto getTaskDtoByTaskLinkId(ResolverContext context, String savedObjectName) {
		return (TaskDto) context.getOrResolveObject(savedObjectName, () -> Optional.ofNullable(RecipientResolverUtils.findTaskLinkId(context))
			.map(taskLinkId -> context.getResolverServiceContainer().getTasksettingServiceClient()
				.getTaskLinks(taskLinkId.longValue(), null, null, null))
			.filter(taskLinkResult -> !CollectionUtils.isEmpty(taskLinkResult))
			.map(taskLinkResult -> {
				Long taskId = SavedObjectNames.TASK_TO.equals(savedObjectName) ? taskLinkResult.get(0).getTaskIdTo() : taskLinkResult.get(0).getTaskIdFrom();
				return context.getResolverServiceContainer().getTasksettingServiceClient().taskFind(new TaskFindRequest().setIds(Collections.singletonList(taskId)));
			})
			.filter(taskResult -> !CollectionUtils.isEmpty(taskResult))
			.map(taskResult -> taskResult.get(0))
			.orElse(null));
	}

	public static List<DivisionTeamAssignmentDto> getAssignmentsByUserId(ResolverContext context, Long userId) {
		return context.getResolverServiceContainer().getOrgstructureServiceAdapter().getAssignments(Collections.singletonList(userId), null);
	}

	public static DivisionTeamAssignmentDto getHeadByAssignments(ResolverContext context, List<DivisionTeamAssignmentDto> assignmentResult) {
		DivisionTeamAssignmentDto headAssignment = null;
		if (!CollectionUtils.isEmpty(assignmentResult)) {
			DivisionTeamAssignmentDto assignment = assignmentResult.getFirst();
			headAssignment = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeHead(assignment.getEmployee().getId(), assignment.getDivisionTeam().getId());
		}
		return headAssignment;
	}
}
